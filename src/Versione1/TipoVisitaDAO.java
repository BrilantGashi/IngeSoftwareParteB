package Versione1;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;


public class TipoVisitaDAO {

	//=======================================================================
	//*********************** INSERIMENTO ***********************************
	//=======================================================================
	
	public void inserisciVisita (TipoVisita visita) throws SQLException {

		//PERIODO
		int idPeriodo = inserisciPeriodo(visita.getPeriodoProgrammabile());
		
		//TIPO VISITA
		int idTipoVisitaGenerato = -1;
		
		String insertSql = "INSERT INTO tipo_visita "
                + "(TITOLO, DESCRIZIONE, ORA_INIZIO, DURATA_VISITA, BIGLIETTO, SOGLIA_MIN_PART, SOGLIA_MAX_PART, PUNTO_INCONTRO, FK_PERIODO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        /*try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
        	conn.setAutoCommit(false);
        	
            ps.setString(1, visita.getTitolo());
            ps.setString(2, visita.getDescrizione());
            //ps.setDate(3, java.sql.Date.valueOf(visita.getPeriodoProgrammabile().getDataInizio()));
            //ps.setDate(4, java.sql.Date.valueOf(visita.getPeriodoProgrammabile().getDataFine()));
            //ps.setString(3, visita.getGiorniSettimanaProgrammabile());
            ps.setTime(3, java.sql.Time.valueOf(visita.getOraInizio()));
            ps.setInt(4, visita.getDurataVisita());
            ps.setBoolean(5, visita.isBiglietto());
            ps.setInt(6, visita.getSogliaMinPartecipanti());
            ps.setInt(7, visita.getSogliaMaxPartecipanti());
            ps.setString(8, visita.getPuntoDiIncontro());
            ps.setInt(9, idPeriodo);
            
            //SE IL TIPO VISITA VA A BUON FINE...
            int righeInserite = ps.executeUpdate();                       
            if (righeInserite > 0) {
	            try (ResultSet keys = ps.getGeneratedKeys()) {
	                if (keys.next()) {
	                	idTipoVisitaGenerato = keys.getInt(1);
	                    System.out.println("ID generato: " + idTipoVisitaGenerato);
	                    
	                    
	                    //...COLLEGO I GIORNI DELLA VISITA
	                    collegaGiorniATipoVisita(idTipoVisitaGenerato, visita.getGiorniSettimanaProgrammabile());
	                    
	                    //... E AGGIUNGO I VOLONTARI
	                    collegaVolontariATipoVisita(idTipoVisitaGenerato, visita.getListaVolontari());
	                } else {
	                    System.out.println("Nessun ID restituito.");
	                }
	            }
	        }

	        System.out.println("Tipo Visita inserito con successo!");


        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento tipo visita: " + e.getMessage());
            //return false;
        }*/
		try (Connection conn = ConnessioneDB.startConnection()) {
		    conn.setAutoCommit(false); // Disabilita auto-commit per gestire transazioni

		    try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

		        ps.setString(1, visita.getTitolo());
		        ps.setString(2, visita.getDescrizione());
		        ps.setTime(3, java.sql.Time.valueOf(visita.getOraInizio()));
		        ps.setInt(4, visita.getDurataVisita());
		        ps.setBoolean(5, visita.isBiglietto());
		        ps.setInt(6, visita.getSogliaMinPartecipanti());
		        ps.setInt(7, visita.getSogliaMaxPartecipanti());
		        ps.setString(8, visita.getPuntoDiIncontro());
		        ps.setInt(9, idPeriodo);

		        int righeInserite = ps.executeUpdate();

		        if (righeInserite > 0) {
		            try (ResultSet keys = ps.getGeneratedKeys()) {
		                if (keys.next()) {
		                    idTipoVisitaGenerato = keys.getInt(1);
		                    System.out.println("ID generato: " + idTipoVisitaGenerato);

		                    // Passa la connessione esplicitamente per evitare aprirne un'altra
		                    collegaGiorniATipoVisita(conn, idTipoVisitaGenerato, visita.getGiorniSettimanaProgrammabile());
		                    collegaVolontariATipoVisita(conn, idTipoVisitaGenerato, visita.getListaVolontari());

		                    conn.commit(); // Conferma la transazione solo se tutto ok
		                    System.out.println("Tipo Visita inserito con successo!");
		                } else {
		                    System.out.println("Nessun ID restituito.");
		                    conn.rollback();
		                }
		            }
		        } else {
		            conn.rollback();
		        }
		    } catch (SQLException e) {
		        conn.rollback(); // rollback in caso di errore
		        System.out.println("Errore durante l'inserimento tipo visita: " + e.getMessage());
		        throw e; // rilancia o gestisci in altro modo
		    } finally {
		        conn.setAutoCommit(true); // Ripristina auto-commit eventualmente per riutilizzo
		    }

		} catch (SQLException e) {
		    e.printStackTrace();
		}

    }
		
	public int inserisciPeriodo (Periodo periodo) throws SQLException {
		
		String insertQuery = "INSERT INTO periodo_programmabile(DATA_INIZIO, DATA_FINE) VALUES (?,?)";
		Date sqlDateInizio = createSqlDate(periodo.getDataInizio());
		Date sqlDateFine = createSqlDate(periodo.getDataFine());
		
		int idGenerato = -1;
		
		try (Connection conn = ConnessioneDB.startConnection();
		     //PreparedStatement ps = conn.prepareStatement(insertQuery)
			PreparedStatement ps = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);) {

			    ps.setDate(1, sqlDateInizio);
			    ps.setDate(2, sqlDateFine);
			    int righe = ps.executeUpdate();
			    
			    if (righe > 0) {
		            try (ResultSet keys = ps.getGeneratedKeys()) {
		                if (keys.next()) {
		                    idGenerato = keys.getInt(1);
		                    System.out.println("ID generato: " + idGenerato);
		                } else {
		                    System.out.println("Nessun ID restituito.");
		                }
		            }
		        }

		        System.out.println("Periodo inserito con successo!");
		    } catch (SQLException e) {
		        System.out.println("Errore: " + e.getMessage());
		        throw e; // rilancio per gestioni superiori
		    }

		    return idGenerato;
	}
	
	public static Date createSqlDate(Data data) {	      
	
	    LocalDate localDate = LocalDate.of(Year.now().getValue(), data.getMese(), data.getGiorno());
	    return Date.valueOf(localDate);
	}
	
    public void collegaGiorniATipoVisita(Connection conn, int idTipoVisita, ArrayList<Giorni> giorniList) throws SQLException {
        String selectIdGiorni = "SELECT id FROM giorni WHERE nome = ?";
        String insertPonte = "INSERT INTO tipo_visita_giorni (fk_tipovisita, fk_giorni) VALUES (?, ?)";

        //try (Connection conn = ConnessioneDB.startConnection()) {
            //conn.setAutoCommit(false); // Gestione transazionale per coerenza

            try (PreparedStatement psSelect = conn.prepareStatement(selectIdGiorni);
                 PreparedStatement psInsert = conn.prepareStatement(insertPonte)) {

                for (Giorni giornoEnum : giorniList) {
                    // Cerca l'ID del giorno nella tabella giorni
                    psSelect.setString(1, giornoEnum.name()); // usa il nome enum come stringa
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (rs.next()) {
                            int idGiorno = rs.getInt("id");

                            // Inserisci nella tabella ponte
                            psInsert.setInt(1, idTipoVisita);
                            psInsert.setInt(2, idGiorno);
                            psInsert.executeUpdate();
                        } else {
                            System.out.println("Giorno non trovato nel DB: " + giornoEnum.name());
                        }
                    }
                }
                conn.commit();
                System.out.println("Associazioni giorni-tipo visita inserite con successo.");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        //}
    }
	 
    public void collegaVolontariATipoVisita(Connection conn, int idTipoVisita, ListaVolontari lista) throws SQLException {
        String selectIdVolontario = "SELECT id FROM credenziali WHERE username = ?";
        String checkEsistenza = "SELECT COUNT(*) FROM tipovisita_volontario WHERE fk_tipovisita = ? AND fk_volontario = ?";
        String insertAssociazione = "INSERT INTO tipovisita_volontario (fk_tipovisita, fk_volontario) VALUES (?, ?)";

        
        //try (Connection conn = ConnessioneDB.startConnection()) {
            //conn.setAutoCommit(false); // transazione manuale

            try (PreparedStatement psSelect = conn.prepareStatement(selectIdVolontario);
            		PreparedStatement psCheck = conn.prepareStatement(checkEsistenza);
            		PreparedStatement psInsert = conn.prepareStatement(insertAssociazione)) {

                for (Volontario volontario : lista.getVolontari()) {
                    // Cerca l'ID del volontario in base all'username
                    psSelect.setString(1, volontario.getNickname());
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (rs.next()) {
                            int idVolontario = rs.getInt("id");

                            
                            psCheck.setInt(1, idTipoVisita);
                            psCheck.setInt(2, idVolontario);
                            try (ResultSet rsCheck = psCheck.executeQuery()) {
                                rsCheck.next();
                                int count = rsCheck.getInt(1);
                                
                                if (count == 0) {
                                	// Inserisce nella tabella ponte
                                	psInsert.setInt(1, idTipoVisita);
                                	psInsert.setInt(2, idVolontario);
                                	psInsert.executeUpdate();

                                	System.out.println("Collegato volontario " + volontario.getNickname() + 
                                					" (ID: " + idVolontario + ") al tipo visita " + idTipoVisita);
                                }else {
                                	System.out.println(" Volontario " + volontario.getNickname() +
                                    " già collegato a tipo visita " + idTipoVisita);
                                }
                            }
                        } else {
                        	System.out.println("Nessun volontario trovato per username: " + volontario.getNickname());
                        }
                    }
                }
                    
                conn.commit();
                System.out.println("Tutti i volontari sono stati collegati con successo!");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Errore durante l'inserimento volontari: " + e.getMessage());
                throw e;
            }
        //}
    }
	 
    public void gestisciOrfani (TipoVisita visita, Volontario v) throws SQLException {

    	String selectIdTipoVisitaSql = "SELECT ID FROM tipo_visita WHERE TITOLO = ?";
        try (Connection conn = ConnessioneDB.startConnection()) {
            conn.setAutoCommit(false);

            try {
                // Recupera ID tipo visita dal titolo
                int idTipoVisita;
                try (PreparedStatement psTipoVisita = conn.prepareStatement(selectIdTipoVisitaSql)) {
                    psTipoVisita.setString(1, visita.getTitolo());
                    try (ResultSet rs = psTipoVisita.executeQuery()) {
                        if (rs.next()) {
                            idTipoVisita = rs.getInt("ID");
                        } else {
                            throw new SQLException("Tipo Visita non trovato per titolo: " + visita.getTitolo());
                        }
                    }
                }

                // Chiama il metodo che associa volontario a tipo visita passando connessione e lista con un solo volontario
                //ArrayList<Volontario> listaVolontariSingolo = new ArrayList<>();
                //listaVolontariSingolo.add(v); // Assumi costruttore con username
                ListaVolontari listaV = new ListaVolontari();
                listaV.aggiungiVolontario(v);
                collegaVolontariATipoVisita(conn, idTipoVisita, listaV);

                conn.commit();
                System.out.println("Volontario " + v.getNickname() + " associato correttamente alla visita \"" + visita.getTitolo() + "\".");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    	
    }

  
    //=======================================================================
  	//*********************** LETTURA**** ***********************************
  	//=======================================================================
    
 // Legge tutti i TipoVisita dal database
    public ListaTipoVisita leggiTuttiTipiVisita(ListaVolontari listaV) throws SQLException, FileNotFoundException, IOException {
    	ListaTipoVisita listaTipi = new ListaTipoVisita();
        //ArrayList<TipoVisita> listaTipi = new ArrayList<>();
        
        String sql = "SELECT tv.ID, tv.TITOLO, tv.DESCRIZIONE, tv.ORA_INIZIO, tv.DURATA_VISITA, " +
                     "tv.BIGLIETTO, tv.SOGLIA_MIN_PART, tv.SOGLIA_MAX_PART, tv.PUNTO_INCONTRO, " +
                     "tv.FK_PERIODO, p.DATA_INIZIO, p.DATA_FINE " +
                     "FROM tipo_visita tv " +
                     "JOIN periodo_programmabile p ON tv.FK_PERIODO = p.ID";
        
        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int idTipoVisita = rs.getInt("ID");
                String titolo = rs.getString("TITOLO");
                String descrizione = rs.getString("DESCRIZIONE");
                LocalTime oraInizio = rs.getTime("ORA_INIZIO").toLocalTime();
                int durataVisita = rs.getInt("DURATA_VISITA");
                boolean biglietto = rs.getBoolean("BIGLIETTO");
                int sogliaMin = rs.getInt("SOGLIA_MIN_PART");
                int sogliaMax = rs.getInt("SOGLIA_MAX_PART");
                String puntoIncontro = rs.getString("PUNTO_INCONTRO");
                
                // Periodo
                java.sql.Date dataInizio = rs.getDate("DATA_INIZIO");
                java.sql.Date dataFine = rs.getDate("DATA_FINE");
                Periodo periodo = new Periodo(
                    new Data(dataInizio.toLocalDate().getDayOfMonth(), dataInizio.toLocalDate().getMonthValue()),
                    new Data(dataFine.toLocalDate().getDayOfMonth(), dataFine.toLocalDate().getMonthValue())
                );
                
                // Leggi giorni associati
                ArrayList<Giorni> giorniList = leggiGiorniPerTipoVisita(conn, idTipoVisita);
                
                // Leggi volontari associati
                ListaVolontari volontari = leggiVolontariPerTipoVisita(conn, idTipoVisita, listaV);
                
                // Crea oggetto TipoVisita
                TipoVisita tipoVisita = new TipoVisita(titolo, descrizione, puntoIncontro, giorniList, 
                                                        oraInizio, durataVisita, biglietto, 
                                                        sogliaMin, sogliaMax, periodo, volontari);
                
                listaTipi.add(tipoVisita);
            }
        }
        
        return listaTipi;
    }
 // Metodo ausiliario per leggere i volontari associati a un tipo visita
    private ListaVolontari leggiVolontariPerTipoVisita(Connection conn, int idTipoVisita, ListaVolontari listaV) throws SQLException, FileNotFoundException, IOException {
        ListaVolontari listaVolontari = new ListaVolontari();
        UtenteDAO UtenteDAO = new UtenteDAO();
        String sql = "SELECT c.USERNAME FROM credenziali c " +
                     "JOIN tipovisita_volontario tv ON c.ID = tv.fk_volontario " +
                     "WHERE tv.fk_tipovisita = ?";
        
        // Lista temporanea per salvare gli username
        ArrayList<String> usernameList = new ArrayList<>();
        
        // Prima fase: leggi tutti gli username dal ResultSet
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoVisita);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("USERNAME");
                    usernameList.add(username);
                }
            }
        } // Qui il ResultSet viene chiuso
        
        // Seconda fase: crea gli oggetti Volontario dagli username salvati
        /*for (String username : usernameList) {
            Credenziali cred = UtenteDAO.leggiSingolaCredenziale(username);
            listaVolontari.aggiungiVolontario(new Volontario(cred));
        }*/
        
        for (String username : usernameList) {
            Volontario v = listaV.cercaPerNickname(username);
            if (v != null) {
                listaVolontari.aggiungiVolontario(v);
            } else {
                //System.out.println("ATTENZIONE: Volontario con username " + username + " NON trovato nella lista globale!");
                // Se vuoi puoi anche gestire eventuali errori/creazioni qui
            }
        }
        
        
        
        return listaVolontari;
    }
 // Metodo ausiliario per leggere i giorni associati a un tipo visita
    private ArrayList<Giorni> leggiGiorniPerTipoVisita(Connection conn, int idTipoVisita) throws SQLException {
        ArrayList<Giorni> giorniList = new ArrayList<>();
        String sql = "SELECT g.NOME FROM giorni g " +
                     "JOIN tipo_visita_giorni tvg ON g.ID = tvg.fk_giorni " +
                     "WHERE tvg.fk_tipovisita = ?";
        
        // Lista temporanea per salvare i nomi dei giorni
        ArrayList<String> nomiGiorni = new ArrayList<>();
        
        // Prima fase: leggi tutti i nomi dal ResultSet
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoVisita);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nomeGiorno = rs.getString("NOME");
                    nomiGiorni.add(nomeGiorno);
                }
            }
        } // Qui il ResultSet viene chiuso
        
        // Seconda fase: converti i nomi in enum Giorni
        for (String nome : nomiGiorni) {
            giorniList.add(Giorni.valueOf(nome));
        }
        
        return giorniList;
    }

    //metodo per popolare listaVolontari
    
    

}
