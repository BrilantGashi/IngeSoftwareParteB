package controller;

import model.CredenzialiModel;
import model.FruitoreModel;
import model.ListaFruitoreModel;
import model.dao.CredenzialiDAOModel;

public class LoginController {
    private final CredenzialiController credenzialiController;
    private final ListaFruitoreModel listaFruitori;
    private final ConfiguratoreController configuratoreController;
    private final VolontarioController volontarioController;
    private final FruitoreController fruitoreController;

    public LoginController(
        CredenzialiController credenzialiController,
        ListaFruitoreModel listaFruitori,
        ConfiguratoreController configuratoreController,
        VolontarioController volontarioController,
        FruitoreController fruitoreController
    ) {
        this.credenzialiController = credenzialiController;
        this.listaFruitori = listaFruitori;
        this.configuratoreController = configuratoreController;
        this.volontarioController = volontarioController;
        this.fruitoreController = fruitoreController;
    }
 
    // ACCEDI CONFIGURATORE
    public void loginConfiguratore() throws Exception {
        boolean logged = credenzialiController.loginPerRuolo("CONFIGURATORE");
        if (logged) {
            configuratoreController.menuConfiguratore(credenzialiController.getModel().getUsername());
        }
    }

    // ACCEDI VOLONTARIO
    public void loginVolontario() throws Exception {
        boolean logged = credenzialiController.loginPerRuolo("VOLONTARIO");
        if (logged) {
            volontarioController.menuVolontario(credenzialiController.getModel().getUsername());
        }
    }

    // ACCEDI/CREA FRUITORE
    public void loginFruitore() throws Exception {
        credenzialiController.logout();
        String username = credenzialiController.getView().leggiUsername();
        CredenzialiModel existing = credenzialiController.getCredenzialiDAOModel().leggiSingolaCredenziale(username);

        if (existing == null) {
            String password = credenzialiController.getView().leggiNuovaPassword();
            CredenzialiModel nuoveCred = new CredenzialiModel("FRUITORE", username, password);
            FruitoreModel nuovoFruitore = new FruitoreModel(nuoveCred);
            listaFruitori.aggiungiFruitore(nuovoFruitore);
            credenzialiController.getCredenzialiDAOModel();
			CredenzialiDAOModel.aggiungiNuovoFruitore(nuovoFruitore);
            credenzialiController.getView().mostraMessaggio("Nuovo fruitore creato con successo.");
            fruitoreController.menuFruitore(username);
        } else if ("FRUITORE".equals(existing.getUtenza())) {
            boolean logged = credenzialiController.loginUtente(existing.getUsername());
            if (logged) {
                fruitoreController.menuFruitore(existing.getUsername());
            }
        } else {
            credenzialiController.getView().mostraMessaggio("Username già assegnato a un altro ruolo.");
        }
    }

}
