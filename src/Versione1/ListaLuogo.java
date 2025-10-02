package Versione1;

import java.util.ArrayList;

public class ListaLuogo {
	private ArrayList<Luogo> listaLuoghi;

	public ListaLuogo() {
		super();
		this.listaLuoghi = new ArrayList<>();
	}

	public ArrayList<Luogo> getListaLuoghi() {
		return listaLuoghi;
	}

	public void setListaLuoghi(ArrayList<Luogo> listaLuoghi) {
		this.listaLuoghi = listaLuoghi;
	}
	
	public void add(Luogo luogo) {
		listaLuoghi.add(luogo);
	}
	
	// Stampa senza visite
    public void stampaLuoghiSenzaTipoVisita() {
        if (listaLuoghi == null || listaLuoghi.isEmpty()) {
            System.out.println("Nessun luogo disponibile.");
            return;
        }

        StringBuilder sb = new StringBuilder("===== ELENCO LUOGHI =====\n");
        for (Luogo l : listaLuoghi) {
            sb.append(stampaLuogo(l, false));
        }
        System.out.println(sb.toString());
    }

    // Stampa con visite
    public void stampaLuoghiConTipoVisita() {
        if (listaLuoghi == null || listaLuoghi.isEmpty()) {
            System.out.println("Nessun luogo disponibile.");
            return;
        }

        StringBuilder sb = new StringBuilder("===== ELENCO LUOGHI =====\n");
        for (Luogo l : listaLuoghi) {
            sb.append(stampaLuogo(l, true));
        }
        System.out.println(sb.toString());
    }

    // Metodo privato di supporto per i metodi stampaLuoghi con/senza TipoVisita
    private String stampaLuogo(Luogo l, boolean conVisite) {
        StringBuilder sb = new StringBuilder();

        sb.append("Nome: ").append(l.getNomeLuogo()).append("\n");
        sb.append("Descrizione: ").append(l.getDescrizione()).append("\n");

        if (l.getIndirizzo() != null) {
            sb.append("Indirizzo: ").append(l.getIndirizzo().toString()).append("\n");
        } else {
            sb.append("Indirizzo: non disponibile\n");
        }

        // 🔹 Qui non serve più ciclare i tipi di visita manualmente:
        if (conVisite) {
            if (l.getListaTipoVisita() != null) {
                sb.append(l.getListaTipoVisita().toString()).append("\n");
            } else {
                sb.append("Nessun tipo di visita disponibile.\n");
            }
        }

        sb.append("-----------------------------------\n");
        return sb.toString();
    }

	
}




