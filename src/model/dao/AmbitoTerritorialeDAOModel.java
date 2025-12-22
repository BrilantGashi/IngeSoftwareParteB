package model.dao;
import java.sql.*;

import model.AmbitoTerritorialeModel;

public class AmbitoTerritorialeDAOModel {

    public static int salvaAmbitoTerritoriale(AmbitoTerritorialeModel ambito) throws SQLException {
        String insertAmbitoSql = "INSERT INTO ambito_territoriale (provincia, comune, cap) VALUES (?,?,?)";

        int idAmbitoGenerato = -1;

        try (Connection conn = ConnessioneDBModel.startConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psAmbito = conn.prepareStatement(insertAmbitoSql, Statement.RETURN_GENERATED_KEYS)) {

                psAmbito.setString(1, ambito.getProvincia());
                psAmbito.setString(2, ambito.getComune());
                psAmbito.setInt(3, ambito.getCap());
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

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }

        return idAmbitoGenerato;
    }

    // Legge tutti gli ambiti territoriali dal database (ritorna il primo/ultimo trovato; per restituirli tutti, usa una lista).
    public static AmbitoTerritorialeModel leggiTuttiAmbitiTerritoriali() throws SQLException {
        AmbitoTerritorialeModel ambito = null;

        String sql = "SELECT ID, PROVINCIA, COMUNE, CAP FROM ambito_territoriale";

        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String provincia = rs.getString("PROVINCIA");
                String comune = rs.getString("COMUNE");
                int cap = rs.getInt("CAP");

                ambito = new AmbitoTerritorialeModel(cap, provincia, comune);
                // Se vuoi restituirli tutti, metti questi oggetti in una lista e restituiscila.
            }
        }
        return ambito;
    }
}
