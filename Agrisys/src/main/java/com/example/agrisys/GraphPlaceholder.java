//package com.example.agrisys;
//
//import javafx.scene.chart.LineChart;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
//import javafx.scene.layout.Pane;
//
//public class GraphPlaceholder {
//
//    public static void addGraphToPane(Pane targetPane) {
//        // Axes
//        NumberAxis xAxis = new NumberAxis();
//        xAxis.setLabel("X-Axis");
//
//        NumberAxis yAxis = new NumberAxis();
//        yAxis.setLabel("Y-Axis");
//
//        // Line chart
//        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//        lineChart.setTitle("Graph");
//        lineChart.setAnimated(false);
//        lineChart.setLegendVisible(false);
//
//        // Size and position (adjust as needed)
//        lineChart.setPrefSize(300, 200); // small square
//        lineChart.setLayoutX(50);        // x position inside the pane
//        lineChart.setLayoutY(50);        // y position inside the pane
//
//        // Data
//        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        series.getData().add(new XYChart.Data<>(0, 0));
//        series.getData().add(new XYChart.Data<>(1, 1));
//        series.getData().add(new XYChart.Data<>(2, 4));
//        series.getData().add(new XYChart.Data<>(3, 9));
//        lineChart.getData().add(series);
//
//        // Add to the pane without touching existing content
//        targetPane.getChildren().add(lineChart);
//    }
//}