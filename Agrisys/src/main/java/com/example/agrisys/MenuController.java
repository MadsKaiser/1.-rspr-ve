package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Button AlarmButton;
    @FXML
    private Button CSVButton;

    public void initialize() {
        AlarmButton.setOnAction(e -> {
            try {
                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Alarm.fxml"));
                Parent root = loader.load();

                // Get the current window (stage) from the button
                Stage stage = (Stage) AlarmButton.getScene().getWindow();

                // Set the new scene
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
