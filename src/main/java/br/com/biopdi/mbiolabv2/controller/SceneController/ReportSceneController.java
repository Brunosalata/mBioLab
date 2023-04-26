package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ReportSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private final SystemVariableDAO systemVariableDAO = new SystemVariableDAO();

    @FXML
    private Label lbCurrentData;
    @FXML
    private Button btnEssayByUserId;
    @FXML
    private ListView<Essay> lwEssayInfo, lwSavedEssay;
    @FXML
    private LineChart<Number,Number> chartSingleLine;
    private XYChart.Series seriesSingle;
    private Essay currentEssay;

    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lastEssay();

        savedEssayView();
        lwSavedEssay.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Essay>() {
            @Override
            public void changed(ObservableValue<? extends Essay> observable, Essay oldValue, Essay newValue) {
                currentEssay = lwSavedEssay.getSelectionModel().getSelectedItem();
                lwEssayInfo.getItems().add(currentEssay);
                essayChart(currentEssay.getEssayId());
                essayInfo(currentEssay.getEssayId());

            }
        });

    }

    @FXML
    private void essayChart(int pk) {
        chartSingleLine.getData().clear();
        XYChart.Series seriesSingle = new XYChart.Series();
        //populating the series with data

        Essay essay = essayDAO.findById(pk);
        seriesSingle.setName(essay.getEssayIdentification());
        // 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10

        String strArraySplit[] = essay.getEssayChart().split(",");
        for(String str : strArraySplit){
            String dot[] = str.split(";");
            for(int i = 0; i<dot.length;i+=2){
                System.out.println(dot[i] + " " + dot[i+1]);
                seriesSingle.getData().add(new XYChart.Data(Double.parseDouble(dot[i]),Double.parseDouble(dot[i+1])));
            }
        }
        chartSingleLine.getData().add(seriesSingle);
    }

    @FXML
    private void essayDataReturn(int pk){

        //Calculos here

    }

    @FXML
    private void lastEssay(){
        lwEssayInfo.getItems().clear();
        currentEssay = essayDAO.findLastId();
        lwEssayInfo.getItems().add(currentEssay);
        essayChart(currentEssay.getEssayId());
    }
    @FXML
    private void essayInfo(int pk){
        lwEssayInfo.getItems().clear();
        Essay essayInfo = essayDAO.findById(pk);
        lwEssayInfo.getItems().addAll(essayInfo);
    }

    @FXML
    private void savedEssayView(){
        List<Essay> essayList = essayDAO.findAll();
        lwSavedEssay.getItems().addAll(essayList);
    }

    @FXML
    private void dataReset(){
        chartSingleLine.getData().clear();
        lastEssay();
        savedEssayView();
    }


}
