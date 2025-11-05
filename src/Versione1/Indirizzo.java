package Versione1;

import java.util.Objects;

/**
 * Classe che rappresenta un indirizzo, utilizzando un oggetto AmbitoTerritoriale per gestire comune, provincia e CAP.
 */
public class Indirizzo {

    private TipoStrada tipoStrada;         // Enum già esistente nel progetto
    private String nomeStrada;             // Nome della strada
    private String numeroCivico;           // Numero civico
    private AmbitoTerritoriale ambitoTerritoriale; // Provincia, comune e CAP

    /**
     * Costruttore vuoto.
     */
    public Indirizzo() {
        this.tipoStrada = TipoStrada.Via; // Valore di default, puoi cambiarlo
        this.nomeStrada = "";
        this.numeroCivico = "";
        this.ambitoTerritoriale = new AmbitoTerritoriale();
    }

    /**
     * Costruttore con parametri.
     */
    public Indirizzo(TipoStrada tipoStrada, String nomeStrada, String numeroCivico, AmbitoTerritoriale ambitoTerritoriale) {
        Objects.requireNonNull(tipoStrada, "Tipo di strada non può essere null.");
        if (nomeStrada == null || nomeStrada.isBlank()) throw new IllegalArgumentException("Nome della strada non valido.");
        if (numeroCivico == null || numeroCivico.isBlank()) throw new IllegalArgumentException("Numero civico non valido.");
        Objects.requireNonNull(ambitoTerritoriale, "Ambito territoriale non può essere null.");
        this.tipoStrada = tipoStrada;
        this.nomeStrada = nomeStrada;
        this.numeroCivico = numeroCivico;
        this.ambitoTerritoriale = ambitoTerritoriale;
    }

    // Getters
    public TipoStrada getTipoStrada() { return tipoStrada; }
    public String getNomeStrada() { return nomeStrada; }
    public String getNumeroCivico() { return numeroCivico; }
    public AmbitoTerritoriale getAmbitoTerritoriale() { return ambitoTerritoriale; }
    public String getProvincia() { return ambitoTerritoriale.getProvincia(); }
    public String getComune() { return ambitoTerritoriale.getComune(); }
    public int getCap() { return ambitoTerritoriale.getCap(); }

    // Setters
    public void setTipoStrada(TipoStrada tipoStrada) {
        Objects.requireNonNull(tipoStrada, "Tipo di strada non può essere null.");
        this.tipoStrada = tipoStrada;
    }
    public void setNomeStrada(String nomeStrada) {
        if (nomeStrada == null || nomeStrada.isBlank()) throw new IllegalArgumentException("Nome della strada non valido.");
        this.nomeStrada = nomeStrada;
    }
    public void setNumeroCivico(String numeroCivico) {
        if (numeroCivico == null || numeroCivico.isBlank()) throw new IllegalArgumentException("Numero civico non valido.");
        this.numeroCivico = numeroCivico;
    }
    public void setAmbitoTerritoriale(AmbitoTerritoriale ambitoTerritoriale) {
        Objects.requireNonNull(ambitoTerritoriale, "Ambito territoriale non può essere null.");
        this.ambitoTerritoriale = ambitoTerritoriale;
    }

    // Stampa
    public void stampaIndirizzo() {
        System.out.println("Tipo strada   : " + tipoStrada);
        System.out.println("Nome strada   : " + nomeStrada);
        System.out.println("Numero civico : " + numeroCivico);
        System.out.println("Comune        : " + getComune());
        System.out.println("Provincia     : " + getProvincia());
        System.out.println("CAP           : " + getCap());
    }

    @Override
    public String toString() {
        return String.format("%s %s, %s - %s (%s), CAP %d",
                tipoStrada.name(), nomeStrada, numeroCivico, getComune(), getProvincia(), getCap());
    }
}
