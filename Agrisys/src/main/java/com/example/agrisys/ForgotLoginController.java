package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ForgotLoginController {

    @FXML
    private TextField emailField;

    // Ændret til at bruge methods fra HelperMethods klassen = Mindre redundans
    @FXML
    private void handleSubmit() {
        String email = emailField.getText();
        if (email.isEmpty()) {
            HelperMethods.Alert2("Error", "Please enter your email.");
        } else {
            HelperMethods.Alert2("Info", "Recovery instructions sent to: " + email);
        }
    }
}