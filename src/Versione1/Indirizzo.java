package Versione1;

public class Indirizzo {
	private TipoStrada tipoStrada;
	private String nomeStrada;
	private String numeroCivico;
	private int cap;
	private String comune;
	private String provincia;
	
	public Indirizzo(TipoStrada tipoStrada, String nomeStrada, String numeroCivico, int cap, String comune,
			String provincia) {
		this.tipoStrada = tipoStrada;
		this.nomeStrada = nomeStrada;
		this.numeroCivico = numeroCivico;
		this.cap = cap;
		this.comune = comune;
		this.provincia = provincia;
	}
	
	public Indirizzo() {}
	
	
	
	
	

	//getters & setters

	public TipoStrada getTipoStrada() {
		return tipoStrada;
	}

	public void setTipoStrada(TipoStrada tipoStrada) {
		this.tipoStrada = tipoStrada;
	}

	public String getNomeStrada() {
		return nomeStrada;
	}

	public void setNomeStrada(String nomeStrada) {
		this.nomeStrada = nomeStrada;
	}

	public String getNumeroCivico() {
		return numeroCivico;
	}

	public void setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
	}

	public Integer getCap() {
		return cap;
	}

	public void setCap(int cap) {
		this.cap = cap;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	public void stampaIndirizzo() {
	    System.out.println("Tipo strada: " + tipoStrada);
	    System.out.println("Nome strada: " + nomeStrada);
	    System.out.println("Numero civico: " + numeroCivico);
	    System.out.println("CAP: " + cap);
	    System.out.println("Comune: " + comune);
	    System.out.println("Provincia: " + provincia);
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    if (tipoStrada != null) {
	        sb.append(tipoStrada.toString()).append(" ");
	    }
	    if (nomeStrada != null) {
	        sb.append(nomeStrada).append(" ");
	    }
	    if (numeroCivico != null) {
	        sb.append(numeroCivico).append(", ");
	    }

	    sb.append(cap).append(" ");

	    if (comune != null) {
	        sb.append(comune).append(" ");
	    }
	    if (provincia != null) {
	        sb.append("(").append(provincia).append(")");
	    }

	    return sb.toString().trim();
	}

}