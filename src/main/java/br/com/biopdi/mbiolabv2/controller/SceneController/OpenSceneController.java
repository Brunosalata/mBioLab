package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.SceneController.switchScene.FxmlLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OpenSceneController implements Initializable {

    private BorderPane borderPane;
    @FXML
    private AnchorPane anchorPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        splash();
    }

    private void splash(){
        new Thread() {
            public void run(){
                try{
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            borderPane = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
                            Stage stage = new Stage();
                            Scene scene = new Scene(borderPane);
                            stage.setTitle("mBioLab");
                            stage.setScene(scene);
                            stage.show();
                            anchorPane.getScene().getWindow().hide();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }.start();
    }
}
