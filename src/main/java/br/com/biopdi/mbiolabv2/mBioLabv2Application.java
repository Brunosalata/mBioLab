package br.com.biopdi.mbiolabv2;

/*
 *  Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
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

import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemVariableDAO;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    private SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private SystemVariable sysVar = sysVarDAO.find();
    @FXML
    public Stage loginStage;
    @Override
    public void start(Stage stage) throws IOException {

        initialSetup();
        Parent login = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
        Scene scene = new Scene(login);
        loginStage = new Stage();
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
        sysVar.setUserId(3);
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