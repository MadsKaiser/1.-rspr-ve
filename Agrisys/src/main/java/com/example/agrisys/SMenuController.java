package com.example.agrisys;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SMenuController implements Initializable {
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
    private Button KPIButton;
    @FXML
    private CheckBox Widget1;
    @FXML
    private CheckBox Widget2;
    @FXML
    private CheckBox Widget3;
    @FXML
    private VBox InnerAnchor;
    @FXML
    private TextField ResponderIDField;


    private GraphPlaceholder graphPlaceholder;

    @FXML
    private void toggleMenuVisibility() {
        hiddenMenu.setVisible(!hiddenMenu.isVisible());
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        DashboardState instance = DashboardState.getInstance();

        graphPlaceholder = new GraphPlaceholder(InnerAnchor);
        if (instance.isPreset()) {
            Widget1.setSelected(true);
            graphPlaceholder.addLineChart();
            Widget2.setSelected(true);
            graphPlaceholder.addScatterChart();
            Widget3.setSelected(true);
            graphPlaceholder.addPieChart();
        }

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
                InnerAnchor.getChildren().removeIf(node -> node instanceof LineChart);
            }
        });

        Widget2.setOnAction(event -> {
            if (Widget2.isSelected()) {
                graphPlaceholder.addScatterChart();
            } else {
                InnerAnchor.getChildren().removeIf(node -> node instanceof ScatterChart);
            }
        });

        Widget3.setOnAction(event -> {
            if (Widget3.isSelected()) {
                graphPlaceholder.addPieChart();
            } else {
                InnerAnchor.getChildren().removeIf(node -> node instanceof PieChart);
            }
        });
    }

    private void displaySelectedKPIs() {
        for (String kpi : KPIStorage.getSavedKPIs()) {
            try {
                ImageView pigHead = new ImageView(new javafx.scene.image.Image(
                        new java.io.File("C:\\Users\\MadsRinggaardKaiser\\OneDrive - Erhvervsakademi MidtVest\\Skrivebord\\Grisehoved.png").toURI().toString()
                ));
                pigHead.setFitWidth(30.0);
                pigHead.setFitHeight(30.0);

                Label kpiLabel = new Label(kpi);
                kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                VBox kpiContainer = new VBox(5, pigHead, kpiLabel);
                InnerAnchor.getChildren().add(kpiContainer);
            } catch (Exception e) {
                System.err.println("Failed to load pig head image: " + e.getMessage());
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
        try {
            long responderIdLong;
            try {
                responderIdLong = Long.parseLong(responderId);
            } catch (NumberFormatException e) {
                HelperMethods.Alert2("Error", "Responder ID must be a numeric value.");
                return;
            }

            try (Connection connection = DatabaseManager.getConnection()) {
                String query = "SELECT * FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] WHERE Responder = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, responderIdLong);

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

                    InnerAnchor.setSpacing(10.0);
                    InnerAnchor.getChildren().add(responderWidget);
                } else {
                    HelperMethods.Alert2("Info", "No data found for Responder: " + responderId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            HelperMethods.Alert2("Error", "Failed to fetch responder data: " + e.getMessage());
        }
    }
}