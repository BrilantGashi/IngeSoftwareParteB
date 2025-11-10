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

	public String toStringUsername() {
		// TODO Auto-generated method stub
		
		return credenziali.getUsername();
	}
	
}
