package controller;

import model.ListaIscrizioniModel;
import model.IscrizioneModel;
import model.dao.IscrizioneDAOModel;
import view.ListaIscrizioniView;

import java.sql.SQLException;
import java.util.List;

public class ListaIscrizioniController {
	private ListaIscrizioniModel model;
	private IscrizioneDAOModel dao;
	private ListaIscrizioniView view;

	public ListaIscrizioniController(ListaIscrizioniModel model, IscrizioneDAOModel dao, ListaIscrizioniView view) {
		this.model = model;
		this.dao = dao;
		this.view = view;
	}

	public void mostraIscrizioniFruitore(String usernameFruitore) {
		List<IscrizioneModel> tutteIscrizioni = model.getIscrizioni();
		view.mostraIscrizioni(tutteIscrizioni.stream()
				.filter(i -> i.getFruitore().getCredenziali().getUsername().equals(usernameFruitore)).toList());
	}

	public void aggiungiIscrizione(IscrizioneModel iscrizione) {
		try {
			boolean ok = dao.inserisciIscrizione(iscrizione);
			if (ok) {
				model.aggiungi(iscrizione);
				view.mostraMessaggio("Iscrizione aggiunta.");
			} else {
				view.mostraMessaggio("Errore nel DB, iscrizione non aggiunta.");
			}
		} catch (SQLException e) {
			view.mostraMessaggio("Errore DB: " + e.getMessage());
		}
	}

	public void disdiciIscrizione(String codicePrenotazione) {
		try {
			boolean ok = dao.disdiciIscrizione(codicePrenotazione);
			if (ok && model.rimuoviPerCodicePrenotazione(codicePrenotazione)) {
				view.mostraMessaggio("Iscrizione rimossa.");
			} else {
				view.mostraMessaggio("Codice prenotazione non trovato o impossibile rimuovere.");
			}
		} catch (SQLException e) {
			view.mostraMessaggio("Errore DB: " + e.getMessage());
		}
	}
}
