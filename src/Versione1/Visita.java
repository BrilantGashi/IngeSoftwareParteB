package Versione1;

import java.util.ArrayList;
import java.util.Objects;

public class Visita {

    private Data dataVisita;
    private StatoVisita statoVisita;
    private TipoVisita tipoVisita;
    private ArrayList<Volontario> volontariEffettivi;

    /**
     * INVARIANTI DI CLASSE:
     * - statoVisita != null
     * - tipoVisita != null (dopo la creazione completa)
     * - volontariEffettivi != null
     * - volontariEffettivi non contiene duplicati
     */

    // Costruttore vuoto
    // Utile per la creazione passo-passo
    // Stato di default = PROPOSTA
    public Visita() {
        this.volontariEffettivi = new ArrayList<>();
        this.statoVisita = StatoVisita.PROPOSTA;
        assert invClass() : "Invariante violato nel costruttore vuoto";
    }

    /**
     * Costruttore completo
     * PRECONDIZIONI:
     * - tipoVisita != null
     * - primoVolontario != null
     * - statoVisita != null
     * - dataVisita != null
     * POSTCONDIZIONI:
     * - this.tipoVisita == tipoVisita
     * - this.statoVisita == statoVisita
     * - this.dataVisita == dataVisita
     * - this.volontariEffettivi contiene primoVolontario
     */
    public Visita(TipoVisita tipoVisita, Volontario primoVolontario, StatoVisita statoVisita, Data dataVisita) {
        Objects.requireNonNull(tipoVisita, "TipoVisita non può essere null");
        Objects.requireNonNull(primoVolontario, "Il primo volontario non può essere null");
        Objects.requireNonNull(statoVisita, "StatoVisita non può essere null");
        Objects.requireNonNull(dataVisita, "DataVisita non può essere null");

        this.tipoVisita = tipoVisita;
        this.volontariEffettivi = new ArrayList<>();
        this.volontariEffettivi.add(primoVolontario);
        this.statoVisita = statoVisita;
        this.dataVisita = dataVisita;

        assert this.volontariEffettivi.contains(primoVolontario) : "Postcondizione violata: volontario non aggiunto";
        assert invClass() : "Invariante violato nel costruttore completo";
    }

    /**
     * Aggiorna lo stato della visita
     * PRECONDIZIONE: nuovoStato != null
     * POSTCONDIZIONE: this.statoVisita == nuovoStato
     */
    public void aggiornaStato(StatoVisita nuovoStato) {
        Objects.requireNonNull(nuovoStato, "Il nuovo stato non può essere null");
        this.statoVisita = nuovoStato;
        assert this.statoVisita == nuovoStato : "Postcondizione violata in aggiornaStato";
        assert invClass() : "Invariante violato in aggiornaStato";
    }

    /**
     * Aggiunge un volontario alla visita
     * PRECONDIZIONE: v != null
     * POSTCONDIZIONE: se non era già presente, v ∈ volontariEffettivi
     */
    public boolean aggiungiVolontario(Volontario v) {
        Objects.requireNonNull(v, "Il volontario non può essere null");
        if (!volontariEffettivi.contains(v)) {
            volontariEffettivi.add(v);
            assert volontariEffettivi.contains(v) : "Postcondizione violata: volontario non aggiunto";
            assert invClass() : "Invariante violato in aggiungiVolontario";
            return true;
        }
        return false;
    }

    /**
     * Rimuove un volontario dalla visita
     * PRECONDIZIONE: v != null
     * POSTCONDIZIONE: v non appartiene più a volontariEffettivi
     */
    public void rimuoviVolontario(Volontario v) {
        Objects.requireNonNull(v, "Il volontario non può essere null");
        volontariEffettivi.remove(v);
        assert !volontariEffettivi.contains(v) : "Postcondizione violata: volontario non rimosso";
        assert invClass() : "Invariante violato in rimuoviVolontario";
    }

    // ================== GETTERS ==================
    public Data getDataVisita() { return dataVisita; }
    public StatoVisita getStatoVisita() { return statoVisita; }
    public TipoVisita getTipoVisita() { return tipoVisita; }
    public ArrayList<Volontario> getVolontariEffettivi() { return new ArrayList<>(volontariEffettivi); }

    // ================== SETTERS ==================

    public void setDataVisita(Data dataVisita) {
        Objects.requireNonNull(dataVisita, "DataVisita non può essere null");
        this.dataVisita = dataVisita;
        assert this.dataVisita == dataVisita : "Postcondizione violata in setDataVisita";
        assert invClass() : "Invariante violato in setDataVisita";
    }

    public void setStatoVisita(StatoVisita statoVisita) {
        Objects.requireNonNull(statoVisita, "StatoVisita non può essere null");
        this.statoVisita = statoVisita;
        assert this.statoVisita == statoVisita : "Postcondizione violata in setStatoVisita";
        assert invClass() : "Invariante violato in setStatoVisita";
    }

    public void setTipoVisita(TipoVisita tipoVisita) {
        Objects.requireNonNull(tipoVisita, "TipoVisita non può essere null");
        this.tipoVisita = tipoVisita;
        assert this.tipoVisita == tipoVisita : "Postcondizione violata in setTipoVisita";
        assert invClass() : "Invariante violato in setTipoVisita";
    }

    public void setVolontariEffettivi(ArrayList<Volontario> volontariEffettivi) {
        Objects.requireNonNull(volontariEffettivi, "La lista dei volontari non può essere null");
        // controllo duplicati
        for (Volontario v : volontariEffettivi) {
            if (volontariEffettivi.indexOf(v) != volontariEffettivi.lastIndexOf(v)) {
                throw new IllegalArgumentException("Lista volontari contiene duplicati");
            }
        }
        this.volontariEffettivi = new ArrayList<>(volontariEffettivi);
        assert invClass() : "Invariante violato in setVolontariEffettivi";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("======VISITA=====\n");
        sb.append("Tipo Visita: ").append(tipoVisita != null ? tipoVisita.getTitolo() : "N/D").append("\n");
        sb.append("Data: ").append(dataVisita != null ? dataVisita.toString() : "N/D").append("\n");
        sb.append("Stato: ").append(statoVisita).append("\n");
        sb.append("Volontari Effettivi: ");
        if (volontariEffettivi != null && !volontariEffettivi.isEmpty()) {
            for (Volontario v : volontariEffettivi) {
                sb.append(v.getNickname()).append(" ");
            }
        } else {
            sb.append("Non ci sono volontari disponibili");
        }
        sb.append("\n=================\n");
        return sb.toString();
    }

    // ================== INVARIANTE ==================
    private boolean invClass() {
        if (statoVisita == null) return false;
        if (volontariEffettivi == null) return false;
        // controllo duplicati
        for (Volontario v : volontariEffettivi) {
            if (volontariEffettivi.indexOf(v) != volontariEffettivi.lastIndexOf(v)) return false;
        }
        return true;
    }
}
