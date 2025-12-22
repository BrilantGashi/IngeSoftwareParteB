package view;

import model.AmbitoTerritorialeModel;
import it.unibs.fp.mylib.InputDati;

import java.util.List;

public class AmbitoTerritorialeView {

    public AmbitoTerritorialeModel leggiDatiAmbito() {
        String provincia = InputDati.leggiStringaNonVuota("Provincia");
        String comune = InputDati.leggiStringaNonVuota("Comune");
        int cap = InputDati.leggiIntero("CAP :", 1, 100000);
        return new AmbitoTerritorialeModel(cap, provincia, comune);
    }

    public void mostraAmbito(AmbitoTerritorialeModel ambito) {
        System.out.println(ambito.toString());
    }
    
    public void mostraTesto(String testo) {
        System.out.println(testo);
    }

    public void mostraErrore(String msg) {
        System.out.println("Errore: " + msg);
    }
}
