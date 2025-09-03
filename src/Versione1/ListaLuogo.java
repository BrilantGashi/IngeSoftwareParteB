package Versione1;

import java.util.ArrayList;

public class ListaLuogo {
	private ArrayList<Luogo> listaLuoghi;

	public ListaLuogo() {
		super();
		this.listaLuoghi = new ArrayList<>();
	}

	public ArrayList<Luogo> getListaLuoghi() {
		return listaLuoghi;
	}

	public void setListaLuoghi(ArrayList<Luogo> listaLuoghi) {
		this.listaLuoghi = listaLuoghi;
	}
	
	public void add(Luogo luogo) {
		listaLuoghi.add(luogo);
	}

}
