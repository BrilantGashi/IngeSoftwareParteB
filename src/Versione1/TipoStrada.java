package Versione1;
import java.util.Scanner;

public enum TipoStrada {
    Corso, Localita, Piazza, Strada, Via, Viale, Vicolo;

    /**
     * INVARIANTI DI CLASSE:
     * - L'insieme dei valori è fisso e non modificabile (Corso, Localita, Piazza, Strada, Via, Viale, Vicolo).
     * - Ogni valore ha un nome non nullo e univoco.
     */

    /**
     * Chiede all'utente di digitare il nome del tipo di strada (case-insensitive)
     * e restituisce l'enum corrispondente.
     *
     * PRECONDIZIONI:
     * - L'input dell'utente non deve essere null o vuoto.
     *
     * POSTCONDIZIONI:
     * - Ritorna un valore non null di TipoStrada.
     * - Il valore ritornato appartiene all'insieme definito da TipoStrada.values().
     *
     * @return il TipoStrada selezionato
     */
    public static TipoStrada scegliDaNome() {
        Scanner sc = new Scanner(System.in);

        stampaStrada();

        TipoStrada scelta = null;
        boolean valido = false;

        while (!valido) {
            System.out.print("Inserisci il tipo di strada: ");
            String input = sc.nextLine().trim();

            // Precondizione: input non vuoto
            if (input.isEmpty()) {
                System.out.println("Errore: input vuoto. Riprova.");
                continue;
            }

            // Normalizzo: prima lettera maiuscola, resto minuscolo
            String normalizzato = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();

            try {
                scelta = TipoStrada.valueOf(normalizzato);
                valido = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Errore: valore non valido. Riprova.");
            }
        }

        // Postcondizioni
        assert scelta != null : "Postcondizione violata: scelta è null";
        boolean appartiene = false;
        for (TipoStrada t : TipoStrada.values()) {
            if (t == scelta) {
                appartiene = true;
                break;
            }
        }
        assert appartiene : "Postcondizione violata: scelta non appartiene a TipoStrada";

        return scelta;
    }

    /**
     * Stampa tutti i tipi di strada disponibili.
     *
     * PRECONDIZIONI:
     * - Nessuna (il metodo non dipende da input esterni).
     *
     * POSTCONDIZIONI:
     * - Vengono stampati tutti i valori definiti in TipoStrada.values().
     */
    public static void stampaStrada() {
        System.out.println("Tipi di strada disponibili:");
        for (TipoStrada t : TipoStrada.values()) {
            System.out.println("- " + t);
        }
    }
}
