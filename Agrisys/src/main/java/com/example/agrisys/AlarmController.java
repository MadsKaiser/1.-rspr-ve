package com.example.agrisys;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
        String query = "SELECT * FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] WHERE FCR IS NOT NULL AND FCR <0;"
              //  + "SELECT r.Responder FROM madserkaiser_dk_db_agrisys.dbo.[Visit Data]
                //  GROUP BY r.Responder HAVING MAX (r.Date) < NOW() - INTERVAL 3 DAY;"
                // Fiks Datasæt ellers virker det ikke :/
                ;
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

        loadDataFromDatabase();

        DeleteAlarmButton.setOnAction(e -> {
            ResponderData selectedItem = ResponderTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                responderData.remove(selectedItem);
               // String deleteQuery = "DELETE FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] WHERE Responder = ? AND FCR = ?";
            } // Tør ikke at køre det overstående kode D:
              // Det sletter fra databasen så pas på hvis i kører det
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
