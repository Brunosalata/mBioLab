package br.com.biopdi.mbiolabv2.controller.SceneController;

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

import java.net.URL;
import java.util.ArrayList;
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
    private LineChart<Number, Number> chartMultiLine;
    private XYChart.Series seriesMulti;
    @FXML
    private ListView<Essay> lwEssayInfo, lwSavedEssay;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        savedEssayList();

        // Permite a alteração do cenário mediante seleção de um item na ListView
        lwSavedEssay.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Essay>() {
            @Override
            public void changed(ObservableValue<? extends Essay> observable, Essay oldValue, Essay newValue) {
                try {
                    currentEssay = lwSavedEssay.getSelectionModel().getSelectedItem();
                    if (currentEssay != null) {
                        if (currentEssay.getEssayChart() == null) {
                            lwEssayInfo.getItems().clear();
                            System.out.println("Problema ao carregar os dados do gráfico! Verifique no banco de dados.");
                        }
                        lwEssayInfo.getItems().add(currentEssay);
                        addEssayChart(currentEssay.getEssayId());
                        essayInfo(currentEssay.getEssayId());
                    } else {
                        lwEssayInfo.getItems().clear();
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
     * REQUER IMPLEMENTAÇÂO >> Método que calcula os índices para retornar na tela
     *
     * @param pk
     */
    @FXML
    private void essayDataReturn(int pk) {

        //Calculos here

    }

    /**
     * Método que retorna informações do essay ao lwEssayInfo, podendo ser adicionados mais de um ensaio
     *
     * @param pk
     */
    @FXML
    private void essayInfo(int pk) {
        lwEssayInfo.getItems().clear();
        Essay essayInfo = essayDAO.findById(pk);
        lwEssayInfo.getItems().addAll(essayInfo);
    }

    /**
     * Método que retorna todos os ensaios já realizados pelo User
     */
    @FXML
    private void savedEssayList() {
        essayList.addAll(essayDAO.findAll());
        obsEssayList = FXCollections.observableList(essayList);
        lwSavedEssay.setItems(obsEssayList);
    }

    /**
     * Método que reseta o gráfico e a lista de essay analizados
     */
    @FXML
    private void multiDataReset() {
        obsEssayList.clear();
        lwEssayInfo.getItems().clear();
        chartMultiLine.getData().clear();
        savedEssayList();
    }

}
