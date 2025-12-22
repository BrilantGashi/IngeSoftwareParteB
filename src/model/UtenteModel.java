package model;

import java.util.Objects;

public class UtenteModel {
	protected CredenzialiModel credenziali;

	public UtenteModel(CredenzialiModel credenziali) {
		Objects.requireNonNull(credenziali, "Credenziali non possono essere null");
		if (credenziali.getUsername() == null || credenziali.getUsername().isBlank())
			throw new IllegalArgumentException("Username non valido");
		this.credenziali = credenziali;
	}

	public CredenzialiModel getCredenziali() {
		return credenziali;
	}

	public void setCredenziali(CredenzialiModel credenziali) {
		Objects.requireNonNull(credenziali, "Credenziali non possono essere null");
		if (credenziali.getUsername() == null || credenziali.getUsername().isBlank())
			throw new IllegalArgumentException("Username non valido");
		this.credenziali = credenziali;
	}

	public String toStringUsername() {
		String username = credenziali.getUsername();
		if (username == null || username.isBlank())
			throw new IllegalStateException("Username invalido");
		return username;
	}
}
