package view;

import java.time.LocalTime;
import java.util.ArrayList;

import controller.DataController;
import controller.GiorniController;
import it.unibs.fp.mylib.InputDati;
import it.unibs.fp.mylib.MyMenu;
import model.AmbitoTerritorialeModel;
import model.ConfiguratoreModel;
import model.DataModel;
import model.GiorniModel;
import model.IndirizzoModel;
import model.ListaLuogoModel;
import model.ListaTipoVisitaModel;
import model.ListaVolontariModel;
import model.LuogoModel;
import model.PeriodoModel;
import model.StatoSistemaModel;
import model.TipoStrada;
import model.TipoVisitaModel;

public class ConfiguratoreView {

    public void mostraConfiguratore(ConfiguratoreModel configuratore) {
        System.out.println(configuratore.toString());
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }
    
    private static final String[] vociConfig = {
    	    "Creazione/Lettura Corpo Dati",
    	    "Inserimento date precluse e apertura raccolta disponibilita' dei volontari",
    	    "Chiusura raccolta disponibilità volontari/date precluse",
    	    "Modifica num max per fruitore",
    	    "Visualizza volontari",
    	    "Visualizza Luoghi",
    	    "Visualizza tipi visita associati a ciascun luogo",
    	    "Visualizza Stato Visite",
    	    "Genera piano delle visite",
    	    "Chiusura raccolta iscrizioni fruitori",
    	    "Modifica Luoghi/Tipi Visita/Volontari"
    	};


        public int scegliVoceMenuPrincipale() {
            MyMenu menu = new MyMenu("Menu configuratore", vociConfig);
            return menu.scegli();
        }

        public boolean chiediConfermaIndietro() {
            return InputDati.yesOrNo("Vuoi veramente tornare indietro?");
        }
        private static final String[] vociConfigModifica = {
        	    "Aggiungi nuovo luogo per visite guidate",
        	    "Aggiungi tipi visita a luogo esistente",
        	    "Aggiungi volontario a tipo visita",
        	    "Rimuovi Luogo",
        	    "Rimuovi Tipo Visita",
        	    "Rimuovi Volontario"
        };

        public int scegliVoceMenuModifica() {
        	    MyMenu menuModifica = new MyMenu("Menu di modifica", vociConfigModifica);
        	    return menuModifica.scegli();
        }

        public boolean chiediConfermaUscitaModifiche() {
        	    return InputDati.yesOrNo(
        	        "Proseguendo, non potrai più apportare modifiche al Corpo Dati. Confermi di voler uscire dal menu?");
        }
        
        public int scegliOperazioneCorpoDati() {
            String[] opzioni = {
                "Crea corpo dati",
                "Leggi corpo dati"
            };
            MyMenu menu = new MyMenu("Operazioni sul corpo dati", opzioni);
            return menu.scegli();
        }
        
        public String leggiProvincia() {
            return InputDati.leggiStringaNonVuota("Inserisci la provincia: ");
        }

        public String leggiComune() {
            return InputDati.leggiStringaNonVuota("Inserisci il nome del comune: ");
        }

        public int leggiCap() {
            return InputDati.leggiIntero("Inserisci il CAP: ", 10000, 99999);
        }

        public boolean continuaInserimento(String cosa) {
            return InputDati.yesOrNo("Vuoi inserire un altro " + cosa + "?");
        }
        
        public int leggiNumMaxPersone() {
            return InputDati.leggiIntero("Inserisci numero massimo persone per fruitore (1-50): ", 1, 50);
        }
        
        public String leggiTitoloTipoVisita() {
            return InputDati.leggiStringaNonVuota("Inserisci il titolo del tipo visita: ").trim();
        }

        public String leggiDescrizioneTipoVisita() {
            return InputDati.leggiStringaNonVuota("Inserisci una breve descrizione del tipo visita: ").trim();
        }

        public String leggiPuntoDiIncontro() {
            return InputDati.leggiStringaNonVuota("Inserisci il punto di incontro: ").trim();
        }

        public ArrayList<GiorniModel> leggiGiorniSettimana(GiorniController giorniController) {
            return giorniController.scegliGiorniSettimana();
        
        }
        public LocalTime leggiOraInizio() {
            LocalTime oraInizio = null;
            do {
                String ora = InputDati.leggiStringaNonVuota("Inserisci l'orario di inizio (es. 14:30): ").trim();
                try {
                    oraInizio = LocalTime.parse(ora); // Formato: "HH:mm"
                } catch (Exception e) {
                    System.out.println("Formato orario non valido! Devi usare HH:mm, es: 08:30, 15:00, 23:45.");
                    oraInizio = null;
                }
            } while (oraInizio == null);
            return oraInizio;
        }


        public int leggiDurataVisita() {
            return InputDati.leggiInteroPositivo("Inserisci in minuti la durata della visita: ");
        }

        public boolean leggiRichiestaBiglietto() {
            return InputDati.yesOrNo("Inserisci N se NON serve il biglietto, S altrimenti.");
        }

        public int leggiSogliaMinimaPartecipanti() {
            return InputDati.leggiInteroNonNegativo("Inserisci soglia MINIMA dei partecipanti: ");
        }

        public int leggiSogliaMassimaPartecipanti(int min) {
            return InputDati.leggiIntero("Inserisci soglia MASSIMA dei partecipanti: ", min, Integer.MAX_VALUE);
        }

        public PeriodoModel leggiPeriodoProgrammabile(DataController dataController) {
            mostraMessaggio("Definisci periodo programmabile per il tipo visita:");
            DataModel dal = dataController.chiediDataAllUtente(
                "Inserisci giorno di INIZIO", "Inserisci mese di INIZIO");

            DataModel al;
            do {
                al = dataController.chiediDataAllUtente(
                    "Inserisci giorno di FINE", "Inserisci mese di FINE");
                if (al.precede(dal)) {
                    mostraMessaggio("La data di fine deve essere successiva o uguale a quella di inizio! Riprova.");
                }
            } while (al.precede(dal));
            return new PeriodoModel(dal, al);
        }


        public boolean yesOrNo(String domanda) {
            return InputDati.yesOrNo(domanda);
        }

        // Gestione volontari disponibili
        public void stampaVolontariDisponibili(ListaVolontariModel volontariDisponibili) {
            System.out.println("Elenco Volontari disponibili:");
            for (int i = 0; i < volontariDisponibili.getVolontari().size(); i++) {
                System.out.println((i + 1) + ") " + volontariDisponibili.getVolontari().get(i).getCredenziali().getUsername());
            }
        }

        public int scegliVolontario(ListaVolontariModel volontariDisponibili) {
            int scelta;
            do {
                scelta = InputDati.leggiInteroNonNegativo("Inserisci il numero del volontario da associare al tipo visita: ");
                if (scelta < 1 || scelta > volontariDisponibili.getVolontari().size()) {
                    mostraMessaggio("Scelta non valida, riprova.");
                }
            } while (scelta < 1 || scelta > volontariDisponibili.getVolontari().size());
            return scelta;
        }
        
     // === INPUT LUOGO COMPLETO ===

        public String leggiNomeLuogo() {
            return InputDati.leggiStringaNonVuota("Inserisci il nome del luogo: ").trim();
        }

        public String leggiDescrizioneLuogo() {
            return InputDati.leggiStringaNonVuota("Inserisci una descrizione del luogo: ").trim();
        }

        public IndirizzoModel leggiIndirizzo(AmbitoTerritorialeModel ambitoTerritorialeModel) {
            // 1. Menu tipo strada
            TipoStrada[] tipi = TipoStrada.values();
            System.out.println("Seleziona il tipo di strada:");
            for (int i = 0; i < tipi.length; i++) {
                System.out.println((i + 1) + ") " + tipi[i].name());
            }
            int sceltaTipo = InputDati.leggiIntero("Tipo strada: ", 1, tipi.length);
            TipoStrada tipoStrada = tipi[sceltaTipo - 1];

            // 2. Nome strada e civico
            String nomeStrada = InputDati.leggiStringaNonVuota("Inserisci il nome della strada: ").trim();
            String numeroCivico = InputDati.leggiStringaNonVuota("Inserisci il numero civico: ").trim();

            return new IndirizzoModel(tipoStrada, nomeStrada, numeroCivico, ambitoTerritorialeModel);
        }

     // Stampa i tipi visita disponibili (usando ListaTipoVisitaModel)
        public void stampaTipiVisitaDisponibili(ListaTipoVisitaModel tipiDisponibili) {
            System.out.println("Elenco Tipi Visite disponibili:");
            ArrayList<TipoVisitaModel> lista = tipiDisponibili.getTipi();
            for (int i = 0; i < lista.size(); i++) {
                System.out.println((i + 1) + ") " + lista.get(i).toStringSemplificato());
            }
        }

        // Scelta da ListaTipoVisitaModel
        public int scegliTipoVisita(ListaTipoVisitaModel tipiDisponibili) {
            ArrayList<TipoVisitaModel> lista = tipiDisponibili.getTipi();
            int scelta;
            do {
                scelta = InputDati.leggiIntero(
                    "Inserisci il numero del Tipo Visita che vuoi associare al luogo: ",
                    1, lista.size()
                );
            } while (scelta < 1 || scelta > lista.size());
            return scelta;
        }
  
        public void stampaLuoghiConTipoVisita(ListaLuogoModel listaLuogoModel) {
            System.out.println("Elenco luoghi e relativi tipi visita:");
            ArrayList<LuogoModel> luoghi = listaLuogoModel.getListaLuoghi();
            for (LuogoModel luogo : luoghi) {
                System.out.println(luogo.toString()); // Assicurati che LuogoModel mostri anche i tipi visita associati
                // volendo puoi stampare anche luogo.getListaTipoVisita().getTipi() con un for
            }
        }
        
        public boolean confermaInserisciDate(String nomeMese) {
            return InputDati.yesOrNo("Vuoi inserire una data preclusa per il mese di " + nomeMese + "?");
        }

        public int leggiGiornoPrecluso(int giorniNelMese, String nomeMese) {
            return InputDati.leggiIntero("Inserisci il giorno da escludere (1-" + giorniNelMese + ") per " + nomeMese + ": ", 1, giorniNelMese);
        }

        public boolean confermaChiusuraRaccolta() {
            return InputDati.yesOrNo("Vuoi veramente chiudere la raccolta delle disponibilità e delle date precluse?");
        }
        
        public void mostraStato(StatoSistemaModel stato) {
            System.out.println(stato.stampaStatoSistema());
        }
        
        public boolean confermaChiusuraIscrizioniFruitore() {
            return InputDati.yesOrNo("Vuoi davvero chiudere la raccolta iscrizioni fruitori?");
        }
        
        public int scegliNumeroElementi(String messaggio, int count) {
            if (count < 1) throw new IllegalArgumentException("Nessun elemento selezionabile.");
            return InputDati.leggiIntero(messaggio, 1, count);
        }






    }


