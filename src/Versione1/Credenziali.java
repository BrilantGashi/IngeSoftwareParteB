package Versione1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import it.unibs.fp.mylib.InputDati;

public class Credenziali {
    
    private GestioneFile g = new GestioneFile();
    private String password;
    private String username;
    private String utenza;
    private UtenteDAO UtenteDao = new UtenteDAO();

    /**
     * INVARIANTI DI CLASSE:
     * - username != null && !username.isBlank()
     * - password != null && !password.isBlank()
     * - utenza != null && !utenza.isBlank()
     */

    // Costruttore vuoto (non garantisce subito l'invariante, da usare con cautela)
    public Credenziali(){ }

    /**
     * Costruttore con caricamento da file.
     * PRECONDIZIONE: username != null && !username.isBlank()
     * POSTCONDIZIONE: this.username == username letto da file
     *                 this.password != null
     *                 this.utenza != null
     */
    /*public Credenziali(String username) throws FileNotFoundException, IOException { 
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        //String[] credenziali = g.leggiSingolaCredenziale(username);
        String[] credenziali = UtenteDao.leggiSingolaCredenziale(username);
        this.utenza = credenziali[0];
        this.username = credenziali[1];
        this.password = credenziali[2]; 
        assert invClass() : "Invariante violato nel costruttore Credenziali";
    }*/
    
 	// Costruttore che NON accede al database
    public Credenziali(String utenza, String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password non valida");
        }
        if (utenza == null || utenza.isBlank()) {
            throw new IllegalArgumentException("Utenza non valida");
        }
        
        this.utenza = utenza;
        this.username = username;
        this.password = password;
        
        assert invClass() : "Invariante violato nel costruttore Credenziali";
    }

    /**
     * Controlla se le credenziali inserite sono corrette.
     * PRECONDIZIONE: usernameP != null && passwordP != null
     * POSTCONDIZIONE: ritorna -1 se password errata,
     *                 1 se primo accesso,
     *                 0 se credenziali corrette
     */
    public int controllaCredenziali(String usernameP, String passwordP) throws FileNotFoundException, IOException {
        Objects.requireNonNull(usernameP, "Username non può essere null");
        Objects.requireNonNull(passwordP, "Password non può essere null");

        if (!passwordP.equals(password)) {
            return -1;
        }
        if (verificaPrimoAccesso(username, password)) {
            return 1;
        }
        return 0;
    }

    public String getPassword() {
        assert password != null && !password.isBlank() : "Postcondizione violata in getPassword";
        return password;
    }

    public String getUsername() {
        assert username != null && !username.isBlank() : "Postcondizione violata in getUsername";
        return username;
    }

    public String getUtenza() {
        assert utenza != null && !utenza.isBlank() : "Postcondizione violata in getUtenza";
        return utenza;
    }

    /**
     * Imposta una nuova password e la aggiorna anche su file.
     * PRECONDIZIONE: password != null && !password.isBlank()
     * POSTCONDIZIONE: this.password == password
     * @throws SQLException 
     */
    public void setPassword(String password) throws FileNotFoundException, IOException, SQLException {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password non valida");
        }
        this.password = password;
        //if (g.aggiornaPasswordSuFile(username, password)) {
        if (UtenteDao.aggiornaPasswordSuDB(username, password)) {
            System.out.println("Password aggiornata correttamente.");
        }
        assert this.password.equals(password) : "Postcondizione violata in setPassword";
        assert invClass() : "Invariante violato in setPassword";
    }

    /**
     * PRECONDIZIONE: username != null && !username.isBlank()
     * POSTCONDIZIONE: this.username == username
     */
    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        this.username = username;
        assert this.username.equals(username) : "Postcondizione violata in setUsername";
        assert invClass() : "Invariante violato in setUsername";
    }

    /**
     * PRECONDIZIONE: utenza != null && !utenza.isBlank()
     * POSTCONDIZIONE: this.utenza == utenza
     */
    public void setUtenza(String utenza) {
        if (utenza == null || utenza.isBlank()) {
            throw new IllegalArgumentException("Utenza non valida");
        }
        this.utenza = utenza;
        assert this.utenza.equals(utenza) : "Postcondizione violata in setUtenza";
        assert invClass() : "Invariante violato in setUtenza";
    }

    @Override
    public String toString() {
        String result = "Credenziali [username=" + username + ", password=" + password + "]";
        assert result.contains(username) : "Postcondizione violata in toString";
        return result;
    }

    /**
     * Verifica se Ã¨ il primo accesso (password di default).
     * PRECONDIZIONE: username != null && password != null
     * POSTCONDIZIONE: ritorna true sse password == "default"
     */
    public boolean verificaPrimoAccesso(String username, String password) throws FileNotFoundException, IOException {
        Objects.requireNonNull(username, "Username non puÃ² essere null");
        Objects.requireNonNull(password, "Password non puÃ² essere null");

        if (password.equalsIgnoreCase("default")) {
            String nuovaPass = InputDati.leggiStringaNonVuota("Inserisci la nuova password: ");
            // setPassword(nuovaPass); // da abilitare per aggiornare anche su file
            return true;
        }
        return false;
    }

    // ================== INVARIANTE ==================
    private boolean invClass() {
        if (username == null || username.isBlank()) return false;
        if (password == null || password.isBlank()) return false;
        if (utenza == null || utenza.isBlank()) return false;
        return true;
    }
}
