package com.example.agrisys;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
// Benjamin
public class HelperMethods {
    //Metode der kan kaldes og den loader den FXML-fil der er givet som parameter
    public static void loadScene(String fxmlFile, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(HelperMethods.class.getResource("/com/example/agrisys/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            System.err.println("Failed to load the scene: " + fxmlFile);
            ex.printStackTrace();
            Alert2("Error", "Failed to load the scene: " + fxmlFile);
        }
    }
    //Metode der kan kaldes og viser en alert med en titel og en besked
    public static void Alert2(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}