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

        AlarmButton.setOnAction(e -> loadScene("Alarm.fxml", AlarmButton));
        WidgetsButton.setOnAction(e -> toggleMenuVisibility());
        LogoutButton.setOnAction(e -> loadScene("Login.fxml", LogoutButton));
        ExportCSVButton.setOnAction(e -> loadScene("Export.fxml", ExportCSVButton));
        ImportCSVButton.setOnAction(e -> loadScene("ImportCSV.fxml", ImportCSVButton));
        DashboardsButton.setOnAction(e -> loadScene("Dashboard.fxml", DashboardsButton));
        KPIButton.setOnAction(e -> loadScene("KPI.fxml", KPIButton));

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
            // Create and configure the pig head image
            try {
                ImageView pigHead = new ImageView(new javafx.scene.image.Image(
                        new java.io.File("C:\\Users\\MadsRinggaardKaiser\\OneDrive - Erhvervsakademi MidtVest\\Skrivebord\\Grisehoved.png").toURI().toString()
                ));
                pigHead.setFitWidth(30.0); // Set image width
                pigHead.setFitHeight(30.0); // Set image height
                pigHead.setLayoutX(10.0); // X position for the image
                pigHead.setLayoutY(yPosition - 5.0); // Align with the label

                // Create and configure the KPI label
                Label kpiLabel = new Label(kpi);
                kpiLabel.setLayoutX(50.0); // X position after the image
                kpiLabel.setLayoutY(yPosition); // Y position
                kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                // Add the image and label to the AnchorPane
                Anchor.getChildren().addAll(pigHead, kpiLabel);
            } catch (Exception e) {
                System.err.println("Failed to load pig head image: " + e.getMessage());
            }

            yPosition += 40.0; // Increment Y position for the next KPI
        }
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