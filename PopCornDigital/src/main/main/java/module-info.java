module PopCornDigital {
    requires java.sql;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    exports vista;
    opens vista to javafx.fxml;
    opens dto to javafx.base;
}