package view;

import model.UtenteModel;

public class UtenteView {

    public void mostraUtente(UtenteModel utente) {
        System.out.println("Username: " + utente.toStringUsername());
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
}
