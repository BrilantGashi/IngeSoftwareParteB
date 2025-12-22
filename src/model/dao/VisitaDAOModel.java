package model.dao;

import model.VisitaModel;
import model.VolontarioModel;
import model.TipoVisitaModel;
import model.ListaTipoVisitaModel;
import model.ListaVisiteModel;
import model.DataModel;
import model.ListaVolontariModel;
import model.StatoVisita;
import java.sql.*;
import java.util.ArrayList;


public class VisitaDAOModel {

    public void salvaVisita(VisitaModel visita, VolontarioModel v) throws SQLException {
        String sqlVisita = "INSERT INTO visita (DATA_GIORNO, DATA_MESE, STATO, TITOLO_TIPO_VISITA) VALUES (?, ?, ?, ?)";
        String sqlPonte = "INSERT INTO visita_volontario (FK_VISITA, FK_CREDENZIALI) VALUES (?, ?)";
        int idVolontario = CredenzialiDAOModel.trovaIdUtenteDaNickname(v.getNickname());
        if (idVolontario == -1) throw new SQLException("Volontario non trovato per username: " + v.getNickname());
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement psVisita = conn.prepareStatement(sqlVisita, Statement.RETURN_GENERATED_KEYS);
        ) {
            psVisita.setInt(1, visita.getDataVisita().getGiorno());
            psVisita.setInt(2, visita.getDataVisita().getMese());
            psVisita.setString(3, visita.getStatoVisita().name());
            psVisita.setString(4, visita.getTipoVisita().getTitolo());
            psVisita.executeUpdate();
            int idVisita = -1;
            try (ResultSet generatedKeys = psVisita.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idVisita = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Errore nel recupero ID visita generata.");
                }
            }
            try (PreparedStatement psPonte = conn.prepareStatement(sqlPonte)) {
                psPonte.setInt(1, idVisita);
                psPonte.setInt(2, idVolontario);
                psPonte.executeUpdate();
            }
        }
    }

    public void collegaVolontarioAVisitaDB(VisitaModel visita, VolontarioModel volontario) throws SQLException {
        String queryIdVisita = "SELECT ID FROM visita WHERE DATA_GIORNO = ? AND DATA_MESE = ? AND STATO = ? AND TITOLO_TIPO_VISITA = ?";
        String sqlInsert = "INSERT INTO visita_volontario (FK_VISITA, FK_CREDENZIALI) VALUES (?, ?)";
        int idVolontario = CredenzialiDAOModel.trovaIdUtenteDaNickname(volontario.getNickname());
        if (idVolontario <= 0) throw new SQLException("ID volontario non trovato per nickname: " + volontario.getNickname());
        int idVisita = -1;
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement psFind = conn.prepareStatement(queryIdVisita)) {
            psFind.setInt(1, visita.getDataVisita().getGiorno());
            psFind.setInt(2, visita.getDataVisita().getMese());
            psFind.setString(3, visita.getStatoVisita().name());
            psFind.setString(4, visita.getTipoVisita().getTitolo());
            try (ResultSet rs = psFind.executeQuery()) {
                if (rs.next()) {
                    idVisita = rs.getInt("ID");
                } else {
                    throw new SQLException("Visita non trovata in tabella per i parametri forniti");
                }
            }
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, idVisita);
                psInsert.setInt(2, idVolontario);
                psInsert.executeUpdate();
            }
        }
    }

    public static int trovaIdVisita(VisitaModel visita) throws SQLException {
        String query = "SELECT id FROM Visita WHERE TITOLO_TIPO_VISITA = ? AND DATA_GIORNO = ? AND DATA_MESE = ?";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, visita.getTipoVisita().getTitolo());
            ps.setInt(2, visita.getDataVisita().getGiorno());
            ps.setInt(3, visita.getDataVisita().getMese());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return 0;
    }

    public static void leggiVisiteDaDB(ArrayList<VisitaModel> listaVisite, ListaTipoVisitaModel listaTipoVisita, ListaVolontariModel listaVolontari) throws SQLException {
        String sql = "SELECT ID, DATA_GIORNO, DATA_MESE, STATO, TITOLO_TIPO_VISITA FROM visita";
        String sqlPonte = "SELECT FK_CREDENZIALI FROM visita_volontario WHERE FK_VISITA = ?";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int idVisita = rs.getInt("ID");
                int giorno = rs.getInt("DATA_GIORNO");
                int mese = rs.getInt("DATA_MESE");
                String statoStr = rs.getString("STATO");
                String titoloTipo = rs.getString("TITOLO_TIPO_VISITA");
                TipoVisitaModel tipo = null;
                for (TipoVisitaModel t : listaTipoVisita.getTipi()) {
                    if (t.getTitolo().equals(titoloTipo)) {
                        tipo = t;
                        break;
                    }
                }
                if (tipo == null) continue;
                DataModel data = new DataModel(giorno, mese);
                StatoVisita stato = StatoVisita.valueOf(statoStr);
                ListaVolontariModel volontariEffettivi = new ListaVolontariModel();
                try (PreparedStatement psPonte = conn.prepareStatement(sqlPonte)) {
                    psPonte.setInt(1, idVisita);
                    try (ResultSet rsVol = psPonte.executeQuery()) {
                        while (rsVol.next()) {
                            int idCred = rsVol.getInt("FK_CREDENZIALI");
                            String usernameVol = CredenzialiDAOModel.trovaUsernameFruitoreDaId(idCred);
                            VolontarioModel volontario = listaVolontari.cercaPerNickname(usernameVol);
                            if (volontario != null) {
                                volontariEffettivi.aggiungiVolontario(volontario);
                            }
                        }
                    }
                }
                VisitaModel visita = new VisitaModel(tipo, volontariEffettivi, stato, data);
                listaVisite.add(visita);
            }
        }
    }

    public static VisitaModel trovaVisitaDaId(int id, ArrayList<VisitaModel> listaVisite) throws SQLException {
        String query = "SELECT DATA_GIORNO, DATA_MESE, TITOLO_TIPO_VISITA FROM visita WHERE id = ?";
        int giorno = -1, mese = -1;
        String titoloTipo = null;
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    giorno = rs.getInt("DATA_GIORNO");
                    mese = rs.getInt("DATA_MESE");
                    titoloTipo = rs.getString("TITOLO_TIPO_VISITA");
                }
            }
        }
        for (VisitaModel v : listaVisite) {
            if (v.getDataVisita().getGiorno() == giorno &&
                v.getDataVisita().getMese() == mese &&
                v.getTipoVisita().getTitolo().equals(titoloTipo)) {
                return v;
            }
        }
        return null;
    }
    
    /*public void leggiVisiteDaDB(ListaVisiteModel listaVisiteModel,
            ListaTipoVisitaModel listaTipoVisitaModel,
            ListaVolontariModel listaVolontariModel) throws SQLException {
			String sql = "SELECT ID, DATA_GIORNO, DATA_MESE, STATO, TITOLO_TIPO_VISITA FROM visita";
			String sqlPonte = "SELECT FK_CREDENZIALI FROM visita_volontario WHERE FK_VISITA = ?";
			
			try (Connection conn = ConnessioneDBModel.startConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
			int idVisita = rs.getInt("ID");
			int giorno = rs.getInt("DATA_GIORNO");
			int mese = rs.getInt("DATA_MESE");
			String statoStr = rs.getString("STATO");
			String titoloTipo = rs.getString("TITOLO_TIPO_VISITA");
			
			// Trova il TipoVisitaModel nella lista globale in memoria
			TipoVisitaModel tipo = listaTipoVisitaModel.cercaPerTitolo(titoloTipo);
			if (tipo == null) continue; // ignora visite con tipi non indicizzati
			
			DataModel data = new DataModel(giorno, mese);
			StatoVisita stato = StatoVisita.valueOf(statoStr);
			
			// Leggi volontari effettivi associati (dalla tabella ponte)
			ListaVolontariModel volontariEffettivi = new ListaVolontariModel();
			try (PreparedStatement psPonte = conn.prepareStatement(sqlPonte)) {
			psPonte.setInt(1, idVisita);
			try (ResultSet rsVol = psPonte.executeQuery()) {
			    while (rsVol.next()) {
			        int idCred = rsVol.getInt("FK_CREDENZIALI");
			        String usernameVol = CredenzialiDAOModel.trovaUsernameFruitoreDaId(idCred);
			        VolontarioModel volontario = listaVolontariModel.cercaPerNickname(usernameVol);
			        if (volontario != null) {
			            volontariEffettivi.aggiungiVolontario(volontario);
			        }
			    }
			}
			}
			
			// Crea la visita completa e aggiungila alla lista globale
			VisitaModel visita = new VisitaModel(tipo, volontariEffettivi, stato, data);
			listaVisiteModel.aggiungiVisita(visita);
			}
		}
	}*/
    
	public void leggiVisiteDaDB(ListaVisiteModel listaVisiteModel, 
                                ListaTipoVisitaModel listaTipoVisitaModel, 
                                ListaVolontariModel listaVolontariModel,
                                CredenzialiDAOModel credenzialiDAOModel) throws SQLException {
        String sql = "SELECT ID, DATA_GIORNO, DATA_MESE, STATO, TITOLO_TIPO_VISITA FROM visita";
        String sqlPonte = "SELECT FK_CREDENZIALI FROM visita_volontario WHERE FK_VISITA = ?";

        Connection conn = ConnessioneDBModel.startConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idVisita = rs.getInt("ID");
                int giorno = rs.getInt("DATA_GIORNO");
                int mese = rs.getInt("DATA_MESE");
                String statoStr = rs.getString("STATO");
                String titoloTipo = rs.getString("TITOLO_TIPO_VISITA");

                // 1. Cerca il tipo visita nella lista globale
                TipoVisitaModel tipo = null;
                for (TipoVisitaModel t : listaTipoVisitaModel.getTipi()) {
                    if (t.getTitolo().equals(titoloTipo)) {
                        tipo = t;
                        break;
                    }
                }
                if (tipo == null) continue;

                // 2. Crea data e stato
                DataModel data = new DataModel(giorno, mese);
                StatoVisita stato = StatoVisita.valueOf(statoStr);

                // 3. Leggi volontari associati a questa visita
                ListaVolontariModel volontariEffettiviModel = new ListaVolontariModel();

                try (PreparedStatement psPonte = conn.prepareStatement(sqlPonte)) {
                    psPonte.setInt(1, idVisita);
                    
                    try (ResultSet rsVol = psPonte.executeQuery()) {
                        while (rsVol.next()) {
                            int idCred = rsVol.getInt("FK_CREDENZIALI");
                            
                            // Usa il DAO passato per trovare username (NON crea nuova connessione)
                            String usernameVol = CredenzialiDAOModel.trovaUsernameFruitoreDaId(idCred);

                            // Cerca volontario nella lista globale
                            VolontarioModel volontario = null;
                            for (VolontarioModel v : listaVolontariModel.getVolontari()) {
                                if (v.getCredenziali().getUsername().equals(usernameVol)) {
                                    volontario = v;
                                    break;
                                }
                            }
                            
                            if (volontario != null) {
                                volontariEffettiviModel.aggiungiVolontario(volontario);
                            }
                        }
                    }
                }

                // 4. Crea la visita e aggiungi alla lista globale
                VisitaModel visitaModel = new VisitaModel(tipo, volontariEffettiviModel, stato, data);
                listaVisiteModel.aggiungiVisita(visitaModel);
            }
        }
    }



    public void aggiornaStatoVisitaDB(VisitaModel visita, StatoVisita nuovoStato) throws SQLException {
        String sql = "UPDATE visita SET STATO = ? WHERE DATA_GIORNO = ? AND DATA_MESE = ? AND TITOLO_TIPO_VISITA = ?";
        try (Connection conn = ConnessioneDBModel.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuovoStato.name());
            ps.setInt(2, visita.getDataVisita().getGiorno());
            ps.setInt(3, visita.getDataVisita().getMese());
            ps.setString(4, visita.getTipoVisita().getTitolo());
            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("Nessuna visita aggiornata: controlla i parametri di ricerca.");
            }
        }
    }

    
	    
    
    
}
