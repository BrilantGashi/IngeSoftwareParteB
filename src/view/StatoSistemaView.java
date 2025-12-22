package view;

import model.StatoSistemaModel;

public class StatoSistemaView {

    public void mostraStato(StatoSistemaModel stato) {
        System.out.println("\nStato attuale del sistema:");
        System.out.println(stato.stampaStatoSistema());
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
}
