package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

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
            String query = "SELECT * FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] WHERE Responder = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, responderId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Create a VBox to represent the widget
                VBox responderWidget = new VBox();
                responderWidget.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 1; -fx-spacing: 5;");
                responderWidget.getChildren().add(new Label("Responder Data:"));

                // Populate the widget with data
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = resultSet.getString(i);
                    responderWidget.getChildren().add(new Label(columnName + ": " + columnValue));
                }

                // Add the widget to the widgetContainer
                widgetContainer.setSpacing(10.0); // Ensure spacing between widgets
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