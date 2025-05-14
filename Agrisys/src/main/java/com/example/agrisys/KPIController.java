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

        KPIBack.setOnAction(event -> goToSmenu());
        SaveButton.setOnAction(event -> {
            saveKPIs(); // Gemmer KPI'erne
            goToSmenu(); // Navigerer tilbage til SMenuController
        });
    }

    private void addKpiToAnchorPane(CheckBox checkBox, String kpiText) {
        // Opret ImageView til grisehovedet
        Image pigImage = new Image(new File("C:\\Users\\MadsRinggaardKaiser\\OneDrive - Erhvervsakademi MidtVest\\Skrivebord\\Grisehoved.png").toURI().toString());
        ImageView pigImageView = new ImageView(pigImage);
        pigImageView.setFitWidth(50); // Sæt bredde
        pigImageView.setFitHeight(50); // Sæt højde
        pigImageView.setLayoutX(10.0); // Fast X-position
        pigImageView.setLayoutY(nextKpiYPosition); // Dynamisk Y-position

        // Opret Label til KPI-teksten
        Label kpiLabel = new Label(kpiText);
        kpiLabel.setLayoutX(70.0); // Placer teksten ved siden af billedet
        kpiLabel.setLayoutY(nextKpiYPosition + 15); // Juster Y-position
        kpiLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Tilføj billede og tekst til AnchorPane
        anchorPane.getChildren().addAll(pigImageView, kpiLabel);

        // Gem label i kpiLabels og opdater Y-position
        kpiLabels.put(checkBox, kpiLabel);
        nextKpiYPosition += 60.0; // Flyt næste element længere ned
    }

    private void removeKpiFromAnchorPane(CheckBox checkBox) {
        Label kpiLabel = kpiLabels.remove(checkBox);
        if (kpiLabel != null) {
            // Find og fjern det tilhørende ImageView
            anchorPane.getChildren().removeIf(node ->
                    node instanceof ImageView &&
                            node.getLayoutY() == kpiLabel.getLayoutY() - 15
            );

            // Fjern label fra AnchorPane
            anchorPane.getChildren().remove(kpiLabel);
            nextKpiYPosition -= 60.0; // Juster positionen, hvis nødvendigt
        }
    }

    private void saveKPIs() {
        KPIStorage.clearKPIs(); // Ryd tidligere gemte KPI'er
        kpiLabels.values().forEach(label -> KPIStorage.saveKPI(label.getText()));
    }

    private void goToSmenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/agrisys/Smenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) KPIBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}