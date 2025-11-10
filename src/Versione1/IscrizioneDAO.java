package Versione1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IscrizioneDAO {

		public void inserisciIscrizione(Iscrizione iscrizione) throws SQLException {
	        String sql = "INSERT INTO Iscrizione (FK_FRUITORE, FK_VISITA, N_ISCRITTI, CODICE_PRENOTAZIONE) VALUES (?, ? , ?, ?)";
		    int idFruitore = UtenteDAO.trovaIdFruitoreDaNickname(iscrizione.getFruitore().getCredenziali().getUsername());
		    int idVisita = VisitaDAO.trovaIdVisita(iscrizione.getVisitaEffettiva());
	        if (idFruitore > 0 && idVisita > 0) {
	    	    try (Connection conn = ConnessioneDB.startConnection();
	    		         PreparedStatement ps = conn.prepareStatement(sql)) {
	                ps.setInt(1, idFruitore);
	                ps.setInt(2, idVisita);
	                ps.setInt(3, iscrizione.getTotalePartecipanti());
	                ps.setString(4, iscrizione.getCodicePrenotazione());
	                ps.executeUpdate();
	            }
	        } else {
	            // Errore: fruitore o visita non trovati
	            throw new SQLException("Impossibile inserire iscrizione: fruitore o visita non trovati.");
	        }
	    }


		public static ListaIscrizioni leggiIscrizioniDaDB(ListaIscrizioni listaIscrizioniFruitore, ArrayList<Fruitore> listaFruitori, ListaVisite listaVisite) throws SQLException {
		    String sql = "SELECT FK_FRUITORE, FK_VISITA, N_ISCRITTI, CODICE_PRENOTAZIONE FROM Iscrizione";
		    try (Connection conn = ConnessioneDB.startConnection();
		         PreparedStatement ps = conn.prepareStatement(sql);
		         ResultSet rs = ps.executeQuery()) {
		        while (rs.next()) {
		            int idFruitore = rs.getInt("FK_FRUITORE");
		            int idVisita = rs.getInt("FK_VISITA");
		            int nIscritti = rs.getInt("N_ISCRITTI");
		            String codicePrenotazione = rs.getString("CODICE_PRENOTAZIONE");
		            
		            String usernameFruitore = UtenteDAO.trovaUsernameFruitoreDaId(idFruitore);
		            Fruitore fruitore = null;
		            for(Fruitore f : listaFruitori){
		                if(f.getCredenziali().getUsername().equals(usernameFruitore)) {
		                    fruitore = f;
		                    break;
		                }
		            }
		            // solo se Fruitore trovato + Visita trovata
		            if (fruitore != null) {
		                Visita visita = VisitaDAO.trovaVisitaDaId(idVisita, listaVisite);
		                if (visita != null) {
		                    Iscrizione iscrizione = new Iscrizione(visita, fruitore, nIscritti);
		                    iscrizione.setCodicePrenotazione(codicePrenotazione);
		                    listaIscrizioniFruitore.aggiungi(iscrizione);
		                }
		            }
		        }
		    }
		    return listaIscrizioniFruitore;
		}
		
		public boolean disdiciIscrizione(String codicePrenotazione) throws SQLException {
		    String sql = "DELETE FROM Iscrizione WHERE CODICE_PRENOTAZIONE = ?";
		    try (Connection conn = ConnessioneDB.startConnection();
		         PreparedStatement ps = conn.prepareStatement(sql)) {
		        ps.setString(1, codicePrenotazione);
		        int affectedRows = ps.executeUpdate();
		        return affectedRows > 0;
		    }
		}
		
		public int countIscrittiPerVisita(int visitaId) throws SQLException {
		    String sql = "SELECT COUNT(*) FROM Iscrizione WHERE ID_VISITA = ?";
		    try (Connection conn = ConnessioneDB.startConnection();
		         PreparedStatement ps = conn.prepareStatement(sql)) {
		        ps.setLong(1, visitaId);
		        try (ResultSet rs = ps.executeQuery()) {
		            if (rs.next()) {
		                return rs.getInt(1);
		            }
		            return 0;
		        }
		    }
		}

		public int trovaIdVisitaPerCodicePrenotazione(String codicePrenotazione) throws SQLException {
		    String sql = "SELECT ID_VISITA FROM Iscrizione WHERE CODICE_PRENOTAZIONE = ?";
		    try (Connection conn = ConnessioneDB.startConnection();
		         PreparedStatement ps = conn.prepareStatement(sql)) {
		        ps.setString(1, codicePrenotazione);
		        try (ResultSet rs = ps.executeQuery()) {
		            if (rs.next()) return rs.getInt(1);
		            return 0;
		        }
		    }
		}

}
