package Versione1;

import java.io.*;
import it.unibs.fp.mylib.*;
import java.util.*;


public class Main {
	
	public static void main(String[] args) throws Exception {
				boolean fineProgramma = false;
				do {
					fineProgramma = Menu.menuPrincipale();
				} while (!fineProgramma);
				System.out.println("Arrivederci!");
	}
}
