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
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import win.zqxu.jrviewer.JRViewerFX;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
    private Button btnEssayByUserId, btnEssaySave;
    @FXML
    private ComboBox cbUserFilter, cbEssayTypeFilter, cbNormFilter;
    @FXML
    private ImageView ivEssayUser;
    @FXML
    private DatePicker datePicker;
    @FXML
    private JRViewerFX jvReport;
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
        ivEssayUser.setClip(new Circle(15,15,15));
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
//                    reportSave();

                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
        cbUserFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                savedEssayByUserView(cbUserFilter.getSelectionModel().getSelectedItem().toString());
            }
        });
        cbNormFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                savedEssayByNormView((cbNormFilter.getSelectionModel().getSelectedItem().toString()));
            }
        });
        cbEssayTypeFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                savedEssayByTypeView((cbEssayTypeFilter.getSelectionModel().getSelectedItem().toString()));
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

    @FXML
    private void savedEssayByDateView(Date date){
        User user = new UserDAO().findByLogin(cbUserFilter.getSelectionModel().getSelectedItem().toString());
        if(sysVar.getUserId()<=2){
            essayList.addAll(essayDAO.findByUser(user.getUserId()));
        } else{
            essayList.addAll(null);
        }
        essayList.addAll(essayDAO.findByDate(String.valueOf(date)));
        obsEssayList = FXCollections.observableList(essayList);
        lvSavedEssay.setItems(obsEssayList);
    }

    @FXML
    private void savedEssayByUserView(String login){
        User user = new UserDAO().findByLogin(cbUserFilter.getSelectionModel().getSelectedItem().toString());
        if(sysVar.getUserId()<=2){
            essayList.addAll(essayDAO.findByUser(user.getUserId()));
        } else{
            essayList.addAll(null);
        }
        obsEssayList = FXCollections.observableList(essayList);
        lvSavedEssay.setItems(obsEssayList);
    }

    @FXML
    private void savedEssayByNormView(String norm){
        //essayList.addAll((Collection<? extends Essay>) userDAO.findByNorm(norm));
        obsEssayList = FXCollections.observableList(essayList);
        lvSavedEssay.setItems(obsEssayList);
    }

    @FXML
    private void savedEssayByTypeView(String type){
        //essayList.addAll((Collection<? extends Essay>) userDAO.findByType(type));
        obsEssayList = FXCollections.observableList(essayList);
        lvSavedEssay.setItems(obsEssayList);
    }

    /**
     * Método que busca um essay pelo id e apresenta na lwEssayInfo de forma tratada
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

        Platform.runLater(() ->{

            try {
//                // selecao do objeto a ser inserido (ou lista de objetos)
//                List<Essay> currentEssayList = new ArrayList<Essay>();
//                currentEssayList.add(currentEssay);
//                // Leitura do arquivo jrxml e criacao do objeto jasperdesign
//                InputStream input = new FileInputStream(new File("src/main/resources/br/com/biopdi/mbiolabv2/jrxml/essayReport.jasper"));
//                // Instancia classe de emissao de relatorio
//                ReportFX report = new ReportFX();
//
//                Platform.runLater(()->{
//                    JasperPrint jasperPrint = report.reportCreator(Collections.singletonList(currentEssayList), input);
//                    jvReport.setReport(jasperPrint);
//                    jvReport.print();
//                });

                // Converte lista para JRBeanCollectionDataSource
                JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(essayList);

                //Map para Armazenar os parametros do relatorio Jasper
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("CollectionBeanParam", itemsJRBean);
                JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/br/com/biopdi/mbiolabv2/jrxml/essayReport.jasper");
                // Compilando jrxml com a classe JasperReport
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());


                jvReport.setReport(jasperPrint);
                jvReport.print();

            } catch (JRException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @FXML
    private void reportPrint(){;

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






    public class getJasperParameter{
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
    }
}
