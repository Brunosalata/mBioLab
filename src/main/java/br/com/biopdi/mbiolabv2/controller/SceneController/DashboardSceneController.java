package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private CategoryAxis xChartSingle;
    @FXML
    private NumberAxis yChartSingle,xChartMulti, yChartMulti;// xChartSingle,
    @FXML
    private LineChart<?,?> xyChartSingle, xyChartMulti;
    private XYChart.Series series;

    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    @FXML
    private void comparativeChartAnalisis(){
        //defining the axes
        xChartMulti.setLabel("Deslocamento");
        yChartMulti.setLabel("Força");

        xyChartMulti.setTitle("Compartivo Força x Posição");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Ensaio 1");

        series1.getData().add(new XYChart.Data(1, 23));
        series1.getData().add(new XYChart.Data(2, 14));
        series1.getData().add(new XYChart.Data(3, 15));
        series1.getData().add(new XYChart.Data(4, 24));
        series1.getData().add(new XYChart.Data(5, 34));
        series1.getData().add(new XYChart.Data(6, 36));
        series1.getData().add(new XYChart.Data(7, 22));
        series1.getData().add(new XYChart.Data(8, 45));
        series1.getData().add(new XYChart.Data(9, 43));
        series1.getData().add(new XYChart.Data(10, 17));
        series1.getData().add(new XYChart.Data(11, 29));
        series1.getData().add(new XYChart.Data(12, 25));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Ensaio 2");
        series2.getData().add(new XYChart.Data(1, 33));
        series2.getData().add(new XYChart.Data(2, 34));
        series2.getData().add(new XYChart.Data(3, 25));
        series2.getData().add(new XYChart.Data(4, 44));
        series2.getData().add(new XYChart.Data(5, 39));
        series2.getData().add(new XYChart.Data(6, 16));
        series2.getData().add(new XYChart.Data(7, 55));
        series2.getData().add(new XYChart.Data(8, 54));
        series2.getData().add(new XYChart.Data(9, 48));
        series2.getData().add(new XYChart.Data(10, 27));
        series2.getData().add(new XYChart.Data(11, 37));
        series2.getData().add(new XYChart.Data(12, 29));

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Ensaio 3");
        series3.getData().add(new XYChart.Data(1, 44));
        series3.getData().add(new XYChart.Data(2, 35));
        series3.getData().add(new XYChart.Data(3, 36));
        series3.getData().add(new XYChart.Data(4, 33));
        series3.getData().add(new XYChart.Data(5, 31));
        series3.getData().add(new XYChart.Data(6, 26));
        series3.getData().add(new XYChart.Data(7, 22));
        series3.getData().add(new XYChart.Data(8, 25));
        series3.getData().add(new XYChart.Data(9, 43));
        series3.getData().add(new XYChart.Data(10, 44));
        series3.getData().add(new XYChart.Data(11, 45));
        series3.getData().add(new XYChart.Data(12, 44));

        xyChartMulti.getData().addAll(series1, series2, series3);
    }
}
