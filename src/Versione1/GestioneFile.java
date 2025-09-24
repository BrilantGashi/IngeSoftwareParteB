package Versione1;
import java.io.*;
import it.unibs.fp.mylib.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.time.LocalTime;


public class GestioneFile {
	
	 
	File file = new File("credenziali.txt");
	File fileXML = new File("CorpoDati.xml");
	
	private String role1 = null;
	private String role2 = null;
	private String role3 = null;
	private String role4 = null;
	private ArrayList<String> rolev;

	/**
	 * Costruttore vuoto
	 */
	public GestioneFile() {
	}
	
	
	//*************************************************************************************************
	//-----------------------------------GESTIONE FILE TXT--------------------------------------------
	//*************************************************************************************************
	
	
	/**
	 * Cerca una riga nel file delle credenziali in base allo username indicato, per verificare l'esistenza
	 * e di conseguenza la correttezza dello username
	 *
	 * Prerequisito: esiste un File file;   inizializzato
	 *
	 * @param username  username da cercare (colonna 2)
	 * @return array di stringhe con i campi della riga trovata;
	 *         null se lo username non � stato trovato
	 * @throws IOException            problemi di I/O 
	 * @throws FileNotFoundException  se il file non esiste
	 */

	public String[] leggiSingolaCredenziale(String username) throws FileNotFoundException, IOException {
		String[] riga;
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	riga = line.split(";");
		       
		    	//1 perch� � il secondo paramatro della riga -- CONFIGURATORE;utente1;default
		       if (riga[1].equalsIgnoreCase(username)) {
		    	   return riga;		    	   
		       }
		    }
		}
		
		return null;
	}
	
	/**
	 * Cambia la password dell�utente indicato senza alterare altre righe o campi.
	 *
	 * Prerequisito: esiste un campo   private final File file;   inizializzato
	 *
	 * @param username   username da cercare (colonna 2)
	 * @param nuovaPass  nuova password (in chiaro o gi� hashata) da salvare
	 * @return true  se la password � stata aggiornata;
	 *         false se lo username non � stato trovato
	 * @throws IOException            problemi di I/O (permessi, disco pieno�)
	 * @throws FileNotFoundException  se il file non esiste
	 */
	public boolean aggiornaPasswordSuFile(String username, String nuovaPass)
	        throws FileNotFoundException, IOException {

	    // *** usa il campo "file" gi� presente ***
	    File tmp = new File(file.getAbsolutePath() + ".tmp");
	    boolean trovato = false;

	    try (BufferedReader br = new BufferedReader(new FileReader(file));
	         BufferedWriter bw = new BufferedWriter(new FileWriter(tmp, false))) {

	        String line;
	        while ((line = br.readLine()) != null) {
	           
	            String[] campi = line.split(";", 3);          // tipo ; username ; password

	            if (campi[1].equalsIgnoreCase(username)) {
	                campi[2] = nuovaPass;                     // cambia SOLO la password
	                trovato = true;
	            }

	            bw.write(String.join(";", campi));            // riscrive la riga
	            bw.newLine();
	        }
	    }

	    if (trovato) {
	        // sostituisce l�originale con il temporaneo
	        if (!file.delete() || !tmp.renameTo(file)) {
	            throw new IOException("Impossibile sostituire il file credenziali");
	        }
	    } else {
	        tmp.delete();   // nessuna modifica: rimuove il temporaneo
	    }

	    return trovato;
	}

	
	public ArrayList restituisciListaUtenza (String utenza) throws FileNotFoundException, IOException {
		String[] riga;
		ArrayList<String> lista = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	riga = line.split(";");
		       
		    	//1 perch� � il secondo paramatro della riga -- CONFIGURATORE;utente1;default
		       if (riga[0].equalsIgnoreCase(utenza)) {
		    	   lista.add(riga[1]);
		       }
		    }
		}
		
		return lista;			
	}
	
	
    public void leggiUtentiDaFile(List<Configuratore> configuratori,
	                              List<Volontario> volontari
	                              /*List<Fruitore> fruitori*/) throws IOException {

	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	            String line;

	            while ((line = br.readLine()) != null) {
	                String[] campi = line.split(";");
	                if (campi.length < 3) continue;

	                String ruolo = campi[0].trim().toUpperCase();
	                String username = campi[1].trim();
	                String password = campi[2].trim();

	                Credenziali cred = new Credenziali(username);
	                
	                switch (ruolo) {
	                    case "CONFIGURATORE":
	                        configuratori.add(new Configuratore(cred));
	                        break;
	                    case "VOLONTARIO":
	                        volontari.add(new Volontario(cred));
	                        break;
	                    case "FRUITORE":
	                        //fruitori.add(new Fruitore(username, password));
	                        break;
	                    default:
	                        System.out.println("Ruolo sconosciuto: " + ruolo);
	                }
	            }
	        }
	    }
    
    

	//*************************************************************************************************
	//-----------------------------------GESTIONE FILE XML--------------------------------------------
	//*************************************************************************************************
	
    //---------------------------------- SCRITTURA -----------------------------------------------------
    
    public void scriviCorpoDati(int nMaxPersone, AmbitoTerritoriale ambito,
                                       ListaLuogo listaLuoghi) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try {
	        	
	        DocumentBuilder db = dbf.newDocumentBuilder();       
	        Document doc = db.newDocument();
	
	        // Radice
	        Element root = doc.createElement("DatiSistema");
	        doc.appendChild(root);
	        
	        /* SOPPRESSO PERCH� � un dato del sistema, non dell'applicazione in s�
	        //N Max per fruitore
	        Element nMaxFruitore = doc.createElement("NMaxFruitore");
	        doc.appendChild(nMaxFruitore);
	        nMaxFruitore.setAttribute("num", String.valueOf(nMaxPersone));
	         */
	        
	        // Ambito Territoriale
	        Element ambitoElem = doc.createElement("AmbitoTerritoriale");
	        root.appendChild(ambitoElem);
	        ambitoElem.setAttribute("provincia", ambito.getProvincia());
	        //ambitoElem.appendChild(ambitoElem);
	        root.appendChild(ambitoElem);
	        // CAP dell'ambito
            Element capsElem = doc.createElement("CAPs");
            for (Integer c : ambito.getCap()) {
               appendChildWithText(doc, capsElem, "CAP", c.toString());
            }
            ambitoElem.appendChild(capsElem);
	        
	        //Luoghi
	        Element luoghiElem = doc.createElement("Luoghi");
	        root.appendChild(luoghiElem);
	        
	        for (int i = 0; i < listaLuoghi.getListaLuoghi().size(); i++ ) {
	        	Element luogoElem = doc.createElement("Luogo");
	            luogoElem.setAttribute("nome", listaLuoghi.getListaLuoghi().get(i).getNomeLuogo());
	            luoghiElem.appendChild(luogoElem);
	            
	            Element descrizioneElem = doc.createElement("Descrizione");
	            descrizioneElem.setTextContent(listaLuoghi.getListaLuoghi().get(i).getDescrizione());
	            luogoElem.appendChild(descrizioneElem);
	            // Tipi di visita
	            Element tipiVisitaElem = doc.createElement("TipiVisita");
	            luogoElem.appendChild(tipiVisitaElem);
	            
	            for (int j = 0; j < listaLuoghi.getListaLuoghi().get(i).getListaTipoVisita().getTipi().size(); j++){
	            		            	
	            	tipiVisitaElem.appendChild(serializeTipoVisita(doc, listaLuoghi.getListaLuoghi().get(i).getListaTipoVisita().getTipo(j)));
	            	            
	            }
	        	
	        }
	        
	        try {
	        // Scrittura su file (sovrascrive se esiste)
		        TransformerFactory tf = TransformerFactory.newInstance();
		        Transformer t = tf.newTransformer();
		        t.setOutputProperty(OutputKeys.INDENT, "yes");
		        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
		        t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("CorpoDati.xml")));
		        System.out.println("XML scritto in " + fileXML);
		        
		        } catch (TransformerException te) {
		            System.out.println(te.getMessage());
		        } catch (IOException ioe) {
		            System.out.println(ioe.getMessage());
		        } 
	    } catch (ParserConfigurationException pce) {
	        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
	    }
    } 
    
    private static Element serializeTipoVisita(Document doc, TipoVisita tv) {
        Element tipoVisitaElem = doc.createElement("TipoVisita");

       appendChildWithText(doc, tipoVisitaElem, "Titolo", nvl(tv.getTitolo()));
        appendChildWithText(doc, tipoVisitaElem, "Descrizione", nvl(tv.getDescrizione()));
        //appendChildWithText(doc, tipoVisitaElem, "PuntoDiIncontro", nvl(tv.getPuntoDiIncontro()));
        tipoVisitaElem.appendChild(serializeIndirizzo(doc, tv.getPuntoDiIncontro()));

        //appendChildWithText(doc, tipoVisitaElem, "DataVisita", nvl(tv.getDataVisita()));
        tipoVisitaElem.appendChild(serializeData(doc, tv.getDataVisita(), "DataVisita"));

        //appendChildWithText(doc, tipoVisitaElem, "OraInizio", nvl(tv.getOraInizio()));
        tipoVisitaElem.appendChild(serializeOra(doc, tv.getOraInizio(), "OraInizio"));

        appendChildWithText(doc, tipoVisitaElem, "DurataVisita", String.valueOf(tv.getDurataVisita()));
        appendChildWithText(doc, tipoVisitaElem, "Biglietto", String.valueOf(tv.isBiglietto()));
        appendChildWithText(doc, tipoVisitaElem, "SogliaMinPartecipanti", String.valueOf(tv.getSogliaMinPartecipanti()));
        appendChildWithText(doc, tipoVisitaElem, "SogliaMaxPartecipanti", String.valueOf(tv.getSogliaMaxPartecipanti()));
        //appendChildWithText(doc, tipoVisitaElem, "PeriodoProgrammabile", nvl(tv.getPeriodoProgrammabile()));
        tipoVisitaElem.appendChild(serializePeriodo(doc, tv.getPeriodoProgrammabile()));


        // Volontari
        Element volontariElem = doc.createElement("Volontari");
        if (tv.getVolontari() != null) {
        	for (int i = 0; i < tv.getVolontari().size(); i++) {
        		appendChildWithText(doc, volontariElem, "Volontario", tv.getVolontari().get(i).getNickname());
        	}
            /*for (String v : tv.getVolontari()) {
                appendChildWithText(doc, volontariElem, "Volontario", v);
            }*/
        }
        tipoVisitaElem.appendChild(volontariElem);
        
        return tipoVisitaElem;
    }

    private static void appendChildWithText(Document doc, Element parent, String tag, String text) {
        Element e = doc.createElement(tag);
        e.appendChild(doc.createTextNode(text != null ? text : ""));
        parent.appendChild(e);
    }

    private static String nvl(String s) { return s == null ? "" : s; }

    private static Element serializeIndirizzo(Document doc, Indirizzo indirizzo) {
        Element indirizzoElem = doc.createElement("Indirizzo");

        if (indirizzo != null) {
            // TipoStrada (enum -> usiamo toString)
            if (indirizzo.getTipoStrada() != null) {
                appendChildWithText(doc, indirizzoElem, "TipoStrada", indirizzo.getTipoStrada().toString());
            }

            // NomeStrada
            appendChildWithText(doc, indirizzoElem, "NomeStrada", nvl(indirizzo.getNomeStrada()));

            // NumeroCivico
            appendChildWithText(doc, indirizzoElem, "NumeroCivico", nvl(indirizzo.getNumeroCivico()));

            // CAP
            appendChildWithText(doc, indirizzoElem, "CAP", nvl(indirizzo.getCap().toString()));

            // Comune
            appendChildWithText(doc, indirizzoElem, "Comune", nvl(indirizzo.getComune()));

            // Provincia
            appendChildWithText(doc, indirizzoElem, "Provincia", nvl(indirizzo.getProvincia()));
        }

        return indirizzoElem;
    }

    private static Element serializeData(Document doc, Data data, String tagName) {
        Element dataElem = doc.createElement(tagName);

        if (data != null) {
            appendChildWithText(doc, dataElem, "Giorno", String.valueOf(data.getGiorno()));
            appendChildWithText(doc, dataElem, "Mese", String.valueOf(data.getMese()));
        }

        return dataElem;
    }

    private static Element serializeOra(Document doc, LocalTime ora, String tagName) {
        Element oraElem = doc.createElement(tagName); // es. "OraInizio" o "OraFine"

        if (ora != null) {
            appendChildWithText(doc, oraElem, "Ore", String.valueOf(ora.getHour()));
            appendChildWithText(doc, oraElem, "Minuti", String.valueOf(ora.getMinute()));
            appendChildWithText(doc, oraElem, "Secondi", String.valueOf(ora.getSecond()));
        }

        return oraElem;
    }

    private static Element serializePeriodo(Document doc, Periodo periodo) {
        Element periodoElem = doc.createElement("PeriodoProgrammabile");

        if (periodo != null) {
            if (periodo.getDataInizio() != null) {
                periodoElem.appendChild(serializeData(doc, periodo.getDataInizio(), "DataInizio"));
            }
            if (periodo.getDataFine() != null) {
                periodoElem.appendChild(serializeData(doc, periodo.getDataFine(), "DataFine"));
            }
        }

        return periodoElem;
    }

    //---------------------------------- LETTURA -----------------------------------------------------
    
    //ATTENZIONE: QUI SVILUPPO DI MERDA PERCHE' APPLICHEREMO FACADE PER LA PARTE B
    

    public static List<AmbitoTerritoriale> leggiAmbiti(String path) throws Exception {
        List<AmbitoTerritoriale> ambiti = new ArrayList<>();

        Document doc = apriDocumento(path);
        NodeList ambitoNodes = doc.getElementsByTagName("AmbitoTerritoriale");

        for (int i = 0; i < ambitoNodes.getLength(); i++) {
            Element ambitoElem = (Element) ambitoNodes.item(i);

            AmbitoTerritoriale amb = new AmbitoTerritoriale();
            amb.setProvincia(ambitoElem.getAttribute("provincia"));

            NodeList caps = ambitoElem.getElementsByTagName("CAP");
            ArrayList<Integer> capList = new ArrayList<>();
            for (int j = 0; j < caps.getLength(); j++) {
            	 Element capElem = (Element) caps.item(j);
                 String capText = capElem.getTextContent().trim();
                 try {
                     int capValue = Integer.parseInt(capText);
                     capList.add(capValue);
                 } catch (NumberFormatException e) {
                     System.out.println("⚠️ CAP non valido nel file XML: " + capText);
                 }
                 amb.setCap(capList); 
            }

            ambiti.add(amb);
        }
        return ambiti;
    }

    public static List<Luogo> leggiLuoghi(String path) throws Exception {
        List<Luogo> luoghi = new ArrayList<>();

        Document doc = apriDocumento(path);
        NodeList luoghiNodes = doc.getElementsByTagName("Luogo");

        for (int i = 0; i < luoghiNodes.getLength(); i++) {
            Element luogoElem = (Element) luoghiNodes.item(i);

            Luogo luogo = new Luogo();
            luogo.setNomeLuogo(luogoElem.getAttribute("Nome"));
            luogo.setNomeLuogo(luogoElem.getAttribute("Descrizione"));
            // TipiVisita
            NodeList tipoVisitaNodes = luogoElem.getElementsByTagName("TipoVisita");
            for (int j = 0; j < tipoVisitaNodes.getLength(); j++) {
                Element tipoVisitaElem = (Element) tipoVisitaNodes.item(j);
                TipoVisita tv = parseTipoVisita(tipoVisitaElem);
                luogo.getListaTipoVisita().add(tv);
            }

            luoghi.add(luogo);
        }

        return luoghi;
    }

    private static TipoVisita parseTipoVisita(Element elem) {
        TipoVisita tv = new TipoVisita();

        tv.setTitolo(getText(elem, "Titolo"));
        tv.setDescrizione(getText(elem, "Descrizione"));

        // Indirizzo
        Element indirizzoElem = (Element) elem.getElementsByTagName("Indirizzo").item(0);
        tv.setPuntoDiIncontro(parseIndirizzo(indirizzoElem));

        // DataVisita
        Element dataElem = (Element) elem.getElementsByTagName("DataVisita").item(0);
        tv.setDataVisita(parseData(dataElem));

        // OraInizio
        Element oraElem = (Element) elem.getElementsByTagName("OraInizio").item(0);
        int ore = Integer.parseInt(getText(oraElem, "Ore"));
        int min = Integer.parseInt(getText(oraElem, "Minuti"));
        int sec = Integer.parseInt(getText(oraElem, "Secondi"));
        tv.setOraInizio(LocalTime.of(ore, min, sec));

        // altri campi
        tv.setDurataVisita(Integer.parseInt(getText(elem, "DurataVisita")));
        tv.setBiglietto(Boolean.parseBoolean(getText(elem, "Biglietto")));
        tv.setSogliaMinPartecipanti(Integer.parseInt(getText(elem, "SogliaMinPartecipanti")));
        tv.setSogliaMaxPartecipanti(Integer.parseInt(getText(elem, "SogliaMaxPartecipanti")));

        // PeriodoProgrammabile
        Element periodoElem = (Element) elem.getElementsByTagName("PeriodoProgrammabile").item(0);
        if (periodoElem != null) {
            Periodo p = new Periodo();
            p.setDataInizio(parseData((Element) periodoElem.getElementsByTagName("DataInizio").item(0)));
            p.setDataFine(parseData((Element) periodoElem.getElementsByTagName("DataFine").item(0)));
            tv.setPeriodoProgrammabile(p);
        }

        // Volontari LAIBA: RITORNARCI SU perch� creazione Volontari dipende da Credenziali
        //idealmente, bisogna fare un metodo che, letto il nickname da file, ricerca e associa il volontario correto da credenziali
        NodeList volontariNodes = elem.getElementsByTagName("Volontario");
        for (int i = 0; i < volontariNodes.getLength(); i++) {
            //tv.getVolontari().add(volontariNodes.item(i).getTextContent());
        	//tv.setVolontari(volontariNodes.item(i).getTextContent());
        }

        return tv;
    }

    private static Indirizzo parseIndirizzo(Element elem) {
        if (elem == null) return null;

        Indirizzo ind = new Indirizzo();
        //ind.setTipoStrada(getText(elem, "TipoStrada"));
        ind.setTipoStrada(TipoStrada.valueOf(getText(elem, "TipoStrada")));
        ind.setNomeStrada(getText(elem, "NomeStrada"));
        ind.setNumeroCivico(getText(elem, "NumeroCivico"));
        String capText = getText(elem, "CAP").trim();
        try {
            int capValue = Integer.parseInt(capText);
            ind.setCap(capValue);
        } catch (NumberFormatException e) {
            System.out.println("CAP non valido: " + capText);
            ind.setCap(0); // valore di default in caso di errore
        }
        ind.setComune(getText(elem, "Comune"));
        ind.setProvincia(getText(elem, "Provincia"));
        return ind;
    }

    private static Data parseData(Element elem) {
        if (elem == null) return null;

        Data d = new Data();
        d.setGiorno(Integer.parseInt(getText(elem, "Giorno")));
        d.setMese(Integer.parseInt(getText(elem, "Mese")));
        return d;
    }

    private static String getText(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }

    private static Document apriDocumento(String path) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(new File(path));
        //return db.parse(FileXML);
        
    }
    

    
    
	}


	
	
	
	

