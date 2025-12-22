package model;

public class CredenzialiModel {
    private String password;
    private String username;
    private String utenza;

    public CredenzialiModel() {}

    public CredenzialiModel(String utenza, String username, String password) {
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
        if (!invClass()) throw new IllegalStateException("Invariante violato");
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getUtenza() {
        return utenza;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password non valida");
        }
        this.password = password;
        if (!invClass()) throw new IllegalStateException("Invariante violato");
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        this.username = username;
        if (!invClass()) throw new IllegalStateException("Invariante violato");
    }

    public void setUtenza(String utenza) {
        if (utenza == null || utenza.isBlank()) {
            throw new IllegalArgumentException("Utenza non valida");
        }
        this.utenza = utenza;
        if (!invClass()) throw new IllegalStateException("Invariante violato");
    }

    @Override
    public String toString() {
        return "Credenziali [username=" + username + ", password=" + password + ", utenza=" + utenza + "]";
    }

    private boolean invClass() {
        return username != null && !username.isBlank()
            && password != null && !password.isBlank()
            && utenza != null && !utenza.isBlank();
    }

}
