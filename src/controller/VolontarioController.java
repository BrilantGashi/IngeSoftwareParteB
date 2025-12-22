package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import model.*;
import model.dao.CredenzialiDAOModel;
import model.dao.DisponibilitaDAOModel;
import view.VolontarioView;

public class VolontarioController {
	private VolontarioModel model;
	private VolontarioView view;
	// Dipendenze globali condivise
	private ListaTipoVisitaModel listaTipoVisitaModel;
	private ListaVolontariModel listaVolontariModel;
	private StatoSistemaController statoSistemaController;
	private DisponibilitaDAOModel disponibilitaDAOModel;
	private ListaVisiteModel listaVisiteModel;
	private ListaIscrizioniModel listaIscrizioniModel;
	private CalendarioModel calendarioModel;
	// ... altri model/DAO globali se servono
	public VolontarioController(
			VolontarioModel model,
			VolontarioView view,
			ListaTipoVisitaModel listaTipoVisitaModel,
			ListaVolontariModel listaVolontariModel,
			StatoSistemaController statoSistemaController,
			DisponibilitaDAOModel disponibilitaDAOModel,
			ListaVisiteModel listaVisiteModel,
			ListaIscrizioniModel listaIscrizioniModel,
			CalendarioModel calendarioModel
			// ... altri se servono
			) {
		this.model = model;
		this.view = view;
		this.listaTipoVisitaModel = listaTipoVisitaModel;
		this.listaVolontariModel = listaVolontariModel;
		this.statoSistemaController = statoSistemaController;
		this.disponibilitaDAOModel = disponibilitaDAOModel;
		this.listaVisiteModel = listaVisiteModel;
		this.listaIscrizioniModel = listaIscrizioniModel;
		this.calendarioModel = calendarioModel;
		// ... altro se serve
	}

	// Se devi impostare il model dopo login:
	public void setModel(VolontarioModel model) {
		this.model = model;
	}


	public void mostraVolontario() {
		view.mostraVolontario(model);
	}

	public void aggiornaNickname(String nuovoNickname) {
		model.setNickname(nuovoNickname);
		view.mostraMessaggio("Nickname aggiornato.");
	}

	public void setAttivo(boolean attivo) {
		model.setAttivo(attivo);
		view.mostraMessaggio(attivo ? "Volontario esistente." : "Volontario non esistente.");
	}

	public VolontarioModel aggiungiNuovoVolontario() {
    String nickname;
    do {
        nickname = view.leggiNicknameVolontario();
        if (listaVolontariModel.contienePerNickname(nickname)) {
            view.mostraMessaggio("Nickname già presente! Inseriscine uno diverso.");
            nickname = null;
        }
    } while (nickname == null);

    // Chiedi la password
    String password = view.leggiPasswordVolontario();

    // 1. Crea le credenziali e il modello volontario
    CredenzialiModel credenziali = new CredenzialiModel("VOLONTARIO", nickname, password);
    VolontarioModel nuovoVol = new VolontarioModel(credenziali);
    nuovoVol.setAttivo(true);

    try {
        // 2. TENTA IL SALVATAGGIO SUL DATABASE
        // Uso il metodo che abbiamo definito nel CredenzialiDAOModel
        CredenzialiDAOModel.salvaNuovoVolontario(nuovoVol);

        // 3. SE IL DB NON LANCIA ECCEZIONI, AGGIUNGI ALLA LISTA IN MEMORIA
        listaVolontariModel.aggiungiVolontario(nuovoVol);
        
        view.mostraMessaggio("Volontario " + nickname + " creato e salvato correttamente nel database.");
        return nuovoVol;

    } catch (java.sql.SQLException e) {
        // Se il database fallisce (es. errore di connessione o vincoli), informiamo l'utente
        view.mostraMessaggio("ERRORE CRITICO: Impossibile salvare il volontario nel database. " + e.getMessage());
        return null; 
    }
}

	public void menuVolontario(String nickname) throws Exception {
		boolean indietro = false;
		do {
			int scelta = view.scegliVoceMenuVolontario();
			StatoSistemaModel statoCorrente = statoSistemaController.getStatoCorrente();

			switch (scelta) {
			case 0:
				indietro = view.chiediConfermaIndietro();
				break;

			case 1: // Visualizza tipi visita associati
			ListaTipoVisitaModel tipiVisitaPerVolontario = listaTipoVisitaModel.getTipiPerVolontario(nickname);
			view.mostraMessaggio("Elenco tipi visita associati al volontario: " + nickname);
			view.stampaTipiVisitaDisponibili(tipiVisitaPerVolontario);
			break;

			case 2: // Esprimi disponibilità per il mese
				if (statoCorrente != StatoSistemaModel.RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3) {
					view.mostraMessaggio("Non puoi esprimere disponibilità in questo stato del sistema.");
					view.mostraStato(statoCorrente);
				} else {
					raccogliDisponibilitaVolontario(nickname); // metodo da implementare se non esiste
				}
				break;

			case 3: // Visualizza visite confermate
				stampaVisiteConfermateVolontario(nickname, listaIscrizioniModel, listaVisiteModel);
				break;
			}
		} while (!indietro);
	}

	public void raccogliDisponibilitaVolontario(String nickname) throws Exception {
		int meseDisponibilita = calendarioModel.getMeseNumero(calendarioModel.getMeseI2());
		boolean continua;
		ArrayList<DisponibilitaModel> disponibilitaInserite = new ArrayList<>();

		do {
			// Chiedi la data tramite view
			int giornoScelto = view.leggiGiornoDisponibilita(meseDisponibilita, calendarioModel.getMeseI2());

			// Verifica che la data NON sia esclusa
			boolean dataValida = !calendarioModel.isGiornoEscluso(giornoScelto, meseDisponibilita);

			// Verifica che non sia già inserita
			boolean giaPresente = false;
			for (DisponibilitaModel d : disponibilitaInserite) {
				if (d.getGiorno() == giornoScelto && d.getMese() == meseDisponibilita) {
					giaPresente = true;
					view.mostraMessaggio("Disponibilità già inserita precedentemente");
					break;
				}
			}

			if (dataValida && !giaPresente) {
				DisponibilitaModel nuova = new DisponibilitaModel(nickname, giornoScelto, meseDisponibilita);
				try {
					disponibilitaDAOModel.salvaDisponibilita(nuova);
					disponibilitaInserite.add(nuova);
				} catch (SQLException e) {
					view.mostraMessaggio("Errore salvataggio disponibilità: " + e.getMessage());
				}
			} else {
				view.mostraMessaggio("Data non valida o preclusa. Riprova.");
			}

			continua = view.yesOrNo("Vuoi esprimere un'altra disponibilità per il mese di " + calendarioModel.getMeseI2() + "?");
		} while (continua);

		// Riepilogo finale
		try {
			ArrayList<DisponibilitaModel> mie = disponibilitaDAOModel.leggiDisponibilitaVolontario(nickname);
			view.mostraMessaggio("----- Le tue disponibilità per " + calendarioModel.getMeseI2() + " -----");
			for (DisponibilitaModel d : mie) {
				if (d.getMese() == meseDisponibilita) {
					view.mostraMessaggio(" - " + d.getGiorno() + "/" + calendarioModel.getMeseI2());
				}
			}
			view.mostraMessaggio("---------------------------------------------");
		} catch (SQLException e) {
			view.mostraMessaggio("Errore lettura disponibilità: " + e.getMessage());
		}
	}

	public void stampaVisiteConfermateVolontario(String username, ListaIscrizioniModel listaIscrizioniModel, ListaVisiteModel listaVisiteModel) {
		System.out.println("Visite confermate: \n");
		boolean trovato = false;
		for (VisitaModel visita : listaVisiteModel.getListaVisite()) { // campo / getter
			if (visita.getStatoVisita() == StatoVisita.CONFERMATA) {
				for (VolontarioModel v : visita.getVolontariEffettivi().getVolontari()) {
					if (v.getNickname().equals(username)) {
						TipoVisitaModel tipo = visita.getTipoVisita();
						System.out.println("-------------------------");
						System.out.println("Titolo: " + tipo.getTitolo());
						System.out.println("Descrizione: " + tipo.getDescrizione());
						System.out.println("Punto di incontro: " + tipo.getPuntoDiIncontro());
						System.out.println("Data: " + visita.getDataVisita().toString());
						System.out.println("Ora di inizio: " + tipo.getOraInizio());
						String bigliettoMsg = tipo.isBiglietto() ? "Sì, necessario all'ingresso" : "No";
						System.out.println("Necessità biglietto: " + bigliettoMsg);

						// Elenco codici prenotazione
						System.out.println("Elenco codici prenotazione e partecipanti:");
						boolean codiciTrovati = false;
						for (IscrizioneModel iscr : listaIscrizioniModel.getIscrizioni()) {
							if (iscr.getVisitaEffettiva().equals(visita)) {
								System.out.println("  Codice: " + iscr.getCodicePrenotazione()
								+ " | Partecipanti: " + iscr.getTotalePartecipanti());
								codiciTrovati = true;
							}
						}
						if (!codiciTrovati) {
							System.out.println("  Nessuna iscrizione associata a questa visita.");
						}
						System.out.println("-------------------------");
						trovato = true;
					}
				}
			}
		}
		if (!trovato) {
			System.out.println("Nessuna visita confermata associata a questo volontario!");
		}
	}


}
