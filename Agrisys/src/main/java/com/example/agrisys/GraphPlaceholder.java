package com.example.agrisys;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GraphPlaceholder {

    private final AnchorPane anchorPane;

    public GraphPlaceholder(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public void addLineChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Responder Index");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("FCR");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Responder Index vs FCR");

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT Responder, FCR FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]");
             ResultSet resultSet = statement.executeQuery()) {

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Foderudnyttelse");

            int index = 1;
            while (resultSet.next()) {
                double fcr = resultSet.getDouble("FCR");
                if (fcr < -500 || fcr > 1000) {
                    continue;
                }
                series.getData().add(new XYChart.Data<>(index++, fcr));
            }

            lineChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addChartToPane(lineChart);
    }

    public void addScatterChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Weight gain");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("FCR");

        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("FCR vs. Weight gain");

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT [Weight_gain_kg], FCR FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]");
             ResultSet resultSet = statement.executeQuery()) {

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Foderudnyttelse vs Weight gain");

            while (resultSet.next()) {
                double weightGain = resultSet.getDouble("Weight_gain_kg");
                double fcr = resultSet.getDouble("FCR");
                if (fcr < -500 || fcr > 1000 || weightGain < 0) {
                    continue;
                }
                series.getData().add(new XYChart.Data<>(weightGain, fcr));
            }

            scatterChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addChartToPane(scatterChart);
    }

    private void addChartToPane(XYChart<Number, Number> chart) {
        double yOffset = 10;
        double spacing = 20;

        for (javafx.scene.Node node : anchorPane.getChildren()) {
            if (node instanceof XYChart) {
                yOffset += ((XYChart<?, ?>) node).getPrefHeight() + spacing;
            }
        }

        AnchorPane.setTopAnchor(chart, yOffset);
        AnchorPane.setLeftAnchor(chart, 10.0);
        chart.setPrefHeight(200);
        chart.setPrefWidth(400);

        anchorPane.getChildren().add(chart);
    }
}