package br.com.biopdi.mbiolabv2;

import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemVariableDAO;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class mBioLabv2Application extends Application {
    private SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private SystemVariable sysVar = sysVarDAO.find();
    @FXML
    private AnchorPane apDarkArea;
    @Override
    public void start(Stage stage) throws IOException {

        initialSetup();
        Parent login = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
        Scene scene = new Scene(login);
        Stage loginStage = new Stage();
        loginStage.setTitle("mBioLab");
        loginStage.getIcons().add(new Image(getClass().getResourceAsStream("img/iconBiopdi.png")));
        loginStage.setResizable(false);  // Impede redimensionamento da janela
        loginStage.setScene(scene);
        loginStage.show();
    }

    /**
     * Método de inserção de parâmetros iniciais
     */
    private void initialSetup() {
        sysVar.setUserId(0);
        sysVarDAO.updateUser(sysVar);
    }

    /**
     * Método que carrega splash Scene
     */
//    public void splashSceneCall() throws IOException {
//
//        Parent pane = FXMLLoader.load(getClass().getResource("openScene.fxml"));
//        Scene open = new Scene(pane);
//        openStage.setScene(open);
//        openStage.initStyle(StageStyle.UNDECORATED);
//        openStage.show();
//    }

    public static void main(String[] args) {
        launch();
    }
}