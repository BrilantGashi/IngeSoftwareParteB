package model;

import java.util.Objects;

/**
 * Rappresenta un ambito territoriale italiano.
 * Invariante: provincia != null && !provincia.isBlank(), comune != null && !comune.isBlank(), cap valido (10000 <= cap <= 99999)
 */
public class AmbitoTerritorialeModel {

    private String provincia;
    private String comune;
    private int cap;

    /**
     * Costruttore vuoto (usato da framework o XML).
     * Lo stato iniziale dell'oggetto non rispetta invarianti e dev'essere valorizzato tramite setter.
     */
    public AmbitoTerritorialeModel() {
        this.provincia = "";
        this.comune = "";
        this.cap = 0;
    }

    /**
     * Costruttore con parametri.
     * @param cap CAP italiano valido (intero 5 cifre)
     * @param provincia nome della provincia, non null e non vuoto
     * @param comune nome del comune, non null e non vuoto
     * @throws IllegalArgumentException se i parametri non rispettano le precondizioni
     */
    public AmbitoTerritorialeModel(int cap, String provincia, String comune) {
        if (!isCapValido(cap)) throw new IllegalArgumentException("CAP non valido: " + cap);
        if (provincia == null || provincia.isBlank()) throw new IllegalArgumentException("Provincia non valida");
        if (comune == null || comune.isBlank()) throw new IllegalArgumentException("Comune non valido");
        this.cap = cap;
        this.provincia = provincia;
        this.comune = comune;
        assert invClass() : "Invariante di classe violato nel costruttore";
    }

    // Getters
    public int getCap() { return cap; }
    public String getProvincia() { return provincia; }
    public String getComune() { return comune; }

    // Setters con validazione
    public void setCap(int cap) {
        if (!isCapValido(cap)) throw new IllegalArgumentException("CAP non valido: " + cap);
        this.cap = cap;
        assert invClass() : "Invariante di classe violato in setCap";
    }

    public void setProvincia(String provincia) {
        if (provincia == null || provincia.isBlank()) throw new IllegalArgumentException("Provincia non valida");
        this.provincia = provincia;
        assert invClass() : "Invariante di classe violato in setProvincia";
    }

    public void setComune(String comune) {
        if (comune == null || comune.isBlank()) throw new IllegalArgumentException("Comune non valida");
        this.comune = comune;
        assert invClass() : "Invariante di classe violato in setComune";
    }

    /**
     * Confronta l'ambito territoriale con un oggetto Indirizzo.
     * @param indirizzo oggetto Indirizzo da confrontare
     * @return true se provincia, comune e cap coincidono
     */
    public boolean confronto(IndirizzoModel indirizzo) {
        Objects.requireNonNull(indirizzo, "Indirizzo non può essere null");
        boolean result = this.cap == indirizzo.getCap() &&
                         this.comune.equalsIgnoreCase(indirizzo.getComune()) &&
                         this.provincia.equalsIgnoreCase(indirizzo.getProvincia());
        assert invClass() : "Invariante di classe violato in confronto";
        return result;
    }

    public void stampaAmbito() {
        System.out.println("Provincia: " + provincia);
        System.out.println("Comune: " + comune);
        System.out.println("CAP: " + cap);
    }

    @Override
    public String toString() {
        return String.format("Ambito territoriale: %s (%s), CAP %d", comune, provincia, cap);
    }

    // Validazione CAP
    private boolean isCapValido(int c) {
        return c >= 10000 && c <= 99999;
    }

    // Invariante di classe
    private boolean invClass() {
        return provincia != null && !provincia.isBlank()
            && comune != null && !comune.isBlank()
            && isCapValido(cap);
    }

    // Opzionale: equals/hashCode
}
