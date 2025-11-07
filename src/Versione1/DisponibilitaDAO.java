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
    public void salvaDisponibilita(String username, int giorno, int mese) throws SQLException {
        
        // Controlla se esiste già una disponibilità
        String checkSql = "SELECT COUNT(*) FROM disponibilita WHERE username = ? AND giorno = ? AND mese = ?";
        
        // Inserisce nuova disponibilità dopo aver verificato che non esiste già
        String insertSql = "INSERT INTO disponibilita (username, giorno, mese) VALUES (?, ?, ?)";
    
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql);
             PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
            
            // Sostituisce i ? con i valori
            checkPs.setString(1, username);
            checkPs.setInt(2, giorno);
            checkPs.setInt(3, mese); // ✅ Cambiato da setString a setInt
             
            // Esegue la query e restituisce il risultato
            ResultSet rs = checkPs.executeQuery();
            
            // Sposta il cursore alla prima riga e prende la prima colonna (count)
            rs.next();
            int count = rs.getInt(1);
            
            // Inserisce la nuova disponibilità solo se non esiste
            if (count == 0) {
                insertPs.setString(1, username);
                insertPs.setInt(2, giorno);
                insertPs.setInt(3, mese); // ✅ Cambiato da setString a setInt
                
                insertPs.executeUpdate();
                System.out.println("✓ Disponibilità salvata per " + username + " (giorno " + giorno + ", mese i+" + mese + ")");
            } else {
                System.out.println("⚠️  La disponibilità per " + username + " nel mese i+" + mese + " esiste già.");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Errore durante il salvataggio della disponibilità: " + e.getMessage());
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
            System.out.println("❌ Errore durante la lettura delle disponibilità: " + e.getMessage());
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
