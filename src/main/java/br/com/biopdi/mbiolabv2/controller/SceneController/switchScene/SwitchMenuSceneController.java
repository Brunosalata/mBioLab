package br.com.biopdi.mbiolabv2.controller.SceneController.switchScene;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SwitchMenuSceneController implements Initializable {
    private final SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private final SystemVariable sysVar = sysVarDAO.find();
    private final UserDAO userDAO = new UserDAO();
    private final User user = userDAO.findById(sysVar.getUserId());
    @FXML
    private Label lbCurrentData, lbUserName;
    @FXML
    private AnchorPane apSwitchMenu;
    @FXML
    private BorderPane mainPane;

    Date systemDate = new Date();
    SimpleDateFormat dateComplete = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

    @FXML
    private void clockView() {


    }

    private void clockActualize() {

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
    private void logout(ActionEvent event) {
        System.out.println("Carregando Login");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("loginScene.fxml");
        mainPane.setCenter(view);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbUserName.setText(user.getUserName());
    }

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
}
