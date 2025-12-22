package model.dao;

import model.LuogoModel;
import model.TipoStrada;
import model.AmbitoTerritorialeModel;
import model.IndirizzoModel;
import model.ListaTipoVisitaModel;
import model.TipoVisitaModel;

import java.sql.*;
import java.util.ArrayList;

public class LuogoDAOModel {

    public void salvaLuogoCompleto(LuogoModel luogo) throws SQLException {
        try (Connection conn = ConnessioneDBModel.startConnection()) {
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

    private void associaTipiVisita(Connection conn, int idLuogo, ListaTipoVisitaModel tipiVisita) throws SQLException {
        String selectIdSql = "SELECT ID FROM tipo_visita WHERE TITOLO = ? AND DESCRIZIONE = ?";
        String insertSql = "INSERT INTO luogo_tipo_visita (FK_LUOGO, FK_TIPO_VISITA) VALUES (?, ?)";

        try (PreparedStatement psSelect = conn.prepareStatement(selectIdSql);
             PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
            for (TipoVisitaModel tv : tipiVisita.getTipi()) {
                psSelect.setString(1, tv.getTitolo());
                psSelect.setString(2, tv.getDescrizione());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        int idTipoVisita = rs.getInt("ID");
                        psInsert.setInt(1, idLuogo);
                        psInsert.setInt(2, idTipoVisita);
                        psInsert.addBatch();
                    }
                }
            }
            psInsert.executeBatch();
        }
    }

    private int salvaIndirizzo(Connection conn, IndirizzoModel indirizzo) throws SQLException {
        // 1. Recuperiamo l'ID dell'ambito territoriale dal DB usando i dati testuali
        // Poiché l'ambito è unico o già esistente, cerchiamo l'ID corrispondente
        int idAmbitoTecnico = -1;
        String sqlCercaAmbito = "SELECT ID FROM ambito_territoriale WHERE PROVINCIA = ? AND COMUNE = ? AND CAP = ?";
        
        try (PreparedStatement psAmbito = conn.prepareStatement(sqlCercaAmbito)) {
            psAmbito.setString(1, indirizzo.getAmbitoTerritoriale().getProvincia());
            psAmbito.setString(2, indirizzo.getAmbitoTerritoriale().getComune());
            psAmbito.setInt(3, indirizzo.getAmbitoTerritoriale().getCap());
            
            try (ResultSet rs = psAmbito.executeQuery()) {
                if (rs.next()) {
                    idAmbitoTecnico = rs.getInt("ID");
                } else {
                    // Se per qualche motivo l'ambito non esiste, l'errore di FK scatterebbe comunque
                    throw new SQLException("Ambito Territoriale non trovato nel database per: " + indirizzo.getAmbitoTerritoriale());
                }
            }
        }

        // 2. Ora inseriamo l'indirizzo usando l'idAmbitoTecnico appena trovato
        String sqlInsertIndirizzo = "INSERT INTO indirizzo (TIPO_STRADA, NOME_STRADA, NUM_CIVICO, FK_AMBITO) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement psIndirizzo = conn.prepareStatement(sqlInsertIndirizzo, Statement.RETURN_GENERATED_KEYS)) {
            psIndirizzo.setString(1, indirizzo.getTipoStrada().name());
            psIndirizzo.setString(2, indirizzo.getNomeStrada());
            psIndirizzo.setString(3, indirizzo.getNumeroCivico());
            psIndirizzo.setInt(4, idAmbitoTecnico); // <--- Questo risolve il tuo errore!

            psIndirizzo.executeUpdate();

            // 3. Recuperiamo l'ID dell'indirizzo appena creato per passarlo a salvaLuogo
            try (ResultSet keys = psIndirizzo.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("Impossibile generare ID per l'indirizzo.");
                }
            }
        }
    }

    private int salvaLuogo(Connection conn, String nome, String descrizione, int idIndirizzo) throws SQLException {
        String sql = "INSERT INTO luogo (NOME, DESCRIZIONE, FK_INDIRIZZO, ISATTIVO) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.setString(2, descrizione);
            ps.setInt(3, idIndirizzo);
            ps.setBoolean(4, true);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }
    
    public ArrayList<LuogoModel> leggiTuttiLuoghi(ListaTipoVisitaModel listaTipoVisitaModel) throws SQLException {
        ArrayList<LuogoModel> listaLuoghi = new ArrayList<>();

        String sql =
            "SELECT l.ID, l.NOME, l.DESCRIZIONE, l.FK_INDIRIZZO, " +
            "i.TIPO_STRADA, i.NOME_STRADA, i.NUM_CIVICO, i.FK_AMBITO, " +
            "a.CAP, a.COMUNE, a.PROVINCIA " +
            "FROM luogo l " +
            "JOIN indirizzo i ON l.FK_INDIRIZZO = i.ID " +
            "JOIN ambito_territoriale a ON i.FK_AMBITO = a.ID " +
            "WHERE l.ISATTIVO = true";

        try (Connection conn = ConnessioneDBModel.startConnection();
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

                AmbitoTerritorialeModel ambito = new AmbitoTerritorialeModel(cap, provincia, comune);
                IndirizzoModel indirizzo = new IndirizzoModel(tipoStrada, nomeStrada, numCivico, ambito);

                // Otteniamo tutti i tipi visita associati a questo luogo
                ListaTipoVisitaModel tipiVisita = leggiTipiVisitaPerLuogo(conn, idLuogo, listaTipoVisitaModel);

                LuogoModel luogo = new LuogoModel(nome, descrizione, indirizzo, tipiVisita);
                listaLuoghi.add(luogo);
            }
        }
        return listaLuoghi;
    }
    
 // In LuogoDAOModel
    private ListaTipoVisitaModel leggiTipiVisitaPerLuogo(Connection conn, int idLuogo, ListaTipoVisitaModel listaTV) throws SQLException {
        ListaTipoVisitaModel listaTipi = new ListaTipoVisitaModel();
        ArrayList<String> titoliList = new ArrayList<>();

        String sql = "SELECT tv.TITOLO " +
                     "FROM tipo_visita tv " +
                     "JOIN luogo_tipo_visita ltv ON tv.ID = ltv.FK_TIPO_VISITA " +
                     "WHERE ltv.FK_LUOGO = ? AND tv.ISATTIVO = true";
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
            TipoVisitaModel tv = listaTV.cercaPerTitolo(titolo);
            if (tv != null) {
                listaTipi.add(tv);
            } else {
                System.out.println("Attenzione: TipoVisitaModel con titolo '" + titolo + "' non trovato nella lista globale!");
            }
        }
        return listaTipi;
    }
    
    public void aggiungiTipoVisitaALuogo(LuogoModel luogo, TipoVisitaModel tipoVisita) throws SQLException {
        String selectTipoSql = "SELECT ID FROM tipo_visita WHERE TITOLO = ? AND DESCRIZIONE = ?";
        String selectLuogoSql = "SELECT ID FROM luogo WHERE NOME = ? AND DESCRIZIONE = ?";
        String insertSql = "INSERT INTO luogo_tipo_visita (FK_LUOGO, FK_TIPO_VISITA) VALUES (?, ?)";

        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement psTipo = conn.prepareStatement(selectTipoSql);
             PreparedStatement psLuogo = conn.prepareStatement(selectLuogoSql);
             PreparedStatement psInsert = conn.prepareStatement(insertSql)) {

            // Trova l'id del tipo visita
            psTipo.setString(1, tipoVisita.getTitolo());
            psTipo.setString(2, tipoVisita.getDescrizione());
            int idTipoVisita = -1;
            try (ResultSet rsTipo = psTipo.executeQuery()) {
                if (rsTipo.next()) {
                    idTipoVisita = rsTipo.getInt("ID");
                } else {
                    throw new SQLException("Tipo Visita non trovato: " + tipoVisita.getTitolo());
                }
            }

            // Trova l'id del luogo
            psLuogo.setString(1, luogo.getNomeLuogo());
            psLuogo.setString(2, luogo.getDescrizione());
            int idLuogo = -1;
            try (ResultSet rsLuogo = psLuogo.executeQuery()) {
                if (rsLuogo.next()) {
                    idLuogo = rsLuogo.getInt("ID");
                } else {
                    throw new SQLException("Luogo non trovato: " + luogo.getNomeLuogo());
                }
            }

            // Inserisci nella tabella ponte
            psInsert.setInt(1, idLuogo);
            psInsert.setInt(2, idTipoVisita);
            psInsert.executeUpdate();
        }
    }

    
    public void eliminaLuogo(LuogoModel luogo) throws SQLException {
        String updateSql = "UPDATE luogo SET ISATTIVO = false WHERE NOME = ? AND DESCRIZIONE = ?";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, luogo.getNomeLuogo());
            ps.setString(2, luogo.getDescrizione());
            int updated = ps.executeUpdate();
            if (updated == 0) throw new SQLException("Nessun luogo eliminato: parametri non trovati.");
        }
    }




    // altri metodi: trovaLuoghiOrfani, eliminaLuogo, trovaIdPerLuogo, ecc.
    // li puoi mantenere come nel tuo esempio, adattando il tipo Luogo --> LuogoModel se serve
}
