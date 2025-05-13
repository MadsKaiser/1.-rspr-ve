module com.example.agrisys {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.agrisys to javafx.fxml;
    exports com.example.agrisys;
}