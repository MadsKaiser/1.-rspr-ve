package com.example.agrisys;

import javafx.scene.control.CheckBox;

import java.util.HashMap;
import java.util.Map;

public class DashboardState {
    private boolean isPreset; //Boolean for at se om isPreset er true eller false
    private static DashboardState instance; //Her laves en singleton instans af klassen
    private final Map<String, CheckBox> checkBoxes; //Et kort for checkboxes

    private DashboardState() {
        checkBoxes = new HashMap<>();} //En konstruktør der forhindre direkte instansiering af DashboardState
//Metoder der henter en instans af DashboardState
    public static DashboardState getInstance() {
        if (instance == null) {
            instance = new DashboardState();
        }
        return instance;
    }
// Metode der sætter og henter isPreset
    public void setIsPreset(boolean isPreset) {
        this.isPreset = isPreset;
    }

    public boolean isPreset() {
        return isPreset;
    }
//Tilføjer en checkbox med et navn til kortet
    public void addCheckBox(String name, CheckBox checkBox) {
        checkBoxes.put(name, checkBox);
    }
//Henter en checkbox fra kortet ved navn
    public CheckBox getCheckBox(String name) {
        return checkBoxes.get(name);
    }
//Henter alle checkboxes i kortet
    public Map<String, CheckBox> getAllCheckBoxes() {
        return checkBoxes;
    }

    //Metode til at se i map'et hvilke widgets der er valgt
    public Map<String, Boolean> getWidgetStates() {
        Map<String, Boolean> widgetStates = new HashMap<>();
        for (Map.Entry<String, CheckBox> entry : checkBoxes.entrySet()) {
            widgetStates.put(entry.getKey(), entry.getValue().isSelected());
        }
        return widgetStates;
    }
}