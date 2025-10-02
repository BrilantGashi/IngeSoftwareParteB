package Versione1;
import java.util.ArrayList;

public class ListaVolontari {

    private ArrayList<Volontario> volontari;

    // Costruttore
    public ListaVolontari() {
        this.volontari = new ArrayList<>();
    }

    // Getter
    public ArrayList<Volontario> getVolontari() {
        return volontari;
    }
    
    public Volontario getVolontario(int indice) {
    	return volontari.get(indice);
    }

    // Aggiungi un volontario
    public void aggiungiVolontario(Volontario v) {
        if (v != null && !volontari.contains(v)) {
            volontari.add(v);
        }
    }

    // Cerca un volontario per nickname
    public Volontario cercaPerNickname(String nickname) {
        for (Volontario v : volontari) {
            if (v.getNickname().equalsIgnoreCase(nickname)) {
                return v;
            }
        }
        return null; // non trovato
    }

    // Stampa tutti i volontari
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

    // Stampa i volontari con le visite a cui partecipano
    public void stampaVolontariConVisite(ArrayList<TipoVisita> tutteLeVisite) {
        if (volontari.isEmpty()) {
            System.out.println("Nessun volontario presente.");
            return;
        }

        for (Volontario v : volontari) {
            System.out.println("Volontario: " + v.getNickname());
            for (TipoVisita visita : tutteLeVisite) {
                if (visita.getListaVolontari().getVolontari().contains(v)) {
                    System.out.println("   - " + visita.getTitolo());                }
            }

            System.out.println("-----------------------------");
        }
    }

	

}

