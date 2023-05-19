package br.com.biopdi.mbiolabv2.controller.SceneController;

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

import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import br.com.biopdi.mbiolabv2.model.bean.Setup;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import br.com.biopdi.mbiolabv2.model.bean.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class ReportSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final SystemParameterDAO sysParDAO = new SystemParameterDAO();
    private final SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private final SystemVariable sysVar = sysVarDAO.find();

    @FXML
    private Label lbCurrentData, lbEssayUserName, lbFmax, lbPmax, lbTmax, lbTesc, lbAlong, lbRedArea, lbMYoung;
    @FXML
    private Button btnEssayByUserId, btnEssaySave;
    @FXML
    private ListView<Essay> lvSavedEssay;
    @FXML
    private LineChart<Number, Number> chartSingleLine;
    private XYChart.Series seriesSingle;
    private Essay currentEssay;
    private final List<Setup> setupList = new ArrayList<>();
    private final List<User> userList = new ArrayList<>();
    private final List<Essay> essayList = new ArrayList<>();
    private final List<Essay> essayByUserIdList = new ArrayList<>();
    private ObservableList<Setup> obsSetupList;
    private ObservableList<User> obsUserList;
    private ObservableList<Essay> obsEssayList;
    private ObservableList<Essay> obsEssayByUserIdList;
    private Stage stage2 = new Stage();
    private TilePane r = new TilePane();

    Date systemDate = new Date();
    SimpleDateFormat expDay = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat expHour = new SimpleDateFormat("HH-mm-ss");
    String currentDay = expDay.format(systemDate);
    String currentHour = expHour.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lastEssay();
        savedEssayView(sysVar.getUserId());

        lvSavedEssay.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Essay>() {
            @FXML
            @Override
            public void changed(ObservableValue<? extends Essay> observable, Essay oldValue, Essay newValue) {

                try {
                    // currentEssay recebe o objeto selecionado na lvSavedEssay
                    currentEssay = lvSavedEssay.getSelectionModel().getSelectedItem();
                    if (currentEssay.getEssayChart() == null) {
                        System.out.println("Problema ao carregar os dados do gráfico! Verifique no banco de dados.");
                    }
                    essayChart(currentEssay.getEssayId());
                    essayInfo(currentEssay.getEssayId());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });



    }

    /**
     * Método que busca a String do DB e o converte em pontos no gráfico
     *
     * @param pk
     */
    @FXML
    private void essayChart(int pk) {
        chartSingleLine.getData().clear();
        XYChart.Series seriesSingle = new XYChart.Series();
        //populating the series with data

        Essay essay = essayDAO.findById(pk);
        seriesSingle.setName(essay.getEssayIdentification());
        // 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10

        String strArraySplit[] = essay.getEssayChart().split(",");
        for (String str : strArraySplit) {
            String dot[] = str.split(";");
            for (int i = 0; i < dot.length; i += 2) {
                System.out.println(dot[i] + " " + dot[i + 1]);
                seriesSingle.getData().add(new XYChart.Data(Double.parseDouble(dot[i]), Double.parseDouble(dot[i + 1])));
            }
        }
        chartSingleLine.getData().add(seriesSingle);
    }

    /**
     * Método que busca o último essay registrado, bem como os parâmetros e gráfico, considerando
     * o userId global no system Variable
     */
    @FXML
    private void lastEssay() {
        // Identifica se id do usuario logado
        if(sysVar.getUserId()<=2){
            currentEssay = essayDAO.findLastId();
        } else{
            currentEssay = essayDAO.findLastIdByUser(sysVar.getUserId());
        }
        // Construcao do grafico a partir das informacoes essayChart do DB do respectivo ensaio
        essayChart(currentEssay.getEssayId());
        //buscando informacoes do currentEssay
        lbFmax.setText(String.valueOf(0.001));
        lbPmax.setText(String.valueOf(0.002));
        lbTmax.setText(String.valueOf(0.003));
        lbTesc.setText(String.valueOf(0.004));
        lbAlong.setText(String.valueOf(0.005));
        lbRedArea.setText(String.valueOf(0.006));
        lbMYoung.setText(String.valueOf(0.007));
    }

    /**
     * Método que busca um essay pelo id e apresenta na lwEssayInfo de forma tratada
     *
     * @param pk
     */
    @FXML
    private void essayInfo(Integer pk) {
        Essay essayInfo = essayDAO.findById(pk);
        //buscando informacoes do essayInfo
        lbFmax.setText(String.valueOf(essayInfo.getEssayMaxForce()));
        lbPmax.setText(String.valueOf(essayInfo.getEssayMaxPosition()));
        lbTmax.setText(String.valueOf(essayInfo.getEssayMaxTension()));
        lbTesc.setText(String.valueOf(essayInfo.getEssayEscapeTension()));
        lbAlong.setText(String.valueOf(essayInfo.getEssayAlong()));
        lbRedArea.setText(String.valueOf(essayInfo.getEssayAreaRed()));
        lbMYoung.setText(String.valueOf(essayInfo.getEssayMYoung()));
    }

    /**
     * Método que lista todos os ensaios salvos na lwSavedEssay
     */
    @FXML
    private void savedEssayView(Integer pk) {
        if(sysVar.getUserId()<=2){
            essayList.addAll(essayDAO.findAll());
        } else{
            essayList.addAll(essayDAO.findByUser(pk));
        }
        obsEssayList = FXCollections.observableList(essayList);
        lvSavedEssay.setItems(obsEssayList);
    }

    /**
     * Método que reseta o gráfico da essay analizada
     */
    @FXML
    private void dataReset() {
        chartSingleLine.getData().clear();
        lastEssay();
    }

    @FXML
    private void reportSave(){

    }

    @FXML
    private void reportPrint(){

    }

    @FXML
    private void csvExport() throws IOException {

        File csvFile = new File("src/main/resources/br/com/biopdi/mbiolabv2/export/report/export_" + currentDay + "_" + currentHour + ".csv");
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.ISO_8859_1);
        System.out.println(csvFile);
        try{
            // definição do header da planilha
            fileWriter.append("Força");
            fileWriter.append(';');
            fileWriter.append("Posição");
            fileWriter.append('\n');
            Essay essay = essayDAO.findById(currentEssay.getEssayId());
            // 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10

            String strArraySplit[] = essay.getEssayChart().split(",");
            for (String str : strArraySplit) {
                String dot[] = str.split(";");
                for (int i = 0; i < dot.length; i += 2) {
                    System.out.println(dot[i] + " " + dot[i + 1]);
                    fileWriter.append(dot[i]);
                    fileWriter.append(';');
                    fileWriter.append(dot[i+1]);
                    fileWriter.append('\n');
                }
            }
            System.out.println(csvFile);
            System.out.println("csv criado");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            fileWriter.flush();
            fileWriter.close();
        }
        System.out.println("Sucesso");
    }

}
