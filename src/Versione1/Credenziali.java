package Versione1;
import java.io.*;
import it.unibs.fp.mylib.*;
import java.util.*;


import it.unibs.fp.mylib.*;
public class Credenziali {
	
	String username;
	String password;
	String utenza;
	
	GestioneFile g = new GestioneFile();
	//String[] credenziali = g.leggiSingolaCredenziale("utente1");
	
	//per memorizzare credenziali, credo mi convenga già memorizzarle su un file
	
	public Credenziali(String username) throws FileNotFoundException, IOException {	
		String[] credenziali = g.leggiSingolaCredenziale(username);
		this.utenza = credenziali[0];
		this.username = credenziali[1];
		this.password = credenziali[2];		
	}
	
	public Credenziali(){	
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) throws FileNotFoundException, IOException {
		this.password = password;
		if (g.aggiornaPasswordSuFile(username, password)) {
			System.out.println("Password aggiornata correttamente.");
		}
	}
	
	
	public String getUtenza() {
		return utenza;
	}

	public void setUtenza(String utenza) {
		this.utenza = utenza;
	}

	@Override
	public String toString() {
		return "Credenziali [username=" + username + ", password=" + password + "]";
	}
	
	
	
	public boolean verificaPrimoAccesso (String username, String password) throws FileNotFoundException, IOException {
		
		if (password.equalsIgnoreCase("default")) {
			String nuovaPass = InputDati.leggiStringaNonVuota("Inserisci la nuova password: ");
			
			//************************************************************************************
			//*************** MOMENTANEAMENTE BLOCCATO MA E' FUNZIONANTE, BLOCCATO SOLO PER COMODITA'*********
			//************************************************************************************
			//setPassword(nuovaPass);
			//DA FARE: aggionare anche su file --> lo faccio in setPassword
			
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Metodo che controlla se le credenziali inserite sono corrette
	 * @param usernameP e passwordP fornite dall'utente
	 * @return il tipo di utenza auth ok, altrimenti errore
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public int controllaCredenziali (String usernameP, String passwordP) throws FileNotFoundException, IOException {
		
		/*
		file strutturato nel seguente modo:
		
		TipoUtente;username;password
		TipoUtente;username2;password2
		
		se tutto okay, return true, altrimenti false
		*/
		
		if (!passwordP.equals(password)) {
			return -1;
		}
		
		if (verificaPrimoAccesso(username, password)) {
			return 1;
		}
		
		return 0;
	}
	
	
	
	
	
	

}
