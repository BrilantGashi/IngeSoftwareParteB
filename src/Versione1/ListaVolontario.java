package Versione1;

import java.util.ArrayList;
import java.util.List;

public class ListaVolontario {

	private ArrayList<Volontario> ListaVolontari;

	public ListaVolontario(ArrayList<Volontario> listaVolontari) {
		super();
		ListaVolontari = listaVolontari;
	}
	
	public ListaVolontario() {
	}

	public ArrayList<Volontario> getListaVolontari() {
		return ListaVolontari;
	}

	public void setListaVolontari(ArrayList<Volontario> listaVolontari) {
		ListaVolontari = listaVolontari;
	}

	
	//************SISTEMARE VISUALIZZAZIONE VOLONTARI
	@Override
	public String toString() {
		
		String syso = "";
		
		for (Volontario v : ListaVolontari) {
			syso = syso + "Volontario: " + v.getNickname() + "\n";
		}
		
		return syso;
	}
	
	
	
}
