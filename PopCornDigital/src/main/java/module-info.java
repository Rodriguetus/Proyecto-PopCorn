module PopCornDigital {
    requires java.sql;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports vista;
    opens vista to javafx.fxml;

    opens dto;
}