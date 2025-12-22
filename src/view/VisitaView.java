package view;

import model.VisitaModel;
import java.util.List;

public class VisitaView {
    public void mostraVisita(VisitaModel visita) {
        System.out.println(visita);
    }

    public void mostraListaVisite(List<VisitaModel> visite) {
        if (visite == null || visite.isEmpty()) {
            System.out.println("Nessuna visita trovata.");
            return;
        }
        for (VisitaModel v : visite) {
            mostraVisita(v);
        }
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
}
