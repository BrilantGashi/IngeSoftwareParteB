package model;

import java.util.Objects;

public class ConfiguratoreModel extends UtenteModel {

	private int nMaxPersone;

    public ConfiguratoreModel(CredenzialiModel credenziali) {
        super(Objects.requireNonNull(credenziali, "Credenziali non possono essere null"));
        if (credenziali.getUsername() == null || credenziali.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CONFIGURATORE\n");
        sb.append("USERNAME: ");
        sb.append(this.credenziali.getUsername());
        return sb.toString();
    }
    
    public int getNMaxPersone() { return nMaxPersone; }
    public void setNMaxPersone(int nMaxPersone) { this.nMaxPersone = nMaxPersone; }
}
