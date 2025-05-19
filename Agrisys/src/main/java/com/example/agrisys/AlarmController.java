package com.example.agrisys;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AlarmController {
    @FXML
    private TableView<ResponderData> ResponderTable;

    @FXML
    private TableColumn<ResponderData, String> responderColumn;

    @FXML
    private TableColumn<ResponderData, String> fcrColumn;

    @FXML
    private TableColumn<ResponderData, String> lastVisitColumn;

    @FXML
    private Button DeleteAlarmButton;
    @FXML
    private Button BackToMenuButton;

    private final ObservableList<ResponderData> responderData = FXCollections.observableArrayList();

    private void loadDataFromDatabase() {
        String query1 = "SELECT responder, FCR FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] " +
                "WHERE FCR IS NOT NULL AND FCR < 0;";
        String query2 = "SELECT r.Responder, MAX(r.Date) AS LastVisitDate " +
                "FROM madserkaiser_dk_db_agrisys.dbo.[Visit Data] r " +
                "GROUP BY r.Responder " +
                "HAVING MAX(r.Date) < DATEADD(DAY, -3, GETDATE());";

        Map<String, ResponderData> dataMap = new HashMap<>();

        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement1 = connection.createStatement();
                 ResultSet resultSet1 = statement1.executeQuery(query1)) {

                while (resultSet1.next()) {
                    String responder = resultSet1.getString("responder");
                    Double fcr = resultSet1.getDouble("FCR");

                    dataMap.put(responder, new ResponderData(responder, fcr, null));
                }
            }

            try (Statement statement2 = connection.createStatement();
                 ResultSet resultSet2 = statement2.executeQuery(query2)) {

                while (resultSet2.next()) {
                    String responder = resultSet2.getString("Responder");
                    Date lastVisitDate = resultSet2.getDate("LastVisitDate");

                    dataMap.computeIfPresent(responder, (key, value) -> {
                        value.setLastVisitDate(lastVisitDate);
                        return value;
                    });
                }
            }

            responderData.clear();
            responderData.addAll(dataMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        responderColumn.setCellValueFactory(data -> data.getValue().responderProperty());
        fcrColumn.setCellValueFactory(data -> data.getValue().fcrProperty());
        lastVisitColumn.setCellValueFactory(data -> data.getValue().lastVisitDateProperty());
        ResponderTable.setItems(responderData);

        loadDataFromDatabase();

        DeleteAlarmButton.setOnAction(e -> {
            ResponderData selectedItem = ResponderTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                responderData.remove(selectedItem);
            }
        });

        BackToMenuButton.setOnAction(e -> {
            String currentUser = UserManager.getInstance().getCurrentUser();
            String role = UserManager.getInstance().getRoles().getOrDefault(currentUser, "USER");

            if ("SUPERUSER".equals(role)) {
                HelperMethods.loadScene("SMenu.fxml", BackToMenuButton);
            } else {
                HelperMethods.loadScene("Menu.fxml", BackToMenuButton);
            }
        });
    }
}