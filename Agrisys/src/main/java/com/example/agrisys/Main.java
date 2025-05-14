package com.example.agrisys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load Login.fxml
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/com/example/agrisys/Login.fxml"));
        Parent menuRoot = menuLoader.load();
        primaryStage.setTitle("Menu");
        primaryStage.setScene(new Scene(menuRoot));
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            // Establish database connection
            DatabaseManager.getConnection();

            // Launch the application
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}