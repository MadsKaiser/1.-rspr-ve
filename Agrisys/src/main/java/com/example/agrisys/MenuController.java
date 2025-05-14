package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Button LogoutButton;

    public void initialize() {
        displaySavedKPIs();

        LogoutButton.setOnAction(e -> loadScene("Login.fxml", LogoutButton));
    }

    private void displaySavedKPIs() {
        double yPosition = 10.0;
        for (String kpi : KPIStorage.getSavedKPIs()) {
            Label kpiLabel = new Label(kpi);
            kpiLabel.setLayoutX(10.0);
            kpiLabel.setLayoutY(yPosition);
            kpiLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            AnchorPane.getChildren().add(kpiLabel);
            yPosition += 30.0;
        }
    }

    private void goBack() {
        // Logic to navigate back to the previous scene
    }

    private void loadScene(String fxmlFile, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
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