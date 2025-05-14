package com.example.agrisys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlserver://mssql17.unoeuro.com";
    private static final String DB_USER = "madserkaiser_dk";
    private static final String DB_PASSWORD = "9dwGEek3ByAbz5FghH6R";

    private Connection connection;

    // Establish a database connection
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

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Disconnected from the database.");
        }
    }

    // Execute a query and return the result
    public ResultSet executeQuery(String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("No active database connection.");
        }
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    // Execute an update (INSERT, UPDATE, DELETE)
    public int executeUpdate(String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("No active database connection.");
        }
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeUpdate();
    }
}