package controller;

import model.UtenteModel;
import view.UtenteView;

public class UtenteController {
    private UtenteModel model;
    private UtenteView view;

    public UtenteController(UtenteModel model, UtenteView view) {
        this.model = model;
        this.view = view;
    }

    public void mostraUtente() {
        view.mostraUtente(model);
    }

}
