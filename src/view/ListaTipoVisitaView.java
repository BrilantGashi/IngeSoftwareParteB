package view;

import model.TipoVisitaModel;
import model.ListaTipoVisitaModel;

import java.util.List;

public class ListaTipoVisitaView {

    public void stampaElencoNumerato(ListaTipoVisitaModel model) {
        List<TipoVisitaModel> tipi = model.getTipi();
        if (tipi.isEmpty()) {
            System.out.println("Nessun tipo visita disponibile.");
            return;
        }
        int count = 1;
        for (TipoVisitaModel tv : tipi) {
            if (tv.isAttivo()) {
                System.out.println(count + ") " + tv.getTitolo() + " - Volontari: " + tv.getListaVolontari().size());
                count++;
            }
        }
    }

    public void stampaListaDettagliata(ListaTipoVisitaModel model) {
        List<TipoVisitaModel> tipi = model.getTipi();
        int count = 1;
        for (TipoVisitaModel tv : tipi) {
            if (tv.isAttivo()) {
                System.out.println(count + ") " + tv.toStringSemplificato());
                count++;
            }
        }
        if (count == 1) {
            System.out.println("Nessun tipo visita disponibile.");
        }
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
}
