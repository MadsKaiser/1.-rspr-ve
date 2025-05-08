package com.example.agrisys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportController {

    @FXML
    private Button ExportButton;

    @FXML
    void onExportButton(ActionEvent event) {
        // Create a FileChooser to select the export location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

        // Get the current stage from the event
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Show the save dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Example data to export (replace with your actual data)
                writer.append("Column1,Column2,Column3\n");
                writer.append("Data1,Data2,Data3\n");
                writer.append("Data4,Data5,Data6\n");

                // Show success alert
                showAlert(Alert.AlertType.INFORMATION, "Export Successful", "The file has been exported successfully.");
            } catch (IOException e) {
                // Show error alert
                showAlert(Alert.AlertType.ERROR, "Export Failed", "An error occurred while exporting the file.");
            }
        }
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}