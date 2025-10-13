package Versione1;

import java.util.ArrayList;

public class ListaVisite {
	
	private ArrayList<Visita> listaVisite;

	public ListaVisite(ArrayList<Visita> listaVisite) {
		this.listaVisite = listaVisite;
	}
	
	//aggiunta visita
	
	public void aggiungiVisita(Visita v) {
        if (v != null && !listaVisite.contains(v)) {
            listaVisite.add(v);
        }
    }
	
	//rimozione visita
	
	 public void rimuoviVisita(Visita v) {
	        listaVisite.remove(v);
	
	 }
	 
	 //cerca visite per stato
	 
	  public ArrayList<Visita> filtraPerStato(StatoVisita stato) {
	        ArrayList<Visita> risultato = new ArrayList<>();
	        for (Visita v : listaVisite) {
	            if (v.getStatoVisita() == stato) {
	                risultato.add(v);
	            }
	        }
	        
	        return risultato;
	  }
	  
	  
	  //cerca visite per volontario
	  
	  public ArrayList<Visita> filtraPerVolontario(Volontario v){
		  ArrayList<Visita> risultato = new ArrayList<>();
		  
		  if(v == null) return risultato;
		  
		  for(Visita visita: listaVisite) {
			  if(visita.getVolontariEffettivi().contains(v)) {
				  risultato.add(visita);
			  }
		  }
		  
		  return risultato;
	  }

	
	  //GETTERS & SETTERS
	  
	  public ArrayList<Visita> getListaVisite() {
			return listaVisite;
		}
	  
	  
	  public void setListaVisite(ArrayList<Visita> listaVisite) {
			this.listaVisite = listaVisite;
		}

	  
	@Override
	public String toString() {
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
	}
	  
		  
		

}
