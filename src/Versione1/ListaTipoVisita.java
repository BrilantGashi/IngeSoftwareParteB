package Versione1;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class ListaTipoVisita {
	private ArrayList<TipoVisita> tipi;

	public ListaTipoVisita() {
		this.tipi = new ArrayList <TipoVisita>();
	}
	
	
    /*public boolean siSovrappongono(TipoVisita a, TipoVisita b) {
    	//mi manca la parte del periodo di Roxy, poi la sistemiamo così abbiamo il codice
    	//corretto scritto direttamente alla fine
        //return inizioA.isBefore(fineB) && inizioB.isBefore(fineA);
    }*/
    /*public boolean aggiungiTIpoVisita(TipoVisita nuova) {
        for (TipoVisita esistente : tipi) {
            if (siSovrappongono(esistente, nuova)) {
                return false; // effettuiamo il controllo ancora prima di aggiungerla per evitare sovrapposizioni
            }
        }
        tipi.add(nuova);
        return true;
    }*/
	
	public void add(TipoVisita tipoVisita) {
		tipi.add(tipoVisita);
	}
	

	public ArrayList<TipoVisita> getTipi() {
		return tipi;
	}

	public void setTipi(ArrayList<TipoVisita> tipi) {
		this.tipi = tipi;
	}
	
	
	public TipoVisita getTipo(int i) {
		return tipi.get(i);
	}
	
	@Override
	public String toString() {
	    if (tipi == null || tipi.isEmpty()) {
	        return "Nessun tipo di visita disponibile.";
	    }

	    StringBuilder sb = new StringBuilder();
	    sb.append("===== ELENCO TIPI DI VISITA =====\n");

	    int i = 1;
	    for (TipoVisita tv : tipi) {
	        sb.append(i++).append(") ").append(tv.getTitolo() != null ? tv.getTitolo() : "Senza titolo").append("\n");

	        sb.append("   Descrizione: ")
	          .append(tv.getDescrizione() != null ? tv.getDescrizione() : "N/D")
	          .append("\n");

	        sb.append("   Data visita: ")
	          .append(tv.getDataVisita() != null ? tv.getDataVisita().toString() : "N/D")
	          .append("\n");

	        sb.append("   Ora inizio: ")
	          .append(tv.getOraInizio() != null ? tv.getOraInizio().toString() : "N/D")
	          .append("\n");

	        sb.append("   Durata (minuti): ").append(tv.getDurataVisita()).append("\n");

	        sb.append("   Biglietto richiesto: ").append(tv.isBiglietto() ? "Sì" : "No").append("\n");

	        sb.append("   Partecipanti: min=")
	          .append(tv.getSogliaMinPartecipanti())
	          .append(", max=")
	          .append(tv.getSogliaMaxPartecipanti())
	          .append("\n");

	        // Punto di incontro (uso toString() di Indirizzo se implementato)
	        if (tv.getPuntoDiIncontro() != null) {
	            sb.append("   Punto di incontro: ").append(tv.getPuntoDiIncontro().toString()).append("\n");
	        }

	        // Periodo programmabile
	        if (tv.getPeriodoProgrammabile() != null) {
	            sb.append("   Periodo: ").append(tv.getPeriodoProgrammabile().toString()).append("\n");
	        }

	        // Volontari (solo se presenti)
	        if (tv.getListaVolontari() != null && !tv.getListaVolontari().getVolontari().isEmpty()) {
	            sb.append("   Volontari:\n");
	            // qui potresti usare tv.getVolontari().stampaVolontari() se vuoi stampare direttamente
	            for (Volontario v : tv.getListaVolontari().getVolontari()) {
	                sb.append("      - ").append(v.getNickname()).append("\n");
	            }
	        }

	        sb.append("-----------------------------------\n");
	    }

	    return sb.toString();
	}

	public String toStringAsList () {
		String returnString = "";
		for (int i = 0; i < tipi.size(); i++) {
			returnString = returnString + (i+1) + ". "+ tipi.get(i).toString() + "\n"; 
		}
		return returnString;
	}
}
