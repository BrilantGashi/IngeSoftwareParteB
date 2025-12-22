package controller;

import model.CalendarioModel;
import model.dao.CalendarioDAOModel;
import view.CalendarioView;
import model.TipoVisitaModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class CalendarioController {
	private CalendarioModel model;
	private CalendarioDAOModel dao;
	private CalendarioView view;

	public CalendarioController(CalendarioModel model, CalendarioDAOModel dao, CalendarioView view) {
		this.model = model;
		this.dao = dao;
		this.view = view;
	}

	// Flusso per scelta data visitabile
	public void scegliDataPerTipo(TipoVisitaModel tipoVisita, int mese) {
		// LOGICA: computa giorni disponibili
		ArrayList<Integer> disponibili = new ArrayList<>();
		String nomeMese = model.getNomeMeseI(mese);
		int meseNumero = model.getMeseNumero(nomeMese);
		int giorniNelMese = model.giorniDelMese(meseNumero);

		for (int giorno = 1; giorno <= giorniNelMese; giorno++) {
			if (!model.checkData(giorno, mese)) {
				disponibili.add(giorno);
			}
		}

		// VIEW: mostra giorni disponibili e raccoglie scelta
		view.mostraDateDisponibili(disponibili, meseNumero, nomeMese, tipoVisita.getTitolo());
		int scelta = view.chiediSceltaDataDisponibile(disponibili.size());

		if (scelta == -1) {
			view.mostraTesto("Operazione annullata (nessuna scelta disponibile)");
			return;
		}
		int giornoScelto = disponibili.get(scelta - 1);

		// MODEL: aggiorna stato del calendario
		model.aggiungiDataPreclusa(giornoScelto, mese);

		// VIEW: conferma all’utente
		view.mostraConfermaScelta(giornoScelto, meseNumero);

		// DAO: salva nel DB
		try {
			dao.salvaDataPreclusa(giornoScelto, meseNumero);
		} catch (SQLException e) {
			view.mostraErrore("Errore salvataggio DB: " + e.getMessage());
		}
	}

	// Altri metodi: carica/salva calendario, visualizza date escluse, ecc.
}
