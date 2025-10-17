package Versione1;
import java.io.*;
import java.time.LocalTime;
import java.util.*;

import it.unibs.fp.mylib.*;


public class Menu{
	private static AmbitoTerritoriale ambito = new AmbitoTerritoriale();
	private static Calendario cal = new Calendario();
	private static ArrayList<Integer> capAmbito = new ArrayList<Integer>();  //modifico capAmbito in Integer perchè semplifica le cose e non possiamo verificare se si tratta di una parola o un numero con string
	private static GestioneFile g = new GestioneFile();
	private static ArrayList<Configuratore> listaConfiguratore = new ArrayList<Configuratore>();
	private static ListaLuogo listaLuoghi = new ListaLuogo();
	private static ListaTipoVisita listaTipoVisita = new ListaTipoVisita();
	private static ListaVolontari listaVolontari = new ListaVolontari();
	private static ListaVisite listaVisite = new ListaVisite();
	private static int nMaxPersone = 0;
	private static String[] voci = {"Login"};

	public static void aggiuntaDataPreclusa() {
		 int giornoEscluso = InputDati.leggiIntero("Inserisci il giorno da escludere dal mese di " +  cal.getMeseDatePrecluse() + "\n", 1, 31);
		 if (!cal.checkData(giornoEscluso)) {
			 cal.aggiungiDataPreclusa(giornoEscluso);
		 } else {
			 System.out.println("Giorno gia' inserito. Inserire un altro giorno.");
		 }
		 
		 
	 }
	
	public static void calendario() {
		 System.out.println("Avvio del Calendario in corso...");		 
		 
		//********************** DATE PRECLUSE ***********************************
		 System.out.println("INSERIMENTO DATE PRECLUSE PER IL MESE DI " + cal.getMeseDatePrecluse());
		 boolean continuaDataPreclusa = false;
		 boolean inserisciDate = false;
		 
		 inserisciDate = InputDati.yesOrNo("Vuoi aggiungere delle date da escludere?");
		 if (inserisciDate) {
			 do {
				 aggiuntaDataPreclusa();
				 continuaDataPreclusa = InputDati.yesOrNo("Vuoi inserire un altra data preclusa per il mese " + cal.getMeseDatePrecluse() +"? ");
			 } while (continuaDataPreclusa);
			 
			 System.out.println(cal.toStringDatePrecluse());
		 } 
		 
		 
		 
	 }
	
	public static void configuratore (int okCred) throws Exception {
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
		//LAIBA: ho tolto 0 - Torna al Menu Principale" perch� c'� gi� di default nel menu fplib. Stesso discorso per i numeri
		String[] vociConfig = {"Creazione Corpo Dati", "Avvio Calendario", "Modifica num max per fruitore","Visualizza volontari" ,"Visualizza Luoghi", "Visualizza l'elenco dei tipi di visita associati a ciascun luogo", "Visualizza Stato Visite"};
		//g.leggiUtentiDaFile(listaConfiguratore, listaVolontari);
				
		MyMenu menu = new MyMenu("Menu", vociConfig);
		sceltaMenuConfig=1;		
		do {
			switch (sceltaMenuConfig) {
			case 0:
				//LAIBA: mi torna indietro ma non stampa conferma, da capire perch�
				boolean indietro; 
				indietro = InputDati.yesOrNo("Vuoi veramente tornare indietro?");
				if (indietro) {
					menuPrincipale();
				}
				break;
				
			case 1:
				if (okCred == 1) {
					creazioneCorpoDati();
					okCred = 0;
					
				} else {
					System.out.println("Corpo Dati già definito.");
					//e devo leggere i dati da file
					leggiCorpoDati();
				}
				break;
				
			case 2:				
				//avvio calendario				
				calendario();
				break;
				
			case 3:			
				modificaNumFruitore();			
				break;
			
			case 4:
				listaVolontari.stampaVolontariConVisite(listaTipoVisita.getTipi());
				break;
				
			case 5:
				listaLuoghi.stampaLuoghiSenzaTipoVisita();
				break;	
			
			case 6:
				listaLuoghi.stampaLuoghiConTipoVisita();
				
			case 7:
				listaVisite.toString();
				break;
				
			}
			sceltaMenuConfig = menu.scegli();
		} while (sceltaMenuConfig!=0);
		
		
		
		
		
	}
	
	public static void volontario (int okCred, String nickname) throws Exception {
		/*
			cose che deve fare un volontario nella V2:
			- visualizzare tutti i tipi di visita a cui egli è associato
			- esprimere le disponibilità del mese i+2 , dove il volontario mette il giorno del mese(il mese è già stampato e fissato)
			dove devi controllare che il giorno del mese sia uno dei giorni programmabile , c'è gia il metodo in enum GIorni 
		*/
		ListaTipoVisita tipoVisitaPerVolontario = listaTipoVisita.getTipiPerVolontario(nickname);
		int sceltaMenuVolontario;
		String[] vociVolontario = {"Visualizza tipi di visita associati", "Esprimi la disponibilità per il mese :" + cal.getMeseI2()};
		MyMenu menu = new MyMenu("Menu", vociVolontario);
		sceltaMenuVolontario = menu.scegli();	
		do {
			switch (sceltaMenuVolontario) {
			case 0:
				boolean indietro; 
				indietro = InputDati.yesOrNo("Vuoi veramente tornare indietro?");
				if (indietro) {
					menuPrincipale();
				}
				break;
				
			case 1:
				System.out.println(tipoVisitaPerVolontario.toString());
				break;
			case 2:
			    /*
			     * PRECONDIZIONI:
			     * - nickname non deve essere null o vuoto
			     * - listaVolontari != null e contiene il volontario con quel nickname
			     * - listaTipoVisita != null
			     * - cal (Calendario) è stato inizializzato correttamente
			     *
			     * INVARIANTI:
			     * - Ogni TipoVisita in tipoVisitaPerVolontario è associato al volontario corrente
			     * - scegliDataPerTipo(t) restituisce null oppure una Data valida (giorno compreso nel mese i+2)
			     * - Una Visita è identificata univocamente da (TipoVisita, Data)
			     *
			     * POSTCONDIZIONI:
			     * - Per ogni TipoVisita associato al volontario, se esiste almeno una data disponibile,
			     *   viene creata o aggiornata una Visita in listaVisite
			     * - Nessuna Visita duplicata viene aggiunta (stesso tipo + stessa data)
			     * - Il volontario non compare due volte nella stessa Visita
			     */

			    assert nickname != null && !nickname.isEmpty() : "Precondizione violata: nickname nullo o vuoto";
			    assert listaVolontari != null : "Precondizione violata: listaVolontari null";
			    assert listaTipoVisita != null : "Precondizione violata: listaTipoVisita null";
			    assert cal != null : "Precondizione violata: calendario null";

			    ListaTipoVisita tipoVisitaPerVolontario = listaTipoVisita.getTipiPerVolontario(nickname);

			    for (TipoVisita t : tipoVisitaPerVolontario.getTipi()) {
			        assert t != null : "Invariante violata: TipoVisita null";

			        Data dataScelta = cal.scegliDataPerTipo(t);

			        if (dataScelta != null) {
			            Volontario v = listaVolontari.cercaPerNickname(nickname);
			            assert v != null : "Invariante violata: volontario non trovato";

			            Visita nuovaVisita = new Visita(t, v, StatoVisita.PROPOSTA, dataScelta);

			            // Metodo aggiungiVisita deve garantire:
			            // - se esiste già una Visita con stesso tipo+data, aggiunge solo il volontario
			            // - altrimenti crea una nuova Visita
			            listaVisite.aggiungiVisita(nuovaVisita);
			        }
			    }

			    // POSTCONDIZIONE: tutte le visite in listaVisite rispettano l’unicità (tipo+data)
			    assert listaVisite.visiteUnichePerTipoEData() : "Postcondizione violata: visite duplicate presenti";

			    break;
			}
			sceltaMenuVolontario = menu.scegli();
		} while (sceltaMenuVolontario!=0);		
	}

	
	public static void creazioneCorpoDati () {
		System.out.println("Creazione Corpo Dati");
		//**************************AMBITO TERRITORIALE ***********************************
		System.out.println("------------------------------");
		System.out.println("Creazione Ambito Territoriale");
		System.out.println("------------------------------");
		
		String provincia;
		provincia = InputDati.leggiStringaNonVuota("Inserisci la provincia: ");
		
		boolean continua;
		do {
	         int cap = InputDati.leggiIntero("Inserisci un CAP: ", 1, 99999);
	         if (capAmbito.contains(cap)) {
	             System.out.println("Attenzione: il CAP " + cap + " è già stato inserito!");
	         } else {
	             capAmbito.add(cap);
	         }
			 continua = InputDati.yesOrNo("Vuoi inserire un altro cap? S per inserire, N per uscire. ");
		} while (continua);
		
		AmbitoTerritoriale ambitoTerritoriale = new AmbitoTerritoriale(capAmbito, provincia);
		
		
		//********************** N MAX PARTECIPANTI PER FRUITORE ***********************************
		/*fissare il numero massimo di persone che un fruitore pu� iscrivere a una iniziativa 
		mediante una singola iscrizione (tale numero � valido per ogni visita)*/
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
		
		
		/*mbitoTerritoriale ambito,
        ListaLuogo listaLuoghi) throws Exception {*/
		//verifico prima di concludere la creaizione del corpo dati che tutti i volontari abbiano un tipovisita associato
		List<Volontario> orfani = listaVolontari.trovaVolontariSenzaVisite(listaTipoVisita);

		if (!orfani.isEmpty()) {
		    System.out.println("Attenzione ci sono volontari senza visite associate!");
		    for (Volontario v : orfani) {
		    	// Guida l’utente ad associarlo
		        System.out.println("Scegli una visita per il volontario " + v.getNickname() + " :");
		        System.out.println(listaTipoVisita.toStringAsList());
		        int scelta = InputDati.leggiIntero("Numero visita: ",1,listaTipoVisita.getTipi().size());
		        TipoVisita visitaScelta = listaTipoVisita.getTipo(scelta - 1);
		        visitaScelta.getListaVolontari().aggiungiVolontario(v);
		        System.out.println("Il volontario " + v.getNickname() + "è stato associato a \"" + visitaScelta.getTitolo() + "\".");
		    }
		}
		try {
			g.scriviCorpoDati(nMaxPersone, ambitoTerritoriale, listaLuoghi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void definizioneLuogo () {
		String nomeLuogo = InputDati.leggiStringaNonVuota("Inserisci il nome del luogo: ");
		String descrizione = InputDati.leggiStringa("Inserisci una descrizione del luogo: ");
		Indirizzo indirizzo = ObjIndirizzo();
		
		//ogni LUOGO dovrebbe contenere al suo interno una LISTA DI TIPI VISITA (pg. 4, "A ciascun luogo � associato almeno un tipo di visita")
			
		
		//mostrare tutti i tipi visita
		//chiedere quale associare al luogo
		//chiedere se ne vogliono associare un altro
					
		boolean continuaTipo = false;
		int sceltaTipo;
		//ArrayList<TipoVisita> tipiToAdd = new ArrayList<>();
		ListaTipoVisita listaTipoVisitaToAdd = new ListaTipoVisita();
		
		do {
		    // Mostra la lista delle visite disponibili
			System.out.println("Elenco Tipi Visite:");
		    System.out.println(listaTipoVisita.toStringAsList());
		    // Scelta dell'utente
		    sceltaTipo = InputDati.leggiInteroNonNegativo("Inserisci il numero del Tipo Visita che vuoi associare a questo Luogo: ");
		    TipoVisita visitaSelezionata = listaTipoVisita.getTipo(sceltaTipo - 1);
		    boolean duplicato = false;
		    boolean sovrapposta = false;

		    // Controlli su duplicati e sovrapposizioni
		    for (TipoVisita v : listaTipoVisitaToAdd.getTipi()) {
		        if (v.equals(visitaSelezionata)) {
		            System.out.println("Attenzione hai selezionato lo stesso Tipo Visita già associato!");
		            duplicato = true;
		            break;
		        } else if (v.siSovrappongono(visitaSelezionata)) {
		            System.out.println("Attenzione questa visita si sovrappone ad un'altra già programmata per lo stesso giorno!");
		            sovrapposta = true;
		            break;
		        }
		    }
		    // Se non è duplicata né sovrapposta, la aggiungo
		    if (!duplicato && !sovrapposta) {
		        listaTipoVisitaToAdd.add(visitaSelezionata);
		        System.out.println("Visita aggiunta correttamente.");
		    }
		    // Chiedo se continuare SOLO una volta per ciclo
		    continuaTipo = InputDati.yesOrNo("Vuoi associare un altro Tipo Visita a questo luogo? ");
		} while (continuaTipo);

	
		
		Luogo luogo = new Luogo(nomeLuogo, descrizione, indirizzo, listaTipoVisitaToAdd);
				
		System.out.println(luogo.toString());
		
		listaLuoghi.add(luogo);
	}

	public static void definizioneTipoVisita() {
		
		//prima definisco il singolo tipo visita, e poi lo aggiungo alla lista listaTipoVisita 
		
		
		String titolo = InputDati.leggiStringaNonVuota("Inserisci il titolo del tipo visita: ");
		String descrizione = InputDati.leggiStringaNonVuota("Inserisci una breve descrizione del tipo visita: ");
		String puntoDiIncontro = InputDati.leggiStringaNonVuota("Inserisci il punto di incontro: ");
	    ArrayList<Giorni> giorniProgrammabiliSettimana = ObjGiorniSettimana();	    
	    LocalTime oraInizio = ObjOraFLT();
	    int durataVisita = InputDati.leggiInteroPositivo("Inserisci in minuti la durata della visita: ");
	    //int biglietto = InputDati.leggiIntero("Inserisci 0 se NON serve il biglietto, 1 altrimenti.", 0, 1);
	    boolean biglietto = InputDati.yesOrNo("Inserisci N se NON serve il biglietto, S altrimenti.");
	    int sogliaMinPartecipanti = InputDati.leggiInteroNonNegativo("Inserisci soglia MINIMA dei partecipanti: ");
	    int sogliaMaxPartecipanti = InputDati.leggiIntero("Inserisci soglia MASSIMA dei partecipanti: ",sogliaMinPartecipanti+1, Integer.MAX_VALUE);
		Periodo periodoProgrammabile = ObjPeriodo();	
		
		boolean continuaVolontario = false;
		int sceltaVolontario;
		ListaVolontari volontariToAdd = new ListaVolontari();
		
		do {
			if(volontariToAdd.getVolontari().size()==listaVolontari.getVolontari().size()) {
				System.out.println("Nessun volontario disponibile");
				continuaVolontario=false;
				break;
			}
	        System.out.println("Elenco Volontari disponibili:");
			listaVolontari.visualizzaVolontariDisponibili(volontariToAdd.getVolontari());
			sceltaVolontario = InputDati.leggiInteroNonNegativo("Inserisci il numero del volontario da associare al tipo visita: ");
			volontariToAdd.aggiungiVolontario(listaVolontari.getVolontario(sceltaVolontario - 1));
			continuaVolontario = InputDati.yesOrNo("Vuoi associare un altro Volontario a questo Tipo Visita? ");
		} while(continuaVolontario);
		
		TipoVisita tipoVisita = new TipoVisita(titolo, descrizione,  puntoDiIncontro, giorniProgrammabiliSettimana, oraInizio,
				durataVisita, biglietto, sogliaMinPartecipanti, sogliaMaxPartecipanti, periodoProgrammabile, volontariToAdd);
		
		System.out.println(tipoVisita.toString());
		
		listaTipoVisita.add(tipoVisita);
		
	}
	
	public static String getCapAmbito() {
		 String result = "";
		 for (int i = 0; i < capAmbito.size(); i++) {
			 result += capAmbito.get(i);
			 if (i < capAmbito.size() - 1) {
				 result += ", ";
			 }	
		 }
		 return result;
    }
	
	public static void leggiCorpoDati() throws Exception {
		ambito = g.leggiAmbiti();
		listaLuoghi.setListaLuoghi(g.leggiLuoghi());
		ambito.stampaAmbito();
		listaLuoghi.toString();
	    //System.out.println("Ambiti: " + ambiti);
	    //System.out.println("Luoghi: " + luoghi);
	}

	
	public static void login() throws Exception {
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
		 	- 0 se non � il primo accesso
		 	- 1 se � il primo accesso
		 	
		 	 questa info la devo mandare al metodo configuratore che, se � il primo accesso, deve fare determinate cose
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
		case "VOLONTARIO":
			volontario(okCred, credenziali.getUsername());
			break;
			
		default:						
			break;			
		}
	}
	
	//private static final String LOG_FILENAME = "matriciopoli.log";
	//////////////////////////////////
	// MENU PRINCIPALE
	//////////////////////////////////
	/**
	 * Metodo per il menu principale dell'applicazione.
	 * @return True se e' stata selezionata l'uscita, altrimenti False.
	 * @throws Exception 
	 */
	public static boolean menuPrincipale() throws Exception{
		
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

	public static void modificaNumFruitore() {
		nMaxPersone = InputDati.leggiInteroPositivo("Inserisci il numero massimo di persone che un fruitore puo' iscrivere a una iniziativa mediante una singola iscrizione: ");
	}



	
	public static Data ObjData (String descrizioneGiorno, String descrizioneMese) {
		int giorno = InputDati.leggiIntero(descrizioneGiorno,1,31);
		int mese = InputDati.leggiIntero(descrizioneMese,1,12);
		
		Data data = new Data(giorno, mese);
		
		return data;
		
	}

	public static ArrayList<Giorni> ObjGiorniSettimana() {
	    ArrayList<Giorni> giorniSelezionati = new ArrayList<>();
	    System.out.println("Inserisci i giorni della settimana in cui è possibile effettuare la visita.");
	    System.out.println("Usa i numeri da 1 a 7 (1=Lunedì, 2=Martedi', 3=Mercoledi', 4=Giovedi', 5=Venerdi', 6=Sabato, 7=Domenica).");
	    boolean continua=true;
		do {
			 int input = InputDati.leggiIntero("Inserire giorno: ", 1, 7);
	         Giorni giorno = Giorni.values()[input - 1]; // converto numero → enum
	         if (giorniSelezionati.contains(giorno)) {
	             System.out.println("Giorno già inserito: " + Giorni.getNomeGiorno(input));
	        	 continua = InputDati.yesOrNo("Vuoi inserire un altro giorno? S per inserire, N per uscire. ");

	         }else {
	        	 giorniSelezionati.add(giorno);
	        	 continua = InputDati.yesOrNo("Vuoi inserire un altro giorno? S per inserire, N per uscire. ");
	         }
		} while (continua);
	    
	    return giorniSelezionati;
	}
	
	public static Indirizzo ObjIndirizzo() {

	    TipoStrada tipoStrada = TipoStrada.scegliDaNome();
	    String nomeStrada = InputDati.leggiStringaNonVuota("Inserisci il nome della Strada: ");
	    String numeroCivico = InputDati.leggiStringaNonVuota("Inserisci il numero civico: ");

	    System.out.println("Seleziona un CAP valido:");
	    for (int i = 0; i < capAmbito.size(); i++) {
	        System.out.println((i + 1) + ". " + capAmbito.get(i));
	    }

	    int scelta;
	    do {
	        scelta = InputDati.leggiIntero("Inserisci il numero corrispondente al CAP: ");
	        if (scelta < 1 || scelta > capAmbito.size()) {
	            System.out.println("Errore: scelta non valida, riprova.");
	        }
	    } while (scelta < 1 || scelta > capAmbito.size());
	    Integer cap = capAmbito.get(scelta - 1);
	    
	    String comune = InputDati.leggiStringaNonVuota("Inserisci il Comune: ");
	    String provincia = ambito.getProvincia();
	    System.out.println("provincia TEST: " + provincia + provincia );
	    Indirizzo indirizzo = new Indirizzo(tipoStrada, nomeStrada, numeroCivico, cap, comune, provincia);
	    return indirizzo;
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
	 
	 
	 

}