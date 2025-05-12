//package com.example.agrisys;
//
//import javafx.event.Event;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class HelperMethods {
//    public static void switchWindow(String fxmlFile, Event event) throws IOException {
//        Parent root = FXMLLoader.load(HelperMethods.class.getResource("/com/example/agrisys/" + fxmlFile));
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//    public static void SMenu( Event event) throws IOException {
//        switchWindow("SMenu.fxml", event);
//    }
//}