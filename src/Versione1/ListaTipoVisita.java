package Versione1;

import java.util.ArrayList;
import java.util.Objects;

import it.unibs.fp.mylib.InputDati;

public class ListaTipoVisita {
    private ArrayList<TipoVisita> tipi;

    /**
     * INVARIANTI DI CLASSE:
     * - tipi != null
     * - tutti gli elementi di tipi != null
     * - nessun duplicato (stesso titolo + stesso periodo)
     */

    public ListaTipoVisita() {
        this.tipi = new ArrayList<>();
        assert invClass() : "Invariante violato nel costruttore";
    }

    /**
     * Aggiunge un tipo di visita.
     * PRECONDIZIONE: tipoVisita != null
     * POSTCONDIZIONE: se non era già presente, tipoVisita ∈ tipi
     */
    public void add(TipoVisita tipoVisita) {
        Objects.requireNonNull(tipoVisita, "Il tipo visita non può essere null");
        if (!tipi.contains(tipoVisita)) {
            tipi.add(tipoVisita);
        }
        assert tipi.contains(tipoVisita) : "Postcondizione violata: tipoVisita non aggiunto";
        assert invClass() : "Invariante violato in add";
    }

    
    /**
     * Restituisce la lista dei tipi di visita.
     * POSTCONDIZIONE: ritorna una lista non null (può essere vuota).
     */
    public ArrayList<TipoVisita> getTipi() {
        return new ArrayList<>(tipi); // copia difensiva
    }

    /**
     * Restituisce il tipo di visita all'indice specificato.
     * PRECONDIZIONE: indice compreso tra 0 e tipi.size()-1
     * POSTCONDIZIONE: ritorna tipi[indice]
     */
    public TipoVisita getTipo(int i) {
        if (i < 0 || i >= tipi.size()) {
            throw new IndexOutOfBoundsException("Indice non valido");
        }
        return tipi.get(i);
    }

    /**
     * Imposta la lista dei tipi di visita.
     * PRECONDIZIONE: tipi != null && non contiene null
     * POSTCONDIZIONE: this.tipi == tipi
     */
    public void setTipi(ArrayList<TipoVisita> tipi) {
        Objects.requireNonNull(tipi, "La lista non può essere null");
        for (TipoVisita t : tipi) {
            if (t == null) throw new IllegalArgumentException("La lista non può contenere elementi null");
        }
        this.tipi = new ArrayList<>(tipi);
        assert invClass() : "Invariante violato in setTipi";
    }

    /**
     * Restituisce i tipi di visita a cui partecipa un volontario.
     * PRECONDIZIONE: username != null && !username.isBlank()
     * POSTCONDIZIONE: ritorna una ListaTipoVisita con tipi ⊆ this.tipi
     */
    public ListaTipoVisita getTipiPerVolontario(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        ListaTipoVisita tipiDisponibiliPerVolontario = new ListaTipoVisita();
        for (TipoVisita visita : tipi) {
            if (visita.volontarioTrovato(username) && visita.isAttivo()) {
                tipiDisponibiliPerVolontario.add(visita);
            }
        }
        return tipiDisponibiliPerVolontario;
    }

    public ListaTipoVisita trovaTipiVisitaOrfani() {
    	ListaTipoVisita orfani = new ListaTipoVisita();
        for (TipoVisita tv : this.getTipi()) { // oppure: for (TipoVisita tv : tipi)
            if (tv.isAttivo()) {
                boolean haVolontarioAttivo = false;
                for (Volontario v : tv.getListaVolontari().getVolontari()) {
                    if (v.isAttivo()) {
                        haVolontarioAttivo = true;
                        break;
                    }
                }
                if (!haVolontarioAttivo) {
                    orfani.add(tv);
                }
            }
        }
        return orfani;
    }

    public boolean isAggiungibile(TipoVisita visitaSelezionata, ListaTipoVisita listaTipoVisitaToAdd) {
    		boolean duplicato = false;
		    boolean sovrapposta = false;		    
		    // Controlli su duplicati e sovrapposizioni
		    for (TipoVisita v : listaTipoVisitaToAdd.getTipi()) {
		        if (v.equals(visitaSelezionata)) {
		            System.out.println("Attenzione: hai selezionato un Tipo Visita già associato!");
		            duplicato = true;
		            break;
		        } else if (v.siSovrappongono(visitaSelezionata)) {
		            System.out.println("Attenzione: questa visita si sovrappone ad un'altra già programmata per lo stesso giorno!");
		            sovrapposta = true;
		            break;
		        }
		    }
		    
		    // Se non è duplicata né sovrapposta, la aggiungo
		    if (!duplicato && !sovrapposta) {
		        return true;
		    }
		    return false;
    }

    
    public TipoVisita cercaPerTitolo(String titolo) {
        if (titolo == null || titolo.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        //ListaTipoVisita tipiDisponibiliPerVolontario = new ListaTipoVisita();
        for (TipoVisita visita : tipi) {
            if (visita.getTitolo().equals(titolo) && visita.isAttivo()==true) {
                return visita;
            }
        }
        return null;
    }
    
    /**
     * Stampa l'elenco numerato e permette la selezione interattiva di un tipo visita.
     * PRECONDIZIONE: tipi non vuota
     * POSTCONDIZIONE: ritorna il TipoVisita selezionato o null se lista vuota
     */
    public TipoVisita selezionaTipoVisita() {
        if (tipi == null || tipi.isEmpty()) {
            System.out.println("Nessun tipo visita disponibile.");
            return null;
        }
        // Filtra solo tipi visita attivi
        ArrayList<TipoVisita> tipiAttivi = new ArrayList<>();
        for (TipoVisita tv : tipi) {
            if (tv.isAttivo()) tipiAttivi.add(tv);
        }
        if (tipiAttivi.isEmpty()) {
            System.out.println("Nessun tipo visita attivo disponibile.");
            return null;
        }

        // Stampa elenco SOLO di tipi attivi
        for (int i = 0; i < tipiAttivi.size(); i++) {
            int numVolontari = tipiAttivi.get(i).getListaVolontari().getVolontari().size();
            System.out.println((i + 1) + ") " + tipiAttivi.get(i).getTitolo() + " - Volontari: " + numVolontari);
        }
        int scelta = InputDati.leggiIntero("Seleziona il numero del tipo visita: ", 1, tipiAttivi.size());
        return tipiAttivi.get(scelta - 1);
    }



    
    
    
    
    public String toString() {
        if (tipi == null || tipi.isEmpty()) {
            return "Nessun tipo di visita disponibile.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("===== ELENCO TIPI DI VISITA ATTIVI =====\n");
        int i = 1;
        boolean almenoUnoStampato = false;
        for (TipoVisita tv : tipi) {
            if (tv.isAttivo()) {
                sb.append(i++).append(") ").append(tv.getTitolo() != null ? tv.getTitolo() : "Senza titolo").append("\n");
                sb.append("   Descrizione: ").append(tv.getDescrizione() != null ? tv.getDescrizione() : "N/D").append("\n");
                sb.append("   Giorni in cui la visita è programmabile: ");
                if (tv.getGiorniSettimanaProgrammabile() != null) {
                    for (int j = 0; j < tv.getGiorniSettimanaProgrammabile().size(); j++) {
                        Giorni g = tv.getGiorniSettimanaProgrammabile().get(j);
                        sb.append(Giorni.getNomeGiorno(g.ordinal() + 1));
                        if (j < tv.getGiorniSettimanaProgrammabile().size() - 1) {
                            sb.append(", ");
                        }
                    }
                } else {
                    sb.append("N/D");
                }
                sb.append("\n   Ora inizio: ").append(tv.getOraInizio() != null ? tv.getOraInizio().toString() : "N/D").append("\n");
                sb.append("   Durata (minuti): ").append(tv.getDurataVisita()).append("\n");
                sb.append("   Biglietto richiesto: ").append(tv.isBiglietto() ? "Sì" : "No").append("\n");
                sb.append("   Partecipanti: min=").append(tv.getSogliaMinPartecipanti()).append(", max=").append(tv.getSogliaMaxPartecipanti()).append("\n");
                sb.append("   Punto di incontro: ").append(tv.getPuntoDiIncontro() != null ? tv.getPuntoDiIncontro() : "N/D").append("\n");
                if (tv.getPeriodoProgrammabile() != null) {
                    sb.append("   Periodo: ").append(tv.getPeriodoProgrammabile().toString()).append("\n");
                }
                if (tv.getListaVolontari() != null && !tv.getListaVolontari().getVolontari().isEmpty()) {
                    sb.append("   Volontari:\n");
                    for (Volontario v : tv.getListaVolontari().getVolontari()) {
                        sb.append("      - ").append(v.getNickname()).append("\n");
                    }
                }
                sb.append("-----------------------------------\n");
                almenoUnoStampato = true;
            }
        }
        if (!almenoUnoStampato) {
            sb.append("Nessun tipo di visita attivo disponibile.\n");
        }
        return sb.toString();
    }


    public String toStringAsList() {
        StringBuilder returnString = new StringBuilder();
        int count = 1;
        for (TipoVisita tv : tipi) {
            if (tv.isAttivo()) {
                returnString.append(count).append(") ").append(tv.toStringSemplificato()).append("\n");
                count++;
            }
        }
        if (count == 1) {
            returnString.append("Nessun tipo visita disponibile.");
        }
        return returnString.toString();
    }
    
    /**
     * Stampa l'elenco numerato dei tipi di visita con titolo e numero di volontari.
     * PRECONDIZIONE: tipi != null
     * POSTCONDIZIONE: stampa tutti i tipi di visita numerati (o messaggio se vuota).
     */
    public void stampaElencoNumeratoConVolontari() {
        if (tipi == null || tipi.isEmpty()) {
            System.out.println("Nessun tipo visita disponibile.");
            return;
        }
        int count = 1;
        for (TipoVisita tv : tipi) {
            if (tv.isAttivo()) {
                int numVolontari = tv.getListaVolontari().getVolontari().size();
                System.out.println(count + ") " + tv.getTitolo() + " - Volontari: " + numVolontari);
                count++;
            }
        }
        if (count == 1) {
            System.out.println("Nessun tipo visita disponibile.");
        }
    }


    // ================== INVARIANTE ==================
    private boolean invClass() {
        if (tipi == null) return false;
        for (TipoVisita t : tipi) {
            if (t == null) return false;
            if (tipi.indexOf(t) != tipi.lastIndexOf(t)) return false; // duplicato
        }
        return true;
    }
    
   


}
