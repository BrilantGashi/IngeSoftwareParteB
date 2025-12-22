package controller;

import java.time.LocalTime;

import view.OrarioView;

public class OrarioController {
    private OrarioView view;

    public OrarioController(OrarioView view) {
        this.view = view;
    }

    public LocalTime chiediOrario() {
        LocalTime orario = null;
        do {
            try {
                int ora = view.leggiOra();
                int minuti = view.leggiMinuti();
                orario = LocalTime.of(ora, minuti);
            } catch (Exception e) {
                view.mostraErrore(e.getMessage());
                orario = null;
            }
        } while (orario == null);
        return orario;
    }
}
