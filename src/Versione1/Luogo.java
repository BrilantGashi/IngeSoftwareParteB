package Versione1;

public class Luogo {
	private String nomeLuogo;
	private String descrizione;
	private Indirizzo indirizzo;
	private ListaTipoVisita listaTipoVisita;		//LAIBA: aggiunto perch� (pg. 4, "A ciascun luogo � associato almeno un tipo di visita")
	
	public Luogo(String nomeLuogo, String descrizione, Indirizzo indirizzo, ListaTipoVisita listaTipoVisita) {
		this.nomeLuogo = nomeLuogo;
		this.descrizione = descrizione;
		this.indirizzo = indirizzo;
		this.listaTipoVisita = listaTipoVisita; 
		
	}
	
	public Luogo() {}


	
	//getters & setters
	
	public String getNomeLuogo() {
		return nomeLuogo;
	}

	public void setNomeLuogo(String nomeLuogo) {
		this.nomeLuogo = nomeLuogo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Indirizzo getIndirizzo() {
		return indirizzo;
	}

	public void setListaTipoVisita(ListaTipoVisita listaTipoVisita) {
		this.listaTipoVisita = listaTipoVisita;
	}

	public ListaTipoVisita getListaTipoVisita() {
		return listaTipoVisita;
	}

	public void setIndirizzo(Indirizzo indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    sb.append("===== LUOGO =====\n");
	    sb.append("Nome: ").append(nomeLuogo != null ? nomeLuogo : "N/D").append("\n");
	    sb.append("Descrizione: ").append(descrizione != null ? descrizione : "N/D").append("\n");

	    if (indirizzo != null) {
	        sb.append("Indirizzo: ").append(indirizzo.toString()).append("\n");
	    } else {
	        sb.append("Indirizzo: non disponibile\n");
	    }

	    if (listaTipoVisita != null && listaTipoVisita.getTipi() != null && !listaTipoVisita.getTipi().isEmpty()) {
	        sb.append(listaTipoVisita.toString()).append("\n");
	    } else {
	        sb.append("Nessun tipo di visita disponibile.\n");
	    }

	    sb.append("=================\n");

	    return sb.toString();
	}

	
}
