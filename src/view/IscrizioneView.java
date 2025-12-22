package view;

import model.IscrizioneModel;
import java.util.List;

public class IscrizioneView {
    public void mostraIscrizione(IscrizioneModel iscrizione) {
        System.out.println(iscrizione.toString());
    }

    public void mostraListaIscrizioni(List<IscrizioneModel> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nessuna iscrizione trovata.");
        } else {
            for (IscrizioneModel i : lista) {
                mostraIscrizione(i);
            }
        }
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
}
