package model;

import java.util.ArrayList;
import java.util.Objects;

public class ListaTipoVisitaModel {
    private ArrayList<TipoVisitaModel> tipi;

    public ListaTipoVisitaModel() {
        this.tipi = new ArrayList<>();
    }

    public void add(TipoVisitaModel tipoVisita) {
        Objects.requireNonNull(tipoVisita);
        if (!tipi.contains(tipoVisita)) {
            tipi.add(tipoVisita);
        }
    }

    public ArrayList<TipoVisitaModel> getTipi() {
        return new ArrayList<>(tipi);
    }

    public TipoVisitaModel getTipo(int i) {
        if (i < 0 || i >= tipi.size()) throw new IndexOutOfBoundsException("Indice non valido");
        return tipi.get(i);
    }

    public void setTipi(ArrayList<TipoVisitaModel> tipi) {
        Objects.requireNonNull(tipi);
        for (TipoVisitaModel t : tipi) {
            if (t == null) throw new IllegalArgumentException("La lista non può contenere elementi null");
        }
        this.tipi = new ArrayList<>(tipi);
    }

    public ListaTipoVisitaModel getTipiPerVolontario(String username) {
        Objects.requireNonNull(username);
        ListaTipoVisitaModel disponibili = new ListaTipoVisitaModel();
        for (TipoVisitaModel visita : tipi) {
            if (visita.volontarioTrovato(username) && visita.isAttivo()) {
                disponibili.add(visita);
            }
        }
        return disponibili;
    }

    public ListaTipoVisitaModel trovaTipiVisitaOrfani() {
        ListaTipoVisitaModel orfani = new ListaTipoVisitaModel();
        for (TipoVisitaModel tv : tipi) {
            if (tv.isAttivo() && !tv.isAttivo()) {
                orfani.add(tv);
            }
        }
        return orfani;
    }

    public TipoVisitaModel cercaPerTitolo(String titolo) {
        Objects.requireNonNull(titolo);
        for (TipoVisitaModel visita : tipi) {
            if (visita.getTitolo().equals(titolo) && visita.isAttivo()) {
                return visita;
            }
        }
        return null;
    }

    public boolean isAggiungibile(TipoVisitaModel visitaSelezionata, ListaTipoVisitaModel listaTipoVisitaToAdd) {
        for (TipoVisitaModel v : listaTipoVisitaToAdd.getTipi()) {
            if (v.equals(visitaSelezionata)) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(TipoVisitaModel tipo) {
        return tipi.contains(tipo);
    }

    public int size() {
        return tipi.size();
    }
    
    @Override
    public String toString() {
        if (tipi == null || tipi.isEmpty()) {
            return "Nessun tipo di visita disponibile.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Tipi Visita associati:\n");

        for (int i = 0; i < tipi.size(); i++) {
            TipoVisitaModel tv = tipi.get(i);
            sb.append("").append(i + 1).append(")");
            sb.append(tv != null ? tv.toStringSemplificato() : "Tipo visita non disponibile");
            sb.append("\n");
        }

        return sb.toString();
    }

}
