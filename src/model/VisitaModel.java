package model;

import java.util.Objects;

public class VisitaModel {
    private DataModel dataVisita;
    private StatoVisita statoVisita;
    private TipoVisitaModel tipoVisita;
    private ListaVolontariModel volontariEffettivi = new ListaVolontariModel();

    public VisitaModel(TipoVisitaModel tipoVisita, VolontarioModel volontario, StatoVisita statoVisita, DataModel dataVisita) {
        Objects.requireNonNull(tipoVisita);
        Objects.requireNonNull(volontario);
        Objects.requireNonNull(statoVisita);
        Objects.requireNonNull(dataVisita);
        this.tipoVisita = tipoVisita;
        this.volontariEffettivi.aggiungiVolontario(volontario);
        this.statoVisita = statoVisita;
        this.dataVisita = dataVisita;
    }

    public VisitaModel(TipoVisitaModel tipoVisita, ListaVolontariModel listaVolontari, StatoVisita statoVisita, DataModel dataVisita) {
        Objects.requireNonNull(tipoVisita);
        Objects.requireNonNull(listaVolontari);
        Objects.requireNonNull(statoVisita);
        Objects.requireNonNull(dataVisita);
        this.tipoVisita = tipoVisita;
        this.volontariEffettivi = listaVolontari;
        this.statoVisita = statoVisita;
        this.dataVisita = dataVisita;
    }

    public void aggiornaStato(StatoVisita nuovoStato) {
        Objects.requireNonNull(nuovoStato);
        this.statoVisita = nuovoStato;
    }

    public boolean aggiungiVolontario(VolontarioModel v) {
        Objects.requireNonNull(v);
        if (!volontariEffettivi.getVolontari().contains(v)) {
            volontariEffettivi.aggiungiVolontario(v);
            return true;
        }
        return false;
    }

    public DataModel getDataVisita() { return dataVisita; }
    public StatoVisita getStatoVisita() { return statoVisita; }
    public TipoVisitaModel getTipoVisita() { return tipoVisita; }
    public ListaVolontariModel getVolontariEffettivi() { return volontariEffettivi; }

    public void setDataVisita(DataModel dataVisita) {
        Objects.requireNonNull(dataVisita);
        this.dataVisita = dataVisita;
    }
    public void setStatoVisita(StatoVisita statoVisita) {
        Objects.requireNonNull(statoVisita);
        this.statoVisita = statoVisita;
    }
    public void setTipoVisita(TipoVisitaModel tipoVisita) {
        Objects.requireNonNull(tipoVisita);
        this.tipoVisita = tipoVisita;
    }
    public void setVolontariEffettivi(ListaVolontariModel volontariEffettivi) {
        Objects.requireNonNull(volontariEffettivi);
        this.volontariEffettivi = volontariEffettivi;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("======VISITA=====\n");
        sb.append("Tipo Visita: ").append(tipoVisita != null ? tipoVisita.getTitolo() : "N/D").append("\n");
        sb.append("Data: ").append(dataVisita != null ? dataVisita.toString() : "N/D").append("\n");
        sb.append("Stato: ").append(statoVisita).append("\n");
        sb.append("Volontari Effettivi: ");
        if (volontariEffettivi != null && !volontariEffettivi.getVolontari().isEmpty()) {
            for (VolontarioModel v : volontariEffettivi.getVolontari()) {
                sb.append(v.getNickname()).append(" ");
            }
        } else {
            sb.append("Non ci sono volontari disponibili");
        }
        sb.append("\n=================\n");
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        // 1. Controllo se è lo stesso identico oggetto in memoria
        if (this == o) return true;
        
        // 2. Controllo se l'oggetto confrontato è nullo o di una classe diversa
        if (o == null || getClass() != o.getClass()) return false;
        
        VisitaModel that = (VisitaModel) o;

        // 3. Confronto i dati reali: Titolo del TipoVisita e stringa della Data
        // Usiamo i getter per sicurezza (gestione null)
        String titoloThis = (this.tipoVisita != null) ? this.tipoVisita.getTitolo() : "";
        String titoloThat = (that.getTipoVisita() != null) ? that.getTipoVisita().getTitolo() : "";
        
        String dataThis = (this.dataVisita != null) ? this.dataVisita.toString() : "";
        String dataThat = (that.getDataVisita() != null) ? that.getDataVisita().toString() : "";

        return Objects.equals(titoloThis, titoloThat) && 
               Objects.equals(dataThis, dataThat);
    }
}
