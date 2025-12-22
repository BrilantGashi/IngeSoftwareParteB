package view;

import model.TipoVisitaModel;
import java.util.List;

public class TipoVisitaView {

    public void mostraTipoVisita(TipoVisitaModel tipoVisita) {
        System.out.println(tipoVisita.toString());
    }

    public void mostraListaTipiVisita(List<TipoVisitaModel> tipi) {
        if (tipi == null || tipi.isEmpty()) {
            System.out.println("Nessun tipo visita presente.");
            return;
        }
        for (TipoVisitaModel t : tipi) {
            System.out.println(t.toString());
        }
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
}
