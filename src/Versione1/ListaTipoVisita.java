package Versione1;

import java.util.ArrayList;
import java.util.Objects;

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
            if (visita.volontarioTrovato(username)) {
                tipiDisponibiliPerVolontario.add(visita);
            }
        }
        return tipiDisponibiliPerVolontario;
    }

    @Override
    public String toString() {
        if (tipi == null || tipi.isEmpty()) {
            return "Nessun tipo di visita disponibile.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("===== ELENCO TIPI DI VISITA =====\n");

        int i = 1;
        for (TipoVisita tv : tipi) {
            sb.append(i++).append(") ").append(tv.getTitolo() != null ? tv.getTitolo() : "Senza titolo").append("\n");
            sb.append("   Descrizione: ").append(tv.getDescrizione() != null ? tv.getDescrizione() : "N/D").append("\n");

            sb.append("Giorni in cui la visita è programmabile: ");
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
        }

        return sb.toString();
    }

    public String toStringAsList() {
        StringBuilder returnString = new StringBuilder();
        for (int i = 0; i < tipi.size(); i++) {
            returnString.append((i+1)).append(") ").append(tipi.get(i).toStringSemplificato()).append("\n");
        }
        return returnString.toString();
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
