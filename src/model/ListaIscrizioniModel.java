package model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import model.dao.IscrizioneDAOModel;
import model.dao.VisitaDAOModel;

public class ListaIscrizioniModel {
	private ArrayList<IscrizioneModel> iscrizioni = new ArrayList<>();

	public void aggiungi(IscrizioneModel iscrizione) {
		Objects.requireNonNull(iscrizione);
		if (trovaPerFruitoreEVisita(iscrizione.getFruitore(), iscrizione.getVisitaEffettiva()) != null) {
			throw new IllegalStateException("Esiste già un'iscrizione per questo fruitore a questa visita");
		}
		iscrizioni.add(iscrizione);x
	}

	// MODEL: ListaIscrizioniModel
	public boolean disdiciIscrizione(
			String codicePrenotazione,
			String usernameFruitore,
			IscrizioneDAOModel iscrizioneDAOModel,
			VisitaDAOModel visitaDAOModel
			) {
		if (codicePrenotazione == null || codicePrenotazione.isBlank()
				|| usernameFruitore == null || usernameFruitore.isBlank()) {
			return false;
		}

		IscrizioneModel iscrizione = trovaPerCodicePrenotazione(codicePrenotazione);
		if (iscrizione == null) return false;

		// Verifica che la richiesta venga dal fruitore giusto
		String owner = null;
		if (iscrizione.getFruitore() != null && iscrizione.getFruitore().getCredenziali() != null) {
			owner = iscrizione.getFruitore().getCredenziali().getUsername();
		}
		if (!usernameFruitore.equals(owner)) return false;

		VisitaModel visita = iscrizione.getVisitaEffettiva();
		StatoVisita stato = (visita != null) ? visita.getStatoVisita() : null;
		// Permetti disdetta solo se la visita è proposta o completa
		if (!(stato == StatoVisita.PROPOSTA || stato == StatoVisita.COMPLETA)) return false;
		try {
			// 1. Cancella su DB
			boolean removedDb = iscrizioneDAOModel.disdiciIscrizione(codicePrenotazione);
			if (!removedDb) return false;

			// 2. Rimuovi dalla struttura in memoria
			iscrizioni.remove(iscrizione);

			// 3. Se era COMPLETA, verifica capienza e aggiorna stato su DB e in memoria
			if (stato == StatoVisita.COMPLETA && visita != null && visita.getTipoVisita() != null) {
				int capienza = visita.getTipoVisita().getSogliaMaxPartecipanti();
				int visitaId = iscrizioneDAOModel.trovaIdVisitaPerCodicePrenotazione(codicePrenotazione);

				if (visitaId != 0) {
					int totaleDb = iscrizioneDAOModel.countIscrittiPerVisita(visitaId);
					if (totaleDb < capienza) {
						visitaDAOModel.aggiornaStatoVisitaDB(visita, StatoVisita.PROPOSTA);
						visita.setStatoVisita(StatoVisita.PROPOSTA);
					}
				}
			}
			assert !iscrizioni.contains(iscrizione) : "Postcondizione violata in disdiciIscrizione";
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public ArrayList<IscrizioneModel> getIscrizioni() {
		return new ArrayList<>(iscrizioni);
	}

	public int getTotaleIscrittiPerVisita(VisitaModel visitaCercata) {
	    int totale = 0;
	    for (IscrizioneModel i : iscrizioni) {
	        VisitaModel vEffettiva = i.getVisitaEffettiva();
	        
	        // Confronto basato sui dati e non sulla memoria
	        if (vEffettiva != null && 
	            vEffettiva.getTipoVisita().getTitolo().equals(visitaCercata.getTipoVisita().getTitolo()) &&
	            vEffettiva.getDataVisita().toString().equals(visitaCercata.getDataVisita().toString())) {
	            
	            // Sommiamo il totale dei partecipanti (se un fruitore ha prenotato per 3 persone, contano 3)
	            totale += i.getTotalePartecipanti();
	        }
	    }
	    return totale;
	}

	public boolean rimuoviPerCodicePrenotazione(String codice) {
		IscrizioneModel iscrizione = trovaPerCodicePrenotazione(codice);
		if (iscrizione != null) {
			return iscrizioni.remove(iscrizione);
		}
		return false;
	}

	public IscrizioneModel trovaPerCodicePrenotazione(String codice) {
		for (IscrizioneModel i : iscrizioni) {
			if (i.getCodicePrenotazione().trim().equalsIgnoreCase(codice.trim())) {
				return i;
			}
		}
		return null;
	}


	public IscrizioneModel trovaPerFruitoreEVisita(FruitoreModel fruitore, VisitaModel visita) {
		for (IscrizioneModel i : iscrizioni) {
			if (i.getFruitore().equals(fruitore) && i.getVisitaEffettiva().equals(visita)) {
				return i;
			}
		}
		return null;
	}

	public void visualizzaIscrizioniFruitore(String nickname) {
		Set<StatoVisita> statiAmmessi = EnumSet.of(
				StatoVisita.PROPOSTA,
				StatoVisita.CONFERMATA,
				StatoVisita.CANCELLATA
				);
		boolean trovata = false;
		for (IscrizioneModel isc : this.iscrizioni) {
			VisitaModel v = isc.getVisitaEffettiva();
			if (v == null || !statiAmmessi.contains(v.getStatoVisita())) continue;

			String userIscrizione = isc.getFruitore().getCredenziali().getUsername();
			if (!userIscrizione.equals(nickname)) continue;

			StatoVisita stato = v.getStatoVisita();
			String titolo = (v.getTipoVisita() != null) ? v.getTipoVisita().getTitolo() : "Senza titolo";
			DataModel data = v.getDataVisita();
			String dataStr = (data != null) ? data.toString() : "Data N/D";

			if (stato == StatoVisita.PROPOSTA || stato == StatoVisita.CONFERMATA) {
				String descrizione = (v.getTipoVisita() != null) ? v.getTipoVisita().getDescrizione() : "N/D";
				String puntoIncontro = (v.getTipoVisita() != null) ? v.getTipoVisita().getPuntoDiIncontro() : "N/D";
				String oraInizio = (v.getTipoVisita() != null && v.getTipoVisita().getOraInizio() != null)
						? v.getTipoVisita().getOraInizio().toString() : "Orario N/D";
				boolean bigliettoRichiesto = (v.getTipoVisita() != null) && v.getTipoVisita().isBiglietto();

				System.out.println("- " + titolo +
						"\n  Descrizione: " + descrizione +
						"\n  Punto di incontro: " + puntoIncontro +
						"\n  Data: " + dataStr +
						"\n  Ora inizio: " + oraInizio +
						"\n  Biglietto da acquistare: " + (bigliettoRichiesto ? "SI" : "NO") +
						"\n  Stato: " + stato.name() +
						"\n  Codice: " + isc.getCodicePrenotazione());
			} else if (stato == StatoVisita.CANCELLATA) {
				System.out.println("- " + titolo +
						"\n  Data del mancato svolgimento: " + dataStr +
						"\n  Stato: " + stato.name() +
						"\n  Codice: " + isc.getCodicePrenotazione());
			}
			trovata = true;
		}
		if (!trovata) {
			System.out.println("Nessuna tua iscrizione trovata su visite proposte, confermate o cancellate.");
		}
	}
}
