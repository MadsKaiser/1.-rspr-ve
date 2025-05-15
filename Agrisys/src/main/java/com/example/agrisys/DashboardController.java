package com.example.agrisys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class DashboardController {

    @FXML
    private Button BackToMenuButton;

    @FXML
    private CheckBox PresetDashboard1;

    @FXML
    public void initialize() {
        BackToMenuButton.setOnAction(e -> HelperMethods.loadScene("SMenu.fxml", BackToMenuButton));
    }

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
}
