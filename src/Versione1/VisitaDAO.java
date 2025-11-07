package Versione1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VisitaDAO {

    // Salva una visita effettiva nel DB con i campi reali (ID volontario!)
	public void salvaVisita(Visita visita, String username_Volontario_iesimo) throws SQLException {
	    String sql = "INSERT INTO visita (data_giorno, data_mese, stato, titolo_tipo_visita, fk_volontario) VALUES (?, ?, ?, ?, ?)";

	    // Supponiamo che VolontarioDAO/UtenteDAO abbia:
	    // public int trovaIdVolontarioDaNickname(String nickname)
	    int idVolontario = UtenteDAO.trovaIdVolontarioDaNickname(username_Volontario_iesimo);

	    try (Connection conn = ConnessioneDB.startConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, visita.getDataVisita().getGiorno());
	        ps.setInt(2, visita.getDataVisita().getMese());
	        ps.setString(3, visita.getStatoVisita().name());
	        ps.setString(4, visita.getTipoVisita().getTitolo());
	        ps.setInt(5, idVolontario);

	        ps.executeUpdate();
	    }
	}


    // Leggi tutte le visite dal database (adatta ai nomi reali dei campi...)
    public ListaVisite leggiTutteVisite(ListaTipoVisita listaTipi, ListaVolontari listaVol) throws SQLException {
        ListaVisite lista = new ListaVisite();
        String sql = "SELECT id, data_giorno, data_mese, stato, titolo_tipo_visita, fk_volontario FROM visita";

        try (Connection conn = ConnessioneDB.startConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int giorno = rs.getInt("data_giorno");
                int mese = rs.getInt("data_mese");
                String statoStr = rs.getString("stato");
                String titoloTipo = rs.getString("titolo_tipo_visita");
                int idVol = rs.getInt("fk_volontario");

                StatoVisita stato = StatoVisita.valueOf(statoStr);
                TipoVisita tipo = listaTipi.cercaPerTitolo(titoloTipo);
                Volontario vol = listaVol.cercaPerId(idVol);

                if (tipo != null && vol != null) {
                    Data data = new Data(giorno, mese);
                    Visita visita = new Visita(tipo, vol, stato, data);
                    lista.aggiungiVisita(visita);
                } else {
                    System.out.println("Attenzione: tipo Visita o Volontario non trovati.");
                }
            }
        }
        return lista;
    }
}
