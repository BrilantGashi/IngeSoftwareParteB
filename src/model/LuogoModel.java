package model;

import java.util.Objects;

public class LuogoModel {
    private String descrizione;
    private IndirizzoModel indirizzo;
    private ListaTipoVisitaModel listaTipoVisita;
    private String nomeLuogo;
    private boolean isAttivo;

    public LuogoModel() {}

    public LuogoModel(String nomeLuogo, String descrizione, IndirizzoModel indirizzo, ListaTipoVisitaModel listaTipoVisita) {
        if (nomeLuogo == null || nomeLuogo.isBlank()) throw new IllegalArgumentException("Nome luogo non valido");
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione non valida");
        Objects.requireNonNull(indirizzo, "Indirizzo non può essere null");
        if (listaTipoVisita == null || listaTipoVisita.getTipi().isEmpty())
            throw new IllegalArgumentException("Ogni luogo deve avere almeno un tipo di visita");
        this.nomeLuogo = nomeLuogo;
        this.descrizione = descrizione;
        this.indirizzo = indirizzo;
        this.listaTipoVisita = listaTipoVisita;
        this.isAttivo = true;
    }

    public String getDescrizione() { return descrizione; }
    public IndirizzoModel getIndirizzo() { return indirizzo; }
    public ListaTipoVisitaModel getListaTipoVisita() { return listaTipoVisita; }
    public String getNomeLuogo() { return nomeLuogo; }
    public boolean isAttivo() { return isAttivo; }

    public void setDescrizione(String descrizione) {
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione non valida");
        this.descrizione = descrizione;
    }

    public void setIndirizzo(IndirizzoModel indirizzo) {
        Objects.requireNonNull(indirizzo);
        this.indirizzo = indirizzo;
    }

    public void setListaTipoVisita(ListaTipoVisitaModel listaTipoVisita) {
        if (listaTipoVisita == null || listaTipoVisita.getTipi().isEmpty())
            throw new IllegalArgumentException("Ogni luogo deve avere almeno un tipo di visita");
        this.listaTipoVisita = listaTipoVisita;
    }

    public void setNomeLuogo(String nomeLuogo) {
        if (nomeLuogo == null || nomeLuogo.isBlank()) throw new IllegalArgumentException("Nome luogo non valido");
        this.nomeLuogo = nomeLuogo;
    }

    public void setAttivo(boolean attivo) {
        this.isAttivo = attivo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== LUOGO =====\n");
        sb.append("Nome: ").append(nomeLuogo != null ? nomeLuogo : "N/D");
        sb.append("Descrizione: ").append(descrizione != null ? descrizione : "N/D").append("\n");

        if (indirizzo != null) {
            sb.append("Indirizzo: ").append(indirizzo.toString()).append("\n");
        } else {
            sb.append("Indirizzo: non disponibile\n");
        }
        if (listaTipoVisita != null && !listaTipoVisita.getTipi().isEmpty()) {
            sb.append(listaTipoVisita.toString()).append("\n");
        } else {
            sb.append("Nessun tipo di visita disponibile.\n");
        }
        sb.append("=================\n");
        return sb.toString();
    }
}
	