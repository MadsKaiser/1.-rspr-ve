package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PigDataController {

    @FXML
    private TextField pigNumberField;

    @FXML
    private TextArea pigDataArea;

    // Method to handle the button click
    @FXML
    private void handleFetchPigData() {
        String pigNumber = pigNumberField.getText();

        if (pigNumber == null || pigNumber.isEmpty()) {
            showAlert("Error", "Please enter a pig number.");
            return;
        }

        // Fetch pig data from the database
        String pigData = getPigData(pigNumber);

        // Display the data in the TextArea
        pigDataArea.setText(pigData);
    }

    // Method to fetch pig data from the database
    private String getPigData(String pigNumber) {
        StringBuilder pigData = new StringBuilder();

        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM PPT WHERE pig_number = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pigNumber);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                pigData.append("Pig Number: ").append(resultSet.getString("pig_number")).append("\n");
                pigData.append("Weight: ").append(resultSet.getString("weight")).append("\n");
                pigData.append("Age: ").append(resultSet.getString("age")).append("\n");
                // Add more fields as needed
            } else {
                pigData.append("No data found for Pig Number: ").append(pigNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch pig data: " + e.getMessage());
        }

        return pigData.toString();
    }

    // Method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}