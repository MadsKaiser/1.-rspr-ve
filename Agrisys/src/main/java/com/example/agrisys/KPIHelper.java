package com.example.agrisys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
// Mads
public class KPIHelper {
    //Henter en KPI fra databasen ved at udføre en SQL-forespørgsel
    public double fetchKPI(String query) {
        double result = 0.0;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                result = resultSet.getDouble(1); //Henter værdien fra den første kolonne
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Sørger for at der er 2 decimaler i resultatet
        return Double.parseDouble(String.format("%.2f", result).replace(",", "."));
    }
    //3 forskellige forespørgsler der henter KPI'er fra databasen
    public String getAverageFCRQuery() {
        return "SELECT AVG([FCR]) FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]";
    }

    public String getAverageEndWeightQuery() {
        return "SELECT AVG([End_weight_kg]) FROM madserkaiser_dk_db_agrisys.dbo.[PPT data]";
    }

    public String getAverageDailyWeightGainQuery() {
        return "SELECT AVG(CAST(Weight_gain_kg AS FLOAT) / CAST(Completed_days_in_test AS FLOAT)) " +
                "FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] " +
                "WHERE Completed_days_in_test > 0";
    }

    public String getFeedConsumptionPerPigPerDayQuery() {
        return "SELECT AVG(CAST(Total_feed_intake_kg AS FLOAT) / CAST(Completed_days_in_test AS FLOAT)) " +
                "FROM madserkaiser_dk_db_agrisys.dbo.[PPT data] " +
                "WHERE Completed_days_in_test > 0";
    }
}