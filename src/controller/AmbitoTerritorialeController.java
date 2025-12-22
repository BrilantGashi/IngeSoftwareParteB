package controller;

import model.AmbitoTerritorialeModel;
import model.dao.AmbitoTerritorialeDAOModel;
import view.AmbitoTerritorialeView;
import java.sql.SQLException;

public class AmbitoTerritorialeController {
	private AmbitoTerritorialeView view;

	public AmbitoTerritorialeController(AmbitoTerritorialeModel model,AmbitoTerritorialeDAOModel dao, AmbitoTerritorialeView view) {
		this.view = view;
	}

	public void inserisciAmbito() {
		// Input da View e creazione locale
		AmbitoTerritorialeModel ambito = view.leggiDatiAmbito();
		try {
			int id = AmbitoTerritorialeDAOModel.salvaAmbitoTerritoriale(ambito);
			view.mostraTesto("Ambito inserito con ID: " + id);
		} catch (SQLException e) {
			view.mostraErrore("Errore inserimento DB: " + e.getMessage());
		}
	}


}
