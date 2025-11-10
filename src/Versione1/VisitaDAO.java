package Versione1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VisitaDAO {

    // Salva una visita effettiva nel DB con i campi reali (ID volontario!)
	public void salvaVisita(Visita visita, Volontario v) throws SQLException {
	    String sqlVisita = "INSERT INTO visita (DATA_GIORNO, DATA_MESE, STATO, TITOLO_TIPO_VISITA) VALUES (?, ?, ?, ?)";
	    String sqlPonte = "INSERT INTO visita_volontario (FK_VISITA, FK_CREDENZIALI) VALUES (?, ?)";
	    
	    // Recupero id volontario
	    int idVolontario = UtenteDAO.trovaIdVolontarioDaNickname(v.getNickname());
	    if (idVolontario == -1) {
	        throw new SQLException("Volontario non trovato per username: " + v.getNickname());
	    }

	    try (Connection conn = ConnessioneDB.startConnection();
	         PreparedStatement psVisita = conn.prepareStatement(sqlVisita, java.sql.Statement.RETURN_GENERATED_KEYS);
	    ) {
	        // 1. Inserisci la visita (senza volontario)
	        psVisita.setInt(1, visita.getDataVisita().getGiorno());
	        psVisita.setInt(2, visita.getDataVisita().getMese());
	        psVisita.setString(3, visita.getStatoVisita().name());
	        psVisita.setString(4, visita.getTipoVisita().getTitolo());
	        psVisita.executeUpdate();

	        int idVisita = -1;
	        // 2. Recupera l'ID generato della visita appena inserita
	        try (ResultSet generatedKeys = psVisita.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                idVisita = generatedKeys.getInt(1);
	            } else {
	                throw new SQLException("Errore nel recupero ID visita generata.");
	            }
	        }

	        // 3. Collega la visita al volontario nella tabella ponte
	        try (PreparedStatement psPonte = conn.prepareStatement(sqlPonte)) {
	            psPonte.setInt(1, idVisita);
	            psPonte.setInt(2, idVolontario);
	            psPonte.executeUpdate();
	        }
	    }
	}

	public void collegaVolontarioAVisitaDB(Visita visita, Volontario volontario) throws SQLException {
	    // Query per trovare l'ID della visita esistente
	    String queryIdVisita = "SELECT ID FROM visita WHERE DATA_GIORNO = ? AND DATA_MESE = ? AND STATO = ? AND TITOLO_TIPO_VISITA = ?";
	    String sqlInsert = "INSERT INTO visita_volontario (FK_VISITA, FK_CREDENZIALI) VALUES (?, ?)";

	    // Recupera l'id volontario da nickname
	    int idVolontario = UtenteDAO.trovaIdVolontarioDaNickname(volontario.getNickname());
	    if (idVolontario <= 0) {
	        throw new SQLException("ID volontario non trovato per nickname: " + volontario.getNickname());
	    }

	    int idVisita = -1;
	    try (Connection conn = ConnessioneDB.startConnection();
	         PreparedStatement psFind = conn.prepareStatement(queryIdVisita)) {

	        psFind.setInt(1, visita.getDataVisita().getGiorno());
	        psFind.setInt(2, visita.getDataVisita().getMese());
	        psFind.setString(3, visita.getStatoVisita().name());
	        psFind.setString(4, visita.getTipoVisita().getTitolo());

	        try (ResultSet rs = psFind.executeQuery()) {
	            if (rs.next()) {
	                idVisita = rs.getInt("ID");
	            } else {
	                throw new SQLException("Visita non trovata in tabella per i parametri forniti");
	            }
	        }

	        // Ora inserisce nella tabella ponte
	        try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
	            psInsert.setInt(1, idVisita);
	            psInsert.setInt(2, idVolontario);
	            psInsert.executeUpdate();
	        }
	    
	    }

	}
	
	public static int trovaIdVisita(Visita visita) throws SQLException {
	    String query = "SELECT id FROM Visita WHERE TITOLO_TIPO_VISITA = ? AND DATA_GIORNO = ? AND DATA_MESE = ?";
	    try (Connection conn = ConnessioneDB.startConnection();
	         PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setString(1, visita.getTipoVisita().getTitolo());
	        ps.setInt(2, visita.getDataVisita().getGiorno());
	        ps.setInt(3, visita.getDataVisita().getMese());
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("id");
	            }
	        }
	    }
	    return null; // non trovato
	}

	
	public static void aggiornaStatoVisitaDB(Visita visita, StatoVisita nuovoStato) throws SQLException {
	    String sql = "UPDATE visita SET STATO = ? WHERE DATA_GIORNO = ? AND DATA_MESE = ? AND TITOLO_TIPO_VISITA = ?";
	    try (Connection conn = ConnessioneDB.startConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, nuovoStato.name());
	        ps.setInt(2, visita.getDataVisita().getGiorno());
	        ps.setInt(3, visita.getDataVisita().getMese());
	        ps.setString(4, visita.getTipoVisita().getTitolo());
	        int updatedRows = ps.executeUpdate();
	        if (updatedRows == 0) {
	            throw new SQLException("Nessuna visita aggiornata: controlla i parametri di ricerca.");
	        }
	    }
	}

}

