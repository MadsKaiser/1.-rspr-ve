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
    // Ændret til at bruge methods fra HelperMethods klassen = Mindre redundans
    @FXML
    private void handleFetchResponderData() {
        String responderId = responderIdField.getText();

        if (responderId == null || responderId.isEmpty()) {
            HelperMethods.Alert2("Error", "Please enter a responder ID.");
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
                String responder = resultSet.getString("Responder");
                double startWeight = resultSet.getDouble("Start_weight_kg");
                double endWeight = resultSet.getDouble("End_weight_kg");

                System.out.println("Responder: " + responder + ", Start Weight: " + startWeight + ", End Weight: " + endWeight);

                widgetContainer.getChildren().add(new Text("Responder ID: " + responder));
                widgetContainer.getChildren().add(new Text("Start Weight: " + startWeight));
                widgetContainer.getChildren().add(new Text("End Weight: " + endWeight));
            } else {
                System.out.println("No data found for Responder: " + responderId);
                widgetContainer.getChildren().add(new Text("No data found for Responder: " + responderId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            HelperMethods.Alert2("Error", "Failed to fetch responder data: " + e.getMessage());
        }
    }

}