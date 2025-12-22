package model.dao;

import java.sql.*;

public class ConnessioneDBModel {
	// Dati di connessione
    private static final String URL = "jdbc:mysql://localhost:3306/ingesw";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection conn = null;

    // Metodo per ottenere la connessione (singleton)
    public static Connection startConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
            	// 1. Carica il driver JDBC (non sempre necessario con versioni recenti)
                Class.forName("com.mysql.cj.jdbc.Driver");
                // 	2. Crea la connessione
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                System.out.println("Driver JDBC non trovato: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Errore di connessione: " + e.getMessage());
            }
        }
        return conn;
    }

    // Metodo per chiudere la connessione
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Errore nella chiusura della connessione: " + e.getMessage());
        }
    }
}



