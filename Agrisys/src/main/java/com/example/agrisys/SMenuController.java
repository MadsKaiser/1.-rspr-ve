package com.example.agrisys;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

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
    @FXML
    private TextField ResponderIDField;

    private GraphPlaceholder graphPlaceholder;

    @FXML
    private void toggleMenuVisibility() {
        hiddenMenu.setVisible(!hiddenMenu.isVisible());
    }

    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resources) {
        graphPlaceholder = new GraphPlaceholder(Anchor);

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
                Anchor.getChildren().removeIf(node -> node instanceof LineChart);
            }
        });

        Widget2.setOnAction(event -> {
            if (Widget2.isSelected()) {
                graphPlaceholder.addScatterChart();
            } else {
                Anchor.getChildren().removeIf(node -> node instanceof ScatterChart);
            }
        });
    }

    private void displaySelectedKPIs() {
        double yPosition = 10.0; // Initial Y position for displaying KPIs
        for (String kpi : KPIStorage.getSavedKPIs()) {
            try {
                Label kpiLabel = new Label(kpi);
                kpiLabel.setLayoutX(50.0);
                kpiLabel.setLayoutY(yPosition);
                kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                Anchor.getChildren().addAll(kpiLabel);
                yPosition += 40.0;
            } catch (Exception e) {
                System.err.println("Failed to load KPI: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleFetchResponderData() {
        String responderId = ResponderIDField.getText();

        if (responderId == null || responderId.isEmpty()) {
            HelperMethods.Alert2("Error", "Please enter a responder ID.");
            return;
        }

        fetchResponderData(responderId);
    }

    private void fetchResponderData(String responderId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] WHERE Responder = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, responderId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                VBox responderWidget = new VBox();
                responderWidget.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 1; -fx-spacing: 5;");
                responderWidget.getChildren().add(new Label("Responder Data:"));

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = resultSet.getString(i);
                    responderWidget.getChildren().add(new Label(columnName + ": " + columnValue));
                }

                widgetContainer.setSpacing(10.0);
                widgetContainer.getChildren().add(responderWidget);
            } else {
                HelperMethods.Alert2("Info", "No data found for Responder: " + responderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            HelperMethods.Alert2("Error", "Failed to fetch responder data: " + e.getMessage());
        }
    }
}