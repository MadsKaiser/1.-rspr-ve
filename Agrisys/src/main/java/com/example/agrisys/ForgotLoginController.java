package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ForgotLoginController {

    @FXML
    private TextField emailField;

    @FXML
    private void handleSubmit() {
        String email = emailField.getText();
        if (email.isEmpty()) {
            showAlert("Error", "Please enter your email.");
        } else {
            showAlert("Info", "Recovery instructions sent to: " + email);
        }
    }
    // Samme komment som i ExportController - Skriver her hvis jeg skulle glemme det
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}