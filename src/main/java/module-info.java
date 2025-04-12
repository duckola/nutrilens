module com.example.nutrilens {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires okhttp3;
    requires java.desktop;

    opens com.example.nutrilens to javafx.fxml;
    opens com.example.nutrilens.controller to javafx.fxml;

    exports com.example.nutrilens;
    exports com.example.nutrilens.controller;
    exports com.example.nutrilens.model;
}