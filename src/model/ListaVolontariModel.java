package model;

import java.util.ArrayList;
import java.util.Objects;

public class ListaVolontariModel {
    private ArrayList<VolontarioModel> volontari;

    public ListaVolontariModel() {
        this.volontari = new ArrayList<>();
    }

    public void addVolontario(VolontarioModel v) {
        Objects.requireNonNull(v);
        if (v.getNickname() == null || v.getNickname().isBlank()) {
            throw new IllegalArgumentException("Nickname volontario non valido");
        }
        volontari.add(v);
    }

    public void aggiungiVolontario(VolontarioModel v) {
        Objects.requireNonNull(v);
        if (v.getNickname() == null || v.getNickname().isBlank()) {
            throw new IllegalArgumentException("Nickname volontario non valido");
        }
        if (!volontari.contains(v)) {
            volontari.add(v);
        }
    }

   

    public VolontarioModel cercaPerNickname(String nickname) {
        Objects.requireNonNull(nickname);
        for (VolontarioModel v : volontari) {
            if (v.getNickname().equalsIgnoreCase(nickname)) {
                return v;
            }
        }
        return null;
    }

    public boolean contienePerNickname(String nickname) {
        Objects.requireNonNull(nickname);
        for (VolontarioModel v : volontari) {
            if (v.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<VolontarioModel> getVolontari() {
        return this.volontari;
    }

    public VolontarioModel getVolontario(int indice) {
        if (indice < 0 || indice >= volontari.size()) {
            throw new IndexOutOfBoundsException("Indice non valido");
        }
        return volontari.get(indice);
    }

    public ListaVolontariModel trovaVolontariSenzaVisite(ListaTipoVisitaModel listaTipoVisita) {
        Objects.requireNonNull(listaTipoVisita);
        ListaVolontariModel orfani = new ListaVolontariModel();
        for (VolontarioModel v : volontari) {
            boolean associato = false;
            for (TipoVisitaModel visita : listaTipoVisita.getTipi()) {
                if (visita.getListaVolontari().getVolontari().contains(v)) {
                    associato = true;
                    break;
                }
            }
            if (!associato) {
                orfani.aggiungiVolontario(v);
            }
        }
        return orfani;
    }
    
    public void rimuoviVolontario(VolontarioModel v) {
        Objects.requireNonNull(v, "Il volontario non può essere null");
        v.setAttivo(false);
        assert !v.isAttivo() : "Postcondizione violata in rimuoviVolontario";
        // Non rimuove dalla lista, solo status
    }


    public int size() {
        return volontari.size();
    }
}
