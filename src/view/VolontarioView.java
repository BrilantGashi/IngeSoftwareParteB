package view;

import java.util.ArrayList;

import it.unibs.fp.mylib.InputDati;
import it.unibs.fp.mylib.MyMenu;
import model.CalendarioModel;
import model.ListaTipoVisitaModel;
import model.StatoSistemaModel;
import model.TipoVisitaModel;
import model.VolontarioModel;

public class VolontarioView {
	private CalendarioModel calendarioModel;

	public  VolontarioView (CalendarioModel calendarioModel) {
		this.calendarioModel = calendarioModel;
	}

	public void mostraVolontario(VolontarioModel volontario) {
		System.out.println(volontario.toString());
	}

	public void mostraMessaggio(String msg) {
		System.out.println(msg);
	}

	public String leggiNicknameVolontario() {
		return InputDati.leggiStringaNonVuota("Inserisci il nickname del volontario: ");
	}

	public String leggiPasswordVolontario() {
		return InputDati.leggiStringaNonVuota("Inserisci la password del volontario: ");
	}

	private static final String[] vociVolontario = {
			"Visualizza miei tipi di visita associati",
			"Esprimi la disponibilita' per il mese",
			"Visualizza le mie visite confermate"
	};

	public int scegliVoceMenuVolontario() {
		MyMenu menu = new MyMenu("Menu volontario", vociVolontario);
		return menu.scegli();
	}

	public boolean chiediConfermaIndietro() {
		return InputDati.yesOrNo("Vuoi veramente tornare indietro?");
	}

	public void stampaTipiVisitaDisponibili(ListaTipoVisitaModel tipiDisponibili) {
		System.out.println("----- ELENCO TIPI DI VISITA ASSOCIATI -----");
		ArrayList<TipoVisitaModel> lista = tipiDisponibili.getTipi();    	    

		int i = 1;
		boolean almenoUnoStampato = false;
		for (TipoVisitaModel tv : lista) {
			if (tv.isAttivo()) {
				System.out.println(i++ + ") " + tv.getTitolo());
				System.out.println("   Descrizione: " + tv.getDescrizione());    	            // Altre info essenziali:
				System.out.println("   Giorni programmabili: " + (tv.getGiorniSettimanaProgrammabile() != null ? tv.getGiorniSettimanaProgrammabile() : "N/D"));
				System.out.println("   Ora inizio: " + (tv.getOraInizio() != null ? tv.getOraInizio().toString() : "N/D"));
				// ... altro
				System.out.println("---------------------------------");
				almenoUnoStampato = true;    	        }
		}
		if (!almenoUnoStampato) {
			System.out.println("Nessun tipo di visita attivo disponibile.");
		}
	}
	public void mostraStato(StatoSistemaModel stato) {
		System.out.println(stato.stampaStatoSistema());
	}

	public boolean yesOrNo(String domanda) {
		return InputDati.yesOrNo(domanda);
	}

	public int leggiGiornoDisponibilita(int mese, String nomeMese) {
		if (mese < 1 || mese > 12) {
			throw new IllegalArgumentException("Mese non valido: " + mese);
		}
		// Puoi richiamare un metodo del tuo calendarioModel per ottenere i giorni
		int giorniNelMese = calendarioModel.giorniDelMese(mese);
		return InputDati.leggiIntero("Scegli il giorno di disponibilità (1-" + giorniNelMese + ") per " + nomeMese + ": ", 1, giorniNelMese);
	}





}
