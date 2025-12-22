package controller;

import model.ListaTipoVisitaModel;
import model.TipoVisitaModel;
import view.ListaTipoVisitaView;

public class ListaTipoVisitaController {
    private ListaTipoVisitaModel model;
    private ListaTipoVisitaView view;

    public ListaTipoVisitaController(ListaTipoVisitaModel model, ListaTipoVisitaView view) {
        this.model = model;
        this.view = view;
    }

    public void aggiungiTipoVisita(TipoVisitaModel tipo) {
        if (model.contains(tipo)) {
            view.mostraMessaggio("Tipo visita già presente.");
        } else {
            model.add(tipo);
            view.mostraMessaggio("Tipo visita aggiunto.");
        }
    }

    public void mostraTipiAttiviNumerati() {
        view.stampaElencoNumerato(model);
    }

    public void mostraTipiDettagliati() {
        view.stampaListaDettagliata(model);
    }

    public TipoVisitaModel selezionaTipoVisita() {
        view.stampaElencoNumerato(model);
        int n = model.size();
        if (n == 0) {
            view.mostraMessaggio("Impossibile selezionare: nessun tipo visita disponibile.");
            return null;
        }
        int scelta = it.unibs.fp.mylib.InputDati.leggiIntero("Seleziona il numero del tipo visita: ", 1, n);
        return model.getTipo(scelta - 1);
    }
}
