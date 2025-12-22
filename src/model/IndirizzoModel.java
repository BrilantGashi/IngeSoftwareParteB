package model;

import java.util.Objects;

/**
 * Model che rappresenta un indirizzo italiano.
 */
public class IndirizzoModel {

    private TipoStrada tipoStrada;         // Enum predefinito che rappresenta il tipo di strada
    private String nomeStrada;             // Nome della strada
    private String numeroCivico;           // Numero civico
    private AmbitoTerritorialeModel ambitoTerritoriale; // Oggetto per provincia, comune e CAP

    /**
     * Costruttore vuoto.
     */
    public IndirizzoModel() {
        this.tipoStrada = TipoStrada.Via; 
        this.nomeStrada = "";
        this.numeroCivico = "";
        this.ambitoTerritoriale = new AmbitoTerritorialeModel();
    }

    /**
     * Costruttore con parametri e validazione.
     */
    public IndirizzoModel(TipoStrada tipoStrada, String nomeStrada, String numeroCivico, AmbitoTerritorialeModel ambitoTerritoriale) {
        Objects.requireNonNull(tipoStrada, "TipoStrada non può essere null");
        if (nomeStrada == null || nomeStrada.isBlank())
            throw new IllegalArgumentException("Nome della strada non valido.");
        if (numeroCivico == null || numeroCivico.isBlank())
            throw new IllegalArgumentException("Numero civico non valido.");
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
    public AmbitoTerritorialeModel getAmbitoTerritoriale() { return ambitoTerritoriale; }
    public String getProvincia() { return ambitoTerritoriale.getProvincia(); }
    public String getComune() { return ambitoTerritoriale.getComune(); }
    public int getCap() { return ambitoTerritoriale.getCap(); }

    // Setters con validazione
    public void setTipoStrada(TipoStrada tipoStrada) {
        Objects.requireNonNull(tipoStrada, "TipoStrada non può essere null.");
        this.tipoStrada = tipoStrada;
    }
    public void setNomeStrada(String nomeStrada) {
        if (nomeStrada == null || nomeStrada.isBlank())
            throw new IllegalArgumentException("Nome della strada non valido.");
        this.nomeStrada = nomeStrada;
    }
    public void setNumeroCivico(String numeroCivico) {
        if (numeroCivico == null || numeroCivico.isBlank())
            throw new IllegalArgumentException("Numero civico non valido.");
        this.numeroCivico = numeroCivico;
    }
    public void setAmbitoTerritoriale(AmbitoTerritorialeModel ambitoTerritoriale) {
        Objects.requireNonNull(ambitoTerritoriale, "Ambito territoriale non può essere null.");
        this.ambitoTerritoriale = ambitoTerritoriale;
    }

    /**
     * toString per una rappresentazione testuale leggibile.
     */
    @Override
    public String toString() {
        return String.format("%s %s, %s - %s (%s), CAP %d",
                tipoStrada.name(), nomeStrada, numeroCivico,
                getComune(), getProvincia(), getCap());
    }

    /**
     * Invariante di classe.
     */
    private boolean invClass() {
        return tipoStrada != null
            && nomeStrada != null && !nomeStrada.isBlank()
            && numeroCivico != null && !numeroCivico.isBlank()
            && ambitoTerritoriale != null;
    }

    // (Eventuali metodi equals/hashCode facoltativi)
}
