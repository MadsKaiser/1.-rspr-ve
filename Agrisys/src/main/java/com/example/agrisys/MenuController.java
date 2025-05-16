package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import static com.example.agrisys.HelperMethods.loadScene;

public class MenuController {
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button LogoutButton;
    @FXML
    private Button AlarmButton;
    @FXML
    private Button ExportCSVButton;

    public void initialize() {
        displaySelectedKPIs();
        LogoutButton.setOnAction(e -> loadScene("Login.fxml", LogoutButton));
        AlarmButton.setOnAction(e -> loadScene("Alarm.fxml", AlarmButton));
        ExportCSVButton.setOnAction(e -> loadScene("Export.fxml", ExportCSVButton));
    }

    private void displaySelectedKPIs() {
        double yPosition = 10.0; // Initial Y position for displaying KPIs
        for (String kpi : KPIStorage.getSavedKPIs()) {
            try {
                // Create and configure the KPI label
                Label kpiLabel = new Label(kpi);
                kpiLabel.setLayoutX(50.0); // X position
                kpiLabel.setLayoutY(yPosition); // Y position
                kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                // Add the label to the AnchorPane
                AnchorPane.getChildren().add(kpiLabel);
            } catch (Exception e) {
                System.err.println("Failed to load KPI: " + e.getMessage());
            }
            yPosition += 40.0; // Increment Y position for the next KPI
        }
    }
}