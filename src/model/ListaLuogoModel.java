package model;

import java.util.ArrayList;
import java.util.Objects;

public class ListaLuogoModel {
    private ArrayList<LuogoModel> listaLuoghi;

    public ListaLuogoModel() {
        this.listaLuoghi = new ArrayList<>();
    }

    public ArrayList<LuogoModel> getListaLuoghi() {
        return new ArrayList<>(listaLuoghi);
    }
    
    public void setLuoghi(ArrayList<LuogoModel> nuoviLuoghi) {
        Objects.requireNonNull(nuoviLuoghi);
        this.listaLuoghi = new ArrayList<>(nuoviLuoghi);
    }

    public void aggiungiLuogo(LuogoModel luogo) {
        Objects.requireNonNull(luogo);
        if (contains(luogo)) {
            throw new IllegalArgumentException("Luogo già presente");
        }
        listaLuoghi.add(luogo);
    }

    public boolean contains(LuogoModel luogo) {
        return listaLuoghi.contains(luogo);
    }

    public void setListaLuoghi(ArrayList<LuogoModel> listaLuoghi) {
        Objects.requireNonNull(listaLuoghi);
        for (LuogoModel l : listaLuoghi) {
            if (l == null) throw new IllegalArgumentException("Elemento nulla");
        }
        this.listaLuoghi = new ArrayList<>(listaLuoghi);
    }

    public LuogoModel cercaPerNome(String titolo) {
        for (LuogoModel l : listaLuoghi) {
            if (l.getNomeLuogo().equals(titolo))
                return l;
        }
        return null;
    }

    public void rimuovi(LuogoModel luogo) {
        listaLuoghi.remove(luogo);
    }

    public LuogoModel trovaLuogoPerTipoVisita(TipoVisitaModel tipo) {
        Objects.requireNonNull(tipo);
        for (LuogoModel luogo : listaLuoghi) {
            if (luogo.getListaTipoVisita().getTipi().contains(tipo)) {
                return luogo;
            }
        }
        return null;
    }

    public ListaLuogoModel trovaLuoghiOrfani() {
        ListaLuogoModel orfani = new ListaLuogoModel();
        for (LuogoModel l : listaLuoghi) {
            if (l.isAttivo()) {
                boolean haTipoVisitaAttivo = l.getListaTipoVisita().getTipi()
                    .stream().anyMatch(TipoVisitaModel::isAttivo);
                if (!haTipoVisitaAttivo) {
                    orfani.aggiungiLuogo(l);
                }
            }
        }
        return orfani;
    }
    
    public ArrayList<LuogoModel> getLuoghiAttivi() {
        ArrayList<LuogoModel> attivi = new ArrayList<>();
        for (LuogoModel luogo : listaLuoghi) {
            if (luogo.isAttivo()) attivi.add(luogo);
        }
        return attivi;
    }

}
