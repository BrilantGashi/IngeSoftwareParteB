package Versione1;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public class ListaIscrizioni {

    private ArrayList<Iscrizione> iscrizioni = new ArrayList<>();

    // Restituisce copia difensiva per evitare modifiche esterne
    public ArrayList<Iscrizione> getIscrizioni() {
        return new ArrayList<>(iscrizioni);
    }

    // Aggiunge un'Iscrizione se non presente (controlla Fruitore-Visita)
    public void aggiungi(Iscrizione iscrizione) {
        Objects.requireNonNull(iscrizione, "iscrizione non può essere null");
        if (trovaPerFruitoreEVisita(iscrizione.getFruitore(), iscrizione.getVisitaEffettiva()) != null) {
            throw new IllegalStateException("Esiste già un'iscrizione per questo fruitore a questa visita");
        }
        iscrizioni.add(iscrizione);
    }

    // Trova iscrizione specifica per fruitore e visita
    public Iscrizione trovaPerFruitoreEVisita(Fruitore fruitore, Visita visita) {
        for (Iscrizione i : iscrizioni) {
            if (i.getFruitore().equals(fruitore) && i.getVisitaEffettiva().equals(visita)) {
                return i;
            }
        }
        return null;
    }

    // Trova iscrizione tramite codice di prenotazione
    public Iscrizione trovaPerCodicePrenotazione(String codice) {
        for (Iscrizione i : iscrizioni) {
            if (i.getCodicePrenotazione().equals(codice)) {
                return i;
            }
        }
        return null;
    }

    // Calcola totale iscritti (fruitori + accompagnatori) per una visita
    public int getTotaleIscrittiPerVisita(Visita visita) {
        int totale = 0;
        for (Iscrizione i : iscrizioni) {
            if (i.getVisitaEffettiva().equals(visita)) {
                totale += i.getTotalePartecipanti();
            }
        }
        return totale;
    }

    public boolean disdiciIscrizione(String codicePrenotazione, String usernameFruitore) {
        Iscrizione iscrizione = trovaPerCodicePrenotazione(codicePrenotazione);
        if (iscrizione == null) {
            System.out.println("Iscrizione non trovata.");
            return false;
        }
        if (!iscrizione.getFruitore().getCredenziali().getUsername().equals(usernameFruitore)) {
            System.out.println("Non puoi disdire una iscrizione che non ti appartiene.");
            return false;
        }
        Visita visita = iscrizione.getVisitaEffettiva();
        StatoVisita stato = visita.getStatoVisita();
        if (!(stato == StatoVisita.PROPOSTA || stato == StatoVisita.COMPLETA)) {
            System.out.println("Puoi disdire solo iscrizioni a visite proposte o complete.");
            return false;
        }

        // Rimuovi dal sistema
        iscrizioni.remove(iscrizione);

        // Aggiorna lo stato della visita se necessario (se era completa e si libera posto)
        int capienza = visita.getTipoVisita().getSogliaMaxPartecipanti();
        int nuovoTotale = getTotaleIscrittiPerVisita(visita);
        if (stato == StatoVisita.COMPLETA && nuovoTotale < capienza) {
            visita.setStatoVisita(StatoVisita.PROPOSTA);
            try {
                VisitaDAO.aggiornaStatoVisitaDB(visita, StatoVisita.PROPOSTA);
            } catch (Exception e) { System.err.println("Errore aggiornamento DB: " + e.getMessage()); }
            System.out.println("La visita è tornata nello stato PROPOSTA.");
        }
        System.out.println("Iscrizione disdetta con successo.");
        return true;
    }

    public void visualizzaIscrizioniFruitore(String nickname) {
        Set<StatoVisita> statiAmmessi = EnumSet.of(
            StatoVisita.PROPOSTA,
            StatoVisita.CONFERMATA,
            StatoVisita.CANCELLATA
        );
        boolean trovata = false;
        System.out.println("[DEBUG] Cerco iscrizioni per fruitore=" + nickname);
        for (Iscrizione isc : this.iscrizioni) {
            Visita v = isc.getVisitaEffettiva();
            if (!statiAmmessi.contains(v.getStatoVisita())) continue;

            // Ora Iscrizione ha SOLO UN fruitore
            String userIscrizione = isc.getFruitore().getCredenziali().getUsername();
            System.out.println("[DEBUG] Controllo iscrizione di: " + userIscrizione);
            if (userIscrizione.equals(nickname)) {
                StatoVisita stato = v.getStatoVisita();
                String titolo = (v.getTipoVisita() != null) ? v.getTipoVisita().getTitolo() : "Senza titolo";
                Data data = v.getDataVisita();
                String dataStr = (data != null) ? data.toString() : "Data N/D";
                if (stato == StatoVisita.PROPOSTA || stato == StatoVisita.CONFERMATA) {
                    String descrizione = (v.getTipoVisita() != null) ? v.getTipoVisita().getDescrizione() : "N/D";
                    String puntoIncontro = (v.getTipoVisita() != null) ? v.getTipoVisita().getPuntoDiIncontro() : "N/D";
                    String oraInizio = (v.getTipoVisita().getOraInizio() != null) ? v.getTipoVisita().getOraInizio().toString() : "Orario N/D";
                    boolean bigliettoRichiesto = (v.getTipoVisita() != null) && v.getTipoVisita().isBiglietto();
                    System.out.println("- " + titolo +
                        "\n  Descrizione: " + descrizione +
                        "\n  Punto di incontro: " + puntoIncontro +
                        "\n  Data: " + dataStr +
                        "\n  Ora inizio: " + oraInizio +
                        "\n  Biglietto da acquistare: " + (bigliettoRichiesto ? "SI" : "NO"));                 
                        } else if (stato == StatoVisita.CANCELLATA) {
                    System.out.println("- " + titolo +
                        "\n  Data del mancato svolgimento: " + dataStr);
                }
                trovata = true;
            }
        }
        if (!trovata) {
            System.out.println("Nessuna tua iscrizione trovata su visite proposte, confermate o cancellate.");
        }
    }


}
