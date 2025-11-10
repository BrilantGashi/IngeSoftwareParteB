package Versione1;

import java.util.Objects;

public class Luogo {
    private String descrizione;
    private Indirizzo indirizzo;
    private ListaTipoVisita listaTipoVisita;
    private String nomeLuogo;
    private boolean isAttivo;  // ✅ Cambiato da public a private

    /**
     * INVARIANTI DI CLASSE:
     * - nomeLuogo != null && !nomeLuogo.isBlank()
     * - descrizione != null && !descrizione.isBlank()
     * - indirizzo != null
     * - listaTipoVisita != null && listaTipoVisita.getTipi() != null && !listaTipoVisita.getTipi().isEmpty()
     */

    public Luogo() {}

    /**
     * Costruttore con parametri
     * PRECONDIZIONI:
     * - nomeLuogo != null && !nomeLuogo.isBlank()
     * - descrizione != null && !descrizione.isBlank()
     * - indirizzo != null
     * - listaTipoVisita != null && contiene almeno un tipo di visita
     * POSTCONDIZIONI:
     * - i campi dell'oggetto coincidono con i parametri passati
     * - isAttivo == true
     */
    public Luogo(String nomeLuogo, String descrizione, Indirizzo indirizzo, ListaTipoVisita listaTipoVisita) {
        if (nomeLuogo == null || nomeLuogo.isBlank()) throw new IllegalArgumentException("Nome luogo non valido");
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione non valida");
        if (indirizzo == null) throw new IllegalArgumentException("Indirizzo non può essere null");
        if (listaTipoVisita == null || listaTipoVisita.getTipi() == null || listaTipoVisita.getTipi().isEmpty()) {
            throw new IllegalArgumentException("Ogni luogo deve avere almeno un tipo di visita");
        }

        this.nomeLuogo = nomeLuogo;
        this.descrizione = descrizione;
        this.indirizzo = indirizzo;
        this.listaTipoVisita = listaTipoVisita;
        this.isAttivo = true;  // Di default è attivo
        assert invClass() : "Invariante violato nel costruttore";
    }

    public String getDescrizione() { return descrizione; }
    public Indirizzo getIndirizzo() { return indirizzo; }
    public ListaTipoVisita getListaTipoVisita() { return listaTipoVisita; }
    public String getNomeLuogo() { return nomeLuogo; }
    public boolean isAttivo() { return isAttivo; }

    /**
     * PRECONDIZIONE: descrizione != null && !descrizione.isBlank()
     * POSTCONDIZIONE: this.descrizione == descrizione
     */
    public void setDescrizione(String descrizione) {
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("Descrizione non valida");
        this.descrizione = descrizione;
        assert this.descrizione.equals(descrizione) : "Postcondizione violata in setDescrizione";
        assert invClass() : "Invariante violato in setDescrizione";
    }

    /**
     * PRECONDIZIONE: indirizzo != null
     * POSTCONDIZIONE: this.indirizzo == indirizzo
     */
    public void setIndirizzo(Indirizzo indirizzo) {
        Objects.requireNonNull(indirizzo, "Indirizzo non può essere null");
        this.indirizzo = indirizzo;
        assert this.indirizzo == indirizzo : "Postcondizione violata in setIndirizzo";
        assert invClass() : "Invariante violato in setIndirizzo";
    }

    /**
     * PRECONDIZIONE: listaTipoVisita != null && contiene almeno un tipo
     * POSTCONDIZIONE: this.listaTipoVisita == listaTipoVisita
     */
    public void setListaTipoVisita(ListaTipoVisita listaTipoVisita) {
        if (listaTipoVisita == null || listaTipoVisita.getTipi() == null || listaTipoVisita.getTipi().isEmpty()) {
            throw new IllegalArgumentException("Ogni luogo deve avere almeno un tipo di visita");
        }
        this.listaTipoVisita = listaTipoVisita;
        assert this.listaTipoVisita == listaTipoVisita : "Postcondizione violata in setListaTipoVisita";
        assert invClass() : "Invariante violato in setListaTipoVisita";
    }

    /**
     * PRECONDIZIONE: nomeLuogo != null && !nomeLuogo.isBlank()
     * POSTCONDIZIONE: this.nomeLuogo == nomeLuogo
     */
    public void setNomeLuogo(String nomeLuogo) {
        if (nomeLuogo == null || nomeLuogo.isBlank()) throw new IllegalArgumentException("Nome luogo non valido");
        this.nomeLuogo = nomeLuogo;
        assert this.nomeLuogo.equals(nomeLuogo) : "Postcondizione violata in setNomeLuogo";
        assert invClass() : "Invariante violato in setNomeLuogo";
    }

    /**
     * Imposta lo stato di attivazione del luogo.
     * POSTCONDIZIONE: this.isAttivo == attivo
     */
    public void setAttivo(boolean attivo) {
        this.isAttivo = attivo;
        assert this.isAttivo == attivo : "Postcondizione violata in setAttivo";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("===== LUOGO =====\n");
        sb.append("Nome: ").append(nomeLuogo != null ? nomeLuogo : "N/D");
        // ✅ AGGIUNTO stato attivo
        sb.append(isAttivo ? " [ATTIVO]" : " [DISATTIVATO]").append("\n");
        
        sb.append("Descrizione: ").append(descrizione != null ? descrizione : "N/D").append("\n");

        if (indirizzo != null) {
            sb.append("Indirizzo: ").append(indirizzo.toString()).append("\n");
        } else {
            sb.append("Indirizzo: non disponibile\n");
        }

        if (listaTipoVisita != null && listaTipoVisita.getTipi() != null && !listaTipoVisita.getTipi().isEmpty()) {
            sb.append(listaTipoVisita.toString()).append("\n");
        } else {
            sb.append("Nessun tipo di visita disponibile.\n");
        }

        sb.append("=================\n");

        return sb.toString();
    }

    private boolean invClass() {
        if (nomeLuogo == null || nomeLuogo.isBlank()) return false;
        if (descrizione == null || descrizione.isBlank()) return false;
        if (indirizzo == null) return false;
        if (listaTipoVisita == null || listaTipoVisita.getTipi() == null || listaTipoVisita.getTipi().isEmpty()) return false;
        return true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Luogo luogo = (Luogo) obj;
        return Objects.equals(nomeLuogo, luogo.nomeLuogo) &&
               Objects.equals(descrizione, luogo.descrizione) &&
               Objects.equals(indirizzo, luogo.indirizzo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeLuogo, descrizione, indirizzo);
    }
}
