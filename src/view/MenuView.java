package view;

import it.unibs.fp.mylib.InputDati;
import it.unibs.fp.mylib.MyMenu;

// Modifica le voci del menu
public class MenuView {
	private static final String[] voci = {
		    "Accedi come configuratore",
		    "Accedi come volontario",
		    "Accedi/crea come fruitore",
		};


    public int mostraMenuPrincipale() {
        MyMenu menu = new MyMenu("Menu principale", voci);
        return menu.scegli();
    }

    public boolean chiediConfermaUscita() {
        return InputDati.yesOrNo("Vuoi veramente uscire?");
    }
    
    public void mostraMessaggioFinale() {
    	System.out.println("Arrivederci!");
    }
}
