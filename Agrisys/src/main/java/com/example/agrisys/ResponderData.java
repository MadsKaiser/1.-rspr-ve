package com.example.agrisys;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
// Benjamin
public class ResponderData {
    private final StringProperty responder;
    private final StringProperty daysSinceLastVisit;

    public ResponderData(String responder, int daysSinceLastVisit) {
        this.responder = new SimpleStringProperty(responder);
        this.daysSinceLastVisit = new SimpleStringProperty(String.valueOf(daysSinceLastVisit));
    }

    public StringProperty responderProperty() {
        return responder;
    }

    public StringProperty daysSinceLastVisitProperty() {
        return daysSinceLastVisit;
    }

    public String getResponder() {
        return responder.get();
    }
    
}