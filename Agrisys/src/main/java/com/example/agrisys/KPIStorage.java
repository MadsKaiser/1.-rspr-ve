package com.example.agrisys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
// Mads
public class KPIStorage { //Et statisk kort til at gemme KPI'er og deres værdier
    private static final Map<String, String> savedKPIsWithValues = new HashMap<>();
    //En metode der gemmer KPI'er med deres værdier i kortet
    public static void saveKPIWithValue(String kpi, String value) {
        savedKPIsWithValues.put(kpi, value);
    }
    //En metode der henter KPI'er med deres værdier fra kortet
    public static Map<String, String> getSavedKPIsWithValues() {
        return new HashMap<>(savedKPIsWithValues);
    }

    public static void clearKPIs() {
        savedKPIsWithValues.clear();
    }
    //Henter alle gemte KPI'er
    public static Set<String> getSavedKPIs() {
        return new HashSet<>(savedKPIsWithValues.keySet());
    }
}