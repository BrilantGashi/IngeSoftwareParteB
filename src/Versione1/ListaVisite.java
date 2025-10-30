package Versione1;

import java.util.ArrayList;
import java.util.Objects;

public class ListaVisite {
    
    private ArrayList<Visita> listaVisite;

    /**
     * INVARIANTI DI CLASSE:
     * - listaVisite != null
     * - tutti gli elementi di listaVisite != null
     * - non ci sono duplicati con stesso TipoVisita + stessa Data
     */

    public ListaVisite() {
        this.listaVisite = new ArrayList<>();
        assert invClass() : "Invariante violato nel costruttore";
    }
    
    /**
     * Aggiunge una visita alla lista.
     * PRECONDIZIONE: v != null
     * POSTCONDIZIONE: se non era già presente, v ∈ listaVisite
     */
    public void aggiungiVisita(Visita v) {
        Objects.requireNonNull(v, "La visita non può essere null");
        if (!listaVisite.contains(v)) {
            listaVisite.add(v);
        }
        assert listaVisite.contains(v) : "Postcondizione violata: visita non aggiunta";
        assert invClass() : "Invariante violato in aggiungiVisita";
    }
    
    /**
     * Filtra le visite per stato.
     * PRECONDIZIONE: stato != null
     * POSTCONDIZIONE: tutte le visite restituite hanno stato == parametro
     */
    public ArrayList<Visita> filtraPerStato(StatoVisita stato) {
        Objects.requireNonNull(stato, "Lo stato non può essere null");
        ArrayList<Visita> risultato = new ArrayList<>();
        for (Visita v : listaVisite) {
            if (v.getStatoVisita() == stato) {
                risultato.add(v);
            }
        }
        for (Visita v : risultato) {
            assert v.getStatoVisita() == stato : "Postcondizione violata in filtraPerStato";
        }
        return risultato;
    }
     
    /**
     * Filtra le visite per volontario.
     * PRECONDIZIONE: v != null
     * POSTCONDIZIONE: tutte le visite restituite contengono v
     */
    public ArrayList<Visita> filtraPerVolontario(Volontario v){
        Objects.requireNonNull(v, "Il volontario non può essere null");
        ArrayList<Visita> risultato = new ArrayList<>();
        for(Visita visita: listaVisite) {
            if(visita.getVolontariEffettivi().contains(v)) {
                risultato.add(visita);
            }
        }
        for (Visita visita : risultato) {
            assert visita.getVolontariEffettivi().contains(v) : "Postcondizione violata in filtraPerVolontario";
        }
        return risultato;
    }
      
    public ArrayList<Visita> getListaVisite() {
        return new ArrayList<>(listaVisite); // copia difensiva
    }

    /**
     * Rimuove una visita dalla lista.
     * PRECONDIZIONE: v != null
     * POSTCONDIZIONE: v non appartiene più a listaVisite
     */
    public void rimuoviVisita(Visita v) {
        Objects.requireNonNull(v, "La visita non può essere null");
        listaVisite.remove(v);
        assert !listaVisite.contains(v) : "Postcondizione violata in rimuoviVisita";
        assert invClass() : "Invariante violato in rimuoviVisita";
    }
      
    /**
     * Imposta la lista delle visite.
     * PRECONDIZIONE: listaVisite != null && non contiene null
     * POSTCONDIZIONE: this.listaVisite == listaVisite
     */
    public void setListaVisite(ArrayList<Visita> listaVisite) {
        Objects.requireNonNull(listaVisite, "La lista non può essere null");
        for (Visita v : listaVisite) {
            if (v == null) throw new IllegalArgumentException("La lista non può contenere elementi null");
        }
        this.listaVisite = new ArrayList<>(listaVisite);
        assert invClass() : "Invariante violato in setListaVisite";
    }

    /**
     * Aggiunge la disponibilità di un volontario per un certo tipo di visita in una data.
     * PRECONDIZIONI:
     * - v != null
     * - tipoVisita != null
     * - data != null
     * POSTCONDIZIONI:
     * - se esiste già una visita con stesso tipo e data, v ∈ volontariEffettivi di quella visita
     * - altrimenti, viene creata una nuova visita con v come primo volontario
     */
    public void aggiungiDisponibilita(Volontario v, TipoVisita tipoVisita, Data data) {
        Objects.requireNonNull(v, "Volontario non può essere null");
        Objects.requireNonNull(tipoVisita, "TipoVisita non può essere null");
        Objects.requireNonNull(data, "Data non può essere null");

        Visita esistente = trovaVisita(tipoVisita, data);
        if (esistente != null) {
            esistente.aggiungiVolontario(v);
        } else {
            Visita nuova = new Visita(tipoVisita, v, StatoVisita.PROPOSTA, data);
            listaVisite.add(nuova);
        }
        assert invClass() : "Invariante violato in aggiungiDisponibilita";
    }

    private Visita trovaVisita(TipoVisita tipoVisita, Data data) {
        for (Visita v : listaVisite) {
            if (v.getTipoVisita().equals(tipoVisita) &&
                v.getDataVisita().equals(data)) {
                return v;
            }
        }
        return null;
    }
        
    /**
     * Verifica che non ci siano visite duplicate (stesso TipoVisita + stessa Data).
     * POSTCONDIZIONE: ritorna true sse non ci sono duplicati
     */
    public boolean visiteUnichePerTipoEData() {
        for (int i = 0; i < listaVisite.size(); i++) {
            Visita v1 = listaVisite.get(i);
            for (int j = i + 1; j < listaVisite.size(); j++) {
                Visita v2 = listaVisite.get(j);
                if (v1.getTipoVisita().equals(v2.getTipoVisita()) &&
                    v1.getDataVisita().equals(v2.getDataVisita())) {
                    return false;
                }
            }
        }
        return true;
    }
      
    
   /* public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== LISTA VISITE =====\n");

        if (listaVisite.isEmpty()) {
            sb.append("Non ci sono visite programmate.\n");
        } else {
            for (int i = 0; i < listaVisite.size(); i++) {
                sb.append((i + 1)).append(". ").append(listaVisite.get(i).toString()).append("\n");
            }
        }

        sb.append("========================\n");
        return sb.toString();
    }*/
    
    //METODO PER LA STAMPA DELLO STATO DELLE VISITE
    public void stampaTitoloEStato() {
        if (listaVisite == null || listaVisite.isEmpty()) {
            System.out.println("Non ci sono visite registrate al momento.");
            return;
        }

        System.out.println("===== ELENCO VISITE E STATI =====");
        for (Visita v : listaVisite) {
            String titolo = (v.getTipoVisita() != null) ? v.getTipoVisita().getTitolo() : "Senza titolo";
            String stato = (v.getStatoVisita() != null) ? v.getStatoVisita().name() : "N/D";
            System.out.println("- " + titolo + " | Stato: " + stato);
        }
        System.out.println("=================================");
    }

   //STAMPA PIANO
    public void stampaPianoVisite() {
        System.out.println("\n===== PIANO DELLE VISITE =====");

        if (listaVisite == null || listaVisite.isEmpty()) {
            System.out.println("Nessuna visita pianificata per questo mese.");
            System.out.println("====================================\n");
            return;
        }

        for (Visita v : listaVisite) {
            System.out.println("Titolo visita   : " + v.getTipoVisita().getTitolo());
            System.out.println("Data            : " + v.getDataVisita().toString());
            System.out.println("Stato           : " + v.getStatoVisita());
            if (!v.getVolontariEffettivi().isEmpty()) {
                System.out.println("Volontario      : " + v.getVolontariEffettivi().get(0).getNickname());
            } else {
                System.out.println("Volontario      : Nessuno assegnato");
            }
            System.out.println("------------------------------------");
        }

    }
    
    
    private boolean invClass() {
        if (listaVisite == null) return false;
        for (Visita v : listaVisite) {
            if (v == null) return false;
        }
        return visiteUnichePerTipoEData();
    }
}
