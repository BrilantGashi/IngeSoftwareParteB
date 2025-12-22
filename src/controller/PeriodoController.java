package controller;

import model.PeriodoModel;
import model.DataModel;
import view.PeriodoView;

public class PeriodoController {
    private PeriodoModel model;
    private final PeriodoView periodoView;
    private final DataController dataController;

    // Costruttore: la dipendenza da DataController è obbligatoria per creare i periodi
    public PeriodoController(PeriodoView periodoView, DataController dataController) {
        this.periodoView = periodoView;
        this.dataController = dataController;
    }

    // Facoltativo: per compatibilità se desideri mantenere un model interno
    public PeriodoController(PeriodoModel model, PeriodoView periodoView, DataController dataController) {
        this.model = model;
        this.periodoView = periodoView;
        this.dataController = dataController;
    }

    // Mostra il periodo interno eventualmente gestito come stato
    public void mostraPeriodo() {
        if (model != null) {
            periodoView.mostraPeriodo(model);
        } else {
            periodoView.mostraMessaggio("Nessun periodo impostato.");
        }
    }

    // Aggiorna (e mostra) il periodo gestito come stato
    public void aggiornaPeriodo(PeriodoModel nuovoPeriodo) {
        this.model = nuovoPeriodo;
        periodoView.mostraPeriodo(model);
    }

    /**
     * Crea un nuovo periodo chiedendo input utente robustamente, incluse le date di inizio e fine.
     * Cicla fino a ottenere un periodo valido oppure in caso di errori di input.
     * Ritorna un nuovo oggetto PeriodoModel con dati validi.
     */
    public PeriodoModel creaNuovoPeriodo() {
        PeriodoModel periodo = null;
        do {
            try {
                DataModel dataInizio = dataController.chiediDataAllUtente(
                    "Inserisci il giorno di inizio del periodo: ",
                    "Inserisci il mese di inizio del periodo: "
                );
                DataModel dataFine = dataController.chiediDataAllUtente(
                    "Inserisci il giorno di fine del periodo: ",
                    "Inserisci il mese di fine del periodo: "
                );
                periodo = new PeriodoModel(dataInizio, dataFine);
            } catch (Exception e) {
                periodoView.mostraMessaggio("Periodo non valido: " + e.getMessage() + ". Riprova.");
                periodo = null;
            }
        } while (periodo == null);
        periodoView.mostraPeriodo(periodo);
        return periodo;
    }
}
