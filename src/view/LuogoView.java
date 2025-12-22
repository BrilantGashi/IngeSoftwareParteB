package view;

import model.ListaLuogoModel;
import model.LuogoModel;
import java.util.List;

public class LuogoView {

	public void mostraLuogo(LuogoModel luogo) {
		System.out.println(luogo);
	}

	public void mostraLuoghi(ListaLuogoModel listaLuogoModel) {
		var luoghi = listaLuogoModel.getListaLuoghi();
		if (luoghi == null || luoghi.isEmpty()) {
			System.out.println("Nessun luogo disponibile.");
			return;
		}
		for (LuogoModel l : luoghi) {
			System.out.println(l);
		}
	}

	public void mostraMessaggio(String msg) {
		System.out.println(msg);
	}
}
