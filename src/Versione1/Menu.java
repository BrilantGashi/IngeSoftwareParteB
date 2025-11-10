package Versione1;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

import it.unibs.fp.mylib.*;


public class Menu{
	private static StatoSistema statoSistema = StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1;
    private static Calendario cal = new Calendario();
    private static CalendarioDAO CalendarioDAO = new CalendarioDAO();
    private static UtenteDAO UtenteDao = new UtenteDAO();
    private static DisponibilitaDAO DisponibilitaDAO = new DisponibilitaDAO();
    private static ArrayList<Configuratore> listaConfiguratore = new ArrayList<Configuratore>();
    private static ListaVolontari listaVolontari = new ListaVolontari();
    private static ArrayList<Fruitore> listaFruitori = new ArrayList<>();
	private static AmbitoTerritoriale ambito = new AmbitoTerritoriale();
    private static AmbitoTerritorialeDAO AmbitoTerritorialeDAO = new AmbitoTerritorialeDAO();
    private static ListaLuogo listaLuoghi = new ListaLuogo();
    private static LuogoDAO LuogoDAO = new LuogoDAO();
    private static ListaTipoVisita listaTipoVisita = new ListaTipoVisita();
    private static TipoVisitaDAO TipoVisitaDAO = new TipoVisitaDAO();
    private static ListaVisite listaVisite = new ListaVisite();
    private static VisitaDAO VisitaDAO = new VisitaDAO();
	private static ListaIscrizioni listaIscrizioniFruitore = new ListaIscrizioni();
	private static IscrizioneDAO listaIscrizioniFruitoreDAO = new IscrizioneDAO();
    private static int nMaxPersone = 5;
    private static String[] voci = {"Login come Configuratore/Volontario", "Login come Fruitore"};
    
    public static void login(boolean isFruitore) throws Exception {
    	String username;
		String password;
		String utenza = "";
		if(!isFruitore) {
		System.out.println("Inserisci lo username :");
		username = InputDati.leggiStringaNonVuota("Username: ").trim();
		
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
		if(utenza == "FRUITORE") {
			System.out.println("Attenzione stai cercando di accedere come fruitore!");
			return;
		}
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
		} else {
		        System.out.println("Inserisci lo username :");
		        username = InputDati.leggiStringaNonVuota("Username: ").trim();

		        // Controllo se username esistente tra i fruitori (puoi usare UtenteDAO o listaFruitori)
		        Credenziali credenziali = UtenteDAO.leggiSingolaCredenziale(username);

		        if (credenziali == null) {
		            // Username NON esiste → creazione nuovo fruitore
		            password = InputDati.leggiStringaNonVuota("Password: ");
		            Credenziali nuoveCredenziali = new Credenziali("FRUITORE", username, password);
		            Fruitore nuovoFruitore = new Fruitore(nuoveCredenziali);

		            listaFruitori.add(nuovoFruitore); // aggiungi a lista in memoria
		            UtenteDAO.aggiungiNuovoFruitore(nuovoFruitore); // aggiungi a DB

		            System.out.println("Nuovo fruitore creato con successo! Effettuato accesso.");
		            fruitore(username); // login diretto
		        } else if (credenziali.getUtenza().equals("FRUITORE")) {
		            // Username esiste ed è di tipo FRUITORE → login classico
		            password = InputDati.leggiStringaNonVuota("Password: ");
		            int okCred = credenziali.controllaCredenziali(username, password);

		            while (okCred == -1) {
		                password = InputDati.leggiStringaNonVuota("Password errata, riprovare: ");
		                okCred = credenziali.controllaCredenziali(username, password);
		            }

		            System.out.println("Accesso effettuato!");
		            fruitore(username); // login classico
		        } else {
		            // Username esistente MA non fruitore (caso teorico: collisione con admin/volontario)
		            System.out.println("Username già assegnato a un'altra tipologia di utenza. Impossibile accedere/creare fruitore.");
		        }
		    }
	}
		
  	public static boolean menuPrincipale() throws Exception{
  		
  		int sceltaMenu;
  		UtenteDao.leggiUtentiDaDB(listaConfiguratore, listaVolontari, listaFruitori);
  		CalendarioDAO.leggiCalendarioDaDB(cal);
  		MyMenu menu = new MyMenu("Menu", voci);
  		sceltaMenu = menu.scegli();
  		
  		switch (sceltaMenu) {
  		case 0:
  			return InputDati.yesOrNo("Vuoi veramente uscire?");			
  		case 1:
  			login(false);
  			break;
  		case 2:
  			login(true);
  		}
  		return false;		
  	}
  	
  	public static void configuratore(int okCred) throws Exception {
    int sceltaMenuConfig = 0;
    String[] vociConfig = {
        "Creazione Corpo Dati",
        "Inserimento date precluse e apertura raccolta disponibilita' dei volontari",
        "Chiusura raccolta disponibilità volontari/date precluse",
        "Modifica num max per fruitore",
        "Visualizza volontari",
        "Visualizza Luoghi",
        "Visualizza l'elenco dei tipi di visita associati a ciascun luogo",
        "Visualizza Stato Visite",
        "Genera piano delle visite",
        "Chiusura raccolta iscrizioni fruitori",
        "Modifica Luoghi/Tipi Visita/Volontari"
    };
    String[] vociConfigModifica = {
        "Aggiungi nuovo luogo per visite guidate",
        "Aggiungi tipi visita a luogo esistente",
        "Aggiungi volontario a tipo visita",
        "Rimuovi Luogo",
        "Rimuovi Tipo Visita",
        "Rimuovi Volontario"
    };

    MyMenu menu = new MyMenu("Menu configuratore", vociConfig);
    MyMenu menuModifica = new MyMenu("Menu di modifica", vociConfigModifica);

    if (okCred == 1) sceltaMenuConfig = 1;
    else sceltaMenuConfig = menu.scegli();

    do {
        switch (sceltaMenuConfig) {
        case 0:
            boolean indietro = InputDati.yesOrNo("Vuoi veramente tornare indietro?");
            if (indietro) return;
            break;

        case 1:
            if (statoSistema != StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1 ) {
                creazioneCorpoDati();
                statoSistema = StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1;
            } else {
                System.out.println("Corpo Dati già definito.");
                leggiCorpoDati();
            }
            break;

        case 2: // Inserimento date precluse e raccolta disponibilità volontari
            if (statoSistema != StatoSistema.RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3) {
                System.out.println("Non puoi inserire/le disponibilità o le date precluse in questo stato.\n" +
                    "Stato attuale: " + statoSistema.stampaStatoSistema());
            } else {
                inserimentoDatePrecluse();
            }
            break;

        case 3: 
            if (statoSistema != StatoSistema.RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3) {
                System.out.println("Non puoi chiudere la raccolta in questo stato.\n" +
                    "Stato attuale: " + statoSistema.stampaStatoSistema());
            } else {
                boolean chiudi = InputDati.yesOrNo("Vuoi veramente chiudere la raccolta delle disponibilità e delle date precluse?");
                if (chiudi) {
                    System.out.println("Raccolta chiusa con successo.");
                    statoSistema = StatoSistema.RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3_COMPLETATA;
                }
            }
            break;

        case 4:
        	if(statoSistema == StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1 || statoSistema == StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA
        			|| statoSistema == StatoSistema.GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE || statoSistema == StatoSistema.GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE_COMPLETATA);
            modificaNumFruitore();
            break;

        case 5:
            listaVolontari.stampaVolontariConVisite(listaTipoVisita.getTipi());
            break;

        case 6:
            listaLuoghi.stampaLuoghiSenzaTipoVisita();
            break;

        case 7:
            listaLuoghi.stampaLuoghiConTipoVisita();
            break;

        case 8:
            listaVisite.stampaTitoloEStato();
            break;

        case 9: 
            if (statoSistema != StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1) {
                System.out.println("Non puoi generare il piano visite in questo stato del sistema.\n" +
                    "Stato attuale: " + statoSistema.stampaStatoSistema());
            } else {
                generaPianoVisite();
                statoSistema = StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA;
            }
            break;

        case 10: 
            if (statoSistema != StatoSistema.RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1) {
                System.out.println("Non puoi chiudere la raccolta iscrizioni in questo stato.\n" +
                    "Stato attuale: " + statoSistema.stampaStatoSistema());
            } else {
                boolean chiudiIscr = InputDati.yesOrNo("Vuoi davvero chiudere la raccolta iscrizioni fruitori?");
                if (chiudiIscr) {
                    statoSistema = StatoSistema.RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3;
                    System.out.println("Raccolta iscrizioni chiusa.");
                }
            }
            break;

        case 11: // Modifiche al corpo dati
            if (statoSistema != StatoSistema.PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA) {
                System.out.println("Non puoi procedere alla modifica del corpo dati in questo stato.\n" +
                    "Stato attuale: " + statoSistema.stampaStatoSistema());
            } else {
                statoSistema = StatoSistema.GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE;
                int sceltaModifica = menuModifica.scegli();
                do {
                    switch (sceltaModifica) {
                    case 0:
                        boolean indietroMod = InputDati.yesOrNo("Proseguendo, non potrai più apportare modifiche al Corpo Dati. Confermi di voler uscire dal menu?");
                        if (indietroMod) {
                            statoSistema = StatoSistema.RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1;
                            return;
                        }
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
            }
            break;
        }
        sceltaMenuConfig = menu.scegli();
    } while (sceltaMenuConfig != 0);
  	}

  	
  	public static void volontario(int okCred, String nickname) throws Exception {
  	    ListaTipoVisita tipoVisitaPerVolontario = listaTipoVisita.getTipiPerVolontario(nickname);
  	    String[] vociVolontario = {
  	        "Visualizza tipi di visita associati",
  	        "Esprimi la disponibilita' per il mese: " + cal.getMeseI2()
  	    };
  	    MyMenu menu = new MyMenu("Menu volontario", vociVolontario);
  	    int sceltaMenuVolontario = menu.scegli();

  	    do {
  	        switch (sceltaMenuVolontario) {
  	        case 0: // Indietro
  	            boolean indietro = InputDati.yesOrNo("Vuoi veramente tornare indietro?");
  	            if (indietro) return;
  	            break;

  	        case 1: // Visualizza tipi visita associati
  	            System.out.println("Elenco tipi visita associati al volontario: " + nickname);
  	            System.out.println(tipoVisitaPerVolontario.toString().replace("----- ELENCO TIPI DI VISITA -----\n", ""));
  	            break;

  	        case 2: // Esprimi disponibilità per mese
  	            if (statoSistema != StatoSistema.RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3) {
  	                System.out.println("Non puoi esprimere disponibilità in questo stato del sistema.\nStato attuale: "
  	                    + statoSistema.stampaStatoSistema());
  	            } else {
  	                raccogliDisponibilitaVolontario(nickname);
  	            }
  	            break;
  	        }
  	        sceltaMenuVolontario = menu.scegli();
  	    } while (sceltaMenuVolontario != 0);
  	}

    
  	public static void fruitore(String nickname) throws Exception {
  		IscrizioneDAO.leggiIscrizioniDaDB(listaIscrizioniFruitore, listaFruitori, listaVisite);
  	    String[] vociFruitore = {
  	        "Visualizzare le visite attualmente nello stato di visita proposta/confermata/cancellata",
  	        "Effettuare l’iscrizione a una visita proposta",
  	        "Visualizzare le proprie iscrizioni e il loro stato",
  	        "Cancellare una propria iscrizione a una visita proposta/confermata"
  	    };
  	    MyMenu menu = new MyMenu("Menu fruitore", vociFruitore);
  	    int sceltaMenuFruitore = menu.scegli();

  	    do {
  	        switch (sceltaMenuFruitore) {
  	        case 0: 
  	            boolean indietro = InputDati.yesOrNo("Vuoi veramente tornare indietro?");
  	            if (indietro) return;
  	            break;

  	        case 1:
  	            listaVisite.stampaVisitaPerFruitore();
  	            break;

  	        case 2: 
  	            if (statoSistema != StatoSistema.GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE_COMPLETATA && statoSistema != StatoSistema.RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1) {
  	                System.out.println("Non puoi effettuare iscrizioni in questo stato del sistema.\nStato attuale: "
  	                    + statoSistema.stampaStatoSistema());
  	            } else {
  	                aggiungiIscrizioneFruitore(nickname);
  	                statoSistema = StatoSistema.RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1;
  	                
  	            }
  	            break;
  	        case 3: 
  	            listaIscrizioniFruitore.visualizzaIscrizioniFruitore(nickname);
  	            break;

  	        case 4: 
  	            if (statoSistema != StatoSistema.RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1) {
  	                System.out.println("Non puoi cancellare iscrizioni in questo stato del sistema.\nStato attuale: "
  	                    + statoSistema.stampaStatoSistema());
  	            } else {
  	                disdiciIscrizione(nickname);
  	            }
  	            break;
  	        }

  	        sceltaMenuFruitore = menu.scegli();
  	    } while (sceltaMenuFruitore != 0);
  	}

  	public static void creazioneCorpoDati () throws SQLException {
		System.out.println("-------CREAZIONE CORPO DATI INIZIALE-------");
		System.out.println("-------------------------------------------");
		System.out.println("Avvio configurazione Ambito Territoriale...");
		System.out.println("-------------------------------------------");
		
		String provincia = InputDati.leggiStringaNonVuota("Inserisci la provincia: ");
		String comune = InputDati.leggiStringaNonVuota("Inserisci il nome del comune: ");
		int cap = InputDati.leggiIntero("Inserisci il CAP: ", 10000, 99999);
		AmbitoTerritoriale ambitoTerritoriale = new AmbitoTerritoriale(cap, provincia, comune);
		AmbitoTerritorialeDAO.salvaAmbitoTerritoriale(ambitoTerritoriale);
		ambito = ambitoTerritoriale;

		System.out.println("\n--------------------------------------------------------");
		System.out.println("Definizione del limite massimo partecipanti per fruitore");
		System.out.println("--------------------------------------------------------");
		
		modificaNumFruitore();	
		
		System.out.println("----------------------------------------------------");
		System.out.println("Avvio procedura di definizione dei tipi di visita...");
		System.out.println("----------------------------------------------------");
		
		boolean continuoTipoVisita;
		do {
			System.out.println();
			aggiungiTipoVisita(); //false perche' nella V1 non si possono creare nuovi volontari
			continuoTipoVisita = InputDati.yesOrNo("Vuoi inserire un altro Tipo Visita?");
		} while (continuoTipoVisita);
		

		System.out.println("--------------------------------------------");
		System.out.println("Avvio procedura di definizione dei luoghi...");
		System.out.println("--------------------------------------------");
		
		boolean continuoLuoghi;
		do {
		    System.out.println();
		    aggiungiLuogo(false); // false: nella V1 non si possono creare nuovi volontari
		    continuoLuoghi = InputDati.yesOrNo("Vuoi inserire un altro luogo?");
		} while (continuoLuoghi);

		associaVolontariOrfani();
	}
  	
	
  	public static void leggiCorpoDati() throws Exception {	
  		
  		ambito = AmbitoTerritorialeDAO.leggiTuttiAmbitiTerritoriali();
		ambito.stampaAmbito();
		
		listaTipoVisita = TipoVisitaDAO.leggiTuttiTipiVisita(listaVolontari);
		System.out.println(listaTipoVisita.toString());
		
		listaLuoghi = LuogoDAO.leggiTuttiLuoghi(listaTipoVisita);
		listaLuoghi.stampaLuoghiConTipoVisita();
		
		CalendarioDAO.leggiCalendarioDaDB(cal);
		
		
	}
  	
  	
	public static void inserimentoDatePrecluse() throws SQLException {
		
		 System.out.println("Avvio procedura di inserimento date precluse per il mese di " + cal.getMeseI3());
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
		
		 int giorniNelMese = cal.giorniDelMese(cal.getMeseNumero(cal.getMeseI3()));
		 int giornoEscluso = InputDati.leggiIntero("Prego inserire il giorno ( compreso tra 1 e +" + giorniNelMese +" )", 1, giorniNelMese);
		 if (!cal.checkData(giornoEscluso,3)) {
			 cal.aggiungiDataPreclusa(giornoEscluso, 3);
			 CalendarioDAO.salvaDataPreclusa(giornoEscluso, 3);
		 } else {
			 System.out.println("Attenzione: il giorno scelto è già indicato come precluso. Inserire un altro giorno.");
		 }	 
	}
	
	public static void modificaNumFruitore() {
		nMaxPersone = InputDati.leggiInteroPositivo("Inserisci il numero massimo di persone che un fruitore puo' iscrivere a una iniziativa mediante una singola iscrizione: ");
	}
	
	
	// NUOVO METODO!! PER GENERARE PIANO VISITE
	public static void generaPianoVisite() throws SQLException {
			System.out.println("\n----- Generazione del piano delle visite per il mese " + cal.getMeseI1() + " -----\n");
			
				for(Volontario v : listaVolontari.getVolontari()) {
						ArrayList<Disponibilita> disponibilitaVolontario = DisponibilitaDAO.leggiDisponibilitaVolontario(v.getNickname());
						for(Disponibilita d : disponibilitaVolontario) {
							
							if(d.getMese()==1) {
								if (cal.isGiornoEscluso(d.getGiorno(), 1)) {
									continue;
								}
							ListaTipoVisita tipiVolontario = listaTipoVisita.getTipiPerVolontario(v.getNickname());
							for(TipoVisita tipo : tipiVolontario.getTipi()) {
								
								if(cal.isGiornoProgrammabilePerTipoVisita(d.getGiorno(), tipo)) {
								    int numero_mese = cal.getMeseNumero(cal.getMeseI1());
								    Data dataVisita = new Data(d.getGiorno(), numero_mese);

								    // 1. Controllo: il volontario NON deve avere altre visite in quella giornata
								    boolean volontarioGiaAssegnatoQuelGiorno = false;
								    for (Visita vExist : listaVisite.getListaVisite()) {
								        if (vExist.getDataVisita().equals(dataVisita)
								            && vExist.getVolontariEffettivi().getVolontari().contains(v)) {
								            volontarioGiaAssegnatoQuelGiorno = true;
								            break;
								        }
								    }
								    if (volontarioGiaAssegnatoQuelGiorno) {
								        continue; // salta questa assegnazione
								    }
							        if (listaVisite.visitaSovrapposta(tipo, dataVisita, listaLuoghi)) {
							            continue; 
							        }							       
							        Visita visitaEsistente = null;
							        for (Visita vExist : listaVisite.getListaVisite()) {
							        	
							            if (vExist.getTipoVisita().equals(tipo)
							                && vExist.getDataVisita().equals(dataVisita)
							                ) {
							                visitaEsistente = vExist;
							                break;
							            }
							        }
							        if (visitaEsistente != null) {
							            // La visita già esiste: aggiungi solo volontario se non già presente
							            if (!visitaEsistente.getVolontariEffettivi().getVolontari().contains(v)) {
							                visitaEsistente.getVolontariEffettivi().aggiungiVolontario(v);
							                // Se serve, aggiorna anche nel database la tabella relazione visita-volontario
							                VisitaDAO.collegaVolontarioAVisitaDB(visitaEsistente, v);							            }
							        } else {
							            // Non esiste, creala nuova
							            Visita nuova = new Visita(tipo, v, StatoVisita.PROPOSTA, dataVisita);
							            listaVisite.aggiungiVisita(nuova);
							            VisitaDAO.salvaVisita(nuova, v);
							        }
							    }
							}

							}
						}
					}
				listaVisite.stampaPianoVisite();
	}
	
	private static void raccogliDisponibilitaVolontario(String nickname) {
	    // nickname già validato a monte
	    int numeroMese = cal.getMeseNumero(cal.getMeseI2());
	    Data dataScelta = new Data();
	    boolean continua;
	    
	    do {
	        dataScelta.scegliDataPerMese(numeroMese);
	        boolean dataValida = cal.isGiornoEscluso(dataScelta.getGiorno(), 2);
	        if (dataScelta != null && !dataValida) {
	            Disponibilita nuova = new Disponibilita(
	                nickname, dataScelta.getGiorno(), 2
	            );
	            try {
	                DisponibilitaDAO.salvaDisponibilita(nuova);
	            } catch (SQLException e) {
	                System.err.println("Errore salvataggio disponibilità: " + e.getMessage());
	            	}
	        }
	        
	        else {
	        	System.out.println("Data non valida o preclusa. Riprova.");
	        }

	        continua = InputDati.yesOrNo(
	            "Vuoi esprimere un'altra disponibilita' per il mese di " + cal.getMeseI2() + "? "
	        );
	    } while (continua);
	    // Riepilogo solo una volta al termine
	    try {
	        ArrayList<Disponibilita> mie = DisponibilitaDAO.leggiDisponibilitaVolontario(nickname);
	        System.out.println("----- Le tue disponibilità per " + cal.getMeseI2() + " -----");
	        for (Disponibilita d : mie) {
	            if (d.getMese() == 2) {
	                System.out.println(" -" + d.getGiorno() + "/" + cal.getMeseI2());
	            }
	        }
	        System.out.println("---------------------------------------------");
	    	}catch (SQLException e) {
	        System.err.println("Errore lettura disponibilità: " + e.getMessage());
	    	}
	}

	private static void aggiungiIscrizioneFruitore(String nickname) throws SQLException {
	    Fruitore fruitore = cercaFruitoreNellaLista(nickname);
	    Visita visitaEffettiva = listaVisite.selezionaVisitaProposta(cal.getMeseI1(),fruitore,listaIscrizioniFruitore);

	    if (fruitore == null) {
	        System.out.println("Fruitore non trovato, registralo prima!");
	        return;
	    }

	    System.out.println("Visita selezionata: " + visitaEffettiva.getTipoVisita().getTitolo() + 
	        " del " + visitaEffettiva.getDataVisita().toString());
	    System.out.println("Quanti accompagnatori vuoi iscrivere (Compresa la propria iscrizione)? (massimo " 
	        + nMaxPersone + ") ");
	    int quotaTotale = InputDati.leggiIntero("Numero di accompagnatori: ", 0, nMaxPersone);

	    // Controlla che il fruitore non sia già iscritto a questa visita
	    Iscrizione giaPresente = listaIscrizioniFruitore.trovaPerFruitoreEVisita(fruitore, visitaEffettiva);
	    if (giaPresente != null) {
	        System.out.println("Sei già iscritto a questa visita.");
	        return;
	    }

	    // Calcolo posti totali in seguito all'iscrizione
	    int capienzaMassima = visitaEffettiva.getTipoVisita().getSogliaMaxPartecipanti();
	    int totaleAttuale = listaIscrizioniFruitore.getTotaleIscrittiPerVisita(visitaEffettiva);
	    if ((totaleAttuale + quotaTotale) > capienzaMassima) {
	        System.out.println("Iscrizione non accettata: si supererebbe la capienza (" 
	            + capienzaMassima + "). Posti rimanenti disponibili: " + (capienzaMassima - totaleAttuale));
	        return;
	    }

	    // Registra l'iscrizione
	    Iscrizione nuovaIscrizione = new Iscrizione(visitaEffettiva, fruitore, quotaTotale);
	    listaIscrizioniFruitore.aggiungi(nuovaIscrizione);
	    listaIscrizioniFruitoreDAO.inserisciIscrizione(nuovaIscrizione);
	    // Aggiorna lo stato della visita se si raggiunge la capienza
	    int nuovoTotale = listaIscrizioniFruitore.getTotaleIscrittiPerVisita(visitaEffettiva);
	    if (nuovoTotale == capienzaMassima) {
	        visitaEffettiva.setStatoVisita(StatoVisita.CONFERMATA);
	        Versione1.VisitaDAO.aggiornaStatoVisitaDB(visitaEffettiva, StatoVisita.CONFERMATA);
	    }
	    System.out.println("Iscrizione registrata. Codice prenotazione: " 
	        + nuovaIscrizione.getCodicePrenotazione());
	}
	
	private static Fruitore cercaFruitoreNellaLista(String nickname) {
	    for (Fruitore f : listaFruitori) {
	        if (f.getCredenziali().getUsername().equals(nickname)) {
	            return f;
	        }
	    }
	    return null; // Non trovato
	}

	public static void disdiciIscrizione(String usernameFruitore) throws SQLException {
	    // 1. Visualizza all'utente tutte le sue iscrizioni valide (con codici prenotazione)
	    System.out.println("\nLe tue iscrizioni:");
	    listaIscrizioniFruitore.visualizzaIscrizioniFruitore(usernameFruitore);

	    // 2. Chiedi il codice prenotazione della iscrizione che vuole disdire
	    String codicePrenotazione = InputDati.leggiStringaNonVuota("Inserisci il codice prenotazione che vuoi disdire (oppure lascia vuoto per annullare");;
	    if (codicePrenotazione == null || codicePrenotazione.trim().isEmpty()) {
	        System.out.println("Operazione annullata.");
	        return;
	    }

	    // 3. Chiamata al metodo di disdetta vera e propria
	    listaIscrizioniFruitoreDAO.disdiciIscrizione(codicePrenotazione);
	    boolean successo = listaIscrizioniFruitore.disdiciIscrizione(codicePrenotazione, usernameFruitore);
	    
	    // 4. Esito
	    if (successo) {
	        System.out.println("Disdetta eseguita con successo!");
	    } else {
	        System.out.println("Nessuna iscrizione disdetta: controlla di aver inserito il tuo codice correttamente e che la visita sia disdittabile.");
	    }
	}

	
	public static void rimuoviLuogo() throws SQLException {
	    System.out.println("-----------------------------------------------------------------------------");
	    System.out.println("Avvio procedura di eliminazione luoghi. Attenzione, operazione irreversibile!");
	    System.out.println("-----------------------------------------------------------------------------");
	    
	    boolean continuaLuogo = false;
	    
	    do {
	        // Mostra lista luoghi
	    	Luogo luogoSceltoDaRimuovere = listaLuoghi.selezionaLuogoAttivo();
	    	if (luogoSceltoDaRimuovere != null) {
		        String nomeLuogo = luogoSceltoDaRimuovere.getNomeLuogo();
		        boolean conferma = InputDati.yesOrNo("Sei sicuro di voler eliminare definitivamente il luogo '" + nomeLuogo + "'? ");
		        if (conferma) {
		            ListaTipoVisita tipiVisitaDaRimuovere = luogoSceltoDaRimuovere.getListaTipoVisita();
		            rimuoviTipiVisitaDaLuogo(luogoSceltoDaRimuovere, tipiVisitaDaRimuovere);
		            gestisciVolontariOrfani();   
		            System.out.println("Eliminazione luogo dal database...");
		            Versione1.LuogoDAO.eliminaLuogo(luogoSceltoDaRimuovere); 
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
	    System.out.println("--------------------------------------------------------------------------------------------");
	    System.out.println("Avvio procedura di rimozione tipo visita da un luogo. Verifica la scelta prima di procedere.");
	    System.out.println("--------------------------------------------------------------------------------------------");
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
	        System.out.println("Nessun tipo visita associato.");
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
	        tipoVisitaDaRimuovere.setAttivo(false);
	        TipoVisitaDAO.eliminaTipoVisita(tipoVisitaDaRimuovere);
	        gestisciVolontariOrfani();
	        gestisciLuoghiOrfani();
	        System.out.println("Tipo visita '" + nomeTipo + "' rimosso con successo!\n");
	    } else {
	        System.out.println("Operazione annullata.\n");
	    }
	}
	
	private static void rimuoviTipiVisitaDaLuogo(Luogo luogo, ListaTipoVisita tipiVisitaDaRimuovere) throws SQLException {
	    if (tipiVisitaDaRimuovere.getTipi().isEmpty()) {
	        System.out.println("Nessun tipo visita associato al luogo.");
	        return;
	    }
	  	System.out.println("Trovati " + tipiVisitaDaRimuovere.getTipi().size() + " tipi visita da eliminare:");
	    
	    for (TipoVisita tv : tipiVisitaDaRimuovere.getTipi()) {
	        System.out.print("  - " + tv.getTitolo());
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
	    TipoVisitaDAO.eliminaTipoVisita(tipoVisita);
	    tipoVisita.setAttivo(false);
	    System.out.println("Eliminato tipo visita: " + tipoVisita.getTitolo());
	}
	
	public static void rimuoviVolontario() throws SQLException {
	    System.out.println("-------------------------------------------------------------------------------------");
	    System.out.println("Avvio procedura di rimozione di un volontario. Attenzione, operazione irreversibile!.");
	    System.out.println("-------------------------------------------------------------------------------------");

	    boolean continuaVolontario=false;
	    
	    do {
	    	// Filtra solo volontari attivi
	    	ListaVolontari volontariAttivi = new ListaVolontari();
	    	for (Volontario v : listaVolontari.getVolontari()) {
	    	    if (v.isAttivo()) {
	    	        volontariAttivi.aggiungiVolontario(v);
	    	    }
	    	}
	    	if (volontariAttivi.getVolontari().isEmpty()) {
	    	    System.out.println("Non sono presenti volontari da rimuovere.");
	    	    return; // oppure break se sei in un ciclo
	    	}
	    	System.out.println("\nElenco volontari disponibili:");
	    	for (int i = 0; i < volontariAttivi.getVolontari().size(); i++) {
	    	    Volontario v = volontariAttivi.getVolontari().get(i);
	    	    System.out.println((i + 1) + ") " + v.getCredenziali().getUsername());
	    	}
	    	// Input solo sui volontari attivi!
	    	int sceltaVolontario = InputDati.leggiIntero(
	    	    "Inserisci il numero del volontario che si desidera rimuovere: ",
	    	    1,
	    	    volontariAttivi.getVolontari().size()
	    	);
	    	// Prendi il volontario attivo selezionato
	    	Volontario volontarioDaRimuovere = volontariAttivi.getVolontari().get(sceltaVolontario - 1);
	    	// ... procedi con la rimozione!
	        boolean conferma = InputDati.yesOrNo(
	            "Sei sicuro di voler eliminare il volontario '" + volontarioDaRimuovere.getNickname() + "'? "
	        );
	        if (conferma) {
	            try {
	                listaVolontari.rimuoviVolontario(volontarioDaRimuovere);
	                gestisciTipiVisitaOrfani();
	                gestisciLuoghiOrfani(); 
	                UtenteDAO.disattivaDaCredenziali(volontarioDaRimuovere);
	                System.out.println("Volontario '" + volontarioDaRimuovere.getNickname() + "' eliminato con successo!\n");         
	            } catch (SQLException e) {
	                System.out.println("Errore durante l'eliminazione: " + e.getMessage());
	                continue;  // Torna all'inizio del ciclo
	            }
	        } else {
	            System.out.println("Operazione annullata.");
	        }
	        if (listaVolontari.getVolontari().isEmpty()) {
	            System.out.println("Non ci sono più volontari da rimuovere.");
	            break;
	        }
	        continuaVolontario = InputDati.yesOrNo("Vuoi rimuovere un altro volontario? ");
	    } while (continuaVolontario);
	    System.out.println("\nProcedura completata.");
	}	


	private static void gestisciLuoghiOrfani() throws SQLException {	
	    ListaLuogo luoghiOrfani = listaLuoghi.trovaLuoghiOrfani();    
	    if (luoghiOrfani.getListaLuoghi().isEmpty()) {
	        return;
	    }
	    System.out.println("Trovati " + luoghiOrfani.getListaLuoghi().size() + " luoghi senza tipi visita:");
	    for (Luogo l : luoghiOrfani.getListaLuoghi()) {
	        System.out.println("      - " + l.getNomeLuogo());
	    }
	    for (Luogo l : luoghiOrfani.getListaLuoghi()) {
	        	Versione1.LuogoDAO.eliminaLuogo(l);
	            l.setAttivo(false); // Rimuovi anche dalla lista locale
	            System.out.println("Eliminato luogo: " + l.getNomeLuogo());
	    }
	}
	
	private static void gestisciTipiVisitaOrfani() throws SQLException {
	    ListaTipoVisita tipiOrfani = listaTipoVisita.trovaTipiVisitaOrfani();

	    if (tipiOrfani.getTipi().isEmpty()) {
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
	    ListaVolontari volontariOrfani = new ListaVolontari();
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
	            volontariOrfani.aggiungiVolontario(v);
	        }
	    }
	    if (volontariOrfani.getVolontari().isEmpty()) {
	        return;
	    }
	    System.out.println("Trovati " + volontariOrfani.getVolontari().size() + " volontari senza tipi visita:");
	    for (Volontario v : volontariOrfani.getVolontari()) {
	        System.out.println("      - " + v.getNickname());
	    }
	    for (Volontario v : volontariOrfani.getVolontari()) {
	        UtenteDAO.disattivaDaCredenziali(v);
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
	            System.out.println("Il volontario " + v.getNickname() + " è stato associato a " + visitaScelta.getTitolo());
	        }
	    } 
	}
	
	public static void aggiungiLuogo(boolean consentiNuoviVol) throws SQLException {
		String nomeLuogo = InputDati.leggiStringaNonVuota("Inserisci il nome del luogo: ").trim();
		String descrizione = InputDati.leggiStringaNonVuota("Inserisci una descrizione del luogo: ").trim();
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
	        ListaTipoVisita tipiAttiviDisponibili = new ListaTipoVisita();
	        for (TipoVisita tv : listaTipoVisita.getTipi()) {
	            if (tv.isAttivo() && !listaTipoVisitaToAdd.getTipi().contains(tv)) {
	                tipiAttiviDisponibili.add(tv);
	            }
	        }
	        // Stampa lista filtrata con numerazione coerente
	        System.out.println("Elenco Tipi Visite:");
	        int count = 1;
	        for (TipoVisita tv : tipiAttiviDisponibili.getTipi()) {
	            System.out.println(count + ") " + tv.toStringSemplificato());
	            count++;
	        }
	        // Input scelta da lista filtrata
	        sceltaTipo = InputDati.leggiIntero(
	            "Inserisci il numero del Tipo Visita che vuoi associare a questo Luogo: ",
	            1, tipiAttiviDisponibili.getTipi().size()
	        );
	        TipoVisita tipoScelto = tipiAttiviDisponibili.getTipi().get(sceltaTipo - 1);

	        if (listaTipoVisita.isAggiungibile(tipoScelto, listaTipoVisitaToAdd)) {
	            listaTipoVisitaToAdd.add(tipoScelto);
	            System.out.println("Visita aggiunta correttamente.");
	        }
	        if(consentiNuoviVol) {
	        
	        boolean continua = true;
	        do {
	        	ListaVolontari volontariDisponibili = new ListaVolontari();
		        for (Volontario vol : listaVolontari.getVolontari()) {
		        	if (vol.isAttivo() && !tipoScelto.getListaVolontari().getVolontari().contains(vol)) {
		        		volontariDisponibili.aggiungiVolontario(vol);
		        	}
		        }
	        	boolean nuovo = InputDati.yesOrNo("Vuoi aggiungere un volontario non ancora registrato al Tipo Visita?");
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
		                System.out.println("Tutti i volontari disponibili sono già associati al tipo visita selezionato.");
		                return;
		            }
		            // Mostra solo volontari attivi ancora disponibili
		            System.out.println("Elenco volontari disponibili per il Tipo Visita selezionato:");
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
	
		        continua = InputDati.yesOrNo("Vuoi aggiungerne un altro Volontario?");
		    } while (continua);
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

	public static void aggiungiTipoVisita() throws SQLException {
		String titolo = InputDati.leggiStringaNonVuota("Inserisci il titolo del tipo visita: ").trim();
		// Descrizione: non vuota
		String descrizione = InputDati.leggiStringaNonVuota("Inserisci una breve descrizione del tipo visita: ").trim();
		// Psunto incontro: non vuoto
		String puntoDiIncontro = InputDati.leggiStringaNonVuota("Inserisci il punto di incontro: ").trim();
		    
	    ArrayList<Giorni> giorniProgrammabiliSettimana = ObjGiorniSettimana();
	    LocalTime oraInizio = ObjOraFLT();
	    int durataVisita = InputDati.leggiInteroPositivo("Inserisci in minuti la durata della visita: ");
	    boolean biglietto = InputDati.yesOrNo("Inserisci N se NON serve il biglietto, S altrimenti.");
	    int sogliaMinPartecipanti = InputDati.leggiInteroNonNegativo("Inserisci soglia MINIMA dei partecipanti: ");
	    int sogliaMaxPartecipanti = InputDati.leggiIntero("Inserisci soglia MASSIMA dei partecipanti: ", sogliaMinPartecipanti + 1, Integer.MAX_VALUE);
	    Periodo periodoProgrammabile = ObjPeriodo();

	    ListaVolontari volontariToAdd = new ListaVolontari();
	    boolean continuaVolontario;

	    /*do {
	        // Se consenti aggiunta nuovi volontari, chiedi se vogliono crearne uno nuovo
	        if (consentiNuoviVolontari) {
	            boolean aggiungiNuovo = InputDati.yesOrNo("Vuoi aggiungere un nuovo volontario non presente nei registri? ");
	            if (aggiungiNuovo) {
	                Volontario nuovoVol = aggiungiNuovoVolontario();
	                if (nuovoVol != null) {
	                    volontariToAdd.aggiungiVolontario(nuovoVol);

	                    System.out.println("Volontario " + nuovoVol.getCredenziali().getUsername() + " creato. Sarà associato al tipo visita.");
	                    // Non aggiunto automaticamente a lista globale qui: si presume sia fatto dentro aggiungiNuovoVolontario()
	                }
	            }
	        }
	        // Costruisci lista volontari disponibili e attivi non ancora associati a questo tipo visita
	        ListaVolontari volontariDisponibili = new ListaVolontari();
	        for (Volontario v : listaVolontari.getVolontari()) {
	            if (v.isAttivo() && !volontariToAdd.getVolontari().contains(v)) {
	                volontariDisponibili.aggiungiVolontario(v);
	            }
	        }
	        if (volontariDisponibili.getVolontari().isEmpty()) {
	            System.out.println("Non ci sono volontari disponibili da associare.");
	            break;
	        }
	        // Stampa lista volontari disponibili con numerazione
	        System.out.println("Elenco Volontari disponibili:");
	        for (int i = 0; i < volontariDisponibili.getVolontari().size(); i++) {
	            System.out.println((i + 1) + ") " + volontariDisponibili.getVolontari().get(i).getCredenziali().getUsername());
	        }
	        // Input scelta volontario, con controllo validità
	        int sceltaVolontario;
	        do {
	            sceltaVolontario = InputDati.leggiInteroNonNegativo("Inserisci il numero del volontario da associare al tipo visita: ");
	            if (sceltaVolontario < 1 || sceltaVolontario > volontariDisponibili.getVolontari().size()) {
	                System.out.println("Scelta non valida, riprova.");
	            }
	        } while (sceltaVolontario < 1 || sceltaVolontario > volontariDisponibili.getVolontari().size());
	        // Aggiungi volontario selezionato alla lista del tipo visita in costruzione
	        Volontario scelto = volontariDisponibili.getVolontari().get(sceltaVolontario - 1);
	        volontariToAdd.aggiungiVolontario(scelto);
	        // Chiede se associare un altro volontario, sempre nel contesto di questo tipo visita
	        continuaVolontario = InputDati.yesOrNo("Vuoi associare un altro volontario a questo Tipo Visita? ");
	    } while (continuaVolontario);*/
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
	        return;
	    }
	    boolean continuaAggiunta = true;
	    do {
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

	    
	    System.out.println("Tipi di visita disponibili da aggiungere:");
	    System.out.println(tipiDisponibili.toStringAsList());
	    int sceltaTipo = InputDati.leggiIntero("Scegli il tipo di visita da aggiungere: ",
	                                               1, tipiDisponibili.getTipi().size());
	    TipoVisita tipoScelto = tipiDisponibili.getTipo(sceltaTipo - 1);
	    
	    boolean continua = true;
	        do {
	        	ListaVolontari volontariDisponibili = new ListaVolontari();
		        for (Volontario vol : listaVolontari.getVolontari()) {
		        	if (vol.isAttivo() && !tipoScelto.getListaVolontari().getVolontari().contains(vol)) {
		        		volontariDisponibili.aggiungiVolontario(vol);
		        	}
		        }
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
		                System.out.println("Tutti i volontari disponibili sono già associati al tipo visita selezionato.");
		                return;
		            }
		            // Mostra solo volontari attivi ancora disponibili
		            System.out.println("Elenco volontari disponibili:");
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
		        continua = InputDati.yesOrNo("Vuoi aggiungerne un altro Volontario?");
		    } while (continua);
	        // Aggiungi il tipo visita selezionato al luogo e salva modifiche
	        luogoScelto.getListaTipoVisita().getTipi().add(tipoScelto);
	        LuogoDAO.aggiungiTipoVisitaALuogo(luogoScelto, tipoScelto);
	        continuaAggiunta = InputDati.yesOrNo("Vuoi aggiungere un altro tipo di visita a questo luogo?");
	    } while (continuaAggiunta);
	    System.out.println("Il luogo è stato correttamente modificato.");
	}

	public static Volontario aggiungiNuovoVolontario() throws SQLException {

		String username = InputDati.leggiStringaNonVuota("Inserisci username per il nuovo volontario: ").trim();
	    Volontario volontarioEsistente = listaVolontari.cercaPerNickname(username);
	    if (volontarioEsistente != null) {
	        System.out.println("Il volontario '" + username + "' esiste già.");
	        return volontarioEsistente;  // 
	    }
	    UtenteDAO utenteDao = new UtenteDAO();
	    Credenziali credEsistente = null;
	    credEsistente = UtenteDAO.leggiSingolaCredenziale(username);
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
	        utenteDao.inserisciNuovoVolontario(nuovoVolontario);
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
	    ListaTipoVisita tipiAttivi = new ListaTipoVisita();
	    for (TipoVisita t : listaTipoVisita.getTipi()) {
	        if (t.isAttivo()) {
	            tipiAttivi.add(t);
	        }
	    }
	    if (tipiAttivi.getTipi().isEmpty()) {
	        System.out.println("Non ci sono tipi di visita attivi disponibili.");
	        return;
	    }
	    // Mostra elenco tipi visita attivi
	    System.out.println("Elenco tipi di visita attivi:");
	    for (int i = 0; i < tipiAttivi.getTipi().size(); i++) {
	        System.out.println((i + 1) + ") " + tipiAttivi.getTipi().get(i).getTitolo());
	    }
	    int scelta = InputDati.leggiIntero("Scegli il numero del tipo visita: ", 1, tipiAttivi.getTipi().size());
	    TipoVisita tipoScelto = tipiAttivi.getTipi().get(scelta - 1);
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
	                System.out.println("Tutti i volontari disponibili sono già associati al tipo visita selezionato.");
	                return;
	            }
	            // Mostra solo volontari attivi ancora disponibili
	            System.out.println("Elenco volontari disponibili:");
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
	        continua = InputDati.yesOrNo("Vuoi aggiungerne un altro Volontario?");
	    } while (continua);

	}
	
	public static Data ObjData(String descrizioneGiorno, String descrizioneMese) {
	    int giorno = 0;
	    int mese = 0;
	    Data data = null;
	    do {
	        try {
	            giorno = InputDati.leggiIntero(descrizioneGiorno, 1, 31);
	            mese = InputDati.leggiIntero(descrizioneMese, 1, 12);
	            data = new Data(giorno, mese); // il costruttore solleverà eccezioni solo se i dati non sono validi per Data
	        } catch (Exception e) {
	            System.out.println("Valori data non validi: " + e.getMessage() + ". Riprova.");
	            data = null;
	        }
	    } while (data == null);

	    return data;
	}
	
	public static ArrayList<Giorni> ObjGiorniSettimana() {
	    ArrayList<Giorni> giorniSelezionati = new ArrayList<>();
	    System.out.println("Inserisci i giorni della settimana in cui è possibile effettuare la visita.");
	    System.out.println("Usa i numeri da 1 a 7 (1=Lunedì, 2=Martedì, ..., 7=Domenica).");
	    boolean continua = true;
	    do {
	        int input = 0;
	        Giorni giorno = null;
	        do {
	            try {
	                input = InputDati.leggiIntero("Inserire giorno: ", 1, 7);
	                giorno = Giorni.values()[input - 1];
	            } catch (Exception e) {
	                giorno = null;
	                System.out.println("Giorno non valido! Riprova.");
	            }
	        } while (giorno == null);
	        if (giorniSelezionati.contains(giorno)) {
	            System.out.println("Giorno già inserito: " + Giorni.getNomeGiorno(input));
	        } else {
	            giorniSelezionati.add(giorno);
	            System.out.println("Aggiunto: " + Giorni.getNomeGiorno(input));
	        }
	        continua = InputDati.yesOrNo("Vuoi inserire un altro giorno? S per inserire, N per uscire. ");
	    } while (continua);
	    return giorniSelezionati;
	}
	
	public static Indirizzo ObjIndirizzo() {
	    Indirizzo indirizzo = null;
	    do {
	        try {
	            TipoStrada tipoStrada = TipoStrada.scegliDaNome();
	            String nomeStrada = InputDati.leggiStringaNonVuota("Inserisci il nome della strada: ");
	            String numeroCivico = InputDati.leggiStringaNonVuota("Inserisci il numero civico: ");
	            indirizzo = new Indirizzo(tipoStrada, nomeStrada, numeroCivico, ambito);
	        } catch (Exception e) {
	            System.out.println("Dati per indirizzo non validi: " + e.getMessage() + ". Riprova.");
	            indirizzo = null;
	        }
	    } while (indirizzo == null);
	    return indirizzo;
	}

	public static LocalTime ObjOraFLT() { 
	    LocalTime orario = null;
	    do {
	        try {
	            int ora = InputDati.leggiIntero("Inserisci ora inizio: ", 0, 23);
	            int minuti = InputDati.leggiIntero("Inserisci minuti inizio: ", 0, 59);
	            orario = LocalTime.of(ora, minuti);
	        } catch (Exception e) {
	            System.out.println("Orario non valido: " + e.getMessage() + ". Riprova.");
	            orario = null;
	        }
	    } while (orario == null);
	    return orario;
	}

	 public static Periodo ObjPeriodo() {
		    Periodo periodo = null;
		    do {
		        Data dataInizio = ObjData("Inserisci il giorno di inizio del periodo: ", "Inserisci il mese di inizio del periodo: ");
		        Data dataFine = ObjData("Inserisci il giorno di fine del periodo: ", "Inserisci il mese di fine del periodo: ");
		        try {
		            periodo = new Periodo(dataInizio, dataFine);
		        } catch (Exception e) {
		            System.out.println("Periodo non valido: " + e.getMessage() + ". Riprova.");
		            periodo = null;
		        }
		    } while (periodo == null);

		    return periodo;
		}

	 
}