package Versione1;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;

public class TipoVisitaDAO {
    
    public void inserisciVisita(TipoVisita visita) throws SQLException {

        // PERIODO
        int idPeriodo = inserisciPeriodo(visita.getPeriodoProgrammabile());
        
        // TIPO VISITA
        int idTipoVisitaGenerato = -1;
        
        String insertSql = "INSERT INTO tipo_visita " +
                "( TITOLO, DESCRIZIONE, ORA_INIZIO, DURATA_VISITA, BIGLIETTO, SOGLIA_MIN_PART, SOGLIA_MAX_PART, PUNTO_INCONTRO, FK_PERIODO, ISATTIVO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDB.startConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, visita.getTitolo());
                ps.setString(2, visita.getDescrizione());
                ps.setTime(3, java.sql.Time.valueOf(visita.getOraInizio()));
                ps.setInt(4, visita.getDurataVisita());
                ps.setBoolean(5, visita.isBiglietto());
                ps.setInt(6, visita.getSogliaMinPartecipanti());
                ps.setInt(7, visita.getSogliaMaxPartecipanti());
                ps.setString(8, visita.getPuntoDiIncontro());
                ps.setInt(9, idPeriodo);
                ps.setBoolean(10, true);  // ✅ Default: attivo quando viene creato

                int righeInserite = ps.executeUpdate();

                if (righeInserite > 0) {
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            idTipoVisitaGenerato = keys.getInt(1);
                            System.out.println("ID generato: " + idTipoVisitaGenerato);

                            collegaGiorniATipoVisita(conn, idTipoVisitaGenerato, visita.getGiorniSettimanaProgrammabile());
                            collegaVolontariATipoVisita(conn, idTipoVisitaGenerato, visita.getListaVolontari());

                            conn.commit();
                            System.out.println("Tipo Visita inserito con successo!");
                        } else {
                            System.out.println("Nessun ID restituito.");
                            conn.rollback();
                        }
                    }
                } else {
                    conn.rollback();
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Errore durante l'inserimento tipo visita: " + e.getMessage());
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    public int inserisciPeriodo(Periodo periodo) throws SQLException {
        String insertQuery = "INSERT INTO periodo_programmabile (GIORNO_INIZIO, MESE_INIZIO, GIORNO_FINE, MESE_FINE) VALUES (?, ?, ?, ?)";
        int idGenerato = -1;
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, periodo.getDataInizio().getGiorno());
            ps.setInt(2, periodo.getDataInizio().getMese());
            ps.setInt(3, periodo.getDataFine().getGiorno());
            ps.setInt(4, periodo.getDataFine().getMese());
            int righe = ps.executeUpdate();
            if (righe > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        idGenerato = keys.getInt(1);
                        System.out.println("ID generato per periodo: " + idGenerato);
                    }
                }
            }
        }
        return idGenerato;
    }
  
    public void collegaGiorniATipoVisita(Connection conn, int idTipoVisita, ArrayList<Giorni> giorniList) throws SQLException {
        String selectIdGiorni = "SELECT id FROM giorni WHERE nome = ?";
        String insertPonte = "INSERT INTO tipo_visita_giorni (fk_tipovisita, fk_giorni) VALUES (?, ?)";

        try (PreparedStatement psSelect = conn.prepareStatement(selectIdGiorni);
             PreparedStatement psInsert = conn.prepareStatement(insertPonte)) {

            for (Giorni giornoEnum : giorniList) {
                psSelect.setString(1, giornoEnum.name());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        int idGiorno = rs.getInt("id");

                        psInsert.setInt(1, idTipoVisita);
                        psInsert.setInt(2, idGiorno);
                        psInsert.executeUpdate();
                    } else {
                        System.out.println("Giorno non trovato nel DB: " + giornoEnum.name());
                    }
                }
            }
            conn.commit();
            System.out.println("Associazioni giorni-tipo visita inserite con successo.");
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
     
    public void collegaVolontariATipoVisita(Connection conn, int idTipoVisita, ListaVolontari lista) throws SQLException {
        String selectIdVolontario = "SELECT id FROM credenziali WHERE username = ?";
        String checkEsistenza = "SELECT COUNT(*) FROM tipovisita_volontario WHERE fk_tipovisita = ? AND fk_volontario = ?";
        String insertAssociazione = "INSERT INTO tipovisita_volontario (fk_tipovisita, fk_volontario) VALUES (?, ?)";

        try (PreparedStatement psSelect = conn.prepareStatement(selectIdVolontario);
             PreparedStatement psCheck = conn.prepareStatement(checkEsistenza);
             PreparedStatement psInsert = conn.prepareStatement(insertAssociazione)) {

            for (Volontario volontario : lista.getVolontari()) {
                psSelect.setString(1, volontario.getNickname());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        int idVolontario = rs.getInt("id");

                        psCheck.setInt(1, idTipoVisita);
                        psCheck.setInt(2, idVolontario);
                        try (ResultSet rsCheck = psCheck.executeQuery()) {
                            rsCheck.next();
                            int count = rsCheck.getInt(1);
                            
                            if (count == 0) {
                                psInsert.setInt(1, idTipoVisita);
                                psInsert.setInt(2, idVolontario);
                                psInsert.executeUpdate();

                                System.out.println("Collegato volontario " + volontario.getNickname() + 
                                        " (ID: " + idVolontario + ") al tipo visita " + idTipoVisita);
                            } else {
                                System.out.println("Volontario " + volontario.getNickname() +
                                        " già collegato a tipo visita " + idTipoVisita);
                            }
                        }
                    } else {
                        System.out.println("Nessun volontario trovato per username: " + volontario.getNickname());
                    }
                }
            }
                
            conn.commit();
            System.out.println("Tutti i volontari sono stati collegati con successo!");
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("Errore durante l'inserimento volontari: " + e.getMessage());
            throw e;
        }
    }
     
    public void gestisciOrfani(TipoVisita visita, Volontario v) throws SQLException {

        String selectIdTipoVisitaSql = "SELECT ID FROM tipo_visita WHERE TITOLO = ?";
        try (Connection conn = ConnessioneDB.startConnection()) {
            conn.setAutoCommit(false);

            try {
                int idTipoVisita;
                try (PreparedStatement psTipoVisita = conn.prepareStatement(selectIdTipoVisitaSql)) {
                    psTipoVisita.setString(1, visita.getTitolo());
                    try (ResultSet rs = psTipoVisita.executeQuery()) {
                        if (rs.next()) {
                            idTipoVisita = rs.getInt("ID");
                        } else {
                            throw new SQLException("Tipo Visita non trovato per titolo: " + visita.getTitolo());
                        }
                    }
                }

                ListaVolontari listaV = new ListaVolontari();
                listaV.aggiungiVolontario(v);
                collegaVolontariATipoVisita(conn, idTipoVisita, listaV);

                conn.commit();
                System.out.println("Volontario " + v.getNickname() + " associato correttamente alla visita \"" + visita.getTitolo() + "\".");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    public ListaTipoVisita leggiTuttiTipiVisita(ListaVolontari listaV) throws SQLException, FileNotFoundException, IOException {
    	ListaTipoVisita listaTipi = new ListaTipoVisita();
        String sql = "SELECT tv.ID, tv.TITOLO, tv.DESCRIZIONE, tv.ORA_INIZIO, tv.DURATA_VISITA, " +
                     "tv.BIGLIETTO, tv.SOGLIA_MIN_PART, tv.SOGLIA_MAX_PART, tv.PUNTO_INCONTRO, " +
                     "tv.FK_PERIODO, p.GIORNO_INIZIO, p.MESE_INIZIO, p.GIORNO_FINE, p.MESE_FINE " +
                     "FROM tipo_visita tv " +
                     "JOIN periodo_programmabile p ON tv.FK_PERIODO = p.ID " +
                     "WHERE tv.ISATTIVO = true";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int idTipoVisita = rs.getInt("ID");
                String titolo = rs.getString("TITOLO");
                String descrizione = rs.getString("DESCRIZIONE");
                LocalTime oraInizio = rs.getTime("ORA_INIZIO").toLocalTime();
                int durataVisita = rs.getInt("DURATA_VISITA");
                boolean biglietto = rs.getBoolean("BIGLIETTO");
                int sogliaMin = rs.getInt("SOGLIA_MIN_PART");
                int sogliaMax = rs.getInt("SOGLIA_MAX_PART");
                String puntoIncontro = rs.getString("PUNTO_INCONTRO");
                
                int giornoInizio = rs.getInt("GIORNO_INIZIO");
                int meseInizio = rs.getInt("MESE_INIZIO");
                int giornoFine = rs.getInt("GIORNO_FINE");
                int meseFine = rs.getInt("MESE_FINE");
                Periodo periodo = new Periodo(
                    new Data(giornoInizio, meseInizio),
                    new Data(giornoFine, meseFine)
                    );
                
                ArrayList<Giorni> giorniList = leggiGiorniPerTipoVisita(conn, idTipoVisita);
                ListaVolontari volontari = leggiVolontariPerTipoVisita(conn, idTipoVisita, listaV);
                
                TipoVisita tipoVisita = new TipoVisita(titolo, descrizione, puntoIncontro, giorniList, 
                                                        oraInizio, durataVisita, biglietto, 
                                                        sogliaMin, sogliaMax, periodo, volontari);
                
                listaTipi.add(tipoVisita);
            }
        }
        
        return listaTipi;
    }
 
    private ListaVolontari leggiVolontariPerTipoVisita(Connection conn, int idTipoVisita, ListaVolontari listaV) throws SQLException {
        ListaVolontari listaVolontari = new ListaVolontari();
        
        String sql = "SELECT c.USERNAME FROM credenziali c " +
                     "JOIN tipovisita_volontario tv ON c.ID = tv.fk_volontario " +
                     "WHERE tv.fk_tipovisita = ?";
        
        ArrayList<String> usernameList = new ArrayList<>();
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoVisita);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("USERNAME");
                    usernameList.add(username);
                }
            }
        }
        
        for (String username : usernameList) {
            Volontario v = listaV.cercaPerNickname(username);
            if (v != null) {
                listaVolontari.aggiungiVolontario(v);
            }
        }
        
        return listaVolontari;
    }
 
    private ArrayList<Giorni> leggiGiorniPerTipoVisita(Connection conn, int idTipoVisita) throws SQLException {
        ArrayList<Giorni> giorniList = new ArrayList<>();
        String sql = "SELECT g.NOME FROM giorni g " +
                     "JOIN tipo_visita_giorni tvg ON g.ID = tvg.fk_giorni " +
                     "WHERE tvg.fk_tipovisita = ?";
        
        ArrayList<String> nomiGiorni = new ArrayList<>();
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoVisita);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nomeGiorno = rs.getString("NOME");
                    nomiGiorni.add(nomeGiorno);
                }
            }
        }
        
        for (String nome : nomiGiorni) {
            giorniList.add(Giorni.valueOf(nome));
        }
        
        return giorniList;
    }

    public ArrayList<TipoVisita> trovaTipiVisitaOrfani(ListaTipoVisita listaTipoVisita) throws SQLException {
        ArrayList<TipoVisita> tipiOrfani = new ArrayList<>();
        
        String sql = "SELECT tv.ID, tv.TITOLO, tv.DESCRIZIONE " +
                     "FROM tipo_visita tv " +
                     "WHERE tv.ID NOT IN ( " +
                     "    SELECT DISTINCT fk_tipovisita " +
                     "    FROM tipovisita_volontario " +
                     ") " +
                     "AND tv.ISATTIVO = true " +  // ✅ Solo attivi
                     "ORDER BY tv.TITOLO";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String titolo = rs.getString("TITOLO");
                
                TipoVisita tv = listaTipoVisita.cercaPerTitolo(titolo);
                if (tv != null) {
                    tipiOrfani.add(tv);
                }
            }
        }
        
        return tipiOrfani;
    }

    public void eliminaTipoVisita(TipoVisita tipoVisita) throws SQLException {
        
        String updateSql = "UPDATE tipo_visita SET ISATTIVO = false " +
                           "WHERE TITOLO = ? AND DESCRIZIONE = ? AND PUNTO_INCONTRO = ?";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            
            ps.setString(1, tipoVisita.getTitolo());
            ps.setString(2, tipoVisita.getDescrizione());
            ps.setString(3, tipoVisita.getPuntoDiIncontro());
            
            int righe = ps.executeUpdate();
            
            if (righe > 0) {
                System.out.println("Tipo visita disattivato");
            } else {
                System.out.println("Tipo visita non trovato");
            }
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
            throw e;
        }
    }
}
