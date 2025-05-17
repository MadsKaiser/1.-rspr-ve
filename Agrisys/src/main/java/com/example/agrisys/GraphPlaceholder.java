package com.example.agrisys;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphPlaceholder {

    private final VBox container;

    public GraphPlaceholder(VBox container) {
        this.container = container;
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

        container.getChildren().add(lineChart);
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

        container.getChildren().add(scatterChart);
    }

    public void addPieChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Weight Distribution of Pigs");

        String query = """
    SELECT CASE
        WHEN [Weight_gain_kg] BETWEEN 0 AND 50 THEN '0-50 kg'
        WHEN [Weight_gain_kg] BETWEEN 51 AND 100 THEN '51-100 kg'
        WHEN [Weight_gain_kg] BETWEEN 101 AND 150 THEN '101-150 kg'
        ELSE '151+ kg' END AS WeightRange,
        COUNT(*) AS Count
    FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]
    GROUP BY CASE
        WHEN [Weight_gain_kg] BETWEEN 0 AND 50 THEN '0-50 kg'
        WHEN [Weight_gain_kg] BETWEEN 51 AND 100 THEN '51-100 kg'
        WHEN [Weight_gain_kg] BETWEEN 101 AND 150 THEN '101-150 kg'
        ELSE '151+ kg' END
    """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String weightRange = resultSet.getString("WeightRange");
                int count = resultSet.getInt("Count");
                pieChart.getData().add(new PieChart.Data(weightRange, count));
            }
        } catch (SQLException e) {
            Logger.getLogger(GraphPlaceholder.class.getName()).log(Level.SEVERE, "Error loading pie chart data", e);
        }

        container.getChildren().add(pieChart);
    }
}