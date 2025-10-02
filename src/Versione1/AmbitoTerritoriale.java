package Versione1;

import java.util.ArrayList;
import java.util.List;

public class AmbitoTerritoriale {
	
	private ArrayList<Integer> cap; 
	private String provincia;
	
	public AmbitoTerritoriale(ArrayList<Integer> cap, String provincia) {
		this.cap = cap;
		this.provincia = provincia;
	}
	
	//LAIBA: implementato questo metodo per leggere con facilit� da file XML
	public AmbitoTerritoriale() {}
	
	/*public boolean contieneCap (String capConfronto) {
		return cap.contains(capConfronto);
	}*/
	
	public boolean confronto(Indirizzo indirizzo) {
		return cap.contains(indirizzo.getCap());
	}

	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("L'ambito territoriale prescelto � la provincia di: %s", provincia));
		return sb.toString();
	}
	
	
	//getters & setters
	
	public ArrayList<Integer> getCap() {
		return cap;
	}

	public void setCap(ArrayList<Integer> capList) {
		this.cap = capList;
	}
	
	public void addCap(int capInput) {
		this.cap.add(capInput);
	}
	
	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	public void stampaAmbito() {
	    System.out.println("Provincia: " + provincia);
	    if (cap != null && !cap.isEmpty()) {
	        System.out.print("CAP: ");
	        for (Integer c : cap) {
	            System.out.print(c + " ");
	        }
	        System.out.println();
	    } else {
	        System.out.println("Nessun CAP disponibile");
	    }
	}
	
	
	
}
