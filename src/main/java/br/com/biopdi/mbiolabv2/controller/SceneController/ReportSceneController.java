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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import win.zqxu.jrviewer.JRViewerFX;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private SystemVariable sysVar = sysVarDAO.find();

    @FXML
    private Label lbCurrentData, lbEssayUserName, lbFmax, lbPmax, lbTmax, lbTesc, lbAlong, lbRedArea, lbMYoung;
    @FXML
    private Button btnEssayByUserId, btnEssaySave, btnReportSave, btnCsvExport, btnReportPrint;
    @FXML
    private ComboBox cbUserFilter, cbEssayTypeFilter, cbNormFilter;
    @FXML
    private ImageView ivEssayUser, ivPreviewReport;
    @FXML
    private DatePicker dpEssayByDate;
    @FXML
    private JRViewerFX jvReport = new JRViewerFX();
    @FXML
    private AnchorPane apEssayFilterOption;
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


    Date systemDate = new Date();
    SimpleDateFormat expDay = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat expHour = new SimpleDateFormat("HH-mm-ss");
    String currentDay = expDay.format(systemDate);
    String currentHour = expHour.format(systemDate);

    /**
     * Metodo chamado ao iniciar a scene
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialSetup();
        lastEssay();
        savedEssayView(sysVar.getUserId());

        lvSavedEssay.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Essay>() {
            @FXML
            @Override
            public void changed(ObservableValue<? extends Essay> observable, Essay oldValue, Essay newValue) {

                try {
                    if(newValue!=null){
                        // currentEssay recebe o objeto selecionado na lvSavedEssay
                        currentEssay = newValue;
                        if (currentEssay.getEssayChart() == null) {
                            System.out.println("Problema ao carregar os dados do gráfico! Verifique no banco de dados.");
                        }
                        essayChart(currentEssay.getEssayId());
                        essayInfo(currentEssay.getEssayId());
                    } else{
                        System.out.println("newValue = null");
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });

        // Listener no DatePicker, que filtra a lista baseado na data selecionada, alem de deselecionar os comboBox
        dpEssayByDate.setOnAction(event -> {
                if(dpEssayByDate.getValue()!=null){
                    savedEssayByDateView(dpEssayByDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    cbUserFilter.getSelectionModel().clearSelection();
                    cbNormFilter.getSelectionModel().clearSelection();
                    cbEssayTypeFilter.getSelectionModel().clearSelection();
                }
        });
        // Listener nos ComboBox, que filtram a lista baseado no parâmetro selecionado, alem de deselecionar os demais elementos de busca
        cbUserFilter.setOnAction(event -> {
                if(cbUserFilter.getSelectionModel().getSelectedItem()!=null){
                    savedEssayByUserView(cbUserFilter.getSelectionModel().getSelectedItem().toString());
                    dpEssayByDate.setValue(null);
                    cbNormFilter.getSelectionModel().select(null);
                    cbEssayTypeFilter.getSelectionModel().select(null);
                }
        });
        cbNormFilter.setOnAction(event -> {
                if(cbNormFilter.getSelectionModel().getSelectedItem()!=null){
                    savedEssayByNormView(cbNormFilter.getSelectionModel().getSelectedItem().toString());
                    dpEssayByDate.setValue(null);
                    cbUserFilter.getSelectionModel().select(null);
                    cbEssayTypeFilter.getSelectionModel().select(null);
                }
        });
        cbEssayTypeFilter.setOnAction(event -> {
                if(cbEssayTypeFilter.getSelectionModel().getSelectedItem()!=null){
                    savedEssayByEssayTypeView(cbEssayTypeFilter.getSelectionModel().getSelectedItem().toString());
                    dpEssayByDate.setValue(null);
                    cbUserFilter.getSelectionModel().select(null);
                    cbNormFilter.getSelectionModel().select(null);
                }
        });

    }

    /**
     * Configuracoes iniciais quando o fxml e chamado
     */
    private void initialSetup() {
        ivEssayUser.setClip(new Circle(15,15,15));
        userListCB();
        normListCB();
        essayTypeListCB();
        if(sysVar.getUserId()>3){
            cbUserFilter.setVisible(false);
        } else if(sysVar.getUserId()==3){
            apEssayFilterOption.setVisible(false);
        }
    }

    /**
     * Método que lista todos os usuarioes da tb_user do banco de dados
     */
    @FXML
    private void userListCB() {
        cbUserFilter.getItems().clear();
        List<User> userList = userDAO.findAll();
        for (User user : userList) {
            cbUserFilter.getItems().add(user.getUserLogin());
        }
    }

    /**
     * Método de listagem de normas dentro do ComboBox (cbNormFilter)
     */
    private void normListCB() {
        List<Essay> normList = essayDAO.findAll();
        for (Essay essay : normList) {
            if(!cbNormFilter.getItems().contains(essay.getEssayNorm())){
                cbNormFilter.getItems().add(essay.getEssayNorm());
            }
        }
    }

    /**
     * Método de listagem de normas dentro do ComboBox (cbNormFilter)
     */
    private void essayTypeListCB() {
        cbEssayTypeFilter.getItems().clear();
        List<Essay> typeList = essayDAO.findAll();
        for (Essay essay : typeList) {
            if(!cbEssayTypeFilter.getItems().contains(essay.getEssayType())){
                cbEssayTypeFilter.getItems().add(essay.getEssayType());
            }
        }
    }

    /**
     * Metodo que lista ensaios salvos baseado na data informada
     * @param day
     */
    @FXML
    private void savedEssayByDateView(String day){
        ObservableList<Essay> newObsEssayList = FXCollections.observableArrayList();
        for(Essay essay : obsEssayList){
            if(essay!=null){
                if(essay.getEssayDay().equals(day)){
                    newObsEssayList.add(essay);
                }
            }
        }
        lvSavedEssay.setItems(newObsEssayList);

    }

    /**
     * Metodo que lista ensaios salvos baseado na data informada
     * @param essayUser
     */
    @FXML
    private void savedEssayByUserView(String essayUser){
        User user = userDAO.findByLogin(essayUser);
        ObservableList<Essay> newObsEssayList = obsEssayList.filtered(x -> x.getUserId() == user.getUserId());
        lvSavedEssay.setItems(newObsEssayList);
    }

    /**
     * Metodo que lista ensaios salvos baseado na norma informada
     * @param norm
     */
    @FXML
    private void savedEssayByNormView(String norm){
        ObservableList<Essay> newObsEssayList = FXCollections.observableArrayList();
        for(Essay essay : obsEssayList){
            if(essay!=null){
                if(essay.getEssayNorm().equals(norm)){
                    newObsEssayList.add(essay);
                }
            }
        }
        lvSavedEssay.setItems(newObsEssayList);
    }

    /**
     * Metodo que lista ensaios salvos baseado no equipamento informada
     * @param type
     */
    @FXML
    private void savedEssayByEssayTypeView(String type){
        ObservableList<Essay> newObsEssayList = FXCollections.observableArrayList();
        for(Essay essay : obsEssayList){
            if(essay!=null){
                if(essay.getEssayType().equals(type)){
                    newObsEssayList.add(essay);
                }
            }
        }
        lvSavedEssay.setItems(newObsEssayList);
    }

    /**
     * Método que busca a stringChart do DB, converte em pontos e plota na area de gráfico na interface
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
                seriesSingle.getData().add(new XYChart.Data(Double.parseDouble(dot[i + 1]), Double.parseDouble(dot[i])));
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
        essayInfo(currentEssay.getEssayId());
//        lbFmax.setText(String.valueOf(currentEssay.getEssayMaxForce()));
//        lbPmax.setText(String.valueOf(currentEssay.getEssayMaxPosition()));
//        lbTmax.setText(String.valueOf(currentEssay.getEssayMaxTension()));
//        lbTesc.setText(String.valueOf(currentEssay.getEssayEscapeTension()));
//        lbAlong.setText(String.valueOf(currentEssay.getEssayAlong()));
//        lbRedArea.setText(String.valueOf(currentEssay.getEssayAreaRed()));
//        lbMYoung.setText(String.valueOf(currentEssay.getEssayMYoung()));
//
//        User user = userDAO.findById(sysVar.getUserId());
//        if(user.getUserImagePath()!=null){
//            ivEssayUser.setImage(new Image(user.getUserImagePath()));
//        } else{
//            ivEssayUser.setImage(new Image(mBioLabv2Application.class.getResource("img\\darkIcon\\icons8-profile-96.png").toExternalForm()));
//        }
    }

    /**
     * Método que busca um essay pelo “id” e apresenta na lwEssayInfo de forma tratada
     *
     * @param pk
     */
    @FXML
    private void essayInfo(Integer pk) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        DecimalFormat percentageFormat = new DecimalFormat("0.00");
        Essay essayInfo = essayDAO.findById(pk);
        User user = userDAO.findById(essayInfo.getUserId());
        //buscando informacoes do essayInfo
        lbFmax.setText(String.valueOf(decimalFormat.format(essayInfo.getEssayMaxForce())));
        lbPmax.setText(String.valueOf(decimalFormat.format(essayInfo.getEssayMaxPosition())));
        lbTmax.setText(String.valueOf(decimalFormat.format(essayInfo.getEssayMaxTension())));
        lbTesc.setText(String.valueOf(decimalFormat.format(essayInfo.getEssayEscapeTension())));
        lbAlong.setText(String.valueOf(percentageFormat.format(essayInfo.getEssayAlong())));
        lbRedArea.setText(String.valueOf(percentageFormat.format(essayInfo.getEssayAreaRed())));
        lbMYoung.setText(String.valueOf(decimalFormat.format(essayInfo.getEssayMYoung())));
        if(user.getUserImagePath()!=null){
            ivEssayUser.setImage(new Image("file:\\" + user.getUserImagePath()));
        } else{
            ivEssayUser.setImage(new Image(mBioLabv2Application.class.getResource("img\\darkIcon\\icons8-profile-96.png").toExternalForm()));
        }
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

    /**
     * Metodo que cria janela Jasper com o preenchimento do jrxml, permitindo save e export para diferentes formatos
     */
    @FXML
    private void reportSave() {

        Stage stage = (Stage) btnReportSave.getScene().getWindow();
        SwingNode swingNode = new SwingNode();
        StackPane stackPane = new StackPane(swingNode);

        try{
            List<Essay> reportEssayList = new ArrayList<>();
            reportEssayList.add(currentEssay);

            // Converte lista para JRBeanCollectionDataSource
            JRBeanCollectionDataSource essayCollection = new JRBeanCollectionDataSource(reportEssayList);

            // Map para Armazenar os parametros do relatorio Jasper
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBeanParam", essayCollection);
            // Adicao de novos parametros para preenchimento de campos do relatorio
            parameters.put("introduction", "Parâmetro enviado com sucesso!");
            parameters.put("essayIdentification", currentEssay.getEssayIdentification());
            parameters.put("essayUsedMachine", currentEssay.getEssayUsedMachine());
            parameters.put("essayNorm", currentEssay.getEssayNorm());
            parameters.put("chargeCell", currentEssay.getEssayChargeCell());
            parameters.put("essayVelocity", currentEssay.getEssayDislocationVelocity());
            parameters.put("velocityUnit", "mm/min");
            parameters.put("essayType", currentEssay.getEssayType());
            parameters.put("essayDay", currentEssay.getEssayDay());
            parameters.put("essayHour", currentEssay.getEssayHour());
            parameters.put("essayPreCharge", currentEssay.getEssayPreCharge());
            parameters.put("essayTemperature", currentEssay.getEssayTemperature());
            parameters.put("essayRelativeHumidity", currentEssay.getEssayRelativeHumidity());
            parameters.put("chartTitle", "Título");
            parameters.put("xAxisLabel", "Eixo X");
            parameters.put("yAxisLabel", "Eixo Y");
            User user = userDAO.findById(currentEssay.getUserId());
            parameters.put("author", user.getUserName());


            // Incluindo dados no grafico do Jasper Report
            List<ChartAxisValueToJR> XYChartData = new ArrayList<ChartAxisValueToJR>();

            // Conversao da String chartEssay para valores double
            String strArraySplit[] = currentEssay.getEssayChart().split(",");
            for (String str : strArraySplit) {
                String dot[] = str.split(";");
                for (int i = 0; i < dot.length; i += 2) {
                    // Preenchimento das listas de dados
                    XYChartData.add(new ChartAxisValueToJR(Double.parseDouble(dot[i + 1]), Double.parseDouble(dot[i]),
                            "série 1"));
                }
            }

            JRBeanCollectionDataSource xyChartDataJR = new JRBeanCollectionDataSource(XYChartData);

            // Adição da lista de dados ao mapa de parâmetros
            parameters.put("xyChartData", xyChartDataJR);

            // Preenchimento do relatorio
            JasperDesign jasperDesign = JRXmlLoader.load(new FileInputStream(new File("src/main/resources/br/com/biopdi/mbiolabv2/jrxml/essayReportV2.jrxml")));

            // Compilando jrxml com a classe JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // Gerar pdf a partir do objeto jasperReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            // Chamar ferramentas jasper para expor o relatorio na janela jasperviewer
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
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
     * Metodo que exporta um csv do ensaio selecionado para a pasta /report na raiz do programa
     * @throws IOException
     */
    @FXML
    private void csvExport() throws IOException {

        // Criar um seletor de arquivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar CSV"); // Título da janela de seleção
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos CSV", "*.csv")); // Filtro de extensão para exibir apenas arquivos CSV

        // Abrir a janela de seleção de arquivo
        File selectedFile = fileChooser.showSaveDialog((Stage) btnReportSave.getScene().getWindow());

        if (selectedFile != null) {
            OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(selectedFile), StandardCharsets.ISO_8859_1);
            try {
                // Definição do header da planilha
                fileWriter.append("Posição");
                fileWriter.append(';');
                fileWriter.append("Força");
                fileWriter.append('\n');

                Essay essay = essayDAO.findById(currentEssay.getEssayId());
                String strArraySplit[] = essay.getEssayChart().split(",");
                DecimalFormat decimalFormat = new DecimalFormat("0.0000"); // Formato para manter duas casas decimais
                for (String str : strArraySplit) {
                    String dot[] = str.split(";");
                    for (int i = 0; i < dot.length; i += 2) {
                        System.out.println(dot[i] + " " + dot[i + 1]);
                        fileWriter.append(decimalFormat.format(Double.parseDouble(dot[i + 1])));
                        fileWriter.append(';');
                        fileWriter.append(decimalFormat.format(Double.parseDouble(dot[i])));
                        fileWriter.append('\n');
                    }
                }
                System.out.println("Arquivo CSV exportado com sucesso!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                fileWriter.flush();
                fileWriter.close();
            }
        }
    }

}
