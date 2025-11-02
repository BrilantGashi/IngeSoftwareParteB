package Versione1;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class UtenteDAO {

	public static Credenziali leggiSingolaCredenziale(String username) {
        //String query = "SELECT * FROM tipologiautente WHERE USERNAME = ' " + username + "'";
        /*String query = "SELECT credenziali.USERNAME, credenziali.PASSWORD, tipologiautente.TIPO "
        		+ " FROM credenziali "
        		+ " JOIN tipologiautente ON credenziali.ID_TIPOUTENTE = tipologiautente.ID "
        		+ " WHERE credenziali.USERNAME = '" + username + "';";*/
		String query = "SELECT credenziali.USERNAME, credenziali.PASSWORD, tipologiautente.TIPO " +  
	               "FROM credenziali " +                                                        
	               "JOIN tipologiautente ON credenziali.ID_TIPOUTENTE = tipologiautente.ID " +  
	               "WHERE credenziali.USERNAME = '" + username + "';";                       

        
        try {
            Connection conn = ConnessioneDB.startConnection(); // ottiene la connessione
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            
            String[] credenziali = new String[3];
            
            
            if (rs.next()) { // sposta il cursore sulla prima riga
            	/*
                credenziali[0] = rs.getNString("TIPO");
                credenziali[1] = rs.getString("USERNAME");
                credenziali[2] = rs.getString("PASSWORD");*/
            	String utenza = rs.getString("TIPO");
                String user = rs.getString("USERNAME");
                String pass = rs.getString("PASSWORD");
                
                // Crea e restituisce l'oggetto Credenziali con tutti i dati
                return new Credenziali(utenza, user, pass);
            } else {
                System.out.println("Nessun risultato trovato.");
            }
           /* while (rs.next()) {
                int id = rs.getInt("ID");
                String tipo = rs.getString("TIPO");
                System.out.println(id + " - " + tipo);
            }*/

            rs.close();
            ps.close();
            
            
            ConnessioneDB.closeConnection();
            //return credenziali;
        } catch (SQLException e) {
            System.out.println("Errore durante la lettura: " + e.getMessage());
        }
        return null;
    }
	
	public void stampaCredenziale(String username) {
        //String[] dati = leggiSingolaCredenziale(username);
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
					+ "	SET credenziali.PASSWORD='" + nuovaPass + "' "					
					+ " WHERE credenziali.USERNAME='"+ username +"';";
		
		Connection conn = ConnessioneDB.startConnection(); // ottiene la connessione
        PreparedStatement ps = conn.prepareStatement(query);
        //ResultSet rs = ps.executeQuery();
        int righeAggiornate = ps.executeUpdate();
        
        if (righeAggiornate > 0) {
            trovato = true;  
        } 

        ps.close();
        ConnessioneDB.closeConnection();
        
		return trovato;
	}
	
	public void leggiUtentiDaDB(ArrayList<Configuratore> configuratori, ListaVolontari volontari) throws SQLException, FileNotFoundException, IOException {
	    String sql = "SELECT id, username, password, id_tipoutente FROM credenziali";

	    // Lista temporanea per salvare i dati letti dal DB
	    ArrayList<DatiUtente> datiUtenti = new ArrayList<>();

	    // Leggi tutti i dati dal database
	    try (Connection conn = ConnessioneDB.startConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            int idTipoUtente = rs.getInt("id_tipoutente");
	            String username = rs.getString("username");
	            String password = rs.getString("password");
	            // Salva i dati in una struttura temporanea
	            datiUtenti.add(new DatiUtente(username, password, idTipoUtente));
	        }
	    }
	    // Ora crea gli oggetti dalle informazioni salvate
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
	                // gestisci fruitore se serve, es:
	                // fruitori.add(new Fruitore(dati.username, dati.password));
	                break;
	            default:	                
	        }	        
	    }
	}

	// Classe interna privata per memorizzare temporaneamente i dati
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
	
	//NUOVO METODO PER LA GESTIONE DEI NUOVI VOLONTARI 
	//CHE SI POSSONO INSERIRE NELLA V3
	
	public void inserisciNuovoVolontario (Volontario v) throws SQLException {
		String sql = "INSERT INTO credenziali (USERNAME, PASSWORD, ID_TIPOUTENTE) VALUES (?, ?, ?)";
		
		try (Connection conn = ConnessioneDB.startConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)){
			
			ps.setString(1,  v.getCredenziali().getUsername());
			ps.setString(2,  v.getCredenziali().getPassword());
			ps.setInt(3, 2); // 2 = ID, e' il volontario nella tabella tipologiautente
			ps.executeUpdate();
			
			System.out.println("Nuovo volontario salvato" + v.getCredenziali().getUsername());
		} catch (SQLException e) {
			System.out.println("Errore durante l'inserimento del nuovo volontario: " + e.getMessage());
			throw e;
		}
			
		
	}

	public static void rimuoviVolontario(Volontario v) throws SQLException {
		
	    String selectIdSql = "SELECT ID FROM credenziali WHERE USERNAME = ?";
	    String deleteVisiteSql = "DELETE FROM tipovisita_volontario WHERE fk_volontario = ?";
	    String deleteDisponibilitaSql = "DELETE FROM disponibilita WHERE fk_volontario = ?";
	    
	    try (Connection conn = ConnessioneDB.startConnection()) {
	        conn.setAutoCommit(false); // Inizia transazione
	        
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
	            
	            conn.commit(); // Conferma transazione
	            System.out.println("Associazioni volontario eliminate con successo!");
	            
	        } catch (SQLException e) {
	            conn.rollback(); // Annulla tutto in caso di errore
	            throw e;
	        }
	    }
		
	}
	
	public static void eliminaDaCredenziali(Volontario v) throws SQLException {
	    String deleteSql = "DELETE FROM credenziali WHERE USERNAME  = ?";
	    
	    try (Connection conn = ConnessioneDB.startConnection();
	         PreparedStatement ps = conn.prepareStatement(deleteSql)) {
	        
	        ps.setString(1, v.getNickname());
	        int righe = ps.executeUpdate();
	        
	        if (righe > 0) {
	            System.out.println("Volontario eliminato da credenziali");
	        } else {
	            System.out.println("Nessuna riga eliminata da credenziali");
	        }
	    }
	}

	





}
