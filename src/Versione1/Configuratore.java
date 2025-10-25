package Versione1;

import java.util.Objects;

public class Configuratore extends Utente {

    /**
     * INVARIANTI DI CLASSE:
     * - credenziali != null
     * - credenziali.username != null && !username.isBlank()
     */

    /**
     * Costruttore
     * PRECONDIZIONE: credenziali != null && credenziali.getUsername() != null && !isBlank()
     * POSTCONDIZIONE: this.credenziali == credenziali
     */
    public Configuratore(Credenziali credenziali) {
        super(Objects.requireNonNull(credenziali, "Credenziali non possono essere null"));
        if (credenziali.getUsername() == null || credenziali.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        assert invClass() : "Invariante violato nel costruttore di Configuratore";
    }

    // ================== INVARIANTE ==================
    private boolean invClass() {
        if (credenziali == null) return false;
        if (credenziali.getUsername() == null || credenziali.getUsername().isBlank()) return false;
        return true;
    }
    
    public String toString () {
    	StringBuilder sb = new StringBuilder();
        sb.append("CONFIGURATORE\n");
        sb.append("USERNAME: ");
        sb.append(this.credenziali.getUsername());
        return sb.toString();
    }
}
