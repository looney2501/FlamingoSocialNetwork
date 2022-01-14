module com.map_toysocialnetwork_gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.pdfbox;
    requires java.desktop;

    opens com.map_toysocialnetwork_gui to javafx.fxml;
    opens com.map_toysocialnetwork_gui.Controller to javafx.fxml;
    opens com.map_toysocialnetwork_gui.Service to javafx.fxml;

    exports com.map_toysocialnetwork_gui;
    exports com.map_toysocialnetwork_gui.Controller;
    exports com.map_toysocialnetwork_gui.Service;
}