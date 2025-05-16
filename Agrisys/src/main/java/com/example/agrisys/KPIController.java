package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KPIController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private CheckBox KPI1, KPI2, KPI3, KPI4;

    @FXML
    private Button KPIBack, SaveButton;

    private final List<String> selectedKPIs = new ArrayList<>();

    @FXML
    public void initialize() {
        // Set up event handlers for each KPI
        KPI1.setOnAction(event -> handleKPISelection(KPI1, "Average FCR"));
        KPI2.setOnAction(event -> handleKPISelection(KPI2, "Average End Weight"));
        KPI3.setOnAction(event -> handleKPISelection(KPI3, "Average Daily Gain"));
        KPI4.setOnAction(event -> handleKPISelection(KPI4, "Feed Consumption"));

        // Load saved KPIs
        displaySavedKPIs();
    }

    private void handleKPISelection(CheckBox checkBox, String kpiText) {
        if (checkBox.isSelected()) {
            addKpiToAnchorPane(kpiText);
            selectedKPIs.add(kpiText);
        } else {
            removeKpiFromAnchorPane(kpiText);
            selectedKPIs.remove(kpiText);
        }
    }

    private void addKpiToAnchorPane(String kpiText) {
        Label kpiLabel = new Label(kpiText);
        kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        kpiLabel.setLayoutX(20); // Adjust X position as needed
        kpiLabel.setLayoutY(50 + (selectedKPIs.size() * 30)); // Adjust Y position dynamically
        kpiLabel.setId("label-" + kpiText); // Set an ID for easy removal
        anchorPane.getChildren().add(kpiLabel);
    }

    private void removeKpiFromAnchorPane(String kpiText) {
        anchorPane.getChildren().removeIf(node -> node instanceof Label && node.getId() != null && node.getId().equals("label-" + kpiText));
    }

    private void displaySavedKPIs() {
        for (String savedKPI : KPIStorage.getSavedKPIs()) {
            addKpiToAnchorPane(savedKPI);
        }
    }

    @FXML
    private void handleKPIBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/agrisys/SMenu.fxml"));
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveButton() {
        KPIStorage.clearKPIs();
        KPIStorage.saveKPIs(selectedKPIs);
        System.out.println("Selected KPIs saved: " + selectedKPIs);
        handleKPIBack();
    }
}