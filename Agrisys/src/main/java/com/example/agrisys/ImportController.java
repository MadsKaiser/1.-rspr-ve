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

public class ImportController {

    // Variabel til at gemme den valgte fil
    private File selectedFile;

    // FXML-komponenter, der er bundet til elementer i FXML-filen
    @FXML
    private Button BrowseButton; // Knap til at åbne filvælgeren

    @FXML
    private Label DirectoryLabel; // Label til at vise filens sti

    @FXML
    private Button ImportCSVButton; // Knap til at importere CSV-filen

    @FXML
    private TextField TextFieldID; // Tekstfelt til at vise den valgte filsti

    // Metode, der håndterer klik på BrowseButton
    @FXML
    void OnBrowseButton(ActionEvent event) {
        // Opretter en filvælger
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vælg CSV-fil"); // Titel på dialogboksen

        // Sætter startmappen til C-drevet
        fileChooser.setInitialDirectory(new File("C:\\"));

        // Tilføjer et filter, så kun CSV-filer vises
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV filer (*.csv)", "*.csv"));

        // Henter det aktive vindue (scene) fra eventet
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Åbner filvælgeren og gemmer den valgte fil
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) { // Hvis en fil blev valgt
            selectedFile = file; // Gemmer den valgte fil
            TextFieldID.setText(file.getAbsolutePath()); // Viser filens sti i tekstfeltet
        }
    }

    // Metode, der håndterer klik på ImportCSVButton
    @FXML
    void OnImportCSVButton(ActionEvent event) {
        if (selectedFile != null) { // Tjekker, om en fil er valgt
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                // Læser filen linje for linje
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // Udskriver hver linje i konsollen
                }
                // Viser en informationsdialog, når importen er færdig
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CSV import");
                alert.setHeaderText(null);
                alert.setContentText("CSV filen er importeret.");
                alert.showAndWait();

            } catch (IOException e) { // Håndterer fejl ved læsning af filen
                showError("Fejl ved import af CSV-fil", e.getMessage());
            }
        } else { // Hvis ingen fil er valgt
            showError("Ingen fil valgt", "Vælg venligst en CSV-fil.");
        }
    }

    // Hjælpefunktion til at vise fejlmeddelelser
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Opretter en fejlmeddelelse
        alert.setTitle("Fejl"); // Titel på dialogboksen
        alert.setHeaderText(null); // Ingen overskrift
        alert.setContentText(message); // Fejlbeskedens indhold
        alert.showAndWait(); // Viser dialogboksen
    }
}