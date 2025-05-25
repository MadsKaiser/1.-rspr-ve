package com.example.agrisys;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.chart.CategoryAxis;

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
    //Tilføjer et linjediagram, der viser FCR i forhold til Responder Index
    public void addLineChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Responder Index");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("FCR");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Responder Index vs FCR");
        //Bruger en PreparedStatement fra DatabaseManager til at hente data fra databasen
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT Responder, FCR FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]");
             ResultSet resultSet = statement.executeQuery()) {

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Foderudnyttelse");
            //Udelukker nogle FCR værdier der er uden for det acceptable interval
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
    //Tilføjer et scatter-diagram, der viser FCR i forhold til vægtøgning
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
    //Tilføjer et cirkeldiagram, der viser vægtfordelingen af grise
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
        //En forespørgsel til at tælle det totale antal grise
        String totalQuery = "SELECT COUNT(DISTINCT Responder) AS TotalPigs FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             PreparedStatement totalStatement = connection.prepareStatement(totalQuery);
             ResultSet resultSet = statement.executeQuery();
             ResultSet totalResultSet = totalStatement.executeQuery()) {

            int totalPigs = 0; //En variabel til at gemme det totale antal grise
            if (totalResultSet.next()) {
                totalPigs = totalResultSet.getInt("TotalPigs");
            }

            while (resultSet.next()) { //Her bruges en løkke til at hente og indsætte data i cirkeldiagrammet
                String weightRange = resultSet.getString("WeightRange");
                int count = resultSet.getInt("Count");
                double percentage = (count / (double) totalPigs) * 100;
                pieChart.getData().add(new PieChart.Data(weightRange + " (" + String.format("%.1f", percentage) + "%)", count));
            }
        } catch (SQLException e) {
            Logger.getLogger(GraphPlaceholder.class.getName()).log(Level.SEVERE, "Error loading pie chart data", e);
        }

        container.getChildren().add(pieChart);
    }
    //Tilføjer et søjlediagram, der viser FCR i forhold til Responder Index
    public void addBarChart() {
        CategoryAxis xAxis = new CategoryAxis(); // Changed to CategoryAxis
        xAxis.setLabel("Responder Index");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("FCR");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis); // Updated type
        barChart.setTitle("Responder Index vs FCR");

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT Responder, FCR FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]");
             ResultSet resultSet = statement.executeQuery()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Foderudnyttelse");

            while (resultSet.next()) {
                String responder = resultSet.getString("Responder");
                double fcr = resultSet.getDouble("FCR");
                if (fcr < -500 || fcr > 1000) {
                    continue;
                }
                series.getData().add(new XYChart.Data<>(responder, fcr));
            }

            barChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }

        container.getChildren().add(barChart);
    }
}