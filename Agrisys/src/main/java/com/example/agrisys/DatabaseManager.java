package com.example.agrisys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// Morten
public class DatabaseManager { //En konstant med en databaseforbindelse
    private static final String DB_URL = "jdbc:sqlserver://mssql17.unoeuro.com;databaseName=madserkaiser_dk_db_agrisys";
    private static final String DB_USER = "madserkaiser_dk";
    private static final String DB_PASSWORD = "9dwGEek3ByAbz5FghH6R";

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
}