package Versione1;
import java.io.*;
import it.unibs.fp.mylib.*;
import java.util.*;



public class GestioneFile {
	
	 
	File file = new File("credenziali.txt");

	/**
	 * Costruttore vuoto
	 */
	public GestioneFile() {
	}
	
	/**
	 * Cerca una riga nel file delle credenziali in base allo username indicato, per verificare l'esistenza
	 * e di conseguenza la correttezza dello username
	 *
	 * Prerequisito: esiste un File file;   inizializzato
	 *
	 * @param username  username da cercare (colonna 2)
	 * @return array di stringhe con i campi della riga trovata;
	 *         null se lo username non è stato trovato
	 * @throws IOException            problemi di I/O 
	 * @throws FileNotFoundException  se il file non esiste
	 */

	public String[] leggiSingolaCredenziale(String username) throws FileNotFoundException, IOException {
		String[] riga;
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	riga = line.split(";");
		       
		    	//1 perchè è il secondo paramatro della riga -- CONFIGURATORE;utente1;default
		       if (riga[1].equalsIgnoreCase(username)) {
		    	   return riga;		    	   
		       }
		    }
		}
		
		return null;
	}
	
	/**
	 * Cambia la password dell’utente indicato senza alterare altre righe o campi.
	 *
	 * Prerequisito: esiste un campo   private final File file;   inizializzato
	 *
	 * @param username   username da cercare (colonna 2)
	 * @param nuovaPass  nuova password (in chiaro o già hashata) da salvare
	 * @return true  se la password è stata aggiornata;
	 *         false se lo username non è stato trovato
	 * @throws IOException            problemi di I/O (permessi, disco pieno…)
	 * @throws FileNotFoundException  se il file non esiste
	 */
	public boolean aggiornaPasswordSuFile(String username, String nuovaPass)
	        throws FileNotFoundException, IOException {

	    // *** usa il campo "file" già presente ***
	    File tmp = new File(file.getAbsolutePath() + ".tmp");
	    boolean trovato = false;

	    try (BufferedReader br = new BufferedReader(new FileReader(file));
	         BufferedWriter bw = new BufferedWriter(new FileWriter(tmp, false))) {

	        String line;
	        while ((line = br.readLine()) != null) {
	           
	            String[] campi = line.split(";", 3);          // tipo ; username ; password

	            if (campi[1].equalsIgnoreCase(username)) {
	                campi[2] = nuovaPass;                     // cambia SOLO la password
	                trovato = true;
	            }

	            bw.write(String.join(";", campi));            // riscrive la riga
	            bw.newLine();
	        }
	    }

	    if (trovato) {
	        // sostituisce l’originale con il temporaneo
	        if (!file.delete() || !tmp.renameTo(file)) {
	            throw new IOException("Impossibile sostituire il file credenziali");
	        }
	    } else {
	        tmp.delete();   // nessuna modifica: rimuove il temporaneo
	    }

	    return trovato;
	}

	
	public ArrayList restituisciListaUtenza (String utenza) throws FileNotFoundException, IOException {
		String[] riga;
		ArrayList<String> lista = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	riga = line.split(";");
		       
		    	//1 perchè è il secondo paramatro della riga -- CONFIGURATORE;utente1;default
		       if (riga[0].equalsIgnoreCase(utenza)) {
		    	   lista.add(riga[1]);
		       }
		    }
		}
		
		return lista;			
	}
	
	
    public void leggiUtentiDaFile(List<Configuratore> configuratori,
	                              List<Volontario> volontari
	                              /*List<Fruitore> fruitori*/) throws IOException {

	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	            String line;

	            while ((line = br.readLine()) != null) {
	                String[] campi = line.split(";");
	                if (campi.length < 3) continue;

	                String ruolo = campi[0].trim().toUpperCase();
	                String username = campi[1].trim();
	                String password = campi[2].trim();

	                Credenziali cred = new Credenziali(username);
	                
	                switch (ruolo) {
	                    case "CONFIGURATORE":
	                        configuratori.add(new Configuratore(cred));
	                        break;
	                    case "VOLONTARIO":
	                        volontari.add(new Volontario(cred));
	                        break;
	                    case "FRUITORE":
	                        //fruitori.add(new Fruitore(username, password));
	                        break;
	                    default:
	                        System.out.println("Ruolo sconosciuto: " + ruolo);
	                }
	            }
	        }
	    }
	}

	
	
	
	

