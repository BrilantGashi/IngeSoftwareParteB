package Versione1;

public class Disponibilita {

	private String username; //volontario che esprime la disponibilità
	private int giorno; 
	private String mese; 
	
	public Disponibilita(String username, int giorno, String mese) {
		super();
		this.username = username;
		this.giorno = giorno;
		this.mese = mese;
	}

	
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    sb.append("===== DISPONIBILITA' VOLONTARIO =====\n");
	    sb.append("Volontario: ").append(username).append("\n");
	    sb.append("Mese: ").append(mese).append("\n");
	    sb.append("Giorno disponibile: ").append(giorno).append("\n");
	    sb.append("=====================================\n");

	    return sb.toString();
	}
	
	//**************************************************************//
	//             GETTERS & SETTERS                             //
	//**************************************************************//
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public String getMese() {
		return mese;
	}

	public void setMese(String mese) {
		this.mese = mese;
	}
	
	
	
}
