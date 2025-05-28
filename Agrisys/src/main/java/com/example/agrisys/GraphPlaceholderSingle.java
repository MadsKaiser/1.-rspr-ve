package com.example.agrisys;

import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
// Alle
public class GraphPlaceholderSingle {

    public static void addResponder(VBox container, long responderId) {
        Label label = new Label("Responder ID: " + responderId);
        container.getChildren().add(label);
    }
    //Søger efter data på en specifik Responder ID i databasen og tilføjer relevante grafer og labels til containeren Bruges?
    public static void searchResponder(VBox container, long responderId) {
        String query = """
            SELECT Responder, [Weight_gain_kg], FCR
            FROM madserkaiser_dk_db_agrisys.dbo.[PPT data], [Visit data]
            WHERE Responder = ?
        """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, responderId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                addResponder(container, responderId);

                double weightGain = resultSet.getDouble("Weight_gain_kg");
                double fcr = resultSet.getDouble("FCR");

                Label weightGainLabel = new Label("Weight Gain: " + weightGain + " kg");
                Label fcrLabel = new Label("FCR: " + fcr);

                container.getChildren().addAll(weightGainLabel, fcrLabel);
                //Tilføjer alle disse grafer til containeren
                addLineChart(container, responderId);
                addBarChartComparison(container, responderId);
                addPieChart(container, responderId);
                addBarChart(container, responderId);
            } else {
                Label noDataLabel = new Label("No data found for Responder ID: " + responderId);
                container.getChildren().add(noDataLabel);
            }
        } catch (SQLException e) {
            Logger.getLogger(GraphPlaceholderSingle.class.getName()).log(Level.SEVERE, "Error searching responder", e);
        }
    }

    public static void addLineChart(VBox container, long responderId) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Completed Days in Test");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Weight (kg)");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Weight Progress Over Test Days");

        String query = """
        SELECT Completed_days_in_test, Start_weight_kg, End_weight_kg
        FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]
        WHERE Responder = ?
    """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, responderId);
            ResultSet resultSet = statement.executeQuery();

            XYChart.Series<Number, Number> weightSeries = new XYChart.Series<>();
            weightSeries.setName("Weight Progress");

            while (resultSet.next()) {
                int days = resultSet.getInt("Completed_days_in_test");
                double startWeight = resultSet.getDouble("Start_weight_kg");
                double endWeight = resultSet.getDouble("End_weight_kg");

                //Tilføjer at startvægten skal være på den første dag
                weightSeries.getData().add(new XYChart.Data<>(0, startWeight));

                //Tilføjer at slutvægten skal være på den sidste dag
                weightSeries.getData().add(new XYChart.Data<>(days, endWeight));
            }

            lineChart.getData().add(weightSeries);
        } catch (SQLException e) {
            Logger.getLogger(GraphPlaceholderSingle.class.getName()).log(Level.SEVERE, "Error loading line chart data", e);
        }

        container.getChildren().add(lineChart);
    }

    public static void addBarChartComparison(VBox container, long responderId) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Measurement Type");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value (kg)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Comparison of Key Metrics");

        String query = """
        SELECT Start_weight_kg, End_weight_kg, FCR
        FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]
        WHERE Responder = ?
    """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, responderId);
            ResultSet resultSet = statement.executeQuery();

            XYChart.Series<String, Number> metricsSeries = new XYChart.Series<>();
            metricsSeries.setName("Pig Metrics");
            //Tilføjer data til hver søjle i diagrammet
            if (resultSet.next()) {
                double startWeight = resultSet.getDouble("Start_weight_kg");
                double endWeight = resultSet.getDouble("End_weight_kg");
                double fcr = resultSet.getDouble("FCR");
                double weightGain = endWeight - startWeight;

                // Add data to the series
                metricsSeries.getData().add(new XYChart.Data<>("Start", startWeight));
                metricsSeries.getData().add(new XYChart.Data<>("End", endWeight));
                metricsSeries.getData().add(new XYChart.Data<>("Gain", weightGain));
                metricsSeries.getData().add(new XYChart.Data<>("FCR", fcr));
            }

            barChart.getData().add(metricsSeries);
        } catch (SQLException e) {
            Logger.getLogger(GraphPlaceholderSingle.class.getName()).log(Level.SEVERE, "Error loading bar chart data", e);
        }

        container.getChildren().add(barChart);
    }

    public static void addPieChart(VBox container, long responderId) {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Final Weight Composition");

        String query = """
        SELECT Start_weight_kg, End_weight_kg
        FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]
        WHERE Responder = ?
    """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, responderId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double startWeight = resultSet.getDouble("Start_weight_kg");
                double endWeight = resultSet.getDouble("End_weight_kg");
                double weightGain = endWeight - startWeight;

                double totalWeight = startWeight + weightGain;

                //Tilføjer date til cirkeldiagrammet med procentvis fordeling og kg
                PieChart.Data startWeightData = new PieChart.Data(
                        String.format("Start Weight: %.2f kg (%.1f%%)", startWeight, (startWeight / totalWeight) * 100),
                        startWeight
                );
                PieChart.Data weightGainData = new PieChart.Data(
                        String.format("Weight Gain: %.2f kg (%.1f%%)", weightGain, (weightGain / totalWeight) * 100),
                        weightGain
                );

                pieChart.getData().addAll(startWeightData, weightGainData);
            }
        } catch (SQLException e) {
            Logger.getLogger(GraphPlaceholderSingle.class.getName()).log(Level.SEVERE, "Error loading pie chart data", e);
        }

        container.getChildren().add(pieChart);
    }

    public static void addBarChart(VBox container, long responderId) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Metrics");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Values");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("FCR and Total Weight Gain");

        String query = """
        SELECT FCR, [Weight_gain_kg]
        FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]
        WHERE Responder = ?
    """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, responderId);
            ResultSet resultSet = statement.executeQuery();

            XYChart.Series<String, Number> fcrSeries = new XYChart.Series<>();
            fcrSeries.setName("FCR");

            XYChart.Series<String, Number> weightGainSeries = new XYChart.Series<>();
            weightGainSeries.setName("Total Weight Gain");

            while (resultSet.next()) {
                double fcr = resultSet.getDouble("FCR");
                double weightGain = resultSet.getDouble("Weight_gain_kg");

                fcrSeries.getData().add(new XYChart.Data<>("FCR", fcr));
                weightGainSeries.getData().add(new XYChart.Data<>("Total Weight Gain", weightGain));
            }

            barChart.getData().addAll(fcrSeries, weightGainSeries);
        } catch (SQLException e) {
            Logger.getLogger(GraphPlaceholderSingle.class.getName()).log(Level.SEVERE, "Error loading bar chart data", e);
        }

        container.getChildren().add(barChart);
    }
}