package com.example.agrisys;

    import javafx.beans.property.SimpleStringProperty;
    import javafx.beans.property.StringProperty;

    import java.util.Date;

    public class ResponderData {
        private final String responder;
        private final Double fcr;
        private Date lastVisitDate;

        public ResponderData(String responder, Double fcr, Date lastVisitDate) {
            this.responder = responder;
            this.fcr = fcr;
            this.lastVisitDate = lastVisitDate;
        }

        public String getResponder() {
            return responder;
        }

        public Double getFcr() {
            return fcr;
        }

        public Date getLastVisitDate() {
            return lastVisitDate;
        }

        public void setLastVisitDate(Date lastVisitDate) {
            this.lastVisitDate = lastVisitDate;
        }

        public StringProperty responderProperty() {
            return new SimpleStringProperty(responder);
        }

        public StringProperty fcrProperty() {
            return new SimpleStringProperty(fcr != null ? fcr.toString() : "N/A");
        }

        public StringProperty lastVisitDateProperty() {
            return new SimpleStringProperty(lastVisitDate != null ? lastVisitDate.toString() : "N/A");
        }
    }