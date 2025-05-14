package com.example.agrisys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KPIStorage {
    private static final Map<String, Boolean> kpiStates = new HashMap<>();
    private static final List<String> savedKPIs = new ArrayList<>();

    public static void setKPIState(String kpiName, boolean state) {
        kpiStates.put(kpiName, state);
    }

    public static boolean getKPIState(String kpiName) {
        return kpiStates.getOrDefault(kpiName, false);
    }

    public static List<String> getSavedKPIs() {
        return new ArrayList<>(savedKPIs);
    }

    public static void clearKPIs() {
        savedKPIs.clear();
    }

    public static void saveKPI(String kpi) {
        savedKPIs.add(kpi);
    }
}