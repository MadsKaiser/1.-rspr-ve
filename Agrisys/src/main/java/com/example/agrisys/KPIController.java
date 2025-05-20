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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KPIController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private CheckBox KPI1, KPI2, KPI3, KPI4;

    @FXML
    private Button KPIBack, SaveButton;

    private final List<String> selectedKPIs = new ArrayList<>();
    private final Map<String, Label> kpiLabels = new HashMap<>();
    private final KPIHelper kpiHelper = new KPIHelper();

    @FXML
    public void initialize() {
        // Reload saved KPIs
        for (String savedKPI : KPIStorage.getSavedKPIs()) {
            switch (savedKPI) {
                case "Average FCR":
                    KPI1.setSelected(true);
                    addKpiToAnchorPane(savedKPI);
                    break;
                case "Average End Weight":
                    KPI2.setSelected(true);
                    addKpiToAnchorPane(savedKPI);
                    break;
                case "Average Daily Gain":
                    KPI3.setSelected(true);
                    addKpiToAnchorPane(savedKPI);
                    break;
                case "Feed Consumption":
                    KPI4.setSelected(true);
                    addKpiToAnchorPane(savedKPI);
                    break;
            }
        }

        // Add event handlers
        KPI1.setOnAction(event -> toggleKPI(KPI1, "Average FCR"));
        KPI2.setOnAction(event -> toggleKPI(KPI2, "Average End Weight"));
        KPI3.setOnAction(event -> toggleKPI(KPI3, "Average Daily Gain"));
        KPI4.setOnAction(event -> toggleKPI(KPI4, "Feed Consumption"));

        KPIBack.setOnAction(event -> handleKPIBack());
        SaveButton.setOnAction(event -> handleSaveButton());
    }

    private void toggleKPI(CheckBox checkBox, String kpiName) {
        if (checkBox.isSelected()) {
            selectedKPIs.add(kpiName);
            addKpiToAnchorPane(kpiName);
        } else {
            selectedKPIs.remove(kpiName);
            removeKpiFromAnchorPane(kpiName);
        }
    }

    private void addKpiToAnchorPane(String kpiText) {
        if (kpiLabels.containsKey(kpiText)) return;

        String calculatedValue = calculateKPI(kpiText);

        Label kpiLabel = new Label(kpiText + ": " + calculatedValue);
        kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        kpiLabel.setLayoutX(20);
        kpiLabel.setLayoutY(findNextFreeYPosition());
        kpiLabel.setId("label-" + kpiText);
        kpiLabels.put(kpiText, kpiLabel);
        anchorPane.getChildren().add(kpiLabel);
    }

    private void removeKpiFromAnchorPane(String kpiName) {
        Label label = kpiLabels.remove(kpiName);
        if (label != null) {
            anchorPane.getChildren().remove(label);
        }
    }

    private double findNextFreeYPosition() {
        double baseY = 50;
        double spacing = 30;
        return baseY + (kpiLabels.size() * spacing);
    }

    private String calculateKPI(String kpiName) {
        try {
            switch (kpiName) {
                case "Average FCR":
                    return String.format("%.2f", kpiHelper.fetchKPI(kpiHelper.getAverageFCRQuery()));
                case "Average End Weight":
                    return String.format("%.2f", kpiHelper.fetchKPI(kpiHelper.getAverageEndWeightQuery()));
                case "Average Daily Gain":
                    return String.format("%.2f", kpiHelper.fetchKPI(kpiHelper.getAverageDailyWeightGainQuery()));
                case "Feed Consumption":
                    return String.format("%.2f", kpiHelper.fetchKPI(kpiHelper.getFeedConsumptionPerPigPerDayQuery()));
                default:
                    return "N/A";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @FXML
    private void handleKPIBack() {
        navigateToSMenu();
    }

    @FXML
    private void handleSaveButton() {
        KPIStorage.clearKPIs();
        for (String kpi : selectedKPIs) {
            String calculatedValue = calculateKPI(kpi);
            KPIStorage.saveKPIWithValue(kpi, calculatedValue);
        }
        navigateToSMenu();
    }

    private void navigateToSMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/agrisys/SMenu.fxml"));
            Parent root = loader.load();

            SMenuController controller = loader.getController();
            controller.loadKPIs(KPIStorage.getSavedKPIsWithValues());

            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}