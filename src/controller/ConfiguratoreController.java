package controller;

import model.*;
import model.dao.*;
import view.ConfiguratoreView;
import view.ListaLuogoView;
import view.ListaVisiteView;
import view.ListaVolontariView;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

public class ConfiguratoreController {
	private ConfiguratoreView view;
	private StatoSistemaController statoSistemaController;
	private ListaVolontariModel listaVolontariModel;
	private ListaVolontariView listaVolontarioView;
	private ListaLuogoModel listaLuogoModel;
	private ListaLuogoView listaLuogoView;
	private ListaTipoVisitaModel listaTipoVisitaModel;
	private ListaFruitoreModel listaFruitoreModel;
	private ListaVisiteModel listaVisiteModel;
	private ListaVisiteView listaVisiteView;
	private AmbitoTerritorialeDAOModel ambitoTerritorialeDAOModel;
	private CalendarioModel calendarioModel;
	private ListaIscrizioniModel listaIscrizioniModel;
	// Iniezione di dipendenze aggiuntive MVC
	private VolontarioController volontarioController;
	private GiorniController giorniController;
	private DataController dataController;
	private TipoVisitaDAOModel tipoVisitaDAOModel;
	private LuogoDAOModel luogoDAOModel;
	private VisitaDAOModel visitaDAOModel;
	private IscrizioneDAOModel iscrizioneDAOModel;
	private CalendarioDAOModel calendarioDAOModel;
	private DisponibilitaDAOModel disponibilitaDAOModel; 
	private CredenzialiDAOModel credenzialiDAOModel;

	public ConfiguratoreController(
			ConfiguratoreView view,
			StatoSistemaController statoSistemaController,
			ListaVolontariModel listaVolontariModel,
			ListaVolontariView listaVolontarioView,
			ListaLuogoModel listaLuogoModel,
			ListaLuogoView listaLuogoView,
			ListaTipoVisitaModel listaTipoVisitaModel,
			ListaFruitoreModel listaFruitoreModel,
			ListaVisiteModel listaVisiteModel,
			ListaVisiteView listaVisiteView,
			AmbitoTerritorialeDAOModel ambitoTerritorialeDAOModel,
			ListaIscrizioniModel listaIscrizioniModel,
			VolontarioController volontarioController,
			GiorniController giorniController,
			DataController dataController,
			TipoVisitaDAOModel tipoVisitaDAOModel,
			LuogoDAOModel luogoDAOModel,
			VisitaDAOModel visitaDAOModel,
			IscrizioneDAOModel iscrizioneDAOModel,
			CalendarioModel calendarioModel,
			CalendarioDAOModel calendarioDAOModel,
			DisponibilitaDAOModel disponibilitaDAOModel,
			CredenzialiDAOModel credenzialiDAOModel
			) {
		this.view = view;
		this.statoSistemaController = statoSistemaController;
		this.listaVolontariModel = listaVolontariModel;
		this.listaVolontarioView = listaVolontarioView;
		this.listaLuogoModel = listaLuogoModel;
		this.listaLuogoView = listaLuogoView;
		this.listaTipoVisitaModel = listaTipoVisitaModel;
		this.listaFruitoreModel = listaFruitoreModel;
		this.listaVisiteModel = listaVisiteModel;
		this.listaVisiteView = listaVisiteView;
		this.ambitoTerritorialeDAOModel = ambitoTerritorialeDAOModel;
		this.volontarioController = volontarioController;
		this.giorniController = giorniController;
		this.dataController = dataController;
		this.tipoVisitaDAOModel = tipoVisitaDAOModel;
		this.luogoDAOModel = luogoDAOModel; 
		this.visitaDAOModel = visitaDAOModel;
		this.iscrizioneDAOModel = iscrizioneDAOModel;
		this.calendarioModel = calendarioModel;
		this.calendarioDAOModel = calendarioDAOModel;
		this.disponibilitaDAOModel = disponibilitaDAOModel;
		this.credenzialiDAOModel = credenzialiDAOModel;
	}

	// Menu principale configuratore
	public void menuConfiguratore(String username) throws Exception {
		boolean indietro = false;
		do {
			int scelta = view.scegliVoceMenuPrincipale();
			StatoSistemaModel statoCorrente = statoSistemaController.getStatoCorrente();

			switch (scelta) {
			case 0:
				indietro = view.chiediConfermaIndietro();
				break;
			case 1:
				gestisciCorpoDati();
				break;
			case 2:
				gestisciDatePrecluseERaccoltaVolontari();
				break;
			case 3:
				gestisciChiusuraRaccolta();
				break;
			case 4:
				modificaNumMaxFruitore();
				break;
			case 5:
				visualizzaVolontari();
				break;
			case 6:
				visualizzaLuoghi();
				break;
			case 7:
				visualizzaTipoVisitaConLuoghi();
				break;
			case 8:
				visualizzaStatoVisite();
				break;
			case 9:
				generaPianoVisite();
				break;
			case 10:
				gestisciChiusuraIscrizioniFruitore();
				break;
			case 11:
				if (statoCorrente != StatoSistemaModel.PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA 
				&& statoCorrente != StatoSistemaModel.GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE) {
					view.mostraMessaggio("Non puoi procedere alla modifica del corpo dati in questo stato.");
					statoSistemaController.mostraStato();
				} else {
					statoSistemaController.aggiornaStato(StatoSistemaModel.GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE);
					menuModificaCorpoDati();
				}
				break;
			}
		} while (!indietro);
	}

	public void menuModificaCorpoDati() throws Exception {
		boolean uscita = false;
		do {
			int scelta = view.scegliVoceMenuModifica();
			switch (scelta) {
			case 0:
				boolean indietroMod = view.chiediConfermaUscitaModifiche();
				if (indietroMod) {
					statoSistemaController.aggiornaStato(StatoSistemaModel.RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1);
					uscita = true;
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
		} while (!uscita);
	}

	public void gestisciCorpoDati() throws Exception {
		int scelta = view.scegliOperazioneCorpoDati();
		switch (scelta) {
		case 0:
			break;
		case 1:
			creaCorpoDati();
			break;
		case 2:
			leggiCorpoDati();
			break;
		}
	}

	public void creaCorpoDati() throws SQLException {
		view.mostraMessaggio("-------CREAZIONE CORPO DATI INIZIALE-------");
		view.mostraMessaggio("Avvio configurazione Ambito Territoriale...");

		String provincia = view.leggiProvincia();
		String comune = view.leggiComune();
		int cap = view.leggiCap();

		AmbitoTerritorialeModel ambitoTerritoriale = new AmbitoTerritorialeModel(cap, provincia, comune);
		AmbitoTerritorialeDAOModel.salvaAmbitoTerritoriale(ambitoTerritoriale); 

		modificaNumMaxFruitore();

		view.mostraMessaggio("Avvio procedura di definizione dei tipi di visita...");
		boolean continuoTipoVisita;
		do {
			aggiungiTipoVisita(false); // false per V1
			continuoTipoVisita = view.continuaInserimento("Tipo Visita");
		} while (continuoTipoVisita);

		view.mostraMessaggio("Avvio procedura di definizione dei luoghi...");
		boolean continuoLuoghi;
		do {
			aggiungiLuogo(false);
			continuoLuoghi = view.continuaInserimento("luogo");
		} while (continuoLuoghi);

		associaVolontariOrfani();
		view.mostraMessaggio("CREAZIONE CORPO DATI COMPLETATA!");
	}

	public void leggiCorpoDati() throws Exception {
		// 1. Ambito territoriale
		AmbitoTerritorialeModel ambitoTerritorialeModel = AmbitoTerritorialeDAOModel.leggiTuttiAmbitiTerritoriali();
		view.mostraMessaggio("--- AMBITO TERRITORIALE ---");
		view.mostraMessaggio(ambitoTerritorialeModel.toString());

		// 2. Tipi visita dal DB, (passando lista volontari per corretta associazione)
		listaTipoVisitaModel.setTipi(
				tipoVisitaDAOModel.leggiTuttiTipiVisita(listaVolontariModel)
				);
		view.mostraMessaggio("--- TIPI VISITA ---");
		view.stampaTipiVisitaDisponibili(listaTipoVisitaModel);

		// 3. Luoghi con tipi visita
		listaLuogoModel.setLuoghi(
				luogoDAOModel.leggiTuttiLuoghi(listaTipoVisitaModel)
				);
		view.mostraMessaggio("--- LUOGHI E TIPI VISITA ---");
		view.stampaLuoghiConTipoVisita(listaLuogoModel);
		
		
		// 4. Visite dal DB
		//VisitaDAOModel.leggiVisiteDaDB(listaVisiteModel.getListaVisite(), listaTipoVisitaModel, listaVolontariModel);

		// 5. Calendario dal DB
		// Se hai un campo calendarioModel globale, usalo qui:
		calendarioDAOModel.leggiCalendarioDaDB(calendarioModel);

		// 6. Iscrizioni fruitori dal DB
		// Se hai o prevedi un ListaIscrizioniModel, passa quello, altrimenti gestisci come sotto
		iscrizioneDAOModel.leggiIscrizioniDaDB(listaFruitoreModel, listaVisiteModel);

		view.mostraMessaggio("Lettura corpo dati completata.");
	}

	public void modificaNumMaxFruitore() {
		view.mostraMessaggio("Definizione del limite massimo partecipanti per fruitore");
		int valoreConfigurato = view.leggiNumMaxPersone();
		ParametriSistema.getInstance().setNMaxAccompagnatori(valoreConfigurato);
		view.mostraMessaggio("Numero massimo aggiornato: " + valoreConfigurato);
		ParametriSistema.getInstance().bloccaConfigurazione();
	}

	public void aggiungiTipoVisita(boolean consentiNuoviVolontari) throws SQLException {
		String titolo = view.leggiTitoloTipoVisita();
		String descrizione = view.leggiDescrizioneTipoVisita();
		String puntoDiIncontro = view.leggiPuntoDiIncontro();

		ArrayList<GiorniModel> giorniProgrammabiliSettimana = view.leggiGiorniSettimana(giorniController);
		LocalTime oraInizio = view.leggiOraInizio();
		int durataVisita = view.leggiDurataVisita();
		boolean biglietto = view.leggiRichiestaBiglietto();
		int sogliaMinPartecipanti = view.leggiSogliaMinimaPartecipanti();
		int sogliaMaxPartecipanti = view.leggiSogliaMassimaPartecipanti(sogliaMinPartecipanti + 1);
		PeriodoModel periodoProgrammabile = view.leggiPeriodoProgrammabile(dataController);

		ListaVolontariModel volontariToAdd = new ListaVolontariModel();
		boolean continuaVolontario;
		do {
			if (consentiNuoviVolontari) {
				boolean aggiungiNuovo = view.yesOrNo("Vuoi aggiungere un nuovo volontario non presente nei registri?");
				if (aggiungiNuovo) {
					VolontarioModel nuovoVol = volontarioController.aggiungiNuovoVolontario();
					if (nuovoVol != null) {
						volontariToAdd.aggiungiVolontario(nuovoVol);
						view.mostraMessaggio("Volontario " + nuovoVol.getNickname() + " creato.");
					}
				}
			}
			ListaVolontariModel volontariDisponibili = new ListaVolontariModel();
			for (VolontarioModel v : listaVolontariModel.getVolontari()) {
				if (v.isAttivo() && !volontariToAdd.getVolontari().contains(v)) {
					volontariDisponibili.aggiungiVolontario(v);
				}
			}
			if (volontariDisponibili.getVolontari().isEmpty()) {
				view.mostraMessaggio("Non ci sono volontari disponibili da associare.");
				break;
			}
			view.stampaVolontariDisponibili(volontariDisponibili);
			int sceltaVolontario = view.scegliVolontario(volontariDisponibili);
			VolontarioModel scelto = volontariDisponibili.getVolontari().get(sceltaVolontario - 1);
			volontariToAdd.aggiungiVolontario(scelto);
			continuaVolontario = view.yesOrNo("Vuoi associare un altro volontario a questo Tipo Visita?");
		} while (continuaVolontario);

		TipoVisitaModel tipoVisita = new TipoVisitaModel(
				titolo, descrizione, puntoDiIncontro,
				giorniProgrammabiliSettimana, oraInizio, durataVisita,
				biglietto, sogliaMinPartecipanti, sogliaMaxPartecipanti,
				periodoProgrammabile, volontariToAdd
				);
		view.mostraMessaggio(tipoVisita.toString());

		try {
			tipoVisitaDAOModel.inserisciVisita(tipoVisita);
			if (!listaTipoVisitaModel.contains(tipoVisita)) {
				listaTipoVisitaModel.add(tipoVisita);
			} else {
				view.mostraMessaggio("Attenzione! Tipo Visita già presente nella lista!");
			}
		} catch (SQLException e) {
			view.mostraMessaggio("Errore nel salvataggio: " + e.getMessage());
		}
	}

	public void aggiungiLuogo(boolean consentiNuoviVolontari) throws SQLException {
		// Input luogo tramite view
		String nomeLuogo = view.leggiNomeLuogo();
		String descrizione = view.leggiDescrizioneLuogo();
		AmbitoTerritorialeModel ambito = AmbitoTerritorialeDAOModel.leggiTuttiAmbitiTerritoriali();
		IndirizzoModel indirizzo = view.leggiIndirizzo(ambito);

		ListaTipoVisitaModel listaTipoVisitaToAdd = new ListaTipoVisitaModel();

		boolean continuaTipo; 
		do {
			// Filtra solo tipi attivi non ancora aggiunti
			ListaTipoVisitaModel tipiAttiviDisponibili = new ListaTipoVisitaModel();
			for (TipoVisitaModel tv : listaTipoVisitaModel.getTipi()) {
				if (tv.isAttivo() && !listaTipoVisitaToAdd.contains(tv)) {
					tipiAttiviDisponibili.add(tv);
				}
			}

			if (tipiAttiviDisponibili.size() == 0) {
				view.mostraMessaggio("Non ci sono Tipi Visita disponibili da associare a questo luogo.");
				break;
			}

			// Mostra tipi disponibili tramite view
			view.stampaTipiVisitaDisponibili(tipiAttiviDisponibili);
			int sceltaTipo = view.scegliTipoVisita(tipiAttiviDisponibili);
			TipoVisitaModel tipoScelto = tipiAttiviDisponibili.getTipo(sceltaTipo - 1);

			// Aggiungi Tipo Visita al luogo (se non già presente)
			if (!listaTipoVisitaToAdd.contains(tipoScelto)) {
				listaTipoVisitaToAdd.add(tipoScelto);
				view.mostraMessaggio("Tipo Visita aggiunto correttamente.");
			}

			// Associazione volontari al Tipo Visita (se permesso)
			if (consentiNuoviVolontari) {
				boolean continuaVolontario;
				do {
					// Costruisci solo volontari attivi non ancora associati a questo tipo visita
					ListaVolontariModel volontariDisponibili = new ListaVolontariModel();
					for (VolontarioModel vol : listaVolontariModel.getVolontari()) {
						if (vol.isAttivo() && !tipoScelto.getListaVolontari().getVolontari().contains(vol)) {
							volontariDisponibili.aggiungiVolontario(vol);
						}
					}

					boolean nuovo = view.yesOrNo("Vuoi aggiungere un volontario non ancora registrato al Tipo Visita?");
					VolontarioModel volontarioDaAggiungere;

					if (nuovo) {
						volontarioDaAggiungere = volontarioController.aggiungiNuovoVolontario();
						if (volontarioDaAggiungere != null && volontarioDaAggiungere.isAttivo()) {
							tipoScelto.getListaVolontari().aggiungiVolontario(volontarioDaAggiungere);
							tipoVisitaDAOModel.gestisciOrfani(tipoScelto, volontarioDaAggiungere);
							view.mostraMessaggio("Volontario \"" + volontarioDaAggiungere.getNickname() +
									"\" aggiunto a \"" + tipoScelto.getTitolo() + "\".");
						} else if (volontarioDaAggiungere != null) {
							view.mostraMessaggio("Il volontario appena creato NON è attivo, quindi non verrà associato.");
						}
					} else {
						if (volontariDisponibili.size() == 0) {
							view.mostraMessaggio("Tutti i volontari disponibili sono già associati al tipo visita selezionato.");
							break;
						}
						view.stampaVolontariDisponibili(volontariDisponibili);
						int sceltaVol = view.scegliVolontario(volontariDisponibili);
						VolontarioModel volontarioScelto = volontariDisponibili.getVolontario(sceltaVol - 1);

						if (volontarioScelto != null && volontarioScelto.isAttivo()) {
							tipoScelto.getListaVolontari().aggiungiVolontario(volontarioScelto);
							tipoVisitaDAOModel.gestisciOrfani(tipoScelto, volontarioScelto);
							view.mostraMessaggio("Volontario \"" + volontarioScelto.getNickname() +
									"\" aggiunto a \"" + tipoScelto.getTitolo() + "\".");
						}
					}

					continuaVolontario = view.yesOrNo("Vuoi aggiungere un altro volontario a questo Tipo Visita?");
				} while (continuaVolontario);
			}

			continuaTipo = view.yesOrNo("Vuoi associare un altro Tipo Visita a questo luogo?");
		} while (continuaTipo);

		LuogoModel luogo = new LuogoModel(nomeLuogo, descrizione, indirizzo, listaTipoVisitaToAdd);
		view.mostraMessaggio(luogo.toString());

		try {
			luogoDAOModel.salvaLuogoCompleto(luogo);
			if (!listaLuogoModel.contains(luogo)) {
				listaLuogoModel.aggiungiLuogo(luogo);
				view.mostraMessaggio("Luogo creato e salvato con successo.");
			} else {
				view.mostraMessaggio("ATTENZIONE: Luogo già presente nella lista!");
			}
		} catch (SQLException e) {
			view.mostraMessaggio("Errore nel salvataggio del luogo: " + e.getMessage());
		}
	}

	public void associaVolontariOrfani() throws SQLException {
		// Trova i volontari senza visite usando ListaVolontariModel + ListaTipoVisitaModel
		ListaVolontariModel orfani = listaVolontariModel.trovaVolontariSenzaVisite(listaTipoVisitaModel);

		if (orfani.size() > 0) {
			view.mostraMessaggio("Attenzione: ci sono volontari senza visite associate!");
			for (VolontarioModel v : orfani.getVolontari()) {
				view.mostraMessaggio("\nScegli una visita per il volontario " + v.getNickname() + ":");
				view.stampaTipiVisitaDisponibili(listaTipoVisitaModel);
				int scelta = view.scegliTipoVisita(listaTipoVisitaModel);
				TipoVisitaModel visitaScelta = listaTipoVisitaModel.getTipo(scelta - 1);
				visitaScelta.getListaVolontari().aggiungiVolontario(v);
				tipoVisitaDAOModel.gestisciOrfani(visitaScelta, v); // aggiorna sul DB
				view.mostraMessaggio("Il volontario " + v.getNickname() +
						" è stato associato a \"" + visitaScelta.getTitolo() + "\"");
			}
		}
	}

	public void gestisciDatePrecluseERaccoltaVolontari() throws Exception {
		// ... altre logiche
		inserisciDatePrecluse(calendarioModel, calendarioDAOModel, 3, view); // qui 3 = mese I3
		// ... continua col resto
	}

	public void inserisciDatePrecluse(CalendarioModel calendario, CalendarioDAOModel calendarioDAOModel, int numeroMese, ConfiguratoreView view) throws SQLException {
		String nomeMese = calendario.getNomeMeseI(numeroMese);
		view.mostraMessaggio("Avvio procedura di inserimento date precluse per il mese di " + nomeMese);

		boolean continua;
		do {
			int giorniNelMese = calendario.giorniDelMese(calendario.getMeseNumero(nomeMese));
			int giornoEscluso = view.leggiGiornoPrecluso(giorniNelMese, nomeMese);

			// LOGICA BUSINESS (chiamata a Model)
			if (!calendario.checkData(giornoEscluso, numeroMese)) {
				calendario.aggiungiDataPreclusa(giornoEscluso, numeroMese);
				calendarioDAOModel.salvaDataPreclusa(giornoEscluso, calendario.getMeseNumero(nomeMese));
				view.mostraMessaggio("Data preclusa aggiunta: " + giornoEscluso + " " + nomeMese);
			} else {
				view.mostraMessaggio("Attenzione: il giorno scelto è già indicato come precluso!");
			}
			continua = view.confermaInserisciDate(nomeMese);
		} while (continua);

		view.mostraMessaggio(calendario.toStringDatePrecluse(numeroMese));
	}

	public void gestisciChiusuraRaccolta() {
		if (statoSistemaController.getStatoCorrente() != StatoSistemaModel.RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3) {
			view.mostraMessaggio(
					"Non puoi chiudere la raccolta in questo stato.\nStato attuale: "
					);
			statoSistemaController.mostraStato(); // stampa lo stato attuale tramite la view
		} else {
			boolean chiudi = view.confermaChiusuraRaccolta();
			if (chiudi) {
				statoSistemaController.aggiornaStato(StatoSistemaModel.RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3_COMPLETATA);
				view.mostraMessaggio("Raccolta chiusa con successo.");
			}
		}
	}

	public void visualizzaVolontari() throws Exception {
		// Usa la view per la stampa di volontari con visite associate
		// (passa i model globali, NON creare oggetti nuovi)
		view.mostraMessaggio("--- ELENCO VOLONTARI CON VISITE ASSOCIATE ---");
		listaVolontarioView.stampaVolontariConVisite(listaVolontariModel, listaTipoVisitaModel);
	}

	public void visualizzaLuoghi() throws Exception {
		view.mostraMessaggio("--- ELENCO LUOGHI E TIPI VISITA ASSOCIATI ---");
		listaLuogoView.stampaLuoghiConTipoVisita(listaLuogoModel);
	}

	public void visualizzaTipoVisitaConLuoghi() throws Exception {
		view.mostraMessaggio("--- ELENCO TIPI VISITA ASSOCIATI AI LUOGHI ---");
		listaLuogoView.stampaLuoghiConTipoVisita(listaLuogoModel);
	}

	public void visualizzaStatoVisite() throws Exception {
		view.mostraMessaggio("--- ELENCO STATO VISITE E VOLONTARI ASSOCIATI ---");
		listaVisiteView.stampaTitoloEStato(listaVisiteModel);
	}

	public void generaPianoVisite() throws Exception {
		// 1. Controllo stato
		if (statoSistemaController.getStatoCorrente() != StatoSistemaModel.PRODUZIONE_VISITE_PROPONIBILI_I_1) {
			view.mostraMessaggio("Non puoi generare il piano visite in questo stato del sistema.");
			statoSistemaController.mostraStato();
			return;
		}

		view.mostraMessaggio("\n----- Generazione del piano delle visite per il mese " + calendarioModel.getMeseI1() + " -----\n");

		for (VolontarioModel volontario : listaVolontariModel.getVolontari()) {
			if (!volontario.isAttivo()) continue;

			ArrayList<DisponibilitaModel> disponibilitaVolontario = disponibilitaDAOModel.leggiDisponibilitaVolontario(volontario.getNickname());

			for (DisponibilitaModel disponibilita : disponibilitaVolontario) {
				if (disponibilita.getMese() != 1) continue;
				if (calendarioModel.isGiornoEscluso(disponibilita.getGiorno(), 1)) continue;

				ListaTipoVisitaModel tipiPerVolontario = listaTipoVisitaModel.getTipiPerVolontario(volontario.getNickname());
				for (TipoVisitaModel tipoVisita : tipiPerVolontario.getTipi()) {
					if (!calendarioModel.isGiornoProgrammabilePerTipoVisita(disponibilita.getGiorno(), tipoVisita)) continue;

					int numeroMese = calendarioModel.getMeseNumero(calendarioModel.getMeseI1());
					DataModel dataVisita = new DataModel(disponibilita.getGiorno(), numeroMese);

					// Check: volontario già assegnato lo stesso giorno?
					if (listaVisiteModel.volontarioGiaAssegnatoQuelGiorno(volontario, dataVisita)) continue;

					VisitaModel visitaEsistente = listaVisiteModel.trovaVisita(tipoVisita, dataVisita);

					if (visitaEsistente != null) {
						if (!visitaEsistente.getVolontariEffettivi().getVolontari().contains(volontario)) {
							visitaEsistente.getVolontariEffettivi().aggiungiVolontario(volontario);
							visitaDAOModel.collegaVolontarioAVisitaDB(visitaEsistente, volontario);
						}
					} else {
						if (listaVisiteModel.visitaSovrapposta(tipoVisita, dataVisita, listaLuogoModel)) {
							view.mostraMessaggio("Visita sovrapposta, NON la creo: " + tipoVisita.getTitolo() + " " + dataVisita);
							continue;
						}
						VisitaModel nuova = new VisitaModel(tipoVisita, volontario, StatoVisita.PROPOSTA, dataVisita);
						listaVisiteModel.aggiungiVisita(nuova);
						visitaDAOModel.salvaVisita(nuova, volontario);
					}
				}
			}
		}
		listaVisiteView.stampaPianoVisite(listaVisiteModel);

		statoSistemaController.aggiornaStato(StatoSistemaModel.PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA);
	}


	public void gestisciChiusuraIscrizioniFruitore() throws Exception {
	    if (statoSistemaController.getStatoCorrente() != StatoSistemaModel.RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1) {
	        view.mostraMessaggio("Non puoi chiudere la raccolta iscrizioni in questo stato.");
	        return;
	    }

	    if (view.confermaChiusuraIscrizioniFruitore()) {
	        // 1. CARICAMENTO REALE: Leggiamo dal DB e otteniamo un nuovo oggetto ListaIscrizioniModel
	        // Usiamo il metodo statico che abbiamo corretto prima
	        ListaIscrizioniModel iscrittiDalDb = IscrizioneDAOModel.leggiIscrizioniDaDB(
	            this.listaFruitoreModel, 
	            this.listaVisiteModel
	        );

	        // 2. ASSEGNAZIONE DIRETTA: Sostituiamo la vecchia lista (che era null o vuota) 
	        // con quella piena appena caricata dal DB
	        this.listaIscrizioniModel = iscrittiDalDb; 

	        // 3. AGGIORNAMENTO STATI: Ora this.listaIscrizioniModel non è più null!
	        listaVisiteModel.aggiornaStatoVisite(this.listaIscrizioniModel, visitaDAOModel);

	        statoSistemaController.aggiornaStato(StatoSistemaModel.RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3);
	        view.mostraMessaggio("Raccolta chiusa. Iscrizioni ricaricate e stati aggiornati con successo.");
	    }
	}

	public void aggiungiTipoVisitaALuogo() throws Exception {
		// 1. Selezione luogo attivo tramite view specializzata
		if (listaLuogoModel.getListaLuoghi().isEmpty()) {
			view.mostraMessaggio("Non ci sono luoghi disponibili.");
			return;
		}

		ArrayList<LuogoModel> luoghiAttivi = listaLuogoModel.getLuoghiAttivi();
		if (luoghiAttivi.isEmpty()) {
			view.mostraMessaggio("Nessun luogo attivo disponibile.");
			return;
		}
		view.mostraMessaggio("--- Selezione Luogo ---");
		listaLuogoView.stampaElencoNumerato(luoghiAttivi);
		int sceltaLuogo = view.scegliNumeroElementi("Scegli il luogo a cui aggiungere il tipo visita: ", luoghiAttivi.size());
		LuogoModel luogoScelto = luoghiAttivi.get(sceltaLuogo - 1);

		boolean continuaTipo = true;
		do {
			// 2. Tipi visita attivi non ancora associati a questo luogo
			ListaTipoVisitaModel tipiDisponibili = new ListaTipoVisitaModel();
			for (TipoVisitaModel tipo : listaTipoVisitaModel.getTipi()) {
				if (tipo.isAttivo() && !luogoScelto.getListaTipoVisita().contains(tipo)) {
					tipiDisponibili.add(tipo);
				}
			}
			if (tipiDisponibili.size() == 0) {
				view.mostraMessaggio("Tutti i tipi visita disponibili sono già associati a questo luogo.");
				return;
			}

			view.mostraMessaggio("--- Tipi Visita disponibili ---");
			view.stampaTipiVisitaDisponibili(tipiDisponibili);
			int sceltaTipo = view.scegliTipoVisita(tipiDisponibili);
			TipoVisitaModel tipoScelto = tipiDisponibili.getTipo(sceltaTipo - 1);

			// 3. Gestione volontari (solo attivi, non già associati a questo tipo visita)
			boolean continuaVolontari = true;
			do {
				ListaVolontariModel volontariDisponibili = new ListaVolontariModel();
				for (VolontarioModel v : listaVolontariModel.getVolontari()) {
					if (v.isAttivo() && !tipoScelto.getListaVolontari().contienePerNickname(v.getNickname())) {
						volontariDisponibili.aggiungiVolontario(v);
					}
				}

				VolontarioModel volontarioScelto = null;
				boolean nuovo = view.yesOrNo("Vuoi aggiungere un volontario non ancora registrato?");
				if (nuovo) {
					volontarioScelto = volontarioController.aggiungiNuovoVolontario();
				} else {
					if (volontariDisponibili.size() == 0) {
						view.mostraMessaggio("Tutti i volontari disponibili sono già associati al tipo visita selezionato.");
						break;
					}
					view.stampaVolontariDisponibili(volontariDisponibili);
					int sceltaVol = view.scegliVolontario(volontariDisponibili);
					volontarioScelto = volontariDisponibili.getVolontario(sceltaVol - 1);
				}

				if (volontarioScelto != null && volontarioScelto.isAttivo() &&
						!tipoScelto.getListaVolontari().contienePerNickname(volontarioScelto.getNickname())) {
					tipoScelto.getListaVolontari().aggiungiVolontario(volontarioScelto);
					tipoVisitaDAOModel.gestisciOrfani(tipoScelto, volontarioScelto); // Aggiorna su DB
					view.mostraMessaggio("Volontario \"" + volontarioScelto.getNickname() +
							"\" aggiunto a \"" + tipoScelto.getTitolo() + "\".");
				} else if (volontarioScelto != null && !volontarioScelto.isAttivo()) {
					view.mostraMessaggio("Il volontario appena creato NON è attivo, quindi non verrà associato.");
				}

				continuaVolontari = view.yesOrNo("Vuoi aggiungere un altro volontario a questo Tipo Visita?");
			} while (continuaVolontari);

			// 4. Aggiungi il tipo visita selezionato al luogo e salva modifiche
			luogoScelto.getListaTipoVisita().add(tipoScelto);
			luogoDAOModel.aggiungiTipoVisitaALuogo(luogoScelto, tipoScelto);

			continuaTipo = view.yesOrNo("Vuoi aggiungere un altro tipo visita a questo luogo?");
		} while (continuaTipo);

		view.mostraMessaggio("Il luogo è stato correttamente modificato.");
	}

	public void aggiungiVolontarioATipoVisita() throws Exception {
		// 1. Filtra i tipi visita attivi
		ListaTipoVisitaModel tipiAttivi = new ListaTipoVisitaModel();
		for (TipoVisitaModel tipo : listaTipoVisitaModel.getTipi()) {
			if (tipo.isAttivo()) tipiAttivi.add(tipo);
		}
		if (tipiAttivi.size() == 0) {
			view.mostraMessaggio("Non ci sono tipi di visita attivi disponibili.");
			return;
		}

		view.mostraMessaggio("--- Selezione Tipo Visita ---");
		view.stampaTipiVisitaDisponibili(tipiAttivi);
		int sceltaTipo = view.scegliTipoVisita(tipiAttivi);
		TipoVisitaModel tipoScelto = tipiAttivi.getTipo(sceltaTipo - 1);
		view.mostraMessaggio("Hai selezionato: " + tipoScelto.getTitolo());

		// 2. Volontari attivi, non già associati
		ListaVolontariModel volontariDisponibili = new ListaVolontariModel();
		for (VolontarioModel v : listaVolontariModel.getVolontari()) {
			if (v.isAttivo() && !tipoScelto.getListaVolontari().contienePerNickname(v.getNickname())) {
				volontariDisponibili.aggiungiVolontario(v);
			}
		}

		boolean continua;
		do {
			VolontarioModel v = null;
			boolean nuovo = view.yesOrNo("Vuoi aggiungere un volontario non ancora registrato?");
			if (nuovo) {
				v = volontarioController.aggiungiNuovoVolontario();
				if (v != null && v.isAttivo() && !tipoScelto.getListaVolontari().contienePerNickname(v.getNickname())) {
					tipoScelto.getListaVolontari().aggiungiVolontario(v);
					tipoVisitaDAOModel.gestisciOrfani(tipoScelto, v);
					view.mostraMessaggio("Volontario \"" + v.getNickname() + "\" aggiunto a \"" + tipoScelto.getTitolo() + "\".");
				} else if (v != null && !v.isAttivo()) {
					view.mostraMessaggio("Il volontario appena creato NON è attivo, quindi non verrà associato.");
				}
			} else {
				if (volontariDisponibili.size() == 0) {
					view.mostraMessaggio("Tutti i volontari disponibili sono già associati al tipo visita selezionato.");
					return;
				}
				view.stampaVolontariDisponibili(volontariDisponibili);
				int sceltaVol = view.scegliVolontario(volontariDisponibili);
				v = volontariDisponibili.getVolontario(sceltaVol - 1);
				if (v != null && v.isAttivo()) {
					tipoScelto.getListaVolontari().aggiungiVolontario(v);
					tipoVisitaDAOModel.gestisciOrfani(tipoScelto, v);
					view.mostraMessaggio("Volontario \"" + v.getNickname() + "\" aggiunto a \"" + tipoScelto.getTitolo() + "\".");
					volontariDisponibili.rimuoviVolontario(v);
				}
			}
			continua = view.yesOrNo("Vuoi aggiungerne un altro Volontario?");
		} while (continua);
	}

	public void rimuoviLuogo() throws Exception {
    view.mostraMessaggio("-----------------------------------------------------------------------------");
    view.mostraMessaggio("Avvio procedura di disattivazione luoghi.");
    view.mostraMessaggio("-----------------------------------------------------------------------------");

    boolean continuaLuogo;
    do {
        // 1. Recupera solo i luoghi che hanno isAttivo == true
        ArrayList<LuogoModel> luoghiAttivi = listaLuogoModel.getLuoghiAttivi();
        
        if (luoghiAttivi.isEmpty()) {
            view.mostraMessaggio("Nessun luogo attivo disponibile.");
            return;
        }

        listaLuogoView.stampaElencoNumerato(luoghiAttivi);
        int sceltaLuogo = view.scegliNumeroElementi("Scegli il luogo da disattivare: ", luoghiAttivi.size());
        LuogoModel luogoDaRimuovere = luoghiAttivi.get(sceltaLuogo - 1);

        boolean conferma = view.yesOrNo("Sei sicuro di voler disattivare il luogo '" + luogoDaRimuovere.getNomeLuogo() + "'?");
        
        if (conferma) {
            // Salviamo il riferimento ai tipi di visita prima di disattivare il luogo
            ListaTipoVisitaModel tipiDaControllare = luogoDaRimuovere.getListaTipoVisita();

            // STEP 1: Disattivazione Luogo (DB + Memoria)
            luogoDAOModel.eliminaLuogo(luogoDaRimuovere); // Imposta ISATTIVO = false sul DB
            luogoDaRimuovere.setAttivo(false);           // Aggiorna l'oggetto in memoria
            
            view.mostraMessaggio("Luogo '" + luogoDaRimuovere.getNomeLuogo() + "' disattivato.");

            // STEP 2: Disattivazione Tipi Visita Orfani (DB + Memoria)
            // Questo metodo controllerà se i tipi erano usati solo da questo luogo
            rimuoviTipiVisitaDaLuogo(luogoDaRimuovere, tipiDaControllare);
            
            // STEP 3: Disattivazione Volontari Orfani (DB + Memoria)
            // Questo metodo controllerà se i volontari sono rimasti senza visite attive
            gestisciVolontariOrfani();

            view.mostraMessaggio("Procedura a cascata terminata con successo.\n");
        } else {
            view.mostraMessaggio("Operazione annullata.\n");
        }

        // Verifichiamo se ci sono ancora luoghi per continuare il ciclo
        if (listaLuogoModel.getLuoghiAttivi().isEmpty()) {
            break;
        }
        
        continuaLuogo = view.yesOrNo("Vuoi rimuovere un altro Luogo?");
    } while (continuaLuogo);

    view.mostraMessaggio("\nProcedura completata.");
}

	public void rimuoviTipoVisitaDaLuogo() throws Exception {
		view.mostraMessaggio("--------------------------------------------------------------------------------------------");
		view.mostraMessaggio("Avvio procedura di rimozione tipo visita da un luogo. Verifica la scelta prima di procedere.");
		view.mostraMessaggio("--------------------------------------------------------------------------------------------");

		// 1. Seleziona luogo attivo
		ArrayList<LuogoModel> luoghiAttivi = listaLuogoModel.getLuoghiAttivi();
		if (luoghiAttivi.isEmpty()) {
			view.mostraMessaggio("Nessun luogo attivo disponibile.");
			return;
		}
		listaLuogoView.stampaElencoNumerato(luoghiAttivi);
		int sceltaLuogo = view.scegliNumeroElementi("Scegli il luogo da cui rimuovere il tipo visita: ", luoghiAttivi.size());
		LuogoModel luogoScelto = luoghiAttivi.get(sceltaLuogo - 1);

		// 2. Mostra solo tipi visita attivi associati a questo luogo
		ListaTipoVisitaModel tipiAttiviDelLuogo = new ListaTipoVisitaModel();
		for (TipoVisitaModel tv : luogoScelto.getListaTipoVisita().getTipi()) {
			if (tv.isAttivo()) {
				tipiAttiviDelLuogo.add(tv);
			}
		}

		if (tipiAttiviDelLuogo.size() == 0) {
			view.mostraMessaggio("Nessun tipo visita attivo associato al luogo.");
			return;
		}
		view.mostraMessaggio("Tipi visita associati al luogo '" + luogoScelto.getNomeLuogo() + "':");
		view.stampaTipiVisitaDisponibili(tipiAttiviDelLuogo);
		int sceltaTipo = view.scegliTipoVisita(tipiAttiviDelLuogo);
		TipoVisitaModel tipoVisitaDaRimuovere = tipiAttiviDelLuogo.getTipo(sceltaTipo - 1);
		String nomeTipo = tipoVisitaDaRimuovere.getTitolo();

		// 3. Conferma eliminazione
		boolean conferma = view.yesOrNo("Sei sicuro di voler rimuovere il tipo visita '" + nomeTipo +
				"' dal luogo '" + luogoScelto.getNomeLuogo() + "'? ");

		if (conferma) {
			tipoVisitaDaRimuovere.setAttivo(false); // in memoria
			tipoVisitaDAOModel.eliminaTipoVisita(tipoVisitaDaRimuovere);
			gestisciVolontariOrfani();
			gestisciLuoghiOrfani();
			view.mostraMessaggio("Tipo visita '" + nomeTipo + "' rimosso con successo!\n");
		} else {
			view.mostraMessaggio("Operazione annullata.\n");
		}

	}

	public void gestisciLuoghiOrfani() throws Exception {
		ListaLuogoModel luoghiOrfani = listaLuogoModel.trovaLuoghiOrfani();
		if (luoghiOrfani.getListaLuoghi().isEmpty()) {
			return;
		}
		view.mostraMessaggio("Trovati " + luoghiOrfani.getListaLuoghi().size() + " luoghi senza tipi visita:");
		for (LuogoModel l : luoghiOrfani.getListaLuoghi()) {
			view.mostraMessaggio("      - " + l.getNomeLuogo());
		}
		for (LuogoModel l : luoghiOrfani.getListaLuoghi()) {
			luogoDAOModel.eliminaLuogo(l);
			l.setAttivo(false); // Aggiorna anche in memoria
			view.mostraMessaggio("Eliminato luogo: " + l.getNomeLuogo());
		}
	}


	private void rimuoviTipiVisitaDaLuogo(LuogoModel luogo, ListaTipoVisitaModel tipiVisitaDaRimuovere) throws Exception {
    if (tipiVisitaDaRimuovere.size() == 0) {
        return;
    }

    for (TipoVisitaModel tipoVisita : tipiVisitaDaRimuovere.getTipi()) {
        boolean orfano = true;
        
        // Verifichiamo se altri luoghi ATTIVI usano ancora questo tipo di visita
        // Grazie al fatto che in rimuoviLuogo() abbiamo già fatto luogo.setAttivo(false),
        // getLuoghiAttivi() non restituirà più il luogo che stiamo cancellando.
        for (LuogoModel l : listaLuogoModel.getLuoghiAttivi()) {
            // Se un altro luogo attivo contiene questo tipo, allora NON è orfano
            if (l.getListaTipoVisita().contains(tipoVisita)) {
                orfano = false;
                break;
            }
        }

        if (orfano) {
            // 1. Disattivazione logica sul DB (UPDATE tipo_visita SET ISATTIVO = false)
            tipoVisitaDAOModel.eliminaTipoVisita(tipoVisita);
            
            // 2. Disattivazione logica in memoria
            tipoVisita.setAttivo(false);
            
            view.mostraMessaggio("Tipo Visita '" + tipoVisita.getTitolo() + "' disattivato perché non più associato ad alcun luogo attivo.");
            
            // Nota: NON rimuoviamo i legami dalle tabelle ponte nel DB perché usiamo isAttivo.
            // In questo modo, se riattivassimo il luogo, i legami sarebbero ancora lì.
        }
    }
    
    // STEP FINALE: Ora che i tipi visita sono stati eventualmente disattivati,
    // controlliamo chi tra i volontari è rimasto senza nulla da fare.
    gestisciVolontariOrfani();
}

	public void gestisciVolontariOrfani() throws Exception {
    ListaVolontariModel volontariOrfani = new ListaVolontariModel();

    // 1. Identifica i volontari che non hanno più ALCUNA associazione ATTIVA
    for (VolontarioModel v : listaVolontariModel.getVolontari()) {
        // Consideriamo solo i volontari attualmente attivi nel sistema
        if (v.isAttivo()) {
            boolean haAssociazioniAttive = false;

            for (TipoVisitaModel tv : listaTipoVisitaModel.getTipi()) {
                // Un'associazione è valida solo se il TipoVisita è attivo 
                // E il volontario è presente nella sua lista
                if (tv.isAttivo() && tv.getListaVolontari().contienePerNickname(v.getNickname())) {
                    haAssociazioniAttive = true;
                    break;
                }
            }

            // Se non ha trovato nemmeno un'associazione con un tipo visita attivo
            if (!haAssociazioniAttive) {
                volontariOrfani.aggiungiVolontario(v);
            }
        }
    }

    if (volontariOrfani.size() == 0) {
        return;
    }

    view.mostraMessaggio("Trovati " + volontariOrfani.size() + " volontari rimasti senza incarichi attivi:");
    
    // 2. Disattivazione logica a cascata (DB + Memoria)
    for (VolontarioModel v : volontariOrfani.getVolontari()) {
        // Aggiorna il DB: imposta ISATTIVO = 0 (o false)
        credenzialiDAOModel.disattivaDaCredenziali(v); 
        
        // Aggiorna la memoria
        v.setAttivo(false);
        
        view.mostraMessaggio("Disattivato volontario: " + v.getNickname());
    }
}

	public void rimuoviVolontario() throws Exception {
    view.mostraMessaggio("-------------------------------------------------------------------------------------");
    view.mostraMessaggio("Avvio procedura di disattivazione di un volontario.");
    view.mostraMessaggio("-------------------------------------------------------------------------------------");

    boolean continuaVolontario = false;
    do {
        // 1. Filtra solo volontari attivi (usa il tuo metodo se presente nel model)
        ArrayList<VolontarioModel> volontariAttivi = new ArrayList<>();
        for (VolontarioModel v : listaVolontariModel.getVolontari()) {
            if (v.isAttivo()) {
                volontariAttivi.add(v);
            }
        }

        if (volontariAttivi.isEmpty()) {
            view.mostraMessaggio("Non sono presenti volontari attivi da rimuovere.");
            break;
        }

        // 2. Stampa elenco
        view.mostraMessaggio("\nElenco volontari disponibili:");
        for (int i = 0; i < volontariAttivi.size(); i++) {
            view.mostraMessaggio((i + 1) + ") " + volontariAttivi.get(i).getNickname());
        }

        int sceltaVolontario = view.scegliNumeroElementi(
                "Inserisci il numero del volontario da disattivare: ",
                volontariAttivi.size()
        );

        VolontarioModel volontarioDaRimuovere = volontariAttivi.get(sceltaVolontario - 1);

        if (view.yesOrNo("Sei sicuro di voler disattivare il volontario '" + volontarioDaRimuovere.getNickname() + "'?")) {
            try {
                // STEP 1: Disattivazione Volontario (DB e Memoria)
                credenzialiDAOModel.disattivaDaCredenziali(volontarioDaRimuovere);
                volontarioDaRimuovere.setAttivo(false); 
                
                view.mostraMessaggio("Volontario '" + volontarioDaRimuovere.getNickname() + "' disattivato.");

                // STEP 2: Gestione cascata inversa (Tipi Visita rimasti senza volontari)
                // Se un tipo visita non ha più volontari attivi, va disattivato
                gestisciTipiVisitaOrfani();

                // STEP 3: Gestione cascata inversa (Luoghi rimasti senza tipi visita attivi)
                // Se un luogo non ha più tipi visita attivi, va disattivato
                gestisciLuoghiOrfani();

            } catch (SQLException e) {
                view.mostraMessaggio("Errore durante la disattivazione: " + e.getMessage());
            }
        } else {
            view.mostraMessaggio("Operazione annullata.");
        }

        continuaVolontario = view.yesOrNo("Vuoi disattivare un altro volontario?");
    } while (continuaVolontario);

    view.mostraMessaggio("\nProcedura completata.");
}

	private void gestisciTipiVisitaOrfani() throws SQLException {
		// Trova i tipi visita senza volontari attivi
		ListaTipoVisitaModel tipiOrfani = listaTipoVisitaModel.trovaTipiVisitaOrfani();

		if (tipiOrfani.getTipi().isEmpty()) {
			return;
		}

		view.mostraMessaggio("Trovati " + tipiOrfani.getTipi().size() + " tipi visita senza volontari attivi:");
		for (TipoVisitaModel tv : tipiOrfani.getTipi()) {
			view.mostraMessaggio("      - " + tv.getTitolo());
		}
		// Elimina realmente i tipi visita orfani
		for (TipoVisitaModel tv : tipiOrfani.getTipi()) {
			tipoVisitaDAOModel.eliminaTipoVisita(tv);   // Soft delete su DB
			tv.setAttivo(false);                        // Soft delete in memoria
			view.mostraMessaggio("Eliminato tipo visita: " + tv.getTitolo());
		}
	}



	// --- Stub per le funzioni, da strutturare ---


}
