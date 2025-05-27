package com.example.agrisys;

import java.util.HashSet;
import java.util.Set;

public class WidgetState {
    private static final WidgetState instance = new WidgetState();
    private final Set<String> selectedWidgets = new HashSet<>();

    private WidgetState() {}

    public static WidgetState getInstance() {
        return instance;
    }

    public void selectWidget(String widgetName) {
        selectedWidgets.add(widgetName);
    }

    public void deselectWidget(String widgetName) {
        selectedWidgets.remove(widgetName);
    }

    public boolean isWidgetSelected(String widgetName) {
        return selectedWidgets.contains(widgetName);
    }

    public Set<String> getSelectedWidgets() {
        return new HashSet<>(selectedWidgets);
    }

    public void clear() {
        selectedWidgets.clear();
    }
}