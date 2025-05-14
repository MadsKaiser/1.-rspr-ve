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

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button forgotLoginButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        forgotLoginButton.setOnAction(event -> HelperMethods.loadScene("ForgotLogin.fxml", forgotLoginButton));

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

        HashMap<String, String> users = UserManager.getInstance().getUsers();
        HashMap<String, String> roles = UserManager.getInstance().getRoles();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in both fields.");
        } else if (users.containsKey(username) && users.get(username).equals(password)) {
            String role = roles.get(username);
            UserManager.getInstance().setCurrentUser(username);
            showAlert("Success", "Login successful! Role: " + role);
            UserManager.getInstance().setCurrentUser(username);
            if ("SUPERUSER".equals(role)) {
                HelperMethods.loadScene("SMenu.fxml", loginButton);
            } else {
                HelperMethods.loadScene("Menu.fxml", loginButton);
            }
        } else {
            showAlert("Error", "Invalid username or password.");


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