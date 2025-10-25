package Versione1;
import java.sql.*;
import java.util.*;

public class AmbitoTerritorialeDAO {

    public int salvaAmbitoTerritoriale(AmbitoTerritoriale ambito) throws SQLException {
        String insertAmbitoSql = "INSERT INTO ambito_territoriale (provincia) VALUES (?)";
        String insertCapSql = "INSERT INTO cap_ambito (cap, fk_ambito) VALUES (?, ?)";

        int idAmbitoGenerato = -1;

        try (Connection conn = ConnessioneDB.startConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psAmbito = conn.prepareStatement(insertAmbitoSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psCap = conn.prepareStatement(insertCapSql)) {

                // Inserisci provincia e prendi ID generato
                psAmbito.setString(1, ambito.getProvincia());
                int righe = psAmbito.executeUpdate();

                if (righe == 0) {
                    throw new SQLException("Creazione ambito territoriale fallita, nessuna riga inserita.");
                }

                try (ResultSet keys = psAmbito.getGeneratedKeys()) {
                    if (keys.next()) {
                        idAmbitoGenerato = keys.getInt(1);
                    } else {
                        throw new SQLException("Creazione ambito territoriale fallita, nessun ID generato.");
                    }
                }

                // Inserisci i CAP collegati
                for (Integer cap : ambito.getCap()) {
                    psCap.setInt(1, cap);
                    psCap.setInt(2, idAmbitoGenerato);
                    psCap.executeUpdate();
                }

                conn.commit();
                System.out.println("Ambito territoriale salvato con ID: " + idAmbitoGenerato);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }

        return idAmbitoGenerato;
    }

    //=======================================================================
  	//*********************** LETTURA**** ***********************************
  	//=======================================================================
    
 // Legge tutti gli ambiti territoriali dal database
    public AmbitoTerritoriale leggiTuttiAmbitiTerritoriali() throws SQLException {
        AmbitoTerritoriale ambito = new AmbitoTerritoriale();
        
        String sql = "SELECT ID, PROVINCIA FROM ambito_territoriale";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int idAmbito = rs.getInt("ID");
                String provincia = rs.getString("PROVINCIA");
                
                // Leggi i CAP associati
                ArrayList<Integer> capList = leggiCapPerAmbito(conn, idAmbito);
                
                // Crea oggetto AmbitoTerritoriale
                ambito = new AmbitoTerritoriale(capList, provincia);
                //listaAmbiti.add(ambito);
            }
        }
        
        return ambito;
    }

    // Metodo ausiliario per leggere i CAP di un ambito
    private ArrayList<Integer> leggiCapPerAmbito(Connection conn, int idAmbito) throws SQLException {
        ArrayList<Integer> capList = new ArrayList<>();
        String sql = "SELECT CAP FROM cap_ambito WHERE fk_ambito = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAmbito);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    capList.add(rs.getInt("CAP"));
                }
            }
        }
        return capList;
    }



}
