package com.example.agrisys;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.*;
import java.util.Map;
import java.util.ResourceBundle;
// Mikkel, Benjamin, Mads, Morten
public class SMenuController implements Initializable {
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
    private Button ClearWidgetsButton;
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
    private VBox InnerAnchor;
    @FXML
    private TextField ResponderIDField;
    @FXML
    private Button ExportWidget;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        DashboardState instance = DashboardState.getInstance();

        InnerAnchor.getChildren().addAll(GraphStorage.getInstance().getGraphs());

        setupGraphEventHandlers();
        if (instance.isPreset()) {
            Widget1.fire();
            Widget2.fire();
            Widget3.fire();
        }
        loadKPIs(KPIStorage.getSavedKPIsWithValues());
        setupEventHandlers();
    }
    //Jeg mangler at lave kommentarer på hele klassen
    private void setupEventHandlers() {
        AlarmButton.setOnAction(e -> HelperMethods.loadScene("Alarm.fxml", AlarmButton));
        WidgetsButton.setOnAction(e -> toggleMenuVisibility());
        LogoutButton.setOnAction(e -> HelperMethods.loadScene("Login.fxml", LogoutButton));
        ExportCSVButton.setOnAction(e -> HelperMethods.loadScene("Export.fxml", ExportCSVButton));
        ImportCSVButton.setOnAction(e -> HelperMethods.loadScene("ImportCSV.fxml", ImportCSVButton));
        DashboardsButton.setOnAction(e -> HelperMethods.loadScene("Dashboard.fxml", DashboardsButton));
        KPIButton.setOnAction(e -> HelperMethods.loadScene("KPI.fxml", KPIButton));
        ExportWidget.setOnAction(e -> handleExportWidget());
    }

    private void setupGraphEventHandlers() {
        GraphPlaceholder graph = new GraphPlaceholder(InnerAnchor);
        WidgetState widgetState = WidgetState.getInstance();

        Widget1.setOnAction(event -> {
            if (Widget1.isSelected()) {
                graph.addLineChart();
                widgetState.selectWidget("LineChart");
            } else {
                InnerAnchor.getChildren().removeIf(node -> node instanceof LineChart);
                widgetState.deselectWidget("LineChart");
            }
        });

        Widget2.setOnAction(event -> {
            if (Widget2.isSelected()) {
                graph.addScatterChart();
                widgetState.selectWidget("ScatterChart");
            } else {
                InnerAnchor.getChildren().removeIf(node -> node instanceof ScatterChart);
                widgetState.deselectWidget("ScatterChart");
            }
        });

        Widget3.setOnAction(event -> {
            if (Widget3.isSelected()) {
                graph.addPieChart();
                widgetState.selectWidget("PieChart");
            } else {
                InnerAnchor.getChildren().removeIf(node -> node instanceof PieChart);
                widgetState.deselectWidget("PieChart");
            }
        });

        Widget4.setOnAction(event -> {
            if (Widget4.isSelected()) {
                graph.addBarChart();
                widgetState.selectWidget("BarChart");
            } else {
                InnerAnchor.getChildren().removeIf(node -> node instanceof BarChart);
                widgetState.deselectWidget("BarChart");
            }
        });

        // Genindlæs widgets baseret på gemt tilstand
        if (widgetState.isWidgetSelected("LineChart")) {
            Widget1.setSelected(true);
            graph.addLineChart();
        }
        if (widgetState.isWidgetSelected("ScatterChart")) {
            Widget2.setSelected(true);
            graph.addScatterChart();
        }
        if (widgetState.isWidgetSelected("PieChart")) {
            Widget3.setSelected(true);
            graph.addPieChart();
        }
        if (widgetState.isWidgetSelected("BarChart")) {
            Widget4.setSelected(true);
            graph.addBarChart();
        }
    }


    private void toggleMenuVisibility() {
        hiddenMenu.setVisible(!hiddenMenu.isVisible());
    }

    private void toggleWidget(CheckBox widget, Class<?> chartType, WidgetAction action) {
        if (widget.isSelected()) {
            long responderId = getResponderId();
            if (responderId != -1) {
                action.execute(responderId);
            }
        } else {
            InnerAnchor.getChildren().removeIf(node -> chartType.isInstance(node));
        }
    }

    @FXML
    private void handleFetchResponderData() {
        long responderId = getResponderId();
        if (responderId <= 0) return;

        // Add graphs to the current layout
        GraphService.loadPigGraphs(InnerAnchor, responderId);

        // Debug: Log the number of graphs added to InnerAnchor
        System.out.println("Graphs added to InnerAnchor: " + InnerAnchor.getChildren().size());
        InnerAnchor.getChildren().forEach(node -> System.out.println(node.getClass().getName()));

        // Save graphs in GraphStorage
        GraphStorage.getInstance().clearGraphs(); // Clear previous graphs
        InnerAnchor.getChildren().forEach(node -> {
            if (node instanceof Node) { // Ensure compatibility
                GraphStorage.getInstance().addGraph(node);
            } else {
                System.out.println("Incompatible node type: " + node.getClass().getName());
            }
        });

        // Debug: Log the number of graphs stored in GraphStorage
        System.out.println("Graphs stored in GraphStorage: " + GraphStorage.getInstance().getGraphs().size());
    }


    private long getResponderId() {
        String responderIdText = ResponderIDField.getText();
        if (responderIdText.isEmpty()) {
            HelperMethods.Alert2("Error", "Please enter a Responder ID.");
            return -1;
        }

        try {
            return Long.parseLong(responderIdText);
        } catch (NumberFormatException e) {
            HelperMethods.Alert2("Error", "Responder ID must be a numeric value.");
            return -1;
        }
    }

    private void fetchResponderData(String responderId) {
        try {
            long responderIdLong = Long.parseLong(responderId);

            try (Connection connection = DatabaseManager.getConnection()) {
                String query = "SELECT * FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] WHERE Responder = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, responderIdLong);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    VBox responderWidget = new VBox();
                    responderWidget.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 1; -fx-spacing: 5;");
                    responderWidget.getChildren().add(new Label("Responder Data:"));

                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        String columnValue = resultSet.getString(i);
                        responderWidget.getChildren().add(new Label(columnName + ": " + columnValue));
                    }

                    InnerAnchor.getChildren().add(responderWidget);
                } else {
                    HelperMethods.Alert2("Info", "No data found for Responder: " + responderId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            HelperMethods.Alert2("Error", "Failed to fetch responder data: " + e.getMessage());
        }
    }

    public void loadKPIs(Map<String, String> kpiValues) {
        kpiValues.forEach((kpi, value) -> {
            Label kpiLabel = new Label(kpi + ": " + value);
            kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            InnerAnchor.getChildren().add(kpiLabel);
        });
    }


    private void handleExportWidget() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Gem grafer som PDF");
        fileChooser.setInitialFileName("widgets.pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(InnerAnchor.getScene().getWindow());

        if (file == null) return;

        try {
            SnapshotParameters params = new SnapshotParameters();
            WritableImage fxImage = InnerAnchor.snapshot(params, null);

            BufferedImage awtImage = SwingFXUtils.fromFXImage(fxImage, null);

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(awtImage, "png", imageStream);
            Image image = Image.getInstance(imageStream.toByteArray());

            float scalePercent = (PageSize.A4.getWidth() - 50) / image.getWidth() * 100;
            image.scalePercent(scalePercent);

            document.add(image);
            document.close();

            HelperMethods.Alert2("Info", "PDF gemt som: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            HelperMethods.Alert2("Fejl", "Kunne ikke gemme PDF: " + e.getMessage());
        }
    }

    @FunctionalInterface
    private interface WidgetAction {
        void execute(long responderId);
    }

    @FXML
    void clearWidgets() {
        InnerAnchor.getChildren().clear();
        Widget1.setSelected(false);
        Widget2.setSelected(false);
        Widget3.setSelected(false);
        Widget4.setSelected(false);

        // Ryd alle gemte widgets i WidgetState
        WidgetState.getInstance().clear();
    }
}