package com.example.agrisys;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                // Ved ikke om det kan ændres til HelperMethods.showAlert / showAlert2
                // Vi må lige se på det på et tidspunkt
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CSV import");
                alert.setHeaderText(null);
                alert.setContentText("CSV filen er importeret.");
                alert.showAndWait();
            } catch (IOException e) {
                HelperMethods.Alert2("Fejl ved import af CSV-fil", e.getMessage());
            }
        } else {
            HelperMethods.Alert2("Ingen fil valgt", "Vælg venligst en CSV-fil.");
        }
    }
    // :)
    @FXML
    public void initialize() {
        BackToMenuButton.setOnAction(e -> HelperMethods.loadScene("SMenu.fxml", BackToMenuButton));

    }
}