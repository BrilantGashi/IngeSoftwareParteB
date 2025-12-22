package controller;

import model.IndirizzoModel;
import view.IndirizzoView;
import view.AmbitoTerritorialeView;

public class IndirizzoController {
	private final IndirizzoView indirizzoView;
	private final AmbitoTerritorialeView ambitoView;

	public IndirizzoController(IndirizzoView indirizzoView, AmbitoTerritorialeView ambitoView) {
		this.indirizzoView = indirizzoView;
		this.ambitoView = ambitoView;
	}

	/**
	 * Crea un nuovo IndirizzoModel raccogliendo i dati tramite la View e lo mostra.
	 * 
	 * @return l'indirizzo creato
	 */
	public IndirizzoModel creaIndirizzo() {
		IndirizzoModel indirizzo = null;
		do {
			try {
				indirizzo = indirizzoView.leggiIndirizzo(ambitoView);
			} catch (IllegalArgumentException e) {
				indirizzoView.mostraErrore(e.getMessage());
				indirizzo = null;
			}
		} while (indirizzo == null);
		indirizzoView.mostraIndirizzo(indirizzo);
		return indirizzo;
	}

}
