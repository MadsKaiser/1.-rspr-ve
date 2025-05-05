package com.example.agrisys;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class AlarmController {
    @FXML
    private TableView<String> ResponderTable;

    @FXML
    private TableColumn<String, String> responderColumn;

    @FXML
    private Button DeleteAlarmButton;
    @FXML
    private Button BackToMenuButton;

    private final ObservableList<String> responderData = FXCollections.observableArrayList();

    public void initialize() {
        // Brug den eksisterende kolonne
        responderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        // Sæt data i TableView
        ResponderTable.setItems(responderData);

        // Dummy data
        responderData.addAll("Test 1", "Test 2", "Test 3");

        // Slet valgte række
        DeleteAlarmButton.setOnAction(e -> {
            String selectedItem = ResponderTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                responderData.remove(selectedItem);
            }
        });

        // Tilbage til menu
        BackToMenuButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) BackToMenuButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
