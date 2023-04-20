package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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


        // Mostra data local na base da aplicação
        lbCurrentData.setText(String.valueOf(systemDate));
    }
}
