package Versione1;

import java.util.Objects;

public class Indirizzo {
    private int cap;
    private String comune;
    private String nomeStrada;
    private String numeroCivico;
    private String provincia;
    private TipoStrada tipoStrada;

    /**
     * INVARIANTI DI CLASSE:
     * - cap è un CAP valido (10000 <= cap <= 99999)
     * - comune != null && !comune.isBlank()
     * - provincia != null && !provincia.isBlank()
     * - nomeStrada != null && !nomeStrada.isBlank()
     * - numeroCivico != null && !numeroCivico.isBlank()
     * - tipoStrada != null
     */

    // Costruttore vuoto (per XML o framework di serializzazione)
    public Indirizzo() {}

    /**
     * Costruttore con parametri
     * PRECONDIZIONI:
     * - tipoStrada != null
     * - nomeStrada, numeroCivico, comune, provincia != null e non vuoti
     * - cap compreso tra 10000 e 99999
     * POSTCONDIZIONI:
     * - i campi dell'oggetto coincidono con i parametri passati
     */
    public Indirizzo(TipoStrada tipoStrada, String nomeStrada, String numeroCivico, int cap, String comune,
                     String provincia) {
        if (tipoStrada == null) throw new IllegalArgumentException("TipoStrada non può essere null");
        if (nomeStrada == null || nomeStrada.isBlank()) throw new IllegalArgumentException("Nome strada non valido");
        if (numeroCivico == null || numeroCivico.isBlank()) throw new IllegalArgumentException("Numero civico non valido");
        if (comune == null || comune.isBlank()) throw new IllegalArgumentException("Comune non valido");
        if (provincia == null || provincia.isBlank()) throw new IllegalArgumentException("Provincia non valida");
        if (cap < 10000 || cap > 99999) throw new IllegalArgumentException("CAP non valido: " + cap);

        this.tipoStrada = tipoStrada;
        this.nomeStrada = nomeStrada;
        this.numeroCivico = numeroCivico;
        this.cap = cap;
        this.comune = comune;
        this.provincia = provincia;

        assert invClass() : "Invariante violato nel costruttore";
    }

    // ================== GETTERS ==================
    public Integer getCap() { return cap; }
    public String getComune() { return comune; }
    public String getNomeStrada() { return nomeStrada; }
    public String getNumeroCivico() { return numeroCivico; }
    public String getProvincia() { return provincia; }
    public TipoStrada getTipoStrada() { return tipoStrada; }

    // ================== SETTERS ==================

    /**
     * PRECONDIZIONE: cap compreso tra 10000 e 99999
     * POSTCONDIZIONE: this.cap == cap
     */
    public void setCap(int cap) {
        if (cap < 10000 || cap > 99999) throw new IllegalArgumentException("CAP non valido: " + cap);
        this.cap = cap;
        assert this.cap == cap : "Postcondizione violata in setCap";
        assert invClass() : "Invariante violato in setCap";
    }

    /**
     * PRECONDIZIONE: comune != null && !comune.isBlank()
     * POSTCONDIZIONE: this.comune == comune
     */
    public void setComune(String comune) {
        if (comune == null || comune.isBlank()) throw new IllegalArgumentException("Comune non valido");
        this.comune = comune;
        assert this.comune.equals(comune) : "Postcondizione violata in setComune";
        assert invClass() : "Invariante violato in setComune";
    }

    /**
     * PRECONDIZIONE: nomeStrada != null && !nomeStrada.isBlank()
     * POSTCONDIZIONE: this.nomeStrada == nomeStrada
     */
    public void setNomeStrada(String nomeStrada) {
        if (nomeStrada == null || nomeStrada.isBlank()) throw new IllegalArgumentException("Nome strada non valido");
        this.nomeStrada = nomeStrada;
        assert this.nomeStrada.equals(nomeStrada) : "Postcondizione violata in setNomeStrada";
        assert invClass() : "Invariante violato in setNomeStrada";
    }

    /**
     * PRECONDIZIONE: numeroCivico != null && !numeroCivico.isBlank()
     * POSTCONDIZIONE: this.numeroCivico == numeroCivico
     */
    public void setNumeroCivico(String numeroCivico) {
        if (numeroCivico == null || numeroCivico.isBlank()) throw new IllegalArgumentException("Numero civico non valido");
        this.numeroCivico = numeroCivico;
        assert this.numeroCivico.equals(numeroCivico) : "Postcondizione violata in setNumeroCivico";
        assert invClass() : "Invariante violato in setNumeroCivico";
    }

    /**
     * PRECONDIZIONE: provincia != null && !provincia.isBlank()
     * POSTCONDIZIONE: this.provincia == provincia
     */
    public void setProvincia(String provincia) {
        if (provincia == null || provincia.isBlank()) throw new IllegalArgumentException("Provincia non valida");
        this.provincia = provincia;
        assert this.provincia.equals(provincia) : "Postcondizione violata in setProvincia";
        assert invClass() : "Invariante violato in setProvincia";
    }

    /**
     * PRECONDIZIONE: tipoStrada != null
     * POSTCONDIZIONE: this.tipoStrada == tipoStrada
     */
    public void setTipoStrada(TipoStrada tipoStrada) {
        if (tipoStrada == null) throw new IllegalArgumentException("TipoStrada non può essere null");
        this.tipoStrada = tipoStrada;
        assert this.tipoStrada == tipoStrada : "Postcondizione violata in setTipoStrada";
        assert invClass() : "Invariante violato in setTipoStrada";
    }

    // ================== METODI DI UTILITÀ ==================

    public void stampaIndirizzo() {
        System.out.println("Tipo strada: " + tipoStrada);
        System.out.println("Nome strada: " + nomeStrada);
        System.out.println("Numero civico: " + numeroCivico);
        System.out.println("CAP: " + cap);
        System.out.println("Comune: " + comune);
        System.out.println("Provincia: " + provincia);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (tipoStrada != null) sb.append(tipoStrada.toString()).append(" ");
        if (nomeStrada != null) sb.append(nomeStrada).append(" ");
        if (numeroCivico != null) sb.append(numeroCivico).append(", ");

        sb.append(cap).append(" ");

        if (comune != null) sb.append(comune).append(" ");
        if (provincia != null) sb.append("(").append(provincia).append(")");

        return sb.toString().trim();
    }

    // ================== INVARIANTE ==================
    private boolean invClass() {
        if (cap < 10000 || cap > 99999) return false;
        if (comune == null || comune.isBlank()) return false;
        if (provincia == null || provincia.isBlank()) return false;
        if (nomeStrada == null || nomeStrada.isBlank()) return false;
        if (numeroCivico == null || numeroCivico.isBlank()) return false;
        if (tipoStrada == null) return false;
        return true;
    }
}
