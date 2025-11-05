package Versione1;

import java.util.ArrayList;
import java.util.Objects;

import it.unibs.fp.mylib.InputDati;

public class ListaLuogo {
    private ArrayList<Luogo> listaLuoghi;

    /**
     * INVARIANTI DI CLASSE:
     * - listaLuoghi != null
     * - tutti gli elementi di listaLuoghi != null
     */

    public ListaLuogo() {
        this.listaLuoghi = new ArrayList<>();
        assert invClass() : "Invariante violato nel costruttore";
    }

    /**
     * Aggiunge un luogo alla lista.
     * PRECONDIZIONE: luogo != null
     * POSTCONDIZIONE: listaLuoghi contiene luogo
     */
    public void add(Luogo luogo) {
        Objects.requireNonNull(luogo, "Il luogo non puo' essere null");
        listaLuoghi.add(luogo);
        assert listaLuoghi.contains(luogo) : "Postcondizione violata: luogo non aggiunto";
        assert invClass() : "Invariante violato in add";
    }

    /**
     * Restituisce la lista dei luoghi.
     * POSTCONDIZIONE: ritorna una lista non null (può essere vuota).
     */
    public ArrayList<Luogo> getListaLuoghi() {
        return new ArrayList<>(listaLuoghi); // copia difensiva
    }

    /**
     * Imposta la lista dei luoghi.
     * PRECONDIZIONE: listaLuoghi != null e non contiene null
     * POSTCONDIZIONE: this.listaLuoghi == listaLuoghi
     */
    public void setListaLuoghi(ArrayList<Luogo> listaLuoghi) {
        Objects.requireNonNull(listaLuoghi, "La lista non può essere null");
        for (Luogo l : listaLuoghi) {
            if (l == null) throw new IllegalArgumentException("La lista non può contenere elementi null");
        }
        this.listaLuoghi = new ArrayList<>(listaLuoghi);
        assert invClass() : "Invariante violato in setListaLuoghi";
    }
    
    
    public Luogo cercaPerNome(String titolo) {
        if (titolo == null || titolo.isBlank()) {
            throw new IllegalArgumentException("Username non valido");
        }
        //ListaTipoVisita tipiDisponibiliPerVolontario = new ListaTipoVisita();
        for (Luogo l : listaLuoghi) {
            if (l.getNomeLuogo().equals(titolo)) {
                return l;
            }
        }
        return null;
    }
    
    public void rimuovi (Luogo l) {
    	listaLuoghi.remove(l);
    }
    
    public Luogo selezionaLuogo() {
        if (listaLuoghi == null || listaLuoghi.isEmpty()) {
            System.out.println("Nessun luogo disponibile.");
            return null;
        }

        stampaElencoNumerato();
        int scelta = InputDati.leggiIntero("Scegli un luogo: ", 1, listaLuoghi.size());
        return listaLuoghi.get(scelta - 1);
    }

    // ================== METODI DI STAMPA ==================

    /**
     * Stampa i luoghi con i tipi di visita associati.
     * PRECONDIZIONE: listaLuoghi != null
     * POSTCONDIZIONE: stampa tutti i luoghi presenti (o messaggio se vuota).
     */
    public void stampaLuoghiConTipoVisita() {
        if (listaLuoghi == null || listaLuoghi.isEmpty()) {
            System.out.println("Nessun luogo disponibile.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Elenco luoghi\n");
        sb.append("-----------------------------------------------\n");

        for (Luogo l : listaLuoghi) {
            sb.append(stampaLuogo(l, true));
            sb.append("-----------------------------------------------\n"); // Separatore dopo ogni luogo
        }
        System.out.println(sb.toString());
    }

    
    

    /**
     * Stampa i luoghi senza i tipi di visita.
     * PRECONDIZIONE: listaLuoghi != null
     * POSTCONDIZIONE: stampa tutti i luoghi presenti (o messaggio se vuota).
     */
    public void stampaLuoghiSenzaTipoVisita() {
        if (listaLuoghi == null || listaLuoghi.isEmpty()) {
            System.out.println("Nessun luogo disponibile.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Elenco luoghi\n");
        sb.append("-----------------------------------------------\n");

        for (Luogo l : listaLuoghi) {
            sb.append(stampaLuogo(l, false));
            sb.append("-----------------------------------------------\n");  // Separatore dopo ogni luogo
        }
        System.out.println(sb.toString());
    }



    // Metodo privato di supporto
    private String stampaLuogo(Luogo l, boolean conVisite) {
        StringBuilder sb = new StringBuilder();

        sb.append("Nome        : ").append(l.getNomeLuogo()).append("\n");
        sb.append("Descrizione : ").append(l.getDescrizione()).append("\n");

        if (l.getIndirizzo() != null) {
            sb.append("Indirizzo   : ").append(l.getIndirizzo().toString()).append("\n");
        }

        if (conVisite) {
            sb.append("Tipi visita associati:\n");
            if (l.getListaTipoVisita() != null && !l.getListaTipoVisita().getTipi().isEmpty()) {
                for (TipoVisita tv : l.getListaTipoVisita().getTipi()) {
                    sb.append("\tTitolo      : ").append(tv.getTitolo()).append("\n");
                    sb.append("\tDescrizione : ").append(tv.getDescrizione()).append("\n");
                    // Altri attributi, sempre indentati
                }
            } else {
                sb.append("\tNessun tipo di visita disponibile.\n");
            }
        }
        // La linea separatrice (più o meno lunga a piacere)
        sb.append("-----------------------------------------------\n");
        return sb.toString();
    }

    
    /**
     * Stampa l'elenco dei luoghi in formato numerato.
     * PRECONDIZIONE: listaLuoghi != null
     * POSTCONDIZIONE: stampa tutti i luoghi numerati (o messaggio se vuota).
     */
    public void stampaElencoNumerato() {
        if (listaLuoghi == null || listaLuoghi.isEmpty()) {
            System.out.println("Nessun luogo disponibile.");
            return;
        }

        System.out.println("===== ELENCO LUOGHI =====");
        for (int i = 0; i < listaLuoghi.size(); i++) {
            Luogo l = listaLuoghi.get(i);
            System.out.println((i + 1) + ") " + l.getNomeLuogo());
        }
    }
    
    /**
     * Stampa l'elenco numerato e permette la selezione di un luogo.
     * PRECONDIZIONE: listaLuoghi non vuota
     * POSTCONDIZIONE: ritorna il Luogo selezionato o null se lista vuota
     */
    


    // ================== INVARIANTE ==================
    private boolean invClass() {
        if (listaLuoghi == null) return false;
        for (Luogo l : listaLuoghi) {
            if (l == null) return false;
        }
        return true;
    }
    
    public boolean contains(Luogo luogo) {
        return listaLuoghi.contains(luogo);
    }
}
