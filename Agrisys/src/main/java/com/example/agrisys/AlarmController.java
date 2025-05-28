package com.example.agrisys;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
// Benjamin
public class AlarmController {
    @FXML
    private TableView<ResponderData> ResponderTable;

    @FXML
    private TableColumn<ResponderData, String> responderColumn;

    @FXML
    private TableColumn<ResponderData, String> daysSinceLastVisitColumn;

    @FXML
    private Button DeleteAlarmButton;
    @FXML
    private Button BackToMenuButton;

    private final ObservableList<ResponderData> responderData = FXCollections.observableArrayList();
    private static Set<String> deletedIds = new HashSet<>();
    //Den her holder styr på hvilke responder der er slettet
    // Vi bruger Hashset over arraylist da at Hashset er bedre til denne funtion (I følge hvad jeg kunne læse mig til)
    // Hashset sikrer at der ikke er dubletter samt er hurtigere at søge i (Ikke at vi kan mærke det)
    // Den er static, så den kan bruges i hele klassen


    private void loadDataFromDatabase() {
        responderData.clear(); //Rydder listen før man fylder den op igen

        String query = "SELECT \n" +
                "    Responder,\n" +
                "    DATEDIFF(DAY, MAX(Date), '2025-02-05') AS days_since_last_visit\n" +
                "FROM madserkaiser_dk_db_agrisys.dbo.[Visit Data]\n" +
                "WHERE Date <= '2025-02-05'\n" +
                "GROUP BY Responder\n" +
                "HAVING DATEDIFF(DAY, MAX(Date), '2025-02-05') > 3;";

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String responder = resultSet.getString("Responder");

                // Hvis responderen er i deletedIds, så bliver den sprunget over
                if (deletedIds.contains(responder))  {
                    continue;
                }
                int daysSinceLastVisit = resultSet.getInt("days_since_last_visit");
                responderData.add(new ResponderData(responder, daysSinceLastVisit));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    ;

    public void initialize() {
        responderColumn.setCellValueFactory(data -> data.getValue().responderProperty());
        daysSinceLastVisitColumn.setCellValueFactory(data -> data.getValue().daysSinceLastVisitProperty());
        ResponderTable.setItems(responderData);

        loadDataFromDatabase();

        DeleteAlarmButton.setOnAction(e -> {
            ResponderData selectedItem = ResponderTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                deletedIds.add(selectedItem.getResponder());
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