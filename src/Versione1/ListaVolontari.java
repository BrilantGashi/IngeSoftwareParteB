package Versione1;

import java.util.ArrayList;
import java.util.Objects;

public class ListaVolontari {

    private ArrayList<Volontario> volontari;

    /**
     * INVARIANTI DI CLASSE:
     * - volontari != null
     * - tutti gli elementi di volontari != null
     * - nessun duplicato in volontari
     * - ogni volontario ha un nickname non nullo e non vuoto
     */

    // Costruttore
    public ListaVolontari() {
        this.volontari = new ArrayList<>();
        assert invClass() : "Invariante violato nel costruttore";
    }

    /**
     * Aggiunge un volontario.
     * PRECONDIZIONE: v != null
     * POSTCONDIZIONE: se non era già presente, v ∈ volontari
     */
    public void aggiungiVolontario(Volontario v) {
        Objects.requireNonNull(v, "Il volontario non può essere null");
        if (!volontari.contains(v)) {
            volontari.add(v);
        }
        assert volontari.contains(v) : "Postcondizione violata: volontario non aggiunto";
        assert invClass() : "Invariante violato in aggiungiVolontario";
    }
    
    public void rimuoviVolontario (Volontario v) {
    	volontari.remove(v);
    }
    
    /**
     * Cerca un volontario per nickname.
     * PRECONDIZIONE: nickname != null && !nickname.isBlank()
     * POSTCONDIZIONE: ritorna un volontario con quel nickname oppure null se non trovato
     */
    public Volontario cercaPerNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname non valido");
        }
        for (Volontario v : volontari) {
            if (v.getNickname().equalsIgnoreCase(nickname)) {
                return v;
            }
        }
        return null;
    }
    
    public boolean contienePerNickname(String nickname) {
        for (Volontario v : volontari) {
            if (v.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Restituisce la lista dei volontari.
     * POSTCONDIZIONE: ritorna una lista non null (può essere vuota).
     */
    public ArrayList<Volontario> getVolontari() {
        return new ArrayList<>(volontari); // copia difensiva
    }

    /**
     * Restituisce il volontario all'indice specificato.
     * PRECONDIZIONE: indice compreso tra 0 e volontari.size()-1
     * POSTCONDIZIONE: ritorna volontari[indice]
     */
    public Volontario getVolontario(int indice) {
        if (indice < 0 || indice >= volontari.size()) {
            throw new IndexOutOfBoundsException("Indice non valido");
        }
        return volontari.get(indice);
    }
    
    /**
     * Trova i volontari che non sono associati ad alcuna visita.
     * PRECONDIZIONE: listaTipoVisita != null
     * POSTCONDIZIONE: ritorna lista di volontari ⊆ this.volontari
     */
    public ArrayList<Volontario> trovaVolontariSenzaVisite(ListaTipoVisita listaTipoVisita) {
        Objects.requireNonNull(listaTipoVisita, "La lista delle visite non può essere null");
        ArrayList<Volontario> orfani = new ArrayList<>();

        for (Volontario v : this.volontari) {
            boolean associato = false;
            for (TipoVisita visita : listaTipoVisita.getTipi()) {
                if (visita.getListaVolontari().getVolontari().contains(v)) {
                    associato = true;
                    break;
                }
            }
            if (!associato) {
                orfani.add(v);
            }
        }
        return orfani;
    }

    // ================== METODI DI STAMPA ==================

    public void stampaVolontari() {
        if (volontari.isEmpty()) {
            System.out.println("Nessun volontario presente.");
            return;
        }
        System.out.println("===== ELENCO VOLONTARI =====");
        for (Volontario v : volontari) {
            System.out.println("- " + v.getNickname());
        }
    }

    public void stampaVolontariConVisite(ArrayList<TipoVisita> tutteLeVisite) {
        Objects.requireNonNull(tutteLeVisite, "La lista delle visite non può essere null");
        if (volontari.isEmpty()) {
            System.out.println("Nessun volontario presente.");
            return;
        }
        for (Volontario v : volontari) {
            System.out.println("Volontario: " + v.getNickname());
            ArrayList<TipoVisita> visiteAssociate = new ArrayList<>();
            //System.out.println("VOLONTARIO IN ESAME: " + v.getNickname());
            for (TipoVisita visita : tutteLeVisite) {
            	//System.out.println("VISITA: "+ visita.getTitolo());
            	//System.out.println("VOLONTARI VISITA: "+ visita.getListaVolontari().stampaVolontari());
            	//visita.getListaVolontari().stampaVolontari();
            	//boolean tmpCheck = visita.getListaVolontari().getVolontari().contains(v);
            	boolean tmpCheck = visita.getListaVolontari().contienePerNickname(v.getNickname());
            	System.out.println(tmpCheck);
                if (tmpCheck) {
                    visiteAssociate.add(visita);
                }
            }
            if (visiteAssociate.isEmpty()) {
                System.out.println("   Nessuna visita associata.");
            } else {
                System.out.println("   Visite associate:");
                for (TipoVisita visita : visiteAssociate) {
                    System.out.println("      - " + visita.getTitolo());
                }
            }
            System.out.println("-----------------------------------");
        }
    }

    public void visualizzaVolontari() {
        System.out.println("Elenco Volontari disponibili:");
        for (int i = 0; i < volontari.size(); i++) {
            System.out.println((i+1) + ". " + volontari.get(i).getCredenziali().getUsername());
        }
    }
    
    public void visualizzaVolontariDisponibili(ArrayList<Volontario> volontariScelti) {
        Objects.requireNonNull(volontariScelti, "La lista dei volontari scelti non può essere null");
        for (int i = 0; i < volontari.size(); i++) {
            if (!volontariScelti.contains(volontari.get(i))) {
                System.out.println((i+1) + ". " + volontari.get(i).getCredenziali().getUsername());
            } else if (volontariScelti.size() == volontari.size()) {
                break;
            }
        }
    }

    // ================== INVARIANTE ==================
    private boolean invClass() {
        if (volontari == null) return false;
        for (Volontario v : volontari) {
            if (v == null) return false;
            if (v.getNickname() == null || v.getNickname().isBlank()) return false;
            if (volontari.indexOf(v) != volontari.lastIndexOf(v)) return false; // duplicato
        }
        return true;
    }
}
