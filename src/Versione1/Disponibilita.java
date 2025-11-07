package Versione1;

public class Disponibilita {

    private String username; // volontario che esprime la disponibilità
    private int giorno; 
    private int mese; // Cambiato da String a int

    /**
     * Costruttore
     * @param username Il nome utente del volontario
     * @param giorno Il giorno di disponibilità (1-31)
     * @param mese Il mese di disponibilità (1=i+1, 2=i+2, 3=i+3)
     */
    public Disponibilita(String username, int giorno, int mese) {
        super();
        this.username = username;
        this.giorno = giorno;
        this.mese = mese;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("===== DISPONIBILITA' VOLONTARIO =====\n");
        sb.append("Volontario: ").append(username).append("\n");
        sb.append("Mese i+").append(mese).append("\n"); // ✅ Mostra come i+1, i+2, i+3
        sb.append("Giorno disponibile: ").append(giorno).append("\n");
        sb.append("=====================================\n");

        return sb.toString();
    }

    //**************************************************************//
    //                    GETTERS & SETTERS                         //
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

    public int getMese() { // ✅ Cambiato da String a int
        return mese;
    }

    public void setMese(int mese) { // ✅ Cambiato da String a int
        this.mese = mese;
    }
}
