package Versione1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
