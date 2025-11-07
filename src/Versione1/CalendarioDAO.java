package Versione1;
import java.sql.*;
import java.util.ArrayList;

public class CalendarioDAO {

    //=======================================================================
    //*********************** SCRITTURA *************************************
    //=======================================================================
    
    /**
     * Salva una data esclusa nel database.
     * @param giorno Il giorno del mese (1-31)
     * @param meseNumero Il numero del mese (1-12)
     */
    public  void salvaDataPreclusa(int giorno, int meseNumero) throws SQLException {
        String insertSql = "INSERT INTO date_escluse (GIORNO, MESE_GENERALE) VALUES (?, ?)";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(insertSql)) {
            
            ps.setInt(1, giorno);
            ps.setInt(2, meseNumero);
            
            ps.executeUpdate();
            System.out.println("✓ Data esclusa salvata: " + giorno + "/" + meseNumero);
            
        } catch (SQLException e) {
            System.out.println("❌ Errore durante il salvataggio: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Salva tutte le date escluse di un calendario nel database.
     * Elimina prima le date esistenti per il mese, poi inserisce le nuove.
     */
    public static void salvaDateEscluseCalendario(Calendario calendario) throws SQLException {
        try (Connection conn = ConnessioneDB.startConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Salva le date del mese i+1
                int meseNumero1 = calendario.getMeseNumero(calendario.getMeseI1());
                salvaDatePrecluseMese(conn, calendario.getDatePreclusePerMese(1), meseNumero1);
                
                // Salva le date del mese i+2
                int meseNumero2 = calendario.getMeseNumero(calendario.getMeseI2());
                salvaDatePrecluseMese(conn, calendario.getDatePreclusePerMese(2), meseNumero2);
                
                // Salva le date del mese i+3
                int meseNumero3 = calendario.getMeseNumero(calendario.getMeseI3());
                salvaDatePrecluseMese(conn, calendario.getDatePreclusePerMese(3), meseNumero3);
                
                conn.commit();
                System.out.println("✓ Tutte le date escluse sono state salvate con successo!");
                
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("❌ Errore durante il salvataggio: " + e.getMessage());
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    /**
     * Salva le date precluse per un singolo mese.
     * Prima elimina le date esistenti per quel mese, poi inserisce le nuove.
     */
    private static void salvaDatePrecluseMese(Connection conn, ArrayList<Integer> datePrecluse, int meseNumero) throws SQLException {
        // 1. Elimina le date esistenti per questo mese
        String deleteSql = "DELETE FROM date_escluse WHERE MESE_GENERALE = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            ps.setInt(1, meseNumero);
            int righeEliminate = ps.executeUpdate();
            System.out.println("Eliminate " + righeEliminate + " date esistenti per il mese " + meseNumero);
        }
        
        // 2. Inserisci le nuove date
        if (!datePrecluse.isEmpty()) {
            String insertSql = "INSERT INTO date_escluse (GIORNO, MESE_GENERALE) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                for (Integer giorno : datePrecluse) {
                    ps.setInt(1, giorno);
                    ps.setInt(2, meseNumero);
                    ps.addBatch();
                }
                int[] risultati = ps.executeBatch();
                System.out.println("✓ Salvate " + risultati.length + " date per il mese " + meseNumero);
            }
        }
    }
    
     

    //=======================================================================
    //*********************** LETTURA ***************************************
    //=======================================================================
    
    /**
     * Legge tutte le date escluse dal database e popola il Calendario.
     * @param calendario Il calendario da popolare con le date escluse
     */
    /**
     * Legge tutte le date escluse dal database e popola il Calendario.
     * MESE_GENERALE indica quale lista riempire: 1=I_1, 2=I_2, 3=I_3
     * @param calendario Il calendario da popolare con le date escluse
     */
    public  void leggiCalendarioDaDB(Calendario calendario) throws SQLException {
        String selectSql = "SELECT GIORNO, MESE_GENERALE FROM calendario ORDER BY MESE_GENERALE, GIORNO";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int giorno = rs.getInt("GIORNO");
                int meseGenerale = rs.getInt("MESE_GENERALE");
                
                // meseGenerale = 1 → datePrecluse_I_1
                // meseGenerale = 2 → datePrecluse_I_2
                // meseGenerale = 3 → datePrecluse_I_3
                
                if (meseGenerale == 1) {
                    calendario.aggiungiDataPreclusa(giorno, 1);
                } else if (meseGenerale == 2) {
                    calendario.aggiungiDataPreclusa(giorno, 2);
                } else if (meseGenerale == 3) {
                    calendario.aggiungiDataPreclusa(giorno, 3);
                } else {
                    System.out.println("Valore del mese di riferimento non valido: " + meseGenerale);
                }
            }
            
            System.out.println("Calendario caricato dal database");
            
        } catch (SQLException e) {
            System.out.println("Errore durante la lettura del calendario: " + e.getMessage());
            throw e;
        }
    }

    
    /**
     * Legge le date escluse per un singolo mese.
     * @param meseNumero Il numero del mese (1-12)
     * @return Lista dei giorni esclusi per quel mese
     */
    public static ArrayList<Integer> leggiDateEscluseMese(int meseNumero) throws SQLException {
        ArrayList<Integer> dateEscluse = new ArrayList<>();
        String selectSql = "SELECT GIORNO FROM date_escluse WHERE MESE_GENERALE = ? ORDER BY GIORNO";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql)) {
            
            ps.setInt(1, meseNumero);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dateEscluse.add(rs.getInt("GIORNO"));
                }
            }
            
            System.out.println("✓ Letti " + dateEscluse.size() + " giorni esclusi per il mese " + meseNumero);
            
        } catch (SQLException e) {
            System.out.println("❌ Errore durante la lettura: " + e.getMessage());
            throw e;
        }
        
        return dateEscluse;
    }

    //=======================================================================
    //*********************** ELIMINAZIONE **********************************
    //=======================================================================
    
    /**
     * Elimina una singola data esclusa dal database.
     * @param giorno Il giorno da eliminare
     * @param meseNumero Il numero del mese (1-12)
     */
    public static void eliminaDataEsclusa(int giorno, int meseNumero) throws SQLException {
        String deleteSql = "DELETE FROM date_escluse WHERE GIORNO = ? AND MESE_GENERALE = ?";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            
            ps.setInt(1, giorno);
            ps.setInt(2, meseNumero);
            
            int righe = ps.executeUpdate();
            
            if (righe > 0) {
                System.out.println("✓ Data esclusa eliminata: " + giorno + "/" + meseNumero);
            } else {
                System.out.println("⚠️  Nessuna data trovata da eliminare");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Errore durante l'eliminazione: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Elimina tutte le date escluse per un determinato mese.
     * @param meseNumero Il numero del mese (1-12)
     */
    public static void eliminaTutteDateMese(int meseNumero) throws SQLException {
        String deleteSql = "DELETE FROM date_escluse WHERE MESE_GENERALE = ?";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            
            ps.setInt(1, meseNumero);
            
            int righe = ps.executeUpdate();
            System.out.println("✓ Eliminate " + righe + " date per il mese " + meseNumero);
            
        } catch (SQLException e) {
            System.out.println("❌ Errore durante l'eliminazione: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Elimina TUTTE le date escluse dal database.
     */
    public static void eliminaTutteDateEscluse() throws SQLException {
        String deleteSql = "DELETE FROM date_escluse";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            
            int righe = ps.executeUpdate();
            System.out.println("✓ Eliminate tutte le " + righe + " date escluse dal database");
            
        } catch (SQLException e) {
            System.out.println("❌ Errore durante l'eliminazione: " + e.getMessage());
            throw e;
        }
    }

    //=======================================================================
    //*********************** UTILITÀ ***************************************
    //=======================================================================
    
    /**
     * Verifica se una data è esclusa.
     * @param giorno Il giorno da verificare
     * @param meseNumero Il numero del mese (1-12)
     * @return true se la data è esclusa, false altrimenti
     */
    public static boolean isDataEsclusa(int giorno, int meseNumero) throws SQLException {
        String selectSql = "SELECT COUNT(*) FROM date_escluse WHERE GIORNO = ? AND MESE_GENERALE = ?";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql)) {
            
            ps.setInt(1, giorno);
            ps.setInt(2, meseNumero);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
}
