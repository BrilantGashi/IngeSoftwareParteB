package controller;

import model.ListaVolontariModel;
import model.VolontarioModel;
import view.ListaVolontariView;
import model.ListaTipoVisitaModel;

public class ListaVolontariController {
    private ListaVolontariModel model;
    private ListaVolontariView view;

    public ListaVolontariController(ListaVolontariModel model, ListaVolontariView view) {
        this.model = model;
        this.view = view;
    }

    public void aggiungiVolontario(VolontarioModel v) {
        if (!model.contienePerNickname(v.getNickname())) {
            model.aggiungiVolontario(v);
            view.mostraMessaggio("Volontario aggiunto.");
        } else {
            view.mostraMessaggio("Volontario già presente.");
        }
    }

    public void rimuoviVolontario(VolontarioModel v) {
        model.rimuoviVolontario(v);
        view.mostraMessaggio("Volontario disattivato.");
    }

    public void mostraVolontari() {
        view.stampaVolontari(model);
    }

    public void mostraVolontariConVisite(ListaTipoVisitaModel tutteLeVisite) {
        view.stampaVolontariConVisite(model, tutteLeVisite);
    }

    public void visualizzaVolontariAttivi() {
        view.visualizzaVolontari(model);
    }
    
    

}
