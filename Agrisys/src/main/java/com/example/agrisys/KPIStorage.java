package com.example.agrisys;

import java.util.ArrayList;
import java.util.List;

public class KPIStorage {
    private static final List<String> savedKPIs = new ArrayList<>();

    public static void saveKPIs(List<String> kpis) {
        savedKPIs.clear();
        savedKPIs.addAll(kpis);
    }

    public static List<String> getSavedKPIs() {
        return new ArrayList<>(savedKPIs);
    }

    public static void clearKPIs() {
        savedKPIs.clear();
    }
}