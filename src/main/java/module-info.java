module com.client.clentapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.base;
    requires java.sql;



    requires jakarta.jakartaee.api;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires jasperreports;
    requires org.icepdf.ri.viewer;
    requires org.icepdf.core;
    requires java.prefs;
    requires org.apache.poi.poi;


    opens com.client.clientapplication to javafx.fxml;
    exports com.client.clientapplication;
    exports com.quickrest.entities;
    opens com.quickrest.entities to javafx.fxml;
    exports com.client.utilities;
    opens com.client.utilities to javafx.fxml;
    opens com.quickrest.resources to javafx.fxml;
    exports com.quickrest.resources;

}