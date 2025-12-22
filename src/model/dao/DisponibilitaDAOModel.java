package model.dao;

import model.DisponibilitaModel;
import java.sql.*;
import java.util.ArrayList;

public class DisponibilitaDAOModel {
	public void salvaDisponibilita(DisponibilitaModel disponibilita) throws SQLException {
		String queryId = "SELECT ID FROM credenziali WHERE USERNAME = ? AND ID_TIPOUTENTE = 2";
		int idVolontario = -1;

		try (Connection conn = ConnessioneDBModel.startConnection();
				PreparedStatement psId = conn.prepareStatement(queryId)) {
			psId.setString(1, disponibilita.getUsername());
			try (ResultSet rs = psId.executeQuery()) {
				if (rs.next()) {
					idVolontario = rs.getInt("ID");
				} else {
					throw new SQLException("Volontario non trovato");
				}
			}

			String queryIns = "INSERT INTO disponibilita (FK_VOLONTARIO, GIORNO, MESE) VALUES (?, ?, ?)";
			try (PreparedStatement psIns = conn.prepareStatement(queryIns)) {
				psIns.setInt(1, idVolontario);
				psIns.setInt(2, disponibilita.getGiorno());
				psIns.setInt(3, disponibilita.getMese());
				psIns.executeUpdate();
			}
		}
	}

	public ArrayList<DisponibilitaModel> leggiDisponibilitaVolontario(String username) throws SQLException {
		ArrayList<DisponibilitaModel> listaDisponibilita = new ArrayList<>();
		String query = "SELECT d.giorno, d.mese FROM disponibilita d INNER JOIN credenziali c ON d.fk_volontario = c.ID WHERE c.USERNAME = ?";
		try (Connection conn = ConnessioneDBModel.startConnection();
				PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int giorno = rs.getInt("giorno");
				int mese = rs.getInt("mese");
				DisponibilitaModel disponibilita = new DisponibilitaModel(username, giorno, mese);
				listaDisponibilita.add(disponibilita);
			}
		}
		return listaDisponibilita;
	}
}
