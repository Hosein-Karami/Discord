module com.example.discordfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    opens com.example.discordfx to javafx.fxml;
    exports com.example.discordfx;
}