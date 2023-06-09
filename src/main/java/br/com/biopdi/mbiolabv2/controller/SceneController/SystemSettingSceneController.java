package br.com.biopdi.mbiolabv2.controller.SceneController;

/*
 *  Copyright (c) 2023.
 *  Licensed under the Biopdi® License, Version 1.0.
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://biopdi.com.br/
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import br.com.biopdi.mbiolabv2.controller.repository.dao.EssayDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemParameterDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemVariableDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.UserDAO;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import br.com.biopdi.mbiolabv2.model.bean.User;
import com.fazecast.jSerialComm.SerialPort;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class SystemSettingSceneController implements Initializable {
    private SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private SystemParameter systemParameter = systemParameterDAO.find();
    private SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private EssayDAO essayDAO = new EssayDAO();
    private UserDAO userDAO = new UserDAO();
    private User user;
    @FXML
    private AnchorPane apUserUpdate;
    @FXML
    private ComboBox cbPorts, cbLanguage, cbSound, cbTheme, cbLoginList;
    @FXML
    private Button btnConnect, btnSettingSave, btnUserUpdate, btnFindUser;
    @FXML
    private RadioButton rbUserEssayDelete;
    @FXML
    private Label lbCurrentData, lbUserUpdate, lbUserUpdateAlert, lbLastBackupDate;
    @FXML
    private TextField txtName, txtLogin, txtPassword;
    private SerialPort port;


    SimpleDateFormat brasilianDay = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat brasilianHour = new SimpleDateFormat("HH-mm");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SystemVariable systemVariable = sysVarDAO.find();
        //Se o usuario logado não for Biopdi ou admin, a manipulação dos usuarios fica indisponivel
        if(systemVariable.getUserId()>2){
            apUserUpdate.setDisable(true);
        }
        lbUserUpdate.setVisible(false);
        lbUserUpdateAlert.setVisible(false);
        portConnectionList();
        systemLanguageList();
        soundSelect();
        themeSelect();
        loginList();

        //Insere valor inicial aos comboBox (set dos atributos de systemParameter - global)
        //Dispensa a seleção de todos os comboBox novamente
        cbPorts.setValue(systemParameter.getPortName());
        cbLanguage.setValue(systemParameter.getSystemLanguage());
        cbSound.setValue(systemParameter.getSoundOn());
        cbTheme.setValue(systemParameter.getTheme());
        //Ixibe nas comboBox os valores salvos anteriormente e presentes no DB
        cbPorts.setPromptText(systemParameter.getPortName());
        cbLanguage.setPromptText(systemParameter.getSystemLanguage());
        cbSound.setPromptText(systemParameter.getSoundOn());
        cbTheme.setPromptText(systemParameter.getTheme());

        lastBackupDate();
        cbLoginList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                showUser();
            }
        });

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

    /**
     * Método de listagem de idiomas dentro do ComboBox (cbLanguage)
     */
    private void systemLanguageList() {
        String[] language = new String[]{"Português", "Inglês", "Espanhol"};
        for (String lang : language) {
            cbLanguage.getItems().add(lang);
        }
    }

    /**
     * Método de listagem status ON e OFF dentro do ComboBox (cbSound)
     */
    private void soundSelect() {
        String[] sound = new String[]{"ON", "OFF"};
        for (String option : sound) {
            cbSound.getItems().add(option);
        }
    }

    /**
     * Método de listagem de temas possíveis dentro do ComboBox (cbTheme)
     */
    private void themeSelect() {
        String[] theme = new String[]{"Claro", "Escuro"};
        for (String option : theme) {
            cbTheme.getItems().add(option);
        }
    }

    /**
     * Método que salva os parâmetros selecionados
     */
    @FXML
    public void userSettingSave() {
        SystemParameter sysPar = systemParameterDAO.find();
        if (sysPar != null) {
            sysPar.setPortName(cbPorts.getSelectionModel().getSelectedItem().toString());
            sysPar.setSystemLanguage(cbLanguage.getSelectionModel().getSelectedItem().toString());
            sysPar.setSoundOn(cbSound.getSelectionModel().getSelectedItem().toString());
            sysPar.setTheme(cbTheme.getSelectionModel().getSelectedItem().toString());
            systemParameterDAO.update(sysPar);
        } else {
            SystemParameter systemParameter = new SystemParameter(
                cbPorts.getSelectionModel().getSelectedItem().toString(),
                cbLanguage.getSelectionModel().getSelectedItem().toString(),
                cbSound.getSelectionModel().getSelectedItem().toString(),
                cbTheme.getSelectionModel().getSelectedItem().toString());
                systemParameterDAO.create(systemParameter);
        }

    }

    /**
     * Método de listagem do login de todos os usuarios cadastrados
     */
    @FXML
    private void loginList(){
        SystemVariable sysVar = sysVarDAO.find();
        List<User> loginList = userDAO.findAll();
        for (User login : loginList) {
            if(sysVar.getUserId()==1 && login.getUserId()>1){
                cbLoginList.getItems().add(login.getUserLogin());
            } else if(sysVar.getUserId()==2){
                if (login.getUserId()==2 || login.getUserId()>3){
                    cbLoginList.getItems().add(login.getUserLogin());
                }
            }
        }
    }

    /**
     * Metodo que carrega informações do usuario selecionado na cbLogin
     */
    @FXML
    private void showUser(){
        lbUserUpdate.setVisible(false);
        lbUserUpdateAlert.setVisible(false);
        user = userDAO.findByLogin(cbLoginList.getSelectionModel().getSelectedItem().toString());
        txtName.setText(user.getUserName());
        txtLogin.setText(user.getUserLogin());
    }

    /**
     * Metodo para atualizar informações do cadastro de usuario pelo Admin
     */
    @FXML
    private void userUpdate(){
        if(txtName.getText()!="" && txtLogin.getText()!=""){
            user.setUserName(txtName.getText());
            user.setUserLogin(txtLogin.getText());
            if(txtPassword.getText()!=""){
                user.setUserPassword(txtPassword.getText());
            } else{
                user.setUserPassword(user.getUserPassword());
            }
            user.setUserImagePath(user.getUserImagePath());
            userDAO.update(user);
            lbUserUpdate.setVisible(true);
            lbUserUpdate.setText("Informações atualizadas!");
            lbUserUpdate.setStyle("-fx-background-color: BLUE");
            lbUserUpdate.setTextFill(Paint.valueOf("#ffffff"));
            cancelDelete();
        } else {
            lbUserUpdateAlert.setVisible(true);
        }
    }

    /**
     * Metodo que reseta os campos e torna user=null, caso ele tenha sido seleciondo no cbLoginList
     */
    @FXML
    private void cancelDelete(){
        loginList();
        user=null;
        txtLogin.setText("");
        txtName.setText("");
        txtPassword.setText("");
        lbUserUpdate.setVisible(false);
        lbUserUpdateAlert.setVisible(false);
    }

    /**
     * Metodo que exclui usuario selecionado pelo Admin
     */
    @FXML
    private void userDelete(){
        if(user.getUserId()>3){
            if(rbUserEssayDelete.isSelected()){
                userEssayDelete(user.getUserId());
            }
            userDAO.delete(user);
            lbUserUpdate.setVisible(true);
            lbUserUpdate.setText("Usuário excluído!");
            lbUserUpdate.setStyle("-fx-background-color: GREEN; -fx-text-fill:  #E5E5E5;-fx-background-radius: 10;");
            cancelDelete();
        } else if(user.getUserId()==2){
            lbUserUpdate.setVisible(true);
            lbUserUpdate.setText("Perfil Admin não pode ser excluído!");
            lbUserUpdate.setStyle("-fx-background-color: RED; -fx-text-fill:  #E5E5E5;-fx-background-radius: 10;");
        }   else if(user.getUserId()==3){
            lbUserUpdate.setVisible(true);
            lbUserUpdate.setText("Perfil Visitante não pode ser excluído!");
            lbUserUpdate.setStyle("-fx-background-color: RED; -fx-text-fill:  #E5E5E5;-fx-background-radius: 10;");
        }
    }

    /**
     * Método que exclui os ensaios do usuario a ser excluido
     * @param pk
     */
    private void userEssayDelete(int pk){
        List<Essay> essayList = essayDAO.findByUser(pk);
        for(Essay essay : essayList){
            essayDAO.delete(essay);
        }
    }

    /**
     * Metodo que gera um backup do banco de dados SQLite e armazena na pasta /backup
     */
    @FXML
    public void backupDB() {
        Date systemDate = new Date();
        String currentDay = brasilianDay.format(systemDate);
        String currentHour = brasilianHour.format(systemDate);
        Path origin = Path.of("database/mBioLabDB.db");
        Path output = Path.of("database/BackupDB/mBioLabDB_BACKUP_"+ currentDay +"_" + currentHour + ".db");

        try {
            Files.copy(origin, output, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo copiado com sucesso.");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao copiar o arquivo: " + e.getMessage());
        }
        lastBackupDate();
    }

    /**
     * Metodo que expoe data e hora do ultimo backup realizado
     */
    public void lastBackupDate(){
        String diretory = "database/BackupDB";

        File dir = new File(diretory);
        File lastBackupFile = null;
        long lastBackupDate = Long.MIN_VALUE;

        if (dir.isDirectory()) {
            File[] fileList = dir.listFiles();

            for (File file : fileList) {
                try {
                    Path arquivoPath = file.toPath();
                    BasicFileAttributes attributes = Files.readAttributes(arquivoPath, BasicFileAttributes.class);
                    long modificationDate = attributes.lastModifiedTime().toMillis();

                    if (modificationDate > lastBackupDate) {
                        lastBackupDate = modificationDate;
                        lastBackupFile = file;
                    }
                } catch (IOException e) {
                    System.out.println("Ocorreu um erro ao ler os atributos do arquivo: " + e.getMessage());
                }
            }
        }

        if (lastBackupFile != null) {
            System.out.println("Arquivo mais recente: " + lastBackupFile.getAbsolutePath());
            lbLastBackupDate.setText(lastBackupFile.getName());
        } else {
            System.out.println("Diretório vazio ou inválido.");
            lbLastBackupDate.setText("Nenhum backup realizado");
        }


    }

}
