module com.example.agrisys {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.agrisys to javafx.fxml;
    exports com.example.agrisys;
}