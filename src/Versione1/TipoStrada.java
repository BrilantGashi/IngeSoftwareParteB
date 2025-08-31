package Versione1;
import java.util.Scanner;

public enum TipoStrada {
	Via, Viale, Vicolo, Strada, Corso, Piazza, Località; 


    /**
     * Chiede all'utente di digitare il nome del tipo di strada (case-insensitive)
     * e restituisce l'enum corrispondente.
     *
     * @return il TipoStrada selezionato
     */
    public static TipoStrada scegliDaNome() {
        Scanner sc = new Scanner(System.in);

        /*// Mostro tutte le opzioni disponibili
        System.out.println("Tipi di strada disponibili:");
        for (TipoStrada t : TipoStrada.values()) {
            System.out.println("- " + t);
        }*/

        stampaStrada();
        
        TipoStrada scelta = null;
        boolean valido = false;

        while (!valido) {
            System.out.print("Inserisci il tipo di strada: ");
            String input = sc.nextLine().trim();

            // Normalizzo: prima lettera maiuscola, resto minuscolo
            String normalizzato = input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();

            try {
                scelta = TipoStrada.valueOf(normalizzato);
                valido = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Errore: valore non valido. Riprova.");
            }
        }

        return scelta;
    }
    
    public static void stampaStrada () {
    	// Mostro tutte le opzioni disponibili
        System.out.println("Tipi di strada disponibili:");
        for (TipoStrada t : TipoStrada.values()) {
            System.out.println("- " + t);
        }
    }
}
