package Versione1;

import java.io.*;
import it.unibs.fp.mylib.*;
import java.util.*;


public class Main {

	//private static String[] voci = {"Registra Configuratore"};
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		//---------------------------------
		

				//System.out.println("Unibs Software Engineering");
				boolean fineProgramma = false;
				
				do {
					fineProgramma = Menu.menuPrincipale();
				} while (!fineProgramma);
				
				System.out.println("Arrivederci!");
	
		
		
		/*
		//---------------------------------
		
		int sceltaMenu;
		
		MyMenu menu = new MyMenu("Menu", voci);
		sceltaMenu = menu.scegli();
		
		switch (sceltaMenu) {
		case 0:
			break;
			
		case 1:
			login();
			break;
		
		
		}
		
*/
	}
	
	/*
	public static void login() throws FileNotFoundException, IOException {
		String username;
		String password;
		String utenza = "";
		GestioneFile g = new GestioneFile();

		System.out.println("Inserisci lo username che ti e' stato fornito dall'amministrazione: ");
		username = InputDati.leggiStringaNonVuota("Username: ");
		
		while (g.leggiSingolaCredenziale(username) == null) {				
			username = InputDati.leggiStringaNonVuota("Username inserito non esistente, riprovare: ");
			
		}
		Credenziali credenziali = new Credenziali (username);
		
		password = InputDati.leggiStringaNonVuota("Password: ");

		/*
		 	okCred restituisce: 
		 	- -1 se credenziali sbagliate
		 	- 0 se non � il primo accesso
		 	- 1 se � il primo accesso
		 	
		 	 questa info la devo mandare al metodo configuratore che, se � il primo accesso, deve fare determinate cose
		 */
	/*
		int okCred = 0;
		okCred = credenziali.controllaCredenziali(username, password);
		
		while (okCred == -1) {
			password = InputDati.leggiStringaNonVuota("Password errata, riprovare: ");
			okCred = credenziali.controllaCredenziali(username, password);
		}
		
		
		utenza = credenziali.getUtenza();
		
		switch (utenza) {
		case "CONFIGURATORE":
			configuratore(okCred);
			break;
			
		default:						
			break;			
		}
	}
	
	
	
	public static void configuratore (int okCred) {
		/*
			cose che deve fare un configuratore nella V1:
			- creazione corpo dati: prima chiedere in input tutti i dati, e alla fine scriverli sul file
			- avviare calendarizzazione
			
		*/
		/*
		if (okCred == 1) {	//cio� se � il primo accesso
			//creazione corpo dati
		}
		
		
		
		
		
	}
	
	*/

}
