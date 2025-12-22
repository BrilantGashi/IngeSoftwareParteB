package model;

public enum Mesi {
	Gennaio,
	Febbraio,
	Marzo,
	Aprile,
	Maggio,
	Giugno,
	Luglio,
	Agosto,
	Settembre,
	Ottobre,
	Novembre,
	Dicembre;
	
	//metodo che ritorna il mese corrispondente
    public static String getNomeMese(int mese) {
        return Mesi.values()[mese - 1].name();
    }
    
    
    
}