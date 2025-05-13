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
import java.util.HashMap;
import java.util.Map;

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
    private final Map<String, String> roles = new HashMap<>();

    public void initialize() {
        responderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        ResponderTable.setItems(responderData);
        responderData.addAll("Test 1", "Test 2", "Test 3");

        DeleteAlarmButton.setOnAction(e -> {
            String selectedItem = ResponderTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                responderData.remove(selectedItem);
            }
        });

        BackToMenuButton.setOnAction(e -> {
            try {
                // Fetch the current user's role correctly
                String currentUser = UserManager.getInstance().getCurrentUser();


                String role = UserManager.getInstance().getRoles().getOrDefault(currentUser, "USER");

                if ("SUPERUSER".equals(role)) {
                    switchToScene("SMenu.fxml");
                } else {
                    switchToScene("Menu.fxml");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }

    private void switchToScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) BackToMenuButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}

