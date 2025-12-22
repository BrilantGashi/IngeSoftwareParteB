package controller;

import model.DisponibilitaModel;
import model.dao.DisponibilitaDAOModel;
import view.DisponibilitaView;

import java.sql.SQLException;
import java.util.ArrayList;

public class DisponibilitaController {
    private DisponibilitaDAOModel dao;
    private DisponibilitaView view;

    public DisponibilitaController(DisponibilitaDAOModel dao, DisponibilitaView view) {
        this.dao = dao;
        this.view = view;
    }

    public void mostraDisponibilitaVolontario(String username) {
        try {
            ArrayList<DisponibilitaModel> lista = dao.leggiDisponibilitaVolontario(username);
            view.mostraListaDisponibilita(lista);
        } catch (SQLException e) {
            view.mostraMessaggio("Errore durante la lettura delle disponibilità: " + e.getMessage());
        }
    }
}
