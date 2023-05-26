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

import br.com.biopdi.mbiolabv2.controller.repository.dao.SetupDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemParameterDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemVariableDAO;
import br.com.biopdi.mbiolabv2.controller.serial.SerialConnection;
import br.com.biopdi.mbiolabv2.model.bean.Setup;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import javafx.application.Application;
import javafx.fxml.FXML;
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
    private SetupDAO setupDAO = new SetupDAO();
    private Integer setupId, userId, MC1M1, MC2M1, MC3M1, MC4M1, MC5M1, MC6M1, MC7M1, MC8M1, MC9M1, MC10M1, MC11M1,
            MC12M1, MC13M1, MC14M1, MC15M1, MC16M1, MC17M1, MC18M1, MC19M1, MC20M1, MC21M1, MC22M1, MC23M1, MC24M1,
            MC25M1, MC26M1, MC27M1, MC28M1, MC29M1, MC30M1;
    @Override
    public void start(Stage primaryStage) throws IOException {

        splashScene();
        initialSetup();
        setupParameter();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginScene.fxml"));
        Parent login = loader.load();
        primaryStage.setTitle("mBioLab");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/iconBiopdi.png")));
        primaryStage.setResizable(false);  // Impede redimensionamento da janela
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
    }

    /**
     * Método de inserção de parâmetros iniciais
     */
    private void initialSetup() {
        sysVar.setUserId(3);
        sysVarDAO.updateUser(sysVar);
    }

    /**
     * Método de conexão automática, buscando o portName do systemSetting no DB
     */
    @FXML
    private void setupParameter() {
        // Instancia classe de conexao via porta serial
        SerialConnection serialConn = new SerialConnection();
        // abertura de porta
        serialConn.openPort();
        // Leitura dos parâmetros MCs
        System.out.println("Lendo parâmetros");
        try {
            Thread.sleep(1000);
            // Leitura do MC1M1
            serialConn.outputInjection("a");
            Thread.sleep(0);
            MC1M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M2
            serialConn.outputInjection("b");
            Thread.sleep(0);
            MC2M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M3
            serialConn.outputInjection("c");
            Thread.sleep(0);
            MC3M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M4
            serialConn.outputInjection("d");
            Thread.sleep(0);
            MC4M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M5
            serialConn.outputInjection("e");
            Thread.sleep(0);
            MC5M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M6
            serialConn.outputInjection("f");
            Thread.sleep(0);
            MC6M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M7
            serialConn.outputInjection("g");
            Thread.sleep(0);
            MC7M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M8
            serialConn.outputInjection("h");
            Thread.sleep(0);
            MC8M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M9
            serialConn.outputInjection("i");
            Thread.sleep(0);
            MC9M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M10
            serialConn.outputInjection("j");
            Thread.sleep(0);
            MC10M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M11
            serialConn.outputInjection("k");
            Thread.sleep(0);
            MC11M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M12
            serialConn.outputInjection("l");
            Thread.sleep(0);
            MC12M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M13
            serialConn.outputInjection("m");
            Thread.sleep(0);
            MC13M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M14
            serialConn.outputInjection("n");
            Thread.sleep(0);
            MC14M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M15
            serialConn.outputInjection("o");
            Thread.sleep(0);
            MC15M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M16
            serialConn.outputInjection("p");
            Thread.sleep(0);
            MC16M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M17
            serialConn.outputInjection("q");
            Thread.sleep(0);
            MC17M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M18
            serialConn.outputInjection("r");
            Thread.sleep(0);
            MC18M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M19
            serialConn.outputInjection("s");
            Thread.sleep(0);
            MC19M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M20
            serialConn.outputInjection("t");
            Thread.sleep(0);
            MC20M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M21
            serialConn.outputInjection("u");
            Thread.sleep(0);
            MC21M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M22
            serialConn.outputInjection("v");
            Thread.sleep(0);
            MC22M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M23
            serialConn.outputInjection("w");
            Thread.sleep(0);
            MC23M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M24
            serialConn.outputInjection("x");
            Thread.sleep(0);
            MC24M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M25
            serialConn.outputInjection("y");
            Thread.sleep(0);
            MC25M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M26
            serialConn.outputInjection("z");
            Thread.sleep(0);
            MC26M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M27
            serialConn.outputInjection("{");
            Thread.sleep(0);
            MC27M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M28
            serialConn.outputInjection("|");
            Thread.sleep(0);
            MC28M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M29
            serialConn.outputInjection("}");
            Thread.sleep(0);
            MC29M1 = Integer.valueOf(serialConn.inputValue());
            // Leitura do MC1M30
            serialConn.outputInjection("~");
            Thread.sleep(0);
            MC30M1 = Integer.valueOf(serialConn.inputValue());

            // Persistindo dados no DB
            setupDAO.update(new Setup(1, MC1M1, MC2M1, MC3M1, MC4M1, MC5M1, MC6M1, MC7M1, MC8M1, MC9M1, MC10M1,
                    MC11M1, MC12M1, MC13M1, MC14M1, MC15M1, MC16M1, MC17M1, MC18M1, MC19M1, MC20M1, MC21M1, MC22M1,
                    MC23M1, MC24M1, MC25M1, MC26M1, MC27M1, MC28M1, MC29M1, MC30M1));
        } catch (InterruptedException e) {
        throw new RuntimeException(e);
        }
        // fechando porta serial
        serialConn.closePort();
    }

    /**
     * Método que carrega splash Scene
     */
    public void splashScene() {

    }

    public static void main(String[] args) {
        launch();
    }
}