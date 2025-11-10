package Versione1;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class UtenteDAO {

    public static Credenziali leggiSingolaCredenziale(String username) {
        String query = "SELECT credenziali.USERNAME, credenziali.PASSWORD, tipologiautente.TIPO " +
                "FROM credenziali " +
                "JOIN tipologiautente ON credenziali.ID_TIPOUTENTE = tipologiautente.ID " +
                "WHERE credenziali.USERNAME = ? AND credenziali.ISATTIVO = true";  // ✅ Solo attivi

        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String utenza = rs.getString("TIPO");
                    String user = rs.getString("USERNAME");
                    String pass = rs.getString("PASSWORD");

                    if (utenza != null) utenza = utenza.trim();
                    if (user != null) user = user.trim();
                    if (pass != null) pass = pass.trim();

                    return new Credenziali(utenza, user, pass);
                } else {
                    System.out.println("Nessun risultato trovato.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la lettura: " + e.getMessage());
        }
        return null;
    }
    
    public static String trovaUsernameFruitoreDaId(int id) throws SQLException {
        String query = "SELECT USERNAME FROM credenziali WHERE ID = ? AND ISATTIVO = true";
        String username = null;
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
        	ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	username = rs.getString("USERNAME");
                } else {
                    System.out.println("Fruitore non trovato per Id: " + id);
                }
            }
        }
        return username; 
    }
    
    public static void aggiungiNuovoFruitore(Fruitore fruitore) throws SQLException {
        String sql = "INSERT INTO credenziali (USERNAME, PASSWORD, ID_TIPOUTENTE, ISATTIVO) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fruitore.getCredenziali().getUsername());
            ps.setString(2, fruitore.getCredenziali().getPassword());
            ps.setInt(3, 3); // 3 = fruitore (come da mappatura dei tuoi ID_TIPOUTENTE)
            ps.setBoolean(4, true); // ISATTIVO = true

            ps.executeUpdate();

            System.out.println("Nuovo fruitore salvato: " + fruitore.getCredenziali().getUsername());
        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento del nuovo fruitore: " + e.getMessage());
            throw e;
        }
    }
    
    public static int trovaIdUtenteDaNickname(String nickname, String tipoMessaggio) throws SQLException {
        String query = "SELECT ID FROM credenziali WHERE USERNAME = ? AND ISATTIVO = true";
        int idUtente = -1;

        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nickname);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idUtente = rs.getInt("ID");
                } else {
                    System.out.println(tipoMessaggio + " non trovato per username: " + nickname);
                }
            }
        }
        return idUtente; // -1 se non trovato
    }
    
    public static int trovaIdFruitoreDaNickname(String nickname) throws SQLException {
        return trovaIdUtenteDaNickname(nickname, "Fruitore");
    }

    public static int trovaIdVolontarioDaNickname(String nickname) throws SQLException {
        return trovaIdUtenteDaNickname(nickname, "Volontario");
    }

    public void stampaCredenziale(String username) {
        Credenziali dati = leggiSingolaCredenziale(username);

        if (dati != null) {
            System.out.println("========== CREDENZIALI ==========");
            System.out.println("Tipo utente: " + dati.getUtenza());
            System.out.println("Username:    " + dati.getUsername());
            System.out.println("Password:    " + dati.getPassword());
            System.out.println("=================================");
        } else {
            System.out.println("Nessuna credenziale trovata per username: " + username);
        }
    }

    public boolean aggiornaPasswordSuDB (String username, String nuovaPass) throws SQLException {
        boolean trovato = false;

        String query = "UPDATE credenziali "
                    + " JOIN tipologiautente ON credenziali.ID_TIPOUTENTE = tipologiautente.ID "
                    + " SET credenziali.PASSWORD=? "
                    + " WHERE credenziali.USERNAME=? AND credenziali.ISATTIVO = true";  // ✅ Solo attivi

        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nuovaPass);
            ps.setString(2, username);

            int righeAggiornate = ps.executeUpdate();

            if (righeAggiornate > 0) {
                trovato = true;
            }
        }

        return trovato;
    }

    public void leggiUtentiDaDB(ArrayList<Configuratore> configuratori, ListaVolontari volontari, ArrayList<Fruitore> fruitori) throws SQLException, FileNotFoundException, IOException {
        String sql = "SELECT ID, USERNAME, PASSWORD, ID_TIPOUTENTE FROM credenziali WHERE ISATTIVO = true";  // ✅ Solo attivi

        ArrayList<DatiUtente> datiUtenti = new ArrayList<>();

        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idTipoUtente = rs.getInt("id_tipoutente");
                String username = rs.getString("username");
                String password = rs.getString("password");
                datiUtenti.add(new DatiUtente(username, password, idTipoUtente));
            }
        }
        
        for (DatiUtente dati : datiUtenti) {
            Credenziali cred = leggiSingolaCredenziale(dati.username);

            switch (dati.idTipoUtente) {
                case 1:
                    configuratori.add(new Configuratore(cred));
                    break;
                case 2:
                    volontari.aggiungiVolontario(new Volontario(cred));
                    break;
                case 3:
                	fruitori.add(new Fruitore(cred));
                    // gestisci fruitore se serve
                    break;
                default:
            }
        }
    }

    private static class DatiUtente {
        String username;
        String password;
        int idTipoUtente;

        DatiUtente(String username, String password, int idTipoUtente) {
            this.username = username;
            this.password = password;
            this.idTipoUtente = idTipoUtente;
        }
    }

    public void inserisciNuovoVolontario (Volontario v) throws SQLException {
        String sql = "INSERT INTO credenziali (USERNAME, PASSWORD, ID_TIPOUTENTE, ISATTIVO) VALUES (?, ?, ?, ?)";  // ✅ Aggiunto ISATTIVO

        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, v.getCredenziali().getUsername());
            ps.setString(2, v.getCredenziali().getPassword());
            ps.setInt(3, 2); // 2 = volontario
            ps.setBoolean(4, true);  // ✅ Default: attivo quando viene creato
            ps.executeUpdate();

            System.out.println("Nuovo volontario salvato: " + v.getCredenziali().getUsername());
        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento del nuovo volontario: " + e.getMessage());
            throw e;
        }
    }

    public static void rimuoviVolontario(Volontario v) throws SQLException {

        String selectIdSql = "SELECT ID FROM credenziali WHERE USERNAME = ?";
        String deleteVisiteSql = "DELETE FROM tipovisita_volontario WHERE FK_VOLONTARIO = ?";
        String deleteDisponibilitaSql = "DELETE FROM disponibilita WHERE FK_VOLONTARIO = ?";

        try (Connection conn = ConnessioneDB.startConnection()) {
            conn.setAutoCommit(false);

            try {
                int idVolontario = -1;

                // 1. Trova l'ID del volontario
                try (PreparedStatement ps = conn.prepareStatement(selectIdSql)) {
                    ps.setString(1, v.getNickname());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            idVolontario = rs.getInt("ID");
                        } else {
                            throw new SQLException("Volontario non trovato: " + v.getNickname());
                        }
                    }
                }

                // 2. Elimina da tipovisita_volontario
                try (PreparedStatement ps = conn.prepareStatement(deleteVisiteSql)) {
                    ps.setInt(1, idVolontario);
                    int righeEliminateVisite = ps.executeUpdate();
                    System.out.println("Eliminate " + righeEliminateVisite + " associazioni visite");
                }

                // 3. Elimina da disponibilità
                try (PreparedStatement ps = conn.prepareStatement(deleteDisponibilitaSql)) {
                    ps.setInt(1, idVolontario);
                    int righeEliminateDisp = ps.executeUpdate();
                    System.out.println("Eliminate " + righeEliminateDisp + " disponibilità");
                }

                conn.commit();
                System.out.println("Associazioni volontario eliminate con successo!");

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static void disattivaDaCredenziali(Volontario volontario) throws SQLException {
        String updateSql = "UPDATE credenziali SET ISATTIVO = false WHERE USERNAME = ?";  // ✅ Corretto con ISATTIVO
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            
            ps.setString(1, volontario.getNickname());
            
            int righe = ps.executeUpdate();
            
            if (righe > 0) {
                System.out.println("✓ Volontario disattivato");
            } else {
                System.out.println("⚠️  Volontario non trovato");
            }
        } catch (SQLException e) {
            System.out.println("❌ Errore: " + e.getMessage());
            throw e;
        }
    }
}
