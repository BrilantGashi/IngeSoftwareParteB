package Versione1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AmbitoTerritoriale {
    
    private ArrayList<Integer> cap; 
    private String provincia;

    /**
     * Invariante di classe:
     * - provincia != null && !provincia.isBlank()
     * - cap != null
     * - tutti i valori in cap sono CAP validi (5 cifre, > 10000 e < 99999)
     */

    // Costruttore vuoto (per XML o framework di serializzazione)
    public AmbitoTerritoriale() {
        this.cap = new ArrayList<>();
        this.provincia = "";
    }

    /**
     * Costruttore con parametri
     * @param cap lista di CAP (non null, non contiene null, valori validi)
     * @param provincia nome della provincia (non null, non vuoto)
     * @throws IllegalArgumentException se i parametri non rispettano le precondizioni
     */
    public AmbitoTerritoriale(ArrayList<Integer> cap, String provincia) {
        if (cap == null) throw new IllegalArgumentException("La lista dei CAP non può essere null");
        if (provincia == null || provincia.isBlank()) throw new IllegalArgumentException("La provincia non può essere null o vuota");
        for (Integer c : cap) {
            if (!isCapValido(c)) throw new IllegalArgumentException("CAP non valido: " + c);
        }
        this.cap = new ArrayList<>(cap); // copia difensiva
        this.provincia = provincia;
        assert invClass() : "Invariante di classe violato nel costruttore";
    }

    /**
     * Aggiunge un CAP alla lista
     * Precondizione: capInput deve essere un CAP valido
     * Postcondizione: la lista contiene il nuovo CAP
     */
    public void addCap(int capInput) {
        if (!isCapValido(capInput)) throw new IllegalArgumentException("CAP non valido: " + capInput);
        this.cap.add(capInput);
        assert this.cap.contains(capInput) : "Postcondizione violata: il CAP non è stato aggiunto";
        assert invClass() : "Invariante di classe violato in addCap";
    }

    /**
     * Confronta se un indirizzo appartiene all'ambito
     * Precondizione: indirizzo != null
     * Postcondizione: ritorna true sse il CAP dell'indirizzo è contenuto nella lista
     */
    public boolean confronto(Indirizzo indirizzo) {
        Objects.requireNonNull(indirizzo, "L'indirizzo non può essere null");
        boolean result = cap.contains(indirizzo.getCap());
        assert (result == cap.contains(indirizzo.getCap())) : "Postcondizione violata in confronto";
        return result;
    }

    // Getters & Setters con pre/post condizioni

    public ArrayList<Integer> getCap() {
        return new ArrayList<>(cap); // copia difensiva
    }

    public String getProvincia() {
        return provincia;
    }
    
    public void setCap(ArrayList<Integer> cap) {
        this.cap = cap;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    

    public void stampaAmbito() {
        System.out.println("Provincia: " + provincia);
        if (!cap.isEmpty()) {
            System.out.print("CAP: ");
            for (Integer c : cap) {
                System.out.print(c + " ");
            }
            System.out.println();
        } else {
            System.out.println("Nessun CAP disponibile");
        }
    }

    @Override
    public String toString() {
        return String.format("L'ambito territoriale prescelto è la provincia di: %s", provincia);
    }

    private boolean isCapValido(Integer c) {
        return c != null && c >= 10000 && c <= 99999;
    }

    /**
     * Verifica l'invariante di classe
     */
    private boolean invClass() {
        if (provincia == null || provincia.isBlank()) return false;
        if (cap == null) return false;
        for (Integer c : cap) {
            if (!isCapValido(c)) return false;
        }
        return true;
    }
}
