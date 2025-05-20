package com.example.agrisys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KPIStorage {
    private static final Map<String, String> savedKPIsWithValues = new HashMap<>();

    public static void saveKPIWithValue(String kpi, String value) {
        savedKPIsWithValues.put(kpi, value);
    }

    public static Map<String, String> getSavedKPIsWithValues() {
        return new HashMap<>(savedKPIsWithValues);
    }

    public static void clearKPIs() {
        savedKPIsWithValues.clear();
    }
    public static Set<String> getSavedKPIs() {
        return new HashSet<>(savedKPIsWithValues.keySet());
    }
}