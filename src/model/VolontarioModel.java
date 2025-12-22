package model;

import java.util.Objects;

public class VolontarioModel extends UtenteModel {

    private String nickname;
    private boolean isAttivo;

    public VolontarioModel(CredenzialiModel credenziali) {
        super(Objects.requireNonNull(credenziali, "Credenziali non possono essere null"));
        if (credenziali.getUsername() == null || credenziali.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        this.nickname = credenziali.getUsername();
        this.isAttivo = true;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname non valido");
        }
        this.nickname = nickname;
    }

    public boolean isAttivo() {
        return isAttivo;
    }

    public void setAttivo(boolean attivo) {
        this.isAttivo = attivo;
    }

    @Override
    public String toString() {
        String statoAttivo = isAttivo ? "[ATTIVO]" : "[DISATTIVATO]";
        return "Volontario " + credenziali.getUsername() + " " + statoAttivo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VolontarioModel that = (VolontarioModel) obj;
        return Objects.equals(credenziali.getUsername(), that.credenziali.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(credenziali.getUsername());
    }
}
