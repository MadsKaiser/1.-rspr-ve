package com.example.agrisys;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;


public class SMenuController {
    @FXML
    private Button AlarmButton;
    @FXML
    private Button ImportCSVButton;
    @FXML
    private Button ExportCSVButton;
    @FXML
    private Button LogoutButton;
    @FXML
    private Button WidgetsButton;
    @FXML
    private Button DashboardsButton;
    @FXML
    private VBox hiddenMenu;
    @FXML
    private Button KPIButton;
    @FXML
    private CheckBox Widget1;
    @FXML
    private CheckBox Widget2;
    @FXML
    private CheckBox Widget3;
    @FXML
    private CheckBox Widget4;
    @FXML
    private CheckBox Widget5;
    @FXML
    private CheckBox Widget6;
    @FXML
    private Label Label1;
    @FXML
    private Label Label2;
    @FXML
    private Label Label3;
    @FXML
    private Label Label4;
    @FXML
    private Label Label5;
    @FXML
    private Label Label6;
    @FXML
    private AnchorPane Anchor;

    private GraphPlaceholder graphPlaceholder;

    @FXML
    private void toggleMenuVisibility() {
        hiddenMenu.setVisible(!hiddenMenu.isVisible());
    }

    public void initialize() {
        graphPlaceholder = new GraphPlaceholder(Anchor);

        hiddenMenu.setVisible(false);
        Label1.setVisible(false);
        Label2.setVisible(false);
        Label3.setVisible(false);
        Label4.setVisible(false);
        Label5.setVisible(false);
        Label6.setVisible(false);

        AlarmButton.setOnAction(e -> HelperMethods.loadScene("Alarm.fxml", AlarmButton));
        WidgetsButton.setOnAction(e -> toggleMenuVisibility());
        LogoutButton.setOnAction(e -> HelperMethods.loadScene("Login.fxml", LogoutButton));
        ExportCSVButton.setOnAction(e -> HelperMethods.loadScene("Export.fxml", ExportCSVButton));
        ImportCSVButton.setOnAction(e -> HelperMethods.loadScene("ImportCSV.fxml", ImportCSVButton));
        DashboardsButton.setOnAction(e -> HelperMethods.loadScene("Dashboard.fxml", DashboardsButton));
        ExportCSVButton.setOnAction(e -> HelperMethods.loadScene("Export.fxml", ExportCSVButton));

        Widget1.setOnAction(event -> {
            if (Widget1.isSelected()) {
                graphPlaceholder.addLineChart();
            } else {
                Anchor.getChildren().removeIf(node -> node instanceof LineChart);
            }
        });

        Widget2.setOnAction(event -> {
            if (Widget2.isSelected()) {
                graphPlaceholder.addScatterChart();
            } else {
                Anchor.getChildren().removeIf(node -> node instanceof ScatterChart);
            }
        });

        Widget3.setOnAction(event -> Label3.setVisible(Widget3.isSelected()));
        Widget4.setOnAction(event -> Label4.setVisible(Widget4.isSelected()));
        Widget5.setOnAction(event -> Label5.setVisible(Widget5.isSelected()));
        Widget6.setOnAction(event -> Label6.setVisible(Widget6.isSelected()));
    }


    }