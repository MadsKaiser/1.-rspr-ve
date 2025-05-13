package com.example.agrisys;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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

public class SMenuController {
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
    private CheckBox Widget4;
    @FXML
    private CheckBox Widget5;
    @FXML
    private CheckBox Widget6;
    @FXML
    private Label Label1;
    @FXML
    private Label Label2;
    @FXML
    private Label Label3;
    @FXML
    private Label Label4;
    @FXML
    private Label Label5;
    @FXML
    private Label Label6;
    @FXML
    private AnchorPane Anchor;

    @FXML
    private void toggleMenuVisibility() {
        hiddenMenu.setVisible(!hiddenMenu.isVisible());
    }

    public void initialize() {
        hiddenMenu.setVisible(false);
        Label1.setVisible(false);
        Label2.setVisible(false);
        Label3.setVisible(false);
        Label4.setVisible(false);
        Label5.setVisible(false);
        Label6.setVisible(false);

        AlarmButton.setOnAction(e -> loadScene("Alarm.fxml", AlarmButton));
        WidgetsButton.setOnAction(e -> toggleMenuVisibility());
        LogoutButton.setOnAction(e -> loadScene("Login.fxml", LogoutButton));
        ExportCSVButton.setOnAction(e -> loadScene("Export.fxml", ExportCSVButton));
        ImportCSVButton.setOnAction(e -> loadScene("ImportCSV.fxml", ImportCSVButton));
        DashboardsButton.setOnAction(e -> loadScene("Dashboard.fxml", DashboardsButton));

        Widget1.setOnAction(event -> {
            Label1.setVisible(Widget1.isSelected());
            if (Widget1.isSelected()) {
                addLineChartToPane();
            }
        });
        Widget2.setOnAction(event -> {
            Label2.setVisible(Widget2.isSelected());
            if (Widget2.isSelected()) {
                addScatterChartToPane();
            }
        });
        Widget3.setOnAction(event -> Label3.setVisible(Widget3.isSelected()));
        Widget4.setOnAction(event -> Label4.setVisible(Widget4.isSelected()));
        Widget5.setOnAction(event -> Label5.setVisible(Widget5.isSelected()));
        Widget6.setOnAction(event -> Label6.setVisible(Widget6.isSelected()));
    }

    private void addLineChartToPane() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Responder Index");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("FCR");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Responder Index vs FCR");

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT Responder, FCR FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]");
             ResultSet resultSet = statement.executeQuery()) {

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Foderudnyttelse");

            int index = 1; // Start index for responders
            while (resultSet.next()) {
                double fcr = resultSet.getDouble("FCR");
                if (fcr < -500 || fcr > 1000) {
                    continue; // Skip invalid FCR values
                }
                series.getData().add(new XYChart.Data<>(index++, fcr));
            }

            lineChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Anchor.getChildren().clear();
        Anchor.getChildren().add(lineChart);
    }

    private void addScatterChartToPane() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Daglig tilvækst");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("FCR");

        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("FCR vs. tilvækst");

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT [Weight gain kg], FCR FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]");
             ResultSet resultSet = statement.executeQuery()) {

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Foderudnyttelse vs Tilvækst");

            while (resultSet.next()) {
                double weightGain = resultSet.getDouble("Weight gain kg");
                double fcr = resultSet.getDouble("FCR");
                if (fcr < -500 || fcr > 1000 || weightGain < 0) {
                    continue; // Skip invalid values
                }
                series.getData().add(new XYChart.Data<>(weightGain, fcr));
            }

            scatterChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Anchor.getChildren().clear();
        Anchor.getChildren().add(scatterChart);
    }

    private void loadScene(String fxmlFile, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            System.err.println("Failed to load the scene: " + fxmlFile);
            ex.printStackTrace();
        }
    }
}