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

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

                try {
                    lwEssayInfo.getItems().clear();
                    currentEssay = lwSavedEssay.getSelectionModel().getSelectedItem();
                    if (currentEssay.getEssayChart() == null) {
                        System.out.println("Problema ao carregar os dados do gráfico! Verifique no banco de dados.");
                    }
                    lwEssayInfo.getItems().add(currentEssay);
                    essayChart(currentEssay.getEssayId());
                    essayInfo(currentEssay.getEssayId());
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
     * REQUER IMPLEMENTAÇÂO >> Método que calcula os índices para retornar na tela
     *
     * @param pk
     */
    @FXML
    private void essayDataReturn(int pk) {

        //Calculos here

    }

    /**
     * Método que busca o último essay registrado, bem como os parâmetros e gráfico
     */
    @FXML
    private void lastEssay() {
        lwEssayInfo.getItems().clear();
        currentEssay = essayDAO.findLastId();
        lwEssayInfo.getItems().add(currentEssay);
        essayChart(currentEssay.getEssayId());
    }

    /**
     * Método que busca um essay pelo id e apresenta na lwEssayInfo de forma tratada
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
     * Método que lista todos os ensaios salvos na lwSavedEssay
     */
    @FXML
    private void savedEssayView() {
        essayList.addAll(essayDAO.findAll());
        obsEssayList = FXCollections.observableList(essayList);
        lwSavedEssay.setItems(obsEssayList);
    }

    /**
     * Método que reseta o gráfico da essay analizada
     */
    @FXML
    private void dataReset() {
        chartSingleLine.getData().clear();
        lastEssay();
    }

}
