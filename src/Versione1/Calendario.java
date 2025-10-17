package Versione1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unibs.fp.mylib.InputDati;

public class Calendario {
    
    private LocalDate dataCorrente;
    private ArrayList<Integer> datePrecluse = new ArrayList<>();
    private String meseCorrente;
    private String meseDatePrecluse;
    private String meseI1;
    private String meseI2;
    private LocalDate oggi = LocalDate.now();

    /**
     * INVARIANTI DI CLASSE:
     * - oggi != null
     * - dataCorrente != null dopo setMesi()
     * - datePrecluse != null
     * - Ogni giorno in datePrecluse è valido (1 <= giorno <= giorni del mese di riferimento)
     * - I campi meseCorrente, meseI1, meseI2, meseDatePrecluse non sono mai null dopo setMesi()
     */

    public Calendario() {       
        setMesi();
        assert invClass() : "Invariante violato nel costruttore";
    }

    /**
     * Aggiunge un giorno alla lista delle date precluse.
     * PRECONDIZIONE: giornoEscluso deve essere compreso tra 1 e giorni del mese target.
     * POSTCONDIZIONE: datePrecluse contiene giornoEscluso.
     */
    public void aggiungiDataPreclusa(int giornoEscluso) {
        LocalDate meseTarget = dataCorrente.plusMonths(3); // mese delle date precluse
        int giorniNelMese = meseTarget.lengthOfMonth();

        if (giornoEscluso < 1 || giornoEscluso > giorniNelMese) {
            throw new IllegalArgumentException(
                "Giorno non valido per il mese di " + meseTarget.getMonth() + ": " + giornoEscluso
            );
        }

        datePrecluse.add(giornoEscluso);

        assert datePrecluse.contains(giornoEscluso) : "Postcondizione violata: giorno non aggiunto";
        assert invClass() : "Invariante violato in aggiungiDataPreclusa";
    }

    /**
     * Setter con controllo coerente con il mese target.
     */
    public void setDatePrecluse(ArrayList<Integer> datePrecluse) {
        Objects.requireNonNull(datePrecluse, "La lista delle date precluse non può essere null");

        LocalDate meseTarget = dataCorrente.plusMonths(3);
        int giorniNelMese = meseTarget.lengthOfMonth();

        for (Integer g : datePrecluse) {
            if (g == null || g < 1 || g > giorniNelMese) {
                throw new IllegalArgumentException(
                    "Data non valida per il mese di " + meseTarget.getMonth() + ": " + g
                );
            }
        }

        this.datePrecluse = new ArrayList<>(datePrecluse);
        assert invClass() : "Invariante violato in setDatePrecluse";
    }


    /**
     * Controlla se un giorno è precluso.
     * PRECONDIZIONE: giorno >= 1 && giorno <= 31
     * POSTCONDIZIONE: ritorna true sse giorno ∈ datePrecluse
     */
    public boolean checkData(int giorno) {
        if (giorno < 1 || giorno > 31) {
            throw new IllegalArgumentException("Giorno non valido: " + giorno);
        }
        boolean result = datePrecluse.contains(giorno);
        assert result == datePrecluse.contains(giorno) : "Postcondizione violata in checkData";
        return result;
    }

    public ArrayList<Integer> getDatePrecluse() {
        return new ArrayList<>(datePrecluse); // copia difensiva
    }

    public String getMeseDatePrecluse() {
        return meseDatePrecluse;
    }

    public LocalDate getOggi() {
        return oggi;
    }

    public void printMesi() {
        System.out.println("Mese i: " + meseCorrente);
        System.out.println("Mese i+1: " + meseI1);
        System.out.println("Mese i+2: " + meseI2);
        System.out.println("Mese i+3: " + meseDatePrecluse);
    }


    public void setMeseDatePrecluse(String meseDatePrecluse) {
        if (meseDatePrecluse == null || meseDatePrecluse.isBlank()) {
            throw new IllegalArgumentException("Il mese delle date precluse non può essere null o vuoto");
        }
        this.meseDatePrecluse = meseDatePrecluse;
        assert invClass() : "Invariante violato in setMeseDatePrecluse";
    }

    /**
     * Calcola i mesi di riferimento a partire da 'oggi'.
     * POSTCONDIZIONE: dataCorrente != null, meseCorrente, meseI1, meseI2, meseDatePrecluse != null
     */
    public void setMesi() {
        if (oggi.getDayOfMonth() < 16) {
            dataCorrente = oggi.withDayOfMonth(16);
        } else {
            dataCorrente = oggi.plusMonths(1).withDayOfMonth(16);
        }

        meseCorrente = Mesi.getNomeMese(dataCorrente.getMonthValue());
        meseI1 = Mesi.getNomeMese(dataCorrente.plusMonths(1).getMonthValue());
        meseI2 = Mesi.getNomeMese(dataCorrente.plusMonths(2).getMonthValue());
        meseDatePrecluse = Mesi.getNomeMese(dataCorrente.plusMonths(3).getMonthValue());

        assert dataCorrente != null : "Postcondizione violata: dataCorrente null";
        assert meseCorrente != null && meseI1 != null && meseI2 != null && meseDatePrecluse != null : "Postcondizione violata: mesi non inizializzati";
        assert invClass() : "Invariante violato in setMesi";
    }

    public void setOggi(LocalDate oggi) {
        Objects.requireNonNull(oggi, "oggi non può essere null");
        this.oggi = oggi;
        assert invClass() : "Invariante violato in setOggi";
    }

    public String toStringDatePrecluse() {
        StringBuilder sb = new StringBuilder();
        if (datePrecluse.isEmpty()) {
            sb.append("Non sono state inserite date da escludere per il mese di ").append(meseDatePrecluse).append(" =====\n");
        } else {
            sb.append("===== DATE ESCLUSE PER IL MESE DI ").append(meseDatePrecluse).append(" =====\n");
            for (int giorno : datePrecluse) {
                sb.append(giorno).append("\n");
            }
            sb.append("=================\n");
        }
        return sb.toString();
    }

    public String getMeseI1() {
        return meseI1;
    }

    public String getMeseI2() {
        return meseI2;
    }

    /**
     * Metodo che permette di scegliere una data disponibile per un certo tipo di visita.
     *
     * PRECONDIZIONI:
     * - tipoVisita != null
     * - dataCorrente != null
     * - datePrecluse != null
     *
     * INVARIANTI:
     * - datePrecluse contiene solo giorni validi (1 <= giorno <= giorni del mese target)
     * - disponibili è sottoinsieme dei giorni validi
     *
     * POSTCONDIZIONI:
     * - ritorna null se non ci sono date disponibili
     * - ritorna Data non null con giorno ∈ disponibili e mese = meseNumero
     */
    public Data scegliDataPerTipo(TipoVisita tipoVisita) {
        Objects.requireNonNull(tipoVisita, "tipoVisita non può essere null");
        assert dataCorrente != null : "Precondizione violata: dataCorrente null";
        assert datePrecluse != null : "Precondizione violata: datePrecluse null";

        System.out.println("\nDate disponibili per la visita: " + tipoVisita.getTitolo());
        System.out.println("Mese considerato : " + meseI2);

        LocalDate meseTarget = dataCorrente.plusMonths(2);
        int meseNumero = meseTarget.getMonthValue();
        int giorniNelMese = meseTarget.lengthOfMonth();

        ArrayList<Integer> disponibili = new ArrayList<>();
        for (int giorno = 1; giorno <= giorniNelMese; giorno++) {
            if (!datePrecluse.contains(giorno)) {
                disponibili.add(giorno);
            }
        }

        for (int g : disponibili) {
            assert g >= 1 && g <= giorniNelMese : "Invariante violata: giorno non valido in disponibili";
        }

        if (disponibili.isEmpty()) {
            System.out.println("Nessuna data disponibile.");
            return null;
        }

        System.out.println("Giorni disponibili:");
        for (int i = 0; i < disponibili.size(); i++) {
            int g = disponibili.get(i);
            System.out.println("   " + (i + 1) + ") " + g + "/" + meseNumero);
        }

        int scelta = InputDati.leggiIntero("Seleziona la tua disponibilità: ", 1, disponibili.size());

        int giornoScelto = disponibili.get(scelta - 1);
        Data dataScelta = new Data(giornoScelto, meseNumero);

        // Postcondizioni
        assert dataScelta != null : "Postcondizione violata: dataScelta è null";
        assert disponibili.contains(dataScelta.getGiorno()) : "Postcondizione violata: giorno scelto non appartiene a disponibili";
        assert dataScelta.getMese() == meseNumero : "Postcondizione violata: mese non coerente";

        System.out.println("Hai scelto: " + dataScelta.getGiorno() + "/" + dataScelta.getMese());

        return dataScelta;
    }
    /**
     * Verifica l'invariante di classe
     */
    private boolean invClass() {
        if (oggi == null) return false;
        if (datePrecluse == null) return false;
        if (dataCorrente == null) return false;
        if (meseCorrente == null || meseI1 == null || meseI2 == null || meseDatePrecluse == null) return false;

        // Controllo che le date precluse siano valori di giorno validi
        for (Integer g : datePrecluse) {
            if (g == null || g < 1 || g > 31) return false;
        }
        return true;
    }
}
