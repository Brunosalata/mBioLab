package br.com.biopdi.mbiolabv2.controller.SceneController.switchScene;

/*
 *  Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
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

import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemVariableDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.UserDAO;
import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import br.com.biopdi.mbiolabv2.model.bean.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class SwitchMenuSceneController implements Initializable {
    private final SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private SystemVariable sysVar = sysVarDAO.find();
    private final UserDAO userDAO = new UserDAO();
    private User user = userDAO.findById(sysVar.getUserId());
    @FXML
    private Label lbCurrentData, lbUserName, lbLogin;
    @FXML
    private AnchorPane apSwitchMenu;
    @FXML
    private BorderPane mainPane;
    @FXML
    private ImageView ivLogin, ivUserImage;

    Date systemDate = new Date();
    SimpleDateFormat dateComplete = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Alteração do lbUserName e lbLogin dependendo do retorno do banco de dados (null indica que userId do banco é 0, logo, sem login)
        if(user.getUserId()==3){
            lbUserName.setText("Visitante");
            lbLogin.setText("Login");
            ivLogin.setImage(new Image(mBioLabv2Application.class.getResource("img/lightIcon/login.png").toExternalForm()));
        } else{
            System.out.println(user);
            lbUserName.setText(user.getUserName());
            lbLogin.setText("Logout");
            ivLogin.setImage(new Image(mBioLabv2Application.class.getResource("img/lightIcon/logout.png").toExternalForm()));
            System.out.println(user.getUserImagePath());
            // AJUSTAR para imagem do usuário (Não está puxando userImagePath do DB)
            if(user.getUserImagePath()==null){
                ivUserImage.setImage(new Image(mBioLabv2Application.class.getResource("img/lightIcon/user96.png").toExternalForm()));
            } else{
                ivUserImage.setImage(new Image(user.getUserImagePath()));
            }
        }
    }

    /**
     * Método de exibição do relógio no rodapé da janela
     */
    @FXML
    private void clockView() {


    }

    /**
     * Método que abre a Scene Home dentro da SwitchMenu
     *
     * @param event
     */
    @FXML
    private void switchToHomeScene(ActionEvent event) {
        System.out.println("Carregando Home");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("homeScene.fxml");
        mainPane.setCenter(view);
    }

    /**
     * Método que abre a Scene Dashboard dentro da SwitchMenu
     *
     * @param event
     */
    @FXML
    private void switchToDashboardScene(ActionEvent event) {
        System.out.println("Carregando Dashboard");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("dashboardScene.fxml");
        mainPane.setCenter(view);
    }

    /**
     * Método que abre a Scene Essay dentro da SwitchMenu
     *
     * @param event
     */
    @FXML
    private void switchToEssayScene(ActionEvent event) {
        System.out.println("Carregando Essay");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("essayScene.fxml");
        mainPane.setCenter(view);
    }

    /**
     * Método que abre a Scene Report dentro da SwitchMenu
     *
     * @param event
     */
    @FXML
    private void switchToReportScene(ActionEvent event) {
        System.out.println("Carregando Report");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("reportScene.fxml");
        mainPane.setCenter(view);
    }

    /**
     * Método que abre a Scene Support dentro da SwitchMenu
     *
     * @param event
     */
    @FXML
    private void switchToSupportScene(ActionEvent event) {
        System.out.println("Carregando Support");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("supportScene.fxml");
        mainPane.setCenter(view);
    }

    /**
     * Método que abre a Scene SystemSetting dentro da SwitchMenu
     *
     * @param event
     */
    @FXML
    private void switchToSystemSettingScene(ActionEvent event) {
        System.out.println("Carregando System Setting");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("systemSettingScene.fxml");
        mainPane.setCenter(view);
    }

    /**
     * Método que abre a Scene Login dentro da SwitchMenu
     *
     * @param event
     */
    @FXML
    private void logout(ActionEvent event) throws IOException {
        SystemVariable sysVar = sysVarDAO.find();
        sysVar.setUserId(3);
        sysVarDAO.updateUser(sysVar);
        apSwitchMenu.getScene().getWindow().hide();
        openNewScene("loginScene.fxml");
    }

    /**
     * Método que abre a Scene UserRegister dentro da SwitchMenu
     *
     * @param event
     */
    @FXML
    private void editProfile(ActionEvent event) throws IOException {
        openNewScene("userRegisterScene.fxml");
    }

    /**
     * Método genbérico para abertura de nova janela
     * @param fxmlFile
     * @throws IOException
     */
    @FXML
    private void openNewScene(String fxmlFile) throws IOException {
        // abre janela de cadastro
        AnchorPane ap = FXMLLoader.load(mBioLabv2Application.class.getResource(fxmlFile));
        Scene scene = new Scene(ap);
        Stage stage = new Stage();
        stage.setTitle("mBioLab");
        stage.getIcons().add(new Image(mBioLabv2Application.class.getResourceAsStream("img/iconBiopdi.png")));
        stage.setResizable(false);  // Impede redimensionamento da janela
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void closeApp(){
        System.exit(0);
    }
}