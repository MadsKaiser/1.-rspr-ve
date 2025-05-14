package com.example.agrisys;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
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
    private TextField ResponderIDField;

    private GraphPlaceholder graphPlaceholder;

    @FXML
    private void toggleMenuVisibility() {
        hiddenMenu.setVisible(!hiddenMenu.isVisible());
    }

    public void initialize() {
        graphPlaceholder = new GraphPlaceholder(Anchor);

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

        Widget3.setOnAction(event -> Label3.setVisible(Widget3.isSelected()));
        Widget4.setOnAction(event -> Label4.setVisible(Widget4.isSelected()));
        Widget5.setOnAction(event -> Label5.setVisible(Widget5.isSelected()));
        Widget6.setOnAction(event -> Label6.setVisible(Widget6.isSelected()));
    }

    @FXML
    private void handleFetchResponderData() {
        String responderId = ResponderIDField.getText();

        if (responderId == null || responderId.isEmpty()) {
            showAlert("Error", "Please enter a responder ID.");
            return;
        }

        fetchResponderData(responderId);
    }

    private void fetchResponderData(String responderId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT Responder, [Start_weight_kg], [End_weight_kg] FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] WHERE Responder = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, responderId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String responder = resultSet.getString("Responder");
                double startWeight = resultSet.getDouble("Start_weight_kg");
                double endWeight = resultSet.getDouble("End_weight_kg");

                // Example: Display data in the console or update UI elements
                System.out.println("Responder: " + responder);
                System.out.println("Start Weight: " + startWeight);
                System.out.println("End Weight: " + endWeight);
            } else {
                showAlert("Info", "No data found for Responder: " + responderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch responder data: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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