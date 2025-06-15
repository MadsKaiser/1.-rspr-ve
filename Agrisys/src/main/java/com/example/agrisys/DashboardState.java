package com.example.agrisys;

import javafx.scene.control.CheckBox;

import java.util.HashMap;
import java.util.Map;

// Morten
public class DashboardState {
    private static DashboardState instance; // Singleton instance of DashboardState

    // Separate boolean fields for each preset
    private boolean preset1;
    private boolean preset2;
    private boolean preset3;
    private boolean preset4;

    // Map to store CheckBoxes by name
    private final Map<String, CheckBox> checkBoxes;

    // Private constructor to prevent direct instantiation
    private DashboardState() {
        checkBoxes = new HashMap<>();
    }

    // Public method to access the singleton instance
    public static DashboardState getInstance() {
        if (instance == null) {
            instance = new DashboardState();
        }
        return instance;
    }

    // Setters for presets
    public void setPreset1(boolean value) {
        this.preset1 = value;
    }

    public void setPreset2(boolean value) {
        this.preset2 = value;
    }

    public void setPreset3(boolean value) {
        this.preset3 = value;
    }

    public void setPreset4(boolean value) {
        this.preset4 = value;
    }

    // Getters for presets
    public boolean isPreset1() {
        return preset1;
    }

    public boolean isPreset2() {
        return preset2;
    }

    public boolean isPreset3() {
        return preset3;
    }

    public boolean isPreset4() {
        return preset4;
    }

    // Add a checkbox to the map
    public void addCheckBox(String name, CheckBox checkBox) {
        checkBoxes.put(name, checkBox);
    }

    // Retrieve a checkbox by name
    public CheckBox getCheckBox(String name) {
        return checkBoxes.get(name);
    }

    // Retrieve all checkboxes
    public Map<String, CheckBox> getAllCheckBoxes() {
        return checkBoxes;
    }
}
