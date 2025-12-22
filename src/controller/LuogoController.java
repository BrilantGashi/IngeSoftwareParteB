package controller;

import model.LuogoModel;
import model.ListaLuogoModel;
import view.LuogoView;


public class LuogoController {
	private ListaLuogoModel model;
	private LuogoView view;

	public LuogoController(ListaLuogoModel model, LuogoView view) {
		this.model = model;
		this.view = view;
	}

	public void aggiungiLuogo(LuogoModel luogo) {
		try {
			model.aggiungiLuogo(luogo);
			view.mostraMessaggio("Luogo aggiunto con successo!");
		} catch (Exception e) {
			view.mostraMessaggio("Errore: " + e.getMessage());
		}
	}

	public void rimuoviLuogo(LuogoModel luogo) {
		model.rimuovi(luogo);
		view.mostraMessaggio("Luogo rimosso.");
	}

	public void mostraLuoghi() {
		view.mostraLuoghi(model); // model è di tipo ListaLuogoModel
	}

	public void mostraLuogo(LuogoModel luogo) {
		view.mostraLuogo(luogo);
	}
}
