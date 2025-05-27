package Versione1;

import java.io.*;
import it.unibs.fp.mylib.*;
import java.util.*;

public class Utente {
	
	Credenziali credenziali = new Credenziali();

	public Utente(Credenziali credenziali) {
		super();
		this.credenziali = credenziali;
	}

	public Credenziali getCredenziali() {
		return credenziali;
	}

	public void setCredenziali(Credenziali credenziali) {
		this.credenziali = credenziali;
	}
	
	//devo popolare credenziali leggendo dal file

	//******************************************
	//PRIMA DI FARE QUALSIASI COSA, devo creare un metodo che dal file mi legga utente, username e password, 
	//e lo setti come variabili in questa classe
	//DOMANDA: lo devo fare qui?
	//*****************************************************************s
	
	
	
	
}
