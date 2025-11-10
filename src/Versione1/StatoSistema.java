package Versione1;

public enum StatoSistema {
    PRODUZIONE_VISITE_PROPONIBILI_I_1,      
    PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA,
    GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE, 
    GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE_COMPLETATA,
    RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1,
    RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1_COMPLETATA,
    RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3,
    RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3_COMPLETATA; 

    public String stampaStatoSistema() {
        switch (this) {
            case PRODUZIONE_VISITE_PROPONIBILI_I_1:
                return "In attesa della generazione delle visite proponibili .";
            case PRODUZIONE_VISITE_PROPONIBILI_I_1_COMPLETATA:
                return "Produzione delle visite proponibili completata.\nIn attesa delle richieste di modifica.";
            case GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE:
                return "In attesa delle richieste di aggiunta/rimozione.";
            case GESTIONE_RICHIESTE_AGGIUNTA_RIMOZIONE_COMPLETATA:
                return "Gestione delle richieste di modifica del corpo dati conclusa.\nIn attesa della raccolta delle iscrizioni del fruitore.";
            case RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1:
                return "In attesa della raccolta delle iscrizioni del fruitore.";
            case RACCOLTA_ISCRIZIONI_VISITE_FRUITORE_I1_COMPLETATA:
                return "Raccolta delle iscrizioni dei fruitori alle visite completata.\nIn attesa della raccolta delle disponibilità dei volontari e della raccolta delle date precluse.";
            case RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3:
                return "In attesa della raccolta delle disponibilità dei volontari e della raccolta delle date precluse.";
            case RACCOLTA_DISPONIBILITA_I_2_DATE_PRECLUSE_I_3_COMPLETATA:
                return "Raccolta delle disponibilità dei volontari e delle date precluse conclusa.";
            default:
                return "Stato non riconosciuto.";
        }
    }
}
