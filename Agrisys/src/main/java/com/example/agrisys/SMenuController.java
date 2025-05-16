package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.Map;

public class SMenuController implements javafx.fxml.Initializable {
    @FXML
    private Button AlarmButton;
    @FXML
    private Button ImportCSVButton;
    @FXML
    private Button ExportCSVButton;
    @FXML
    private Button LogoutButton;
    @FXML
    private Button WidgetsButton;
    @FXML
    private Button DashboardsButton;
    @FXML
    private VBox hiddenMenu;
    @FXML
    private VBox widgetContainer;
    @FXML
    private Button KPIButton;
    @FXML
    private CheckBox Widget1;
    @FXML
    private CheckBox Widget2;
    @FXML
    private CheckBox Widget3;
    @FXML
    private CheckBox Widget4;
    @FXML
    private CheckBox Widget5;
    @FXML
    private CheckBox Widget6;
    @FXML
    private AnchorPane Anchor;

    private GraphPlaceholder graphPlaceholder;

    @FXML
    private void toggleMenuVisibility() {
        hiddenMenu.setVisible(!hiddenMenu.isVisible());
    }

    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resources) {
        graphPlaceholder = new GraphPlaceholder(widgetContainer);

        // Load selected KPIs from KPIStorage
        displaySelectedKPIs();

        AlarmButton.setOnAction(e -> HelperMethods.loadScene("Alarm.fxml", AlarmButton));
        WidgetsButton.setOnAction(e -> toggleMenuVisibility());
        LogoutButton.setOnAction(e -> HelperMethods.loadScene("Login.fxml", LogoutButton));
        ExportCSVButton.setOnAction(e -> HelperMethods.loadScene("Export.fxml", ExportCSVButton));
        ImportCSVButton.setOnAction(e -> HelperMethods.loadScene("ImportCSV.fxml", ImportCSVButton));
        DashboardsButton.setOnAction(e -> HelperMethods.loadScene("Dashboard.fxml", DashboardsButton));
        KPIButton.setOnAction(e -> HelperMethods.loadScene("KPI.fxml", KPIButton));

        Widget1.setOnAction(event -> {
            if (Widget1.isSelected()) {
                graphPlaceholder.addLineChart();
            } else {
                widgetContainer.getChildren().removeIf(node -> node instanceof javafx.scene.chart.LineChart);
            }
        });

        Widget2.setOnAction(event -> {
            if (Widget2.isSelected()) {
                graphPlaceholder.addScatterChart();
            } else {
                widgetContainer.getChildren().removeIf(node -> node instanceof javafx.scene.chart.ScatterChart);
            }
        });

        Widget3.setOnAction(event -> {
            if (Widget3.isSelected()) {
                graphPlaceholder.addPieChart();
            } else {
                widgetContainer.getChildren().removeIf(node -> node instanceof javafx.scene.chart.PieChart);
            }
        });
    }

    private void displaySelectedKPIs() {
        widgetContainer.getChildren().clear(); // Clear existing content
        for (Map.Entry<String, String> entry : KPIStorage.getSavedKPIsWithValues().entrySet()) {
            Label kpiLabel = new Label(entry.getKey() + ": " + entry.getValue());
            kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            widgetContainer.getChildren().add(kpiLabel);
        }
    }

    public void loadKPIs(Map<String, String> savedKPIsWithValues) {
        widgetContainer.getChildren().clear(); // Clear existing content
        for (Map.Entry<String, String> entry : savedKPIsWithValues.entrySet()) {
            Label kpiLabel = new Label(entry.getKey() + ": " + entry.getValue());
            kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            widgetContainer.getChildren().add(kpiLabel);
        }
    }

    @FXML
    private void handleFetchResponderData() {
        // Implement the logic for fetching responder data here
        System.out.println("Fetching responder data...");
        // Example: Add your data-fetching logic or API call here
    }
}