package com.example.agrisys;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HelperMethods {

    public static void loadScene(String fxmlFile, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(HelperMethods.class.getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            System.err.println("Failed to load the scene: " + fxmlFile);
            ex.printStackTrace();
        }
    }
}