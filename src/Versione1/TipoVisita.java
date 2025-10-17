package Versione1;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class TipoVisita {
    
    private boolean biglietto;
    private String descrizione;
    private int durataVisita;
    private ArrayList<Giorni> giorniSettimanaProgrammabile;
    private LocalTime oraInizio;
    private Periodo periodoProgrammabile;
    private String puntoDiIncontro;
    private int sogliaMaxPartecipanti;
    private int sogliaMinPartecipanti;
    private String titolo;
    private ListaVolontari volontari;

    /**
     * INVARIANTI DI CLASSE:
     * - titolo != null && !titolo.isBlank()
     * - durataVisita > 0
     * - sogliaMinPartecipanti >= 1
     * - sogliaMaxPartecipanti >= sogliaMinPartecipanti
     * - periodoProgrammabile != null
     * - volontari != null
     * - giorniSettimanaProgrammabile != null (può essere vuota)
     */

    public TipoVisita() {}

    /**
     * Costruttore completo
     * PRECONDIZIONI:
     * - titolo != null && !titolo.isBlank()
     * - descrizione != null && !descrizione.isBlank()
     * - puntoDiIncontro != null && !puntoDiIncontro.isBlank()
     * - giorniSettimanaProgrammabile != null
     * - oraInizio != null
     * - durataVisita > 0
     * - sogliaMinPartecipanti >= 1
     * - sogliaMaxPartecipanti >= sogliaMinPartecipanti
     * - periodoProgrammabile != null
     * - volontari != null
     * POSTCONDIZIONI:
     * - i campi dell'oggetto coincidono con i parametri passati
     */
    public TipoVisita(String titolo, String descrizione, String puntoDiIncontro, ArrayList<Giorni> giorniSettimanaProgrammabile,
                      LocalTime oraInizio, int durataVisita, boolean biglietto,
                      int sogliaMinPartecipanti, int sogliaMaxPartecipanti,
                      Periodo periodoProgrammabile, ListaVolontari volontari) {

        if (titolo == null || titolo.isBlank()) throw new IllegalArgumentException("Titolo non valido");
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione non valida");
        if (puntoDiIncontro == null || puntoDiIncontro.isBlank()) throw new IllegalArgumentException("Punto di incontro non valido");
        Objects.requireNonNull(giorniSettimanaProgrammabile, "La lista dei giorni non può essere null");
        Objects.requireNonNull(oraInizio, "Ora inizio non può essere null");
        if (durataVisita <= 0) throw new IllegalArgumentException("Durata non valida");
        if (sogliaMinPartecipanti < 1) throw new IllegalArgumentException("Soglia minima non valida");
        if (sogliaMaxPartecipanti < sogliaMinPartecipanti) throw new IllegalArgumentException("Soglia massima non valida");
        Objects.requireNonNull(periodoProgrammabile, "Periodo programmabile non può essere null");
        Objects.requireNonNull(volontari, "Lista volontari non può essere null");

        this.titolo = titolo;
        this.descrizione = descrizione;
        this.puntoDiIncontro = puntoDiIncontro;
        this.giorniSettimanaProgrammabile = giorniSettimanaProgrammabile;
        this.oraInizio = oraInizio;
        this.durataVisita = durataVisita;
        this.biglietto = biglietto;
        this.sogliaMinPartecipanti = sogliaMinPartecipanti;
        this.sogliaMaxPartecipanti = sogliaMaxPartecipanti;
        this.periodoProgrammabile = periodoProgrammabile;
        this.volontari = volontari;

        assert invClass() : "Invariante violato nel costruttore";
    }

    // ================== GETTERS ==================
    public String getDescrizione() { return descrizione; }
    public int getDurataVisita() { return durataVisita; }
    public ArrayList<Giorni> getGiorniSettimanaProgrammabile() { return giorniSettimanaProgrammabile; }
    public ListaVolontari getListaVolontari() { return volontari; }
    public LocalTime getOraInizio() { return oraInizio; }
    public Periodo getPeriodoProgrammabile() { return periodoProgrammabile; }
    public String getPuntoDiIncontro() { return puntoDiIncontro; }
    public int getSogliaMaxPartecipanti() { return sogliaMaxPartecipanti; }
    public int getSogliaMinPartecipanti() { return sogliaMinPartecipanti; }
    public String getTitolo() { return titolo; }
    public boolean isBiglietto() { return biglietto; }

    // ================== SETTERS ==================

    public void setBiglietto(boolean biglietto) { this.biglietto = biglietto; }

    public void setDescrizione(String descrizione) {
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione non valida");
        this.descrizione = descrizione;
        assert invClass() : "Invariante violato in setDescrizione";
    }

    public void setDurataVisita(int durataVisita) {
        if (durataVisita <= 0) throw new IllegalArgumentException("Durata non valida");
        this.durataVisita = durataVisita;
        assert invClass() : "Invariante violato in setDurataVisita";
    }

    public void setGiorniSettimanaProgrammabile(ArrayList<Giorni> giorniSettimana) {
        Objects.requireNonNull(giorniSettimana, "La lista dei giorni non può essere null");
        this.giorniSettimanaProgrammabile = giorniSettimana;
        assert invClass() : "Invariante violato in setGiorniSettimanaProgrammabile";
    }

    public void setListaVolontari(ListaVolontari volontari) {
        Objects.requireNonNull(volontari, "Lista volontari non può essere null");
        this.volontari = volontari;
        assert invClass() : "Invariante violato in setListaVolontari";
    }

    public void setOraInizio(LocalTime oraInizio) {
        Objects.requireNonNull(oraInizio, "Ora inizio non può essere null");
        this.oraInizio = oraInizio;
        assert invClass() : "Invariante violato in setOraInizio";
    }

    public void setPeriodoProgrammabile(Periodo periodoProgrammabile) {
        Objects.requireNonNull(periodoProgrammabile, "Periodo programmabile non può essere null");
        this.periodoProgrammabile = periodoProgrammabile;
        assert invClass() : "Invariante violato in setPeriodoProgrammabile";
    }

    public void setPuntoDiIncontro(String string) {
        if (string == null || string.isBlank()) throw new IllegalArgumentException("Punto di incontro non valido");
        this.puntoDiIncontro = string;
        assert invClass() : "Invariante violato in setPuntoDiIncontro";
    }

    public void setSogliaMaxPartecipanti(int sogliaMaxPartecipanti) {
        if (sogliaMaxPartecipanti < this.sogliaMinPartecipanti) {
            throw new IllegalArgumentException("Soglia massima non valida");
        }
        this.sogliaMaxPartecipanti = sogliaMaxPartecipanti;
        assert invClass() : "Invariante violato in setSogliaMaxPartecipanti";
    }

    public void setSogliaMinPartecipanti(int sogliaMinPartecipanti) {
        if (sogliaMinPartecipanti < 1 || sogliaMinPartecipanti > this.sogliaMaxPartecipanti) {
            throw new IllegalArgumentException("Soglia minima non valida");
        }
        this.sogliaMinPartecipanti = sogliaMinPartecipanti;
        assert invClass() : "Invariante violato in setSogliaMinPartecipanti";
    }

    public void setTitolo(String titolo) {
        if (titolo == null || titolo.isBlank()) throw new IllegalArgumentException("Titolo non valido");
        this.titolo = titolo;
        assert invClass() : "Invariante violato in setTitolo";
    }

    /**
     * Verifica se un volontario con username è associato a questo tipo di visita.
     * PRECONDIZIONE: username != null && !username.isBlank()
     * POSTCONDIZIONE: ritorna true sse esiste un volontario con quel nickname
     */
    public boolean volontarioTrovato(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        for (Volontario v : volontari.getVolontari()) {
            if (v.getNickname().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Controlla se due tipi di visita si sovrappongono temporalmente.
     * PRECONDIZIONE: altraVisita != null
     * POSTCONDIZIONE: ritorna true sse gli intervalli temporali si intersecano
     */
    public boolean siSovrappongono(TipoVisita altraVisita) {
        Objects.requireNonNull(altraVisita, "La visita di confronto non può essere null");

        LocalTime fineQuesta = this.oraInizio.plusMinutes(this.durataVisita);
        LocalTime fineAltra = altraVisita.oraInizio.plusMinutes(altraVisita.durataVisita);

        boolean result = this.oraInizio.isBefore(fineAltra) && altraVisita.oraInizio.isBefore(fineQuesta);

        assert result == (this.oraInizio.isBefore(fineAltra) && altraVisita.oraInizio.isBefore(fineQuesta))
            : "Postcondizione violata in siSovrappongono";

        return result;
    }

    // ================== INVARIANTE ==================
    private boolean invClass() {
        if (titolo == null || titolo.isBlank()) return false;
        if (durataVisita <= 0) return false;
        if (sogliaMinPartecipanti < 1) return false;
        if (sogliaMaxPartecipanti < sogliaMinPartecipanti) return false;
        if (periodoProgrammabile == null) return false;
        if (volontari == null) return false;
        if (giorniSettimanaProgrammabile == null) return false;
        if (oraInizio == null) return false;
        return true;
    }
}
