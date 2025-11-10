package Versione1;

import java.sql.*;

public class ConnessioneDB {
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
                System.out.println("Connessione chiusa.");
            }
        } catch (SQLException e) {
            System.out.println("Errore nella chiusura della connessione: " + e.getMessage());
        }
    }
}


/*package Versione1;

import java.sql.*;


public class ConnessioneDB {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Dati di connessione
        String url = "jdbc:mysql://localhost:3306/ingesw"; // nome del DB
        String user = "root"; // utente predefinito XAMPP
        String password = ""; // di solito vuota

        //try {
            // 1. Carica il driver JDBC (non sempre necessario con versioni recenti)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Crea la connessione
            Connection conn = DriverManager.getConnection(url, user, password);

            System.out.println("Connessione riuscita al database!");

            // 3. Usa la connessione per query (facoltativo)
            // ... codice per Statement o PreparedStatement ...
            
            Statement stm = conn.createStatement();
            
            
            //exec query
            String query = "SELECT * FROM tipologiautente";
            ResultSet rs = ((java.sql.Statement) stm).executeQuery(query);
            
            while (rs.next()) {
            	System.out.println(rs.getString("ID") + " " + rs.getString("TIPO") );
            }

            // 4. Chiudi la connessione
            conn.close();
/*
        } catch (ClassNotFoundException e) {
            System.out.println(" Driver JDBC non trovato: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(" Errore di connessione: " + e.getMessage());
        }
    }
}*/


