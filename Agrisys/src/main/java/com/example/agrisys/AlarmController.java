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
                 private TableColumn<ResponderData, String> daysSinceLastVisitColumn;

                 @FXML
                 private Button DeleteAlarmButton;
                 @FXML
                 private Button BackToMenuButton;
                 //En liste til at holde på ResponderData
                 private final ObservableList<ResponderData> responderData = FXCollections.observableArrayList();
                 //En metode til at hente data fra databasen og laver flere SQL forespørgelser/Quaries for at finde de grise der ikke har spsist i 3 dage i den angivne periode
                 private void loadDataFromDatabase() {
                     String query = "SELECT \n" +
                             "    Responder,\n" +
                             "    DATEDIFF(DAY, MAX(Date), '2025-02-05') AS days_since_last_visit\n" +
                             "FROM madserkaiser_dk_db_agrisys.dbo.[Visit Data]\n" +
                             "WHERE Date <= '2025-02-05'\n" +
                             "GROUP BY Responder\n" +
                             "HAVING DATEDIFF(DAY, MAX(Date), '2025-02-05') > 3;\n";
                     // Endelig fikset alarm. Virkelig ringe data...
                     try (Connection connection = DatabaseManager.getConnection();
                          Statement statement = connection.createStatement();
                          ResultSet resultSet = statement.executeQuery(query)) {
                         //En while loop der kører igennem resultSet og tilføjer data til responderData listen
                         while (resultSet.next()) {
                             String responder = resultSet.getString("Responder");
                             int daysSinceLastVisit = resultSet.getInt("days_since_last_visit");

                             responderData.add(new ResponderData(responder, daysSinceLastVisit));
                         }
                     } catch (SQLException e) {
                         e.printStackTrace();
                     }
                 }
                 //En initialiserings metode der kaldes automatisk når controlleren indlæses
                 public void initialize() {
                     //Binder kolonnerne i tabellen til ResponderData klassen
                     responderColumn.setCellValueFactory(data -> data.getValue().responderProperty());
                     daysSinceLastVisitColumn.setCellValueFactory(data -> data.getValue().daysSinceLastVisitProperty());
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
                         //Får dig tilbage til den menu der passer til dit login
                         if ("SUPERUSER".equals(role)) {
                             HelperMethods.loadScene("SMenu.fxml", BackToMenuButton);
                         } else {
                             HelperMethods.loadScene("Menu.fxml", BackToMenuButton);
                         }

                     });
                 }
             }