package controller;

import model.TipoVisitaModel;
import model.dao.TipoVisitaDAOModel;
import view.TipoVisitaView;
import java.util.List;

public class TipoVisitaController {

    private TipoVisitaDAOModel dao;
    private TipoVisitaView view;

    public TipoVisitaController(TipoVisitaDAOModel dao, TipoVisitaView view) {
        this.dao = dao;
        this.view = view;
    }

    public void aggiungiTipoVisita(TipoVisitaModel tipoVisita) {
        try {
            dao.inserisciVisita(tipoVisita);
            view.mostraMessaggio("Tipo visita aggiunto con successo.");
        } catch (Exception e) {
            view.mostraMessaggio("Errore nell'inserimento: " + e.getMessage());
        }
    }

    public void mostraTipiVisita(List<TipoVisitaModel> tipi) {
        view.mostraListaTipiVisita(tipi);
    }

    public void mostraTipoVisita(TipoVisitaModel tipoVisita) {
        view.mostraTipoVisita(tipoVisita);
    }
}
