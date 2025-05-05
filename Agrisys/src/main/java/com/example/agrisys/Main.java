package com.example.agrisys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Loader Menu.fxml
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent menuRoot = menuLoader.load();
        primaryStage.setTitle("Menu");
        primaryStage.setScene(new Scene(menuRoot));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        // Opretter databaseforbindelse
        // DatabaseConnection.getConnection();

        // Starter applikationen
        launch(args);
    }
}