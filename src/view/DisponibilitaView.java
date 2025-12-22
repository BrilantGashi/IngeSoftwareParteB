package view;

import model.DisponibilitaModel;
import java.util.List;

public class DisponibilitaView {

    public void mostraListaDisponibilita(List<DisponibilitaModel> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println("Nessuna disponibilità trovata.");
        } else {
            for (DisponibilitaModel d : lista) {
                System.out.println(d.toString());
            }
        }
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
}
