package view;

import model.DataModel;
import model.IscrizioneModel;
import model.ListaVisiteModel;
import model.StatoVisita;
import model.TipoVisitaModel;
import model.VisitaModel;

import java.util.ArrayList;
import java.util.List;

public class ListaIscrizioniView {
	public void mostraIscrizioni(List<IscrizioneModel> iscrizioni) {
		if (iscrizioni == null || iscrizioni.isEmpty()) {
			System.out.println("Nessuna iscrizione trovata.");
		} else {
			for (IscrizioneModel i : iscrizioni) {
				System.out.println(i.toString());
			}
		}
	}

	public void mostraMessaggio(String msg) {
		System.out.println(msg);
	}

	public void stampaVisitePerFruitore(ListaVisiteModel listaVisiteModel) {
		ArrayList<VisitaModel> listaVisite = listaVisiteModel.getListaVisite();
		if (listaVisite == null || listaVisite.isEmpty()) {
			System.out.println("Non ci sono visite registrate al momento.");
			return;
		}
		for (VisitaModel visita : listaVisite) {
			StatoVisita stato = visita.getStatoVisita();
			TipoVisitaModel tipo = visita.getTipoVisita();
			DataModel data = visita.getDataVisita();

			// Mostra solo PROPOSTA, CONFERMATA, CANCELLATA
			if (stato == StatoVisita.PROPOSTA || stato == StatoVisita.CONFERMATA) {
				String titolo = (tipo != null) ? tipo.getTitolo() : "Senza titolo";
				String descrizione = (tipo != null) ? tipo.getDescrizione() : "N/D";
				String puntoIncontro = (tipo != null) ? tipo.getPuntoDiIncontro() : "N/D";
				String oraInizio = (tipo != null && tipo.getOraInizio() != null) ? tipo.getOraInizio().toString()
						: "Orario N/D";
				boolean bigliettoRichiesto = (tipo != null) && tipo.isBiglietto();
				String dataStr = (data != null) ? data.toString() : "Data N/D";

				System.out.println("- " + titolo + "\n  Descrizione: " + descrizione + "\n  Punto di incontro: "
						+ puntoIncontro + "\n  Data: " + dataStr + "\n  Ora inizio: " + oraInizio
						+ "\n  Biglietto da acquistare: " + (bigliettoRichiesto ? "SI" : "NO") + "\n  Stato: "
						+ stato.name());
			} else if (stato == StatoVisita.CANCELLATA) {
				String titolo = (tipo != null) ? tipo.getTitolo() : "Senza titolo";
				String dataStr = (data != null) ? data.toString() : "Data N/D";
				System.out.println(
						"- " + titolo + "\n  Data del mancato svolgimento: " + dataStr + "\n  Stato: " + stato.name());
			}
		}
	}

}
