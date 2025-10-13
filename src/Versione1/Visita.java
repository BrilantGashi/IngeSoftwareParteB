package Versione1;

import java.util.ArrayList;

public class Visita {

	private TipoVisita tipoVisita;
	private ArrayList<Volontario> volontariEffettivi;
	private StatoVisita statoVisita;
	private Data dataVisita;

	
	//Costruttore vuoto
	//Utile per la creazione della visita passo-passo con inserimento dati man mano
	//Imposta lo stato di default a PROPOSTA
	 
	
	public Visita() {
		this.volontariEffettivi = new ArrayList<>();
		this.statoVisita = statoVisita.PROPOSTA;
	}
	
	//costruttore completo
	
	public Visita(TipoVisita tipoVisita, ArrayList<Volontario> volontariEffettivi, StatoVisita statoVisita,
			Data dataVisita) {
		this.tipoVisita = tipoVisita;
		this.volontariEffettivi = volontariEffettivi;
		this.statoVisita = statoVisita;
		this.dataVisita = dataVisita;
		
		
	}
	
	// aggiorna lo stato della visita 
	
	public void aggiornaStato(StatoVisita nuovoStato) {
		if(nuovoStato != null) {
			this.statoVisita = nuovoStato;
		}
	}

	//aggiunta volontario alla visita
	
	public void aggiungiVolontario(Volontario v) {
		if(v != null && !volontariEffettivi.contains(v)) {
			volontariEffettivi.add(v);
		}
	}
	
	//rimozione volontario dalla visita
	
	public void rimuoviVolontario(Volontario v) {
		volontariEffettivi.remove(v);
	}
	
	
	
	//GETTERS & SETTERS
	
	public TipoVisita getTipoVisita() {
		return tipoVisita;
	}


	public void setTipoVisita(TipoVisita tipoVisita) {
		this.tipoVisita = tipoVisita;
	}


	public ArrayList<Volontario> getVolontariEffettivi() {
		return volontariEffettivi;
	}


	public void setVolontariEffettivi(ArrayList<Volontario> volontariEffettivi) {
		this.volontariEffettivi = volontariEffettivi;
	}


	public StatoVisita getStatoVisita() {
		return statoVisita;
	}


	public void setStatoVisita(StatoVisita statoVisita) {
		this.statoVisita = statoVisita;
	}


	public Data getDataVisita() {
		return dataVisita;
	}


	public void setDataVisita(Data dataVisita) {
		this.dataVisita = dataVisita;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("======VISITA=====\n");
		sb.append("Tipo Visita: ").append(tipoVisita != null ? tipoVisita.getTitolo() : "N/D").append("\n");
        sb.append("Data: ").append(dataVisita != null ? dataVisita.toString() : "N/D").append("\n");
        sb.append("Stato: ").append(statoVisita).append("\n");
        sb.append("Volontari Effettivi: ");
        if (volontariEffettivi != null && !volontariEffettivi.isEmpty()) {
            for (Volontario v : volontariEffettivi) {
                sb.append(v.getNickname()).append(" ");
            }
        } else {
            sb.append("Non ci sono volontari disponibili");
        }
        sb.append("\n=================\n");
        return sb.toString();
    }
		
	

}
	
	
	






