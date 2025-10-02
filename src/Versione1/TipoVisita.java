package Versione1;
import java.time.LocalTime;
import java.util.ArrayList;

public class TipoVisita {
    
	private String titolo;
    private String descrizione;
    private Indirizzo puntoDiIncontro;
    private Data dataVisita;
    //private List<GiornoSettimana> giorniSettimana;
    private LocalTime oraInizio;
    private int durataVisita;
    private boolean biglietto;
    private int sogliaMinPartecipanti;
    private int sogliaMaxPartecipanti;
    private ListaVolontari volontari;
	private Periodo periodoProgrammabile;		//COMMENTO LAIBA: nella consegna c'� esplicitamente scritto che il tipo visita deve 
												//contenere il periodo dell'anno durante il quale la visita � programmabile. In caso 
												//contrario, sarebbe difficile distinguere il tipo visita "astratto" dalla visita effettiva programmata
    
    public TipoVisita(String titolo, String descrizione, Indirizzo puntoDiIncontro, Data dataVisita, LocalTime oraInizio,
			int durataVisita, boolean biglietto, int sogliaMinPartecipanti, int sogliaMaxPartecipanti, Periodo periodoProgrammabile, ListaVolontari volontari) {
		super();
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.puntoDiIncontro = puntoDiIncontro;
		this.dataVisita = dataVisita;
		this.oraInizio = oraInizio;
		this.durataVisita = durataVisita;
		this.biglietto = biglietto;
		this.sogliaMinPartecipanti = sogliaMinPartecipanti;
		this.sogliaMaxPartecipanti = sogliaMaxPartecipanti;
		this.periodoProgrammabile = periodoProgrammabile;
		this.volontari = volontari;
	}

    public TipoVisita() {}
	//public Data getDataVisita() {
	//	return dataVisita;
	//}

	public LocalTime getOraInizio() {
		return oraInizio;
	}

	public int getDurataVisita() {
		return durataVisita;
	}


	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Indirizzo getPuntoDiIncontro() {
		return puntoDiIncontro;
	}

	public void setPuntoDiIncontro(Indirizzo puntoDiIncontro) {
		this.puntoDiIncontro = puntoDiIncontro;
	}

	public Data getDataVisita() {
		return dataVisita;
	}

	public void setDataVisita(Data dataVisita) {
		this.dataVisita = dataVisita;
	}

	public boolean isBiglietto() {
		return biglietto;
	}

	public void setBiglietto(boolean biglietto) {
		this.biglietto = biglietto;
	}

	public int getSogliaMinPartecipanti() {
		return sogliaMinPartecipanti;
	}

	public void setSogliaMinPartecipanti(int sogliaMinPartecipanti) {
		this.sogliaMinPartecipanti = sogliaMinPartecipanti;
	}

	public int getSogliaMaxPartecipanti() {
		return sogliaMaxPartecipanti;
	}

	public void setSogliaMaxPartecipanti(int sogliaMaxPartecipanti) {
		this.sogliaMaxPartecipanti = sogliaMaxPartecipanti;
	}

	public ListaVolontari getListaVolontari() {
		return volontari;
	}

	public void setListaVolontari(ListaVolontari volontari) {
		this.volontari = volontari;
	}

	public Periodo getPeriodoProgrammabile() {
		return periodoProgrammabile;
	}

	public void setPeriodoProgrammabile(Periodo periodoProgrammabile) {
		this.periodoProgrammabile = periodoProgrammabile;
	}

	public void setOraInizio(LocalTime oraInizio) {
		this.oraInizio = oraInizio;
	}

	public void setDurataVisita(int durataVisita) {
		this.durataVisita = durataVisita;
	}
	
	public boolean siSovrappongono(TipoVisita altraVisita) {
        if (altraVisita == null) return false;

        LocalTime fineQuesta = this.oraInizio.plusMinutes(this.durataVisita);
        LocalTime fineAltra = altraVisita.oraInizio.plusMinutes(altraVisita.durataVisita);

        // Controllo sovrapposizione
        return this.oraInizio.isBefore(fineAltra) && altraVisita.oraInizio.isBefore(fineQuesta);
    }
    
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    sb.append("===== TIPO VISITA =====\n");
	    sb.append("Titolo: ").append(titolo != null ? titolo : "N/D").append("\n");
	    sb.append("Descrizione: ").append(descrizione != null ? descrizione : "N/D").append("\n");

	    sb.append("Data visita: ").append(dataVisita != null ? dataVisita.toString() : "N/D").append("\n");
	    sb.append("Ora inizio: ").append(oraInizio != null ? oraInizio.toString() : "N/D").append("\n");
	    sb.append("Durata (minuti): ").append(durataVisita).append("\n");

	    sb.append("Biglietto richiesto: ").append(biglietto ? "Sì" : "No").append("\n");
	    sb.append("Partecipanti: min=").append(sogliaMinPartecipanti)
	      .append(", max=").append(sogliaMaxPartecipanti).append("\n");

	    if (puntoDiIncontro != null) {
	        sb.append("Punto di incontro: ").append(puntoDiIncontro.toString()).append("\n");
	    }

	    if (periodoProgrammabile != null) {
	        sb.append("Periodo programmabile: ").append(periodoProgrammabile.toString()).append("\n");
	    }

	    // Volontari: se vuoi un output leggibile, cicliamo la lista
	    if (volontari != null && volontari.getVolontari() != null && !volontari.getVolontari().isEmpty()) {
	        sb.append("Volontari:\n");
	        for (Volontario v : volontari.getVolontari()) {
	            sb.append("   - ").append(v.getNickname()).append("\n");
	        }
	    } else {
	        sb.append("Volontari: nessuno assegnato\n");
	    }

	    sb.append("========================\n");

	    return sb.toString();
	}

    
    
}

