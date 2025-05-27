module com.example.agrisys {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires java.desktop;
    requires com.github.librepdf.openpdf;
    requires javafx.swing;

    opens com.example.agrisys to javafx.fxml;
    exports com.example.agrisys;
}