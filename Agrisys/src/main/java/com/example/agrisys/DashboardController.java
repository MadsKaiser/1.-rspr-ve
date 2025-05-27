package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class DashboardController {

    @FXML
    private Button BackToMenuButton;

    @FXML
    private Button SaveDashboard;

    @FXML
    private CheckBox PresetDashboard1;

    @FXML
    public void initialize() {
        // Initialiser en singleton instans fra DashboardState
        DashboardState singleton = DashboardState.getInstance();

        // Event handler for den første Preset knap
        PresetDashboard1.setOnAction(event -> {
            boolean isSelected = PresetDashboard1.isSelected();
            singleton.setIsPreset(isSelected);
        });

        BackToMenuButton.setOnAction(e -> HelperMethods.loadScene("SMenu.fxml", BackToMenuButton));
    }
}