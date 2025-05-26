package com.example.agrisys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager { //En konstant med en databaseforbindelse
    private static final String DB_URL = "jdbc:sqlserver://mssql17.unoeuro.com;databaseName=madserkaiser_dk_db_agrisys";
    private static final String DB_USER = "madserkaiser_dk";
    private static final String DB_PASSWORD = "9dwGEek3ByAbz5FghH6R";

    private Connection connection;

    //Opretter en forbindelse til databasen
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database.");
            return connection;
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            return null;
        }
    }
    //Afbryder forbindelsen til databasen Bruges?
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Disconnected from the database.");
        }
    }

    //Udføre en forespørgsel og returner et resultat Bruges?
    public ResultSet executeQuery(String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("No active database connection.");
        }
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    //Udføre en opdatering og returner antallet af rækker påvirket Bruges?
    public int executeUpdate(String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("No active database connection.");
        }
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeUpdate();
    }
}