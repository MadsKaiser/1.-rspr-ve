package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KPIController {

    @FXML
    private CheckBox KPI1;

    @FXML
    private CheckBox KPI2;

    @FXML
    private CheckBox KPI3;

    @FXML
    private CheckBox KPI4;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button KPIBack;

    @FXML
    private Button SaveButton;

    private Map<Double, Boolean> occupiedPositions = new HashMap<>();
    private double nextKpiYPosition = 10.0; // Startposition for første KPI
    private Map<CheckBox, Label> kpiLabels = new HashMap<>(); // Holder styr på labels
    private KPIHelper kpiHelper = new KPIHelper(); // Instans af KPIHelper

    @FXML
    public void initialize() {
        // Load saved states for each KPI
        KPI1.setSelected(KPIStorage.getKPIState("KPI1"));
        KPI2.setSelected(KPIStorage.getKPIState("KPI2"));
        KPI3.setSelected(KPIStorage.getKPIState("KPI3"));
        KPI4.setSelected(KPIStorage.getKPIState("KPI4"));

        // Genskab gemte KPI'er med grisehovedet
        for (String savedKPI : KPIStorage.getSavedKPIs()) {
            if (savedKPI.startsWith("Average FCR")) {
                addKpiToAnchorPane(KPI1, savedKPI);
            } else if (savedKPI.startsWith("Average End Weight")) {
                addKpiToAnchorPane(KPI2, savedKPI);
            } else if (savedKPI.startsWith("Average Daily Gain")) {
                addKpiToAnchorPane(KPI3, savedKPI);
            } else if (savedKPI.startsWith("Feed Consumption")) {
                addKpiToAnchorPane(KPI4, savedKPI);
            }
        }

        // Set up event handlers for each KPI
        KPI1.setOnAction(event -> {
            boolean isSelected = KPI1.isSelected();
            KPIStorage.setKPIState("KPI1", isSelected);
            if (isSelected) {
                double averageFCR = kpiHelper.fetchKPI(kpiHelper.getAverageFCRQuery());
                addKpiToAnchorPane(KPI1, "Average FCR: " + averageFCR);
            } else {
                removeKpiFromAnchorPane(KPI1);
            }
        });

        KPI2.setOnAction(event -> {
            boolean isSelected = KPI2.isSelected();
            KPIStorage.setKPIState("KPI2", isSelected);
            if (isSelected) {
                double averageEndWeight = kpiHelper.fetchKPI(kpiHelper.getAverageEndWeightQuery());
                addKpiToAnchorPane(KPI2, "Average End Weight: " + averageEndWeight);
            } else {
                removeKpiFromAnchorPane(KPI2);
            }
        });

        KPI3.setOnAction(event -> {
            boolean isSelected = KPI3.isSelected();
            KPIStorage.setKPIState("KPI3", isSelected);
            if (isSelected) {
                double averageDailyGain = kpiHelper.fetchKPI(kpiHelper.getAverageDailyWeightGainQuery());
                addKpiToAnchorPane(KPI3, "Average Daily Gain: " + averageDailyGain + " kg/day");
            } else {
                removeKpiFromAnchorPane(KPI3);
            }
        });

        KPI4.setOnAction(event -> {
            boolean isSelected = KPI4.isSelected();
            KPIStorage.setKPIState("KPI4", isSelected);
            if (isSelected) {
                double feedConsumption = kpiHelper.fetchKPI(kpiHelper.getFeedConsumptionPerPigPerDayQuery());
                addKpiToAnchorPane(KPI4, "Feed Consumption: " + feedConsumption + " kg/day");
            } else {
                removeKpiFromAnchorPane(KPI4);
            }
        });
        // Skal måske ses på og testet
        KPIBack.setOnAction(event -> HelperMethods.loadScene("SMenu.fxml", KPIBack)); // Mangler test
        SaveButton.setOnAction(event -> {
            saveKPIs(); // Gemmer KPI'erne
            HelperMethods.loadScene("SMenu.fxml", SaveButton); // Virker måske ikke :)
        });
    }

    private void addKpiToAnchorPane(CheckBox checkBox, String kpiText) {
        // Find the first available position
        double yPosition = 10.0;
        while (isPositionOccupied(yPosition)) {
            yPosition += 60.0;
        }

        // Load the pig head image from resources
        Image pigImage = new Image(getClass().getResource("/com/example/agrisys/Grisehoved.png").toExternalForm());
        ImageView pigImageView = new ImageView(pigImage);
        pigImageView.setFitWidth(50);
        pigImageView.setFitHeight(50);
        pigImageView.setLayoutX(10.0);
        pigImageView.setLayoutY(yPosition);

        // Create a label for the KPI text
        Label kpiLabel = new Label(kpiText);
        kpiLabel.setLayoutX(70.0);
        kpiLabel.setLayoutY(yPosition + 15);
        kpiLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Add the image and text to the AnchorPane
        anchorPane.getChildren().addAll(pigImageView, kpiLabel);

        // Save label and position
        kpiLabels.put(checkBox, kpiLabel);
        occupiedPositions.put(yPosition, true);
    }

    private void removeKpiFromAnchorPane(CheckBox checkBox) {
        Label kpiLabel = kpiLabels.remove(checkBox);
        if (kpiLabel != null) {
            double yPosition = kpiLabel.getLayoutY() - 15;

            // Fjern det tilhørende ImageView
            anchorPane.getChildren().removeIf(node ->
                    node instanceof ImageView && node.getLayoutY() == yPosition
            );

            // Fjern label fra AnchorPane
            anchorPane.getChildren().remove(kpiLabel);

            // Marker position som ledig
            occupiedPositions.remove(yPosition);
        }
    }

    private boolean isPositionOccupied(double yPosition) {
        return occupiedPositions.getOrDefault(yPosition, false);
    }

    private void saveKPIs() {
        KPIStorage.clearKPIs(); // Ryd tidligere gemte KPI'er
        kpiLabels.values().forEach(label -> KPIStorage.saveKPI(label.getText()));
    }

}