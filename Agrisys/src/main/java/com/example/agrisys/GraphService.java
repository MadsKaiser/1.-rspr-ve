package com.example.agrisys;

import javafx.scene.layout.VBox;

public class GraphService {

    public static void loadPigGraphs(VBox container, long responderId) {
        container.getChildren().clear();
        GraphPlaceholderSingle.addLineChart(container, responderId);
        GraphPlaceholderSingle.addBarChartComparison(container, responderId);
        GraphPlaceholderSingle.addPieChart(container, responderId);
        GraphPlaceholderSingle.addBarChart(container, responderId);
    }
}
