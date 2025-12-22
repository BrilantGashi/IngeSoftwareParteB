package view;

import java.util.Scanner;

import it.unibs.fp.mylib.InputDati;
import it.unibs.fp.mylib.MyMenu;
import model.FruitoreModel;
import model.StatoSistemaModel;

public class FruitoreView {
	public void mostraFruitore(FruitoreModel fruitore) {
		System.out.println("Fruitore: " + fruitore.getCredenziali().getUsername());
		System.out.println("Utenza: " + fruitore.getCredenziali().getUtenza());
	}

	private static final String[] vociFruitore = { "Visualizza tutte le visite proposte/confermate/cancellate",
			"Iscriviti a una visita proposta", "Visualizza le tue iscrizioni e il loro stato",
			"Cancella una tua iscrizione a visita proposta/confermata" };

	public int scegliVoceMenuFruitore() {
		MyMenu menu = new MyMenu("Menu fruitore", vociFruitore);
		return menu.scegli();
	}

	public boolean chiediConfermaIndietro() {
		return InputDati.yesOrNo("Vuoi veramente tornare indietro?");
	}

	public void mostraStato(StatoSistemaModel stato) {
		System.out.println(stato.stampaStatoSistema());
	}

	public void mostraMessaggio(String msg) {
		System.out.println(msg);
	}

	public int leggiNumeroAccompagnatori(String messaggio, int nMaxPersone) {
		return InputDati.leggiIntero(messaggio, 1, nMaxPersone);
	}

	public String chiediCodicePrenotazione() {
		Scanner scanner = new Scanner(System.in); // Crea l'oggetto di input locale
		String codicePrenotazione = "";
		do {
			System.out.print("Inserisci il codice prenotazione che vuoi disdire (oppure lascia vuoto per annullare): ");
			codicePrenotazione = scanner.nextLine().trim();
			if (codicePrenotazione.isEmpty()) {
				System.out.println("Operazione annullata.");
				return ""; // Oppure return null;
			}
		} while (codicePrenotazione.isEmpty());
		return codicePrenotazione;
	}

}
