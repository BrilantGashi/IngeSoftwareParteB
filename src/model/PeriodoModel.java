package model;

import java.util.Objects;

public class PeriodoModel {
    private DataModel dataInizio;
    private DataModel dataFine;

    public PeriodoModel() {}

    public PeriodoModel(DataModel dataInizio, DataModel dataFine) {
        Objects.requireNonNull(dataInizio, "Data di inizio non può essere null");
        Objects.requireNonNull(dataFine, "Data di fine non può essere null");
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    public DataModel getDataFine() {
        return dataFine;
    }

    public DataModel getDataInizio() {
        return dataInizio;
    }

    public void setDataFine(DataModel dataFine) {
        Objects.requireNonNull(dataFine, "La data di fine non può essere null");
        if (this.dataInizio != null && dataFine.precede(this.dataInizio)) {
            throw new IllegalArgumentException("La data di fine non può precedere la data di inizio");
        }
        this.dataFine = dataFine;
    }

    public void setDataInizio(DataModel dataInizio) {
        Objects.requireNonNull(dataInizio, "La data di inizio non può essere null");
        if (this.dataFine != null && this.dataFine.precede(dataInizio)) {
            throw new IllegalArgumentException("La data di inizio non può essere successiva alla data di fine");
        }
        this.dataInizio = dataInizio;
    }
    
    public boolean siIntersecaCon(PeriodoModel altro) {
        Objects.requireNonNull(altro, "Il periodo di confronto non può essere null");
        Objects.requireNonNull(this.dataInizio, "Data inizio non può essere null");
        Objects.requireNonNull(this.dataFine, "Data fine non può essere null");
        Objects.requireNonNull(altro.dataInizio, "Data inizio del periodo di confronto non può essere null");
        Objects.requireNonNull(altro.dataFine, "Data fine del periodo di confronto non può essere null");
        boolean thisAttraversaCapodanno = this.dataFine.precede(this.dataInizio);
        boolean altroAttraversaCapodanno = altro.dataFine.precede(altro.dataInizio);

        if (!thisAttraversaCapodanno && !altroAttraversaCapodanno) {
            return !(this.dataFine.precede(altro.dataInizio) || altro.dataFine.precede(this.dataInizio));
        }
        if (thisAttraversaCapodanno && altroAttraversaCapodanno) {
            return true;
        }
        if (thisAttraversaCapodanno && !altroAttraversaCapodanno) {
            return !altro.dataInizio.precede(this.dataFine) || !this.dataInizio.precede(altro.dataFine);
        }
        if (!thisAttraversaCapodanno && altroAttraversaCapodanno) {
            return !this.dataInizio.precede(altro.dataFine) || !altro.dataInizio.precede(this.dataFine);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Periodo valido: " + dataInizio.toString() + " a " + dataFine.toString();
    }
}
