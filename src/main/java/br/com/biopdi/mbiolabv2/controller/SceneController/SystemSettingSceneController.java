package br.com.biopdi.mbiolabv2.controller.SceneController;

import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SystemSettingSceneController implements Initializable {

    @FXML
    private ComboBox cbPorts;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        portConnectionList();
    }


    /**
     * Método de listagem de portas Seriais disponíveis dentro do ComboBox (cbPorts)
     */
    private void portConnectionList() {
        SerialPort[] portNames = SerialPort.getCommPorts();
        for (SerialPort portName : portNames) {
            cbPorts.getItems().add(portName.getSystemPortName());
        }
    }


}
