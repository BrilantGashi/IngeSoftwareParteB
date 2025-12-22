package app;
import java.util.ArrayList;
import controller.*;
import view.*;
import model.*;
import model.dao.*;

public class MainApp {
    public static void main(String[] args) throws Exception {
        // === MODEL GLOBALI ===
        StatoSistemaModel statoSistema = StatoSistemaModel.PRODUZIONE_VISITE_PROPONIBILI_I_1;
        
        CalendarioModel calendarioModel = new CalendarioModel();
        ListaLuogoModel listaLuogoModel = new ListaLuogoModel();
        ListaTipoVisitaModel listaTipoVisitaModel = new ListaTipoVisitaModel();
        ArrayList<ConfiguratoreModel> listaConfiguratori = new ArrayList<>();
        ListaVolontariModel listaVolontariModel = new ListaVolontariModel();
		CredenzialiDAOModel.leggiVolontariDaDB(listaVolontariModel);
        ListaFruitoreModel listaFruitoreModel = new ListaFruitoreModel();
        CredenzialiDAOModel.leggiFruitoriDaDB(listaFruitoreModel);
        ListaVisiteModel listaVisiteModel = new ListaVisiteModel();
        AmbitoTerritorialeModel ambitoTerritorialeModel = new AmbitoTerritorialeModel();
        ListaIscrizioniModel listaIscrizioniModel = new ListaIscrizioniModel();
        // === DAO GLOBALI ===
        CalendarioDAOModel calendarioDAOModel = new CalendarioDAOModel();
        CredenzialiDAOModel credenzialiDAOModel = new CredenzialiDAOModel();
        DisponibilitaDAOModel disponibilitaDAOModel = new DisponibilitaDAOModel();
        AmbitoTerritorialeDAOModel ambitoTerritorialeDAOModel = new AmbitoTerritorialeDAOModel();
        LuogoDAOModel luogoDAOModel = new LuogoDAOModel();
        TipoVisitaDAOModel tipoVisitaDAOModel = new TipoVisitaDAOModel();
        VisitaDAOModel visitaDAOModel = new VisitaDAOModel();
        IscrizioneDAOModel iscrizioniDAOModel = new IscrizioneDAOModel();

        // === VIEW GLOBALI ===
        StatoSistemaView statoSistemaView = new StatoSistemaView();
        ConfiguratoreView configuratoreView = new ConfiguratoreView();
        VolontarioView volontarioView = new VolontarioView(calendarioModel);
        FruitoreView fruitoreView = new FruitoreView();
        CredenzialiView credenzialiView = new CredenzialiView();
        ListaVolontariView listaVolontariView = new ListaVolontariView();
        ListaLuogoView listaLuogoView = new ListaLuogoView();
        ListaVisiteView listaVisiteView = new ListaVisiteView();
        // === CONTROLLER SPECIALIZZATI ===
        GiorniView giorniView = new GiorniView();
        GiorniController giorniController = new GiorniController(giorniView);
        DataView dataView = new DataView();
        DataController dataController = new DataController(dataView);
        StatoSistemaController statoSistemaController = new StatoSistemaController(statoSistema, statoSistemaView);
        ListaVisiteController listaVisiteController = new ListaVisiteController(listaVisiteModel, listaVisiteView);
        // === CONTROLLER DI RUOLO ===
        VolontarioController volontarioController = new VolontarioController(
            null,
            volontarioView,
            listaTipoVisitaModel,
            listaVolontariModel,
            statoSistemaController,
            disponibilitaDAOModel,
            listaVisiteModel,
            listaIscrizioniModel,
            calendarioModel
        );
        FruitoreController fruitoreController = new FruitoreController(
            null,
            fruitoreView,
             listaFruitoreModel,
			 listaVisiteModel,
			 statoSistemaController,
			 listaVisiteView,
			 listaIscrizioniModel,
			 calendarioModel,
			 iscrizioniDAOModel,
			 visitaDAOModel,
			 listaVisiteController
        );
        ConfiguratoreController configuratoreController = new ConfiguratoreController(
            configuratoreView,
            statoSistemaController,
            listaVolontariModel,
            listaVolontariView,
            listaLuogoModel,
            listaLuogoView,
            listaTipoVisitaModel,
            listaFruitoreModel,
            listaVisiteModel,
            listaVisiteView,
            ambitoTerritorialeDAOModel,
            listaIscrizioniModel,
            volontarioController,
            giorniController,
            dataController,
            tipoVisitaDAOModel,
            luogoDAOModel,
            visitaDAOModel,
            iscrizioniDAOModel,
            calendarioModel,
            calendarioDAOModel,
            disponibilitaDAOModel,
            credenzialiDAOModel
        );

        // === CONTROLLER SISTEMA ===
        CredenzialiController credenzialiController = new CredenzialiController(credenzialiDAOModel, credenzialiView);
        LoginController loginController = new LoginController(
            credenzialiController,
            listaFruitoreModel,
            configuratoreController,
            volontarioController,
            fruitoreController
        );
        MenuView menuView = new MenuView();
        MenuController menuController = new MenuController(menuView, loginController);

        // === AVVIO DEL CICLO PRINCIPALE ===
        menuController.avviaMenuPrincipale();
    }
}
