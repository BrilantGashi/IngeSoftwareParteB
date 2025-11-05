package Versione1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DisponibilitaDAO {
	
	/*
	 * classe per la gestione della disponibilita' dei volontari, permette
	 * di salvare e leggere i giorni in cui un volontario � disponibile	
	 */

	public void salvaDisponibilita(String username, int giorno, String mese) throws SQLException {
		
		//controlla se esiste gi� una disponibilit�
		String checkSql = "SELECT COUNT(*) FROM disponibilita WHERE username = ? AND giorno = ? AND mese = ?";
		
		//inserisce nuova disponibilit� dopo aver verificato che non esiste gi�
		String insertSql = "INSERT INTO disponibilita (username, giorno, mese) VALUES (?, ?, ?)";
	
		try(Connection conn = ConnessioneDB.startConnection();
			PreparedStatement checkPs = conn.prepareStatement(checkSql);
			PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
			
			//mi sostituiscono i ? con i valori
			checkPs.setString(1, username);
			checkPs.setInt(2, giorno);
			checkPs.setString(3, mese);
			 
			//esegue la query e restituisce il risultato
			ResultSet rs = checkPs.executeQuery();
			
			//sposto il cursore alla prima riga e prende la prima colonna, ossia il risultato di count
			//count = 0 --> non esiste disponibilit� per quel mese
			//count = 1 --> esiste disponibilit� 
			rs.next();
			int count = rs.getInt(1);
			
			//inserisco la nuova disponibilit� solo se non esiste
			if (count == 0) {
				insertPs.setString(1, username);
				insertPs.setInt(2, giorno);
				insertPs.setString(3, mese);
				
				insertPs.executeUpdate();
				System.out.println("Disponibilita' salvata per " + username + " (" + giorno + " " + mese + ")");
			} else {
				System.out.println("La disponibilita' per " + username + " nel mese di " + mese + " esiste gia'.");
			}
			
		} catch (SQLException e) {
			System.out.println("Errore durante il salvataggio della disponibilita': " + e.getMessage());
			throw e;
		}
		
	}
	
	
	public ArrayList<Disponibilita> leggiDisponibilitaVolontario(String username) throws SQLException {
		
		ArrayList<Disponibilita> listaDisponibilita = new ArrayList<>();
		
		//prende tutte le righe della tabella disponibilita' dove
		//lo username � uguale a quello passato come parametro
		//String query = "SELECT giorno, mese FROM disponibilita WHERE username = ?";
		String query = 
			    "SELECT d.giorno, d.mese " +
			    "FROM disponibilita d " +
			    "INNER JOIN credenziali c ON d.fk_volontario = c.ID " +
			    "WHERE c.USERNAME = ?";

		
		
		try (Connection conn = ConnessioneDB.startConnection();
			 PreparedStatement ps = conn.prepareStatement(query)) {
			
			//sostituisce i ? con lo username
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				int giorno = rs.getInt("giorno");
				String mese = rs.getString("mese");
				
				Disponibilita disponibilita = new Disponibilita(username, giorno, mese);
				listaDisponibilita.add(disponibilita);
			}
			
		} catch (SQLException e) {
			System.out.println("Errore durante la lettura delle disponibilita': " + e.getMessage());
			throw e;
		}
		
		return listaDisponibilita;
	}
	
	
	
	//stampa le disponibilit� di un volontario
	
	public void stampaDisponibilitaVolontario(String username) throws SQLException {
		ArrayList<Disponibilita> listaDisponibilita = leggiDisponibilitaVolontario(username);
		
		if (listaDisponibilita.isEmpty()) {
            System.out.println("Nessuna disponibilit� trovata per " + username);
        } else {
            System.out.println("===== DISPONIBILIT� DI " + username.toUpperCase() + " =====");
            for (Disponibilita d : listaDisponibilita) {
                System.out.println("- Giorno " + d.getGiorno() + " di " + d.getMese());
            }
            System.out.println("==========================================");
        }
	}
	
	
}
