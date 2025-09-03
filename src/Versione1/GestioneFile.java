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
	 *         null se lo username non è stato trovato
	 * @throws IOException            problemi di I/O 
	 * @throws FileNotFoundException  se il file non esiste
	 */

	public String[] leggiSingolaCredenziale(String username) throws FileNotFoundException, IOException {
		String[] riga;
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	riga = line.split(";");
		       
		    	//1 perchè è il secondo paramatro della riga -- CONFIGURATORE;utente1;default
		       if (riga[1].equalsIgnoreCase(username)) {
		    	   return riga;		    	   
		       }
		    }
		}
		
		return null;
	}
	
	/**
	 * Cambia la password dell’utente indicato senza alterare altre righe o campi.
	 *
	 * Prerequisito: esiste un campo   private final File file;   inizializzato
	 *
	 * @param username   username da cercare (colonna 2)
	 * @param nuovaPass  nuova password (in chiaro o già hashata) da salvare
	 * @return true  se la password è stata aggiornata;
	 *         false se lo username non è stato trovato
	 * @throws IOException            problemi di I/O (permessi, disco pieno…)
	 * @throws FileNotFoundException  se il file non esiste
	 */
	public boolean aggiornaPasswordSuFile(String username, String nuovaPass)
	        throws FileNotFoundException, IOException {

	    // *** usa il campo "file" già presente ***
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
	        // sostituisce l’originale con il temporaneo
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
		       
		    	//1 perchè è il secondo paramatro della riga -- CONFIGURATORE;utente1;default
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
	
    /*public void saveToXML(String xml) {
        Document dom;
        Element e = null;

        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();

            // create the root element
            Element rootEle = dom.createElement("roles");

            // create data elements and place them under root
            e = dom.createElement("role1");
            e.appendChild(dom.createTextNode(role1));
            rootEle.appendChild(e);

            e = dom.createElement("role2");
            e.appendChild(dom.createTextNode(role2));
            rootEle.appendChild(e);

            e = dom.createElement("role3");
            e.appendChild(dom.createTextNode(role3));
            rootEle.appendChild(e);

            e = dom.createElement("role4");
            e.appendChild(dom.createTextNode(role4));
            rootEle.appendChild(e);

            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom), 
                                     new StreamResult(new FileOutputStream(xml)));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }
    */

    


    public void scriviCorpoDati(AmbitoTerritoriale ambito,
                                       ListaLuogo listaLuoghi) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try {
	        	
	        DocumentBuilder db = dbf.newDocumentBuilder();       
	        Document doc = db.newDocument();
	
	        // Radice
	        Element root = doc.createElement("DatiSistema");
	        doc.appendChild(root);
	
	        // Ambito Territoriale
	        Element ambitoElem = doc.createElement("AmbitoTerritoriale");
	        root.appendChild(ambitoElem);
	        ambitoElem.setAttribute("provincia", ambito.getProvincia());
	        //ambitoElem.appendChild(ambitoElem);
	        root.appendChild(ambitoElem);
	        // CAP dell'ambito
            Element capsElem = doc.createElement("CAPs");
            for (String c : ambito.getCap()) {
               appendChildWithText(doc, capsElem, "CAP", c);
            }
            ambitoElem.appendChild(capsElem);
	        
	        //Luoghi
	        Element luoghiElem = doc.createElement("Luoghi");
	        root.appendChild(luoghiElem);
	        
	        for (int i = 0; i < listaLuoghi.getListaLuoghi().size(); i++ ) {
	        	Element luogoElem = doc.createElement("Luogo");
	            luogoElem.setAttribute("nome", listaLuoghi.getListaLuoghi().get(i).getNomeLuogo());
	            luoghiElem.appendChild(luogoElem);
	            
	            // Tipi di visita
	            Element tipiVisitaElem = doc.createElement("TipiVisita");
	            luogoElem.appendChild(tipiVisitaElem);
	            
	            for (int j = 0; j < listaLuoghi.getListaLuoghi().get(i).getListaTipoVisita().getTipi().size(); j++){
	            	tipiVisitaElem.appendChild(serializeTipoVisita(doc, listaLuoghi.getListaLuoghi().get(i).getListaTipoVisita().getTipi().get(i)));
	            
	            
	            /*for (TipoVisita tv : listaLuoghi.getListaLuoghi().get(i).getListaTipoVisita()) {
	                tipiVisitaElem.appendChild(serializeTipoVisita(doc, tv));
	            }*/
	            }
	        	
	        }
	        
	        
	       /*for (Luogo l : listaLuoghi) {
	            Element luogoElem = doc.createElement("Luogo");
	            luogoElem.setAttribute("nome", l.getNomeLuogo());
	            luoghiElem.appendChild(luogoElem);
	
	            //appendChildWithText(doc, luogoElem, "Descrizione", nvl(l.getDescrizione()));
	
	            // Indirizzo (oggetto annidato)
	            //luogoElem.appendChild(serializeIndirizzo(doc, l.getIndirizzo()));
	
	            // Tipi di visita
	            Element tipiVisitaElem = doc.createElement("TipiVisita");
	            luogoElem.appendChild(tipiVisitaElem);
	
	            for (TipoVisita tv : l.getListaTipoVisitaToAdd()) {
	                tipiVisitaElem.appendChild(serializeTipoVisita(doc, tv));
	            }
	        }*/
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
    

    // ---- Helpers di serializzazione ----
/*
    private static Element serializeIndirizzo(Document doc, Indirizzo ind) {
        Element indirizzoElem = doc.createElement("Indirizzo");

        appendChildWithText(doc, indirizzoElem, "TipoStrada",
                ind.getTipoStrada() != null ? ind.getTipoStrada().name() : "");

        appendChildWithText(doc, indirizzoElem, "NomeStrada", nvl(ind.getNomeStrada()));
        appendChildWithText(doc, indirizzoElem, "NumeroCivico", nvl(ind.getNumeroCivico()));
        appendChildWithText(doc, indirizzoElem, "CAP", nvl(ind.getCap()));
        appendChildWithText(doc, indirizzoElem, "Comune", nvl(ind.getComune()));
        appendChildWithText(doc, indirizzoElem, "Provincia", nvl(ind.getProvincia()));

        return indirizzoElem;
    }
*/
    private static Element serializeTipoVisita(Document doc, TipoVisita tv) {
        Element tipoVisitaElem = doc.createElement("TipoVisita");

       appendChildWithText(doc, tipoVisitaElem, "Titolo", nvl(tv.getTitolo()));
        appendChildWithText(doc, tipoVisitaElem, "Descrizione", nvl(tv.getDescrizione()));
        //appendChildWithText(doc, tipoVisitaElem, "PuntoDiIncontro", nvl(tv.getPuntoDiIncontro()));
        //appendChildWithText(doc, tipoVisitaElem, "DataVisita", nvl(tv.getDataVisita()));
        //appendChildWithText(doc, tipoVisitaElem, "OraInizio", nvl(tv.getOraInizio()));
        appendChildWithText(doc, tipoVisitaElem, "DurataVisita", String.valueOf(tv.getDurataVisita()));
        //appendChildWithText(doc, tipoVisitaElem, "Biglietto", String.valueOf(tv.getBiglietto()));
        appendChildWithText(doc, tipoVisitaElem, "SogliaMinPartecipanti", String.valueOf(tv.getSogliaMinPartecipanti()));
        appendChildWithText(doc, tipoVisitaElem, "SogliaMaxPartecipanti", String.valueOf(tv.getSogliaMaxPartecipanti()));
        //appendChildWithText(doc, tipoVisitaElem, "PeriodoProgrammabile", nvl(tv.getPeriodoProgrammabile()));

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


    
    
    
    
    
    
	}


	
	
	
	

