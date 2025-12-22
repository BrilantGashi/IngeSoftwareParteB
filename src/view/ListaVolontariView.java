package view;

import model.VolontarioModel;
import model.ListaVolontariModel;
import model.TipoVisitaModel;
import model.ListaTipoVisitaModel;

import java.util.List;

public class ListaVolontariView {

    public void stampaVolontari(ListaVolontariModel model) {
        List<VolontarioModel> volontari = model.getVolontari();
        if (volontari.isEmpty()) {
            System.out.println("Nessun volontario presente.");
        } else {
            System.out.println("===== ELENCO VOLONTARI =====");
            for (VolontarioModel v : volontari) {
                System.out.println("- " + v.getNickname());
            }
        }
    }

    public void stampaVolontariConVisite(ListaVolontariModel model, ListaTipoVisitaModel tutteLeVisite) {
        List<VolontarioModel> volontari = model.getVolontari();
        if (volontari.isEmpty()) {
            System.out.println("Nessun volontario presente.");
            return;
        }
        for (VolontarioModel v : volontari) {
            if (!v.isAttivo()) continue;
            System.out.println("Volontario: " + v.getNickname());
            boolean associato = false;
            for (TipoVisitaModel visita : tutteLeVisite.getTipi()) {
                if (visita.getListaVolontari().contienePerNickname(v.getNickname())) {
                    System.out.println("   - " + visita.getTitolo());
                    associato = true;
                }
            }
            if (!associato) {
                System.out.println("   Nessuna visita associata.");
            }
            System.out.println("-----------------------------------");
        }
    }

    public void visualizzaVolontari(ListaVolontariModel model) {
        int count = 1;
        for (VolontarioModel v : model.getVolontari()) {
            if (v.isAttivo()) {
                System.out.println(count + ". " + v.getCredenziali().getUsername());
                count++;
            }
        }
        if (count == 1) {
            System.out.println("Nessun volontario presente");
        }
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
}
