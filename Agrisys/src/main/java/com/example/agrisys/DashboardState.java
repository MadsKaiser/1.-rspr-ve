package com.example.agrisys;

import javafx.scene.control.CheckBox;

import java.util.HashMap;
import java.util.Map;

public class DashboardState {
    private boolean isPreset;
    private static DashboardState instance;
    private final Map<String, CheckBox> checkBoxes;

    private DashboardState() {
        checkBoxes = new HashMap<>();
    }

    public static DashboardState getInstance() {
        if (instance == null) {
            instance = new DashboardState();
        }
        return instance;
    }

    public void setIsPreset(boolean isPreset) {
        this.isPreset = isPreset;
    }

    public boolean isPreset() {
        return isPreset;
    }

    public void addCheckBox(String name, CheckBox checkBox) {
        checkBoxes.put(name, checkBox);
    }

    public CheckBox getCheckBox(String name) {
        return checkBoxes.get(name);
    }

    public Map<String, CheckBox> getAllCheckBoxes() {
        return checkBoxes;
    }

    // New method to get widget states
    public Map<String, Boolean> getWidgetStates() {
        Map<String, Boolean> widgetStates = new HashMap<>();
        for (Map.Entry<String, CheckBox> entry : checkBoxes.entrySet()) {
            widgetStates.put(entry.getKey(), entry.getValue().isSelected());
        }
        return widgetStates;
    }
}