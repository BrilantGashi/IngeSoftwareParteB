package controller;

import model.GiorniModel;
import view.GiorniView;

import java.util.ArrayList;

public class GiorniController {
    private GiorniView view;

    public GiorniController(GiorniView view) {
        this.view = view;
    }

    public ArrayList<GiorniModel> scegliGiorniSettimana() {
        ArrayList<GiorniModel> giorniSelezionati = new ArrayList<>();
        view.mostraTitoloIniziale();
        boolean continua;
        do {
        	GiorniModel giorno = null;
            do {
                try {
                    int input = view.scegliGiorno();
                    giorno = GiorniModel.values()[input - 1];
                } catch (Exception e) {
                    view.mostraErroreGiorno();
                    giorno = null;
                }
            } while (giorno == null);

            if (giorniSelezionati.contains(giorno)) {
                view.mostraGiornoGiaInserito(giorno.name());
            } else {
                giorniSelezionati.add(giorno);
                view.mostraGiornoAggiunto(giorno.name());
            }
            continua = view.chiediAggiungiAltro();
        } while (continua);
        return giorniSelezionati;
    }
}
