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
    private Button BackButton;

    // Fixet
    @FXML
    private void initialize() {
        BackButton.setOnAction(event -> HelperMethods.loadScene("SMenu.fxml", BackButton));

    }
    @FXML
    void onExportButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.append("Column1,Column2,Column3\n");
                writer.append("Data1,Data2,Data3\n");
                writer.append("Data4,Data5,Data6\n");

                // Ændret til at bruge methods fra HelperMethods klassen = Mindre redundans
                HelperMethods.showAlert(Alert.AlertType.INFORMATION, "Export Successful", "The file has been exported successfully.");
            } catch (IOException e) {
                HelperMethods.showAlert(Alert.AlertType.ERROR, "Export Failed", "An error occurred while exporting the file.");
            }
        }
    }
}