package Versione1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Calendario {
	
	LocalDate oggi = LocalDate.now();
	
    LocalDate dataCorrente;
    String meseCorrente;
    String meseI1;
    String meseI2;
    String meseDatePrecluse;
    
    
    ArrayList<Integer> datePrecluse = new ArrayList<>();
	
	public Calendario() {		
	    setMesi();
	}

    
    public void setMesi () {
    	if (oggi.getDayOfMonth() < 16) {
    		//faccio partire il mese corrente dal giorno 16
    		dataCorrente = oggi.withDayOfMonth(16);
    	} else {
    		//faccio partire il mese prossimo dal giorno 16
    		dataCorrente = oggi.plusMonths(1).withDayOfMonth(16);
    	}
    	
    	//man mano ccambiare i nomi mesi per renderli piu' intuitivi
    	meseCorrente = Mesi.getNomeMese(dataCorrente.getMonthValue());
    	meseI1 = Mesi.getNomeMese(dataCorrente.plusMonths(1).getMonthValue());
    	meseI2 = Mesi.getNomeMese(dataCorrente.plusMonths(2).getMonthValue());
    	meseDatePrecluse = Mesi.getNomeMese(dataCorrente.plusMonths(3).getMonthValue());	//mese i+3
    }
	
    public void printMesi () {
    	System.out.println("Mese i: " + meseCorrente);
    	System.out.println("Mese i+1: " + meseI1);
    	System.out.println("Mese i+2: " + meseI2);
    	System.out.println("Mese i+3: " + meseDatePrecluse);
    }
		
	public ArrayList<Integer> getDatePrecluse() {
		return datePrecluse;
	}

	public void setDatePrecluse(ArrayList<Integer> datePrecluse) {
		this.datePrecluse = datePrecluse;
	}

	public LocalDate getOggi() {
		return oggi;
	}
	public void setOggi(LocalDate oggi) {
		this.oggi = oggi;
	}

	public String getMeseDatePrecluse() {
		return meseDatePrecluse;
	}

	public void setMeseDatePrecluse(String meseDatePrecluse) {
		this.meseDatePrecluse = meseDatePrecluse;
	}	

	public void aggiungiDataPreclusa (int giornoEscluso) {
		datePrecluse.add(giornoEscluso);
	}
	
	public boolean checkData (int giorno) {
		if (datePrecluse.contains(giorno)) {
			return true;
		}
		
		return false;
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
	
	
	
}
