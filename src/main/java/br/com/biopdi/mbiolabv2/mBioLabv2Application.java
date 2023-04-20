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
//            Parent root = FXMLLoader.load(getClass().getResource("homeScene.fxml"));
//            Parent root = FXMLLoader.load(getClass().getResource("dashboardScene.fxml"));
//            Parent root = FXMLLoader.load(getClass().getResource("essayScene.fxml"));
//            Parent root = FXMLLoader.load(getClass().getResource("reportScene.fxml"));
//            Parent root = FXMLLoader.load(getClass().getResource("systemSettingScene.fxml"));
//            Parent root = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
//            Parent root = FXMLLoader.load(getClass().getResource("userRegisterScene.fxml"));
            Scene scene = new Scene(root, 900, 675);
            stage.setTitle("mBioLab");
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