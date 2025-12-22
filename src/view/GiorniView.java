package view;


import it.unibs.fp.mylib.InputDati;
public class GiorniView {

    public void mostraTitoloIniziale() {
        System.out.println("Inserisci i giorni della settimana in cui è possibile effettuare la visita.");
        System.out.println("Usa i numeri da 1 a 7 (1=Lunedì, ..., 7=Domenica).");
    }

    public int scegliGiorno() {
        return InputDati.leggiIntero("Inserire giorno: ", 1, 7);
    }

    public void mostraErroreGiorno() {
        System.out.println("Giorno non valido! Riprova.");
    }

    public void mostraGiornoAggiunto(String nomeGiorno) {
        System.out.println("Aggiunto: " + nomeGiorno);
    }

    public void mostraGiornoGiaInserito(String nomeGiorno) {
        System.out.println("Giorno già inserito: " + nomeGiorno);
    }

    public boolean chiediAggiungiAltro() {
        return InputDati.yesOrNo("Vuoi inserire un altro giorno? S per inserire, N per uscire.");
    }
}
