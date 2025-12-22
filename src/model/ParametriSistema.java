package model;

public class ParametriSistema {
    private static ParametriSistema instance;

    private int nMaxAccompagnatori = 5; // Valore standard

    private boolean bloccato = false;   // True dopo la fase di configurazione

    private ParametriSistema() {}

    public static ParametriSistema getInstance() {
        if (instance == null) instance = new ParametriSistema();
        return instance;
    }

    /**
     * Setter del valore. Può essere chiamato solo prima del blocco, poi ignora.
     */
    public void setNMaxAccompagnatori(int valore) {
        if (!bloccato) this.nMaxAccompagnatori = valore;
    }

    /**
     * Blocca la modifica dei parametri (chiamato dal controller quando il sistema passa a stato produzione).
     */
    public void bloccaConfigurazione() {
        bloccato = true;
    }

    public int getNMaxAccompagnatori() {
        return nMaxAccompagnatori;
    }

    public boolean isBloccato() {
        return bloccato;
    }
}
