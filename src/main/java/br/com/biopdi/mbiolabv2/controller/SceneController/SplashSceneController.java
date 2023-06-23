package br.com.biopdi.mbiolabv2.controller.SceneController;
/*
 *  Copyright (c) 2023.
 *  Licensed under the Biopdi® License, Version 1.0.
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://biopdi.com.br/
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bruno Salata Lima - 20/06/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project Essay.java
 */
public class SplashSceneController implements Initializable {

    @FXML
    private AnchorPane apSplash;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        apSplash.setClip(new Circle(233,233,233));
        new SplashScreen().start();
    }

    class SplashScreen extends Thread{
        public void run(){
            try {
                Thread.sleep(2000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        Parent login = null;
                        try {
                            login = FXMLLoader.load(mBioLabv2Application.class.getResource("loginScene.fxml"));
                        } catch (IOException e) {
                            Logger.getLogger(SplashSceneController.class.getName()).log(Level.SEVERE, null, e);
                        }

                        Stage stage = new Stage();
                        stage.setTitle("mBioLab");
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/iconBiopdi.png")));
                        stage.setResizable(false);  // Impede redimensionamento da janela
                        stage.setScene(new Scene(login));
                        stage.show();

                        apSplash.getScene().getWindow().hide();
                    }
                });

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
