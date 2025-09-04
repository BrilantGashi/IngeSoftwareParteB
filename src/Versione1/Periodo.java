package Versione1;

public class Periodo {
	Data dataInizio;
	Data dataFine;
	//ciao
	
	public Periodo(Data dataInizio, Data dataFine) {
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
	}

	@Override
	public String toString() {
		return "Periodo valido: " + dataInizio.toString() + " a " + dataFine.toString();
	}

	public Data getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Data dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Data getDataFine() {
		return dataFine;
	}

	public void setDataFine(Data dataFine) {
		this.dataFine = dataFine;
	}

	
	
	
}
