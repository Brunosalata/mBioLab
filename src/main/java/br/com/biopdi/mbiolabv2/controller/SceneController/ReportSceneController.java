package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private Button btnEssayByUserId, btnSwitchToHomeScene, btnSwitchToDashboardScene, btnSwitchToEssayScene, btnSwitchToReportScene, btnSwitchToSettingScene;
    @FXML
    private ListView lwLastEssay, lwSavedEssay;
    @FXML
    private LineChart<Number,Number> chartSingleLine;
    private XYChart.Series seriesSingle;

    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    @FXML
    private void essayChart(int pk) {
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
    private void selectEssayMouseClick(){
        Essay essay = essayDAO.findById(lwSavedEssay.getSelectionModel().getSelectedIndex());
        essayChart(essay.getEssayId());

    }

    @FXML
    private void chartClear(){
        chartSingleLine.getData().clear();
    }


}
