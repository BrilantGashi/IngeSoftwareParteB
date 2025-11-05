
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

    //File file = new File("credenziali.txt");
    //File fileXML = new File("CorpoDati.xml");

    private String role1 = null;
    private String role2 = null;
    private String role3 = null;
    private String role4 = null;
    private ArrayList<String> rolev;

    /**
     * Costruttore vuoto
     */
    public GestioneFile() { }

    //*************************************************************************************************
    //----------------------------------- GESTIONE FILE TXT -------------------------------------------
    //*************************************************************************************************

    /**
     * Cambia la password dell'utente indicato senza alterare altre righe o campi.
     *
     * @param username   username da cercare (colonna 2)
     * @param nuovaPass  nuova password da salvare
     * @return true se la password è stata aggiornata; false se lo username non è stato trovato
     */
    /*public boolean aggiornaPasswordSuFile(String username, String nuovaPass)
            throws FileNotFoundException, IOException {

        File tmp = new File(file.getAbsolutePath() + ".tmp");
        boolean trovato = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tmp, false))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] campi = line.split(";", 3); // tipo ; username ; password

                if (campi[1].equalsIgnoreCase(username)) {
                    campi[2] = nuovaPass; // cambia SOLO la password
                    trovato = true;
                }

                bw.write(String.join(";", campi));
                bw.newLine();
            }
        }

        if (trovato) {
            if (!file.delete() || !tmp.renameTo(file)) {
                throw new IOException("Impossibile sostituire il file credenziali");
            }
        } else {
            tmp.delete();
        }

        return trovato;
    }*/

    private void appendChildWithText(Document doc, Element parent, String tag, String text) {
        Element e = doc.createElement(tag);
        e.appendChild(doc.createTextNode(text != null ? text : ""));
        parent.appendChild(e);
    }

    private Document apriDocumento(String path) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(new File(path));
    }

    private String getText(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }

    //*************************************************************************************************
    //----------------------------------- GESTIONE FILE XML -------------------------------------------
    //*************************************************************************************************

    //---------------------------------- LETTURA ------------------------------------------------------

    public AmbitoTerritoriale leggiAmbiti() throws Exception {
        Document doc = apriDocumento("CorpoDati.xml");
        NodeList ambitoNodes = doc.getElementsByTagName("AmbitoTerritoriale");

        Element ambitoElem = (Element) ambitoNodes.item(0);

        AmbitoTerritoriale ambito = new AmbitoTerritoriale();
        ambito.setProvincia(ambitoElem.getAttribute("provincia"));

        NodeList caps = ambitoElem.getElementsByTagName("CAP");
        ArrayList<Integer> capList = new ArrayList<>();

        for (int j = 0; j < caps.getLength(); j++) {
            String capText = caps.item(j).getTextContent().trim();
            try {
                capList.add(Integer.parseInt(capText));
            } catch (NumberFormatException e) {
                System.out.println(" CAP non valido nel file XML: " + capText);
            }
        }

        ambito.setCap(capList);
        return ambito;
    }

    public ArrayList<Luogo> leggiLuoghi() throws Exception {
        ArrayList<Luogo> luoghi = new ArrayList<>();
        Document doc = apriDocumento("CorpoDati.xml");
        NodeList luoghiNodes = doc.getElementsByTagName("Luogo");

        for (int i = 0; i < luoghiNodes.getLength(); i++) {
            Element luogoElem = (Element) luoghiNodes.item(i);

            Luogo luogo = new Luogo();
            luogo.setNomeLuogo(luogoElem.getAttribute("Nome"));
            luogo.setDescrizione(luogoElem.getAttribute("Descrizione"));

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

    /*public String[] leggiSingolaCredenziale(String username) throws IOException {
        String[] riga;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                riga = line.split(";");
                if (riga[1].equalsIgnoreCase(username)) {
                    return riga;
                }
            }
        }
        return null;
    }
*/
    /*public void leggiUtentiDaFile(List<Configuratore> configuratori, ListaVolontari volontari) throws IOException {
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
                        volontari.aggiungiVolontario(new Volontario(cred));
                        break;
                    case "FRUITORE":
                        // fruitori.add(new Fruitore(username, password));
                        break;
                    default:
                        System.out.println("Ruolo sconosciuto: " + ruolo);
                }
            }
        }
    }
*/
    /*public ArrayList<String> restituisciListaUtenza(String utenza) throws IOException {
        ArrayList<String> lista = new ArrayList<>();
        String[] riga;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                riga = line.split(";");
                if (riga[0].equalsIgnoreCase(utenza)) {
                    lista.add(riga[1]);
                }
            }
        }
        return lista;
    }*/

    //---------------------------------- PARSING ------------------------------------------------------

    private String nvl(String s) {
        return s == null ? "" : s;
    }

    private Data parseData(Element elem) {
        if (elem == null) return null;
        Data d = new Data();
        d.setGiorno(Integer.parseInt(getText(elem, "Giorno")));
        d.setMese(Integer.parseInt(getText(elem, "Mese")));
        return d;
    }

    private ArrayList<Giorni> parseGiorniSettimana(Element elem) {
        if (elem == null) return null;
        ArrayList<Giorni> giorni = new ArrayList<>();
        NodeList listaGiorni = elem.getElementsByTagName("Giorno");

        for (int i = 0; i < listaGiorni.getLength(); i++) {
            Element giornoElem = (Element) listaGiorni.item(i);
            String nomeGiorno = giornoElem.getTextContent().trim();
            try {
                Giorni g = Giorni.valueOf(nomeGiorno);
                if (!giorni.contains(g)) {
                    giorni.add(g);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Giorno non valido trovato nell'XML: " + nomeGiorno);
            }
        }
        return giorni;
    }

    private Indirizzo parseIndirizzo(Element elem) {
        if (elem == null) return null;

        Indirizzo ind = new Indirizzo();
        ind.setTipoStrada(TipoStrada.valueOf(getText(elem, "TipoStrada")));
        ind.setNomeStrada(getText(elem, "NomeStrada"));
        ind.setNumeroCivico(getText(elem, "NumeroCivico"));

        String capText = getText(elem, "CAP").trim();
        try {
            ind.setCap(Integer.parseInt(capText));
        } catch (NumberFormatException e) {
            System.out.println("CAP non valido: " + capText);
            ind.setCap(0); // valore di default in caso di errore
        }
        ind.setComune(getText(elem, "Comune"));
        ind.setProvincia(getText(elem, "Provincia"));
        return ind;
    }

    private TipoVisita parseTipoVisita(Element elem) {
        TipoVisita tv = new TipoVisita();

        tv.setTitolo(getText(elem, "Titolo"));
        tv.setDescrizione(getText(elem, "Descrizione"));

        // Indirizzo
        tv.setPuntoDiIncontro(getText(elem, "PuntoDiIncontro"));

        // Giorni programmabili
        Element giorniElem = (Element) elem.getElementsByTagName("GiorniSettimanaProgrammabile").item(0);
        tv.setGiorniSettimanaProgrammabile(parseGiorniSettimana(giorniElem));

        // OraInizio
        Element oraElem = (Element) elem.getElementsByTagName("OraInizio").item(0);
        int ore = Integer.parseInt(getText(oraElem, "Ore"));
        int min = Integer.parseInt(getText(oraElem, "Minuti"));
        int sec = Integer.parseInt(getText(oraElem, "Secondi"));
        tv.setOraInizio(LocalTime.of(ore, min, sec));

        // Altri campi
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

        // Volontari (da completare in futuro)
        NodeList volontariNodes = elem.getElementsByTagName("Volontario");
        for (int i = 0; i < volontariNodes.getLength(); i++) {
            // tv.getVolontari().add(volontariNodes.item(i).getTextContent());
            // tv.setVolontari(volontariNodes.item(i).getTextContent());
        }

        return tv;
    }

    //---------------------------------- SCRITTURA -----------------------------------------------------
/*
    public void scriviCorpoDati(int nMaxPersone, AmbitoTerritoriale ambito, ListaLuogo listaLuoghi) throws Exception {
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

            // CAP dell'ambito
            Element capsElem = doc.createElement("CAPs");
            for (Integer c : ambito.getCap()) {
                appendChildWithText(doc, capsElem, "CAP", c.toString());
            }
            ambitoElem.appendChild(capsElem);

            // Luoghi
            Element luoghiElem = doc.createElement("Luoghi");
            root.appendChild(luoghiElem);

            for (Luogo luogo : listaLuoghi.getListaLuoghi()) {
                Element luogoElem = doc.createElement("Luogo");
                luogoElem.setAttribute("nome", luogo.getNomeLuogo());
                luoghiElem.appendChild(luogoElem);

                Element descrizioneElem = doc.createElement("Descrizione");
                descrizioneElem.setTextContent(luogo.getDescrizione());
                luogoElem.appendChild(descrizioneElem);
                
                //Indirizzo
                luogoElem.appendChild(serializeIndirizzo(doc, luogo.getIndirizzo()));
                // Tipi di visita
                Element tipiVisitaElem = doc.createElement("TipiVisita");
                luogoElem.appendChild(tipiVisitaElem);

                for (TipoVisita tv : luogo.getListaTipoVisita().getTipi()) {
                    tipiVisitaElem.appendChild(serializeTipoVisita(doc, tv));
                }
            }

            // Scrittura su file
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("CorpoDati.xml")));
            System.out.println("XML scritto in " + fileXML);

        } catch (ParserConfigurationException | TransformerException | IOException e) {
            System.out.println("Errore durante la scrittura XML: " + e.getMessage());
        }
    }
*/
    //---------------------------------- SERIALIZZAZIONE ----------------------------------------------

    private Element serializeData(Document doc, Data data, String tagName) {
        Element dataElem = doc.createElement(tagName);
        if (data != null) {
            appendChildWithText(doc, dataElem, "Giorno", String.valueOf(data.getGiorno()));
            appendChildWithText(doc, dataElem, "Mese", String.valueOf(data.getMese()));
        }
        return dataElem;
    }

    private Element serializeGiorniSettimana(Document doc, ArrayList<Giorni> giorniSettimana, String tagName) {
        Element giorniElem = doc.createElement(tagName);
        if (giorniSettimana != null && !giorniSettimana.isEmpty()) {
            for (Giorni g : giorniSettimana) {
                String nomeGiorno = Giorni.getNomeGiorno(g.ordinal() + 1);
                appendChildWithText(doc, giorniElem, "Giorno", nomeGiorno);
            }
        }
        return giorniElem;
    }

    private Element serializeIndirizzo(Document doc, Indirizzo indirizzo) {
        Element indirizzoElem = doc.createElement("Indirizzo");
        if (indirizzo != null) {
            if (indirizzo.getTipoStrada() != null) {
                appendChildWithText(doc, indirizzoElem, "TipoStrada", indirizzo.getTipoStrada().toString());
            }
            appendChildWithText(doc, indirizzoElem, "NomeStrada", nvl(indirizzo.getNomeStrada()));
            appendChildWithText(doc, indirizzoElem, "NumeroCivico", nvl(indirizzo.getNumeroCivico()));
            appendChildWithText(doc, indirizzoElem, "CAP", nvl(indirizzo.getCap().toString()));
            appendChildWithText(doc, indirizzoElem, "Comune", nvl(indirizzo.getComune()));
            appendChildWithText(doc, indirizzoElem, "Provincia", nvl(indirizzo.getProvincia()));
        }
        return indirizzoElem;
    }

    private Element serializeOra(Document doc, LocalTime ora, String tagName) {
        Element oraElem = doc.createElement(tagName);
        if (ora != null) {
            appendChildWithText(doc, oraElem, "Ore", String.valueOf(ora.getHour()));
            appendChildWithText(doc, oraElem, "Minuti", String.valueOf(ora.getMinute()));
        }
        return oraElem;
    }

    private Element serializePeriodo(Document doc, Periodo periodo) {
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

    private Element serializeTipoVisita(Document doc, TipoVisita tv) {
        Element tipoVisitaElem = doc.createElement("TipoVisita");

        appendChildWithText(doc, tipoVisitaElem, "Titolo", nvl(tv.getTitolo()));
        appendChildWithText(doc, tipoVisitaElem, "Descrizione", nvl(tv.getDescrizione()));
        appendChildWithText(doc, tipoVisitaElem, "PuntoDiIncontro", nvl(tv.getPuntoDiIncontro()));

        tipoVisitaElem.appendChild(serializeGiorniSettimana(doc, tv.getGiorniSettimanaProgrammabile(), "GiorniSettimanaProgrammabile"));
        tipoVisitaElem.appendChild(serializeOra(doc, tv.getOraInizio(), "OraInizio"));

        appendChildWithText(doc, tipoVisitaElem, "DurataVisita", String.valueOf(tv.getDurataVisita()));
        appendChildWithText(doc, tipoVisitaElem, "Biglietto", String.valueOf(tv.isBiglietto()));
        appendChildWithText(doc, tipoVisitaElem, "SogliaMinPartecipanti", String.valueOf(tv.getSogliaMinPartecipanti()));
        appendChildWithText(doc, tipoVisitaElem, "SogliaMaxPartecipanti", String.valueOf(tv.getSogliaMaxPartecipanti()));

        // Periodo programmabile
        tipoVisitaElem.appendChild(serializePeriodo(doc, tv.getPeriodoProgrammabile()));

        // Volontari
        Element volontariElem = doc.createElement("Volontari");
        if (tv.getListaVolontari() != null) {
            for (int i = 0; i < tv.getListaVolontari().getVolontari().size(); i++) {
                appendChildWithText(doc, volontariElem, "Volontario",
                        tv.getListaVolontari().getVolontari().get(i).getNickname());
            }
            /* 
            // Versione alternativa con lista di stringhe
            for (String v : tv.getVolontari()) {
                appendChildWithText(doc, volontariElem, "Volontario", v);
            }
            */
        }
        tipoVisitaElem.appendChild(volontariElem);

        return tipoVisitaElem;
    }

}
