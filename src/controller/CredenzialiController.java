package controller;

import java.sql.SQLException;

import model.CredenzialiModel;
import model.dao.CredenzialiDAOModel;
import view.CredenzialiView;

public class CredenzialiController {
    private CredenzialiModel model;
    private CredenzialiDAOModel dao;
    private CredenzialiView view;

    public CredenzialiController(CredenzialiDAOModel dao, CredenzialiView view) {
        this.dao = dao;
        this.view = view;
    }
    
    public CredenzialiModel getModel() {
        return model;
    }
    
    public CredenzialiView getView() {
        return view;
    }
    
    public CredenzialiDAOModel getCredenzialiDAOModel() {
    	return dao;
    }
    
    public boolean loginPerRuolo(String ruoloRichiesto) throws SQLException {
        CredenzialiModel cred = view.leggiCredenziali(ruoloRichiesto);
        this.model = cred;
        boolean accesso = false;
        try {
            accesso = dao.verificaAccesso(cred.getUsername(), cred.getPassword());
        } catch (Exception e) {
            view.mostraMessaggio("Errore di accesso: " + e.getMessage());
            return false;
        }
        if (!accesso) {
            view.mostraMessaggio("Credenziali non valide.");
            return false;
        }
        // Qui controllo che il ruolo sia corretto!
        CredenzialiModel credDB = dao.leggiSingolaCredenziale(cred.getUsername()); 
        if (credDB == null || !ruoloRichiesto.equalsIgnoreCase(credDB.getUtenza())) {
            view.mostraMessaggio("Ruolo non autorizzato! Richiesto: " + ruoloRichiesto);
            return false;
        }
        // Gestione cambio password
        if ("default".equalsIgnoreCase(cred.getPassword())) {
            String nuovaPass = view.leggiNuovaPassword();
            try {
                boolean aggiornata = dao.aggiornaPasswordSuDB(cred.getUsername(), nuovaPass);
                if (aggiornata) {
                    model.setPassword(nuovaPass);
                    view.mostraMessaggio("Password aggiornata con successo!");
                } else {
                    view.mostraMessaggio("Errore durante l'aggiornamento della password.");
                    return false;
                }
            } catch (Exception e) {
                view.mostraMessaggio("Errore DB: " + e.getMessage());
                return false;
            }
        }
        view.mostraMessaggio("Login effettuato.");
        return true;
    }

    public boolean loginUtente(String username) {
        String password = view.leggiNuovaPassword();
        boolean accesso = false;
        try {
            accesso = dao.verificaAccesso(username, password);
            if (accesso) {
                CredenzialiModel cred = dao.leggiSingolaCredenziale(username);
                this.model = cred;
                view.mostraMessaggio("Accesso effettuato.");
            } else {
                view.mostraMessaggio("Password errata.");
            }
        } catch (Exception e) {
            view.mostraMessaggio("Errore di accesso: " + e.getMessage());
            return false;
        }
        return accesso;
    }

    

    public void mostraCredenzialiCorrenti() {
        if (model != null) {
            view.mostraCredenziali(model);
        } else {
            view.mostraMessaggio("Nessun utente autenticato.");
        }
    }
    
    public void logout() {
        model = null;
    }
}
