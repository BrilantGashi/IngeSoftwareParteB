package model;

import java.util.Objects;

public class DataModel {
    private int giorno;
    private int mese;

    public DataModel() {}

    public DataModel(int giorno, int mese) {
        if (mese < 1 || mese > 12) {
            throw new IllegalArgumentException("Mese non valido: " + mese);
        }
        int giorniNelMese = giorniDelMese(mese);
        if (giorno < 1 || giorno > giorniNelMese) {
            throw new IllegalArgumentException("Giorno non valido per il mese " + mese + ": " + giorno);
        }
        this.giorno = giorno;
        this.mese = mese;
        if (!invClass()) throw new IllegalStateException("Invariante violato");
    }

    public int getGiorno() { return giorno; }
    public int getMese() { return mese; }

    public void setGiorno(int giorno) {
        int giorniNelMese = giorniDelMese(this.mese);
        if (giorno < 1 || giorno > giorniNelMese) {
            throw new IllegalArgumentException("Giorno non valido per il mese " + this.mese + ": " + giorno);
        }
        this.giorno = giorno;
        if (!invClass()) throw new IllegalStateException("Invariante violato");
    }

    public void setMese(int mese) {
        if (mese < 1 || mese > 12) {
            throw new IllegalArgumentException("Mese non valido: " + mese);
        }
        int giorniNelMese = giorniDelMese(mese);
        if (this.giorno > giorniNelMese) {
            throw new IllegalArgumentException("Il giorno " + this.giorno + " non è valido per il mese " + mese);
        }
        this.mese = mese;
        if (!invClass()) throw new IllegalStateException("Invariante violato");
    }

    public boolean precede(DataModel dataInput) {
        Objects.requireNonNull(dataInput, "La data di confronto non può essere null");
        if (dataInput.mese < this.mese) return true;
        return (dataInput.mese == this.mese && dataInput.giorno < this.giorno);
    }

    public int giorniDelMese(int mese) {
        switch (mese) {
            case 2: return 28;
            case 4: case 6: case 9: case 11: return 30;
            default: return 31;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        DataModel other = (DataModel) obj;
        return giorno == other.giorno && mese == other.mese;
    }

    @Override
    public int hashCode() {
        return Objects.hash(giorno, mese);
    }

    @Override
    public String toString() {
        return this.giorno + "/" + this.mese;
    }

    private boolean invClass() {
        if (mese < 1 || mese > 12) return false;
        int giorniNelMese = giorniDelMese(mese);
        return giorno >= 1 && giorno <= giorniNelMese;
    }
}
