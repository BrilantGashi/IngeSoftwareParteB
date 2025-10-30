package Versione1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VisitaDAO {
	
	//salva la visita effettiva nel DB legata a tipo visita e volontario
	
	 public void salvaVisita(Visita visita) throws SQLException {
	        String sql = "INSERT INTO visita (data_giorno, data_mese, stato, titolo_tipo_visita, username_volontario) VALUES (?, ?, ?, ?, ?)";

	        try (Connection conn = ConnessioneDB.startConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setInt(1, visita.getDataVisita().getGiorno());
	            ps.setInt(2, visita.getDataVisita().getMese());
	            ps.setString(3, visita.getStatoVisita().name());
	            ps.setString(4, visita.getTipoVisita().getTitolo());
	            ps.setString(5, visita.getVolontariEffettivi().get(0).getNickname());

	            ps.executeUpdate();
	
	        }
	    }
	
	// legge tutte le visite dal database
   
	 public ArrayList<Visita> leggiTutteVisite(ListaTipoVisita listaTipi, ListaVolontari listaVol) throws SQLException {
	        ArrayList<Visita> lista = new ArrayList<>();
	        String sql = "SELECT dataGiorno, dataMese, stato, titoloTipoVisita, usernameVolontario FROM visita";

	        try (Connection conn = ConnessioneDB.startConnection();
	             PreparedStatement ps = conn.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {
	                int giorno = rs.getInt("datagiorno");
	                int mese = rs.getInt("dataMese");
	                String statoStr = rs.getString("stato");
	                String titoloTipo = rs.getString("titoloTipoVisita");
	                String usernameVol = rs.getString("usernameVolontario");

	                StatoVisita stato = StatoVisita.valueOf(statoStr);
	                TipoVisita tipo = listaTipi.cercaPerTitolo(titoloTipo);
	                Volontario vol = listaVol.cercaPerNickname(usernameVol);

	                if (tipo != null && vol != null) {
	                    Data data = new Data(giorno, mese);
	                    Visita visita = new Visita(tipo, vol, stato, data);
	                    lista.add(visita);
	                } else {
	                    System.out.println("Attenzione: tipo Visita o Volontario non trovati.");
	                }
	            }
	        }
	        return lista;
	}

 
   
	

}
