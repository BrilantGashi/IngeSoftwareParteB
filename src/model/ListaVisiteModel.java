package model;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

import model.dao.VisitaDAOModel;

public class ListaVisiteModel {
    private ArrayList<VisitaModel> listaVisite;

    public ListaVisiteModel() {
        this.listaVisite = new ArrayList<>();
    }

    public void aggiungiVisita(VisitaModel v) {
        Objects.requireNonNull(v, "La visita non può essere null");
        listaVisite.add(v);
    }

    public ArrayList<VisitaModel> getListaVisite() {
        return new ArrayList<>(listaVisite);
    }

    public void setListaVisite(ArrayList<VisitaModel> listaVisite) {
        Objects.requireNonNull(listaVisite);
        for (VisitaModel v : listaVisite) {
            if (v == null) throw new IllegalArgumentException("Elemento null");
        }
        this.listaVisite = new ArrayList<>(listaVisite);
    }

    public void rimuoviVisita(VisitaModel v) {
        Objects.requireNonNull(v);
        listaVisite.remove(v);
    }

    public ArrayList<VisitaModel> filtraPerStato(StatoVisita stato) {
        Objects.requireNonNull(stato);
        ArrayList<VisitaModel> risultato = new ArrayList<>();
        for (VisitaModel v : listaVisite) {
            if (v.getStatoVisita() == stato) risultato.add(v);
        }
        return risultato;
    }

    public ArrayList<VisitaModel> filtraPerVolontario(VolontarioModel v) {
        Objects.requireNonNull(v);
        ArrayList<VisitaModel> risultato = new ArrayList<>();
        for (VisitaModel visita : listaVisite) {
            if (visita.getVolontariEffettivi().getVolontari().contains(v)) {
                risultato.add(visita);
            }
        }
        return risultato;
    }

    public void aggiungiDisponibilita(VolontarioModel v, TipoVisitaModel tipoVisita, DataModel data) {
        Objects.requireNonNull(v);
        Objects.requireNonNull(tipoVisita);
        Objects.requireNonNull(data);
        VisitaModel esistente = trovaVisita(tipoVisita, data);
        if (esistente != null) {
            esistente.aggiungiVolontario(v);
        } else {
            VisitaModel nuova = new VisitaModel(tipoVisita, v, StatoVisita.PROPOSTA, data);
            listaVisite.add(nuova);
        }
    }

    public VisitaModel trovaVisita(TipoVisitaModel tipoVisita, DataModel data) {
        for (VisitaModel v : listaVisite) {
            if (v.getTipoVisita().equals(tipoVisita)
                    && v.getDataVisita().equals(data)) {
                return v;
            }
        }
        return null;
    }

    public boolean visiteUnichePerTipoEData() {
        for (int i = 0; i < listaVisite.size(); i++) {
            VisitaModel v1 = listaVisite.get(i);
            for (int j = i + 1; j < listaVisite.size(); j++) {
                VisitaModel v2 = listaVisite.get(j);
                if (v1.getTipoVisita().equals(v2.getTipoVisita())
                        && v1.getDataVisita().equals(v2.getDataVisita())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean volontarioGiaAssegnatoQuelGiorno(VolontarioModel volontario, DataModel data) {
        for (VisitaModel visita : listaVisite) {
            if (visita.getDataVisita().equals(data) &&
                visita.getVolontariEffettivi().getVolontari().contains(volontario)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean visitaSovrapposta(TipoVisitaModel tipoDaAggiungere, DataModel giorno, ListaLuogoModel listaLuogoModel) {
        // Trova il luogo associato al tipo visita che vuoi aggiungere
        LuogoModel luogoCorrente = listaLuogoModel.trovaLuogoPerTipoVisita(tipoDaAggiungere);
        if (luogoCorrente == null) return false;

        LocalTime inizioA = tipoDaAggiungere.getOraInizio();
        LocalTime fineA = inizioA.plusMinutes(tipoDaAggiungere.getDurataVisita());

        for (VisitaModel visitaEsistente : getListaVisite()) {
            LuogoModel luogoVisitaEsistente = listaLuogoModel.trovaLuogoPerTipoVisita(visitaEsistente.getTipoVisita());
            if (luogoCorrente.equals(luogoVisitaEsistente) && visitaEsistente.getDataVisita().equals(giorno)) {
                LocalTime inizioB = visitaEsistente.getTipoVisita().getOraInizio();
                LocalTime fineB = inizioB.plusMinutes(visitaEsistente.getTipoVisita().getDurataVisita());
                // Verifica intervallo orario
                boolean sovrapposti = !(fineA.isBefore(inizioB) || inizioA.isAfter(fineB));
                if (sovrapposti) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void aggiornaStatoVisite(ListaIscrizioniModel listaIscrizioniFruitore, VisitaDAOModel visitaDAOModel) throws SQLException {
        // Protezione contro il NullPointerException
        if (listaIscrizioniFruitore == null) {
            listaIscrizioniFruitore = new ListaIscrizioniModel(); // Crea una lista vuota se arriva null
        }

        for (VisitaModel visita : this.getListaVisite()) {
            int sogliaMinima = visita.getTipoVisita().getSogliaMinPartecipanti();
            
            // Se la lista è vuota, questo metodo restituirà 0
            int numeroIscritti = listaIscrizioniFruitore.getTotaleIscrittiPerVisita(visita);

            // GESTIONE STATI
            if (numeroIscritti == 0) {
                // Se non c'è nessuno, la visita è sicuramente CANCELLATA
                visita.setStatoVisita(StatoVisita.CANCELLATA);
                visitaDAOModel.aggiornaStatoVisitaDB(visita, StatoVisita.CANCELLATA);
            } else if (numeroIscritti >= sogliaMinima) {
                // Soglia raggiunta o superata
                visita.setStatoVisita(StatoVisita.CONFERMATA);
                visitaDAOModel.aggiornaStatoVisitaDB(visita, StatoVisita.CONFERMATA);
            } else {
                // Ci sono iscritti ma meno della soglia minima
                visita.setStatoVisita(StatoVisita.CANCELLATA);
                visitaDAOModel.aggiornaStatoVisitaDB(visita, StatoVisita.CANCELLATA);
            }
        }
    }

    public ArrayList<VisitaModel> getVisiteProposteDisponibili(String meseNome, FruitoreModel fruitore, ListaIscrizioniModel listaIscrizioni) {
        Objects.requireNonNull(meseNome);
        Objects.requireNonNull(fruitore);

        ArrayList<VisitaModel> proposte = filtraPerStato(StatoVisita.PROPOSTA);
        ArrayList<VisitaModel> disponibili = new ArrayList<>();

        for (VisitaModel v : proposte) {
            DataModel data = v.getDataVisita();
            if (data != null && meseNome.equalsIgnoreCase(Mesi.getNomeMese(data.getMese()))) {
                boolean giaIscritto = listaIscrizioni.trovaPerFruitoreEVisita(fruitore, v) != null;
                int tot = listaIscrizioni.getTotaleIscrittiPerVisita(v);
                int max = v.getTipoVisita().getSogliaMaxPartecipanti();
                if (!giaIscritto && tot < max) {
                    disponibili.add(v);
                }
            }
        }
        return disponibili;
    }

    

    public boolean contains(VisitaModel v) {
        return listaVisite.contains(v);
    }
    
    
}
