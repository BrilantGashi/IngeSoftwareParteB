package model.dao;

import model.IscrizioneModel;
import model.ListaFruitoreModel;
import model.ListaIscrizioniModel;
import model.ListaVisiteModel;
import model.FruitoreModel;
import model.VisitaModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IscrizioneDAOModel {

	public boolean inserisciIscrizione(IscrizioneModel iscrizione) throws SQLException {
	    // SQL per inserire l'iscrizione
	    String sql = "INSERT INTO Iscrizione (FK_FRUITORE, FK_VISITA, N_ISCRITTI, CODICE_PRENOTAZIONE) VALUES (?, ?, ?, ?)";
	    
	    // Recupera ID fruitore e visita usando DAO appropriati
	    int idFruitore = CredenzialiDAOModel.trovaIdUtenteDaNickname(iscrizione.getFruitore().getCredenziali().getUsername());
	    int idVisita = VisitaDAOModel.trovaIdVisita(iscrizione.getVisitaEffettiva());

	    // Verifica che gli ID siano validi
	    if (idFruitore > 0 && idVisita > 0) {
	        try (Connection conn = ConnessioneDBModel.startConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setInt(1, idFruitore);
	            ps.setInt(2, idVisita);
	            ps.setInt(3, iscrizione.getTotalePartecipanti());
	            ps.setString(4, iscrizione.getCodicePrenotazione());
	            int rows = ps.executeUpdate();
	            return rows > 0;
	        }
	    } else {
	        throw new SQLException("Impossibile inserire iscrizione: fruitore o visita non trovati.");
	    }
	}



	public static ListaIscrizioniModel leggiIscrizioniDaDB(ListaFruitoreModel listaFruitoreModel, ListaVisiteModel listaVisiteModel) throws SQLException {
	    ListaIscrizioniModel listaIscrizioni = new ListaIscrizioniModel();
	    
	    // --- STEP 1: PRE-CARICAMENTO (Mappatura) ---
	    // Creiamo delle mappe per associare l'ID del database all'oggetto Java.
	    // Questo evita di fare query dentro il ciclo while.
	    java.util.Map<Integer, FruitoreModel> mappaFruitori = new java.util.HashMap<>();
	    for (FruitoreModel f : listaFruitoreModel.getListaFruitore()) {
	        int id = CredenzialiDAOModel.trovaIdUtenteDaNickname(f.getCredenziali().getUsername());
	        if (id > 0) mappaFruitori.put(id, f);
	    }

	    java.util.Map<Integer, VisitaModel> mappaVisite = new java.util.HashMap<>();
	    for (VisitaModel v : listaVisiteModel.getListaVisite()) {
	        int id = VisitaDAOModel.trovaIdVisita(v);
	        if (id > 0) mappaVisite.put(id, v);
	    }

	    // --- STEP 2: QUERY PRINCIPALE ---
	    String sql = "SELECT FK_FRUITORE, FK_VISITA, N_ISCRITTI, CODICE_PRENOTAZIONE FROM Iscrizione";

	    try (Connection conn = ConnessioneDBModel.startConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            int idFruitore = rs.getInt("FK_FRUITORE");
	            int idVisita = rs.getInt("FK_VISITA");
	            int nIscritti = rs.getInt("N_ISCRITTI");
	            String codicePrenotazione = rs.getString("CODICE_PRENOTAZIONE");

	            // Ora cerchiamo nelle mappe in memoria, senza toccare il DB!
	            FruitoreModel fruitore = mappaFruitori.get(idFruitore);
	            VisitaModel visita = mappaVisite.get(idVisita);

	            if (fruitore != null && visita != null) {
	                IscrizioneModel iscrizione = new IscrizioneModel(visita, fruitore, nIscritti);
	                iscrizione.setCodicePrenotazione(codicePrenotazione);
	                listaIscrizioni.aggiungi(iscrizione);
	            }
	        }
	    } 
	    // Qui le risorse (conn, ps, rs) si chiudono automaticamente grazie al try-with-resources
	    return listaIscrizioni;
	}
 

	public boolean disdiciIscrizione(String codicePrenotazione) throws SQLException {
	    String sql = "DELETE FROM Iscrizione WHERE CODICE_PRENOTAZIONE = ?";
	    try (Connection conn = ConnessioneDBModel.startConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, codicePrenotazione);
	        int affectedRows = ps.executeUpdate();
	        return affectedRows > 0;
	    }
	}

    public int countIscrittiPerVisita(int visitaId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Iscrizione WHERE FK_VISITA = ?";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, visitaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int trovaIdVisitaPerCodicePrenotazione(String codicePrenotazione) throws SQLException {
        String sql = "SELECT FK_VISITA FROM Iscrizione WHERE CODICE_PRENOTAZIONE = ?";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codicePrenotazione);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    



}
