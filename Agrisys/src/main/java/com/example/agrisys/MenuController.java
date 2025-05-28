package com.example.agrisys;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

import static com.example.agrisys.HelperMethods.loadScene;
// Benjamin og Mads
public class MenuController {
    @FXML
    private VBox VBoxMenu; // Opdateret fra AnchorPane til VBox
    @FXML
    private VBox InnerVBox; // Tilføjet for at holde KPI'er
    @FXML
    private Button LogoutButton;
    @FXML
    private Button AlarmButton;
    @FXML
    private Button ExportCSVButton;
    @FXML
    private Button ExportWidgets;

    public void initialize() {
        loadKPIs(KPIStorage.getSavedKPIsWithValues()); // Loader dine gemte KPI'er
        loadWidget(); // Loader dine gemte widgets
        LogoutButton.setOnAction(e -> loadScene("Login.fxml", LogoutButton));
        AlarmButton.setOnAction(e -> loadScene("Alarm.fxml", AlarmButton));
        ExportCSVButton.setOnAction(e -> loadScene("Export.fxml", ExportCSVButton));
        ExportWidgets.setOnAction(e -> handleExportWidget());

    }

    public void loadKPIs(Map<String, String> kpiValues) {
        InnerVBox.setSpacing(10); // Laver mellemrum mellem KPI'er
        InnerVBox.setStyle("-fx-padding: 10; -fx-alignment: top-left;"); // Juster layout

        kpiValues.forEach((kpi, value) -> {
            Label kpiLabel = new Label(kpi + ": " + value);
            kpiLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            InnerVBox.getChildren().add(kpiLabel); // Tilføj KPI'er til VBox
        });
    }
    private void loadWidget(){
    InnerVBox.setSpacing(20);
    InnerVBox.setStyle("-fx-padding: 10; -fx-alignment: top-left;");

    // GraphPlaceholder bruges til at tilføje grafer
    GraphPlaceholder graphPlaceholder = new GraphPlaceholder(InnerVBox);

    Set<String> selectedWidgets = WidgetState.getInstance().getSelectedWidgets();
    if (selectedWidgets.isEmpty()) {
        Label noWidgetsLabel = new Label("Ingen widgets valgt.");
        noWidgetsLabel.setStyle("-fx-font-size: 14px;");
        InnerVBox.getChildren().add(noWidgetsLabel);
    } else {
        for (String widgetName : selectedWidgets) {
            switch (widgetName.toLowerCase()) {
                case "linechart":
                    graphPlaceholder.addLineChart();
                    break;
                case "scatterchart":
                    graphPlaceholder.addScatterChart();
                    break;
                case "barchart":
                    graphPlaceholder.addBarChart();
                    break;
                case "piechart":
                    graphPlaceholder.addPieChart();
                    break;
                default:
                    Label unknown = new Label("Unknown widget: " + widgetName);
                    InnerVBox.getChildren().add(unknown);
            }
        }
        }
    }
    private void handleExportWidget() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save graphs as PDF");
        fileChooser.setInitialFileName("widgets.pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(InnerVBox.getScene().getWindow());

        if (file == null) return;

        try {
            SnapshotParameters params = new SnapshotParameters();
            WritableImage fxImage = InnerVBox.snapshot(params, null);

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

            HelperMethods.Alert2("Info", "PDF saved as: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            HelperMethods.Alert2("Fejl", "Could not save PDF: " + e.getMessage());
        }
    }
}