package br.com.biopdi.mbiolabv2;

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

import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemParameterDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemVariableDAO;
import br.com.biopdi.mbiolabv2.controller.serial.SerialConnection;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class mBioLabv2Application extends Application {
    private SystemParameterDAO sysParDAO = new SystemParameterDAO();
    private SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private SystemVariable sysVar = sysVarDAO.find();
    private SerialConnection serialConnection = new SerialConnection();
    public static Boolean isSplashLoaded = false;

    @Override
    public void start(Stage primaryStage) throws IOException {



        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginScene.fxml"));
        Parent login = loader.load();
        primaryStage.setTitle("mBioLab");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/iconBiopdi.png")));
        primaryStage.setResizable(false);  // Impede redimensionamento da janela
        primaryStage.setScene(new Scene(login));
        primaryStage.show();

        // Carrega parâmetros iniciais durante exposicao da cena splash
        SerialConnection serialConn = new SerialConnection();
        Thread t1 = new Thread(()->{
            initialSetup();
            if(serialConn.testConnection()){
                serialConn.setupParameter();
            }
        });
        t1.start();

        // Chama SplashScene a partir da classe SplashSceneController
//        initialSetup();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("splashScene.fxml"));
//        Parent splash = loader.load();
//        primaryStage.initStyle(StageStyle.UNDECORATED);
//        primaryStage.setScene(new Scene(splash));
//        primaryStage.show();
    }

    /**
     * Método de inserção de parâmetros iniciais
     */
    private void initialSetup() {
        sysVar.setUserId(3);
        sysVarDAO.updateUser(sysVar);
    }

    public static void main(String[] args) {
        launch();
    }
}
/*  Caixa Dialog customizada

    Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + selection + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    alert.showAndWait();

    if (alert.getResult() == ButtonType.YES) {
        //do stuff
    }

 */