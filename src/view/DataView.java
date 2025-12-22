package view;

import it.unibs.fp.mylib.InputDati;
import model.DataModel;

public class DataView {

	public DataModel leggiData(int mese) {
		if (mese < 1 || mese > 12) {
			throw new IllegalArgumentException("Mese non valido: " + mese);
		}
		int giorniNelMese = new DataModel().giorniDelMese(mese);
		int giornoScelto = InputDati.leggiIntero("Scegli il giorno (1-" + giorniNelMese + "): ", 1, giorniNelMese);
		return new DataModel(giornoScelto, mese);
	}

	public DataModel leggiDataDaUtente(String promptGiorno, String promptMese) {
		int giorno = InputDati.leggiIntero(promptGiorno, 1, 31);
		int mese = InputDati.leggiIntero(promptMese, 1, 12);
		// oppure inverti l'ordine se preferisci prima il mese
		return new DataModel(giorno, mese);
	}

	public void mostraMessaggioErrore(String errore) {
		System.out.println("Valori data non validi: " + errore + ". Riprova.");
	}

	public void mostraData(DataModel data) {
		System.out.println("Data: " + data.toString());
	}

	public void mostraMessaggio(String msg) {
		System.out.println(msg);
	}
}
