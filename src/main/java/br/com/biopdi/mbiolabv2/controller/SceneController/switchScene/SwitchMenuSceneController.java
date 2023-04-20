package br.com.biopdi.mbiolabv2.controller.SceneController.switchScene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SwitchMenuSceneController implements Initializable {
    @FXML
    private BorderPane mainPane;

    @FXML
    private void switchToHomeScene(ActionEvent event) {
        System.out.println("Carregando Home");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("homeScene.fxml");
        mainPane.setCenter(view);

    }
    @FXML
    private void switchToSystemSettingScene(ActionEvent event) {
        System.out.println("Carregando System Setting");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("systemSettingScene.fxml");
        mainPane.setCenter(view);
    }
    @FXML
    private void switchToDashboardScene(ActionEvent event) throws IOException {
        System.out.println("Carregando Dashboard");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("dashboardScene.fxml");
        mainPane.setCenter(view);
    }
    @FXML
    private void switchToEssayScene(ActionEvent event) {
        System.out.println("Carregando Essay");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("essayScene.fxml");
        mainPane.setCenter(view);
    }
    @FXML
    private void switchToReportScene(ActionEvent event) {
        System.out.println("Carregando Report");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("reportScene.fxml");
        mainPane.setCenter(view);
    }
    @FXML
    private void switchToLoginScene(ActionEvent event) {
        System.out.println("Carregando Login");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("loginScene.fxml");
        mainPane.setCenter(view);
    }
    @FXML
    private void switchToUserRegisterScene(ActionEvent event) {
        System.out.println("Carregando User Register");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("userRegisterScene.fxml");
        mainPane.setCenter(view);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
