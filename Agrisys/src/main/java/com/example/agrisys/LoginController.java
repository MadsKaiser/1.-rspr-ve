package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

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
    // Måske kig på det her? Ved ikke om der er en nemmere løsning
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
//       Dette skal nok fikses på en eller andet måde, ved ikke hvordan det skal være sat op
//       Håber Morten fikser det :)

//       if (fxmlFile.equals("SMenu.fxml")) {
//        stage.setMaximized(true); // eller stage.setFullScreen(true);
//            }
//
//                    stage.show();
//        } catch (IOException e) {
//        e.printStackTrace();
//showAlert("Error", "Failed to load the scene: " + fxmlFile);
//        }
//                }