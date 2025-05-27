package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Map;

import static com.example.agrisys.HelperMethods.loadScene;

public class MenuController {
    @FXML
    private VBox VBoxMenu; // Opdateret fra AnchorPane til VBox
    @FXML
    private VBox InnerVBox; // Tilføjet for at holde KPI'er
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
        InnerVBox.getChildren().clear(); // Ryd eksisterende indhold
        InnerVBox.setSpacing(10); // Tilføj spacing mellem elementer
        InnerVBox.setStyle("-fx-padding: 10; -fx-alignment: top-left;"); // Juster layout

        kpiValues.forEach((kpi, value) -> {
            Label kpiLabel = new Label(kpi + ": " + value);
            kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            InnerVBox.getChildren().add(kpiLabel); // Tilføj KPI'er til VBox
        });
    }
}