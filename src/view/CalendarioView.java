package view;

import it.unibs.fp.mylib.InputDati;

import java.util.ArrayList;

public class CalendarioView {

    public void mostraDateDisponibili(ArrayList<Integer> disponibili, int meseNumero, String nomeMese, String titoloVisita) {
        if (disponibili.isEmpty()) {
            System.out.println("Nessuna data disponibile.");
            return;
        }
        System.out.println("\nDate disponibili per la visita: " + titoloVisita);
        System.out.println("Mese: " + nomeMese);
        for (int i = 0; i < disponibili.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + disponibili.get(i) + "/" + meseNumero);
        }
    }

    public int chiediSceltaDataDisponibile(int max) {
        if (max == 0) return -1;
        return InputDati.leggiIntero("Seleziona la tua disponibilità (1-\" + max + \"): ", 1, max);
    }

    public void mostraConfermaScelta(int giorno, int mese) {
        System.out.println("Hai scelto: " + giorno + "/" + mese);
    }

    public void mostraTesto(String testo) {
        System.out.println(testo);
    }

    public void mostraErrore(String msg) {
        System.out.println("ERRORE: " + msg);
    }
}
