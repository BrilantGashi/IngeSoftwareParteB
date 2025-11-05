package Versione1;
import java.sql.*;

public class AmbitoTerritorialeDAO {

    public int salvaAmbitoTerritoriale(AmbitoTerritoriale ambito) throws SQLException {
        String insertAmbitoSql = "INSERT INTO ambito_territoriale (provincia, comune, cap) VALUES (?,?,?)";

        int idAmbitoGenerato = -1;

        try (Connection conn = ConnessioneDB.startConnection()) {
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

    // Legge tutti gli ambiti territoriali dal database (ritorna il primo/ultimo trovato; per restituirli tutti, usa una lista).
    public AmbitoTerritoriale leggiTuttiAmbitiTerritoriali() throws SQLException {
        AmbitoTerritoriale ambito = null;

        String sql = "SELECT ID, PROVINCIA, COMUNE, CAP FROM ambito_territoriale";

        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String provincia = rs.getString("PROVINCIA");
                String comune = rs.getString("COMUNE");
                int cap = rs.getInt("CAP");

                ambito = new AmbitoTerritoriale(cap, provincia, comune);
                // Se vuoi restituirli tutti, metti questi oggetti in una lista e restituiscila.
            }
        }
        return ambito;
    }
}
