package view;

import model.LuogoModel;
import model.ListaLuogoModel;
import model.TipoVisitaModel;

import java.util.List;

import it.unibs.fp.mylib.InputDati;

public class ListaLuogoView {
	
    public void stampaLuoghiConTipoVisita(ListaLuogoModel model) {
        List<LuogoModel> luoghi = model.getListaLuoghi();
        System.out.println("Elenco luoghi:");
        for (LuogoModel l : luoghi) {
            if (l.isAttivo()) {
                System.out.println("Nome: " + l.getNomeLuogo());
                System.out.println("Descrizione: " + l.getDescrizione());
                System.out.println("Indirizzo: " + l.getIndirizzo());
                System.out.println("Tipi visita associati:");
                for (TipoVisitaModel tv : l.getListaTipoVisita().getTipi()) {
                    System.out.println("\tTitolo: " + tv.getTitolo());
                    System.out.println("\tDescrizione: " + tv.getDescrizione());
                }
                System.out.println("--------------------------------");
            }
        }
    }

    public void stampaLuoghiSenzaTipoVisita(ListaLuogoModel model) {
        List<LuogoModel> luoghi = model.getListaLuoghi();
        System.out.println("Elenco luoghi senza tipo visita:");
        for (LuogoModel l : luoghi) {
            if (l.isAttivo()) {
                System.out.println("Nome: " + l.getNomeLuogo());
                System.out.println("Descrizione: " + l.getDescrizione());
                System.out.println("Indirizzo: " + l.getIndirizzo());
                System.out.println("--------------------------------");
            }
        }
    }

    public void stampaElencoNumerato(ListaLuogoModel model) {
        List<LuogoModel> luoghi = model.getListaLuoghi();
        for (int i = 0; i < luoghi.size(); i++) {
            System.out.println((i + 1) + ") " + luoghi.get(i).getNomeLuogo());
        }
    }
    
    public void stampaElencoNumerato(List<LuogoModel> luoghi) {
        for (int i = 0; i < luoghi.size(); i++) {
            System.out.println((i + 1) + ") " + luoghi.get(i).getNomeLuogo());
        }
    }

    public int scegliNumeroElementi(String messaggio, int max) {
        return InputDati.leggiIntero(messaggio, 1, max);
    }


    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
}
