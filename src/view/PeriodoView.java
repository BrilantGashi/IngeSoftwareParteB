package view;

import model.PeriodoModel;

public class PeriodoView {

	public void mostraPeriodo(PeriodoModel periodo) {
		System.out.println(periodo.toString());
	}

	public void mostraMessaggio(String msg) {
		System.out.println(msg);
	}
}
