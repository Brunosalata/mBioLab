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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import win.zqxu.jrviewer.JRViewerFX;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    private ComboBox cbUserFilter, cbEssayUsedMachineFilter, cbNormFilter;
    @FXML
    private ImageView ivEssayUser;
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
    private TilePane r = new TilePane();


    Date systemDate = new Date();
    SimpleDateFormat expDay = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat expHour = new SimpleDateFormat("HH-mm-ss");
    String currentDay = expDay.format(systemDate);
    String currentHour = expHour.format(systemDate);

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
                    // currentEssay recebe o objeto selecionado na lvSavedEssay
                    currentEssay = newValue;
                    if (currentEssay.getEssayChart() == null) {
                        System.out.println("Problema ao carregar os dados do gráfico! Verifique no banco de dados.");
                    }
                    essayChart(currentEssay.getEssayId());
                    essayInfo(currentEssay.getEssayId());
//                    reportSave();

                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
        dpEssayByDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                savedEssayByDateView(t1.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        });
        cbUserFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                savedEssayByUserView(t1.toString());
            }
        });
        cbNormFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                savedEssayByNormView(t1.toString());
            }
        });
        cbEssayUsedMachineFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                savedEssayByMachineView(t1.toString());
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
        machineListCB();
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
        List<User> userList = userDAO.findAll();
        for (User user : userList) {
            cbUserFilter.getItems().add(user.getUserLogin());
        }
    }

    /**
     * Método de listagem de normas dentro do ComboBox (cbNormFilter)
     */
    private void machineListCB() {
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
    private void normListCB() {
        List<Essay> machineList = essayDAO.findAll();
        for (Essay essay : machineList) {
            if(!cbEssayUsedMachineFilter.getItems().contains(essay.getEssayUsedMachine())){
                cbEssayUsedMachineFilter.getItems().add(essay.getEssayUsedMachine());
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
     * @param machine
     */
    @FXML
    private void savedEssayByMachineView(String machine){
        ObservableList<Essay> newObsEssayList = FXCollections.observableArrayList();
        for(Essay essay : obsEssayList){
            if(essay!=null){
                if(essay.getEssayUsedMachine().equals(machine)){
                    newObsEssayList.add(essay);
                }
            }
        }
        lvSavedEssay.setItems(newObsEssayList);
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
        lbFmax.setText(String.valueOf(currentEssay.getEssayMaxForce()));
        lbPmax.setText(String.valueOf(currentEssay.getEssayMaxPosition()));
        lbTmax.setText(String.valueOf(currentEssay.getEssayMaxTension()));
        lbTesc.setText(String.valueOf(currentEssay.getEssayEscapeTension()));
        lbAlong.setText(String.valueOf(currentEssay.getEssayAlong()));
        lbRedArea.setText(String.valueOf(currentEssay.getEssayAreaRed()));
        lbMYoung.setText(String.valueOf(currentEssay.getEssayMYoung()));

        User user = userDAO.findById(sysVar.getUserId());
        if(user.getUserImagePath()!=null){
            ivEssayUser.setImage(new Image(user.getUserImagePath()));
        } else{
            ivEssayUser.setImage(new Image(mBioLabv2Application.class.getResource("img\\darkIcon\\icons8-profile-96.png").toExternalForm()));
        }
    }

    /**
     * Método que busca um essay pelo “id” e apresenta na lwEssayInfo de forma tratada
     *
     * @param pk
     */
    @FXML
    private void essayInfo(Integer pk) {
        Essay essayInfo = essayDAO.findById(pk);
        User user = userDAO.findById(essayInfo.getUserId());
        //buscando informacoes do essayInfo
        lbFmax.setText(String.valueOf(essayInfo.getEssayMaxForce()));
        lbPmax.setText(String.valueOf(essayInfo.getEssayMaxPosition()));
        lbTmax.setText(String.valueOf(essayInfo.getEssayMaxTension()));
        lbTesc.setText(String.valueOf(essayInfo.getEssayEscapeTension()));
        lbAlong.setText(String.valueOf(essayInfo.getEssayAlong()));
        lbRedArea.setText(String.valueOf(essayInfo.getEssayAreaRed()));
        lbMYoung.setText(String.valueOf(essayInfo.getEssayMYoung()));
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

    @FXML
    private void reportSave() {

        Stage stage = (Stage) btnReportSave.getScene().getWindow();

        Platform.runLater(() ->{

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
                parameters.put("essayType", "Requer implementar código");
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
                        XYChartData.add(new ChartAxisValueToJR((Double.parseDouble(dot[i])), Double.parseDouble(dot[i + 1]),
                                "Título do Gráfico", "xAxis", "yAxis"));
                    }
                }

                JRBeanCollectionDataSource teste = new JRBeanCollectionDataSource(XYChartData);

                // Adição da lista de dados ao mapa de parâmetros
                parameters.put("xyChartData", teste);

//                // Incluindo dados no grafico do Jasper Report
//                List<Double> xData = new ArrayList<>();
//                List<Double> yData = new ArrayList<>();
//
//                // Conversao da String chartEssay para valores double
//                String strArraySplit[] = currentEssay.getEssayChart().split(",");
//                for (String str : strArraySplit) {
//                    String dot[] = str.split(";");
//                    for (int i = 0; i < dot.length; i += 2) {
//                        // Preenchimento das listas de dados
//                        xData.add(Double.parseDouble(dot[i]));
//                        yData.add(Double.parseDouble(dot[i + 1]));
//                    }
//                }
//
//                Double[] xDataArray = xData.toArray(new Double[xData.size()]);
//                Double[] yDataArray = yData.toArray(new Double[yData.size()]);
//
//                // Adição da lista de dados ao mapa de parâmetros
//                parameters.put("XData", xData);
//                parameters.put("YData", yData);




                // Preenchimento do relatorio
                JasperDesign jasperDesign = JRXmlLoader.load(new FileInputStream(new File("src/main/resources/br/com/biopdi/mbiolabv2/jrxml/essayReport.jrxml")));

                // Compilando jrxml com a classe JasperReport
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                // Gerar pdf a partir do objeto jasperReport
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

                // Chamar ferramentas jasper para expor o relatorio na janela jasperviewer

                JasperViewer jv = new JasperViewer(jasperPrint, false);
                jv.setTitle("Emissão de relatório");
                jv.setVisible(true);


            } catch (JRException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

    }

    public class ChartAxisValueToJR extends JRAbstractScriptlet{
        private Double xAxis, yAxis;
        private String chartTitle, xAxisLabel, yAxisLabel;

        public ChartAxisValueToJR() {
        }
        public ChartAxisValueToJR(Double xAxis, Double yAxis, String chartTitle, String xAxisLabel, String yAxisLabel) {
            this.xAxis = xAxis;
            this.yAxis = yAxis;
            this.chartTitle = chartTitle;
            this.xAxisLabel = xAxisLabel;
            this.yAxisLabel = yAxisLabel;
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

        public String getChartTitle() {
            return chartTitle;
        }

        public void setChartTitle(String chartTitle) {
            this.chartTitle = chartTitle;
        }

        public String getxAxisLabel() {
            return xAxisLabel;
        }

        public void setxAxisLabel(String xAxisLabel) {
            this.xAxisLabel = xAxisLabel;
        }

        public String getyAxisLabel() {
            return yAxisLabel;
        }

        public void setyAxisLabel(String yAxisLabel) {
            this.yAxisLabel = yAxisLabel;
        }

        @Override
        public void beforeReportInit() throws JRScriptletException {

        }
        @Override
        public void afterReportInit() throws JRScriptletException {

        }
        @Override
        public void beforePageInit() throws JRScriptletException {

        }
        @Override
        public void afterPageInit() throws JRScriptletException {
        }
        @Override
        public void beforeColumnInit() throws JRScriptletException {

        }
        @Override
        public void afterColumnInit() throws JRScriptletException {

        }
        @Override
        public void beforeGroupInit(String groupName) throws JRScriptletException {

        }
        @Override
        public void afterGroupInit(String groupName) throws JRScriptletException {

        }
        @Override
        public void beforeDetailEval() throws JRScriptletException {

        }
        @Override
        public void afterDetailEval() throws JRScriptletException {

        }

    }

    @FXML
    private void reportPrint() throws JRException, URISyntaxException {;

        Platform.runLater(()->{

            try {
                jvReport.printWithPrintDialog();
                jvReport.setReport((JasperPrint) JRLoader.loadObject(new File("src/main/resources/br/com/biopdi/mbiolabv2/jrxml/essayReport.jasper")));
            jvReport.print();

            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Metodo que exporta um csv do ensaio selecionado para a pasta /report na raiz do programa
     * @throws IOException
     */
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


    public class MyJRDataSource implements net.sf.jasperreports.engine.JRDataSource{
        public String getIdentification(){
            return currentEssay.getEssayIdentification();
        }
        public Double getMaxForce(){
            return currentEssay.getEssayMaxForce();
        }
        public Double getMaxPosition(){
            return currentEssay.getEssayMaxPosition();
        }
        public Double getMaxTension(){
            return currentEssay.getEssayMaxTension();
        }
        public Double getEscapeTension(){
            return currentEssay.getEssayEscapeTension();
        }
        public Double getAlong(){
            return currentEssay.getEssayMaxForce();
        }
        public Double getAreaRed(){
            return currentEssay.getEssayAreaRed();
        }
        public Double getMYoung(){
            return currentEssay.getEssayMYoung();
        }

        private Iterator<Object> iterator;
        public MyJRDataSource(List<Object> collection){
            this.iterator = collection.iterator();
        }
        @Override
        public boolean next() throws JRException {
            return iterator.hasNext();
        }
        @Override
        public Object getFieldValue(JRField jrField) throws JRException {
            Object object = iterator.next();

            // Retorne o valor do campo com base no objeto atual
            if (jrField.getName().equals("identification")) {
                return currentEssay.getEssayIdentification();
            } else if (jrField.getName().equals("maxForce")) {
                return currentEssay.getEssayMaxForce();
            } else if (jrField.getName().equals("maxPosition")){
                return currentEssay.getEssayMaxPosition();
            } else if (jrField.getName().equals("maxTension")){
                return currentEssay.getEssayMaxTension();
            } else if (jrField.getName().equals("escapeTension")){
                return currentEssay.getEssayEscapeTension();
            } else if (jrField.getName().equals("along")){
                return currentEssay.getEssayAlong();
            } else if (jrField.getName().equals("areaRed")){
                return currentEssay.getEssayAreaRed();
            } else if (jrField.getName().equals("mYoung")){
                return currentEssay.getEssayMYoung();
            }
            return null;
        }
    }
}
