package Versione1;

import java.util.Objects;

public class Volontario extends Utente {
    
    private String nickname;

    /**
     * INVARIANTI DI CLASSE:
     * - credenziali != null
     * - credenziali.username != null && !username.isBlank()
     * - nickname != null && !nickname.isBlank()
     */

    /**
     * Costruttore
     * PRECONDIZIONE: credenziali != null && credenziali.getUsername() != null && !isBlank()
     * POSTCONDIZIONE: this.nickname == credenziali.getUsername()
     */
    public Volontario(Credenziali credenziali) {
        super(Objects.requireNonNull(credenziali, "Credenziali non possono essere null"));
        if (credenziali.getUsername() == null || credenziali.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        this.nickname = credenziali.getUsername();
        assert invClass() : "Invariante violato nel costruttore";
    }

    /**
     * Restituisce il nickname del volontario.
     * POSTCONDIZIONE: ritorna un valore non nullo e non vuoto
     */
    public String getNickname() {
        assert nickname != null && !nickname.isBlank() : "Postcondizione violata in getNickname";
        return nickname;
    }

    /**
     * Imposta il nickname del volontario.
     * PRECONDIZIONE: nickname != null && !nickname.isBlank()
     * POSTCONDIZIONE: this.nickname == nickname
     */
    public void setNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname non valido");
        }
        this.nickname = nickname;
        assert this.nickname.equals(nickname) : "Postcondizione violata in setNickname";
        assert invClass() : "Invariante violato in setNickname";
    }

    /**
     * Restituisce una rappresentazione testuale del volontario.
     * POSTCONDIZIONE: il risultato contiene la parola "Volontario" e l'username delle credenziali
     */
    @Override
    public String toString() {
        String result = "Volontario " + credenziali.getUsername();
        assert result.contains("Volontario") : "Postcondizione violata in toString";
        assert result.contains(credenziali.getUsername()) : "Postcondizione violata in toString";
        return result;
    }

    // ================== INVARIANTE ==================
    private boolean invClass() {
        if (credenziali == null) return false;
        if (credenziali.getUsername() == null || credenziali.getUsername().isBlank()) return false;
        if (nickname == null || nickname.isBlank()) return false;
        return true;
    }
}
