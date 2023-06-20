module br.com.biopdi.mbiolabv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires jasperreports;
    requires jrviewer.fx;
    requires jfxtras.icalendaragenda;
    requires kernel;
    requires layout;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.media;


    opens br.com.biopdi.mbiolabv2 to javafx.fxml;
    exports br.com.biopdi.mbiolabv2;
    exports br.com.biopdi.mbiolabv2.controller.SceneController;
    exports br.com.biopdi.mbiolabv2.model.bean;
    opens br.com.biopdi.mbiolabv2.controller.SceneController to javafx.fxml;
    exports br.com.biopdi.mbiolabv2.controller.SceneController.switchScene;
    opens br.com.biopdi.mbiolabv2.controller.SceneController.switchScene to javafx.fxml;
}