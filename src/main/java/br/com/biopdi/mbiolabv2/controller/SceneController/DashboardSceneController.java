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
import br.com.biopdi.mbiolabv2.model.bean.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class DashboardSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private final SystemVariableDAO systemVariableDAO = new SystemVariableDAO();

    @FXML
    private BorderPane bpDashboardReport;

    @FXML
    private Label lbFMax, lbFMin, lbFMed, lbFDev, lbPMax, lbPMin, lbPMed, lbPDev, lbTMax, lbTMin, lbTMed, lbTDev,
            lbTEscMax, lbTEscMin, lbTEscMed, lbTEscDev, lbAlongMax, lbAlongMin, lbAlongMed, lbAlongDev,
            lbRedAreaMax, lbRedAreaMin, lbRedAreaMed, lbRedAreaDev, lbMYoungMax, lbMYoungMin, lbMYoungMed, lbMYoungDev;
    @FXML
    private Button btnReport, btnChartClear;
    @FXML
    private TextField txtLed;
    @FXML
    private LineChart<Number, Number> chartMultiLine;
    private XYChart.Series seriesMulti;
    @FXML
    private ListView<Essay> lvEssayInfo, lvSavedEssay;
    @FXML
    private LineChart<Number, Number> chartSingleLine;
    private XYChart.Series seriesSingle;
    private Essay currentEssay;
    private final List<Setup> setupList = new ArrayList<>();
    private final List<User> userList = new ArrayList<>();
    private final List<Essay> essayList = new ArrayList<>();
    private List<Essay> selectedEssayList = new ArrayList<>();
    private List<Double> stanDevFMax = new ArrayList<>();
    private List<Double> stanDevPMax = new ArrayList<>();
    private List<Double> stanDevTMax = new ArrayList<>();
    private List<Double> stanDevTEsc = new ArrayList<>();
    private List<Double> stanDevAlong = new ArrayList<>();
    private List<Double> stanDevRedArea = new ArrayList<>();
    private List<Double> stanDevMYoung = new ArrayList<>();
    private final List<Essay> csvEssayList = new ArrayList<>();
    private ObservableList<Setup> obsSetupList;
    private ObservableList<User> obsUserList;
    private ObservableList<Essay> obsEssayList;
    private ObservableList<Essay> obsEssayByUserIdList;
    private Double maxFMax, maxPMax, maxTMax, maxTEsc, maxAlong, maxRedArea, maxMYoung, minFMax, minPMax, minTMax,
            minTEsc, minAlong, minRedArea, minMYoung, medFMax, medPMax, medTMax, medTEsc, medAlong, medRedArea,
            medMYoung, devFMax, devPMax, devTMax, devTEsc, devAlong, devRedArea, devMYoung;

    Date systemDate = new Date();
    SimpleDateFormat expDay = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat expHour = new SimpleDateFormat("HH-mm-ss");
    String currentDay = expDay.format(systemDate);
    String currentHour = expHour.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        savedEssayList();
        infoReset();


        // Estrutura que permite implementar ações mediante seleção de um item na ListView
        lvSavedEssay.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Essay>() {
            @Override
            public void changed(ObservableValue<? extends Essay> observable, Essay oldValue, Essay newValue) {
                try {
                    currentEssay = lvSavedEssay.getSelectionModel().getSelectedItem();
                    if (currentEssay != null) {
                        if (currentEssay.getEssayChart() == null) {
                            //condicao de nulidade?
                            System.out.println("Problema ao carregar os dados do gráfico! Verifique no banco de dados.");
                        }
                        //alterar calculos dos indicadores
                        //labels
                        addEssayChart(currentEssay.getEssayId());
                        selectedEssayList.add(currentEssay);
                        essayInfo(currentEssay.getEssayId());

                    } else {
                        System.out.println("Problemas ao carregar ensaio. Ensaio = null.");
                    }
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
    private void addEssayChart(int pk) {
        seriesMulti = new XYChart.Series();
        //populating the series with data

        Essay essay = essayDAO.findById(pk);
        seriesMulti.setName(essay.getEssayIdentification());
        // 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10

        String strArraySplit[] = essay.getEssayChart().split(",");
        for (String str : strArraySplit) {
            String dot[] = str.split(";");
            for (int i = 0; i < dot.length; i += 2) {
                System.out.println(dot[i] + " " + dot[i + 1]);
                seriesMulti.getData().add(new XYChart.Data(Double.parseDouble(dot[i]), Double.parseDouble(dot[i + 1])));
            }
        }
        chartMultiLine.getData().add(seriesMulti);
    }

    /**
     * Método que realiza os calculos para as analises estatisticas do grupo amostral dos ensaios selecionados
     *
     * @param pk
     */
    @FXML
    private void essayInfo(int pk) {

        infoReset();

        Double fSum = 0D, pSum = 0D, tSum = 0D, tEscSum = 0D, alongSum = 0D, redAreaSum = 0D, mYoungSum = 0D;
        //labels indicadores
        for(Essay essay : selectedEssayList){

            // Calculos da Forca maxima
            if(essay.getEssayMaxForce() > maxFMax){
                maxFMax = essay.getEssayMaxForce();
            }
            if(minFMax != 0){
                if(essay.getEssayMaxForce() < minFMax){
                    minFMax = essay.getEssayMaxForce();
                }
            } else{
                minFMax = essay.getEssayMaxForce();
            }
            fSum += essay.getEssayMaxForce();
            medFMax = fSum / selectedEssayList.size();
            stanDevFMax.add(essay.getEssayMaxForce());
            devFMax = getStandardDev(stanDevFMax, medFMax);

            // Calculos da Posicao maxima
            if(essay.getEssayMaxPosition() > maxPMax){
                maxPMax = essay.getEssayMaxPosition();
            }
            if(minPMax != 0){
                if(essay.getEssayMaxPosition() < minPMax){
                    minPMax = essay.getEssayMaxPosition();
                }
            } else{
                minPMax = essay.getEssayMaxPosition();
            }
            pSum += essay.getEssayMaxPosition();
            medPMax = pSum / selectedEssayList.size();
            stanDevPMax.add(essay.getEssayMaxPosition());
            devPMax = getStandardDev(stanDevPMax, medPMax);

            // Calculos da Tensao maxima
            if(essay.getEssayMaxTension() > maxTMax){
                maxTMax = essay.getEssayMaxTension();
            }
            if(minTMax != 0){
                if(essay.getEssayMaxTension() < minTMax){
                    minTMax = essay.getEssayMaxTension();
                }
            }else{
                minTMax = essay.getEssayMaxTension();
            }
            tSum += essay.getEssayMaxTension();
            medTMax = tSum / selectedEssayList.size();
            stanDevTMax.add(essay.getEssayMaxTension());
            devTMax = getStandardDev(stanDevTMax, medTMax);

            // Calculos da Tensao escoamento
            if(essay.getEssayEscapeTension() > maxTEsc){
                maxTEsc = essay.getEssayEscapeTension();
            }
            if(minTEsc != 0){
                if(essay.getEssayEscapeTension() < minTEsc){
                    minTEsc = essay.getEssayEscapeTension();
                }
            } else{
                minTEsc = essay.getEssayEscapeTension();
            }
            tEscSum += essay.getEssayEscapeTension();
            medTEsc = tEscSum / selectedEssayList.size();
            stanDevTEsc.add(essay.getEssayEscapeTension());
            devTEsc = getStandardDev(stanDevTEsc, medTEsc);

            // Calculos da Alongamento
            if(essay.getEssayAlong() > maxAlong){
                maxAlong = essay.getEssayAlong();
            }
            if(minAlong != 0){
                if(essay.getEssayAlong() < minAlong){
                    minAlong = essay.getEssayAlong();
                }
            } else{
                minAlong = essay.getEssayAlong();
            }
            alongSum += essay.getEssayAlong();
            medAlong = alongSum / selectedEssayList.size();
            stanDevAlong.add(essay.getEssayAlong());
            devAlong = getStandardDev(stanDevAlong, medAlong);

            // Calculos da Red Area
            if(essay.getEssayAreaRed() > maxRedArea){
                maxRedArea = essay.getEssayAreaRed();
            }
            if(minRedArea != 0){
                if(essay.getEssayAreaRed() < minRedArea){
                    minRedArea = essay.getEssayAreaRed();
                }
            } else{
                minRedArea = essay.getEssayAreaRed();
            }
            redAreaSum += essay.getEssayAreaRed();
            medRedArea = redAreaSum / selectedEssayList.size();
            stanDevRedArea.add(essay.getEssayAreaRed());
            devRedArea = getStandardDev(stanDevRedArea, medRedArea);

            // Calculos da M Young
            if(essay.getEssayMYoung() > maxMYoung){
                maxMYoung = essay.getEssayMYoung();
            }
            if(minMYoung != 0){
                if(essay.getEssayMYoung() < minMYoung){
                    minMYoung = essay.getEssayMYoung();
                }
            } else{
                minMYoung = essay.getEssayMYoung();
            }
            mYoungSum += essay.getEssayMYoung();
            medMYoung = mYoungSum / selectedEssayList.size();
            stanDevMYoung.add(essay.getEssayMYoung());
            devMYoung = getStandardDev(stanDevMYoung, medMYoung);

        }
        setUIValues();
    }

    /**
     * Metodo para o calculo do desvio padrao dos ensaios selecionados
     * @param dValue
     * @param media
     * @return
     */
    public Double getStandardDev(List<Double> dValue, Double media){
        Double stdDev = 0D;
        for(Double value : dValue){
            Double aux = value - media;
            stdDev += aux * aux;
        }
        return Math.sqrt(stdDev / dValue.size());
    }

    /**
     * Metodo que atualiza as label dos calculos da UI
     */
    private void setUIValues(){
        // Atualização das labels de Forca Maxima
        lbFMax.setText(String.valueOf(maxFMax));
        lbFMin.setText(String.valueOf(minFMax));
        lbFMed.setText(String.valueOf(medFMax));
        lbFDev.setText(String.valueOf(devFMax));

        // Atualização das labels de Posicao Maxima
        lbPMax.setText(String.valueOf(maxPMax));
        lbPMin.setText(String.valueOf(minPMax));
        lbPMed.setText(String.valueOf(medPMax));
        lbPDev.setText(String.valueOf(devPMax));

        // Atualização das labels de Tensao Maxima
        lbTMax.setText(String.valueOf(maxTMax));
        lbTMin.setText(String.valueOf(minTMax));
        lbTMed.setText(String.valueOf(medTMax));
        lbTDev.setText(String.valueOf(devTMax));

        // Atualização das labels de Tensao de escoamento
        lbTEscMax.setText(String.valueOf(maxTEsc));
        lbTEscMin.setText(String.valueOf(minTEsc));
        lbTEscMed.setText(String.valueOf(medTEsc));
        lbTEscDev.setText(String.valueOf(devTEsc));

        // Atualização das labels de Alongamento
        lbAlongMax.setText(String.valueOf(maxAlong));
        lbAlongMin.setText(String.valueOf(minAlong));
        lbAlongMed.setText(String.valueOf(medAlong));
        lbAlongDev.setText(String.valueOf(devAlong));

        // Atualização das labels de Redução de Area
        lbRedAreaMax.setText(String.valueOf(maxRedArea));
        lbRedAreaMin.setText(String.valueOf(minRedArea));
        lbRedAreaMed.setText(String.valueOf(medRedArea));
        lbRedAreaDev.setText(String.valueOf(devRedArea));

        // Atualização das labels de M. Young
        lbMYoungMax.setText(String.valueOf(maxMYoung));
        lbMYoungMin.setText(String.valueOf(minMYoung));
        lbMYoungMed.setText(String.valueOf(medMYoung));
        lbMYoungDev.setText(String.valueOf(devMYoung));
    }

    /**
     * Metodo que zera os parametros para os calculos dos ensaios
     */
    public void infoReset(){
        // Zerar variaveis
        maxFMax = 0D;
        maxPMax = 0D;
        maxTMax = 0D;
        maxTEsc = 0D;
        maxAlong = 0D;
        maxRedArea = 0D;
        maxMYoung = 0D;
        minFMax = 0D;
        minPMax = 0D;
        minTMax = 0D;
        minTEsc = 0D;
        minAlong = 0D;
        minRedArea = 0D;
        minMYoung = 0D;
        medFMax = 0D;
        medPMax = 0D;
        medTMax = 0D;
        medTEsc = 0D;
        medAlong = 0D;
        medRedArea = 0D;
        medMYoung = 0D;
        devFMax = 0D;
        devPMax = 0D;
        devTMax = 0D;
        devTEsc = 0D;
        devAlong = 0D;
        devRedArea = 0D;
        devMYoung = 0D;

        // Zerar List para desvio padrao
        stanDevFMax.clear();
        stanDevPMax.clear();
        stanDevTMax.clear();
        stanDevTEsc.clear();
        stanDevAlong.clear();
        stanDevRedArea.clear();
        stanDevMYoung.clear();

    }

    /**
     * Método que retorna todos os ensaios já realizados pelo User
     */
    @FXML
    private void savedEssayList() {
        essayList.addAll(essayDAO.findAll());
        obsEssayList = FXCollections.observableList(essayList);
        lvSavedEssay.setItems(obsEssayList);
    }

    /**
     * Método que reseta o gráfico e a lista de essay analizados
     */
    @FXML
    private void multiDataReset() {
        // Zerar lista de ensaios selecionados
        selectedEssayList.clear();
        // Zerar variaveis calculadas
        infoReset();
        // Atualizar UI com valores zerados
        setUIValues();
        // Zerar lista
        obsEssayList.clear();
        //zerar gráficos
        chartMultiLine.getData().clear();
        savedEssayList();
    }

    /**
     * Metodo que mostra ou esconde o relatório na Dashboard
     */
    @FXML
    private void showReport(){
        if(bpDashboardReport.isVisible()){
            bpDashboardReport.setVisible(false);
            bpDashboardReport.setMinSize(0,0);
            bpDashboardReport.setMaxSize(0,0);
        } else{
            bpDashboardReport.setMinSize(Region.USE_COMPUTED_SIZE,Region.USE_COMPUTED_SIZE);
            bpDashboardReport.setMaxSize(Region.USE_COMPUTED_SIZE,Region.USE_COMPUTED_SIZE);
            bpDashboardReport.setVisible(true);
        }
    }

    @FXML
    private void reportSave(){

    }

    @FXML
    private void reportPrint(){

    }

    @FXML
    private void csvExport() throws IOException {

        File csvFile = new File("src/main/resources/br/com/biopdi/mbiolabv2/export/dashboard/export_" + currentDay + "_" + currentHour + ".csv");
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.ISO_8859_1);
        System.out.println(csvFile);
        try{
            for(Essay essay : selectedEssayList){
                // definição do header da planilha
                fileWriter.append("Força - " + essay.getEssayIdentification());
                fileWriter.append(';');
                fileWriter.append("Posição - " + essay.getEssayIdentification());

                if(selectedEssayList.get(selectedEssayList.size() - 1) != essay){
                    fileWriter.append(';'+""+';');
                } else {
                    fileWriter.append('\n');
                }
            }

            int count = 0;
            for(Essay essaySize : selectedEssayList) {
                String strArraySplitSize[] = essaySize.getEssayChart().split(",");
                if (count < strArraySplitSize.length) {
                    count = strArraySplitSize.length;
                }
                System.out.println(count);
            }

            for(int i = 0; i < count; i++){

                for(Essay essayDot : selectedEssayList){    // varre os ensaios listados
                    String strArraySplitDot[] = new String[count];
                    String strArraySplitDotAux[] = essayDot.getEssayChart().split(",");    // quebra string em pontos
                    strArraySplitDot = Arrays.copyOf(strArraySplitDotAux,strArraySplitDot.length);
                    System.out.println(strArraySplitDotAux.length);
                    System.out.println(strArraySplitDot.length);

                    System.out.println("i = " + i);
                    if(strArraySplitDot[i] != null){
                        String dot[] = strArraySplitDot[i].split(";");  // quebra em forca e posicao
                        System.out.println(dot[0] + " " + dot[1]);
                        fileWriter.append(dot[0] + ';' + dot[1]);
                        fileWriter.append(';'+""+';');
                    } else{
                        fileWriter.append("" + ';' + "");
                        fileWriter.append(';'+""+';');
                    }
                }
                fileWriter.append('\n');
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

////  METODO 2
//        File csvFile = new File("src/main/resources/br/com/biopdi/mbiolabv2/export/dashboard/export_" + currentDay + "_" + currentHour + ".csv");
//        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.ISO_8859_1);
//        System.out.println(csvFile);
//        try{
//            fileWriter.append('\n');
//            for(Essay essay : selectedEssayList){
//
//                fileWriter.append("Força - " + essay.getEssayIdentification());
//                fileWriter.append('\n');
//
//                String strArraySplit[] = essay.getEssayChart().split(",");
//                for (String force : strArraySplit) {
//                    String dotF[] = force.split(";");
//                    // Incluindo valores de forca
//                    for (int i = 0; i < dotF.length; i += 2) {
//                        fileWriter.append(dotF[i]);
//                        if (i < dotF.length - 1) {
//                            fileWriter.append(';');
//                        }
//                        fileWriter.append('\n');
//                    }
//                }
//                fileWriter.append("Posição - " + essay.getEssayIdentification());
//                fileWriter.append('\n');
//
//                for (String position : strArraySplit) {
//                    String dotP[] = position.split(";");
//                    // Incluindo valores de posicao
//                    for (int i = 0; i < dotP.length; i += 2) {
//                        fileWriter.append(dotP[i+1]);
//                        if(i < dotP.length - 1){
//                            fileWriter.append(';');
//                        }
//                        fileWriter.append('\n');
//                    }
//
//                }
//
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            fileWriter.flush();
//            fileWriter.close();
//        }
//        System.out.println("Sucesso");

////  METODO 1
//        File csvFile = new File("src/main/resources/br/com/biopdi/mbiolabv2/export/dashboard/export_" + currentDay + "_" + currentHour + ".csv");
//        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.ISO_8859_1);
//        System.out.println(csvFile);
//        try{
//            for(Essay essay : selectedEssayList){
//                // definição do header da planilha
//                fileWriter.append("Força - " + essay.getEssayIdentification());
//                fileWriter.append(';');
//                fileWriter.append("Posição - " + essay.getEssayIdentification());
//
//                if(selectedEssayList.get(selectedEssayList.size() - 1) != essay){
//                    fileWriter.append(';');
//                } else {
//                    fileWriter.append('\n');
//                }
//            }
//
//            for(Essay essay : selectedEssayList){
//
//                String strArraySplit[] = essay.getEssayChart().split(",");
//                for (String str : strArraySplit) {
//                    String dot[] = str.split(";");
//                    for (int i = 0; i < dot.length; i += 2) {
//                        System.out.println(dot[i] + " " + dot[i + 1]);
//                        fileWriter.append(dot[i]);
//                        fileWriter.append(';');
//                        fileWriter.append(dot[i+1]);
//                        fileWriter.append('\n');
//                    }
//                }
//            }
//            System.out.println(csvFile);
//            System.out.println("csv criado");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            fileWriter.flush();
//            fileWriter.close();
//        }
//        System.out.println("Sucesso");

    }

    private void convertCsv(){

    }

}