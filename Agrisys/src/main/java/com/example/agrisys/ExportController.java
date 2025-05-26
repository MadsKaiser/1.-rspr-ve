package com.example.agrisys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExportController {

    @FXML
    private Button ExportButton;

    @FXML
    private Button BackButton;

    @FXML
    private void initialize() {
        BackButton.setOnAction(event -> HelperMethods.loadScene("SMenu.fxml", BackButton));
    }

    @FXML
    void onExportButton(ActionEvent event) { //Åbner en filvælger så man kan gemme CSV-filen
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file);
                 Connection connection = DatabaseManager.getConnection()) {

                //Henter alle tabeller i databasen
                ResultSet tables = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});

                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    writer.append("Table: ").append(tableName).append("\n");

                    //Henter dataen der er i tabbelerne
                    String query = "SELECT * FROM \"" + tableName + "\"";
                    try (PreparedStatement statement = connection.prepareStatement(query);
                         ResultSet resultSet = statement.executeQuery()) {

                        //Skriver kolonnenavne
                        int columnCount = resultSet.getMetaData().getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            writer.append(resultSet.getMetaData().getColumnName(i));
                            if (i < columnCount) writer.append(";"); // <- semikolon separator
                        }
                        writer.append("\n");

                        //Skriver rækkerne
                        while (resultSet.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                String value = resultSet.getString(i);
                                writer.append(value != null ? value.replace("\"", "\"\"") : "");
                                if (i < columnCount) writer.append(";"); // <- semikolon separator
                            }
                            writer.append("\n");
                        }
                    } catch (Exception ex) {
                        HelperMethods.Alert2("Error", "Error querying table " + tableName + ": " + ex.getMessage());
                    }

                    writer.append("\n");
                }
                //Bruger metode fra HelperMethods klassen til at vise en besked om at eksporten er fuldført eller fejlet
                HelperMethods.Alert2("Export Successful", "All tables have been exported successfully.");
            } catch (Exception e) {
                HelperMethods.Alert2("Export Failed", "An error occurred while exporting the tables: " + e.getMessage());
            }
        }
    }
}