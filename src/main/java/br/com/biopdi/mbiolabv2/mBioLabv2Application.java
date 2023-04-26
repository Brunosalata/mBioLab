package br.com.biopdi.mbiolabv2;


import br.com.biopdi.mbiolabv2.controller.SceneController.HomeSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class mBioLabv2Application extends Application {

    @Override
    public void start(Stage stage) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("switchMenuScene.fxml"));
            Scene scene = new Scene(root, 900, 675);
            stage.setTitle("mBioLab");
            stage.setScene(scene);
            stage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}