package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
// Morten
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


        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
    }
    // Ændret til at bruge methods fra HelperMethods klassen = Mindre redundans
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        HashMap<String, String> users = UserManager.getInstance().getUsers();
        HashMap<String, String> roles = UserManager.getInstance().getRoles();

        if (username.isEmpty() || password.isEmpty()) {
            HelperMethods.Alert2("Error", "Please fill in both fields.");
        } else if (users.containsKey(username) && users.get(username).equals(password)) {
            String role = roles.get(username);
            UserManager.getInstance().setCurrentUser(username);
            HelperMethods.Alert2("Success", "Login successful! Role: " + role);

            if ("SUPERUSER".equals(role)) {
                HelperMethods.loadScene("SMenu.fxml", loginButton);
            } else {
                HelperMethods.loadScene("Menu.fxml", loginButton);
            }
        } else {
            HelperMethods.Alert2("Error", "Invalid username or password.");
        }
    }
}
