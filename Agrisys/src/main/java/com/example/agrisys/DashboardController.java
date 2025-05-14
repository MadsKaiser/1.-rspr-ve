package com.example.agrisys;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public class DashboardController {

    @FXML
    private Label widget1;

    @FXML
    private Label widget2;

    @FXML
    private Label widget3;

    @FXML
    private Label widget4;

    @FXML
    private Label widget5;

    @FXML
    private Label widget6;

    @FXML
    private Pane dashboardPane;

    @FXML
    private Button BackToMenuButton;

    @FXML
    public void initialize() {
        makeDraggable(widget1);
        makeDraggable(widget2);
        makeDraggable(widget3);
        makeDraggable(widget4);
        makeDraggable(widget5);
        makeDraggable(widget6);
        setupDashboardDrop();

        BackToMenuButton.setOnAction(e -> HelperMethods.loadScene("Menu.fxml", BackToMenuButton));
    }

    private void makeDraggable(Label widget) {
        widget.setOnDragDetected(event -> {
            Dragboard db = widget.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(widget.getText());
            db.setContent(content);
            event.consume();
        });
    }

    private void setupDashboardDrop() {
        dashboardPane.setOnDragOver(event -> {
            if (event.getGestureSource() != dashboardPane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        dashboardPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                Label newWidget = new Label(db.getString());
                newWidget.setLayoutX(event.getX());
                newWidget.setLayoutY(event.getY());
                newWidget.setStyle("-fx-background-color: lightgray; -fx-padding: 10; -fx-border-color: black;");
                dashboardPane.getChildren().add(newWidget);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
}