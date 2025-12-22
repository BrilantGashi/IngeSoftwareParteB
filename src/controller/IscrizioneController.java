package controller;

import model.IscrizioneModel;
import model.dao.IscrizioneDAOModel;
import view.IscrizioneView;

import java.sql.SQLException;
import java.util.List;

public class IscrizioneController {
    private IscrizioneDAOModel dao;
    private IscrizioneView view;

    public IscrizioneController(IscrizioneDAOModel dao, IscrizioneView view) {
        this.dao = dao;
        this.view = view;
    }

    public void inserisciIscrizione(IscrizioneModel iscrizione) {
        try {
            dao.inserisciIscrizione(iscrizione);
            view.mostraMessaggio("Iscrizione inserita con successo!");
        } catch (SQLException e) {
            view.mostraMessaggio("Errore inserimento: " + e.getMessage());
        }
    }

    public void mostraIscrizioni(List<IscrizioneModel> iscrizioni) {
        view.mostraListaIscrizioni(iscrizioni);
    }

    public void disdiciIscrizione(String codicePrenotazione) {
        try {
            boolean ok = dao.disdiciIscrizione(codicePrenotazione);
            if (ok) view.mostraMessaggio("Iscrizione annullata.");
            else view.mostraMessaggio("Codice prenotazione non trovato.");
        } catch (SQLException e) {
            view.mostraMessaggio("Errore durante annullamento iscrizione: " + e.getMessage());
        }
    }
}
