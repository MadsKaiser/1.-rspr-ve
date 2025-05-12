package com.example.agrisys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
                addGraphToPane();
            }
        });
        Widget2.setOnAction(event -> Label2.setVisible(Widget2.isSelected()));
        Widget3.setOnAction(event -> Label3.setVisible(Widget3.isSelected()));
        Widget4.setOnAction(event -> Label4.setVisible(Widget4.isSelected()));
        Widget5.setOnAction(event -> Label5.setVisible(Widget5.isSelected()));
        Widget6.setOnAction(event -> Label6.setVisible(Widget6.isSelected()));
    }

    private void addGraphToPane() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Responder");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("FCR");

        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Responder vs FCR");

        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT Responder, FCR FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("PPT data");

            while (resultSet.next()) {
                double responder = resultSet.getDouble("Responder");
                double fcr = resultSet.getDouble("FCR");
                series.getData().add(new XYChart.Data<>(responder, fcr));
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