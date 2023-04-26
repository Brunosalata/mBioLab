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
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private final SystemVariableDAO systemVariableDAO = new SystemVariableDAO();

    @FXML
    private Label lbCurrentData;
    @FXML
    private Button btnLed, btnChartClear;
    @FXML
    private TextField txtLed;
    @FXML
    private LineChart<Number,Number> chartMultiLine;
    private XYChart.Series seriesMulti;
    @FXML
    private ListView<Essay> lwEssayInfo, lwSavedEssay;
    @FXML
    private LineChart<Number,Number> chartSingleLine;
    private XYChart.Series seriesSingle;
    private Essay currentEssay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        savedEssayList();
        lwSavedEssay.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Essay>() {
            @Override
            public void changed(ObservableValue<? extends Essay> observable, Essay oldValue, Essay newValue) {
                currentEssay = lwSavedEssay.getSelectionModel().getSelectedItem();
                lwEssayInfo.getItems().add(currentEssay);
                addEssayChart(currentEssay.getEssayId());
                essayInfo(currentEssay.getEssayId());

            }
        });

    }

    @FXML
    private void addEssayChart(int pk) {
        seriesMulti = new XYChart.Series();
        //populating the series with data

        Essay essay = essayDAO.findById(pk);
        seriesMulti.setName(essay.getEssayIdentification());
        // 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10

        String strArraySplit[] = essay.getEssayChart().split(",");
        for(String str : strArraySplit){
            String dot[] = str.split(";");
            for(int i = 0; i<dot.length;i+=2){
                System.out.println(dot[i] + " " + dot[i+1]);
                seriesMulti.getData().add(new XYChart.Data(Double.parseDouble(dot[i]),Double.parseDouble(dot[i+1])));
            }
        }
        chartMultiLine.getData().add(seriesMulti);

    }

    @FXML
    private void led(){
        addEssayChart(Integer.parseInt(txtLed.getText()));
    }

    @FXML
    private void essayDataReturn(int pk){

        //Calculos here

    }

    @FXML
    private void essayInfo(int pk){
        lwEssayInfo.getItems().clear();
        Essay essayInfo = essayDAO.findById(pk);
        lwEssayInfo.getItems().addAll(essayInfo);
    }

    @FXML
    private void savedEssayList(){
        List<Essay> essayList = essayDAO.findAll();
        lwSavedEssay.getItems().addAll(essayList);
    }

    @FXML
    private void multiDataReset(){
        lwEssayInfo.getItems().clear();
        chartMultiLine.getData().clear();
        savedEssayList();
    }

}
