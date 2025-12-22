package model;

import java.util.Objects;

public class FruitoreModel extends UtenteModel {

    public FruitoreModel(CredenzialiModel credenziali) {
        super(Objects.requireNonNull(credenziali, "Credenziali non possono essere null"));
        String user = credenziali.getUsername();
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
    }

    public CredenzialiModel getCredenziali() {
        return credenziali;
    }
    
}
