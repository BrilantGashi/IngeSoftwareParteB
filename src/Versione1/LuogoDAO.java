package Versione1;
import java.sql.*;
import java.util.*;

public class LuogoDAO {
	
	
	// Metodo pubblico per salvare un Luogo con Indirizzo e TipiVisita
    public void salvaLuogoCompleto(Luogo luogo) throws SQLException {
        try (Connection conn = ConnessioneDB.startConnection()) {
            conn.setAutoCommit(false);
            try {
                int idIndirizzo = salvaIndirizzo(conn, luogo.getIndirizzo());
                int idLuogo = salvaLuogo(conn, luogo.getNomeLuogo(), luogo.getDescrizione(), idIndirizzo);
                
                
                
                //FARE ATTENZIONE *********
                associaTipiVisita(conn, idLuogo, luogo.getListaTipoVisita().getTipi());
                
                
                
                
                
                
                conn.commit();
                System.out.println("Luogo e associazioni salvati correttamente.");
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // Salva un'indirizzo e restituisce l'ID generato
    private int salvaIndirizzo(Connection conn, Indirizzo indirizzo) throws SQLException {
        String sql = "INSERT INTO indirizzo (TIPO_STRADA, NOME_STRADA, NUM_CIVICO, CAP, COMUNE, PROVINCIA) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, indirizzo.getTipoStrada().name());
            ps.setString(2, indirizzo.getNomeStrada());
            ps.setString(3, indirizzo.getNumeroCivico());
            ps.setInt(4, indirizzo.getCap());
            ps.setString(5, indirizzo.getComune());
            ps.setString(6, indirizzo.getProvincia());
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

    // Salva un luogo e restituisce l'ID generato
    private int salvaLuogo(Connection conn, String nome, String descrizione, int idIndirizzo) throws SQLException {
        String sql = "INSERT INTO luogo (NOME, DESCRIZIONE, FK_INDIRIZZO) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.setString(2, descrizione);
            ps.setInt(3, idIndirizzo);
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

    // Associa i tipi visita al luogo
    /*private void associaTipiVisita(Connection conn, int idLuogo, List<TipoVisita> tipiVisita) throws SQLException {
        String sql = "INSERT INTO luogo_tipo_visita (FK_LUOGO, FK_TIPO_VISITA) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (TipoVisita tv : tipiVisita) {
            	
            	
            	
            	//DEVO SELEZIONARE ESATTAMENTE QUEL TIPO VISITA, QUINDI DEVO FARE UNA SELECT IN MODO CHE TUTTI I
            	//PARAMETRI COINCIDANO
            	
            	
            	
            	
                ps.setInt(1, idLuogo);
                ps.setInt(2, tv.getId()); // assume che TipoVisita abbia un metodo getId()
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }*/
    
    
    private void associaTipiVisita(Connection conn, int idLuogo, List<TipoVisita> tipiVisita) throws SQLException {
        String selectIdSql = "SELECT ID FROM tipo_visita WHERE TITOLO = ?";
        String insertSql = "INSERT INTO luogo_tipo_visita (FK_LUOGO, FK_TIPO_VISITA) VALUES (?, ?)";
        
        try (PreparedStatement psSelect = conn.prepareStatement(selectIdSql);
             PreparedStatement psInsert = conn.prepareStatement(insertSql)) {

            for (TipoVisita tv : tipiVisita) {
                // Esegui la SELECT per ottenere l'ID in base al titolo
                psSelect.setString(1, tv.getTitolo());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        int idTipoVisita = rs.getInt("ID");
                        psInsert.setInt(1, idLuogo);
                        psInsert.setInt(2, idTipoVisita);
                        psInsert.addBatch();
                    } else {
                        System.out.println("Tipo Visita non trovato per titolo: " + tv.getTitolo());
                    }
                }
            }
            psInsert.executeBatch();
        }
    }

    //=======================================================================
  	//*********************** LETTURA**** ***********************************
  	//=======================================================================
    
    
 // Legge tutti i Luoghi dal database
    public ListaLuogo leggiTuttiLuoghi(ListaTipoVisita listaTV) throws SQLException {
    	ListaLuogo listaLuoghi = new ListaLuogo();
        
        String sql = "SELECT l.ID, l.NOME, l.DESCRIZIONE, l.FK_INDIRIZZO, " +
                     "i.TIPO_STRADA, i.NOME_STRADA, i.NUM_CIVICO, i.CAP, i.COMUNE, i.PROVINCIA " +
                     "FROM luogo l " +
                     "JOIN indirizzo i ON l.FK_INDIRIZZO = i.ID";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int idLuogo = rs.getInt("ID");
                String nome = rs.getString("NOME");
                String descrizione = rs.getString("DESCRIZIONE");
                
                // Indirizzo
                TipoStrada tipoStrada = TipoStrada.valueOf(rs.getString("TIPO_STRADA"));
                String nomeStrada = rs.getString("NOME_STRADA");
                String numCivico = rs.getString("NUM_CIVICO");
                int cap = rs.getInt("CAP");
                String comune = rs.getString("COMUNE");
                String provincia = rs.getString("PROVINCIA");
                
                Indirizzo indirizzo = new Indirizzo(tipoStrada, nomeStrada, numCivico, cap, comune, provincia);
                
                // Leggi tipi visita associati
                ListaTipoVisita tipiVisita = leggiTipiVisitaPerLuogo(conn, idLuogo, listaTV);
                
                // Crea oggetto Luogo
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
                     "WHERE ltv.FK_LUOGO = ?";
        
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
                System.out.println("ATTENZIONE: TipoVisita con titolo '" + titolo + "' NON trovato nella lista globale!");
            }
        }
        return listaTipi;
    }


    
}
