package com.example.agrisys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImportController {

    private File selectedFile;

    @FXML
    private Button BrowseButton;

    @FXML
    private Label DirectoryLabel;

    @FXML
    private Button ImportCSVButton;

    @FXML
    private TextField TextFieldID;

    @FXML
    private Button BackToMenuButton;

    @FXML
    void OnBrowseButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vælg CSV-fil");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV filer (*.csv)", "*.csv"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedFile = file;
            TextFieldID.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void OnImportCSVButton(ActionEvent event) {
        if (selectedFile != null) {
            String tableName = "[" + selectedFile.getName().replace(".csv", "") + "]"; // Ensure valid table name
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                 Connection connection = DatabaseManager.getConnection()) {

                // Read the first line to get column names
                String headerLine = reader.readLine();
                if (headerLine == null) {
                    HelperMethods.Alert2("Fejl", "CSV-filen er tom.");
                    return;
                }

                // Detect delimiter (default to comma)
                String delimiter = headerLine.contains(";") ? ";" : ",";

                String[] columns = headerLine.split(delimiter);

                // Create table with dynamic column names
                StringBuilder createTableQuery = new StringBuilder("CREATE TABLE " + tableName + " (");
                for (String column : columns) {
                    createTableQuery.append("[").append(column.trim()).append("] NVARCHAR(MAX), ");
                }
                createTableQuery.setLength(createTableQuery.length() - 2); // Remove last comma
                createTableQuery.append(");");
                try (PreparedStatement createTableStmt = connection.prepareStatement(createTableQuery.toString())) {
                    createTableStmt.execute();
                }

                // Prepare insert query
                StringBuilder insertQuery = new StringBuilder("INSERT INTO " + tableName + " (");
                for (String column : columns) {
                    insertQuery.append("[").append(column.trim()).append("], ");
                }
                insertQuery.setLength(insertQuery.length() - 2); // Remove last comma
                insertQuery.append(") VALUES (");
                insertQuery.append("?,".repeat(columns.length));
                insertQuery.setLength(insertQuery.length() - 1); // Remove last comma
                insertQuery.append(");");

                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery.toString());

                // Read and insert data
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(delimiter);
                    for (int i = 0; i < values.length; i++) {
                        preparedStatement.setString(i + 1, values[i].trim());
                    }
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CSV import");
                alert.setHeaderText(null);
                alert.setContentText("CSV-filen er importeret og gemt i tabellen: " + tableName);
                alert.showAndWait();

            } catch (IOException e) {
                HelperMethods.Alert2("Fejl ved import af CSV-fil", e.getMessage());
            } catch (SQLException e) {
                HelperMethods.Alert2("Fejl ved databaseoperation", e.getMessage());
            }
        } else {
            HelperMethods.Alert2("Ingen fil valgt", "Vælg venligst en CSV-fil.");
        }
    }

    @FXML
    public void initialize() {
        BackToMenuButton.setOnAction(e -> HelperMethods.loadScene("SMenu.fxml", BackToMenuButton));
    }
}