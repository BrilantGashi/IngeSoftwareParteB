package controller;

import model.StatoSistemaModel;
import view.StatoSistemaView;

public class StatoSistemaController {
	private StatoSistemaModel statoCorrente;
	private StatoSistemaView view;

	public StatoSistemaController(StatoSistemaModel statoCorrente, StatoSistemaView view) {
		this.statoCorrente = statoCorrente;
		this.view = view;
	}

	public StatoSistemaModel getStatoCorrente() {
		return statoCorrente;
	}

	public void mostraStato() {
		view.mostraStato(statoCorrente);
	}

	public void aggiornaStato(StatoSistemaModel nuovoStato) {
		this.statoCorrente = nuovoStato;
		view.mostraStato(nuovoStato);
	}

}
