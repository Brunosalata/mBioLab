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

import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import br.com.biopdi.mbiolabv2.model.bean.Setup;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import br.com.biopdi.mbiolabv2.model.bean.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
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
    private final SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private SystemVariable sysVar = sysVarDAO.find();
    private User user = userDAO.findById(sysVar.getUserId());

    @FXML
    private BorderPane bpDashboardReport;

    @FXML
    private Label lbFMax, lbFMin, lbFMed, lbFDev, lbPMax, lbPMin, lbPMed, lbPDev, lbTMax, lbTMin, lbTMed, lbTDev,
            lbTEscMax, lbTEscMin, lbTEscMed, lbTEscDev, lbAlongMax, lbAlongMin, lbAlongMed, lbAlongDev, lbRedAreaMax,
            lbRedAreaMin, lbRedAreaMed, lbRedAreaDev, lbMYoungMax, lbMYoungMin, lbMYoungMed, lbMYoungDev, lbEssayUserName;
    @FXML
    private Button btnReport, btnChartClear, btnReportSave;
    @FXML
    private TextField txtLed;
    @FXML
    private ImageView ivEssayUser, ivPreviewDashboard;
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
    JRBeanCollectionDataSource essayCollection;

    // Lista de agrupa objetos do tipo ChartAxisValueToJR, que representam cada serie do grafico no JR
    List<ChartAxisValueToJR> XYChartData = new ArrayList<ChartAxisValueToJR>();
    // Map para Armazenar os parametros do relatorio Jasper
    Map<String, Object> parameters = new HashMap<String, Object>();
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
        ivEssayUser.setClip(new Circle(20,20,20));
        lbEssayUserName.setText(user.getUserName());

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

                        // Incluindo dados da serie na List a ser inserida no grafico do Jasper Report
                        serieInclude();

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
                seriesMulti.getData().add(new XYChart.Data(Double.parseDouble(dot[i + 1]), Double.parseDouble(dot[i])));
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

        User user = userDAO.findById(sysVar.getUserId());
        // Definição da imagem do usuario
        if(user.getUserImagePath()!=null){
            ivEssayUser.setImage(new Image("file:\\" + user.getUserImagePath()));
        } else{
            ivEssayUser.setImage(new Image(mBioLabv2Application.class.getResource("img/darkIcon/icons8-profile-96.png").toExternalForm()));
        }

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
    private synchronized void multiDataReset() {
        Platform.runLater(()->{
            // Zerar lista de ensaios selecionados
            selectedEssayList.clear();
            // Zerar XYChartData (List<ChartAxisValueToJR>)
            XYChartData.clear();
            // Zerar Map<String, Object> parameters = new HashMap<String, Object>() que define os parametros enviados ao JR
            parameters.clear();
            // Zerar variaveis calculadas
            infoReset();
            // Atualizar UI com valores zerados
            setUIValues();
            // Zerar lista
            obsEssayList.clear();
            //zerar gráficos
            chartMultiLine.getData().clear();
            savedEssayList();
        });
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
    private void reportSave() {

        Stage stage = (Stage) btnReportSave.getScene().getWindow();
        SwingNode swingNode = new SwingNode();
        StackPane stackPane = new StackPane(swingNode);

        try{
            // Converte lista para JRBeanCollectionDataSource
            essayCollection = new JRBeanCollectionDataSource(selectedEssayList);

            // Adicao de novos parametros para preenchimento de campos do relatorio
            User user = userDAO.findById(currentEssay.getUserId());
            parameters.put("author", user.getUserName());
            parameters.put("introduction", "Parâmetro enviado com sucesso!");
            parameters.put("reportIdentification", "reportIdentification");
            parameters.put("essayUsedMachine", currentEssay.getEssayUsedMachine());
            parameters.put("essayNorm", currentEssay.getEssayNorm());
            parameters.put("chargeCell", currentEssay.getEssayChargeCell());
            parameters.put("essayVelocity", currentEssay.getEssayDislocationVelocity());
            parameters.put("velocityUnit", "mm/min");
            parameters.put("essayType", "Requer implementar código");
            parameters.put("essayDay", currentEssay.getEssayDay());
            parameters.put("essayHour", currentEssay.getEssayHour());
            parameters.put("essayPreCharge", currentEssay.getEssayPreCharge());
            parameters.put("essayTemperature", currentEssay.getEssayTemperature());
            parameters.put("essayRelativeHumidity", currentEssay.getEssayRelativeHumidity());
            parameters.put("chartTitle", "Título");
            parameters.put("xAxisLabel", "Eixo X");
            parameters.put("yAxisLabel", "Eixo Y");
            // Preenchimento da tabela
            parameters.put("CollectionBeanParam", essayCollection);
            // Preenchimento das analises dos ensaios
            parameters.put("maxFMax", maxFMax);
            parameters.put("maxPMax", maxPMax);
            parameters.put("maxTMax", maxTMax);
            parameters.put("maxTEsc", maxTEsc);
            parameters.put("maxAlong", maxAlong);
            parameters.put("maxRedArea", maxRedArea);
            parameters.put("maxMYoung", maxMYoung);
            parameters.put("minFMax", minFMax);
            parameters.put("minPMax", minPMax);
            parameters.put("minTMax", minTMax);
            parameters.put("minTEsc", minTEsc);
            parameters.put("minAlong", minAlong);
            parameters.put("minRedArea", minRedArea);
            parameters.put("minMYoung", minMYoung);
            parameters.put("medFMax", medFMax);
            parameters.put("medPMax", medPMax);
            parameters.put("medTMax", medTMax);
            parameters.put("medTEsc", medTEsc);
            parameters.put("medAlong", medAlong);
            parameters.put("medRedArea", medRedArea);
            parameters.put("medMYoung", medMYoung);
            parameters.put("devFMax", devFMax);
            parameters.put("devPMax", devPMax);
            parameters.put("devTMax", devTMax);
            parameters.put("devTEsc", devTEsc);
            parameters.put("devAlong", devAlong);
            parameters.put("devRedArea", devRedArea);
            parameters.put("devMYoung", devMYoung);

            // Conversao da List<XYChartData> em uma fonte de dados de classe JRBeanCollectionDataSource
            JRBeanCollectionDataSource xyChartDataJR = new JRBeanCollectionDataSource(XYChartData);

            // Adição da lista de dados ao mapa de parâmetros
            parameters.put("xyChartData", xyChartDataJR);

            // Preenchimento do relatorio
            JasperDesign jasperDesign = JRXmlLoader.load(new FileInputStream(new File("src/main/resources/br/com/biopdi/mbiolabv2/jrxml/dashboardReport.jrxml")));

            // Compilando jrxml com a classe JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // Gerar pdf a partir do objeto jasperReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            if (jasperPrint != null && !jasperPrint.getPages().isEmpty()) {

                Stage reportStage = new Stage();
                reportStage.initOwner(stage);
                reportStage.setTitle("Emissão de relatório");
                reportStage.getIcons().add(new Image(mBioLabv2Application.class.getResourceAsStream("img/iconBiopdi.png")));
                reportStage.setOnCloseRequest(event -> swingNode.setContent(null));

                // Chamar ferramentas jasper para expor o relatorio na janela jasperviewer
                JasperViewer viewer = new JasperViewer(jasperPrint, false);

                swingNode.setContent((JComponent) viewer.getContentPane());

                stackPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                StackPane.setMargin(swingNode, new Insets(10));

                reportStage.setScene(new Scene(stackPane, 850, 600));
                reportStage.show();
            }
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Metodo que chama a stringChart do ensaio e plota os valores na area do grafico da interface
     */
    private void serieInclude(){
        // Conversao da String chartEssay para valores double
        String strArraySplit[] = currentEssay.getEssayChart().split(",");
        for (String str : strArraySplit) {
            String dot[] = str.split(";");
            for (int i = 0; i < dot.length; i += 2) {
                // Preenchimento das listas de dados
                XYChartData.add(new ChartAxisValueToJR(Double.parseDouble(dot[i + 1]), Double.parseDouble(dot[i]),
                        currentEssay.getEssayIdentification()));
            }
        }
    }

    /**
     * Classe para a criacao de objeto para preenchimento do grafico no Jasper Report
     */
    public class ChartAxisValueToJR {
        private Double xAxis, yAxis;
        private String chartSerieTitle;

        public ChartAxisValueToJR() {
        }
        public ChartAxisValueToJR(Double xAxis, Double yAxis, String chartSerieTitle) {
            this.xAxis = xAxis;
            this.yAxis = yAxis;
            this.chartSerieTitle = chartSerieTitle;
        }

        public Double getxAxis() {
            return xAxis;
        }
        public void setxAxis(Double xAxis) {
            this.xAxis = xAxis;
        }
        public Double getyAxis() {
            return yAxis;
        }
        public void setyAxis(Double yAxis) {
            this.yAxis = yAxis;
        }
        public String getChartSerieTitle() {
            return chartSerieTitle;
        }
        public void setChartSerieTitle(String chartSerieTitle) {
            this.chartSerieTitle = chartSerieTitle;
        }
    }

    /**
     * Metodo que exporta os ensaios no formato CSV
     * @throws IOException
     */
    @FXML
    private void csvExport() throws IOException {

        // Criar um seletor de arquivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar arquivo CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos CSV", "*.csv"));

        // Abrir a janela de seleção de arquivo
        File selectedFile = fileChooser.showSaveDialog((Stage) btnReportSave.getScene().getWindow());

        if (selectedFile == null) {
            // O usuário cancelou a seleção do arquivo
            return;
        }

        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(selectedFile), StandardCharsets.ISO_8859_1);
        try{
            // Criacao do header, inserindo coluna Forca e Posicao com o nome de cada ensaio
            for(Essay essay : selectedEssayList){
                // definição do header da planilha
                fileWriter.append("Posição - " + essay.getEssayIdentification());
                fileWriter.append(';');
                fileWriter.append("Força - " + essay.getEssayIdentification());
                // inclui coluna vazia entre dois ensaios, ou quebra linha se for o ultimo ensaio
                if(selectedEssayList.get(selectedEssayList.size() - 1) != essay){
                    fileWriter.append(';'+""+';');
                } else {
                    fileWriter.append('\n');
                }
            }

            // definicao do tamanho do vetor base, considerando o ensaio com mair numero de pontos
            int count = 0;
            for(Essay essaySize : selectedEssayList) {
                String strArraySplitSize[] = essaySize.getEssayChart().split(",");
                if (count < strArraySplitSize.length) {
                    count = strArraySplitSize.length;
                }
            }

            // Arquivo csv é preenchido linha por linha, entao, o preenchimento precisa ser feito de forma fragmentada,
            // ou seja, posicao 1 do ensaio 1 (forca e posicao), posicao 1 do ensaio 2 (forca e posicao), ate o
            // ultimo ensaio listado. Entao, ocorre quebra de linha e inicia o preenchimento da posicao 2 de cada
            // ensaio, considerando uma coluna vazia entre cada um (para facilitar compreencao do usuario)
            // Para a insercao de cada valor, para cada ensaio da lista é criado um vetor com numero de posicoes
            // igual ao numero de pontos do ensaio com maior numero de pontos. Esse vetor recebe o numero de cada
            // posicao dos ensaios. Quando o numero de pontos termina antes de acabar as posicoes do vetor, essa posicao
            // recebe "" (vazio)
            for(int i = 0; i < count; i++){
                for(Essay essayDot : selectedEssayList){    // varre os ensaios listados
                    String strArraySplitDot[] = new String[count];
                    String strArraySplitDotAux[] = essayDot.getEssayChart().split(",");    // quebra string em pontos
                    strArraySplitDot = Arrays.copyOf(strArraySplitDotAux,strArraySplitDot.length);
                    DecimalFormat decimalFormat = new DecimalFormat("0.0000"); // Formato para manter duas casas decimais

                    if(strArraySplitDot[i] != null){
                        String dot[] = strArraySplitDot[i].split(";");  // quebra em forca e posicao
                        fileWriter.append(decimalFormat.format(Double.parseDouble((dot[1]))) + ';' + decimalFormat.format(Double.parseDouble(dot[0])));
                        fileWriter.append(';'+""+';');
                    } else{
                        fileWriter.append("" + ';' + "");
                        fileWriter.append(';'+""+';');
                    }
                }
                fileWriter.append('\n');
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            fileWriter.flush();
            fileWriter.close();
        }
        System.out.println("Sucesso");
    }


}