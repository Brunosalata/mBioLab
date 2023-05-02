package br.com.biopdi.mbiolabv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class mBioLabv2Application extends Application {

    @Override
    public void start(Stage stage) throws IOException {

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