package Versione1;

import java.util.Objects;

public class Periodo {
    private Data dataInizio;
    private Data dataFine;

    /**
     * INVARIANTI DI CLASSE:
     * - dataInizio != null
     * - dataFine != null
     * - dataInizio non è successiva a dataFine
     */

    // Costruttore vuoto (per XML o framework di serializzazione)
    public Periodo() {}

    /**
     * Costruttore con parametri
     * PRECONDIZIONI:
     * - dataInizio != null
     * - dataFine != null
     * - dataInizio <= dataFine
     * POSTCONDIZIONI:
     * - this.dataInizio == dataInizio
     * - this.dataFine == dataFine
     */
    public Periodo(Data dataInizio, Data dataFine) {
        Objects.requireNonNull(dataInizio, "La data di inizio non può essere null");
        Objects.requireNonNull(dataFine, "La data di fine non può essere null");

        this.dataInizio = dataInizio;
        this.dataFine = dataFine;

        assert invClass() : "Invariante violato nel costruttore";
    }

    public Data getDataFine() {
        return dataFine;
    }

    public Data getDataInizio() {
        return dataInizio;
    }

    /**
     * PRECONDIZIONE: dataFine != null && dataInizio <= dataFine
     * POSTCONDIZIONE: this.dataFine == dataFine
     */
    public void setDataFine(Data dataFine) {
        Objects.requireNonNull(dataFine, "La data di fine non può essere null");
        if (this.dataInizio != null && dataFine.precede(this.dataInizio)) {
            throw new IllegalArgumentException("La data di fine non può precedere la data di inizio");
        }
        this.dataFine = dataFine;
        assert this.dataFine == dataFine : "Postcondizione violata in setDataFine";
        assert invClass() : "Invariante violato in setDataFine";
    }

    /**
     * PRECONDIZIONE: dataInizio != null && dataInizio <= dataFine
     * POSTCONDIZIONE: this.dataInizio == dataInizio
     */
    public void setDataInizio(Data dataInizio) {
        Objects.requireNonNull(dataInizio, "La data di inizio non può essere null");
        if (this.dataFine != null && this.dataFine.precede(dataInizio)) {
            throw new IllegalArgumentException("La data di inizio non può essere successiva alla data di fine");
        }
        this.dataInizio = dataInizio;
        assert this.dataInizio == dataInizio : "Postcondizione violata in setDataInizio";
        assert invClass() : "Invariante violato in setDataInizio";
    }
    
    /**
     * Determina se due periodi si intersecano (almeno parzialmente).
     * PRECONDIZIONI:
     * - this != null
     * - altro != null
     * - this.dataInizio != null, this.dataFine != null
     * - altro.dataInizio != null, altro.dataFine != null
     * - this.dataInizio non deve essere successiva a this.dataFine
     * - altro.dataInizio non deve essere successiva a altro.dataFine
     *
     * POSTCONDIZIONI:
     * - Restituisce true se esiste almeno un giorno comune tra i due periodi.
     * - Restituisce false se i due periodi sono completamente disgiunti
     *   (cioè uno termina prima che l'altro inizi).
     *
     * @param altro il periodo da confrontare
     * @return true se i due periodi si intersecano, false altrimenti
     */
    public boolean siIntersecaCon(Periodo altro) {
        Objects.requireNonNull(altro, "Il periodo di confronto non può essere null");
        Objects.requireNonNull(this.dataInizio, "Data inizio non può essere null");
        Objects.requireNonNull(this.dataFine, "Data fine non può essere null");
        Objects.requireNonNull(altro.dataInizio, "Data inizio del periodo di confronto non può essere null");
        Objects.requireNonNull(altro.dataFine, "Data fine del periodo di confronto non può essere null");

        // Caso 1: questo periodo finisce prima che inizi l'altro
        if (this.dataFine.precede(altro.dataInizio)) {
            return false;
        }

        // Caso 2: l'altro periodo finisce prima che inizi questo
        if (altro.dataFine.precede(this.dataInizio)) {
            return false;
        }

        // Altrimenti c'è almeno una sovrapposizione
        return true;
    }
    
    @Override
    public String toString() {
        return "Periodo valido: " + dataInizio.toString() + " a " + dataFine.toString();
    }

    // ================== INVARIANTE ==================
    private boolean invClass() {
        if (dataInizio == null || dataFine == null) return false;
        return true;
    }
}
