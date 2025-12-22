package view;

import model.IndirizzoModel;
import model.AmbitoTerritorialeModel;
import model.TipoStrada; // Enum va nel model!
import it.unibs.fp.mylib.InputDati;

public class IndirizzoView {

    public IndirizzoModel leggiIndirizzo(AmbitoTerritorialeView ambitoView) {
        TipoStrada tipoStrada = scegliTipoStrada();
        String nomeStrada = InputDati.leggiStringaNonVuota("Nome della strada: ");
        String numeroCivico = InputDati.leggiStringaNonVuota("Numero civico: ");
        AmbitoTerritorialeModel ambito = ambitoView.leggiDatiAmbito();
        return new IndirizzoModel(tipoStrada, nomeStrada, numeroCivico, ambito);
    }

    public TipoStrada scegliTipoStrada() {
        System.out.println("Scegli il tipo di strada tra i seguenti:");
        for (TipoStrada t : TipoStrada.values()) {
            System.out.println(" - " + t.name());
        }
        TipoStrada scelta = null;
        while (scelta == null) {
            String input = InputDati.leggiStringaNonVuota("Tipo di strada: ");
            input = normalizza(input);

            // Cerca corrispondenza anche se l'utente scrive in minuscolo
            for (TipoStrada t : TipoStrada.values()) {
                if (t.name().equalsIgnoreCase(input)) {
                    scelta = t;
                    break;
                }
            }
            if (scelta == null) {
                System.out.println("Valore non valido. Riprova.");
            }
        }
        return scelta;
    }

    private String normalizza(String input) {
        if (input == null || input.isBlank()) return "";
        String trimmed = input.trim();
        return trimmed.substring(0,1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }

    public void mostraIndirizzo(IndirizzoModel ind) {
        System.out.println(ind.toString());
    }

    public void mostraErrore(String msg) {
        System.out.println("Errore: " + msg);
    }
}
