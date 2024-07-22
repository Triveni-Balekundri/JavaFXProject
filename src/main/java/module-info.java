module jforkts.in.myjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.desktop;

    opens project.in.myjavafx to javafx.fxml;
    exports project.in.myjavafx;
}