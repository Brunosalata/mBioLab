package br.com.biopdi.mbiolabv2.controller.SceneController.switchScene;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;

public class SwitchMenuSceneController implements Initializable {
    @FXML
    private Label lbCurrentData;
    @FXML
    private BorderPane mainPane;

    Date systemDate = new Date();
    SimpleDateFormat dateComplete = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

    @FXML
    private void clockView(){


    }

    private void clockActualize(){
        Date now = new Date();
        lbCurrentData.setText(dateComplete.format(systemDate));
    }




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


        // Mostra data local na base da aplicação

    }
}
