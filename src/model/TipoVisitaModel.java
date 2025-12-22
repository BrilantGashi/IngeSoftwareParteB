package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;


public class TipoVisitaModel {
    private boolean biglietto;
    private String descrizione;
    private int durataVisita;
    private ArrayList<GiorniModel> giorniSettimanaProgrammabile;
    private LocalTime oraInizio;
    private PeriodoModel periodoProgrammabile;
    private String puntoDiIncontro;
    private int sogliaMaxPartecipanti;
    private int sogliaMinPartecipanti;
    private String titolo;
    private ListaVolontariModel volontari;
    private boolean isAttivo;

    public TipoVisitaModel() {}

    public TipoVisitaModel(String titolo, String descrizione, String puntoDiIncontro, ArrayList<GiorniModel> giorniSettimanaProgrammabile,
                           LocalTime oraInizio, int durataVisita, boolean biglietto,
                           int sogliaMinPartecipanti, int sogliaMaxPartecipanti,
                           PeriodoModel periodoProgrammabile, ListaVolontariModel volontari) {
        if (titolo == null || titolo.isBlank()) throw new IllegalArgumentException("Titolo non valido");
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione non valida");
        if (puntoDiIncontro == null || puntoDiIncontro.isBlank()) throw new IllegalArgumentException("Punto di incontro non valido");
        Objects.requireNonNull(giorniSettimanaProgrammabile);
        Objects.requireNonNull(oraInizio);
        if (durataVisita <= 0) throw new IllegalArgumentException("Durata non valida");
        if (sogliaMinPartecipanti < 1) throw new IllegalArgumentException("Soglia minima non valida");
        if (sogliaMaxPartecipanti < sogliaMinPartecipanti) throw new IllegalArgumentException("Soglia massima non valida");
        Objects.requireNonNull(periodoProgrammabile);
        Objects.requireNonNull(volontari);

        this.titolo = titolo;
        this.descrizione = descrizione;
        this.puntoDiIncontro = puntoDiIncontro;
        this.giorniSettimanaProgrammabile = giorniSettimanaProgrammabile;
        this.oraInizio = oraInizio;
        this.durataVisita = durataVisita;
        this.biglietto = biglietto;
        this.sogliaMinPartecipanti = sogliaMinPartecipanti;
        this.sogliaMaxPartecipanti = sogliaMaxPartecipanti;
        this.periodoProgrammabile = periodoProgrammabile;
        this.volontari = volontari;
        this.isAttivo = true;
    }

    public String getTitolo() { return titolo; }
    public String getDescrizione() { return descrizione; }
    public String getPuntoDiIncontro() { return puntoDiIncontro; }
    public ArrayList<GiorniModel> getGiorniSettimanaProgrammabile() { return giorniSettimanaProgrammabile; }
    public LocalTime getOraInizio() { return oraInizio; }
    public int getDurataVisita() { return durataVisita; }
    public boolean isBiglietto() { return biglietto; }
    public int getSogliaMinPartecipanti() { return sogliaMinPartecipanti; }
    public int getSogliaMaxPartecipanti() { return sogliaMaxPartecipanti; }
    public PeriodoModel getPeriodoProgrammabile() { return periodoProgrammabile; }
    public ListaVolontariModel getListaVolontari() { return volontari; }
    public boolean isAttivo() { return isAttivo; }

    public void setAttivo(boolean attivo) { this.isAttivo = attivo; }
    public void setBiglietto(boolean biglietto) { this.biglietto = biglietto; }
    public void setDescrizione(String descrizione) {
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione non valida");
        this.descrizione = descrizione;
    }
    public void setDurataVisita(int durataVisita) {
        if (durataVisita <= 0) throw new IllegalArgumentException("Durata non valida");
        this.durataVisita = durataVisita;
    }
    public void setGiorniSettimanaProgrammabile(ArrayList<GiorniModel> giorniSettimana) {
        Objects.requireNonNull(giorniSettimana);
        this.giorniSettimanaProgrammabile = giorniSettimana;
    }
    public void setListaVolontari(ListaVolontariModel volontari) {
        Objects.requireNonNull(volontari);
        this.volontari = volontari;
    }
    public void setOraInizio(LocalTime oraInizio) {
        Objects.requireNonNull(oraInizio);
        this.oraInizio = oraInizio;
    }
    public void setPeriodoProgrammabile(PeriodoModel periodoProgrammabile) {
        Objects.requireNonNull(periodoProgrammabile);
        this.periodoProgrammabile = periodoProgrammabile;
    }
    public void setPuntoDiIncontro(String puntoDiIncontro) {
        if (puntoDiIncontro == null || puntoDiIncontro.isBlank()) throw new IllegalArgumentException("Punto di incontro non valido");
        this.puntoDiIncontro = puntoDiIncontro;
    }
    public void setSogliaMaxPartecipanti(int sogliaMaxPartecipanti) {
        if (sogliaMaxPartecipanti < this.sogliaMinPartecipanti) {
            throw new IllegalArgumentException("Soglia massima non valida");
        }
        this.sogliaMaxPartecipanti = sogliaMaxPartecipanti;
    }
    public void setSogliaMinPartecipanti(int sogliaMinPartecipanti) {
        if (sogliaMinPartecipanti < 1 || sogliaMinPartecipanti > this.sogliaMaxPartecipanti) {
            throw new IllegalArgumentException("Soglia minima non valida");
        }
        this.sogliaMinPartecipanti = sogliaMinPartecipanti;
    }
    public void setTitolo(String titolo) {
        if (titolo == null || titolo.isBlank()) throw new IllegalArgumentException("Titolo non valido");
        this.titolo = titolo;
    }

    public boolean volontarioTrovato(String username) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username non valido");
        return volontari != null && volontari.contienePerNickname(username);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("|=================================================|\n");
        sb.append("|                   TIPO VISITA                   |\n");
        sb.append("|=================================================|\n");
        sb.append("Titolo: ").append(titolo).append("\n");
        sb.append("Descrizione: ").append(descrizione).append("\n");
        sb.append("Biglietto richiesto: ").append(biglietto ? "Sì" : "No").append("\n");
        sb.append("Durata visita: ").append(durataVisita).append(" minuti\n");
        sb.append("Ora di inizio: ").append(oraInizio != null ? oraInizio.toString() : "Non specificata").append("\n");
        sb.append("Punto di incontro: ").append(puntoDiIncontro).append("\n");
        sb.append("Soglia partecipanti: min ").append(sogliaMinPartecipanti)
          .append(" - max ").append(sogliaMaxPartecipanti).append("\n");
        sb.append("Periodo programmabile:\n");
        if (periodoProgrammabile != null) {
            DataModel inizio = periodoProgrammabile.getDataInizio();
            DataModel fine = periodoProgrammabile.getDataFine();
            sb.append("  - Data inizio: ").append(inizio.getGiorno()).append("/")
              .append(inizio.getMese()).append("\n");
            sb.append("  - Data fine:   ").append(fine.getGiorno()).append("/")
              .append(fine.getMese()).append("\n");
        } else {
            sb.append("  - Non definito\n");
        }
        sb.append("Giorni disponibili: ");
        if (giorniSettimanaProgrammabile != null && !giorniSettimanaProgrammabile.isEmpty()) {
            for (int i = 0; i < giorniSettimanaProgrammabile.size(); i++) {
                sb.append(giorniSettimanaProgrammabile.get(i).name());
                if (i < giorniSettimanaProgrammabile.size() - 1) sb.append(", ");
            }
        } else {
            sb.append("Nessun giorno disponibile");
        }
        sb.append("\n");
        sb.append("Volontari assegnati: ");
        if (volontari != null && volontari.getVolontari() != null && !volontari.getVolontari().isEmpty()) {
            for (int i = 0; i < volontari.getVolontari().size(); i++) {
                sb.append(volontari.getVolontari().get(i).getNickname());
                if (i < volontari.getVolontari().size() - 1) sb.append(", ");
            }
        } else {
            sb.append("Nessun volontario assegnato");
        }
        sb.append("\n");
        sb.append("=================================================\n");
        return sb.toString();
    }
    
    public String toStringSemplificato() {
        StringBuilder sb = new StringBuilder();

        sb.append("Titolo: ").append(titolo);
          
        sb.append("\nDescrizione: ").append(descrizione).append("\n");

        sb.append("Giorni disponibili: ");
        if (giorniSettimanaProgrammabile != null && !giorniSettimanaProgrammabile.isEmpty()) {
            for (int i = 0; i < giorniSettimanaProgrammabile.size(); i++) {
                sb.append(giorniSettimanaProgrammabile.get(i).name());
                if (i < giorniSettimanaProgrammabile.size() - 1) sb.append(", ");
            }
        } else {
            sb.append("Nessun giorno disponibile");
        }
        sb.append("\n");

        sb.append("Volontari: ");
        if (volontari != null && volontari.getVolontari() != null) {
            ArrayList<VolontarioModel> lista = volontari.getVolontari();
            for (int i = 0; i < lista.size(); i++) {
                sb.append(lista.get(i).getNickname());
                if (i < lista.size() - 1) sb.append(", ");
            }
        } else {
            sb.append("Nessun volontario");
        }

        return sb.toString();
    }
}
