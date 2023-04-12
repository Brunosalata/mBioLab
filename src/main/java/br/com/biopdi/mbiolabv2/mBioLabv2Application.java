package br.com.biopdi.mbiolabv2;


import br.com.biopdi.mbiolabv2.controller.SceneController.MainSceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;

public class mBioLabv2Application extends Application {
    MainSceneController ms = new MainSceneController();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(mBioLabv2Application.class.getResource("mainScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 675);
        stage.setTitle("mBioLab");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();

    }
}