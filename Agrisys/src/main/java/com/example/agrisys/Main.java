package com.example.agrisys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Loader Login FXML filen og sætter den som scene i primaryStage
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent menuRoot = menuLoader.load();
        primaryStage.setTitle("Menu");
        primaryStage.setScene(new Scene(menuRoot,800,600));
        primaryStage.show();
    }
    public static void main(String[] args) {
        try {
            //Opretter databaseforbindelse
            DatabaseManager.getConnection();

            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}