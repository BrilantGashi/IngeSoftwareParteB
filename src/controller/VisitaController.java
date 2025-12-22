package controller;

import model.VisitaModel;
import model.dao.VisitaDAOModel;
import view.VisitaView;

import java.util.List;

public class VisitaController {
    private VisitaDAOModel dao;
    private VisitaView view;

    public VisitaController(VisitaDAOModel dao, VisitaView view) {
        this.dao = dao;
        this.view = view;
    }

    public void aggiungiVisita(VisitaModel visita, model.VolontarioModel volontario) {
        try {
            dao.salvaVisita(visita, volontario);
            view.mostraMessaggio("Visita inserita con successo.");
        } catch (Exception e) {
            view.mostraMessaggio("Errore nell'inserimento: " + e.getMessage());
        }
    }

    public void mostraVisite(List<VisitaModel> visite) {
        view.mostraListaVisite(visite);
    }

    public void mostraVisita(VisitaModel visita) {
        view.mostraVisita(visita);
    }
}
