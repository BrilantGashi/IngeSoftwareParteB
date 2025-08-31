package Versione1;
import java.io.*;
import java.time.LocalTime;
import java.util.*;

import it.unibs.fp.mylib.*;

public class Menu{
	private static String[] voci = {"Registra Configuratore", "Definisci Volontari", "Visualizza Volontari"};
	private static ArrayList<String> listaVolontari = new ArrayList<String>();
	private static ListaTipoVisita listaTipoVisita = new ListaTipoVisita();
	private static ListaLuogo listaLuogo = new ListaLuogo();
	
	private static GestioneFile g = new GestioneFile();
	
	//private static final String LOG_FILENAME = "matriciopoli.log";
	//////////////////////////////////
	// MENU PRINCIPALE
	//////////////////////////////////
	/**
	 * Metodo per il menu principale dell'applicazione.
	 * @return True se e' stata selezionata l'uscita, altrimenti False.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static boolean menuPrincipale() throws FileNotFoundException, IOException{
		
		int sceltaMenu;
				
			MyMenu menu = new MyMenu("Menu", voci);
			sceltaMenu = menu.scegli();
			
			switch (sceltaMenu) {
			case 0:
				return InputDati.yesOrNo("Vuoi veramente uscire?");
				
				
			case 1:
				login();
				break;
				
			case 2:
				//definizioneVolontari();
				//visualizzaVolontari();
				break;
				
			case 3:
				visualizzaVolontari();
				break;
			
			
			}
			return false;
		
		
	}
	
	public static void login() throws FileNotFoundException, IOException {
		String username;
		String password;
		String utenza = "";
		
	
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
		 	- 0 se non è il primo accesso
		 	- 1 se è il primo accesso
		 	
		 	 questa info la devo mandare al metodo configuratore che, se è il primo accesso, deve fare determinate cose
		 */
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
	
	public static void configuratore (int okCred) throws FileNotFoundException, IOException {
		/*
			cose che deve fare un configuratore nella V1:
			- creazione corpo dati: prima chiedere in input tutti i dati, e alla fine scriverli sul file
			- avviare calendarizzazione
			
		*/
		
		if (okCred == 1) {	//cioè se è il primo accesso
			//a. creazione corpo dati -- TUTTO QUESTO DOVRA' POI ESSERE MESSO IN CLASSE CONFIGURATORE (o un altro metodo in classe Menu, da pensare)
			
			System.out.println("Creazione Corpo Dati");
			
			//**************************AMBITO TERRITORIALE ***********************************
			System.out.println("Creazione Ambito Territoriale");
			
			String provincia;
			provincia = InputDati.leggiStringaNonVuota("Inserisci la provincia: ");
			
			ArrayList<String> cap = new ArrayList<String>();
			int continua = 0;
			do {
				
				 cap.add(InputDati.leggiStringaNonVuota("Inserisci almeno un cap: "));
				 
				 continua = InputDati.leggiIntero("Vuoi inserire un altro cap? 1 per inserire, 0 per uscire. ");
			} while (continua != 0);
			
			AmbitoTerritoriale ambitoTerritoriale = new AmbitoTerritoriale(cap, provincia);
			
			
			//********************** N MAX PARTECIPANTI PER FRUITORE ***********************************
			/*fissare il numero massimo di persone che un fruitore può iscrivere a una iniziativa 
			mediante una singola iscrizione (tale numero è valido per ogni visita)*/			
			int nMaxPersone = 0;
			
			nMaxPersone = InputDati.leggiInteroPositivo("Inserisci il numero massimo di persone che un fruitore può iscrivere a una iniziativa mediante una singola iscrizione");
			
			//********************** LUOGHI ***********************************
			
			//prerequisiti: lista volontari --FATTO**** -->metodo visualizzaVolontari
			visualizzaVolontari();
			
			//1. definizione TipoVisita e poi inserite in ListaTipoVisita, da cui verranno pescate per essere mostrate e scelte dal config
			
			definizioneTipoVisita();
						
			//2. chiedere quanti luoghi vuole definire
			
			int luoghi = InputDati.leggiInteroNonNegativo("Quanti luoghi vuoi definire?");
			
			//3. per ogni luogo, scegliere tipo visita e almeno un volontario
			
			for (int i = 0; i < luoghi; i++) {
				//definizione luogo a cui si andra' ad associare un tipo visita e almeno un volontario
				System.out.println("Creazione luogo n. " + i+1 + ": ");				
				definizioneLuogo();							
			}		
		}
		
		
	}

	public static void visualizzaVolontari () throws FileNotFoundException, IOException {
		
		listaVolontari = g.restituisciListaUtenza("VOLONTARIO");
		
		for (String nickname : listaVolontari) {
			System.out.println(nickname);
		}
	}
	
	public static void definizioneTipoVisita() {
		
		//prima definisco il singolo tipo visita, e poi lo aggiungo alla lista listaTipoVisita 
		
		
		String titolo = InputDati.leggiStringaNonVuota("Inserisci il titolo del tipo visita: ");
		String descrizione = InputDati.leggiStringaNonVuota("Inserisci una breve descrizione del tipo visita: ");
		
		
		Indirizzo puntoDiIncontro = ObjIndirizzo();;
	    Data dataVisita = ObjData("Inserisci il giorno della visita: ", "Inserisci il mese della visita: ");	    
	    LocalTime oraInizio = ObjOraFLT();
	    int durataVisita = InputDati.leggiInteroPositivo("Inserisci in minuti la durata della visita: ");
	    //int biglietto = InputDati.leggiIntero("Inserisci 0 se NON serve il biglietto, 1 altrimenti.", 0, 1);
	    boolean biglietto = InputDati.yesOrNo("Inserisci N se NON serve il biglietto, S altrimenti.");
	    int sogliaMinPartecipanti = InputDati.leggiInteroNonNegativo("Inserisci soglia MINIMA dei partecipanti: ");
	    int sogliaMaxPartecipanti = InputDati.leggiInteroNonNegativo("Inserisci soglia MASSIMA dei partecipanti: ");
		Periodo periodoProgrammabile = ObjPeriodo();	
		
		TipoVisita tipoVisita = new TipoVisita(titolo, descrizione,  puntoDiIncontro, oraInizio,
				durataVisita, biglietto, sogliaMinPartecipanti, sogliaMaxPartecipanti, periodoProgrammabile);
		
		System.out.println(tipoVisita.toString());
		
		
		
		//listaTipoVisita.add(tipoVisita);
		
	}
	
	public static Indirizzo ObjIndirizzo () {
		
		TipoStrada tipoStrada =  TipoStrada.scegliDaNome();
		String nomeStrada = InputDati.leggiStringaNonVuota("Inserisci il nome della Strada: ");
		String numeroCivico = InputDati.leggiStringaNonVuota("Inserisci il numero civico: ");		
		String cap = InputDati.leggiStringaNonVuota("Inserisci il cap: ");
		String comune = InputDati.leggiStringaNonVuota("Inserisci il Comune: ");		
		String provincia = InputDati.leggiStringaNonVuota("Inserisci Provincia: ");
		
		Indirizzo indirizzo = new Indirizzo (tipoStrada, nomeStrada, numeroCivico, cap, comune, provincia);
		return indirizzo;
	}
	
	public static Data ObjData (String descrizioneGiorno, String descrizioneMese) {
		int giorno = InputDati.leggiInteroPositivo(descrizioneGiorno);
		int mese = InputDati.leggiInteroPositivo(descrizioneMese);
		
		Data data = new Data(giorno, mese);
		
		return data;
		
	}
	
	public static LocalTime ObjOraFLT () { //obj Ora Formato LocalTime
		int ora = InputDati.leggiIntero("Inserisci ora inizio: ", 0, 23);
		int minuti = InputDati.leggiIntero("Inserisci minuti inizio: ", 0, 59);
		LocalTime orario = LocalTime.of(ora, minuti);
		
		return orario;
	}

	public static Periodo ObjPeriodo () {
		Data dataInizio = ObjData("Inserisci il giorno di inizio del periodo: ", "Inserisci il mese di inizio del periodo: ");
		Data dataFine = ObjData("Inserisci il giorno di fine del periodo: ", "Inserisci il mese di fine del periodo: ");
		
		Periodo periodo = new Periodo (dataInizio, dataFine);
		
		return periodo;
		
	}

	public static void definizioneLuogo () {
		String nomeLuogo = InputDati.leggiStringaNonVuota("Inserisci il nome del luogo: ");
		String descrizione = InputDati.leggiStringa("Inserisci una descrizione del luogo: ");
		Indirizzo indirizzo = ObjIndirizzo();
		
		//ogni LUOGO dovrebbe contenere al suo interno una LISTA DI TIPI LUOGO (pg. 4, "A ciascun luogo è associato almeno un tipo di visita")
		//far scegliere il tippo visita in base al nome (quindi bisogna fare controlli affinchè il nome sia univoo)
		
	}
}