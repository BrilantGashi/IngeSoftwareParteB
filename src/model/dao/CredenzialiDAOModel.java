package model.dao;

import model.CredenzialiModel;
import model.FruitoreModel;
import model.ListaFruitoreModel;
import model.ListaVolontariModel;
import model.VolontarioModel;

import java.sql.*;


public class CredenzialiDAOModel {

    public CredenzialiModel leggiSingolaCredenziale(String username) throws SQLException {
        String query = "SELECT credenziali.USERNAME, credenziali.PASSWORD, tipologiautente.TIPO " +
                "FROM credenziali " +
                "JOIN tipologiautente ON credenziali.ID_TIPOUTENTE = tipologiautente.ID " +
                "WHERE credenziali.USERNAME = ? AND credenziali.ISATTIVO = true";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String utenza = rs.getString("TIPO");
                    String user = rs.getString("USERNAME");
                    String pass = rs.getString("PASSWORD");
                    return new CredenzialiModel(utenza != null ? utenza.trim() : "", 
                                                user != null ? user.trim() : "",
                                                pass != null ? pass.trim() : "");
                }
            }
        }
        return null;
    }
    
    public static void  leggiVolontariDaDB(ListaVolontariModel listaVolontariModel) throws SQLException {
        String sql = "SELECT c.USERNAME, c.PASSWORD, tu.TIPO " +
                     "FROM credenziali c " +
                     "JOIN tipologiautente tu ON c.ID_TIPOUTENTE = tu.ID " +
                     "WHERE tu.TIPO = 'Volontario' AND c.ISATTIVO = true";

        Connection conn = ConnessioneDBModel.startConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String tipo = rs.getString("TIPO");

                // Crea le credenziali con il costruttore a 3 parametri
                CredenzialiModel credenziali = new CredenzialiModel(tipo, username, password);
                
                // Crea il volontario
                VolontarioModel volontario = new VolontarioModel(credenziali);
                volontario.setAttivo(true);

                // Aggiungi alla lista globale
                listaVolontariModel.aggiungiVolontario(volontario);
            }
        }
    }

    public static void leggiFruitoriDaDB(ListaFruitoreModel listaFruitoreModel) throws SQLException {
        String sql = "SELECT c.USERNAME, c.PASSWORD, tu.TIPO " +
                     "FROM credenziali c " +
                     "JOIN tipologiautente tu ON c.ID_TIPOUTENTE = tu.ID " +
                     "WHERE tu.TIPO = 'Fruitore' AND c.ISATTIVO = true";

        Connection conn = ConnessioneDBModel.startConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String tipo = rs.getString("TIPO");

                // Crea le credenziali con il costruttore a 3 parametri
                CredenzialiModel credenziali = new CredenzialiModel(tipo, username, password);
                
                // Crea il fruitore
                FruitoreModel fruitore = new FruitoreModel(credenziali);

                // Aggiungi alla lista globale
                listaFruitoreModel.aggiungiFruitore(fruitore);
            }
        }
    }

    public static void salvaNuovoVolontario(VolontarioModel nuovoVolontario) throws SQLException {
        // La query inserisce nelle credenziali. 
        // Nota: ID_TIPOUTENTE deve corrispondere all'ID di 'Volontario' nella tua tabella tipologiautente
        String sql = "INSERT INTO credenziali (USERNAME, PASSWORD, ID_TIPOUTENTE, ISATTIVO) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuovoVolontario.getNickname());
            ps.setString(2, nuovoVolontario.getCredenziali().getPassword());
            ps.setInt(3, 2); // Assumendo che 2 sia l'ID per 'Volontario'
            ps.setBoolean(4, true);

            ps.executeUpdate();
            System.out.println("Nuovo volontario salvato nel DB: " + nuovoVolontario.getNickname());
        } catch (SQLException e) {
            System.err.println("Errore salvataggio volontario: " + e.getMessage());
            throw e;
        }
    }
    
    public static void aggiungiNuovoFruitore(FruitoreModel nuovoFruitore) throws SQLException {
        String sql = "INSERT INTO credenziali (USERNAME, PASSWORD, ID_TIPOUTENTE, ISATTIVO) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuovoFruitore.getCredenziali().getUsername());
            ps.setString(2, nuovoFruitore.getCredenziali().getPassword());
            ps.setInt(3, 3); // 3 = fruitore (come da mappatura dei tuoi ID_TIPOUTENTE)
            ps.setBoolean(4, true); // ISATTIVO = true

            ps.executeUpdate();

            System.out.println("Nuovo fruitore salvato: " + nuovoFruitore.getCredenziali().getUsername());
        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento del nuovo fruitore: " + e.getMessage());
            throw e;
        }
    }
    
    public boolean aggiornaPasswordSuDB(String username, String nuovaPass) throws SQLException {
        String query = "UPDATE credenziali SET PASSWORD=? WHERE USERNAME=? AND ISATTIVO = true";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nuovaPass);
            ps.setString(2, username);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public static String trovaUsernameFruitoreDaId(int id) throws SQLException {
        String query = "SELECT USERNAME FROM credenziali WHERE ID = ? AND ISATTIVO = true";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("USERNAME");
                }
            }
        }
        return null;
    }

    public static int trovaIdUtenteDaNickname(String nickname) throws SQLException {
        String query = "SELECT ID FROM credenziali WHERE USERNAME = ? AND ISATTIVO = true";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nickname);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        }
        return -1;
    }
    
    public void disattivaDaCredenziali(VolontarioModel volontario) throws SQLException {
        String updateSql = "UPDATE credenziali SET ISATTIVO = false WHERE USERNAME = ?";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, volontario.getNickname());
            int righe = ps.executeUpdate();
            if (righe > 0) {
                System.out.println("Volontario disattivato");
            } else {
                System.out.println("Volontario non trovato");
            }
        }
    }


    public boolean verificaAccesso(String username, String password) throws SQLException {
        CredenzialiModel cred = leggiSingolaCredenziale(username);
        return cred != null && cred.getPassword().equals(password);
    }
}
