package controller;

import java.util.ArrayList;

import model.FruitoreModel;
import model.ListaIscrizioniModel;
import model.ListaVisiteModel;
import view.ListaVisiteView;
import model.VisitaModel;

public class ListaVisiteController {
	private ListaVisiteModel model;
	private ListaVisiteView view;

	public ListaVisiteController(ListaVisiteModel model, ListaVisiteView view) {
		this.model = model;
		this.view = view;
	}

	public void aggiungiVisita(VisitaModel visita) {
		if (!model.contains(visita)) {
			model.aggiungiVisita(visita);
			view.mostraMessaggio("Visita aggiunta.");
		} else {
			view.mostraMessaggio("Visita già presente.");
		}
	}

	public void rimuoviVisita(VisitaModel visita) {
		if (model.contains(visita)) {
			model.rimuoviVisita(visita);
			view.mostraMessaggio("Visita rimossa.");
		} else {
			view.mostraMessaggio("Visita non trovata.");
		}
	}

	public VisitaModel selezionaVisitaProposta(String meseNome, FruitoreModel fruitore,
			ListaIscrizioniModel listaIscrizioni) {

		ArrayList<VisitaModel> visiteDisponibili = model.getVisiteProposteDisponibili(meseNome, fruitore,
				listaIscrizioni);
		view.mostraVisiteDisponibiliConPosti(visiteDisponibili, listaIscrizioni);

		if (visiteDisponibili == null || visiteDisponibili.isEmpty()) {
			return null;
		}

		int sceltaIdx = view.chiediSelezioneVisita(visiteDisponibili.size());
		if (sceltaIdx < 0 || sceltaIdx >= visiteDisponibili.size()) {
			return null;
		}
		return visiteDisponibili.get(sceltaIdx);
	}

	public void mostraTutte() {
		view.mostraVisite(model.getListaVisite());
	}

	public void mostraPianoVisite() {
		view.stampaPianoVisite(model);
	}
}
