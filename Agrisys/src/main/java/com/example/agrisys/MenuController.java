package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
            // Create and configure the pig head image
            try {
                ImageView pigHead = new ImageView(new javafx.scene.image.Image(
                        new java.io.File("C:\\Users\\MadsRinggaardKaiser\\OneDrive - Erhvervsakademi MidtVest\\Skrivebord\\Grisehoved.png").toURI().toString()
                ));
                pigHead.setFitWidth(30.0); // Set image width
                pigHead.setFitHeight(30.0); // Set image height
                pigHead.setLayoutX(10.0); // X position for the image
                pigHead.setLayoutY(yPosition - 5.0); // Align with the label

                // Create and configure the KPI label
                Label kpiLabel = new Label(kpi);
                kpiLabel.setLayoutX(50.0); // X position after the image
                kpiLabel.setLayoutY(yPosition); // Y position
                kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                // Add the image and label to the AnchorPane
                AnchorPane.getChildren().addAll(pigHead, kpiLabel);
            } catch (Exception e) {
                System.err.println("Failed to load pig head image: " + e.getMessage());
            }

            yPosition += 40.0; // Increment Y position for the next KPI
        }
    }
}