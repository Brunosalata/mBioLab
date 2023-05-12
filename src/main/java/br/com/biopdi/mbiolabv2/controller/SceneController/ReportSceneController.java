package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import br.com.biopdi.mbiolabv2.model.bean.Setup;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
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
import javafx.scene.control.TableView;

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
    private final SystemParameterDAO sysParDAO = new SystemParameterDAO();
    private final SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private final SystemVariable sysVar = sysVarDAO.find();

    @FXML
    private Label lbCurrentData, lbEssayUserName, lbFmax, lbPmax, lbTmax, lbTesc, lbAlong, lbRedArea, lbMYoung;
    @FXML
    private Button btnEssayByUserId;
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

    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        lbFmax.setText(String.valueOf(0.001));
        lbPmax.setText(String.valueOf(0.002));
        lbTmax.setText(String.valueOf(0.003));
        lbTesc.setText(String.valueOf(0.004));
        lbAlong.setText(String.valueOf(0.005));
        lbRedArea.setText(String.valueOf(0.006));
        lbMYoung.setText(String.valueOf(0.007));
    }

    /**
     * Método que busca um essay pelo id e apresenta na lwEssayInfo de forma tratada
     *
     * @param pk
     */
    @FXML
    private void essayInfo(Integer pk) {
        Essay essayInfo = essayDAO.findById(pk);
        //buscando informacoes do essayInfo
        //Alterar para valores salvos nos ensaios
        lbFmax.setText(String.valueOf(0.01));
        lbPmax.setText(String.valueOf(0.02));
        lbTmax.setText(String.valueOf(0.03));
        lbTesc.setText(String.valueOf(0.04));
        lbAlong.setText(String.valueOf(0.05));
        lbRedArea.setText(String.valueOf(0.06));
        lbMYoung.setText(String.valueOf(0.07));
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

}
