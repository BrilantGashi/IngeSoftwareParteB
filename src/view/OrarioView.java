package view;

import it.unibs.fp.mylib.InputDati;

public class OrarioView {
    public int leggiOra() {
        return InputDati.leggiIntero("Inserisci ora inizio: ", 0, 23);
    }
    public int leggiMinuti() {
        return InputDati.leggiIntero("Inserisci minuti inizio: ", 0, 59);
    }
    public void mostraErrore(String msg) {
        System.out.println("Orario non valido: " + msg + ". Riprova.");
    }
}
