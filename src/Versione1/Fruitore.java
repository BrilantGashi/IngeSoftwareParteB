package Versione1;

import java.util.Objects;

public class Fruitore extends Utente {

    public Fruitore(Credenziali credenziali) {
        super(Objects.requireNonNull(credenziali, "Credenziali non possono essere null"));
        String user = credenziali.getUsername();
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
    }

    // Getter per credenziali (eredita spesso direttamente da Utente, ma lo lasciamo se serve)
    public Credenziali getCredenziali() {
        return credenziali;
    }

    // Nessun attributo per accompagnatori: adesso è solo anagrafica!
}
