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

    // deklarer gemmested til et map til at gemme CheckBox objekter (felt til at opbevare hashmap med CheckBox objekter)

    //her opretter vi et felt som kun kan holde hashmaps med med værdier string og checkbox, og den kalder vi så for checkbox:
//    private final Map<String, CheckBox> checkBoxes;

    //opretter et map til at gemme CheckBox objekter og gemmer i checkboxe

    //her opretter vi så et nyt hashmap og gemmer det tomme hashmap i feltet
    private DashboardState() {
//        checkBoxes = new HashMap<>();
    }

    // kan kaldes fra andre klasser, den tjekker om der allerede er en instans af DashboardState og returnere den, hvis der ikke
    //er en instans, opretter den en ny og returnerer den
    public static DashboardState getInstance() {
        if (instance == null) {
            instance = new DashboardState();
        }
        return instance;
    }

    // metoden der opretter en ny instans af DashboardState
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

    // den metode der returnerer værdien af de forskellige presets (kan kaldes i fx smenuen)
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

//    // Add a checkbox to the map
//    public void addCheckBox(String name, CheckBox checkBox) {
//        checkBoxes.put(name, checkBox);
//    }
//
//    // Retrieve a checkbox by name
//    public CheckBox getCheckBox(String name) {
//        return checkBoxes.get(name);
//    }
//
//    // Retrieve all checkboxes
//    public Map<String, CheckBox> getAllCheckBoxes() {
//        return checkBoxes;
//    }
}
