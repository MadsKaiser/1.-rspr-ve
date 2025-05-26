package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.Map;

import static com.example.agrisys.HelperMethods.loadScene;

public class MenuController {
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button LogoutButton;
    @FXML
    private Button AlarmButton;
    @FXML
    private Button ExportCSVButton;

    public void initialize() {
        loadKPIs(KPIStorage.getSavedKPIsWithValues());
        LogoutButton.setOnAction(e -> loadScene("Login.fxml", LogoutButton));
        AlarmButton.setOnAction(e -> loadScene("Alarm.fxml", AlarmButton));
        ExportCSVButton.setOnAction(e -> loadScene("Export.fxml", ExportCSVButton));
    }
    public void loadKPIs(Map<String, String> kpiValues) {
        kpiValues.forEach((kpi, value) -> {
            Label kpiLabel = new Label(kpi + ": " + value);
            kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            AnchorPane.getChildren().add(kpiLabel);
        });
    }
}