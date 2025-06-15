package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
// Morten
public class DashboardController {

    @FXML
    private Button BackToMenuButton;

//    @FXML
//    private Button SaveDashboard;

    @FXML
    private CheckBox PresetDashboard1;
    @FXML
    private CheckBox PresetDashboard2;
    @FXML
    private CheckBox PresetDashboard3;
    @FXML
    private CheckBox PresetDashboard4;

    @FXML
    public void initialize() {
        // Initialiser en singleton instans fra DashboardState
        DashboardState singleton = DashboardState.getInstance();

        // Event handler for den første Preset knap
        //
        PresetDashboard1.setOnAction(event -> { // holder øje med klik på presetdashboard1
            boolean isSelected = PresetDashboard1.isSelected();//hvis der bliver trykket på den sætter den tilstanden til true eller false
            singleton.setPreset1(isSelected);//her gemmer den tilstanden true eller false i singleton instansen
        });
        PresetDashboard2.setOnAction(event -> {
            boolean isSelected = PresetDashboard2.isSelected();
            singleton.setPreset2(isSelected);
        });
        PresetDashboard3.setOnAction(event -> {
            boolean isSelected = PresetDashboard3.isSelected();
            singleton.setPreset3(isSelected);
        });
        PresetDashboard4.setOnAction(event -> {
            boolean isSelected = PresetDashboard4.isSelected();
            singleton.setPreset4(isSelected);
        });



        BackToMenuButton.setOnAction(e -> HelperMethods.loadScene("SMenu.fxml", BackToMenuButton));
    }
}