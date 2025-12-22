package model;

import java.util.Objects;
import java.util.UUID;

public class IscrizioneModel {
    private String codicePrenotazione;
    private final VisitaModel visitaEffettiva;
    private final FruitoreModel fruitore;
    private int totalePartecipanti;

    public IscrizioneModel(VisitaModel visitaEffettiva, FruitoreModel fruitore, int totalePartecipanti) {
        this.codicePrenotazione = UUID.randomUUID().toString();
        this.visitaEffettiva = Objects.requireNonNull(visitaEffettiva, "visitaEffettiva non può essere null");
        this.fruitore = Objects.requireNonNull(fruitore, "fruitore non può essere null");
        if (totalePartecipanti < 0) throw new IllegalArgumentException("Numero accompagnatori negativo non consentito");
        this.totalePartecipanti = totalePartecipanti;
        if (!invClass()) throw new IllegalStateException("Invariante di classe violato");
    }

    public String getCodicePrenotazione() { return codicePrenotazione; }
    public VisitaModel getVisitaEffettiva() { return visitaEffettiva; }
    public FruitoreModel getFruitore() { return fruitore; }
    public int getTotalePartecipanti() { return totalePartecipanti; }

    public void setCodicePrenotazione(String codicePrenotazione) {
        if (codicePrenotazione == null) throw new IllegalArgumentException("Codice prenotazione non può essere null");
        this.codicePrenotazione = codicePrenotazione;
        if (!invClass()) throw new IllegalStateException("Invariante violato");
    }
    


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IscrizioneModel)) return false;
        IscrizioneModel that = (IscrizioneModel) o;
        return Objects.equals(visitaEffettiva, that.visitaEffettiva)
            && Objects.equals(fruitore, that.fruitore)
            && Objects.equals(codicePrenotazione, that.codicePrenotazione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitaEffettiva, fruitore, codicePrenotazione);
    }

    @Override
    public String toString() {
        return "Iscrizione{" +
            "codicePrenotazione='" + codicePrenotazione + '\'' +
            ", visitaEffettiva=" + visitaEffettiva +
            ", fruitore=" + (fruitore != null ? fruitore.getCredenziali().getUsername() : "null") +
            ", totalePartecipanti=" + totalePartecipanti +
            '}';
    }

    private boolean invClass() {
        return codicePrenotazione != null && visitaEffettiva != null && fruitore != null && totalePartecipanti >= 0;
    }
}
