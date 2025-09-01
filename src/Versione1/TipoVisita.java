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
    private ArrayList<Volontario> volontari;
	private Periodo periodoProgrammabile;		//COMMENTO LAIBA: nella consegna c'è esplicitamente scritto che il tipo visita deve 
												//contenere il periodo dell'anno durante il quale la visita è programmabile. In caso 
												//contrario, sarebbe difficile distinguere il tipo visita "astratto" dalla visita effettiva programmata
    
    public TipoVisita(String titolo, String descrizione, Indirizzo puntoDiIncontro, Data dataVisita, LocalTime oraInizio,
			int durataVisita, boolean biglietto, int sogliaMinPartecipanti, int sogliaMaxPartecipanti, Periodo periodoProgrammabile, ArrayList<Volontario> volontari) {
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

	//public Data getDataVisita() {
	//	return dataVisita;
	//}

	public LocalTime getOraInizio() {
		return oraInizio;
	}

	public int getDurataVisita() {
		return durataVisita;
	}

	@Override
	public String toString() {
		return "TipoVisita [titolo=" + titolo + ", descrizione=" + descrizione + ", puntoDiIncontro=" + puntoDiIncontro
				+ ", dataVisita=" + dataVisita.toString() +", oraInizio=" + oraInizio + ", durataVisita=" + durataVisita
				+ ", biglietto=" + biglietto + ", sogliaMinPartecipanti=" + sogliaMinPartecipanti
				+ ", sogliaMaxPartecipanti=" + sogliaMaxPartecipanti + ", periodoProgrammabile=" + periodoProgrammabile.toString()
				+ ", volontari=" + volontari.toString() + "]";
	}
    
    
    
}
