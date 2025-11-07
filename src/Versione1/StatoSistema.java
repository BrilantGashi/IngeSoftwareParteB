package Versione1;

public enum StatoSistema {
    PRODUZIONE_VISITE_PROPONIBILI_I_1,      
    PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA,
    GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE, 
    GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE_COMPLETATA,
    RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1,
    RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1_COMPLETATA,
    RACCOLTA_DISPONIBILITA_I_2,
    RACCOLTA_DISPONIBILITA_I_2_COMPLETATA,   
    RACCOLTA_DATE_PRECLUSE_I_3,
    RACCOLTA_DATE_PRECLUSE_I_3_COMPLETATA; 

    public String stampaStatoSistema() {
        switch (this) {
            case PRODUZIONE_VISITE_PROPONIBILI_I_1:
                return "In attesa della generazione delle visite proponibili: .";
            case PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA:
                return "Produzione delle visite proponibili completata. In attesa delle richieste di modifica.";
            case GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE:
                return "In attesa delle richieste di aggiunta/rimozione.";
            case GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE_COMPLETATA:
                return "Gestione delle richieste di modifica del corpo dati conclusa. In attesa della raccolta delle iscrizioni del fruitore.";
            case RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1:
                return "In attesa della raccolta delle iscrizioni del fruitore.";
            case RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1_COMPLETATA:
                return "Raccolta delle iscrizioni dei fruitori alle visite completata. In attesa della raccolta delle disponibilità dei volontari.";
            case RACCOLTA_DISPONIBILITA_I_2:
                return "In attesa della raccolta delle disponibilità dei volontari.";
            case RACCOLTA_DISPONIBILITA_I_2_COMPLETATA:
                return "Raccolta delle disponibilità dei volontari conclusa. In attesa della raccolta delle date precluse.";
            case RACCOLTA_DATE_PRECLUSE_I_3:
                return "In attesa della raccolta delle date precluse.";
            case RACCOLTA_DATE_PRECLUSE_I_3_COMPLETATA:
                return "Raccolta delle date precluse completata.";
            default:
                return "Stato non riconosciuto.";
        }
    }
}
