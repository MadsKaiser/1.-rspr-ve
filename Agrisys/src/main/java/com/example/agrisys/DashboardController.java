package com.example.agrisys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML
    private Button BackToMenuButton;
    @FXML
    private CheckBox PresetDashboard1;

    @FXML
    private void handleDashboardCheckBox() {
        if (PresetDashboard1.isSelected()) {
            System.out.println("Preset Dashboard 1 selected!");
            // Add logic for activating widgets here
        } else {
            System.out.println("Preset Dashboard 1 deselected!");
            // Add logic for deactivating widgets here
        }
    }

    @FXML
    void BackToMenuButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the menu scene.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}