package controller;
import model.*;
import model.dao.IscrizioneDAOModel;
import model.dao.VisitaDAOModel;
import view.FruitoreView;
import view.ListaVisiteView;


public class FruitoreController {
	private FruitoreModel model;
	private FruitoreView view;
	// Dipendenze globali
	private ListaFruitoreModel listaFruitoreModel;
	private ListaVisiteModel listaVisiteModel;
	private StatoSistemaController statoSistemaController;
	private ListaVisiteView listaVisiteView;
	private ListaIscrizioniModel listaIscrizioniModel;
	private CalendarioModel calendarioModel;
	private IscrizioneDAOModel iscrizioneDAOModel;
	private VisitaDAOModel visitaDAOModel;
	private ListaVisiteController listaVisiteController;
	// ... altri se ti servono
	
	public FruitoreController(
			FruitoreModel model,
			FruitoreView view,
			ListaFruitoreModel listaFruitoreModel,
			ListaVisiteModel listaVisiteModel,
			StatoSistemaController statoSistemaController,
			ListaVisiteView listaVisiteView,
			ListaIscrizioniModel listaIscrizioniModel,
			CalendarioModel calendarioModel,
			IscrizioneDAOModel iscrizioneDAOModel,
			VisitaDAOModel visitaDAOModel,
			ListaVisiteController listaVisiteController
			) {
		this.model = model;
		this.view = view;
		this.listaFruitoreModel = listaFruitoreModel;
		this.listaVisiteModel = listaVisiteModel;
		this.statoSistemaController = statoSistemaController;
		this.listaVisiteView = listaVisiteView;
		this.listaIscrizioniModel = listaIscrizioniModel;
		this.calendarioModel = calendarioModel;
		this.iscrizioneDAOModel = iscrizioneDAOModel;
		this.visitaDAOModel = visitaDAOModel;
		this.listaVisiteController = listaVisiteController;
		// ... altro se serve
	}

	public void setModel(FruitoreModel model) { this.model = model; }

	public void mostraFruitore() {
		view.mostraFruitore(model);
	}

	public void menuFruitore(String nickname) throws Exception {
		boolean indietro = false;
		do {
			int scelta = view.scegliVoceMenuFruitore();
			StatoSistemaModel statoCorrente = statoSistemaController.getStatoCorrente();

			switch (scelta) {
			case 0:
				indietro = view.chiediConfermaIndietro();
				break;

			case 1: // Visualizza tutte le visite proposte/confermate/cancellate
				listaVisiteView.stampaVisitePerFruitore(listaVisiteModel);
				break;

			case 2: // Iscriviti a una visita proposta
				if (statoCorrente != StatoSistemaModel.GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE_COMPLETATA &&
				statoCorrente != StatoSistemaModel.RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1) {
					view.mostraMessaggio("Non puoi effettuare iscrizioni in questo stato del sistema.");
					view.mostraStato(statoCorrente);
				} else {
					aggiungiIscrizioneFruitore(nickname);
				}
				break;

			case 3: // Visualizza le proprie iscrizioni e il loro stato
				listaIscrizioniModel.visualizzaIscrizioniFruitore(nickname);
				break;

			case 4: // Cancella iscrizione
				if (statoCorrente != StatoSistemaModel.RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1) {
					view.mostraMessaggio("Non puoi cancellare iscrizioni in questo stato del sistema.");
					view.mostraStato(statoCorrente);
				} else {
					disdiciIscrizione(nickname);
				}
				break;
			}

		} while (!indietro);
	}
	
	
	public void aggiungiIscrizioneFruitore(String nickname) throws Exception {
    FruitoreModel fruitore = listaFruitoreModel.cercaFruitore(nickname);
    if (fruitore == null) {
        view.mostraMessaggio("Fruitore non trovato, registralo prima!");
        return;
    }

    VisitaModel visitaEffettiva = listaVisiteController.selezionaVisitaProposta(
            calendarioModel.getMeseI1(),
            fruitore,
            listaIscrizioniModel
        );

    if (visitaEffettiva == null) {
        view.mostraMessaggio("Nessuna visita selezionata.");
        return;
    }

    // --- AGGIUNTA: Controllo stato visita ---
    if (visitaEffettiva.getStatoVisita() == StatoVisita.COMPLETA) {
        view.mostraMessaggio("Spiacenti, la visita è già completa.");
        return;
    }

    // --- ATTENZIONE: Questo controllo funziona solo se hai fatto l'override di equals in VisitaModel! ---
    IscrizioneModel giaPresente = listaIscrizioniModel.trovaPerFruitoreEVisita(fruitore, visitaEffettiva);
    if (giaPresente != null) {
        view.mostraMessaggio("Sei già iscritto a questa visita (Codice: " + giaPresente.getCodicePrenotazione() + ").");
        return;
    }

    view.mostraMessaggio("Visita selezionata: " + visitaEffettiva.getTipoVisita().getTitolo() +
            " del " + visitaEffettiva.getDataVisita().toString());
    
    int nMaxPersonePerIscrizione = ParametriSistema.getInstance().getNMaxAccompagnatori();
    int quotaRichiesta = view.leggiNumeroAccompagnatori("Quanti partecipanti totali (te compreso)? Max " + nMaxPersonePerIscrizione + ":", nMaxPersonePerIscrizione);

    // --- Controllo capienza REALE ---
    int capienzaMassima = visitaEffettiva.getTipoVisita().getSogliaMaxPartecipanti();
    int totaleAttuale = listaIscrizioniModel.getTotaleIscrittiPerVisita(visitaEffettiva);
    
    if ((totaleAttuale + quotaRichiesta) > capienzaMassima) {
        int postiLiberi = capienzaMassima - totaleAttuale;
        view.mostraMessaggio("Iscrizione rifiutata: Posti disponibili insufficienti (" + postiLiberi + ").");
        return;
    }

    // Registra l'iscrizione
    IscrizioneModel nuovaIscrizione = new IscrizioneModel(visitaEffettiva, fruitore, quotaRichiesta);
    
    // Aggiorna DB e Memoria
    iscrizioneDAOModel.inserisciIscrizione(nuovaIscrizione);
    listaIscrizioniModel.aggiungi(nuovaIscrizione);

    // Aggiorna lo stato se necessario
    int nuovoTotale = totaleAttuale + quotaRichiesta;
    if (nuovoTotale >= capienzaMassima) {
        visitaEffettiva.setStatoVisita(StatoVisita.COMPLETA);
        visitaDAOModel.aggiornaStatoVisitaDB(visitaEffettiva, StatoVisita.COMPLETA);
        view.mostraMessaggio("La visita è ora COMPLETA.");
    }
    
    view.mostraMessaggio("Iscrizione registrata con successo! Codice: " + nuovaIscrizione.getCodicePrenotazione());
}
	
	public void disdiciIscrizione(String usernameFruitore) {
	    listaIscrizioniModel.visualizzaIscrizioniFruitore(usernameFruitore);
	    String codicePrenotazione;
	    do {
	        codicePrenotazione = view.chiediCodicePrenotazione();
	        if (codicePrenotazione.isEmpty()) {
	            view.mostraMessaggio("Operazione annullata.");
	            return;
	        }
	    } while (codicePrenotazione.isEmpty());

	    boolean successo = listaIscrizioniModel.disdiciIscrizione(
	        codicePrenotazione, usernameFruitore, iscrizioneDAOModel, visitaDAOModel);

	    if (successo) {
	        view.mostraMessaggio("Disdetta eseguita con successo!");
	    } else {
	        view.mostraMessaggio("Nessuna iscrizione disdetta: controlla il codice e che la visita sia disdittabile.");
	    }
	}





}
