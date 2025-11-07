package Versione1;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

import it.unibs.fp.mylib.*;


public class Menu{
	private static StatoSistema statoSistema = StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1;
	private static AmbitoTerritoriale ambito = new AmbitoTerritoriale();
    private static Calendario cal = new Calendario();
    private static ArrayList<Configuratore> listaConfiguratore = new ArrayList<Configuratore>();
    private static ListaLuogo listaLuoghi = new ListaLuogo();
    private static ListaTipoVisita listaTipoVisita = new ListaTipoVisita();
    private static ListaVolontari listaVolontari = new ListaVolontari();
    private static ListaVisite listaVisite = new ListaVisite();
    private static int nMaxPersone = 0;
    private static String[] voci = {"Login"};
    private static GestioneFile g = new GestioneFile();
    private static UtenteDAO UtenteDao = new UtenteDAO();
    private static TipoVisitaDAO TipoVisitaDAO = new TipoVisitaDAO();
    private static AmbitoTerritorialeDAO AmbitoTerritorialeDAO = new AmbitoTerritorialeDAO();
    private static LuogoDAO LuogoDAO = new LuogoDAO();
    private static DisponibilitaDAO DisponibilitaDAO = new DisponibilitaDAO();
    private static VisitaDAO VisitaDAO = new VisitaDAO();
    private static CalendarioDAO CalendarioDAO = new CalendarioDAO();
    
    public static void login() throws Exception {
		String username;
		String password;
		String utenza = "";
		
		//***************************POSSIBILE FARLO MEGLIO, CONTROLLANDO SOLO ARRAYLIST CONFIGU********************
	
		System.out.println("Inserisci lo username che ti e' stato fornito dall'amministrazione: ");
		username = InputDati.leggiStringaNonVuota("Username: ");
		
		//while (g.leggiSingolaCredenziale(username) == null) {				
		while (UtenteDao.leggiSingolaCredenziale(username) == null) {
			username = InputDati.leggiStringaNonVuota("Username inserito non esistente, riprovare: ");
			
		}
		Credenziali credenziali = UtenteDAO.leggiSingolaCredenziale(username);
		
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
  
  	public static boolean menuPrincipale() throws Exception{
  		
  		int sceltaMenu;
  		UtenteDao.leggiUtentiDaDB(listaConfiguratore, listaVolontari);
  		CalendarioDAO.leggiCalendarioDaDB(cal);
  		MyMenu menu = new MyMenu("Menu", voci);
  		sceltaMenu = menu.scegli();
  		
  		switch (sceltaMenu) {
  		case 0:
  			return InputDati.yesOrNo("Vuoi veramente uscire?");
  			
  		case 1:
  			login();
  			break;
  		
  		}
  		return false;
  			
  	}
  	
  	public static void configuratore (int okCred) throws Exception {
		int sceltaMenuConfig = 0;
		String[] vociConfig = {"Creazione Corpo Dati", "Avvio Calendario", "Modifica num max per fruitore","Visualizza volontari" ,
								"Visualizza Luoghi", "Visualizza l'elenco dei tipi di visita associati a ciascun luogo", "Visualizza Stato Visite", 
								"Genera piano delle visite", "Modifica Luoghi/Tipi Visita/Volontari"};	
		String[] vociConfigModifica = {"Aggiungi nuovo luogo per visite guidate", "Aggiungi tipi visita a luogo esistente", 
										"Aggiungi volontario a tipo visita", "Rimuovi Luogo","Rimuovi Tipo Visita","Rimuovi Volontario"};
		MyMenu menu = new MyMenu("Menu configuratore", vociConfig);
		MyMenu menuModifica = new MyMenu("Menu di modifica", vociConfigModifica);
		okCred=0;
		if(okCred==1) sceltaMenuConfig= 1; else sceltaMenuConfig = menu.scegli();		
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
					statoSistema = StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1;
					
				} else {
					System.out.println("Corpo Dati già definito.");
					//e devo leggere i dati da file
					leggiCorpoDati();
					statoSistema = StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1;
				}
				break;
				    
			case 2:				
				if(StatoSistema.RACCOLTA_DATE_PRECLUSE_I_3!=statoSistema) {
					System.out.println("Non puoi inserire le date precluse in questo stato del sistema.\nStato attuale: " + statoSistema.stampaStatoSistema() + ")");
				}
				else {
				inserimentoDatePrecluse();
				statoSistema=StatoSistema.RACCOLTA_DATE_PRECLUSE_I_3_COMPLETATA;
				}
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
				break;
				
			case 7:
				listaVisite.stampaTitoloEStato();
				break;
				
			case 8:
				if(statoSistema!=StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1) {
					System.out.println("Non puoi generare il piano visite in questo stato del sistema.\nStato attuale: " + statoSistema.stampaStatoSistema() + ")");
					}
				else {
					generaPianoVisite();
					statoSistema=StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA;
				}
				break;
				
			case 9: // Sottomenu per modifiche
			    if (statoSistema == StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA) {
			    	statoSistema = StatoSistema.GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE;
			        int sceltaModifica;
		            sceltaModifica = menuModifica.scegli();

			        do {
			            switch (sceltaModifica) {
			                case 0: // Indietro dal sottomenu
			                    break;
			                case 1:
			                    aggiungiLuogo(true);
			                    break;
			                case 2:
			                    aggiungiTipoVisitaALuogo();
			                    break;
			                case 3:
			                    aggiungiVolontarioATipoVisita();
			                    break;
			                case 4:
			                    rimuoviLuogo();
			                    break;
			                case 5:
			                    rimuoviTipoVisitaDaLuogo();
			                    break;
			                case 6:
			                    rimuoviVolontario();
			                    break;
			            }
			            sceltaModifica = menuModifica.scegli();
			        } while (sceltaModifica != 0);
			        statoSistema = StatoSistema.GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE_COMPLETATA;
			    } else {
					System.out.println("Non puoi procedere alla moodifica del corpo dati in questo stato del sistema.\nStato attuale: " + statoSistema.stampaStatoSistema() + ")");
			    }
			    break;
	
				
			}
			sceltaMenuConfig = menu.scegli();
		} while (sceltaMenuConfig!=0);
		
	}
  	
  	public static void volontario (int okCred, String nickname) throws Exception {

		ListaTipoVisita tipoVisitaPerVolontario = listaTipoVisita.getTipiPerVolontario(nickname);
		int sceltaMenuVolontario;
		String[] vociVolontario = {"Visualizza tipi di visita associati", "Esprimi la disponibilita' per il mese :" + cal.getMeseI2()};
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
			    System.out.println("Elenco tipi visita associati al volontario: " + nickname);
			    System.out.println(tipoVisitaPerVolontario.toString().replace("===== ELENCO TIPI DI VISITA =====\n", ""));
			    break;

			case 2:
				if(statoSistema!=StatoSistema.RACCOLTA_DISPONIBILITA_I_2) {
					System.out.println("Non puoi esprimere disponibilita' in questo stato del sistema.\nStato attuale: " + statoSistema.stampaStatoSistema() + ")");
					}
				else
				raccogliDisponibilitaVolontario(nickname);
				statoSistema=StatoSistema.RACCOLTA_DISPONIBILITA_I_2_COMPLETATA;
			    break;
		
			}
			sceltaMenuVolontario = menu.scegli();
		} while (sceltaMenuVolontario!=0);		
	}
    
  	public static void creazioneCorpoDati () throws SQLException {
		System.out.println("Creazione Corpo Dati");
		//**************************AMBITO TERRITORIALE ***********************************
		System.out.println("------------------------------");
		System.out.println("Creazione Ambito Territoriale");
		System.out.println("------------------------------");
		
		String provincia = InputDati.leggiStringaNonVuota("Inserisci la provincia: ");
		String comune = InputDati.leggiStringaNonVuota("Inserisci il nome del comune: ");
		int cap = InputDati.leggiIntero("Inserisci il CAP: ", 10000, 99999);
		AmbitoTerritoriale ambitoTerritoriale = new AmbitoTerritoriale(cap, provincia, comune);
		AmbitoTerritorialeDAO.salvaAmbitoTerritoriale(ambitoTerritoriale);
		// Aggiorna il riferimento globale all’ambito corrente
		ambito = ambitoTerritoriale;


		
		//********************** N MAX PARTECIPANTI PER FRUITORE ***********************************
		/*fissare il numero massimo di persone che un fruitore pu� iscrivere a una iniziativa 
		mediante una singola iscrizione (tale numero � valido per ogni visita)*/
		//**************************AMBITO TERRITORIALE ***********************************
		System.out.println("---------------------------------");
		System.out.println("NUM MAX PARTECIPANTI PER FRUITORE");
		System.out.println("---------------------------------");
		
		modificaNumFruitore();
		//********************** LUOGHI ***********************************
		
		//prerequisiti: lista volontari --FATTO**** -->metodo visualizzaVolontari
		//visualizzaVolontari();
		
		//1. definizione TipoVisita e poi inserite in ListaTipoVisita, da cui verranno pescate per essere mostrate e scelte dal config
		
		System.out.println("-----------------------");
		System.out.println("DEFINIZIONE TIPI VISITA");
		System.out.println("-----------------------");
		
		boolean continuoTipoVisita;
		do {
			System.out.println();
			aggiungiTipoVisita(false); //false perche' nella V1 non si possono creare nuovi volontari
			continuoTipoVisita = InputDati.yesOrNo("Vuoi inserire un altro Tipo Visita?");
		} while (continuoTipoVisita);
		

		System.out.println("------------------");
		System.out.println("DEFINIZIONE LUOGHI");
		System.out.println("------------------");
		//2. chiedere quanti luoghi vuole definire
		
		boolean continuoLuoghi;
		do {
		    System.out.println();
		    aggiungiLuogo(false); // false: nella V1 non si possono creare nuovi volontari
		    continuoLuoghi = InputDati.yesOrNo("Vuoi inserire un altro luogo?");
		} while (continuoLuoghi);

		
		
		/*mbitoTerritoriale ambito,
        ListaLuogo listaLuoghi) throws Exception {*/
		//verifico prima di concludere la creaizione del corpo dati che tutti i volontari abbiano un tipovisita associato
		associaVolontariOrfani();
		/*try {
			//g.scriviCorpoDati(nMaxPersone, ambitoTerritoriale, listaLuoghi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
  	public static void leggiCorpoDati() throws Exception {	
  		
  		ambito = AmbitoTerritorialeDAO.leggiTuttiAmbitiTerritoriali();
		ambito.stampaAmbito();
		
		listaTipoVisita = TipoVisitaDAO.leggiTuttiTipiVisita(listaVolontari);
		System.out.println(listaTipoVisita.toString());
		
		listaLuoghi = LuogoDAO.leggiTuttiLuoghi(listaTipoVisita);
		listaLuoghi.stampaLuoghiConTipoVisita();
		
		
		
		/*listaVisite = VisitaDAO.leggiTutteVisite(listaTipoVisita, listaVolontari);
		listaVisite.stampaPianoVisite();*/
		
		CalendarioDAO.leggiCalendarioDaDB(cal);
		
		for(Volontario v : listaVolontari.getVolontari()) {
			DisponibilitaDAO.leggiDisponibilitaVolontario(v.getNickname());
		}
		
	}
  	
	public static void inserimentoDatePrecluse() throws SQLException {
		 System.out.println("Inserimento delle date precluse per il mese di " + cal.getMeseI3());
		 boolean continuaDataPreclusa = false;
		 boolean inserisciDate = true;
		 if (inserisciDate) {
			 do {
				 aggiuntaDataPreclusa();
				 continuaDataPreclusa = InputDati.yesOrNo("Vuoi inserire un altra data preclusa per il mese " + cal.getMeseI3() +"? ");
			 } while (continuaDataPreclusa);
			 System.out.println(cal.toStringDatePrecluse(3));
		 }	 
	}
	
	public static void aggiuntaDataPreclusa() throws SQLException {
		 int giornoEscluso = InputDati.leggiIntero("Inserisci il giorno da escludere dal mese di " +  cal.getMeseI3() + "\n", 1, 31);
		 if (!cal.checkData(giornoEscluso,3)) {
			 cal.aggiungiDataPreclusa(giornoEscluso, 3);
			 CalendarioDAO.salvaDataPreclusa(giornoEscluso, 3);
		 } else {
			 System.out.println("Giorno gia' inserito. Inserire un altro giorno.");
		 }
		 
		 
	}
	
	public static void modificaNumFruitore() {
		nMaxPersone = InputDati.leggiInteroPositivo("Inserisci il numero massimo di persone che un fruitore puo' iscrivere a una iniziativa mediante una singola iscrizione: ");
	}
	
	// NUOVO METODO!! PER GENERARE PIANO VISITE
	public static void generaPianoVisite() {
			System.out.println("\n===== Generazione del piano delle visite per il mese " + cal.getMeseI1() + " =====\n");
		   
			
			try {
				for(Volontario v : listaVolontari.getVolontari()) {
					ArrayList<Disponibilita> disponibilitaVolontario = DisponibilitaDAO.leggiDisponibilitaVolontario(v.getNickname());
					
					for(Disponibilita d : disponibilitaVolontario) {
						if(d.getMese()==1) {
							if (cal.isGiornoEscluso(d.getGiorno(), 1)) {
		                        continue;
		                    }
							ListaTipoVisita tipiVolontario = listaTipoVisita.getTipiPerVolontario(v.getNickname());
							for(TipoVisita tipo : tipiVolontario.getTipi()) {
								int numero_mese = cal.getMeseNumero(cal.getMeseI1());
								Data dataVisita = new Data(d.getGiorno(), numero_mese);
								Visita nuova = new Visita(tipo, v, StatoVisita.PROPOSTA, dataVisita);
								listaVisite.aggiungiVisita(nuova);
								VisitaDAO.salvaVisita(nuova,v.getNickname());
							}
						}
					}
				
				}
				
				//System.out.println("Il piano delle visite e' stato generato con successo!");
				listaVisite.stampaPianoVisite();
				
			}catch(SQLException e) {
				System.out.println("Errore durante la generazione del piano delle visite: " + e.getMessage());
			}
	}
	
	private static void raccogliDisponibilitaVolontario(String nickname) throws SQLException {
	    if (statoSistema != StatoSistema.GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE_COMPLETATA) {
	        System.out.println("Non puoi esprimere disponibilità in questo stato del sistema. (Stato attuale: " + statoSistema + ")");
	        return;
	    }
	    statoSistema = StatoSistema.RACCOLTA_DISPONIBILITA_I_2;   
	    ListaTipoVisita tipoVisitaPerVolontario = listaTipoVisita.getTipiPerVolontario(nickname);
	    
	    for (TipoVisita t : tipoVisitaPerVolontario.getTipi()) {
	        assert t != null : "Invariante violata: TipoVisita null";

	        boolean continua = true;
	        do {
	            Data dataScelta = cal.scegliDataPerTipo(t, 2);

	            if (dataScelta != null) {
	                Volontario v = listaVolontari.cercaPerNickname(nickname);
	                assert v != null : "Invariante violata: volontario non trovato";

	                Visita nuovaVisita = new Visita(t, v, StatoVisita.PROPOSTA, dataScelta);

	                // Metodo aggiungiVisita deve garantire:
	                // - se esiste già una Visita con stesso tipo+data, aggiunge solo il volontario
	                // - altrimenti crea una nuova Visita
	                listaVisite.aggiungiVisita(nuovaVisita);
	                
	               
	                    DisponibilitaDAO.salvaDisponibilita(nickname, dataScelta.getGiorno(), 2);
	                    
	                    // Stampa le disponibilità inserite
	                    ArrayList<Disponibilita> mie = DisponibilitaDAO.leggiDisponibilitaVolontario(nickname);
	                    System.out.println("==== Le tue disponibilità per " + cal.getMeseI2() + " ====");
	                    for (Disponibilita d : mie) {
	                        if (d.getMese()==2) {
	                            System.out.println("- " + d.getGiorno() + " " + d.getMese());
	                        }
	                    }
	                    System.out.println("==============================================");
	                
	            	}
	            
	            continua = InputDati.yesOrNo("Vuoi esprimere un'altra disponibilità per il mese di " + cal.getMeseI2() + "? ");
	        } while (continua);
	    }
	    // POSTCONDIZIONE: tutte le visite in listaVisite rispettano l'unicità (tipo+data)
	    assert listaVisite.visiteUnichePerTipoEData() : "Postcondizione violata: visite duplicate presenti";
	    statoSistema = StatoSistema.RACCOLTA_DISPONIBILITA_I_2_COMPLETATA;
	}
	
	
	
	public static void rimuoviLuogo() throws SQLException {
	    System.out.println("================================================================");
	    System.out.println("INIZIO PROCEDURA ELIMINAZIONE LUOGHI, procedere con cautela.");
	    System.out.println("================================================================");
	    
	    boolean continuaLuogo = false;
	    
	    do {
	        // Mostra lista luoghi
	    	Luogo luogoSceltoDaRimuovere = listaLuoghi.selezionaLuogoAttivo();
	    	if (luogoSceltoDaRimuovere != null) {
		        String nomeLuogo = luogoSceltoDaRimuovere.getNomeLuogo();
		        boolean conferma = InputDati.yesOrNo("Sei sicuro di voler eliminare il luogo '" + nomeLuogo + "'? ");
		        if (conferma) {
		            // PASSO 1: Salva i tipi visita del luogo prima di eliminarli
		            ArrayList<TipoVisita> tipiVisitaDaRimuovere = luogoSceltoDaRimuovere.getListaTipoVisita().getTipi();
		            
		            // PASSO 2: Elimina i tipi visita associati al luogo
		            rimuoviTipiVisitaDaLuogo(luogoSceltoDaRimuovere, tipiVisitaDaRimuovere);
		          
		            // PASSO 3: Gestisci volontari orfani (senza pi� tipi visita)
		            gestisciVolontariOrfani();
		            
		            // PASSO 4: Elimina il luogo dal database
		            System.out.println("Eliminazione luogo dal database...");
		            LuogoDAO.eliminaLuogo(luogoSceltoDaRimuovere);
		            
		            // PASSO 5: Rimuovi dalla lista locale
		            luogoSceltoDaRimuovere.setAttivo(false);
		            
		            System.out.println("Luogo '" + nomeLuogo + "' eliminato con successo!\n");
		        } else {
		            System.out.println("Operazione annullata.\n");
		        }
	    	}
		        continuaLuogo = InputDati.yesOrNo("Vuoi rimuovere un altro Luogo? ");
		        
		    } while (continuaLuogo);
	    
	    System.out.println("\nProcedura completata.");
	}
	
	public static void rimuoviTipoVisitaDaLuogo() throws SQLException {
	    System.out.println("================================================================");
	    System.out.println("RIMOZIONE TIPO DI VISITA DA LUOGO, procedere con cautela.");
	    System.out.println("================================================================");
	    
	    // Seleziona solo un luogo attivo
	    Luogo luogoScelto = listaLuoghi.selezionaLuogoAttivo();
	    if (luogoScelto == null) {
	        System.out.println("Selezione annullata o nessun luogo attivo disponibile.");
	        return;
	    }

	    // Mostra tipi visita attivi del luogo
	    System.out.println("\nTipi visita per '" + luogoScelto.getNomeLuogo() + "':");
	    ListaTipoVisita tipiDelLuogo = luogoScelto.getListaTipoVisita();

	    if (tipiDelLuogo.getTipi().isEmpty()) {
	        System.out.println("   Nessun tipo visita associato.");
	        return;
	    }

	    // Seleziona solo tipi visita attivi tramite il metodo specifico
	    TipoVisita tipoVisitaDaRimuovere = tipiDelLuogo.selezionaTipoVisita();
	    if (tipoVisitaDaRimuovere == null) {
	        System.out.println("Selezione non valida!");
	        return;
	    }

	    String nomeTipo = tipoVisitaDaRimuovere.getTitolo();

	    // Conferma eliminazione
	    boolean conferma = InputDati.yesOrNo(
	        "Sei sicuro di voler rimuovere il tipo visita '" + nomeTipo + "' dal luogo '" +
	        luogoScelto.getNomeLuogo() + "'? "
	    );

	    if (conferma) {
	        // Rimozione dal luogo (basta non mostrarlo più: lo segni non attivo)
	        tipoVisitaDaRimuovere.setAttivo(false);

	        // Rimozione fisica dal DB
	        System.out.println("\nEliminazione tipo visita dal database...");
	        TipoVisitaDAO.eliminaTipoVisita(tipoVisitaDaRimuovere);

	        // Rimozione da associazioni con volontari (se appropriato)
	        // Questa logica va gestita all'interno di eliminaTipoVisita/DAO oppure chiamando qui la funzione appropriata!

	        // Gestione volontari orfani (senza più tipi visita)
	        System.out.println("Verifica volontari orfani...");
	        gestisciVolontariOrfani();

	        // Gestione luoghi orfani (senza più tipi visita attivi associati)
	        System.out.println("Verifica luoghi orfani...");
	        gestisciLuoghiOrfani();

	        System.out.println("Tipo visita '" + nomeTipo + "' rimosso con successo!\n");
	    } else {
	        System.out.println("Operazione annullata.\n");
	    }
	}

	
	private static void rimuoviTipiVisitaDaLuogo(Luogo luogo, ArrayList<TipoVisita> tipiVisitaDaRimuovere) throws SQLException {
	    if (tipiVisitaDaRimuovere.isEmpty()) {
	        System.out.println("Nessun tipo visita associato al luogo.");
	        return;
	    }
	    
	    System.out.println("Rimozione tipi visita associati al luogo...");
	    System.out.println("Trovati " + tipiVisitaDaRimuovere.size() + " tipi visita da eliminare:");
	    
	    for (TipoVisita tv : tipiVisitaDaRimuovere) {
	        System.out.print("      - " + tv.getTitolo());

	        // Verifica se il tipo visita è rimasto "orfano"
	        boolean orfano = true;
	        for (Luogo l : listaLuoghi.getListaLuoghi()) {
	            if (l.isAttivo() && l.getListaTipoVisita().getTipi().contains(tv)) {
	                // C'è almeno un altro luogo attivo che usa questo tipo visita
	                orfano = false;
	                break;
	            }
	        }
	        if (orfano) {
	            // Se è orfano, allora elimina/disattiva tipo visita
	            eliminaTipoVisitaCompleto(tv);
	        }
	    }
	}
	
	private static void eliminaTipoVisitaCompleto(TipoVisita tipoVisita) throws SQLException {
	    // 1. Elimina da database (con CASCADE elimina anche da luogo_tipo_visita e tipo_visita_giorni)
	    TipoVisitaDAO.eliminaTipoVisita(tipoVisita);
	       
	    // 2. Rimuovi da listaTipoVisita
	    tipoVisita.setAttivo(false);
	    
	    // 3. Rimuovi da tutti i luoghi in memoria
	    /*for (Luogo l : listaLuoghi.getListaLuoghi()) {
	        l.getListaTipoVisita().rimuovi(tipoVisita);
	    }*/
	    
	    System.out.println("Eliminato tipo visita: " + tipoVisita.getTitolo());
	}
	
	public static void rimuoviVolontario() throws SQLException {
	    System.out.println("================================================================");
	    System.out.println("INIZIO PROCEDURA ELIMINAZIONE VOLONTARI, procedere con cautela.");
	    System.out.println("================================================================");

	    boolean continuaVolontario=false;
	    
	    do {
	        // ✅ CONTROLLO 1: Verifica se ci sono volontari
	        if (listaVolontari.getVolontari().isEmpty()) {
	            System.out.println("Non ci sono volontari da rimuovere.");
	            break;  // Esce dal ciclo
	        }

	        System.out.println("\nElenco Volontari disponibili:");
	        for (int i = 0; i < listaVolontari.getVolontari().size(); i++) {
	            Volontario v = listaVolontari.getVolontari().get(i);
	            System.out.println((i + 1) + ") " + v.getCredenziali().getUsername());
	        }

	        // ✅ CONTROLLO 2: Leggi input con validazione
	        int sceltaVolontario = InputDati.leggiIntero(
	            "Inserisci il numero del volontario da rimuovere: ",
	            1,  // minimo
	            listaVolontari.getVolontari().size()  // massimo
	        );

	        Volontario volontarioDaRimuovere = listaVolontari.getVolontario(sceltaVolontario - 1);
	        String username = volontarioDaRimuovere.getNickname();

	        boolean conferma = InputDati.yesOrNo(
	            "Sei sicuro di voler eliminare il volontario '" + username + "'? "
	        );

	        if (conferma) {
	            try {
	                // PASSO 1: Rimuovi volontario dalle associazioni nel db
	                //UtenteDAO.rimuoviVolontario(volontarioDaRimuovere);
	                listaVolontari.rimuoviVolontario(volontarioDaRimuovere);

	                // PASSO 2: Rimuovi volontario da listaTipoVisita in memoria
	                //rimuoviVolontarioDaTipiVisita(volontarioDaRimuovere);
	                
	                // PASSO 3: Trova e gestisci tipi visita orfani
	                gestisciTipiVisitaOrfani();
	                
	                // PASSO 4: Trova e gestisci luoghi orfani
	                gestisciLuoghiOrfani(); 
	                
	                // PASSO 5: SOLO ORA elimina il volontario da credenziali 
	                UtenteDAO.eliminaDaCredenziali(volontarioDaRimuovere);
	                
	                // PASSO 6: Rimuovi dalla lista locale
	                
	                System.out.println("Volontario '" + username + "' eliminato con successo!\n");
	                
	            } catch (SQLException e) {
	                System.out.println("Errore durante l'eliminazione: " + e.getMessage());
	                continue;  // Torna all'inizio del ciclo
	            }
	        } else {
	            System.out.println("Eliminazione annullata.");
	        }

	        // ✅ CONTROLLO 3: Se non ci sono più volontari, esci
	        if (listaVolontari.getVolontari().isEmpty()) {
	            System.out.println("Non ci sono più volontari da rimuovere.");
	            break;
	        }

	        continuaVolontario = InputDati.yesOrNo("Vuoi rimuovere un altro Volontario? ");

	    } while (continuaVolontario);
	    
	    System.out.println("\nProcedura completata.");
	}
	
	private static void rimuoviVolontarioDaTipiVisita(Volontario volontarioDaRimuovere) {
	    for (TipoVisita tv : listaTipoVisita.getTipi()) {
	        ListaVolontari volontariTipoVisita = tv.getListaVolontari();
	        volontariTipoVisita.rimuoviVolontario(volontarioDaRimuovere);
	    }
	    System.out.println("Volontario rimosso dai tipi visita in memoria");
	}
	/**
	 * Associa interattivamente i volontari orfani a un tipo visita durante la creazione del corpo dati.
	 * @throws SQLException se si verifica un errore durante il salvataggio nel database
	 */
	
	private static void gestisciLuoghiOrfani() throws SQLException {
	    //LuogoDAO luogoDAO = new LuogoDAO();
	    
	    // Trova tutti i luoghi senza tipi visita
	    ListaLuogo luoghiOrfani = listaLuoghi.trovaLuoghiOrfani();
	    
	    if (luoghiOrfani.getListaLuoghi().isEmpty()) {
	        System.out.println("Nessun luogo orfano trovato.");
	        return;
	    }
	    
	    System.out.println("Trovati " + luoghiOrfani.getListaLuoghi().size() + " luoghi senza tipi visita:");
	    for (Luogo l : luoghiOrfani.getListaLuoghi()) {
	        System.out.println("      - " + l.getNomeLuogo());
	    }
	    
	    /*boolean conferma = InputDati.yesOrNo(
	        "    Eliminare i luoghi orfani? "
	    );
	    
	    if (conferma) {*/
	        for (Luogo l : luoghiOrfani.getListaLuoghi()) {
	        	LuogoDAO.eliminaLuogo(l);
	            l.setAttivo(false); // Rimuovi anche dalla lista locale
	            System.out.println("Eliminato luogo: " + l.getNomeLuogo());
	        }
	    /*} else {
	        System.out.println("     Luoghi orfani mantenuti.");
	    }*/
	}
	
	private static void gestisciTipiVisitaOrfani() throws SQLException {
	    // Trova tutti i tipi visita attivi senza volontari attivi
	    ListaTipoVisita tipiOrfani = listaTipoVisita.trovaTipiVisitaOrfani();

	    if (tipiOrfani.getTipi().isEmpty()) {
	        System.out.println("Nessun tipo visita orfano trovato.");
	        return;
	    }

	    System.out.println("Trovati " + tipiOrfani.getTipi().size() + " tipi visita senza volontari attivi:");
	    for (TipoVisita tv : tipiOrfani.getTipi()) {
	        System.out.println("      - " + tv.getTitolo());
	    }

	    // Elimina realmente i tipi visita orfani
	    for (TipoVisita tv : tipiOrfani.getTipi()) {
	        TipoVisitaDAO.eliminaTipoVisita(tv);
	        tv.setAttivo(false); // segna come disattivo (soft delete)
	        System.out.println("Eliminato tipo visita: " + tv.getTitolo());
	    }
	}

	private static void gestisciVolontariOrfani() throws SQLException {
	    ArrayList<Volontario> volontariOrfani = new ArrayList<>();
	    
	    // Trova volontari che non hanno pi� tipi visita associati
	    for (Volontario v : listaVolontari.getVolontari()) {
	        boolean haAssociazioni = false;
	        
	        // Controlla se il volontario � presente in almeno un tipo visita
	        for (TipoVisita tv : listaTipoVisita.getTipi()) {
	            if (tv.getListaVolontari().getVolontari().contains(v)) {
	                haAssociazioni = true;
	                break;
	            }
	        }
	        
	        if (!haAssociazioni) {
	            volontariOrfani.add(v);
	        }
	    }
	    
	    if (volontariOrfani.isEmpty()) {
	        System.out.println("Nessun volontario orfano trovato.");
	        return;
	    }
	    
	    System.out.println("Trovati " + volontariOrfani.size() + " volontari senza tipi visita:");
	    for (Volontario v : volontariOrfani) {
	        System.out.println("      - " + v.getNickname());
	    }
	    
	    // Elimina automaticamente (come da consegna)
	    System.out.println("Eliminazione automatica volontari orfani...");
	    for (Volontario v : volontariOrfani) {
	        // Elimina dal database
	        UtenteDAO.eliminaDaCredenziali(v);
	        
	        // Rimuovi dalla lista locale
	        v.setAttivo(false);
	        		
	        System.out.println("Eliminato volontario: " + v.getNickname());
	    }
	}
	
	private static void associaVolontariOrfani() throws SQLException {

	    List<Volontario> orfani = listaVolontari.trovaVolontariSenzaVisite(listaTipoVisita);

	    if (!orfani.isEmpty()) {
	        System.out.println("Attenzione: ci sono volontari senza visite associate!");
	        for (Volontario v : orfani) {
	            // Guida l'utente ad associarlo
	            System.out.println("\nScegli una visita per il volontario " + v.getNickname() + ":");
	            System.out.println(listaTipoVisita.toStringAsList());
	            int scelta = InputDati.leggiIntero("Numero visita: ", 1, listaTipoVisita.getTipi().size());
	            
	            TipoVisita visitaScelta = listaTipoVisita.getTipo(scelta - 1);
	            visitaScelta.getListaVolontari().aggiungiVolontario(v);
	            TipoVisitaDAO.gestisciOrfani(visitaScelta, v);
	            
	            System.out.println("Il volontario " + v.getNickname() + " è stato associato a \"" + visitaScelta.getTitolo() + "\".");
	        }
	    } else {
	        System.out.println("Tutti i volontari hanno almeno un tipo visita associato.");
	    }
	}
	
	
	public static void aggiungiLuogo(boolean consentiNuoviVol) throws SQLException {
	    String nomeLuogo = InputDati.leggiStringaNonVuota("Inserisci il nome del luogo: ");
	    String descrizione = InputDati.leggiStringa("Inserisci una descrizione del luogo: ");
	    Indirizzo indirizzo = ObjIndirizzo();

	    boolean continuaTipo = false;
	    int sceltaTipo;

	    ListaTipoVisita listaTipoVisitaToAdd = new ListaTipoVisita();

	    do {
	        // Conta tipi attivi totali
	        int attiviTotali = 0;
	        for (TipoVisita tv : listaTipoVisita.getTipi()) {
	            if (tv.isAttivo()) {
	                attiviTotali++;
	            }
	        }
	        // Controlla se ci sono tipi attivi non ancora aggiunti
	        if (attiviTotali == 0 || listaTipoVisitaToAdd.getTipi().size() == attiviTotali) {
	            System.out.println("Non ci sono Tipi Visita disponibili da associare a questo luogo.");
	            break;
	        }

	        // Costruisci lista tipi attivi disponibili per selezione
	        List<TipoVisita> tipiAttiviDisponibili = new ArrayList<>();
	        for (TipoVisita tv : listaTipoVisita.getTipi()) {
	            if (tv.isAttivo() && !listaTipoVisitaToAdd.getTipi().contains(tv)) {
	                tipiAttiviDisponibili.add(tv);
	            }
	        }

	        // Stampa lista filtrata con numerazione coerente
	        System.out.println("Elenco Tipi Visite:");
	        int count = 1;
	        for (TipoVisita tv : tipiAttiviDisponibili) {
	            System.out.println(count + ") " + tv.toStringSemplificato());
	            count++;
	        }

	        // Input scelta da lista filtrata
	        sceltaTipo = InputDati.leggiIntero(
	            "Inserisci il numero del Tipo Visita che vuoi associare a questo Luogo: ",
	            1, tipiAttiviDisponibili.size()
	        );
	        TipoVisita visitaSelezionata = tipiAttiviDisponibili.get(sceltaTipo - 1);

	        if (listaTipoVisita.isAggiungibile(visitaSelezionata, listaTipoVisitaToAdd)) {
	            listaTipoVisitaToAdd.add(visitaSelezionata);
	            System.out.println("Visita aggiunta correttamente.");
	        }

	        if (consentiNuoviVol) {
	            boolean aggiungiNuoviVolontari = InputDati.yesOrNo("Vuoi aggiungere nuovi volontari per questo tipo di visita?");
	            if (aggiungiNuoviVolontari) {
	                boolean continuaAggiunta = true;
	                do {
	                    Volontario nuovoVol = aggiungiNuovoVolontario();
	                    if (nuovoVol != null) {
	                        visitaSelezionata.getListaVolontari().aggiungiVolontario(nuovoVol);
	                        System.out.println("Volontario " + nuovoVol.getCredenziali().getUsername() +
	                                " aggiunto al tipo visita \"" + visitaSelezionata.getTitolo() + "\".");
	                    }
	                    continuaAggiunta = InputDati.yesOrNo("Vuoi aggiungere un altro nuovo volontario a questo tipo di visita?");
	                } while (continuaAggiunta);
	            }
	        }

	        if (listaTipoVisitaToAdd.getTipi().size() == attiviTotali) {
	            System.out.println("Hai associato tutti i Tipi Visita disponibili per questo luogo.");
	            break;
	        }

	        // Chiede se continuare
	        continuaTipo = InputDati.yesOrNo("Vuoi associare un altro Tipo Visita a questo luogo? ");

	    } while (continuaTipo);

	    Luogo luogo = new Luogo(nomeLuogo, descrizione, indirizzo, listaTipoVisitaToAdd);
	    System.out.println(luogo.toString());
	    try {
	        LuogoDAO.salvaLuogoCompleto(luogo);
	        if (!listaLuoghi.contains(luogo)) {
	            listaLuoghi.add(luogo);
	            System.out.println("Luogo creato e salvato con successo.");
	        } else {
	            System.out.println("ATTENZIONE: Luogo già presente nella lista!");
	        }
	    } catch (SQLException e) {
	        System.out.println("Errore nel salvataggio del luogo: " + e.getMessage());
	    }
	}


	public static void aggiungiTipoVisita(boolean consentiNuoviVolontari) throws SQLException {

	    // Acquisizione dati base tipo visita
	    String titolo = InputDati.leggiStringaNonVuota("Inserisci il titolo del tipo visita: ");
	    String descrizione = InputDati.leggiStringaNonVuota("Inserisci una breve descrizione del tipo visita: ");
	    String puntoDiIncontro = InputDati.leggiStringaNonVuota("Inserisci il punto di incontro: ");
	    ArrayList<Giorni> giorniProgrammabiliSettimana = ObjGiorniSettimana();
	    LocalTime oraInizio = ObjOraFLT();
	    int durataVisita = InputDati.leggiInteroPositivo("Inserisci in minuti la durata della visita: ");
	    boolean biglietto = InputDati.yesOrNo("Inserisci N se NON serve il biglietto, S altrimenti.");
	    int sogliaMinPartecipanti = InputDati.leggiInteroNonNegativo("Inserisci soglia MINIMA dei partecipanti: ");
	    int sogliaMaxPartecipanti = InputDati.leggiIntero("Inserisci soglia MASSIMA dei partecipanti: ", sogliaMinPartecipanti + 1, Integer.MAX_VALUE);
	    Periodo periodoProgrammabile = ObjPeriodo();

	    ListaVolontari volontariToAdd = new ListaVolontari();
	    boolean continuaVolontario;

	    do {
	        // Se consenti aggiunta nuovi volontari, chiedi se vogliono crearne uno nuovo
	        if (consentiNuoviVolontari) {
	            boolean aggiungiNuovo = InputDati.yesOrNo("Vuoi aggiungere un nuovo volontario? ");
	            if (aggiungiNuovo) {
	                Volontario nuovoVol = aggiungiNuovoVolontario();
	                if (nuovoVol != null) {
	                    System.out.println("Volontario " + nuovoVol.getCredenziali().getUsername() + " creato. Sarà associato al tipo visita se confermi.");
	                    // Non aggiunto automaticamente a lista globale qui: si presume sia fatto dentro aggiungiNuovoVolontario()
	                }
	            }
	        }

	        // Costruisci lista volontari disponibili e attivi non ancora associati a questo tipo visita
	        List<Volontario> volontariDisponibili = new ArrayList<>();
	        for (Volontario v : listaVolontari.getVolontari()) {
	            if (v.isAttivo() && !volontariToAdd.getVolontari().contains(v)) {
	                volontariDisponibili.add(v);
	            }
	        }

	        if (volontariDisponibili.isEmpty()) {
	            System.out.println("Non ci sono volontari attivi disponibili da associare.");
	            break;
	        }

	        // Stampa lista volontari disponibili con numerazione
	        System.out.println("Elenco Volontari disponibili:");
	        for (int i = 0; i < volontariDisponibili.size(); i++) {
	            System.out.println((i + 1) + ") " + volontariDisponibili.get(i).getCredenziali().getUsername());
	        }

	        // Input scelta volontario, con controllo validità
	        int sceltaVolontario;
	        do {
	            sceltaVolontario = InputDati.leggiInteroNonNegativo("Inserisci il numero del volontario da associare al tipo visita: ");
	            if (sceltaVolontario < 1 || sceltaVolontario > volontariDisponibili.size()) {
	                System.out.println("Scelta non valida, riprova.");
	            }
	        } while (sceltaVolontario < 1 || sceltaVolontario > volontariDisponibili.size());

	        // Aggiungi volontario selezionato alla lista del tipo visita in costruzione
	        Volontario scelto = volontariDisponibili.get(sceltaVolontario - 1);
	        volontariToAdd.aggiungiVolontario(scelto);

	        // Chiede se associare un altro volontario, sempre nel contesto di questo tipo visita
	        continuaVolontario = InputDati.yesOrNo("Vuoi associare un altro Volontario a questo Tipo Visita? ");

	    } while (continuaVolontario);

	    // Crea il nuovo oggetto TipoVisita con i volontari selezionati
	    TipoVisita tipoVisita = new TipoVisita(titolo, descrizione, puntoDiIncontro,
	            giorniProgrammabiliSettimana, oraInizio, durataVisita,
	            biglietto, sogliaMinPartecipanti, sogliaMaxPartecipanti,
	            periodoProgrammabile, volontariToAdd);

	    System.out.println(tipoVisita.toString());

	    try {
	        TipoVisitaDAO.inserisciVisita(tipoVisita);

	        // Verifica se tipo visita è già presente, aggiunge se no
	        if (!listaTipoVisita.getTipi().contains(tipoVisita)) {
	            listaTipoVisita.add(tipoVisita);
	            System.out.println("Tipo Visita creato con successo.");
	        } else {
	            System.out.println("ATTENZIONE: Tipo Visita già presente nella lista!");
	        }
	    } catch (SQLException e) {
	        System.out.println("Errore nel salvataggio: " + e.getMessage());
	    }
	}


	private static void aggiungiTipoVisitaALuogo() throws SQLException {
	    // Usa selezione solo tra luoghi attivi
	    Luogo luogoScelto = listaLuoghi.selezionaLuogoAttivo();
	    if (luogoScelto == null) {
	        System.out.println("Nessun luogo attivo disponibile o operazione annullata.");
	        return;
	    }

	    // Filtra tipi visita attivi non ancora associati al luogo
	    ListaTipoVisita tipiDisponibili = new ListaTipoVisita();
	    for (TipoVisita t : listaTipoVisita.getTipi()) {
	        if (t.isAttivo() && !luogoScelto.getListaTipoVisita().getTipi().contains(t)) {
	            tipiDisponibili.add(t);
	        }
	    }

	    if (tipiDisponibili.getTipi().isEmpty()) {
	        System.out.println("Tutti i tipi di visita disponibili sono già associati a questo luogo.");
	        return;
	    }

	    boolean continuaAggiunta = true;
	    do {
	        System.out.println("Tipi di visita disponibili da aggiungere:");
	        System.out.println(tipiDisponibili.toStringAsList());

	        int sceltaTipo = InputDati.leggiIntero("Scegli il tipo di visita da aggiungere: ",
	                                               1, tipiDisponibili.getTipi().size());
	        TipoVisita tipoScelto = tipiDisponibili.getTipo(sceltaTipo - 1);

	        boolean aggiungiVolontari = InputDati.yesOrNo(
	            "Vuoi aggiungere volontari (nuovi o esistenti) per questo tipo di visita?");
	        if (aggiungiVolontari) {
	            boolean continuaAggiungiVol = true;
	            do {
	                Volontario nuovoVol = aggiungiNuovoVolontario();
	                if (nuovoVol != null) {
	                    tipoScelto.getListaVolontari().aggiungiVolontario(nuovoVol);

	                    // Aggiorna anche il database e gestisce orfani
	                    TipoVisitaDAO tipoVisitaDAO = new TipoVisitaDAO();
	                    tipoVisitaDAO.gestisciOrfani(tipoScelto, nuovoVol);

	                    System.out.println("Volontario aggiunto al tipo visita.");
	                }
	                continuaAggiungiVol = InputDati.yesOrNo("Vuoi aggiungere un altro volontario?");
	            } while (continuaAggiungiVol);
	        }

	        // Aggiungi il tipo visita selezionato al luogo e salva modifiche
	        luogoScelto.getListaTipoVisita().add(tipoScelto);
	        LuogoDAO.salvaLuogoCompleto(luogoScelto);

	        continuaAggiunta = InputDati.yesOrNo("Vuoi aggiungere un altro tipo di visita a questo luogo?");
	    } while (continuaAggiunta);

	    System.out.println("Il luogo è stato correttamente modificato.");
	}


	public static Volontario aggiungiNuovoVolontario() throws SQLException {
	    
	    String username = InputDati.leggiStringaNonVuota("Inserisci username per il nuovo volontario: ");
	    
	    // ✅ CONTROLLO 1: Verifica se esiste già nella lista globale
	    Volontario volontarioEsistente = listaVolontari.cercaPerNickname(username);
	    if (volontarioEsistente != null) {
	        System.out.println("Il volontario '" + username + "' esiste già.");
	        return volontarioEsistente;  // 
	    }
	    
	    // ✅ CONTROLLO 2: Verifica se esiste nel database (ma non nella lista)
	    UtenteDAO utenteDao = new UtenteDAO();
	    Credenziali credEsistente = null;
	    credEsistente = utenteDao.leggiSingolaCredenziale(username);
	    
	    if (credEsistente != null) {
	        // Il volontario esiste nel DB ma non nella lista globale
	        System.out.println("Il volontario '" + username + "' esiste già nel database.");
	        System.out.println("Lo aggiungo alla lista globale...");
	        
	        Volontario volRecuperato = new Volontario(credEsistente);
	        listaVolontari.aggiungiVolontario(volRecuperato);
	        
	        System.out.println("Volontario recuperato con successo!");
	        return volRecuperato;
	    }
	    
	    String passwordPredefinita = "default";
	    String utenza = "VOLONTARIO";

	    Credenziali nuoveCred = new Credenziali(utenza, username, passwordPredefinita);
	    Volontario nuovoVolontario = new Volontario(nuoveCred);

	    try {
	        // Salva PRIMA nel DB
	        utenteDao.inserisciNuovoVolontario(nuovoVolontario);
	        
	        // POI aggiungi alla lista globale (solo se salvataggio OK)
	        listaVolontari.aggiungiVolontario(nuovoVolontario);

	        System.out.println("\nNuovo volontario aggiunto con successo!");
	        System.out.println("Username: " + username);
	        System.out.println("Password predefinita: " + passwordPredefinita);
	        System.out.println("------------------------------------------");

	        return nuovoVolontario;
	        
	    } catch (SQLException e) {
	        System.out.println("Errore durante l'inserimento del nuovo volontario: " + e.getMessage());
	        return null;  //  Non crasha, ritorna null
	    }
	}

	private static void aggiungiVolontarioATipoVisita() throws SQLException {

	    // Prepara lista solo con tipi visita attivi
	    List<TipoVisita> tipiAttivi = new ArrayList<>();
	    for (TipoVisita t : listaTipoVisita.getTipi()) {
	        if (t.isAttivo()) {
	            tipiAttivi.add(t);
	        }
	    }

	    if (tipiAttivi.isEmpty()) {
	        System.out.println("Non ci sono tipi di visita attivi disponibili.");
	        return;
	    }

	    // Mostra elenco tipi visita attivi
	    System.out.println("Elenco tipi di visita attivi:");
	    for (int i = 0; i < tipiAttivi.size(); i++) {
	        System.out.println((i + 1) + ") " + tipiAttivi.get(i).getTitolo());
	    }

	    int scelta = InputDati.leggiIntero("Scegli il numero del tipo visita: ", 1, tipiAttivi.size());
	    TipoVisita tipoScelto = tipiAttivi.get(scelta - 1);
	    System.out.println("Hai selezionato: " + tipoScelto.getTitolo() + "\n");

	    // Costruisci lista volontari attivi disponibili e non già associati a tipoScelto
	    ListaVolontari volontariDisponibili = new ListaVolontari();
	    for (Volontario vol : listaVolontari.getVolontari()) {
	        if (vol.isAttivo() && !tipoScelto.getListaVolontari().getVolontari().contains(vol)) {
	            volontariDisponibili.aggiungiVolontario(vol);
	        }
	    }

	    boolean continua = true;
	    do {
	        boolean nuovo = InputDati.yesOrNo("Vuoi aggiungere un volontario non ancora registrato?");
	        Volontario v;

	        if (nuovo) {
	            v = aggiungiNuovoVolontario(); // uso il metodo creato apposta
	            if (v != null && v.isAttivo() && !tipoScelto.getListaVolontari().getVolontari().contains(v)) {
	                tipoScelto.getListaVolontari().aggiungiVolontario(v);
	                TipoVisitaDAO.gestisciOrfani(tipoScelto, v);
	                System.out.println("Volontario \"" + v.getCredenziali().getUsername() + "\" aggiunto a \"" + tipoScelto.getTitolo() + "\".");
	            } else if (v != null && !v.isAttivo()) {
	                System.out.println("Il volontario appena creato NON è attivo, quindi non verrà associato.");
	            }
	        } else {
	            if (volontariDisponibili.getVolontari().isEmpty()) {
	                System.out.println("Tutti i volontari attivi disponibili sono già associati al tipo visita selezionato.");
	                return;
	            }
	            // Mostra solo volontari attivi ancora disponibili
	            System.out.println("Elenco volontari attivi disponibili:");
	            for (int i = 0; i < volontariDisponibili.getVolontari().size(); i++) {
	                System.out.println((i + 1) + ") " + volontariDisponibili.getVolontari().get(i).getCredenziali().getUsername());
	            }
	            int sceltaVol = InputDati.leggiIntero("Scegli il numero del volontario: ", 1, volontariDisponibili.getVolontari().size());
	            v = volontariDisponibili.getVolontario(sceltaVol - 1);

	            if (v != null && v.isAttivo()) {
	                tipoScelto.getListaVolontari().aggiungiVolontario(v);
	                TipoVisitaDAO.gestisciOrfani(tipoScelto, v);
	                System.out.println("Volontario \"" + v.getCredenziali().getUsername() + "\" aggiunto a \"" + tipoScelto.getTitolo() + "\".");
	            }
	        }

	        continua = InputDati.yesOrNo("Vuoi aggiungerne un altro?");
	    } while (continua);

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
	    TipoStrada tipoStrada = TipoStrada.scegliDaNome(); // o il tuo metodo enum
	    String nomeStrada = InputDati.leggiStringaNonVuota("Inserisci il nome della strada: ");
	    String numeroCivico = InputDati.leggiStringaNonVuota("Inserisci il numero civico: ");
	    // Usa l'ambito territoriale già configurato (ambito)
	    return new Indirizzo(tipoStrada, nomeStrada, numeroCivico, ambito);
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