package Versione1;
import java.time.LocalDateTime;
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

	@Override
	public String toString() {
		return "ListaTipoVisita [tipi=" + tipi.toString() + "]";
	}
	
	
	public String toStringAsList () {
		String returnString = "";
		for (int i = 0; i < tipi.size(); i++) {
			returnString = returnString + (i+1) + ". "+ tipi.get(i).toString() + "\n"; 
		}
		return returnString;
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
	
    
}
