package Versione1;

import java.io.*;
import it.unibs.fp.mylib.*;
import java.util.*;


public class Main {

	private static String[] voci = {"Registra Configuratore"};
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		int sceltaMenu;
		
		MyMenu menu = new MyMenu("Menu", voci);
		sceltaMenu = menu.scegli();
		
		switch (sceltaMenu) {
		case 0:
			break;
			
		case 1:
			accesso();
			break;
		
		
		}
		
		
		

	}
	
	/*METODO TMP PER REINSERIMENTO PASS SE SBAGLIO E CONTROLLO CHE ESISTA USERNAME
	public static void login() throws FileNotFoundException, IOException {
		String username;
		String password;
		String utenza = "";
		GestioneFile g = new GestioneFile();
		
		System.out.println("Inserisci le credenziali che ti sono state fornite dall'amministrazione: ");
		username = InputDati.leggiStringaNonVuota("Username: ");
		password = InputDati.leggiStringaNonVuota("Password: ");
		if (g.leggiSingolaCredenziale(username) == null) {
			System.out.println("Username errato");
		}
		Credenziali credenziali = new Credenziali (username);
	}
	
	*/
	
	
	
	
	/**
	 * Metodo per entrare nel portale
	 * 
	 */
	public static void accesso() throws FileNotFoundException, IOException {
		String username;
		String password;
		String utenza = "";
		
		System.out.println("Inserisci le credenziali che ti sono state fornite dall'amministrazione: ");
		username = InputDati.leggiStringaNonVuota("Username: ");
		password = InputDati.leggiStringaNonVuota("Password: ");
		Credenziali credenziali = new Credenziali (username);
		
		/*
		 	okCred restituisce: 
		 	- -1 se credenziali sbagliate
		 	- 0 se non è il primo accesso
		 	- 1 se è il primo accesso
		 	
		 	 questa info la devo mandare al metodo configuratore che, se è il primo accesso, deve fare determinate cose
		 */
		int okCred = 0;
		okCred = credenziali.controllaCredenziali(username, password);
		
		
		if (okCred != -1) {
			utenza = credenziali.getUtenza();
			System.out.println("ok credenziali");
		}
		
		
		switch (utenza) {
		case "CONFIGURATORE":
			configuratore(okCred);
			break;
			
		default:
			System.out.println("Credenziali errate");
			break;			
		}		
	}
	
	public static void configuratore (int okCred) {
		/*
			cose che deve fare un configuratore nella V1:
			
		*/
		
		if (okCred == 1) {	//cioè se è il primo accesso
			//creazione corpo dati
		}
		
		
		
		
		
	}
	
	

}
