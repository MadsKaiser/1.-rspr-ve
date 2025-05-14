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
    // Button eksistere ikke på dashboard???
    @FXML
    private void initilize() {
        BackToMenuButton.setOnAction(e -> HelperMethods.loadScene("SMenu.fxml", BackToMenuButton));
    }
}

