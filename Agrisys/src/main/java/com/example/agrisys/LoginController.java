package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button forgotLoginButton;

    public final Map<String, String> users = new HashMap<>();
    public final Map<String, String> roles = new HashMap<>(); // Stores user roles

    @FXML
    public void initialize() {
        // Add a superuser and a normal user
        users.put("MortenSuper", "Babløg");
        roles.put("MortenSuper", "SUPERUSER");

        users.put("normaluser", "normalpassword");
        roles.put("normaluser", "USER");

        loginButton.setOnAction(event -> handleLogin());
        forgotLoginButton.setOnAction(event -> handleForgotLogin());

        // Add Enter key functionality to passwordField
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in both fields.");
        } else if (users.containsKey(username) && users.get(username).equals(password)) {
            String role = roles.get(username);
            showAlert("Success", "Login successful! Role: " + role);

            if ("SUPERUSER".equals(role)) {
                switchToScene("SMenu.fxml");
            } else {
                switchToScene("Menu.fxml");
            }
        } else {
            showAlert("Error", "Invalid username or password.");
        }
    }

    private void handleForgotLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ForgotLogin.fxml"));
            Parent root = loader.load();
            Stage forgotLoginStage = new Stage();
            forgotLoginStage.setTitle("Forgot Login");
            forgotLoginStage.setScene(new Scene(root));
            forgotLoginStage.initOwner(loginButton.getScene().getWindow());
            forgotLoginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Forgot Login window.");
        }
    }

    private void switchToScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the scene: " + fxmlFile);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}