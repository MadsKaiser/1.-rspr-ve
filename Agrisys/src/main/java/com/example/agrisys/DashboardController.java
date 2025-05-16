package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class DashboardController {

    @FXML
    private Button BackToMenuButton;

    @FXML
    private Button SaveDashboard;

    @FXML
    private CheckBox Widget1, Widget2, PresetDashboard1;

    @FXML
    public void initialize() {
        // Add checkboxes to the singleton
        DashboardState singleton = DashboardState.getInstance();

        // Event handler for PresetDashboard1
        PresetDashboard1.setOnAction(event -> {
            boolean isSelected = PresetDashboard1.isSelected();
            singleton.setIsPreset(isSelected);
        });

        // Back to menu button
        BackToMenuButton.setOnAction(e -> HelperMethods.loadScene("SMenu.fxml", BackToMenuButton));

    }
}