package controller;

import model.ListaLuogoModel;
import model.LuogoModel;
import view.ListaLuogoView;

public class ListaLuogoController {
	private ListaLuogoModel model;
	private ListaLuogoView view;

	public ListaLuogoController(ListaLuogoModel model, ListaLuogoView view) {
		this.model = model;
		this.view = view;
	}

	public void aggiungiLuogo(LuogoModel luogo) {
		try {
			model.aggiungiLuogo(luogo);
			view.mostraMessaggio("Luogo aggiunto correttamente.");
		} catch (Exception e) {
			view.mostraMessaggio("Errore: " + e.getMessage());
		}
	}

	public void rimuoviLuogo(LuogoModel luogo) {
		model.rimuovi(luogo);
		view.mostraMessaggio("Luogo rimosso.");
	}

	public void mostraLuoghiAttiviConVisite() {
		view.stampaLuoghiConTipoVisita(model);
	}

	public void mostraLuoghiAttiviSenzaVisite() {
		view.stampaLuoghiSenzaTipoVisita(model);
	}

	public void stampaElencoNumerato() {
		view.stampaElencoNumerato(model);
	}
}
