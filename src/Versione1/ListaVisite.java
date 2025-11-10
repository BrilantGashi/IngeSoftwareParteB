package Versione1;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

import it.unibs.fp.mylib.InputDati;

public class ListaVisite {
    
    private ArrayList<Visita> listaVisite;

    /**
     * INVARIANTI DI CLASSE:
     * - listaVisite != null
     * - tutti gli elementi di listaVisite != null
     * - non ci sono duplicati con stesso TipoVisita + stessa Data
     */

    public ListaVisite() {
        this.listaVisite = new ArrayList<>();
        assert invClass() : "Invariante violato nel costruttore";
    }
    
    /**
     * Aggiunge una visita alla lista.
     * PRECONDIZIONE: v != null
     * POSTCONDIZIONE: se non era già presente, v ∈ listaVisite
     */
    public void aggiungiVisita(Visita v) {
    	Objects.requireNonNull(v, "La visita non può essere null");
        listaVisite.add(v);
        assert listaVisite.contains(v) : "Postcondizione violata: visita non aggiunta";
        assert invClass() : "Invariante violato in aggiungiVisita";
    }
    
    
    /**
     * Filtra le visite per stato.
     * PRECONDIZIONE: stato != null
     * POSTCONDIZIONE: tutte le visite restituite hanno stato == parametro
     */
    public ArrayList<Visita> filtraPerStato(StatoVisita stato) {
        Objects.requireNonNull(stato, "Lo stato non può essere null");
        ArrayList<Visita> risultato = new ArrayList<>();
        for (Visita v : this.listaVisite) {
            if (v.getStatoVisita() == stato) {
                risultato.add(v);
            }
        }
        for (Visita v : risultato) {
            assert v.getStatoVisita() == stato : "Postcondizione violata in filtraPerStato";
        }
        return risultato;
    }
     
    /**
     * Filtra le visite per volontario.
     * PRECONDIZIONE: v != null
     * POSTCONDIZIONE: tutte le visite restituite contengono v
     */
    public ArrayList<Visita> filtraPerVolontario(Volontario v){
        Objects.requireNonNull(v, "Il volontario non può essere null");
        ArrayList<Visita> risultato = new ArrayList<>();
        for(Visita visita: listaVisite) {
            if(visita.getVolontariEffettivi().getVolontari().contains(v)) {
                risultato.add(visita);
            }
        }
        for (Visita visita : risultato) {
            assert visita.getVolontariEffettivi().getVolontari().contains(v) : "Postcondizione violata in filtraPerVolontario";
        }
        return risultato;
    }
      
    public ArrayList<Visita> getListaVisite() {
        return listaVisite; // copia difensiva
    }

    /**
     * Rimuove una visita dalla lista.
     * PRECONDIZIONE: v != null
     * POSTCONDIZIONE: v non appartiene più a listaVisite
     */
    public void rimuoviVisita(Visita v) {
        Objects.requireNonNull(v, "La visita non può essere null");
        listaVisite.remove(v);
        assert !listaVisite.contains(v) : "Postcondizione violata in rimuoviVisita";
        assert invClass() : "Invariante violato in rimuoviVisita";
    }
      
    /**
     * Imposta la lista delle visite.
     * PRECONDIZIONE: listaVisite != null && non contiene null
     * POSTCONDIZIONE: this.listaVisite == listaVisite
     */
    public void setListaVisite(ArrayList<Visita> listaVisite) {
        Objects.requireNonNull(listaVisite, "La lista non può essere null");
        for (Visita v : listaVisite) {
            if (v == null) throw new IllegalArgumentException("La lista non può contenere elementi null");
        }
        this.listaVisite = new ArrayList<>(listaVisite);
        assert invClass() : "Invariante violato in setListaVisite";
    }

    /**
     * Aggiunge la disponibilità di un volontario per un certo tipo di visita in una data.
     * PRECONDIZIONI:
     * - v != null
     * - tipoVisita != null
     * - data != null
     * POSTCONDIZIONI:
     * - se esiste già una visita con stesso tipo e data, v ∈ volontariEffettivi di quella visita
     * - altrimenti, viene creata una nuova visita con v come primo volontario
     */
    public void aggiungiDisponibilita(Volontario v, TipoVisita tipoVisita, Data data) {
        Objects.requireNonNull(v, "Volontario non può essere null");
        Objects.requireNonNull(tipoVisita, "TipoVisita non può essere null");
        Objects.requireNonNull(data, "Data non può essere null");

        Visita esistente = trovaVisita(tipoVisita, data);
        if (esistente != null) {
            esistente.aggiungiVolontario(v);
        } else {
            Visita nuova = new Visita(tipoVisita, v, StatoVisita.PROPOSTA, data);
            listaVisite.add(nuova);
        }
        assert invClass() : "Invariante violato in aggiungiDisponibilita";
    }

    private Visita trovaVisita(TipoVisita tipoVisita, Data data) {
        for (Visita v : listaVisite) {
            if (v.getTipoVisita().equals(tipoVisita) &&
                v.getDataVisita().equals(data)) {
                return v;
            }
        }
        return null;
    }
    
    public Visita selezionaVisitaProposta(String meseNome, Fruitore fruitore, ListaIscrizioni listaIscrizioniFruitore) {
        Objects.requireNonNull(meseNome, "Il nome del mese non può essere null");
        Objects.requireNonNull(fruitore, "Fruitore non può essere null");

        ArrayList<Visita> proposte = filtraPerStato(StatoVisita.PROPOSTA);
        ArrayList<Visita> proposteNelMeseDisponibili = new ArrayList<>();

        for (Visita v : proposte) {
            Data data = v.getDataVisita();
            if (data != null && meseNome.equalsIgnoreCase(Mesi.getNomeMese(data.getMese()))) {
                // Exclude se fruitore già iscritto o visita piena
                boolean giaIscritto = listaIscrizioniFruitore.trovaPerFruitoreEVisita(fruitore, v) != null;
                int tot = listaIscrizioniFruitore.getTotaleIscrittiPerVisita(v);
                int max = v.getTipoVisita().getSogliaMaxPartecipanti();
                if (!giaIscritto && tot < max) {
                    proposteNelMeseDisponibili.add(v);
                }
            }
        }

        if (proposteNelMeseDisponibili.isEmpty()) {
            System.out.println("Nessuna visita disponibile per iscrizione per il mese di " + meseNome + ".");
            return null;
        }

        // Stampa elenco
        System.out.println("===== Visite disponibili per " + meseNome + " =====");
        for (int i = 0; i < proposteNelMeseDisponibili.size(); i++) {
            Visita v = proposteNelMeseDisponibili.get(i);
            String titolo = (v.getTipoVisita() != null) ? v.getTipoVisita().getTitolo() : "Senza titolo";
            int max = v.getTipoVisita().getSogliaMaxPartecipanti();
            int tot = listaIscrizioniFruitore.getTotaleIscrittiPerVisita(v);
            int postiRimasti = max - tot;
            System.out.println((i + 1) + ") " + titolo + " - " + v.getDataVisita() + " | Posti rimasti: " + postiRimasti);
        }

        int scelta = InputDati.leggiIntero(
            "Seleziona una visita (1-" + proposteNelMeseDisponibili.size() + "): ",
            1, proposteNelMeseDisponibili.size()
        );

        return proposteNelMeseDisponibili.get(scelta - 1);
    }
    
    /**
     * Verifica che non ci siano visite duplicate (stesso TipoVisita + stessa Data).
     * POSTCONDIZIONE: ritorna true sse non ci sono duplicati
     */
    public boolean visiteUnichePerTipoEData() {
        for (int i = 0; i < listaVisite.size(); i++) {
            Visita v1 = listaVisite.get(i);
            for (int j = i + 1; j < listaVisite.size(); j++) {
                Visita v2 = listaVisite.get(j);
                if (v1.getTipoVisita().equals(v2.getTipoVisita()) &&
                    v1.getDataVisita().equals(v2.getDataVisita())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean visitaSovrapposta(TipoVisita tipoDaAggiungere, Data giorno, ListaLuogo listaLuogo) {
        Luogo luogoCorrente = listaLuogo.trovaLuogoPerTipoVisita(tipoDaAggiungere);
        if (luogoCorrente == null) return false;

        LocalTime inizioA = tipoDaAggiungere.getOraInizio();
        LocalTime fineA = inizioA.plusMinutes(tipoDaAggiungere.getDurataVisita());

        for (Visita v : this.getListaVisite()) { // usa l'array della classe
            Luogo luogoVisitaEsistente = listaLuogo.trovaLuogoPerTipoVisita(v.getTipoVisita());
            if (luogoCorrente.equals(luogoVisitaEsistente) && v.getDataVisita().equals(giorno)) {
                LocalTime inizioB = v.getTipoVisita().getOraInizio();
                LocalTime fineB = inizioB.plusMinutes(v.getTipoVisita().getDurataVisita());
                if (!(fineA.isBefore(inizioB) || inizioA.isAfter(fineB))) {
                    return true;
                }
            }
        }
        return false;
    }


      
    public void stampaTitoloEStato() {
        if (listaVisite == null || listaVisite.isEmpty()) {
            System.out.println("Non ci sono visite registrate al momento.");
            return;
        }

        for (Visita visita : listaVisite) {
            String titolo = (visita.getTipoVisita() != null) ? visita.getTipoVisita().getTitolo() : "Senza titolo";
            Data data = visita.getDataVisita();
            String dataStr = (data != null) ? data.toString() : "Data N/D";

            // Volontari associati
            String volontariStr = "Volontari: ";
            if (visita.getVolontariEffettivi() != null && !visita.getVolontariEffettivi().getVolontari().isEmpty()) {
                ArrayList<String> nomiVol = new ArrayList<>();
                for (Volontario vol : visita.getVolontariEffettivi().getVolontari()) {
                    nomiVol.add(vol.getCredenziali().getUsername());
                }
                volontariStr += String.join(", ", nomiVol);
            } else {
                volontariStr += "nessuno";
            }

            System.out.println("- " + titolo 
                + " | Data: " + dataStr
                + " | " + volontariStr);
        }
    }

    public void stampaVisitaPerFruitore() {
        if (listaVisite == null || listaVisite.isEmpty()) {
            System.out.println("Non ci sono visite registrate al momento.");
            return;
        }

        for (Visita visita : listaVisite) {
            StatoVisita stato = visita.getStatoVisita();
            // Mostra solo PROPOSTA, CONFERMATA, CANCELLATA
            if (stato == StatoVisita.PROPOSTA || stato == StatoVisita.CONFERMATA) {
                TipoVisita tipo = visita.getTipoVisita();
                String titolo = (tipo != null) ? tipo.getTitolo() : "Senza titolo";
                String descrizione = (tipo != null) ? tipo.getDescrizione() : "N/D";
                String puntoIncontro = (tipo != null) ? tipo.getPuntoDiIncontro() : "N/D";
                String oraInizio = (tipo != null && tipo.getOraInizio() != null) ? tipo.getOraInizio().toString() : "Orario N/D";
                boolean bigliettoRichiesto = (tipo != null) && tipo.isBiglietto();
                Data data = visita.getDataVisita();
                String dataStr = (data != null) ? data.toString() : "Data N/D";

                System.out.println("- " + titolo
                    + "\n  Descrizione: " + descrizione
                    + "\n  Punto di incontro: " + puntoIncontro
                    + "\n  Data: " + dataStr
                    + "\n  Ora inizio: " + oraInizio
                    + "\n  Biglietto da acquistare: " + (bigliettoRichiesto ? "SI" : "NO")
                    + "\n  Stato: " + stato.name());
            } else if (stato == StatoVisita.CANCELLATA) {
                TipoVisita tipo = visita.getTipoVisita();
                String titolo = (tipo != null) ? tipo.getTitolo() : "Senza titolo";
                Data data = visita.getDataVisita();
                String dataStr = (data != null) ? data.toString() : "Data N/D";

                System.out.println("- " + titolo
                    + "\n  Data del mancato svolgimento: " + dataStr
                    + "\n  Stato: " + stato.name());
            }
            // Puoi aggiungere altri else if per altri stati, oppure ignorarli
        }
    }
    
    public void stampaPianoVisite() {
        if (listaVisite == null || listaVisite.isEmpty()) {
            System.out.println("Non ci sono visite registrate al momento.");
            return;
        }

        System.out.println("===== PIANO DELLE VISITE =====");
        for (Visita v : listaVisite) {
            String titolo = (v.getTipoVisita() != null) ? v.getTipoVisita().getTitolo() : "Senza titolo";
            Data data = v.getDataVisita();
            String stato = (v.getStatoVisita() != null) ? v.getStatoVisita().name() : "N/D";

            String volontariString = "";
            if (v.getVolontariEffettivi() != null && !v.getVolontariEffettivi().getVolontari().isEmpty()) {
                ArrayList<String> nomiVol = new ArrayList<>();
                for (Volontario vol : v.getVolontariEffettivi().getVolontari()) {
                    nomiVol.add(vol.getCredenziali().getUsername());
                }
                volontariString = String.join(", ", nomiVol);
            } else {
                volontariString = "Nessuno";
            }
            System.out.println("Titolo visita   : " + titolo);
            System.out.println("Data            : " + data.toString());
            System.out.println("Stato           : " + stato);
            System.out.println("Volontario/i    : " + volontariString);
            System.out.println("------------------------------------");
        }
    }
  
    private boolean invClass() {
        if (listaVisite == null) return false;
        for (Visita v : listaVisite) {
            if (v == null) return false;
        }
        return visiteUnichePerTipoEData();
    }
	
}
