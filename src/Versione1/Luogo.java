package Versione1;

public class Luogo {
	private String nomeLuogo;
	private String descrizione;
	private Indirizzo indirizzo;
	private ListaTipoVisita listaTipoVisita;		//LAIBA: aggiunto perchè (pg. 4, "A ciascun luogo è associato almeno un tipo di visita")
	
	public Luogo(String nomeLuogo, String descrizione, Indirizzo indirizzo, ListaTipoVisita listaTipoVisita) {
		this.nomeLuogo = nomeLuogo;
		this.descrizione = descrizione;
		this.indirizzo = indirizzo;
		this.listaTipoVisita = listaTipoVisita; 
		
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Luogo: \n");
		sb.append(String.format("Nome: %s\n", nomeLuogo));
		sb.append(String.format("Descrizione: %s\n", descrizione));
		sb.append(String.format("Indirizzo: %s\n", indirizzo.toString()));
		
		
		return sb.toString();
	}
	

	
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
	
	
	
}
