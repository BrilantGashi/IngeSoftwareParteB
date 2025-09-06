package Versione1;

public class Data {
	private int giorno;
	private int mese;
	
	public Data(int giorno, int mese) {
		this.giorno = giorno;
		this.mese = mese;
	}
	//LAIBA: implementato questo metodo per leggere con facilità da file XML
	public Data() {}
	
	public int getGiorno() {
		return giorno;
	}

	public int getMese() {
		return mese;
	}

	@Override
	public String toString() {
		return giorno + "/" + mese;
	}
	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}
	public void setMese(int mese) {
		this.mese = mese;
	}
	
	
}
