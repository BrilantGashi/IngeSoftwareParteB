package view;

import model.VisitaModel;
import model.VolontarioModel;
import model.DataModel;
import model.FruitoreModel;
import model.ListaIscrizioniModel;
import model.ListaVisiteModel;
import model.StatoVisita;
import model.TipoVisitaModel;

import java.util.ArrayList;
import java.util.List;

import it.unibs.fp.mylib.InputDati;

public class ListaVisiteView {

    public void mostraVisite(List<VisitaModel> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println("Nessuna visita disponibile.");
        } else {
            for (VisitaModel v : lista) {
                System.out.println(v.toString());
            }
        }
    }

    public void stampaPianoVisite(ListaVisiteModel model) {
        List<VisitaModel> lista = model.getListaVisite();
        if (lista.isEmpty()) {
            System.out.println("Nessuna visita programmata.");
        } else {
            System.out.println("===== PIANO DELLE VISITE =====");
            for (VisitaModel v : lista) {
                System.out.println(v.getTipoVisita().getDescrizione());
                System.out.println("------------------------------------");
            }
        }
    }
    
    public void stampaTitoloEStato(ListaVisiteModel model) {
        List<VisitaModel> listaVisite = model.getListaVisite();
        if (listaVisite == null || listaVisite.isEmpty()) {
            System.out.println("Non ci sono visite registrate al momento.");
            return;
        }

        for (VisitaModel visita : listaVisite) {
            String titolo = (visita.getTipoVisita() != null) ? visita.getTipoVisita().getTitolo() : "Senza titolo";
            DataModel data = visita.getDataVisita();
            String dataStr = (data != null) ? data.toString() : "Data N/D";
            String statoStr = (visita.getStatoVisita() != null) ? visita.getStatoVisita().name() : "Stato N/D";

            // Volontari associati
            String volontariStr = "Volontari: ";
            if (visita.getVolontariEffettivi() != null && !visita.getVolontariEffettivi().getVolontari().isEmpty()) {
                ArrayList<String> nomiVol = new ArrayList<>();
                for (VolontarioModel vol : visita.getVolontariEffettivi().getVolontari()) {
                    nomiVol.add(vol.getCredenziali().getUsername());
                }
                volontariStr += String.join(", ", nomiVol);
            } else {
                volontariStr += "nessuno";
            }

            System.out.println("- " + titolo
                + " | Data: " + dataStr
                + " | Stato: " + statoStr
                + " | " + volontariStr);
        }
    }
    
    public void stampaVisitePerFruitore(ListaVisiteModel listaVisiteModel) {
        List<VisitaModel> listaVisite = listaVisiteModel.getListaVisite();
        if (listaVisite == null || listaVisite.isEmpty()) {
            System.out.println("Non ci sono visite registrate al momento.");
            return;
        }
        for (VisitaModel visita : listaVisite) {
            StatoVisita stato = visita.getStatoVisita();
            TipoVisitaModel tipo = visita.getTipoVisita();
            DataModel data = visita.getDataVisita();

            // Mostra solo PROPOSTA, CONFERMATA, CANCELLATA
            if (stato == StatoVisita.PROPOSTA || stato == StatoVisita.CONFERMATA) {
                String titolo = (tipo != null) ? tipo.getTitolo() : "Senza titolo";
                String descrizione = (tipo != null) ? tipo.getDescrizione() : "N/D";
                String puntoIncontro = (tipo != null) ? tipo.getPuntoDiIncontro() : "N/D";
                String oraInizio = (tipo != null && tipo.getOraInizio() != null) ? tipo.getOraInizio().toString() : "Orario N/D";
                boolean bigliettoRichiesto = (tipo != null) && tipo.isBiglietto();
                String dataStr = (data != null) ? data.toString() : "Data N/D";

                System.out.println("- " + titolo
                    + "\n  Descrizione: " + descrizione
                    + "\n  Punto di incontro: " + puntoIncontro
                    + "\n  Data: " + dataStr
                    + "\n  Ora inizio: " + oraInizio
                    + "\n  Biglietto da acquistare: " + (bigliettoRichiesto ? "SI" : "NO")
                    + "\n  Stato: " + stato.name());
            } else if (stato == StatoVisita.CANCELLATA) {
                String titolo = (tipo != null) ? tipo.getTitolo() : "Senza titolo";
                String dataStr = (data != null) ? data.toString() : "Data N/D";
                System.out.println("- " + titolo
                    + "\n  Data del mancato svolgimento: " + dataStr
                    + "\n  Stato: " + stato.name());
            }
        }
    }

    public void mostraVisiteDisponibiliConPosti(ArrayList<VisitaModel> visiteDisponibili, ListaIscrizioniModel listaIscrizioni) {
        if (visiteDisponibili == null || visiteDisponibili.isEmpty()) {
            System.out.println("Nessuna visita disponibile per iscrizione.");
            return;
        }
        System.out.println("===== Visite disponibili =====");
        for (int i = 0; i < visiteDisponibili.size(); i++) {
            VisitaModel v = visiteDisponibili.get(i);
            String titolo = v.getTipoVisita() != null ? v.getTipoVisita().getTitolo() : "Senza titolo";
            int max = v.getTipoVisita().getSogliaMaxPartecipanti();
            int tot = listaIscrizioni.getTotaleIscrittiPerVisita(v);
            System.out.println((i + 1) + ") " + titolo + " - " + v.getDataVisita()
                     + " | Posti rimasti: " + (max - tot));
        }
    }

    public int chiediSelezioneVisita(int max) {
        if (max == 0) return -1;
        return InputDati.leggiIntero("Seleziona una visita (1-" + max + "): ", 1, max) - 1;
    }


    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
}
