package Versione1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DisponibilitaDAO {
    
    /*
     * Classe per la gestione della disponibilità dei volontari, permette
     * di salvare e leggere i giorni in cui un volontario è disponibile    
     */

    /**
     * Salva una disponibilità nel database.
     * @param username Il nome utente del volontario
     * @param giorno Il giorno (1-31)
     * @param mese Il mese (1=i+1, 2=i+2, 3=i+3)
     */
	public void salvaDisponibilita(Disponibilita disponibilita) throws SQLException {
	    // Prima trovi l'ID (PK) del volontario nella tabella credenziali
	    String queryId = "SELECT ID FROM credenziali WHERE USERNAME = ? AND ID_TIPOUTENTE = 2";
	    int idVolontario = -1;

	    try (Connection conn = ConnessioneDB.startConnection();
	         PreparedStatement psId = conn.prepareStatement(queryId)) {
	        psId.setString(1, disponibilita.getUsername());
	        try (ResultSet rs = psId.executeQuery()) {
	            if (rs.next()) {
	                idVolontario = rs.getInt("ID");
	            } else {
	                System.out.println("Volontario non trovato per username: " + disponibilita.getUsername());
	                throw new SQLException("Volontario non trovato");
	            }
	        }
	        
	        // Poi inserisci la disponibilità
	        String queryIns = "INSERT INTO disponibilita (FK_VOLONTARIO, GIORNO, MESE) VALUES (?, ?, ?)";
	        try (PreparedStatement psIns = conn.prepareStatement(queryIns)) {
	            psIns.setInt(1, idVolontario);
	            psIns.setInt(2, disponibilita.getGiorno());
	            psIns.setInt(3, disponibilita.getMese());
	            psIns.executeUpdate();
	        }
	        
	        System.out.println("Disponibilità inserita con successo per volontario ID " + idVolontario);
	    } catch (SQLException e) {
	        System.out.println("Errore nell'inserimento disponibilità: " + e.getMessage());
	        throw e;
	    }
	}



    
    /**
     * Legge tutte le disponibilità di un volontario.
     * @param username Il nome utente del volontario
     * @return Lista delle disponibilità
     */
    public  ArrayList<Disponibilita> leggiDisponibilitaVolontario(String username) throws SQLException {
        
        ArrayList<Disponibilita> listaDisponibilita = new ArrayList<>();
        
        // Prende tutte le righe della tabella disponibilità dove
        // lo username è uguale a quello passato come parametro
        String query = 
            "SELECT d.giorno, d.mese " +
            "FROM disponibilita d " +
            "INNER JOIN credenziali c ON d.fk_volontario = c.ID " +
            "WHERE c.USERNAME = ?";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            // Sostituisce i ? con lo username
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int giorno = rs.getInt("giorno");
                int mese = rs.getInt("mese"); //  Cambiato da getString a getInt
                
                Disponibilita disponibilita = new Disponibilita(username, giorno, mese);
                listaDisponibilita.add(disponibilita);
            }
            
        } catch (SQLException e) {
            System.out.println("Errore durante la lettura delle disponibilità: " + e.getMessage());
            throw e;
        }
        
        return listaDisponibilita;
    }
    
    /**
     * Stampa le disponibilità di un volontario.
     * @param username Il nome utente del volontario
     */
    public void stampaDisponibilitaVolontario(String username) throws SQLException {
        ArrayList<Disponibilita> listaDisponibilita = leggiDisponibilitaVolontario(username);
        
        if (listaDisponibilita.isEmpty()) {
            System.out.println("Nessuna disponibilità trovata per " + username);
        } else {
            System.out.println("===== DISPONIBILITÀ DI " + username.toUpperCase() + " =====");
            for (Disponibilita d : listaDisponibilita) {
                System.out.println("- Giorno " + d.getGiorno() + " del mese i+" + d.getMese()); // ✅ Mostra i+1, i+2, i+3
            }
            System.out.println("==========================================");
        }
    }
}
