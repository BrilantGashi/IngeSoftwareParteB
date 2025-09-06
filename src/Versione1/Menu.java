package Versione1;
import java.io.*;
import java.time.LocalTime;
import java.util.*;

import it.unibs.fp.mylib.*;

public class Menu{
	private static String[] voci = {"Login"};
	private static ArrayList<Configuratore> listaConfiguratore = new ArrayList<Configuratore>();
	private static ArrayList<Volontario> listaVolontari = new ArrayList<Volontario>();
	private static ListaTipoVisita listaTipoVisita = new ListaTipoVisita();
	private static ListaLuogo listaLuogo = new ListaLuogo();
	private static int nMaxPersone = 0;
	
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
		g.leggiUtentiDaFile(listaConfiguratore, listaVolontari);
				
		MyMenu menu = new MyMenu("Menu", voci);
		sceltaMenu = menu.scegli();
		
		switch (sceltaMenu) {
		case 0:
			return InputDati.yesOrNo("Vuoi veramente uscire?");
			
			
		case 1:
			login();
			break;
			
		case 2:				
			//testWriteXML();
			break;
			
		case 3:
			//visualizzaVolontari();
			break;
		
		
		}
		return false;
		
		
	}
	
	public static void login() throws FileNotFoundException, IOException {
		String username;
		String password;
		String utenza = "";
		
		//***************************POSSIBILE FARLO MEGLIO, CONTROLLANDO SOLO ARRAYLIST CONFIGU********************
	
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
			- modificare n max persone per fruitore
			- visualizzare elenco dei luoghi 
			- visualizzare tipi visita per ciascun luogo
			- visualizzare visita con relativo stato 
			
		*/
		
		int sceltaMenuConfig;
		System.out.println("NON VANNO TUTTI, CONTROLLARE PRIMA DI PROCEDERE");
		String[] vociConfig = {"Creazione Corpo Dati", "Avvio Calendario", "Modifica num max per fruitore", "Visualizza Luoghi", "Visualizza Tipo Visita", "Visualizza Stato Visita"};
		//g.leggiUtentiDaFile(listaConfiguratore, listaVolontari);
				
		MyMenu menu = new MyMenu("Menu", vociConfig);
		sceltaMenuConfig = menu.scegli();
		
		switch (sceltaMenuConfig) {
		case 0:
			//IMPLEMENTARE DOPO
			/*
			boolean indietro = InputDati.yesOrNo("Vuoi veramente tornare indietro?");
			if (indietro) {
				menuPrincipale();
			}
			*/
			
			
		case 1:
			if (okCred == 1) {
				creazioneCorpoDati();
			} else {
				System.out.println("Corpo Dati già definito.");
				//e devo leggere i dati da file
				leggiCorpoDati();
			}
			break;
			
		case 2:				
			//avvio calendario
			System.out.println("sei nel case 2");
			break;
			
		case 3:			
			System.out.println("sei nel case 3");
			modificaNumFruitore();			
			break;
			
		case 4:
			//visualizza elenco luoghi
			System.out.println("sei nel case 4");
			//non va perchè non inizializzo con info file
			//devo vedere se NON E' il primo accesso, in tale caso fare inizzializzazione
			System.out.println(listaLuogo.toString());
			
			break;
		
		
		}
		
		
		
		
	}
	
	public static void modificaNumFruitore() {
		nMaxPersone = InputDati.leggiInteroPositivo("Inserisci il numero massimo di persone che un fruitore può iscrivere a una iniziativa mediante una singola iscrizione: ");
	}
	
	public static void creazioneCorpoDati () {
		//a. creazione corpo dati			
		System.out.println("Creazione Corpo Dati");
		
		//**************************AMBITO TERRITORIALE ***********************************
		System.out.println("------------------------------");
		System.out.println("Creazione Ambito Territoriale");
		System.out.println("------------------------------");
		
		String provincia;
		provincia = InputDati.leggiStringaNonVuota("Inserisci la provincia: ");
		
		ArrayList<String> cap = new ArrayList<String>();
		boolean continua;
		do {
			
			 cap.add(InputDati.leggiStringaNonVuota("Inserisci almeno un cap: "));
			 
			 continua = InputDati.yesOrNo("Vuoi inserire un altro cap? S per inserire, N per uscire. ");
		} while (continua);
		
		AmbitoTerritoriale ambitoTerritoriale = new AmbitoTerritoriale(cap, provincia);
		
		
		//********************** N MAX PARTECIPANTI PER FRUITORE ***********************************
		/*fissare il numero massimo di persone che un fruitore può iscrivere a una iniziativa 
		mediante una singola iscrizione (tale numero è valido per ogni visita)*/
		//**************************AMBITO TERRITORIALE ***********************************
		System.out.println("------------------------------");
		System.out.println("NUM MAX PARTECIPANTI PER FRUITORE");
		System.out.println("------------------------------");
		
		modificaNumFruitore();
		//********************** LUOGHI ***********************************
		
		//prerequisiti: lista volontari --FATTO**** -->metodo visualizzaVolontari
		//visualizzaVolontari();
		
		//1. definizione TipoVisita e poi inserite in ListaTipoVisita, da cui verranno pescate per essere mostrate e scelte dal config
		
		System.out.println("------------------------------");
		System.out.println("DEFINIZIONE TIPI VISITA");
		System.out.println("------------------------------");
		
		boolean continuoTipoVisita;
		do {
			System.out.println();
			definizioneTipoVisita();
			continuoTipoVisita = InputDati.yesOrNo("Vuoi inserire un altro Tipo Visita?");
		} while (continuoTipoVisita);
		

		System.out.println("------------------------------");
		System.out.println("DEFINZIONE LUOGHI");
		System.out.println("------------------------------");
		//2. chiedere quanti luoghi vuole definire
		
		int luoghi = InputDati.leggiInteroNonNegativo("Quanti luoghi vuoi definire?");
		
		//3. per ogni luogo, scegliere tipo visita e almeno un volontario
		
		for (int i = 0; i < luoghi; i++) {
			//definizione luogo a cui si andra' ad associare un tipo visita e almeno un volontario
			System.out.println();
			System.out.println("Creazione luogo n. " + (i+1) + ": ");
			System.out.println();
			definizioneLuogo();							
		}
		
		try {
			g.scriviCorpoDati(nMaxPersone, ambitoTerritoriale, listaLuogo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*mbitoTerritoriale ambito,
        ListaLuogo listaLuoghi) throws Exception {*/

		
	}

	public static void visualizzaVolontari (){
		
		//listaVolontari = g.restituisciListaUtenza("VOLONTARIO");
		//int i = 0;
		/*for (String nickname : listaVolontari)) {
			System.out.println(nickname);
		}*/
		
		for (int i = 0; i < listaVolontari.size(); i++) {
			System.out.println((i+1) + ". "+ listaVolontari.get(i).getCredenziali().getUsername());
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
		
		boolean continuaVolontario = false;
		int sceltaVolontario;
		ArrayList<Volontario> volontariToAdd = new ArrayList<>();
		
		do {			
			visualizzaVolontari();
			sceltaVolontario = InputDati.leggiInteroNonNegativo("Inserisci il numero del volontario da associare al tipo visita: ");
			volontariToAdd.add(listaVolontari.get(sceltaVolontario - 1));
			continuaVolontario = InputDati.yesOrNo("Vuoi associare un altro Volontario a questo Tipo Visita? ");
		} while(continuaVolontario);
		
		TipoVisita tipoVisita = new TipoVisita(titolo, descrizione,  puntoDiIncontro, dataVisita, oraInizio,
				durataVisita, biglietto, sogliaMinPartecipanti, sogliaMaxPartecipanti, periodoProgrammabile, volontariToAdd);
		
		System.out.println(tipoVisita.toString());
		
		listaTipoVisita.add(tipoVisita);
		
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
		
		//ogni LUOGO dovrebbe contenere al suo interno una LISTA DI TIPI VISITA (pg. 4, "A ciascun luogo è associato almeno un tipo di visita")
			
		
		//mostrare tutti i tipi visita
		//chiedere quale associare al luogo
		//chiedere se ne vogliono associare un altro
					
		boolean continuaTipo = false;
		int sceltaTipo;
		//ArrayList<TipoVisita> tipiToAdd = new ArrayList<>();
		ListaTipoVisita listaTipoVisitaToAdd = new ListaTipoVisita();
		
		do {
			System.out.println(listaTipoVisita.toStringAsList());
			
			sceltaTipo = InputDati.leggiInteroNonNegativo("Inserisci il numero del Tipo Visita che vuoi associare a questo Luogo: ");
			listaTipoVisitaToAdd.add(listaTipoVisita.getTipo(sceltaTipo - 1));
			
			continuaTipo = InputDati.yesOrNo("Vuoi associare un altro Tipo Visita a questo luogo? ");
						
		} while (continuaTipo);
		
		Luogo luogo = new Luogo(nomeLuogo, descrizione, indirizzo, listaTipoVisitaToAdd);
				
		System.out.println(luogo.toString());
		
		listaLuogo.add(luogo);
	}
	
	public static void leggiCorpoDati() {
		//List<AmbitoTerritoriale> ambiti = g.leggiAmbiti();
	    //List<Luogo> luoghi = g.leggiLuoghi();
	
	    //System.out.println("Ambiti: " + ambiti);
	    //System.out.println("Luoghi: " + luoghi);
	}
	
	
}