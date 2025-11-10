package Versione1;

import java.util.Objects;
import java.util.UUID;

public class Iscrizione {
    private String codicePrenotazione;
    private final Visita visitaEffettiva;
    private final Fruitore fruitore;              // Unico fruitore per questa iscrizione
    private int totalePartecipanti;        // Numero accompagnatori (escluso il fruitore principale)

    /**
     * Crea una iscrizione: fruitore singolo, visita e numero accompagnatori.
     * Genera automaticamente un codice di prenotazione univoco (UUID).
     */
    public Iscrizione(Visita visitaEffettiva, Fruitore fruitore, int totalePartecipanti) {
        this.codicePrenotazione = UUID.randomUUID().toString();
        this.visitaEffettiva = Objects.requireNonNull(visitaEffettiva, "visitaEffettiva non può essere null");
        this.fruitore = Objects.requireNonNull(fruitore, "fruitore non può essere null");
        if (totalePartecipanti < 0) {
            throw new IllegalArgumentException("Numero accompagnatori negativo non consentito");
        }
        this.totalePartecipanti = totalePartecipanti;
    }

    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }

    public Visita getVisitaEffettiva() {
        return visitaEffettiva;
    }

    public Fruitore getFruitore() {
        return fruitore;
    }

    public int getTotalePartecipanti() {
        return totalePartecipanti;
    }
    
    public void setCodicePrenotazione(String codice_prenotazione) {
    	this.codicePrenotazione = codice_prenotazione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Iscrizione)) return false;
        Iscrizione that = (Iscrizione) o;
        // Distingui una iscrizione per visita, fruitore e codice prenotazione
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
            ", fruitore=" + fruitore.getCredenziali().getUsername() +
            ", totalePartecipanti=" + totalePartecipanti +
            ", totalePartecipanti=" + getTotalePartecipanti() +
            '}';
    }
}
