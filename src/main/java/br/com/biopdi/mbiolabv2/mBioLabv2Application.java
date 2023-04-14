package br.com.biopdi.mbiolabv2;


import br.com.biopdi.mbiolabv2.controller.SceneController.HomeSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mBioLabv2Application extends Application {
    HomeSceneController ms = new HomeSceneController();

    @Override
    public void start(Stage stage) throws IOException {
        try{

            Parent root = FXMLLoader.load(getClass().getResource("homeScene.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

//            FXMLLoader fxmlLoader = new FXMLLoader(mBioLabv2Application.class.getResource("homeScene.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(), 900, 675);
//            stage.setTitle("mBioLab");
//            stage.setScene(scene);
//            stage.show();

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();

    }
}