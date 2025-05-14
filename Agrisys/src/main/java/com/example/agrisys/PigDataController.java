package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PigDataController {

    @FXML
    private TextField responderIdField;

    @FXML
    private VBox widgetContainer;

    // Method to handle the button click
    @FXML
    private void handleFetchResponderData() {
        String responderId = responderIdField.getText();

        if (responderId == null || responderId.isEmpty()) {
            showAlert("Error", "Please enter a responder ID.");
            return;
        }

        // Fetch responder data from the database
        fetchResponderData(responderId);
    }

    // Method to fetch responder data and create widgets
    private void fetchResponderData(String responderId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT Responder, [Start_weight_kg], [End_weight_kg] FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] WHERE Responder = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, responderId);

            ResultSet resultSet = statement.executeQuery();
            widgetContainer.getChildren().clear(); // Clear previous widgets

            if (resultSet.next()) {
                // Create widgets dynamically based on the data
                String responder = resultSet.getString("Responder");
                double startWeight = resultSet.getDouble("Start_weight_kg");
                double endWeight = resultSet.getDouble("End_weight_kg");

                widgetContainer.getChildren().add(new Text("Responder ID: " + responder));
                widgetContainer.getChildren().add(new Text("Start Weight: " + startWeight));
                widgetContainer.getChildren().add(new Text("End Weight: " + endWeight));
            } else {
                widgetContainer.getChildren().add(new Text("No data found for Responder: " + responderId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch responder data: " + e.getMessage());
        }
    }

    // Method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}