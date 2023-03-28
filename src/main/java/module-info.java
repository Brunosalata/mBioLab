module br.com.biopdi.mbiolabv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires java.sql;


    opens br.com.biopdi.mbiolabv2 to javafx.fxml;
    exports br.com.biopdi.mbiolabv2;
    exports br.com.biopdi.mbiolabv2.controller.SceneController;
    opens br.com.biopdi.mbiolabv2.controller.SceneController to javafx.fxml;
}