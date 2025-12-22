package model;

public class DisponibilitaModel {
	private String username;
	private int giorno;
	private int mese;

	public DisponibilitaModel(String username, int giorno, int mese) {
		if (username == null || username.isBlank())
			throw new IllegalArgumentException("Username non valido");
		if (giorno < 1 || giorno > 31)
			throw new IllegalArgumentException("Giorno non valido");
		if (mese < 1 || mese > 12)
			throw new IllegalArgumentException("Mese non valido");
		this.username = username;
		this.giorno = giorno;
		this.mese = mese;
		if (!invClass())
			throw new IllegalStateException("Invariante violato");
	}

	public String getUsername() {
		return username;
	}

	public int getGiorno() {
		return giorno;
	}

	public int getMese() {
		return mese;
	}

	public void setUsername(String username) {
		if (username == null || username.isBlank())
			throw new IllegalArgumentException("Username non valido");
		this.username = username;
		if (!invClass())
			throw new IllegalStateException("Invariante violato");
	}

	public void setGiorno(int giorno) {
		if (giorno < 1 || giorno > 31)
			throw new IllegalArgumentException("Giorno non valido");
		this.giorno = giorno;
		if (!invClass())
			throw new IllegalStateException("Invariante violato");
	}

	public void setMese(int mese) {
		if (mese < 1 || mese > 12)
			throw new IllegalArgumentException("Mese non valido");
		this.mese = mese;
		if (!invClass())
			throw new IllegalStateException("Invariante violato");
	}

	@Override
	public String toString() {
		return "===== DISPONIBILITA' VOLONTARIO =====\n" + "Volontario: " + username + "\n" + "Mese: i+" + mese + "\n"
				+ "Giorno disponibile: " + giorno + "\n" + "=====================================\n";
	}

	private boolean invClass() {
		return username != null && !username.isBlank() && giorno >= 1 && giorno <= 31 && mese >= 1 && mese <= 12;
	}
}
