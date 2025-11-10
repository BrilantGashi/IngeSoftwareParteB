package Versione1;

import java.util.Objects;

import it.unibs.fp.mylib.InputDati;

public class Data {
    private int giorno;
    private int mese;

    /**
     * INVARIANTI DI CLASSE:
     * - 1 <= mese <= 12
     * - 1 <= giorno <= giorniNelMese(mese)
     */

    // Costruttore vuoto (per XML o framework di serializzazione)
    public Data() {}

    /**
     * Costruttore con parametri
     * PRECONDIZIONI:
     * - mese compreso tra 1 e 12
     * - giorno compreso tra 1 e giorni del mese
     * POSTCONDIZIONI:
     * - this.giorno == giorno
     * - this.mese == mese
     */
    public Data(int giorno, int mese) {
        if (mese < 1 || mese > 12) {
            throw new IllegalArgumentException("Mese non valido: " + mese);
        }
        int giorniNelMese = giorniDelMese(mese);
        if (giorno < 1 || giorno > giorniNelMese) {
            throw new IllegalArgumentException("Giorno non valido per il mese " + mese + ": " + giorno);
        }
        this.giorno = giorno;
        this.mese = mese;

        assert invClass() : "Invariante violato nel costruttore";
    }

    public int getGiorno() {
        return giorno;
    }

    public int getMese() {
        return mese;
    }

    /**
     * Setter giorno
     * PRECONDIZIONE: 1 <= giorno <= giorniDelMese(this.mese)
     * POSTCONDIZIONE: this.giorno == giorno
     */
    public void setGiorno(int giorno) {
        int giorniNelMese = giorniDelMese(this.mese);
        if (giorno < 1 || giorno > giorniNelMese) {
            throw new IllegalArgumentException("Giorno non valido per il mese " + this.mese + ": " + giorno);
        }
        this.giorno = giorno;
        assert this.giorno == giorno : "Postcondizione violata in setGiorno";
        assert invClass() : "Invariante violato in setGiorno";
    }

    /**
     * Setter mese
     * PRECONDIZIONE: 1 <= mese <= 12
     * POSTCONDIZIONE: this.mese == mese e this.giorno rimane valido
     */
    public void setMese(int mese) {
        if (mese < 1 || mese > 12) {
            throw new IllegalArgumentException("Mese non valido: " + mese);
        }
        int giorniNelMese = giorniDelMese(mese);
        if (this.giorno > giorniNelMese) {
            throw new IllegalArgumentException("Il giorno " + this.giorno + " non è valido per il mese " + mese);
        }
        this.mese = mese;
        assert this.mese == mese : "Postcondizione violata in setMese";
        assert invClass() : "Invariante violato in setMese";
    }

    /**
     * Confronta se la dataInput precede questa data.
     * PRECONDIZIONE: dataInput != null
     * POSTCONDIZIONE: ritorna true sse (dataInput.mese < this.mese) 
     *                 oppure (stesso mese e dataInput.giorno < this.giorno)
     */
    public boolean precede(Data dataInput) {
        Objects.requireNonNull(dataInput, "La data di confronto non può essere null");

        boolean result = false;
        if (dataInput.mese < this.mese) {
            result = true;
        } else if (dataInput.mese == this.mese && dataInput.giorno < this.giorno) {
            result = true;
        }

        assert result == ((dataInput.mese < this.mese) ||
                         (dataInput.mese == this.mese && dataInput.giorno < this.giorno))
            : "Postcondizione violata in precede";

        return result;
    }
    
    public void scegliDataPerMese(int mese) {
		if (mese < 1 || mese > 12) {
			throw new IllegalArgumentException("Mese non valido: " + mese);
		}
		int giorniNelMese = giorniDelMese(mese);
		int giornoScelto = InputDati.leggiIntero("Scegli il giorno relativo a(1-" + giorniNelMese + "): ", 1, giorniNelMese);
		this.giorno = giornoScelto;
		this.mese = mese;
    }

    /**
     * Restituisce il numero di giorni del mese (non considera anni bisestili).
     */
    public int giorniDelMese(int mese) {
        switch (mese) {
            case 2: return 28; // semplificazione: non gestiamo bisestili
            case 4: case 6: case 9: case 11: return 30;
            default: return 31;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Data other = (Data) obj;
        return giorno == other.giorno && mese == other.mese; // aggiungi anno se serve
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(giorno, mese); // aggiungi anno se serve
    }
    
    @Override
    public String toString() {
        return giorno + "/" + Mesi.getNomeMese(mese);
    }

    private boolean invClass() {
        if (mese < 1 || mese > 12) return false;
        int giorniNelMese = giorniDelMese(mese);
        if (giorno < 1 || giorno > giorniNelMese) return false;
        return true;
    }

}
