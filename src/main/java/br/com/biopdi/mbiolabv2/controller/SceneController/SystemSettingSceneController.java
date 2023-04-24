package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemParameterDAO;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SystemSettingSceneController implements Initializable {
    private SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    @FXML
    private ComboBox cbPorts, cbLanguage, cbSound;
    @FXML
    private Button btnConnect, btnSettingSave, btnUserSettingSave;
    @FXML
    private Label lbCurrentData;
    private SerialPort port;

    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        portConnectionList();
        systemLanguageList();
        soundSelect();

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
    private void systemLanguageList() {
        String[] language = new String[]{"Português", "Inglês", "Espanhol"};
        for (String lang : language) {
            cbLanguage.getItems().add(lang);
        }
    }
    private void soundSelect() {
        String[] sound = new String[]{"ON", "OFF"};
        for (String option : sound) {
            cbSound.getItems().add(option);
        }
    }
    public void userSettingSave(){
        SystemParameter sysPar = systemParameterDAO.find();
        if(sysPar!=null){
            sysPar.setPortName(cbPorts.getSelectionModel().getSelectedItem().toString());
            sysPar.setSystemLanguage(cbLanguage.getSelectionModel().getSelectedItem().toString());
            sysPar.setSoundOn(cbSound.getSelectionModel().getSelectedItem().toString());
            systemParameterDAO.update(sysPar);
        } else{
            SystemParameter systemParameter = new SystemParameter(
                cbPorts.getSelectionModel().getSelectedItem().toString(),
                cbLanguage.getSelectionModel().getSelectedItem().toString(),
                cbSound.getSelectionModel().getSelectedItem().toString());
            systemParameterDAO.create(systemParameter);
        }


    }
}
