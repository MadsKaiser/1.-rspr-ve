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
import java.sql.*;

public class AlarmController {
    @FXML
    private TableView<ResponderData> ResponderTable;

    @FXML
    private TableColumn<ResponderData, String> responderColumn;

    @FXML
    private TableColumn<ResponderData, String> fcrColumn;

    @FXML
    private Button DeleteAlarmButton;
    @FXML
    private Button BackToMenuButton;

    private final ObservableList<ResponderData> responderData = FXCollections.observableArrayList();

    private void loadDataFromDatabase() {
        String query = "SELECT * FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] WHERE FCR IS NOT NULL AND FCR <0";
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String responder = resultSet.getString("responder");
                Double fcr = resultSet.getDouble("FCR");
                responderData.add(new ResponderData(responder, fcr));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        responderColumn.setCellValueFactory(data -> data.getValue().responderProperty());
        fcrColumn.setCellValueFactory(data -> data.getValue().fcrProperty());
        ResponderTable.setItems(responderData);

        // Load data from the database
        loadDataFromDatabase();

        DeleteAlarmButton.setOnAction(e -> {
            ResponderData selectedItem = ResponderTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                responderData.remove(selectedItem);
            }
        });

        BackToMenuButton.setOnAction(e -> {
            try {
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
        stage.show();
    }
}