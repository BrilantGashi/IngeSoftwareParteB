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

	
}
