package Versione1;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import it.unibs.fp.mylib.InputDati;

public class Calendario {
    
    private LocalDate dataCorrente;
    private ArrayList<Integer> datePrecluse_I_1 = new ArrayList<>();
    private ArrayList<Integer> datePrecluse_I_2 = new ArrayList<>();
    private ArrayList<Integer> datePrecluse_I_3 = new ArrayList<>();
    private String meseCorrente;
    private String meseI1;
    private String meseI2;
    private String meseI3; // rinominato da meseDatePrecluse per coerenza
    private LocalDate oggi = LocalDate.now();

    /**
     * INVARIANTI DI CLASSE:
     * - oggi != null
     * - dataCorrente != null dopo setMesi()
     * - datePrecluse_I_1, datePrecluse_I_2, datePrecluse_I_3 != null
     * - Ogni giorno nelle liste è valido (1 <= giorno <= giorni del mese di riferimento)
     * - I campi meseCorrente, meseI1, meseI2, meseI3 non sono mai null dopo setMesi()
     */

    public Calendario() {   
        setMesi();
        assert invClass() : "Invariante violato nel costruttore";
    }
    /**
     * Setter per le date precluse del mese specificato (1, 2 o 3).
     */
    public void setDatePrecluse(ArrayList<Integer> datePrecluse, int mese) {
        Objects.requireNonNull(datePrecluse, "La lista delle date precluse non può essere null");

        LocalDate meseTarget = dataCorrente.plusMonths(mese);
        int giorniNelMese = meseTarget.lengthOfMonth();

        for (Integer g : datePrecluse) {
            if (g == null || g < 1 || g > giorniNelMese) {
                throw new IllegalArgumentException(
                    "Data non valida per il mese di " + meseTarget.getMonth() + ": " + g
                );
            }
        }

        if (mese == 1) {
            this.datePrecluse_I_1 = new ArrayList<>(datePrecluse);
        } else if (mese == 2) {
            this.datePrecluse_I_2 = new ArrayList<>(datePrecluse);
        } else if (mese == 3) {
            this.datePrecluse_I_3 = new ArrayList<>(datePrecluse);
        } else {
            throw new IllegalArgumentException("Mese non valido: deve essere 1, 2 o 3");
        }

        assert invClass() : "Invariante violato in setDatePrecluse";
    }

    /**
     * Controlla se un giorno è precluso per il mese specificato.
     * PRECONDIZIONE: giorno >= 1 && giorno <= 31, mese in {1, 2, 3}
     * POSTCONDIZIONE: ritorna true sse giorno è presente nella lista del mese
     */
    public boolean checkData(int giorno, int mese) {
        if (giorno < 1 || giorno > 31) {
            throw new IllegalArgumentException("Giorno non valido: " + giorno);
        }
        boolean result = getDatePreclusePerMese(mese).contains(giorno);
        assert result == getDatePreclusePerMese(mese).contains(giorno) : "Postcondizione violata in checkData";
        return result;
    }
    
    /**
     * Restituisce la lista delle date precluse per il mese specificato (1=i+1, 2=i+2, 3=i+3).
     */
    public ArrayList<Integer> getDatePreclusePerMese(int mese) {
        if (mese == 1) {
            return datePrecluse_I_1;
        } else if (mese == 2) {
            return datePrecluse_I_2;
        } else if (mese == 3) {
            return datePrecluse_I_3;
        } else {
            throw new IllegalArgumentException("Mese non valido: deve essere 1, 2 o 3");
        }
    }

    public String getMeseI3() {
        return meseI3;
    }

    public LocalDate getOggi() {
        return oggi;
    }

    public void setMeseI3(String meseI3) {
        if (meseI3 == null || meseI3.isBlank()) {
            throw new IllegalArgumentException("Il mese i+3 non può essere null o vuoto");
        }
        this.meseI3 = meseI3;
        assert invClass() : "Invariante violato in setMeseI3";
    }

    /**
     * Calcola i mesi di riferimento a partire da 'oggi'.
     * POSTCONDIZIONE: dataCorrente != null, meseCorrente, meseI1, meseI2, meseI3 != null
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
        meseI3 = Mesi.getNomeMese(dataCorrente.plusMonths(3).getMonthValue());

        assert dataCorrente != null : "Postcondizione violata: dataCorrente null";
        assert meseCorrente != null && meseI1 != null && meseI2 != null && meseI3 != null : "Postcondizione violata: mesi non inizializzati";
        assert invClass() : "Invariante violato in setMesi";
    }

    public void setOggi(LocalDate oggi) {
        Objects.requireNonNull(oggi, "oggi non può essere null");
        this.oggi = oggi;
        assert invClass() : "Invariante violato in setOggi";
    }

    public String toStringDatePrecluse(int mese) {
        StringBuilder sb = new StringBuilder();
        String nomeMese = getNomeMeseI(mese);

        if (getDatePreclusePerMese(mese).isEmpty()) {
            sb.append("Non sono state inserite date da escludere per il mese di ").append(nomeMese).append("\n");
        } else {
            String header = "===== DATE ESCLUSE PER IL MESE DI " + nomeMese.toUpperCase() + " =====";
            sb.append(header).append("\n");
            
            for (int giorno : getDatePreclusePerMese(mese)) {
                sb.append(giorno).append(" ").append(nomeMese.toLowerCase()).append("\n");
            }
            
            // Crea una riga di chiusura della stessa lunghezza dell'header
            sb.append("=".repeat(header.length())).append("\n");
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
     * Trasforma la stringa del mese nel corrispondente numero
     */
    public int getMeseNumero(String nomeMese) {
        for (Mesi m : Mesi.values()) {              
            if (m.name().equalsIgnoreCase(nomeMese)) {  
                return m.ordinal() + 1;            
            }
        }
        return -1; // se non trovato
    }
    
    /**
     * Restituisce il nome del mese i+n in base al parametro mese.
     * @param mese 1 per i+1, 2 per i+2, 3 per i+3
     * @return il nome del mese corrispondente
     */
    public String getNomeMeseI(int mese) {
        switch (mese) {
            case 1:
                return meseI1;
            case 2:
                return meseI2;
            case 3:
                return meseI3;
            default:
                throw new IllegalArgumentException("Mese non valido: deve essere 1, 2 o 3");
        }
    }
    
    public int giorniDelMese(int mese) {
        switch (mese) {
            case 2: return 28; // semplificazione: non gestiamo bisestili
            case 4: case 6: case 9: case 11: return 30;
            default: return 31;
        }
    }
    
    /**
     * Aggiunge un giorno alla lista delle date precluse per il mese specificato (1, 2 o 3).
     * PRECONDIZIONE: giornoEscluso deve essere compreso tra 1 e giorni del mese target.
     * POSTCONDIZIONE: la lista corrispondente contiene giornoEscluso.
     */
    public void aggiungiDataPreclusa(int giornoEscluso, int mese) {
        LocalDate meseTarget = dataCorrente.plusMonths(mese);
        int giorniNelMese = meseTarget.lengthOfMonth();

        if (giornoEscluso < 1 || giornoEscluso > giorniNelMese) {
            throw new IllegalArgumentException(
                "Giorno non valido per il mese di " + meseTarget.getMonth() + ": " + giornoEscluso
            );
        }

        getDatePreclusePerMese(mese).add(giornoEscluso);

        assert getDatePreclusePerMese(mese).contains(giornoEscluso) : "Postcondizione violata: giorno non aggiunto";
        assert invClass() : "Invariante violato in aggiungiDataPreclusa";
    }
    
    /**
     * Metodo che permette di scegliere una data disponibile per un certo tipo di visita.
     * PRECONDIZIONI:
     * - tipoVisita != null
     * - dataCorrente != null
     * - mese in {1, 2, 3}
     *
     * POSTCONDIZIONI:
     * - ritorna null se non ci sono date disponibili
     * - ritorna Data non null con giorno disponibile e mese = meseNumero
     */
    public  boolean isGiornoProgrammabilePerTipoVisita(int giornoDelMese, TipoVisita tipoVisita) {
        LocalDate date = LocalDate.of(dataCorrente.getYear(), getMeseNumero(meseI1), giornoDelMese);
        DayOfWeek dow = date.getDayOfWeek(); // 1=LUNEDI ... 7=DOMENICA

        Giorni listaGiorni = Giorni.values()[dow.getValue() - 1]; // enum Giorni

        ArrayList<Giorni> giorniProgrammabili = tipoVisita.getGiorniSettimanaProgrammabile();
        if (giorniProgrammabili == null || giorniProgrammabili.isEmpty()) {
            return false;
        }

        return giorniProgrammabili.contains(listaGiorni);
    }
    
    public Data scegliDataPerTipo(TipoVisita tipoVisita, int mese) {
        Objects.requireNonNull(tipoVisita, "tipoVisita non può essere null");
        assert dataCorrente != null : "Precondizione violata: dataCorrente null";
        assert getDatePreclusePerMese(mese) != null : "Precondizione violata: datePrecluse null";

        String nomeMese = "";
        if (mese == 1) nomeMese = meseI1;
        else if (mese == 2) nomeMese = meseI2;
        else if (mese == 3) nomeMese = meseI3;

        System.out.println("\nDate disponibili per la visita: " + tipoVisita.getTitolo());
        System.out.println("Mese considerato: " + nomeMese);

        LocalDate meseTarget = dataCorrente.plusMonths(mese);
        int meseNumero = meseTarget.getMonthValue();
        int giorniNelMese = meseTarget.lengthOfMonth();

        ArrayList<Integer> disponibili = new ArrayList<>();
        for (int giorno = 1; giorno <= giorniNelMese; giorno++) {
            if (!getDatePreclusePerMese(mese).contains(giorno)) {
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
    
    public boolean isGiornoEscluso(int giorno, int mese) {
        return getDatePreclusePerMese(mese).contains(giorno);
    }
    
    public void printMesi() {
        System.out.println("Mese i: " + meseCorrente);
        System.out.println("Mese i+1: " + meseI1);
        System.out.println("Mese i+2: " + meseI2);
        System.out.println("Mese i+3: " + meseI3);
    }
    
    /**
     * Verifica l'invariante di classe
     */
    private boolean invClass() {
        if (oggi == null) return false;
        if (datePrecluse_I_1 == null || datePrecluse_I_2 == null || datePrecluse_I_3 == null) return false;
        if (dataCorrente == null) return false;
        if (meseCorrente == null || meseI1 == null || meseI2 == null || meseI3 == null) return false;

        // Controllo che le date precluse siano valori di giorno validi
        for (Integer g : datePrecluse_I_1) {
            if (g == null || g < 1 || g > 31) return false;
        }
        for (Integer g : datePrecluse_I_2) {
            if (g == null || g < 1 || g > 31) return false;
        }
        for (Integer g : datePrecluse_I_3) {
            if (g == null || g < 1 || g > 31) return false;
        }
        return true;
    }
  
}
