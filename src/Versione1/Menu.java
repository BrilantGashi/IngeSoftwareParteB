package Versione1;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

import it.unibs.fp.mylib.*;


public class Menu{
	private static AmbitoTerritoriale ambito = new AmbitoTerritoriale();
	private static Calendario cal = new Calendario();
	private static ArrayList<Integer> capAmbito = new ArrayList<Integer>();  //modifico capAmbito in Integer perchÃ¨ semplifica le cose e non possiamo verificare se si tratta di una parola o un numero con string
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
		//System.out.println("NON VANNO TUTTI, CONTROLLARE PRIMA DI PROCEDERE");
		//LAIBA: ho tolto 0 - Torna al Menu Principale" perchï¿½ c'ï¿½ giï¿½ di default nel menu fplib. Stesso discorso per i numeri
		String[] vociConfig = {"Creazione Corpo Dati", "Avvio Calendario", "Modifica num max per fruitore","Visualizza volontari" ,
								"Visualizza Luoghi", "Visualizza l'elenco dei tipi di visita associati a ciascun luogo", "Visualizza Stato Visite", 
								"Genera piano delle visite", "Aggiungi nuovo luogo per visite guidate", "Aggiungi tipi visita a luogo esistente", 
								"Aggiungi volontario a tipo visita", "Rimuovi Luogo","Rimuovi Tipo Visita","Rimuovi Volontario"};
		//g.leggiUtentiDaFile(listaConfiguratore, listaVolontari);
				
		MyMenu menu = new MyMenu("Menu", vociConfig);
		sceltaMenuConfig= 1;		
		do {
			switch (sceltaMenuConfig) {
			case 0:
				//LAIBA: mi torna indietro ma non stampa conferma, da capire perchï¿½
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
					System.out.println("Corpo Dati giÃ  definito.");
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
				break;
			case 7:
				listaVisite.stampaTitoloEStato();
				break;
			case 8:
				generaPianoVisite();
				break;
			case 9:
				definizioneLuogo(ambito.getProvincia(), true);
				break;
			case 10:
				aggiungiTipoVisitaALuogo();
				break;
			case 11:
				aggiungiVolontarioATipoVisita();
				break;
			case 12:
				//rimuovere un luogo dall’elenco dei luoghi visitabili,
				rimuoviLuogo();
				break;
			case 13:
				//rimuovere un tipo di visita relativo a un luogo
				rimuoviTipoVisitaDaLuogo();
				break;
			case 14:
				// rimuovere un volontario dall’elenco dei volontari
				rimuoviVolontario();
				break;
				
			}
			sceltaMenuConfig = menu.scegli();
		} while (sceltaMenuConfig!=0);
		
		
	}
	
	public static void rimuoviTipoVisitaDaLuogo() throws SQLException {
	    System.out.println("================================================================");
	    System.out.println("RIMOZIONE TIPO DI VISITA DA LUOGO");
	    System.out.println("================================================================");
	    
	    // Mostra lista luoghi
	    System.out.println("\nElenco Luoghi:");
	    for (int i = 0; i < listaLuoghi.getListaLuoghi().size(); i++) {
	        Luogo l = listaLuoghi.getListaLuoghi().get(i);
	        System.out.println((i + 1) + ") " + l.getNomeLuogo());
	    }
	    
	    int sceltaLuogo = InputDati.leggiInteroNonNegativo(
	        "Seleziona il numero del luogo: "
	    );
	    
	    if (sceltaLuogo < 1 || sceltaLuogo > listaLuoghi.getListaLuoghi().size()) {
	        System.out.println("Selezione non valida!");
	        return;
	    }
	    
	    Luogo luogoScelto = listaLuoghi.getListaLuoghi().get(sceltaLuogo - 1);
	    
	    // Mostra tipi visita del luogo
	    System.out.println("\nTipi visita per '" + luogoScelto.getNomeLuogo() + "':");
	    ArrayList<TipoVisita> tipiDelLuogo = luogoScelto.getListaTipoVisita().getTipi();
	    
	    if (tipiDelLuogo.isEmpty()) {
	        System.out.println("   Nessun tipo visita associato.");
	        return;
	    }
	    
	    for (int i = 0; i < tipiDelLuogo.size(); i++) {
	        TipoVisita tv = tipiDelLuogo.get(i);
	        System.out.println((i + 1) + ") " + tv.getTitolo() + 
	                         " - Volontari: " + tv.getListaVolontari().getVolontari().size());
	    }
	    
	    int sceltaTipo = InputDati.leggiInteroNonNegativo(
	        "Seleziona il numero del tipo visita da rimuovere: "
	    );
	    
	    if (sceltaTipo < 1 || sceltaTipo > tipiDelLuogo.size()) {
	        System.out.println("Selezione non valida!");
	        return;
	    }
	    
	    TipoVisita tipoVisitaDaRimuovere = tipiDelLuogo.get(sceltaTipo - 1);
	    String nomeTipo = tipoVisitaDaRimuovere.getTitolo();
	    
	    // Conferma eliminazione
	    boolean conferma = InputDati.yesOrNo(
	        "Sei sicuro di voler rimuovere il tipo visita '" + nomeTipo + "' dal luogo '" + 
	        luogoScelto.getNomeLuogo() + "'? "
	    );
	    
	    if (conferma) {
	        // PASSO 1: Elimina il tipo visita completamente (con tutte le conseguenze)
	        System.out.println("\nEliminazione tipo visita dal database...");
	        TipoVisitaDAO.eliminaTipoVisita(tipoVisitaDaRimuovere);
	        
	        // PASSO 2: Rimuovi da listaTipoVisita
	        listaTipoVisita.rimuovi(tipoVisitaDaRimuovere);
	        
	        // PASSO 3: Rimuovi da tutti i luoghi
	        for (Luogo l : listaLuoghi.getListaLuoghi()) {
	            l.getListaTipoVisita().rimuovi(tipoVisitaDaRimuovere);
	        }
	        
	        // PASSO 4: Gestisci volontari orfani (senza più tipi visita)
	        System.out.println("Verifica volontari orfani...");
	        gestisciVolontariOrfani();
	        
	        // PASSO 5: Gestisci luoghi orfani (senza più tipi visita)
	        System.out.println("Verifica luoghi orfani...");
	        gestisciLuoghiOrfani();
	        
	        System.out.println("Tipo visita '" + nomeTipo + "' rimosso con successo!\n");
	    } else {
	        System.out.println("Operazione annullata.\n");
	    }
	}

	
	
	
	
	//************************* INIZIO ELIMINAZIONE LUOGO ******************************
	
	
	public static void rimuoviLuogo() throws SQLException {
	    System.out.println("================================================================");
	    System.out.println("INIZIO PROCEDURA ELIMINAZIONE LUOGHI, procedere con cautela.");
	    System.out.println("================================================================");
	    
	    boolean continuaLuogo = false;
	    
	    do {
	        // Mostra lista luoghi
	        System.out.println("\nElenco Luoghi disponibili:");
	        for (int i = 0; i < listaLuoghi.getListaLuoghi().size(); i++) {
	            Luogo l = listaLuoghi.getListaLuoghi().get(i);
	            System.out.println((i + 1) + ") " + l.getNomeLuogo());
	        }
	        
	        int sceltaLuogo = InputDati.leggiInteroNonNegativo(
	            "Inserisci il numero del luogo da rimuovere: "
	        );
	        
	        if (sceltaLuogo < 1 || sceltaLuogo > listaLuoghi.getListaLuoghi().size()) {
	            System.out.println("Selezione non valida!");
	            continue;
	        }
	        
	        Luogo luogoDaRimuovere = listaLuoghi.getListaLuoghi().get(sceltaLuogo - 1);
	        String nomeLuogo = luogoDaRimuovere.getNomeLuogo();
	        
	        // Conferma eliminazione
	        boolean conferma = InputDati.yesOrNo(
	            "Sei sicuro di voler eliminare il luogo '" + nomeLuogo + "'? "
	        );
	        
	        if (conferma) {
	            // PASSO 1: Salva i tipi visita del luogo prima di eliminarli
	            ArrayList<TipoVisita> tipiVisitaDaRimuovere = new ArrayList<>(
	                luogoDaRimuovere.getListaTipoVisita().getTipi()
	            );
	            
	            // PASSO 2: Elimina i tipi visita associati al luogo
	            rimuoviTipiVisitaDaLuogo(luogoDaRimuovere, tipiVisitaDaRimuovere);
	            
	            // PASSO 3: Gestisci volontari orfani (senza più tipi visita)
	            gestisciVolontariOrfani();
	            
	            // PASSO 4: Elimina il luogo dal database
	            System.out.println("Eliminazione luogo dal database...");
	            LuogoDAO.eliminaLuogo(luogoDaRimuovere);
	            
	            // PASSO 5: Rimuovi dalla lista locale
	            listaLuoghi.rimuovi(luogoDaRimuovere);
	            
	            System.out.println("Luogo '" + nomeLuogo + "' eliminato con successo!\n");
	        } else {
	            System.out.println("Operazione annullata.\n");
	        }
	        
	        continuaLuogo = InputDati.yesOrNo("Vuoi rimuovere un altro Luogo? ");
	        
	    } while (continuaLuogo);
	    
	    System.out.println("\nProcedura completata.");
	}

	private static void rimuoviTipiVisitaDaLuogo(Luogo luogo, ArrayList<TipoVisita> tipiVisitaDaRimuovere) throws SQLException {
	    if (tipiVisitaDaRimuovere.isEmpty()) {
	        System.out.println("Nessun tipo visita associato al luogo.");
	        return;
	    }
	    
	    System.out.println("Rimozione tipi visita associati al luogo...");
	    System.out.println("Trovati " + tipiVisitaDaRimuovere.size() + " tipi visita da eliminare:");
	    
	    for (TipoVisita tv : tipiVisitaDaRimuovere) {
	        System.out.println("      - " + tv.getTitolo());
	        
	        // Elimina il tipo visita (dal DB e da tutte le strutture)
	        eliminaTipoVisitaCompleto(tv);
	    }
	}

	private static void gestisciVolontariOrfani() throws SQLException {
	    ArrayList<Volontario> volontariOrfani = new ArrayList<>();
	    
	    // Trova volontari che non hanno più tipi visita associati
	    for (Volontario v : listaVolontari.getVolontari()) {
	        boolean haAssociazioni = false;
	        
	        // Controlla se il volontario è presente in almeno un tipo visita
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
	        listaVolontari.rimuoviVolontario(v);
	        
	        System.out.println("Eliminato volontario: " + v.getNickname());
	    }
	}

	private static void eliminaTipoVisitaCompleto(TipoVisita tipoVisita) throws SQLException {
	    // 1. Elimina da database (con CASCADE elimina anche da luogo_tipo_visita e tipo_visita_giorni)
	    TipoVisitaDAO.eliminaTipoVisita(tipoVisita);
	    
	    // 2. Rimuovi dalle associazioni volontari in memoria
	    for (Volontario v : tipoVisita.getListaVolontari().getVolontari()) {
	        // Nota: non eliminiamo il volontario, solo l'associazione
	        // Il volontario potrebbe avere altri tipi visita
	    }
	    
	    // 3. Rimuovi da listaTipoVisita
	    listaTipoVisita.rimuovi(tipoVisita);
	    
	    // 4. Rimuovi da tutti i luoghi in memoria
	    for (Luogo l : listaLuoghi.getListaLuoghi()) {
	        l.getListaTipoVisita().rimuovi(tipoVisita);
	    }
	    
	    System.out.println("Eliminato tipo visita: " + tipoVisita.getTitolo());
	}

	//************************* FINE ELIMINAZIONE LUOGO ******************************
	
	//************************* INIZIO ELIMINAZIONE VOLONTARIO ******************************
	public static void rimuoviVolontario() throws SQLException {
		System.out.println("================================================================");
		System.out.println("INIZIO PROCEDURA ELIMINAZIONE VOLONTARI, procedere con cautela.");
		System.out.println("================================================================");
		
		//fare vedere lista Volontari
		//selezionare numero
		//prima di eliminare volontario, deve essere eliminato dalle tabelle:
		/*
		 - VISITA --> NO, perchè sono visite ormai confermate
		 - tipovisita_volontario
		 - disponibilita
		 - E PER ULTIMO da Credenziali	 
		 
		  
		  
		  ******************IMPORTANTE*******************
		 se tipo visita rimane senza volontario: eliminare tale tipo visita
		 se luogo rimane senza tipo visita: eliminare tale luogo
		 * */
		
		//listaVolontari.stampaVolontari();
		boolean continuaVolontario;
		int sceltaVolontario;
		Volontario volontarioDaRimuovere;
		//ListaVolontari volontariToRem = new ListaVolontari();
		do {

			System.out.println("Elenco Volontari disponibili:");
			for(int i= 0; i < listaVolontari.getVolontari().size();i++) {
				Volontario v = listaVolontari.getVolontari().get(i);
				System.out.println((i+1) + ")" + v.getCredenziali().getUsername());
			}
			
			sceltaVolontario = InputDati.leggiInteroNonNegativo("Inserisci il numero del volontario da rimuovere: ");
			//volontariToRem.aggiungiVolontario(listaVolontari.getVolontario(sceltaVolontario - 1));
			volontarioDaRimuovere = listaVolontari.getVolontario(sceltaVolontario - 1);
			String username = volontarioDaRimuovere.getNickname();
			
			 boolean conferma = InputDati.yesOrNo(
			            "Sei sicuro di voler eliminare il volontario '" + username + "'? "
			        );

			 if (conferma) {
				 // PASSO 1: Rimuovi volontario dalle associazioni nel db (da tipo visita e disponibilita)
				 UtenteDAO.rimuoviVolontario(volontarioDaRimuovere);
				 
				// PASSO 2: Rimuovi volontario da listaTipoVisita in memoria
			    rimuoviVolontarioDaTipiVisita(volontarioDaRimuovere);
				 
				 // PASSO 3: Trova e gestisci tipi visita orfani, cioè senza volontari, nel db e da lista luoghi
				 gestisciTipiVisitaOrfani();
				 
				// PASSO 4: Rimuovi tipi visita orfani da listaLuoghi in memoria
			    //rimuoviTipiVisitaOrfaniDaLuoghi();
				 
				 // PASSO 3: Trova e gestisci luoghi orfani
				 gestisciLuoghiOrfani();				 
	            
				 // PASSO 4: SOLO ORA elimina il volontario da credenziali				 
				 UtenteDAO.eliminaDaCredenziali(volontarioDaRimuovere);
	            
				 // PASSO 5: Rimuovi dalla lista locale
				 listaVolontari.rimuoviVolontario(volontarioDaRimuovere);
				 //BISOGNA ANCHE RIMUOVERE IL TIPO VISITA/VOLONTARIO SINGOLO DA LISTALUOGO
				 
				 System.out.println("Volontario '" + username + "' eliminato con successo!\n");
			 }
			
			
			
			
			//check tipo visita e luogo
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
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

	
	private static void gestisciTipiVisitaOrfani() throws SQLException {
	    //TipoVisitaDAO tipoVisitaDAO = new TipoVisitaDAO();
	    
	    // Trova tutti i tipi visita senza volontari
	    ArrayList<TipoVisita> tipiOrfani = TipoVisitaDAO.trovaTipiVisitaOrfani(listaTipoVisita);
	    
	    if (tipiOrfani.isEmpty()) {
	        System.out.println("Nessun tipo visita orfano trovato.");
	        return;
	    }
	    
	    System.out.println("Trovati " + tipiOrfani.size() + " tipi visita senza volontari:");
	    for (TipoVisita tv : tipiOrfani) {
	        System.out.println("      - " + tv.getTitolo());
	    }
	    
	   /* boolean conferma = InputDati.yesOrNo(
	        "    Eliminare i tipi visita orfani? "
	    );
	    
	    if (conferma) {*/
	        for (TipoVisita tv : tipiOrfani) {
	        	TipoVisitaDAO.eliminaTipoVisita(tv);
	            listaTipoVisita.rimuovi(tv); // Rimuovi anche dalla lista locale
	            for (Luogo luogo : listaLuoghi.getListaLuoghi()) {
	                luogo.getListaTipoVisita().rimuovi(tv);
	            }
	            System.out.println("Eliminato tipo visita: " + tv.getTitolo());
	        }
	    /*} else {
	        System.out.println("     Tipi visita orfani mantenuti.");
	    }*/
	}

	private static void gestisciLuoghiOrfani() throws SQLException {
	    //LuogoDAO luogoDAO = new LuogoDAO();
	    
	    // Trova tutti i luoghi senza tipi visita
	    ArrayList<Luogo> luoghiOrfani = LuogoDAO.trovaLuoghiOrfani(listaLuoghi);
	    
	    if (luoghiOrfani.isEmpty()) {
	        System.out.println("Nessun luogo orfano trovato.");
	        return;
	    }
	    
	    System.out.println("Trovati " + luoghiOrfani.size() + " luoghi senza tipi visita:");
	    for (Luogo l : luoghiOrfani) {
	        System.out.println("      - " + l.getNomeLuogo());
	    }
	    
	    /*boolean conferma = InputDati.yesOrNo(
	        "    Eliminare i luoghi orfani? "
	    );
	    
	    if (conferma) {*/
	        for (Luogo l : luoghiOrfani) {
	        	LuogoDAO.eliminaLuogo(l);
	            listaLuoghi.rimuovi(l); // Rimuovi anche dalla lista locale
	            System.out.println("Eliminato luogo: " + l.getNomeLuogo());
	        }
	    /*} else {
	        System.out.println("     Luoghi orfani mantenuti.");
	    }*/
	}

	//************************* FINE ELIMINAZIONE VOLONTARIO ******************************
	
	public static void volontario (int okCred, String nickname) throws Exception {
		/*
			cose che deve fare un volontario nella V2:
			- visualizzare tutti i tipi di visita a cui egli Ã¨ associato
			- esprimere le disponibilitÃ  del mese i+2 , dove il volontario mette il giorno del mese(il mese Ã¨ giÃ  stampato e fissato)
			dove devi controllare che il giorno del mese sia uno dei giorni programmabile , c'Ã¨ gia il metodo in enum GIorni 
		*/
		ListaTipoVisita tipoVisitaPerVolontario = listaTipoVisita.getTipiPerVolontario(nickname);
		int sceltaMenuVolontario;
		String[] vociVolontario = {"Visualizza tipi di visita associati", "Esprimi la disponibilita'  per il mese :" + cal.getMeseI2()};
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
			     * - cal (Calendario) Ã¨ stato inizializzato correttamente
			     *
			     * INVARIANTI:
			     * - Ogni TipoVisita in tipoVisitaPerVolontario Ã¨ associato al volontario corrente
			     * - scegliDataPerTipo(t) restituisce null oppure una Data valida (giorno compreso nel mese i+2)
			     * - Una Visita Ã¨ identificata univocamente da (TipoVisita, Data)
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

			  
				for (TipoVisita t : tipoVisitaPerVolontario.getTipi()) {
			   
			        assert t != null : "Invariante violata: TipoVisita null";

			        boolean continua = true;
			        do {
			        Data dataScelta = cal.scegliDataPerTipo(t);

			        if (dataScelta != null) {
			            Volontario v = listaVolontari.cercaPerNickname(nickname);
			            assert v != null : "Invariante violata: volontario non trovato";

			            Visita nuovaVisita = new Visita(t, v, StatoVisita.PROPOSTA, dataScelta);

			            // Metodo aggiungiVisita deve garantire:
			            // - se esiste giÃ  una Visita con stesso tipo+data, aggiunge solo il volontario
			            // - altrimenti crea una nuova Visita
			            listaVisite.aggiungiVisita(nuovaVisita);
			            
			            //NUOVO METODO - salva disponibilita nel DB
			            try {
			                DisponibilitaDAO.salvaDisponibilita(nickname, dataScelta.getGiorno(), cal.getMeseI2());
			                
			                //QUI MI STAMPA LE DISPONIBILITA' INSERITE SOPRA
			                ArrayList<Disponibilita> mie = DisponibilitaDAO.leggiDisponibilitaVolontario(nickname);
			                System.out.println("==== Le tue disponibilità per " + cal.getMeseI2() + " ====");
			                for (Disponibilita d : mie) {
			                    if (d.getMese().equalsIgnoreCase(cal.getMeseI2())) {
			                        System.out.println("- " + d.getGiorno() + " " + d.getMese());
			                    }
			                }
			                System.out.println("==============================================");
			            } catch (SQLException e) {
			                System.out.println("Errore nel salvataggio della disponibilità: " + e.getMessage());
			            }
			        }
			        
			        	continua = InputDati.yesOrNo("Vuoi esprimere un'altra disponibilita' per i mese di" + cal.getMeseI2() + "? ");
			   			} while (continua);
			    }
				   
				
			    // POSTCONDIZIONE: tutte le visite in listaVisite rispettano lâ€™unicitÃ  (tipo+data)
			    assert listaVisite.visiteUnichePerTipoEData() : "Postcondizione violata: visite duplicate presenti";

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
		
		String provincia;
		provincia = InputDati.leggiStringaNonVuota("Inserisci la provincia: ");
		
		boolean continua;
		do {
	         int cap = InputDati.leggiIntero("Inserisci un CAP: ", 1, 99999);
	         if (capAmbito.contains(cap)) {
	             System.out.println("Attenzione: il CAP " + cap + " e' gia'  stato inserito!");
	         } else {
	             capAmbito.add(cap);
	         }
			 continua = InputDati.yesOrNo("Vuoi inserire un altro cap? S per inserire, N per uscire. ");
		} while (continua);
		
		AmbitoTerritoriale ambitoTerritoriale = new AmbitoTerritoriale(capAmbito, provincia);
		AmbitoTerritorialeDAO.salvaAmbitoTerritoriale(ambitoTerritoriale);
		ambito = ambitoTerritoriale;
		
		//********************** N MAX PARTECIPANTI PER FRUITORE ***********************************
		/*fissare il numero massimo di persone che un fruitore puï¿½ iscrivere a una iniziativa 
		mediante una singola iscrizione (tale numero ï¿½ valido per ogni visita)*/
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
			definizioneTipoVisita(false); //false perche' nella V1 non si possono creare nuovi volontari
			continuoTipoVisita = InputDati.yesOrNo("Vuoi inserire un altro Tipo Visita?");
		} while (continuoTipoVisita);
		

		System.out.println("------------------------------");
		System.out.println("DEFINIZIONE LUOGHI");
		System.out.println("------------------------------");
		//2. chiedere quanti luoghi vuole definire
		
		int luoghi = InputDati.leggiInteroNonNegativo("Quanti luoghi vuoi definire?");
		
		//3. per ogni luogo, scegliere tipo visita e almeno un volontario
		
		for (int i = 0; i < luoghi; i++) {
			//definizione luogo a cui si andra' ad associare un tipo visita e almeno un volontario
			System.out.println();
			System.out.println("Creazione luogo n. " + (i+1) + ": ");
			System.out.println();
			definizioneLuogo(provincia, false); //false perche' nella V1 non si possono creare nuovi volontari							
		}
		
		
		/*mbitoTerritoriale ambito,
        ListaLuogo listaLuoghi) throws Exception {*/
		//verifico prima di concludere la creaizione del corpo dati che tutti i volontari abbiano un tipovisita associato
		List<Volontario> orfani = listaVolontari.trovaVolontariSenzaVisite(listaTipoVisita);

		if (!orfani.isEmpty()) {
		    System.out.println("Attenzione ci sono volontari senza visite associate!");
		    for (Volontario v : orfani) {
		    	// Guida lâ€™utente ad associarlo
		        System.out.println("Scegli una visita per il volontario " + v.getNickname() + " :");
		        System.out.println(listaTipoVisita.toStringAsList());
		        int scelta = InputDati.leggiIntero("Numero visita: ",1,listaTipoVisita.getTipi().size());
		        TipoVisita visitaScelta = listaTipoVisita.getTipo(scelta - 1);
		        visitaScelta.getListaVolontari().aggiungiVolontario(v);
		        TipoVisitaDAO.gestisciOrfani(visitaScelta, v);
		        
		        System.out.println("Il volontario " + v.getNickname() + " e' stato associato a \"" + visitaScelta.getTitolo() + "\".");
		    }
		}
		/*try {
			//g.scriviCorpoDati(nMaxPersone, ambitoTerritoriale, listaLuoghi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	
	public static void definizioneLuogo (String pronvinciaAmbito, boolean consentiNuoviVol) throws SQLException {
		String nomeLuogo = InputDati.leggiStringaNonVuota("Inserisci il nome del luogo: ");
		String descrizione = InputDati.leggiStringa("Inserisci una descrizione del luogo: ");
		Indirizzo indirizzo = ObjIndirizzo(pronvinciaAmbito);
		
		//ogni LUOGO dovrebbe contenere al suo interno una LISTA DI TIPI VISITA (pg. 4, "A ciascun luogo ï¿½ associato almeno un tipo di visita")
			
		
		//mostrare tutti i tipi visita
		//chiedere quale associare al luogo
		//chiedere se ne vogliono associare un altro
					
		boolean continuaTipo = false;
		int sceltaTipo;
		//ArrayList<TipoVisita> tipiToAdd = new ArrayList<>();
		ListaTipoVisita listaTipoVisitaToAdd = new ListaTipoVisita();
		
		do {
		    // Se non ci sono piÃ¹ visite disponibili da associare â†’ esco subito
		    if (listaTipoVisita.getTipi().isEmpty() || 
		        listaTipoVisitaToAdd.getTipi().size() == listaTipoVisita.getTipi().size()) {
		        System.out.println("Non ci sono piu' Tipi Visita disponibili da associare a questo luogo.");
		        break;
		    }

		    // Mostra la lista delle visite disponibili
		    System.out.println("Elenco Tipi Visite:");
		    System.out.println(listaTipoVisita.toStringAsList());

		    // Scelta dell'utente
		    sceltaTipo = InputDati.leggiInteroNonNegativo(
		        "Inserisci il numero del Tipo Visita che vuoi associare a questo Luogo: "
		    );
		    TipoVisita visitaSelezionata = listaTipoVisita.getTipo(sceltaTipo - 1);

		    
		    //NUOVO BLOCCO GESTIONE NUOVE AGGIUNTE V3
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
		    boolean duplicato = false;
		    boolean sovrapposta = false;

		    // Controlli su duplicati e sovrapposizioni
		    for (TipoVisita v : listaTipoVisitaToAdd.getTipi()) {
		        if (v.equals(visitaSelezionata)) {
		            System.out.println("Attenzione: hai selezionato lo stesso Tipo Visita giÃ  associato!");
		            duplicato = true;
		            break;
		        } else if (v.siSovrappongono(visitaSelezionata)) {
		            System.out.println("Attenzione: questa visita si sovrappone ad un'altra giÃ  programmata per lo stesso giorno!");
		            sovrapposta = true;
		            break;
		        }
		    }

		    // Se non Ã¨ duplicata nÃ© sovrapposta, la aggiungo
		    if (!duplicato && !sovrapposta) {
		        listaTipoVisitaToAdd.add(visitaSelezionata);
		        System.out.println("Visita aggiunta correttamente.");
		    }

		    // Se dopo l'aggiunta non ci sono piÃ¹ visite disponibili â†’ esco
		    if (listaTipoVisitaToAdd.getTipi().size() == listaTipoVisita.getTipi().size()) {
		        System.out.println("Hai associato tutti i Tipi Visita disponibili a questo luogo.");
		        break;
		    }

		    // Altrimenti chiedo se continuare
		    continuaTipo = InputDati.yesOrNo("Vuoi associare un altro Tipo Visita a questo luogo? ");

		} while (continuaTipo);


	
		
		Luogo luogo = new Luogo(nomeLuogo, descrizione, indirizzo, listaTipoVisitaToAdd);
				
		System.out.println(luogo.toString());
		
		listaLuoghi.add(luogo);
		LuogoDAO.salvaLuogoCompleto(luogo);
	}

	
	/* ROXY
	 * Metodo già esistente modificato leggermente per gestire i nuovi volontari della v3;
	 * se il boolean è false, nelle voci della v1 non viene chiesto se si vuole aggiungere un nuovo volontario,
	 * viene messo a true nelle voci della v3 per permettere l'aggiunta di nuovi volontari
	 * 
	 * Se non avessi fatto così avrei avuto due metodi praticamente uguali e ridondanza del codice
	 * (se avete idee migliori aggiornatemi)
	 */
	public static void definizioneTipoVisita(boolean consentiNuoviVolontari) throws SQLException {
		
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
		ListaVolontari volontariToAdd = new ListaVolontari();
		int sceltaVolontario;
		
		//la modifica è qui all'interno del do while
		do {
			Volontario volontarioScelto = null;
			
			if(consentiNuoviVolontari) {
				boolean aggiungiNuovo = InputDati.yesOrNo("Vuoi aggiungere un nuovo volontario? ");
				
				if(aggiungiNuovo) {
					volontarioScelto = aggiungiNuovoVolontario();
				}
			}
			
			if(volontariToAdd.getVolontari().size()==listaVolontari.getVolontari().size()) {
				System.out.println("Nessun volontario disponibile");
				continuaVolontario=false;
				break;
			}
			
	        System.out.println("Elenco Volontari disponibili:");
			for(int i= 0; i < listaVolontari.getVolontari().size();i++) {
				Volontario v = listaVolontari.getVolontari().get(i);
				System.out.println((i+1) + ")" + v.getCredenziali().getUsername());
			}
			
			sceltaVolontario = InputDati.leggiInteroNonNegativo("Inserisci il numero del volontario da associare al tipo visita: ");
			volontariToAdd.aggiungiVolontario(listaVolontari.getVolontario(sceltaVolontario - 1));
			continuaVolontario = InputDati.yesOrNo("Vuoi associare un altro Volontario a questo Tipo Visita? ");
			
		}while(continuaVolontario);
		
		/*do {
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
		} while(continuaVolontario);*/
		
		TipoVisita tipoVisita = new TipoVisita(titolo, descrizione,  puntoDiIncontro, giorniProgrammabiliSettimana, oraInizio,
				durataVisita, biglietto, sogliaMinPartecipanti, sogliaMaxPartecipanti, periodoProgrammabile, volontariToAdd);
		
		System.out.println(tipoVisita.toString());
		
		listaTipoVisita.add(tipoVisita);
		TipoVisitaDAO.inserisciVisita(tipoVisita);
		
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
		listaTipoVisita = TipoVisitaDAO.leggiTuttiTipiVisita(listaVolontari);
		System.out.println(listaTipoVisita.toString());
		
		ambito = AmbitoTerritorialeDAO.leggiTuttiAmbitiTerritoriali();
		ambito.stampaAmbito();
		
		listaLuoghi = LuogoDAO.leggiTuttiLuoghi(listaTipoVisita);
		listaLuoghi.stampaLuoghiConTipoVisita();
	
		
	}

	
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
		 	- 0 se non è il primo accesso
		 	- 1 se è il primo accesso
		 	
		 	 questa info la devo mandare al metodo configuratore che, se ï¿½ il primo accesso, deve fare determinate cose
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
		//g.leggiUtentiDaFile(listaConfiguratore, listaVolontari);
		UtenteDao.leggiUtentiDaDB(listaConfiguratore, listaVolontari);
		
		/*for (Configuratore c : listaConfiguratore) {
			System.out.println(c.toString());
		}
		
		//listaConfiguratore.toString();
		listaVolontari.stampaVolontari();
		*/
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
	
	// NUOVO METODO!! PER GENERARE PIANO VISITE
	public static void generaPianoVisite() {
		System.out.println("\n===== Generazione del piano delle visite per il mese " + cal.getMeseI2() + " =====\n");
	    
		String meseConsiderato = cal.getMeseI2();
		
		try {
			for(Volontario v : listaVolontari.getVolontari()) {
				ArrayList<Disponibilita> disponibilitaVolontario = DisponibilitaDAO.leggiDisponibilitaVolontario(v.getNickname());
				
				for(Disponibilita d : disponibilitaVolontario) {
					if(d.getMese().equalsIgnoreCase(meseConsiderato)) {
					
						ListaTipoVisita tipiVolontario = listaTipoVisita.getTipiPerVolontario(v.getNickname());
						for(TipoVisita tipo : tipiVolontario.getTipi()) {
							Data dataVisita = new Data(d.getGiorno(), cal.getMeseNumero(meseConsiderato));
							Visita nuova = new Visita(tipo, v, StatoVisita.PROPOSTA, dataVisita);
							listaVisite.aggiungiVisita(nuova);
							
							VisitaDAO.salvaVisita(nuova);
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


	//NUOVO METODO PER CREARE NUOVO VOLONTARIO PER V3
	
	public static Volontario aggiungiNuovoVolontario() throws SQLException {
	 
	    String username = InputDati.leggiStringaNonVuota("Inserisci username per il nuovo volontario: ");
	    String passwordPredefinita = "default"; //password iniziale predefinita per semplificare le cose l'ho intesa cosi se no non so come fare
	    String utenza = "VOLONTARIO";

	    Credenziali nuoveCred = new Credenziali(utenza, username, passwordPredefinita);
	    Volontario nuovoVolontario = new Volontario(nuoveCred);

	    // Salva nel DB e nella listaVolontari
	    listaVolontari.aggiungiVolontario(nuovoVolontario);
	    UtenteDao.inserisciNuovoVolontario(nuovoVolontario);

	    System.out.println("\nNuovo volontario aggiunto con successo!");
	    System.out.println("Username: " + username);
	    System.out.println("Password predefinita: " + passwordPredefinita);
	    System.out.println("------------------------------------------");

	    return nuovoVolontario;
	}
	
	//NUOVO METODO V3 PER AGGIUNGERE TIPO VISITA A LUOGO ESISTENTE 
	//+ VOLONTARI NUOVI O GIA' ESISTENTI

	private static void aggiungiTipoVisitaALuogo() throws SQLException{

		if (listaLuoghi.getListaLuoghi().isEmpty()) {
	        System.out.println("Non ci sono luoghi esistenti.");
	        return;
	    }
		
		//elenco dei luoghi esistenti
		
		System.out.println("Elenco dei luoghi esistenti:");
	    for (int i = 0; i < listaLuoghi.getListaLuoghi().size(); i++) {
	        Luogo l = listaLuoghi.getListaLuoghi().get(i);
	        System.out.println((i + 1) + ") " + l.getNomeLuogo());
	    }
	    
	    int sceltaLuogo = InputDati.leggiIntero("Seleziona il luogo a cui aggiungere tipi di visita: ",
	    				1, listaLuoghi.getListaLuoghi().size());
	    Luogo luogoScelto = listaLuoghi.getListaLuoghi().get(sceltaLuogo - 1);
		
		//fa vedere i tipi visita non ancora associati al luogo
	    
	    ListaTipoVisita tipiDisponibili = new ListaTipoVisita();
	    for (TipoVisita t : listaTipoVisita.getTipi()) {
	        if (!luogoScelto.getListaTipoVisita().getTipi().contains(t)) {
	            tipiDisponibili.add(t);
	        }
	    }

	    if (tipiDisponibili.getTipi().isEmpty()) {
	        System.out.println("Tutti i tipi di visita sono già associati a questo luogo.");
	        return;
	    }
	    
	    boolean continuaAggiunta = true;
	    do {
	        System.out.println("Tipi di visita disponibili da aggiungere:");
	        System.out.println(tipiDisponibili.toStringAsList());

	        int sceltaTipo = InputDati.leggiIntero("Scegli il tipo di visita da aggiungere: ",
	                                               1, tipiDisponibili.getTipi().size());
	        TipoVisita tipoScelto = tipiDisponibili.getTipo(sceltaTipo - 1);

	        // gestisce i volontari (nuovi o già esistenti)
	        boolean aggiungiVolontari = InputDati.yesOrNo(
	            "Vuoi aggiungere volontari (nuovi o esistenti) per questo tipo di visita?");
	        if (aggiungiVolontari) {
	            definizioneTipoVisita(true); //richiama metodo con true per i nuovi volontari
	        }

	        // aggiungi il tipo al luogo e salva le modifiche
	        luogoScelto.getListaTipoVisita().add(tipoScelto);
	        LuogoDAO.salvaLuogoCompleto(luogoScelto);

	        continuaAggiunta = InputDati.yesOrNo("Vuoi aggiungere un altro tipo di visita a questo luogo?");
	    } while (continuaAggiunta);

	    System.out.println("Il luogo è stato correttamente modificato.");
	    
	}
	
	//NUOVO METODO V3 PER AGGIUNGERE VOLONTARIO NUOVO O ESISTENTE A TIPO VISITA ESISTENTE
	private static void aggiungiVolontarioATipoVisita() throws SQLException{
		
		if (listaTipoVisita.getTipi().isEmpty()) {
	        System.out.println("Non ci sono tipi di visita disponibili.");
	        return;
	    }

	    // mostra elenco tipi visita
	    System.out.println("Elenco tipi di visita:");
	    for (int i = 0; i < listaTipoVisita.getTipi().size(); i++) {
	        System.out.println((i + 1) + ") " + listaTipoVisita.getTipo(i).getTitolo());
	    }

	    int scelta = InputDati.leggiIntero("Scegli il numero del tipo visita: ", 1, listaTipoVisita.getTipi().size());
	    TipoVisita tipoScelto = listaTipoVisita.getTipo(scelta - 1);

	    System.out.println("\nHai selezionato: " + tipoScelto.getTitolo());

	    boolean continua = true;
	    do {
	        // Chiedo se aggiungere nuovo volontario o uno già esistente
	        boolean nuovo = InputDati.yesOrNo("Vuoi aggiungere un volontario ancora non registrato?");
	        Volontario v;

	        if (nuovo) {
	            v = aggiungiNuovoVolontario(); // uso il metodo creato apposta
	        } else {
	            if (listaVolontari.getVolontari().isEmpty()) {
	                System.out.println("Nessun volontario disponibile.");
	                return;
	            }

	            System.out.println("Elenco volontari esistenti:");
	            for (int i = 0; i < listaVolontari.getVolontari().size(); i++) {
	                System.out.println((i + 1) + ") " + listaVolontari.getVolontari().get(i).getCredenziali().getUsername());
	            }

	            int sceltaVol = InputDati.leggiIntero("Scegli il numero del volontario: ", 1, listaVolontari.getVolontari().size());
	            v = listaVolontari.getVolontario(sceltaVol - 1);
	        }

	        //salva nel db
	        if (v != null) {
	            tipoScelto.getListaVolontari().aggiungiVolontario(v);
	            TipoVisitaDAO.gestisciOrfani(tipoScelto, v); 
	            System.out.println("Volontario \"" + v.getCredenziali().getUsername() + "\" aggiunto a \"" + tipoScelto.getTitolo() + "\".");
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
	    System.out.println("Inserisci i giorni della settimana in cui Ã¨ possibile effettuare la visita.");
	    System.out.println("Usa i numeri da 1 a 7 (1=LunedÃ¬, 2=Martedi', 3=Mercoledi', 4=Giovedi', 5=Venerdi', 6=Sabato, 7=Domenica).");
	    boolean continua=true;
		do {
			 int input = InputDati.leggiIntero("Inserire giorno: ", 1, 7);
	         Giorni giorno = Giorni.values()[input - 1]; // converto numero â†’ enum
	         if (giorniSelezionati.contains(giorno)) {
	             System.out.println("Giorno giÃ  inserito: " + Giorni.getNomeGiorno(input));
	        	 continua = InputDati.yesOrNo("Vuoi inserire un altro giorno? S per inserire, N per uscire. ");

	         }else {
	        	 giorniSelezionati.add(giorno);
	        	 continua = InputDati.yesOrNo("Vuoi inserire un altro giorno? S per inserire, N per uscire. ");
	         }
		} while (continua);
	    
	    return giorniSelezionati;
	}
	
	public static Indirizzo ObjIndirizzo(String provincia) {

	    TipoStrada tipoStrada = TipoStrada.scegliDaNome();
	    String nomeStrada = InputDati.leggiStringaNonVuota("Inserisci il nome della Strada: ");
	    String numeroCivico = InputDati.leggiStringaNonVuota("Inserisci il numero civico: ");

	    
	    capAmbito = ambito.getCap();
	    //non capisco il senso di capAmbito se ce l'abbiamo nell'oggetto ambito. 
	    //sovrascrivo capAmbito al posto di modificarlo ogni volta con capAmbito
	    
	    System.out.println("Seleziona un CAP valido:");
	    //for (int i = 0; i < ambito.getCap().size(); i++) {
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