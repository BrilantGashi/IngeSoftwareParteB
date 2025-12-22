package controller;

import view.MenuView;

public class MenuController {
	private final MenuView menuView;
	private final LoginController loginController;

	public MenuController(MenuView menuView, LoginController loginController) {
		this.menuView = menuView;
		this.loginController = loginController;
	}

	public void avviaMenuPrincipale() throws Exception {
		boolean fineProgramma = false;

		// Eventuale caricamento dati/DAO/servizi...
		
		while (!fineProgramma) {
			int scelta = menuView.mostraMenuPrincipale();

			switch (scelta) {
			case 0:
				fineProgramma = menuView.chiediConfermaUscita();
				if (fineProgramma) menuView.mostraMessaggioFinale();;
				break;
			case 1:
				loginController.loginConfiguratore();
				break;
			case 2:
				loginController.loginVolontario();
				break;
			case 3:
				loginController.loginFruitore();
				break;
			default:
				break;
			}
		}
	}
}
