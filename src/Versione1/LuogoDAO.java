package Versione1;
import java.sql.*;
import java.util.*;

public class LuogoDAO {

    // Salva un Luogo con Indirizzo e TipiVisita
    public  void salvaLuogoCompleto(Luogo luogo) throws SQLException {
        try (Connection conn = ConnessioneDB.startConnection()) {
            conn.setAutoCommit(false);
            try {
                int idIndirizzo = salvaIndirizzo(conn, luogo.getIndirizzo());
                int idLuogo = salvaLuogo(conn, luogo.getNomeLuogo(), luogo.getDescrizione(), idIndirizzo);
                associaTipiVisita(conn, idLuogo, luogo.getListaTipoVisita());
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    private void associaTipiVisita(Connection conn, int idLuogo, ListaTipoVisita tipiVisita) throws SQLException {
        String selectIdSql = "SELECT ID FROM tipo_visita WHERE TITOLO = ? AND DESCRIZIONE = ?";
        String insertSql = "INSERT INTO luogo_tipo_visita (FK_LUOGO, FK_TIPO_VISITA) VALUES (?, ?)";

        try (PreparedStatement psSelect = conn.prepareStatement(selectIdSql);
             PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
            for (TipoVisita tv : tipiVisita.getTipi()) {
                // Cerca per titolo + descrizione
                psSelect.setString(1, tv.getTitolo());
                psSelect.setString(2, tv.getDescrizione());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        int idTipoVisita = rs.getInt("ID");
                        psInsert.setInt(1, idLuogo);
                        psInsert.setInt(2, idTipoVisita);
                        psInsert.addBatch();
                    } else {
                        System.out.println("Tipo Visita non trovato per titolo: " + tv.getTitolo() +
                                           ", descrizione: " + tv.getDescrizione());
                    }
                }
            }
            psInsert.executeBatch();
        }
    }

    // Salva un indirizzo e restituisce l'ID generato
    private int salvaIndirizzo(Connection conn, Indirizzo indirizzo) throws SQLException {
        int idAmbito = salvaORecuperaAmbito(conn, indirizzo.getAmbitoTerritoriale());
        
        String sql = "INSERT INTO indirizzo (TIPO_STRADA, NOME_STRADA, NUM_CIVICO, FK_AMBITO) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, indirizzo.getTipoStrada().name());
            ps.setString(2, indirizzo.getNomeStrada());
            ps.setString(3, indirizzo.getNumeroCivico());
            ps.setInt(4, idAmbito);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("Errore: nessun ID generato per l'indirizzo.");
                }
            }
        }
    }

    private int salvaORecuperaAmbito(Connection conn, AmbitoTerritoriale ambito) throws SQLException {
        String checkSql = "SELECT ID FROM ambito_territoriale WHERE CAP = ? AND PROVINCIA = ? AND COMUNE = ?";
        try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
            psCheck.setInt(1, ambito.getCap());
            psCheck.setString(2, ambito.getProvincia());
            psCheck.setString(3, ambito.getComune());
            
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        }
        
        String insertSql = "INSERT INTO ambito_territoriale (CAP, PROVINCIA, COMUNE) VALUES (?, ?, ?)";
        try (PreparedStatement psInsert = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            psInsert.setInt(1, ambito.getCap());
            psInsert.setString(2, ambito.getProvincia());
            psInsert.setString(3, ambito.getComune());
            psInsert.executeUpdate();
            
            try (ResultSet keys = psInsert.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("Errore: nessun ID generato per l'ambito territoriale.");
                }
            }
        }
    }

    // Salva un luogo e restituisce l'ID generato
    private int salvaLuogo(Connection conn, String nome, String descrizione, int idIndirizzo) throws SQLException {
        String sql = "INSERT INTO luogo (NOME, DESCRIZIONE, FK_INDIRIZZO, ISATTIVO) VALUES (?, ?, ?, ?)";  // ✅ Aggiunto ISATTIVO
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.setString(2, descrizione);
            ps.setInt(3, idIndirizzo);
            ps.setBoolean(4, true);  // ✅ Default: attivo quando viene creato
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("Errore: nessun ID generato per il luogo.");
                }
            }
        }
    }

    public ArrayList<Luogo> trovaLuoghiOrfani(ListaLuogo listaLuoghi) throws SQLException {
        ArrayList<Luogo> luoghiOrfani = new ArrayList<>();
        String sql = "SELECT l.ID, l.NOME, l.DESCRIZIONE " +
                     "FROM luogo l " +
                     "WHERE l.ID NOT IN (SELECT DISTINCT FK_LUOGO FROM luogo_tipo_visita) " +
                     "AND l.ISATTIVO = true " +  // ✅ Solo attivi
                     "ORDER BY l.NOME";
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nome = rs.getString("NOME");
                Luogo luogo = listaLuoghi.cercaPerNome(nome);
                if (luogo != null) {
                    luoghiOrfani.add(luogo);
                }
            }
        }
        return luoghiOrfani;
    }

    public static void eliminaLuogo(Luogo luogo) throws SQLException {
        String updateSql = "UPDATE luogo SET ISATTIVO = false WHERE NOME = ?";  // ✅ Corretto con ISATTIVO
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            
            ps.setString(1, luogo.getNomeLuogo());
            
            int righe = ps.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
            throw e;
        }
    }

 // Modifica: passa la connessione a trovaIdPerLuogo!
    public static int trovaIdPerLuogo(Connection conn, Luogo luogo) throws SQLException {
        String sql = "SELECT ID FROM luogo WHERE NOME = ? AND DESCRIZIONE = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, luogo.getNomeLuogo());
            ps.setString(2, luogo.getDescrizione());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        }
        return -1; // oppure gestisci eccezioni
    }

    // Associa i tipi visita al luogo
    public void aggiungiTipoVisitaALuogo(Luogo luogo, TipoVisita tipoVisita) throws SQLException {
        String selectIdSql = "SELECT ID FROM tipo_visita WHERE TITOLO = ? AND DESCRIZIONE = ?";
        String insertSql = "INSERT INTO luogo_tipo_visita (FK_LUOGO, FK_TIPO_VISITA) VALUES (?, ?)";

        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement psSelect = conn.prepareStatement(selectIdSql);
             PreparedStatement psInsert = conn.prepareStatement(insertSql)) {

            psSelect.setString(1, tipoVisita.getTitolo());
            psSelect.setString(2, tipoVisita.getDescrizione());

            int idTipoVisita = -1;
            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs.next()) {
                    idTipoVisita = rs.getInt("ID");
                } else {
                    System.out.println("Tipo visita non trovato per titolo: " + tipoVisita.getTitolo() +
                                       " e descrizione: " + tipoVisita.getDescrizione());
                    return;
                }
            }

            int idLuogo = trovaIdPerLuogo(conn, luogo); // usa la stessa connessione!
            psInsert.setInt(1, idLuogo);
            psInsert.setInt(2, idTipoVisita);
            psInsert.executeUpdate();
        }
    }
    
    // Legge tutti i Luoghi dal database
    public ListaLuogo leggiTuttiLuoghi(ListaTipoVisita listaTV) throws SQLException {
        ListaLuogo listaLuoghi = new ListaLuogo();
        String sql =
            "SELECT l.ID, l.NOME, l.DESCRIZIONE, l.FK_INDIRIZZO, " +
            "i.TIPO_STRADA, i.NOME_STRADA, i.NUM_CIVICO, i.FK_AMBITO, " +
            "a.CAP, a.COMUNE, a.PROVINCIA " +
            "FROM luogo l " +
            "JOIN indirizzo i ON l.FK_INDIRIZZO = i.ID " +
            "JOIN ambito_territoriale a ON i.FK_AMBITO = a.ID " +
            "WHERE l.ISATTIVO = true";  // ✅ Solo attivi
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int idLuogo = rs.getInt("ID");
                String nome = rs.getString("NOME");
                String descrizione = rs.getString("DESCRIZIONE");
                TipoStrada tipoStrada = TipoStrada.valueOf(rs.getString("TIPO_STRADA"));
                String nomeStrada = rs.getString("NOME_STRADA");
                String numCivico = rs.getString("NUM_CIVICO");
                int cap = rs.getInt("CAP");
                String comune = rs.getString("COMUNE");
                String provincia = rs.getString("PROVINCIA");
                
                AmbitoTerritoriale ambito = new AmbitoTerritoriale(cap, provincia, comune);
                Indirizzo indirizzo = new Indirizzo(tipoStrada, nomeStrada, numCivico, ambito);
                ListaTipoVisita tipiVisita = leggiTipiVisitaPerLuogo(conn, idLuogo, listaTV);
                Luogo luogo = new Luogo(nome, descrizione, indirizzo, tipiVisita);
                listaLuoghi.add(luogo);
            }
        }
        return listaLuoghi;
    }

    // Metodo ausiliario per leggere i tipi visita associati a un luogo
    private ListaTipoVisita leggiTipiVisitaPerLuogo(Connection conn, int idLuogo, ListaTipoVisita listaTV) throws SQLException {
        ListaTipoVisita listaTipi = new ListaTipoVisita();
        ArrayList<String> titoliList = new ArrayList<>();
        String sql = "SELECT tv.TITOLO FROM tipo_visita tv " +
                     "JOIN luogo_tipo_visita ltv ON tv.ID = ltv.FK_TIPO_VISITA " +
                     "WHERE ltv.FK_LUOGO = ? AND tv.ISATTIVO = true";  // ✅ Solo tipi visita attivi
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLuogo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String titolo = rs.getString("TITOLO");
                    titoliList.add(titolo);
                }
            }
        }
        for (String titolo : titoliList) {
            TipoVisita tv = listaTV.cercaPerTitolo(titolo);
            if (tv != null) {
                listaTipi.add(tv);
            } else {
                System.out.println("Attenzione: TipoVisita con titolo '" + titolo + "' non trovato nella lista globale!");
            }
        }
        return listaTipi;
    }
}
