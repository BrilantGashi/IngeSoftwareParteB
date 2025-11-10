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

        // Controlla se un periodo attraversa il capodanno (dataFine < dataInizio)
        boolean thisAttraversaCapodanno = this.dataFine.precede(this.dataInizio);
        boolean altroAttraversaCapodanno = altro.dataFine.precede(altro.dataInizio);

        // CASO 1: Nessuno dei due periodi attraversa il capodanno
        if (!thisAttraversaCapodanno && !altroAttraversaCapodanno) {
            // Logica normale: si intersecano se non sono completamente separati
            return !(this.dataFine.precede(altro.dataInizio) || altro.dataFine.precede(this.dataInizio));
        }

        // CASO 2: Entrambi attraversano il capodanno
        if (thisAttraversaCapodanno && altroAttraversaCapodanno) {
            // Entrambi sono attivi per tutto l'anno (con buchi), quindi si intersecano sempre
            return true;
        }

        // CASO 3: Solo THIS attraversa il capodanno
        if (thisAttraversaCapodanno && !altroAttraversaCapodanno) {
            // THIS è attivo da 1/1 a dataFine E da dataInizio a 31/12
            // Si interseca se altro cade in uno di questi due intervalli
            return !altro.dataInizio.precede(this.dataFine) || !this.dataInizio.precede(altro.dataFine);
        }

        // CASO 4: Solo ALTRO attraversa il capodanno
        if (!thisAttraversaCapodanno && altroAttraversaCapodanno) {
            // ALTRO è attivo da 1/1 a dataFine E da dataInizio a 31/12
            // Si interseca se this cade in uno di questi due intervalli
            return !this.dataInizio.precede(altro.dataFine) || !altro.dataInizio.precede(this.dataFine);
        }

        // Non dovrebbe mai arrivare qui
        return false;
    }
 
    @Override
    public String toString() {
        return "Periodo valido: " + dataInizio.toString() + " a " + dataFine.toString();
    }

    private boolean invClass() {
        if (dataInizio == null || dataFine == null) return false;
        return true;
    }
}
