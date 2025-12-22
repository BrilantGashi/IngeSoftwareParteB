package view;

import it.unibs.fp.mylib.InputDati;
import model.CredenzialiModel;

public class CredenzialiView {

    public CredenzialiModel leggiCredenziali(String utenza) {
        String username = InputDati.leggiStringaNonVuota("Username: ");
        String password = InputDati.leggiStringaNonVuota("Password: ");
        return new CredenzialiModel(utenza, username, password);
    }

    public String leggiNuovaPassword() {
        return InputDati.leggiStringaNonVuota("Inserisci la nuova password: ");
    }
    
    public String leggiUsername() {
    	return InputDati.leggiStringaNonVuota("Inserisci username: ");
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
    

    public void mostraCredenziali(CredenzialiModel cred) {
        System.out.println("Username: " + cred.getUsername());
        System.out.println("Utenza: " + cred.getUtenza());
        System.out.println("Password: " + cred.getPassword());
    }
}
