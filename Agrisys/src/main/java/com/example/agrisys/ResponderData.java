package com.example.agrisys;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResponderData {
    private final StringProperty responder;
    private final StringProperty fcr;

    public ResponderData(String responder, Double fcr) {
        this.responder = new SimpleStringProperty(responder);
        this.fcr = new SimpleStringProperty(fcr.toString());


    }

    public StringProperty responderProperty() {
        return responder;
    }

    public StringProperty fcrProperty() {
        return fcr;
    }

    public String getResponder() {
        return responder.get();
    }

    public String getFcr() {
        return fcr.get();
    }
}