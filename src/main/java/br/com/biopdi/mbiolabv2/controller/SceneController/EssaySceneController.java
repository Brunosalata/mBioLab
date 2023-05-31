package br.com.biopdi.mbiolabv2.controller.SceneController;


import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.controller.serial.SerialConnection;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import br.com.biopdi.mbiolabv2.model.bean.Method;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class EssaySceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final MethodDAO methodDAO = new MethodDAO();
    private final SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private final SystemVariableDAO systemVariableDAO = new SystemVariableDAO();
    private SystemVariable sysVar = systemVariableDAO.find();
    private SerialConnection serialConn = new SerialConnection();
    private Essay essayFinalyzed;


    // Puxar dados do DB systemVariable salvas na Thread de leitura FPReadingThread()
    @FXML
    private ComboBox cbMethodList, cbNormList, cbEssayType, cbForceUnitSelection, cbPositionUnitSelection,
            cbExtensometer1, cbExtensometer2;
    @FXML
    private LineChart<Number, Number> chartEssayLine;
    @FXML
    private NumberAxis xAxis = new NumberAxis();
    @FXML
    private NumberAxis yAxis = new NumberAxis();
    private XYChart.Series<Number, Number> series;
    @FXML
    private Label lbFMax, lbPMax, lbTMax, lbTEsc, lbAlong, lbRedArea, lbMYoung, lbEssayTemperature,
            lbEssayRelativeHumidity;
    @FXML
    private TextField txtConnected, txtForceView, txtPositionView, txtEssayVelocity, txtAdjustVelocity,
            txtEssayIdentification, txtEssayNorm, txtUsedMachine, txtEssayChargeCell, txtInitialForce,
            txtFinalForce, txtInitialPosition, txtFinalPosition, txtDislocationVelocity, txtEssayPreCharge,
            txtMaxForceBreak, txtDislocationValueBreak, txtDislocationValuePause, txtForcePercentageBreak,
            txtMethodName, txtOffsetIntersectionLine, txtGainIntersectionLine, txtSpecimenCrossSectionCalcule,
            txtSpecimenCrossSectionArea, txtSpecimenCrossSectionLength, txtSpecimenAValueRectangle,
            txtSpecimenBValueRectangle, txtSpecimenAValueCylinder, txtSpecimenBValueCylinder, txtSpecimenValueTubular,
            txtPercentObtainedForce, txtObtainedForce;
    @FXML
    private Button btnPositionUp, btnPositionDown, btnStart, btnPause, btnStop, btnChargeMethod,
            btnEssayByUserId, btnEssaySave, btnEssayDiscart, btnForceZero, btnSaveMethod;
    @FXML
    private RadioButton rbForceXPosition, rbStrainXDeform, rbForceDownBreak, rbMaxForceBreak, rbDislocationBreak,
            rbDislocationPause, rbRectangle, rbCylinder, rbTubular;
    @FXML
    private Slider shAdjustVelocity;
    @FXML
    private TabPane tpEssayFlow;

    private Double currentBaseForce = 0D, taredCurrentForce = 0D, currentNewtonForce = 0D, currentKgForce = 0D,
            forceTare = 0D, currentBasePosition = 0D, currentMmPosition = 0D, currentPolPosition = 0D,
            referencePosition = 0D, initialForce, finalForce, initialPosition, finalPosition, fMax, pMax, tMax, tEsc,
            along, redArea, mYoung, nForceConversionFactor = 1D, kgForceConversionFactor = 1D,
            mmPositionConversionFactor = 1D, inPositionConversionFactor = 1D;
    private String chartString = null, currentForceUnit = "N", currentPositionUnit = "mm";
    private Boolean moving = false, errorEmergencyButton = false, errorInfLimit = false, errorSupLimit = false,
            errorChargeCellLimit = false, errorChargeCellDisconnected = false;
    private Integer forceAdjustInversion = 1, positionAdjustInversion = 1, forceAdjustInversionView = 1,
            positionAdjustInversionView = 1, currentChargeCell = 0, errorDecValue = 0;
    @FXML
    private VBox vBoxEssayStart, vbRectangleSpecimen, vbCylinderSpecimen, vbTubularSpecimen;


    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDay = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat brasilianHour = new SimpleDateFormat("HH:mm");
    String currentDay = brasilianDay.format(systemDate);
    String currentHour = brasilianHour.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        normList();
        essayTypeList();
        savedMethodList();
        extensometer1List();
        extensometer2List();
        forceUnitSelection();
        positionUnitSelection();
        autoConnect();
        xyAxisAdjust();
        newEssay();

        // Altera opcao de unidade de forca e os rotulos do grafico do ensaio
        cbForceUnitSelection.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                try{
                    currentForceUnit = cbForceUnitSelection.getSelectionModel().getSelectedItem().toString();
                    xyAxisAdjust();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // Altera opcao de unidade de deslocamento e os rotulos do grafico do ensaio
        cbPositionUnitSelection.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                try{
                    currentPositionUnit = cbPositionUnitSelection.getSelectionModel().getSelectedItem().toString();
                    xyAxisAdjust();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // Altera valores de referencia (adjustInvestion) se o ensaio for para cima ou para baixo
        cbEssayType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                essayType();
            }
        });
        // Atualizacao do valor no txtField de adjustVelocity em funcao da mudanca no slider
        shAdjustVelocity.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                txtAdjustVelocity.setText(String.format("%.0f",shAdjustVelocity.getValue()));
            }
        });
        // Evento que chama medoto que define velocidade de ajuste assim o usuario solta o slider
        shAdjustVelocity.setOnMouseReleased(event -> {
            Platform.runLater(()->{
                adjustVelocity(Integer.valueOf(txtAdjustVelocity.getText()));
            });
        });

        // Atualiza valor de area calculada para o corpo de amostra retangular
        txtSpecimenAValueRectangle.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Platform.runLater(()->{
                    txtSpecimenCrossSectionCalcule.setText("0.0000");
                    if(Double.parseDouble(txtSpecimenAValueRectangle.getText())!=0 &&
                            Double.parseDouble(txtSpecimenBValueRectangle.getText())!=0){
                        txtSpecimenCrossSectionCalcule.setText(String.format("%.4f",Double.parseDouble(txtSpecimenAValueRectangle.getText()) *
                                Double.parseDouble(txtSpecimenBValueRectangle.getText())));
                    }
                });
            }
        });
        txtSpecimenBValueRectangle.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Platform.runLater(()->{
                    txtSpecimenCrossSectionCalcule.setText("0.0000");
                    if(Double.parseDouble(txtSpecimenAValueRectangle.getText())!=0 &&
                            Double.parseDouble(txtSpecimenBValueRectangle.getText())!=0){
                        txtSpecimenCrossSectionCalcule.setText(String.format("%.4f",Double.parseDouble(txtSpecimenAValueRectangle.getText()) *
                                Double.parseDouble(txtSpecimenBValueRectangle.getText())));
                    }
                });
            }
        });

        // Atualiza valor de area calculada para o corpo de amostra cilindrica
        txtSpecimenAValueCylinder.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Platform.runLater(()->{
                    txtSpecimenCrossSectionCalcule.setText("0.0000");
                    if(Double.parseDouble(txtSpecimenAValueCylinder.getText())!=0 &&
                            Double.parseDouble(txtSpecimenBValueCylinder.getText())!=0){
                        txtSpecimenCrossSectionCalcule.setText(String.format("%.4f",Double.parseDouble(txtSpecimenAValueCylinder.getText()) *
                                Double.parseDouble(txtSpecimenBValueCylinder.getText())));
                    }
                });
            }
        });
        txtSpecimenBValueCylinder.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Platform.runLater(()->{
                    txtSpecimenCrossSectionCalcule.setText("0.0000");
                    if(Double.parseDouble(txtSpecimenAValueCylinder.getText())!=0 &&
                            Double.parseDouble(txtSpecimenBValueCylinder.getText())!=0){
                        txtSpecimenCrossSectionCalcule.setText(String.format("%.4f",Double.parseDouble(txtSpecimenAValueCylinder.getText()) *
                                Double.parseDouble(txtSpecimenBValueCylinder.getText())));
                    }
                });
            }
        });

        // Atualiza valor de area calculada para o corpo de amostra tubular
        txtSpecimenValueTubular.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Platform.runLater(()->{
                        txtSpecimenCrossSectionCalcule.setText("0.0000");
                    if(Double.parseDouble(txtSpecimenAValueCylinder.getText())==0){
                        txtSpecimenCrossSectionCalcule.setText(String.format("%.4f",Math.pow(Double.parseDouble(txtSpecimenValueTubular.getText()),2)));
                    }
                });
            }
        });

        // Atualiza valor da textField Area da amostra (central), espelhada do calculo da area, mas que permite edicao
        txtSpecimenCrossSectionCalcule.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Platform.runLater(()->{
                    txtSpecimenCrossSectionArea.setText(txtSpecimenCrossSectionCalcule.getText());
                });
            }
        });

        // Desabilitar campos de edicao em amostras retangulares se a opcao não estiver selecionada
        rbRectangle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    txtSpecimenAValueRectangle.setText("0.0000");
                    txtSpecimenBValueRectangle.setText("0.0000");
                if(rbRectangle.isSelected()){
                    txtSpecimenAValueRectangle.setDisable(false);
                    txtSpecimenBValueRectangle.setDisable(false);
                } else{
                    txtSpecimenAValueRectangle.setDisable(true);
                    txtSpecimenBValueRectangle.setDisable(true);
                }
            }
        });
        // Desabilitar campos de edicao em amostras cilindricas se a opcao não estiver selecionada
        rbCylinder.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    txtSpecimenAValueCylinder.setText("0.0000");
                    txtSpecimenBValueCylinder.setText("0.0000");
                if(rbCylinder.isSelected()){
                    txtSpecimenAValueCylinder.setDisable(false);
                    txtSpecimenBValueCylinder.setDisable(false);
                } else{
                    txtSpecimenAValueCylinder.setDisable(true);
                    txtSpecimenBValueCylinder.setDisable(true);
                }
            }
        });
        // Desabilitar campos de edicao em amostras cilindricas se a opcao não estiver selecionada
        rbTubular.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    txtSpecimenValueTubular.setText("0.0000");
                if(rbTubular.isSelected()){
                    txtSpecimenValueTubular.setDisable(false);
                } else{
                    txtSpecimenValueTubular.setDisable(true);
                }
            }
        });

        // Desabilitar campos de edicao para parada por queda de forca se a opcao não estiver selecionada
        rbForceDownBreak.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    txtForcePercentageBreak.setText("20");
                    txtPercentObtainedForce.setText("0.00");
                    txtObtainedForce.setText("0.00");
                if(rbForceDownBreak.isSelected()){
                    txtForcePercentageBreak.setDisable(false);
                    txtPercentObtainedForce.setDisable(false);
                    txtObtainedForce.setDisable(false);
                } else{                                             // AJUSTAR E FAZER OS DEMAIS E DESABILITAR NO SCENE BUILDER
                    txtForcePercentageBreak.setDisable(true);
                    txtPercentObtainedForce.setDisable(true);
                    txtObtainedForce.setDisable(true);
                }
            }
        });
        // Desabilitar campos de edicao para parada por forca maxima se a opcao não estiver selecionada
        rbMaxForceBreak.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                txtMaxForceBreak.setText("0.00");
                if(rbMaxForceBreak.isSelected()){
                    txtMaxForceBreak.setDisable(false);
                } else{                                             // AJUSTAR E FAZER OS DEMAIS E DESABILITAR NO SCENE BUILDER
                    txtMaxForceBreak.setDisable(true);
                }
            }
        });
        // Desabilitar campos de edicao para parada por deslocamento maximo se a opcao não estiver selecionada
        rbDislocationBreak.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                txtDislocationValueBreak.setText("0.00");
                if(rbDislocationBreak.isSelected()){
                    txtDislocationValueBreak.setDisable(false);
                } else{                                             // AJUSTAR E FAZER OS DEMAIS E DESABILITAR NO SCENE BUILDER
                    txtDislocationValueBreak.setDisable(true);
                }
            }
        });
        // Desabilitar campos de edicao para pause por deslocamento se a opcao não estiver selecionada
        rbDislocationPause.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                txtDislocationValuePause.setText("0.00");
                if(rbDislocationPause.isSelected()){
                    txtDislocationValuePause.setDisable(false);
                } else{                                             // AJUSTAR E FAZER OS DEMAIS E DESABILITAR NO SCENE BUILDER
                    txtDislocationValuePause.setDisable(true);
                }
            }
        });

    }


    /**
     * Classe que define o algorítmo da Thread que faz a leitura dinâmica da Força e da Posição
     */
    class ForcePositionReader implements Runnable {

        @FXML
        @Override
        public synchronized void run() {

            try {
                Thread.sleep(1000);
                while (true) {
                    // Obtendo valor analogico da forca
                    serialConn.forceRequest();  // Requerimento do valor da força
                    Thread.sleep(0);
                    currentBaseForce = Double.valueOf(serialConn.inputValue());
                    taredCurrentForce = currentBaseForce - forceTare;
                    currentNewtonForce = taredCurrentForce * nForceConversionFactor;
                    currentKgForce = taredCurrentForce * kgForceConversionFactor;
                    // Obtendo valor analogico da posicao
                    serialConn.positionRequest();  // requerimento do valor da posição
                    Thread.sleep(0);
                    currentBasePosition = Double.valueOf(serialConn.inputValue());
                    currentMmPosition = currentBasePosition * mmPositionConversionFactor;
                    currentPolPosition = currentBasePosition * inPositionConversionFactor;
                    // Obtendo informacao sobre qual celula de carga esta conectada (reflete no indice multiplicador
                    // de conversao de unidade de forca)
                    serialConn.chargeCellRequest();
                    Thread.sleep(0);
                    currentChargeCell = Integer.valueOf(serialConn.inputValue());
                    // Leitura do sinal de erro
                    serialConn.errorRead();
                    Thread.sleep(0);
                    errorDecValue = Integer.valueOf(serialConn.inputValue());

                    // Definicao do fator multiplicativo para conversao de unidade de forca em funcao da celula de carga
                    unitConvertion(currentChargeCell);

                    // Monitoramento do erro em tempo real e resposta imediata na interface
                    Platform.runLater(()->{
                        errorIdentification();
                    });

                    // Atualização da UI pela Thread a partir das variáveis globais
                    Platform.runLater(() -> {
                        txtForceView.setText(String.format("%.2f", selectedUnitForceValue() * forceAdjustInversionView));
                        txtPositionView.setText(String.format("%.2f", selectedUnitPositionValue() * positionAdjustInversionView));
                    });
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Metodo que recebe valor de celula de carga e define fator multiplicativo para conversor de unidade
     */
    private void unitConvertion(Integer chargeCell){
        switch(chargeCell){

            //nForceConversionFactor
            //kgForceConversionFactor
            //mmPositionConversionFactor
            //inPositionConversionFactor

            case 0: ;
            case 1: ;
            case 2: ;
            case 3: ;
            case 4: ;
            case 5: ;
            case 6: ;
            case 7: ;
            case 8: ;
            case 9: ;
            case 10: ;
            case 11: ;
            case 12: ;
            case 13: ;
            case 14: ;
            case 15: ;
        }
    }

    /**
     * Metodo para conversao de decimal em binario e interpretacao do sinal de erro em tempo de execucao
     */
    private void errorIdentification(){
        String binary = Integer.toBinaryString(errorDecValue);
        Character[] splitBin = new Character[5];
        for(int i =0; i < splitBin.length; i++){
            splitBin[i] = binary.charAt(i);
        }
        // Erro - Botaode emergencia
        if(splitBin[0]==1){
            errorEmergencyButton = true;
            Platform.runLater(()->{

            });
        } else if(splitBin[0]==0){
            errorEmergencyButton = false;
            Platform.runLater(()->{

            });
        }
        // Erro - Fim do curso inferior
        if(splitBin[1]==1){
            errorInfLimit = true;
            Platform.runLater(()->{

            });
        } else if(splitBin[1]==0){
            errorInfLimit = false;
            Platform.runLater(()->{

            });
        }
        // Erro - Fim do curso superior
        if(splitBin[2]==1){
            errorSupLimit = true;
            Platform.runLater(()->{

            });
        } else if(splitBin[2]==0){
            errorSupLimit = false;
            Platform.runLater(()->{

            });
        }
        // Erro - Limite de celula de carga
        if(splitBin[3]==1){
            errorChargeCellLimit = true;
            Platform.runLater(()->{

            });
        } else if(splitBin[3]==0){
            errorChargeCellLimit = false;
            Platform.runLater(()->{

            });
        }
        // Erro - Celula de carga desconectada
        if(splitBin[4]==1){
            errorChargeCellDisconnected = true;
            Platform.runLater(()->{

            });
        } else if(splitBin[4]==0){
            errorChargeCellDisconnected = false;
            Platform.runLater(()->{

            });
        }

    }

    /**
     * Método de conexão automática, buscando o portName do systemSetting no DB
     */
    @FXML
    private synchronized void autoConnect() {

//        // Instance to get an object SystemParameter from tb_systemParameter (single line)
//        SystemParameter sysPar = systemParameterDAO.find();
//        // portName receives portName from systemParameterDAO
//        port = SerialPort.getCommPort(sysPar.getPortName());
//        System.out.println("Conectado à porta: " + sysPar.getPortName() + " - " + port);
//        port.openPort();
//        System.out.println("Opening port");
        serialConn.openPort();
        if (serialConn.isOpen()) {
            txtConnected.setText("Conectado");
            txtConnected.setStyle("-fx-background-color: #06BC0E");
//            port.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
//            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 10, 10);
//            port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

            // Thread to update the Força and Posição textLabel from GUI
            Thread FPReader = new Thread(new ForcePositionReader());
            FPReader.start();

        } else {
            serialConn.closePort();
            txtConnected.setText("Desconectado");
            txtConnected.setStyle("-fx-background-color: #BCAA06");
        }
    }

    /**
     * Metodo que configura fator multiplicados para calculo da forca e posicao na respectiva unidade
     */
    private void convertionDefinition(){


    }

    /**
     * Método que lista todos os methodName salvos na tb_method do banco de dados
     */
    @FXML
    private void savedMethodList() {
        List<Method> methodList = methodDAO.findAll();
        for (Method method : methodList) {
            cbMethodList.getItems().add(method.getMethodName());
        }
    }

    /**
     * Método que carrega um método já salvo a partir de uma comboBox
     *
     * @return method
     */
    @FXML
    private void chargeMethod() {
        Method method = new MethodDAO().findByMethod(cbMethodList.getSelectionModel().getSelectedItem().toString());
        System.out.println(method);
        // Settar valores nos TextFields de configuracao
        // Settar opcao de parada automatica
        switch(method.getAutoBreakIndex()){
            case 0: rbForceDownBreak.isSelected();
            case 1: rbMaxForceBreak.isSelected();
            case 2: rbDislocationBreak.isSelected();
            case 3: rbDislocationPause.isSelected();
        }
        // Settar tipo de amostra
        switch(method.getSpecimenTypeIndex()){
            case 0:
                rbRectangle.isSelected();
                txtSpecimenAValueRectangle.setText(String.valueOf(method.getSpecimenAValue()));
                txtSpecimenBValueRectangle.setText(String.valueOf(method.getSpecimenBValue()));
            case 1:
                rbCylinder.isSelected();
                txtSpecimenAValueCylinder.setText(String.valueOf(method.getSpecimenAValue()));
                txtSpecimenBValueCylinder.setText(String.valueOf(method.getSpecimenBValue()));
            case 2:
                rbTubular.isSelected();
                txtSpecimenValueTubular.setText(String.valueOf(method.getSpecimenAValue()));
        }
        // Settar cbNormIndex
        switch(method.getNormIndex()){
            case 0: cbNormList.getSelectionModel().select(0);
            case 1: cbNormList.getSelectionModel().select(1);
            case 2: cbNormList.getSelectionModel().select(2);
        }
        // Settar cbEssayType
        switch(method.getEssayTypeIndex()){
            case 0: cbEssayType.getSelectionModel().select(0);
            case 1: cbEssayType.getSelectionModel().select(1);
            case 2: cbEssayType.getSelectionModel().select(2);
            case 3: cbEssayType.getSelectionModel().select(3);
        }
        // Settar cbExtensometer1
        switch(method.getExtensometer1Index()){
            case 0: cbExtensometer1.getSelectionModel().select(0);
            case 1: cbExtensometer1.getSelectionModel().select(1);
            case 2: cbExtensometer1.getSelectionModel().select(2);
        }
        // Settar cbExtensometer2
        switch(method.getExtensometer2Index()){
            case 0: cbExtensometer2.getSelectionModel().select(0);
            case 1: cbExtensometer2.getSelectionModel().select(1);
            case 2: cbExtensometer2.getSelectionModel().select(2);
        }
        // Settar chartView
        switch(method.getChartViewIndex()){
            case 0: rbForceXPosition.isSelected();
            case 1: rbStrainXDeform.isSelected();
        }
        // Settar demais valores
        txtMethodName.setText(method.getMethodName());
        txtEssayVelocity.setText(String.valueOf(method.getEssayVelocity()));
        txtSpecimenCrossSectionArea.setText(String.valueOf(method.getSpecimenCrossSectionArea()));
        txtSpecimenCrossSectionLength.setText(String.valueOf(method.getSpecimenCrossSectionLength()));
        txtOffsetIntersectionLine.setText(String.valueOf(method.getOffsetIntersectionLine()));
        txtGainIntersectionLine.setText(String.valueOf(method.getGainIntersectionLine()));

    }

    /**
     * Método de listagem de idiomas dentro do ComboBox (cbLanguage)
     */
    private void normList() {
        String[] normList = new String[]{"ISO 6892-1", "NBR13384", "NBR5739"};
        for (String norm : normList) {
            cbNormList.getItems().add(norm);
        }
    }

    /**
     * Método de listagem de idiomas dentro do ComboBox (cbLanguage)
     */
    private void essayTypeList() {
        String[] essayTypes = new String[]{"Tração", "Compressão", "Flexão"};
        for (String type : essayTypes) {
            cbEssayType.getItems().add(type);
        }
    }

    /**
     * Método de listagem de Extensometro 1 dentro do ComboBox (cbExtensometer1)
     */
    private void extensometer1List() {
        String[] extList = new String[]{"Desconectado", "50", "250", "500"};
        for (String ext : extList) {
            cbExtensometer1.getItems().add(ext);
        }
    }

    /**
     * Método de listagem de Extensometro 2 dentro do ComboBox (cbExtensometer2)
     */
    private void extensometer2List() {
        String[] extList = new String[]{"Desconectado", "50", "250", "500"};
        for (String ext : extList) {
            cbExtensometer2.getItems().add(ext);
        }
    }

    /**
     * Método de listagem de unidades de Forca dentro do ComboBox (cbForceSelection)
     */
    private void forceUnitSelection() {
        String[] forceUnitList = new String[]{"N", "Kg"};
        for (String unit : forceUnitList) {
            cbForceUnitSelection.getItems().add(unit);
        }
    }

    /**
     * Método de listagem de unidade de Posicao dentro do ComboBox (cbPositionSelection)
     */
    private void positionUnitSelection() {
        String[] positionUnitList = new String[]{"mm", "in"};
        for (String unit : positionUnitList) {
            cbPositionUnitSelection.getItems().add(unit);
        }
    }

//    ************* Chart Construction *****************

    /**
     * Metodo que atualiza os rotulos do eixo x e y do grafico em funcao das unidades selecionadas nos paineis
     */
    @FXML
    private void xyAxisAdjust(){
        // Rotulando eixos X e Y
        if(currentForceUnit == "Kg"){
            yAxis.setLabel("Força (Kg)");
        } else {
            yAxis.setLabel("Força (N)");
        }
        if(currentPositionUnit == "in"){
            xAxis.setLabel("Deslocamento (in)");
        } else{
            xAxis.setLabel("Deslocamento (mm)");
        }
        chartEssayLine.setTitle("Gráfico " + currentForceUnit + " x " + currentPositionUnit);
    }

    /**
     * MODIFICAR CÓDIGO (TROCAR COUNT POR CONDICOES) >> Método que cria, em tempo real, o gráfico do ensaio.
     * Nele, podemos implementar switch (para alteração dos parâmetros N/mm ou MPa/%)
     */
    class RTChartCreate implements Runnable {

        int count = 0;

        @Override
        public synchronized void run() {
            fMax = 0D;
            pMax = 0D;
            tMax = 0D;
            tEsc = 0D;
            along = 0D;
            redArea = 0D;
            mYoung = 0D;

            Double count = 0D;
            Double adjustedForce;
            Double adjustedPosition;

            if(forceAdjustInversion==-1){
                forceAdjustInversionView=-1;
                positionAdjustInversionView=-1;
            }

            List<Double> forceList = new ArrayList<>();
            List<Double> positionList = new ArrayList<>();

            try {
                while (count<60) {
//                while(autoBreak()==false){
                    Thread.sleep(10);

                    // Aquisicao dos valores ja convertidos para a unidade selecionada e ajustados para ensaio para
                    // cima ou para baixo
                    adjustedForce = selectedUnitForceValue() * forceAdjustInversion;
                    adjustedPosition = selectedUnitPositionValue() * positionAdjustInversion;

                    forceList.add(adjustedForce);
                    positionList.add(adjustedPosition);


                    //Identifica valor de Forca Max N
                    Platform.runLater(() -> {
                        // Update UI.
                        for(Double force : forceList) {
                            if (force > fMax) {
                                fMax = force;
                            }
                            lbFMax.setText(String.format("%.2f", Double.valueOf(fMax)));
                        }
                    });
                    //Identifica valor de Posicao Max mm
                    Platform.runLater(() -> {
                        // Update UI.
                         for(Double position : positionList) {
                             if (position > pMax) {
                                 pMax = position;
                             }
                             lbPMax.setText(String.format("%.2f", Double.valueOf(pMax)));
                         }
                    });
                    //Identifica valor de Tensao Max MPa
                    tMax = count;
                    Platform.runLater(() -> {
                        // Update UI.
                        // condição
                        for (int i = 0; i < tMax; i++) {
                            lbTMax.setText(String.valueOf(i));
                        }
                    });
                    //Identifica valor de Tensao de escoamento MPa
                   tEsc = count;
                    Platform.runLater(() -> {
                        // Update UI.
                        // condição
                        for (int i = 0; i < tEsc; i++) {
                            lbTEsc.setText(String.valueOf(i));
                        }
                    });
                    //Identifica valor de Alongamento %
                    along = count;
                    Platform.runLater(() -> {
                        // Update UI.
                        // condição
                        for (int i = 0; i < along; i++) {
                            lbAlong.setText(String.valueOf(i));
                        }
                    });
                    //Identifica valor de Reducao de Area %
                    redArea = count;
                    Platform.runLater(() -> {
                        // Update UI.
                        // condição
                        for (int i = 0; i < redArea; i++) {
                            lbRedArea.setText(String.valueOf(i));
                        }
                    });
                    //Identifica valor de M. Young MPa
                    mYoung = count;
                    Platform.runLater(() -> {
                        // Update UI.
                        // condição
                        for (int i = 0; i < mYoung; i++) {
                            lbMYoung.setText(String.valueOf(i));
                        }
                    });
                    // Setting final values
                    Double finalAdjustedForce = adjustedForce;
                    Double finalAdjustedPosition = adjustedPosition;
                    Platform.runLater(() -> {
                        // Update UI.
                        finalForce = finalAdjustedForce;
                        finalPosition = finalAdjustedPosition;
                    });



                    System.out.println(adjustedForce + " " + adjustedPosition);
                    // Exposicao dos valores no grafico em funcao da escolha do usuario MPa x % ou N x mm
                    // INCLUIR FORMULA DE CONVERSAO DOS VALORES
                    Platform.runLater(() -> {
                        // Update UI.
                        series.getData().add(new XYChart.Data<>(finalAdjustedPosition, finalAdjustedForce));
                    });

                    // Adding dot values in a global String chartString
                    // essayChart String type: 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10
                    if (chartString != null) {
                        chartString += "," + String.format("%.4f", adjustedForce) + ";" + String.format("%.4f", adjustedPosition);
                    } else {
                        chartString = String.format("%.4f", adjustedForce) + ";" + String.format("%.4f", adjustedPosition);
                    }
                    System.out.println(chartString);
                    count++;
                }
                serialConn.stopMove();
                try{
                    Platform.runLater(()->{
                        // Mudando background do ensaio de volta ao padrao
                        vBoxEssayStart.setStyle("-fx-background-color: #A1A1A1");
                        // Alerta de confirmação de finalização de ensaio
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Ensaio finalizado");
                        alert.setHeaderText("Ensaio finalizado com sucesso! Verifique os dados e salve o ensaio. Dados não salvos serão descartados.");
                        Stage stage = (Stage) btnEssaySave.getScene().getWindow();
                        alert.initOwner(stage);
                        alert.showAndWait();
                        // Retornando fator multiplicador dos valores Force e Position para 1 (padrao)
                        forceAdjustInversionView=1;
                        positionAdjustInversionView=1;
                    });

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println(forceList);
                System.out.println(positionList);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Método de construção do gráfico no ensaio, tendo como referência essayChart, na tb_essay do DB.
     *
     * @param pk
     */
    @FXML
    private void essayChart(int pk) {
        XYChart.Series seriesSingle = new XYChart.Series();
        //populating the series with data

        Essay essay = essayDAO.findById(pk);
        seriesSingle.setName(essay.getEssayIdentification());

        // essayChart String type: 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10
        // array receives the split parts of the essayChart string
        String strArraySplit[] = essay.getEssayChart().split(",");
        for (String str : strArraySplit) {
            // each part creates a dot[2] to receive x and y value
            String dot[] = str.split(";");
            for (int i = 0; i < dot.length; i += 2) {
                System.out.println(dot[i] + " " + dot[i + 1]);
                // seriesSingle receives two sequencial values, representing x and y value
                seriesSingle.getData().add(new XYChart.Data(Double.parseDouble(dot[i]), Double.parseDouble(dot[i + 1])));
            }
        }
        chartEssayLine.getData().add(seriesSingle);
    }

//    /**
//     * Método genérico para injeção do output, aplicável para os diferentes processos port.OutputStream() que requeiram uma 'string'
//     *
//     * @param stg
//     */
//    private synchronized void outputInjection(String stg) {
//        PrintWriter output = new PrintWriter(port.getOutputStream(), true);
//        output.print(stg);
//        output.flush();
//    }
//
//    /**
//     * Método que recebe o valor real do input
//     *
//     * @return String
//     */
//    private synchronized String inputValue() {
//        Scanner s = new Scanner(port.getInputStream());
//        return s.nextLine();
//    }
//
//    // INICIO*********** Métodos pré ensaio ***********
//
//    /**
//     * Método que zera a posição (injeção de '0')
//     */
//    @FXML
//    private synchronized void resetPosition() {
//        outputInjection("0");
//    }
//
//    /**
//     * Método que solicita o valor da força (injeção de '1')
//     */
//    @FXML
//    private synchronized void forceRequest() throws InterruptedException {
//        outputInjection("1");
//    }
//
//    /**
//     * Método que solicita o valor da posição (injeção de '2')
//     */
//    @FXML
//    private synchronized void positionRequest() throws InterruptedException {
//        outputInjection("2");
//    }
//
//    // FIM*********** Métodos pré ensaio ***********
//
//    // INICIO*********** Métodos de Movimento ***********
//
//    /**
//     * Método que solicita interrupção do movimento (injeção de '3')
//     */
//    @FXML
//    private synchronized void stopMove() {
//        outputInjection("3");
//    }
//
//    /**
//     * Método que solicita movimento de ajuste para cima (injeção de '4')
//     */
//    @FXML
//    private synchronized void moveUp() {
//        outputInjection("4");
//    }
//
//    /**
//     * Método que solicita movimento de ajuste para baixo (injeção de '5')
//     */
//    @FXML
//    private synchronized void moveDown() {
//        outputInjection("5");
//    }
//
//    /**
//     * Método que solicita movimento de ensaio para cima (injeção de '6')
//     */
//    @FXML
//    private synchronized void moveUpEssay() {
//        outputInjection("6");
//    }
//
//    /**
//     * Método que solicita movimento de ensaio para baixo (injeção de '7')
//     */
//    @FXML
//    private synchronized void moveDownEssay() {
//        outputInjection("7");
//    }
//
//    // FIM*********** Métodos de Movimento ***********
//
//    // INICIO*********** Métodos Ajuste de Velocidade ***********
//
    /**
     * REQUER IMPLEMENTACAO CORRETA: Método que define a velocidade de ajuste
     */
    @FXML
    private synchronized void adjustVelocity(Integer value) {

        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if (value != null) {
            if(value >= 40000){
                txtAdjustVelocity.setText("40000");
                serialConn.adjustVelocity(40000);
            } else if(value >= 15000){
                serialConn.adjustVelocity(value);
            } else {
                txtAdjustVelocity.setText("15000");
                serialConn.adjustVelocity(15000);
            }
        } else {
            txtAdjustVelocity.setText("15000");
            serialConn.adjustVelocity(15000);
        }
    }

    /**
     * REQUER IMPLEMENTACAO CORRETA: Método que define a velocidade de ensaio
     */
    @FXML
    private synchronized void essayVelocity(Integer value) {

        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if (value != null) {
            if(value >= 40000){
                txtEssayVelocity.setText("40000");
                serialConn.essayVelocity(40000);
            } else if(value >= 15000){
                serialConn.essayVelocity(value);
            } else {
                txtEssayVelocity.setText("15000");
                serialConn.essayVelocity(15000);
            }
        } else {
            txtEssayVelocity.setText("15000");
            serialConn.essayVelocity(15000);
        }
    }

    /**
     * Método que solicita movimento de ajuste para cima (injeção de '4')
     */
    @FXML
    public synchronized void moveUpAdjust() {
        serialConn.moveUpAdjust();
        System.out.println("Moveu CIMA 4");
    }

    /**
     * Método que solicita movimento de ajuste para baixo para a classe SerialConnection
     */
    @FXML
    public synchronized void moveDownAdjust() {
        serialConn.moveDownAdjust();
        System.out.println("Moveu BAIXO 5");
    }

    /**
     * Método solicita a posição para a classe SerialConnection
     */
    @FXML
    public synchronized void resetPosition() {
        serialConn.resetPosition();
    }

    /**
     *  Metodo que injeto o atual valor analogico a variavel forceTare
     */
    @FXML
    private void forceTare(){
        forceTare = currentBaseForce;
    }

    // FIM*********** Métodos Ajuste de Velocidade ***********

    // INICIO*********** Métodos de realização do Ensaio ***********


///////////////////////////////////
    @FXML
    private synchronized void test() throws InterruptedException {


        try{
            // Mudando background do ensaio para amarelo (preparando)
            vBoxEssayStart.setStyle("-fx-background-color: #BCAA06");
            // Alerta de inicio de ensaio
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Iniciando ensaio");
            alert.setHeaderText("O ensaio está começando! Libere a área de movimentação do eixo.");
            Stage stage = (Stage) btnForceZero.getScene().getWindow();
            alert.initOwner(stage);
            alert.showAndWait();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        // Alterando fator multiplicador dos valores Force e Position para -1 (caso o ensaio seja negativo)
//        if(forceAdjustInversion==-1){
//            forceAdjustInversionView=-1;
//            positionAdjustInversionView=-1;
//        }

        try{
            // Mudando background do ensaio para vermelho (iniciado)
            vBoxEssayStart.setStyle("-fx-background-color: #ed0202");
            Thread.sleep(1000);
            // Construcao do grafico, da serie e inclusao de parametros do grafico
            chartEssayLine.getData().clear();
            // Criacao da serie a qual serao incluidos os valores
            series = new XYChart.Series<>();
            series.setName("Leitura");
            // Plotagem dos pontos da serie no grafico
            chartEssayLine.getData().add(series);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Aquisicao de valores iniciais de Forca e Posicao
        initialForce = selectedUnitForceValue();
        initialPosition = selectedUnitPositionValue();

        //indica que eixo está em movimento
        moving = true;

        // Thread que atualiza os valores no grafico
        Thread chartThread = new Thread(new RTChartCreate());
        Platform.runLater(() -> {
            // Inicia o movimento
            essayTypeMove();
            chartThread.start();
        });
    }

    @FXML
    private boolean chartConstructor(){
        // Thread que atualiza os valores no grafico
        Thread chartThread = new Thread(new RTChartCreate());
        Platform.runLater(() -> {
            // Inicia o movimento
            essayTypeMove();
            chartThread.start();
        });
        return true;
    }

    /**
     * REQUER IMPLEMENTACAO CORRETA: Metodo que retoma a leitura dos pontos para retomar o ensaio pausado
     * @throws InterruptedException
     */
    @FXML
    private synchronized void essayReturn() throws InterruptedException {


        // Thread que atualiza os valores no grafico
        Thread chartThread = new Thread(new RTChartCreate());
        Platform.runLater(() -> {
            // Inicia o movimento
            essayTypeMove();
            while(true){
                chartThread.start();
            }
        });

        // Setting final values
        finalForce = selectedUnitForceValue();
        finalPosition = selectedUnitPositionValue();

    }
///////////////////////////////////

    /**
     * REQUER IMPLEMENTACAO CORRETA: Método que inicia o ensaio
     *
     * @throws InterruptedException
     */
    @FXML
    private synchronized void essayStart() throws InterruptedException {

        // Alerta de inicio de ensaio
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registro de ensaio");
        alert.setHeaderText("Ensaio salvo com sucesso! Ele está disponível em Relatório");
        Stage stage = (Stage) btnEssaySave.getScene().getWindow();
        alert.initOwner(stage);
        alert.showAndWait();

        // Aquisicao dos parametros inseridos pelo usuario
        try{
            essayVelocity(Integer.valueOf(txtEssayVelocity.getText()));
            Thread.sleep(15);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Comando para subir ou descer o eixo, baseado no tipo de ensaio selecionado
        Platform.runLater(()-> {
            while(true){
                essayTypeMove();
            }
        });

        //indica que eixo está em movimento
        moving = true;

        // Construcao do grafico, da serie e inclusao de parametros do grafico
        chartEssayLine.getData().clear();
        series = new XYChart.Series<>();
        if(txtEssayIdentification.getText()!=""){
            series.setName(txtEssayIdentification.getText());
        } else{
            series.setName("Leitura");
        }
        chartEssayLine.getData().add(series);

        // Aquisicao de valores iniciais de Forca e Posicao
        initialForce = selectedUnitForceValue();
        initialPosition = selectedUnitPositionValue();

        // Thread que atualiza os valores no grafico
        Thread chartThread = new Thread(new RTChartCreate());
        chartThread.start();
        chartThread.join();

    }

    /**
     * Metodo que retorna valor de Forca dependendo da unidade de medida selecionada (Newton - N, quilograma - Kg)
     */
    @FXML
    private Double selectedUnitForceValue(){
        if(cbForceUnitSelection.getSelectionModel().getSelectedItem().toString() == "N"){
            currentForceUnit = "N";
            return currentNewtonForce;
        } else if(cbForceUnitSelection.getSelectionModel().getSelectedItem().toString() == "Kg"){
            currentForceUnit = "Kg";
            return currentKgForce;
        }
        return null;
    }

    /**
     * Metodo que retorna valor Posicao dependendo da unidade de medida selecionada (milimetros - mm, polegadas - in)
     */
    @FXML
    private Double selectedUnitPositionValue(){
        if(cbPositionUnitSelection.getSelectionModel().getSelectedItem().toString() == "mm"){
            currentPositionUnit = "mm";
            return currentMmPosition;
        } else if(cbPositionUnitSelection.getSelectionModel().getSelectedItem().toString() == "in"){
            currentPositionUnit = "in";
            return currentPolPosition;
        }
        return null;
    }

    /**
     * Metodo que define direcao do movimento do eixo, baseado no tipo de ensaio
     */
    private void essayTypeMove(){
        if(cbEssayType.getSelectionModel().getSelectedItem().toString()=="Tração"){
//            serialConn.moveUpEssay();
            serialConn.moveUpAdjust();
        } else if(cbEssayType.getSelectionModel().getSelectedItem().toString()=="Compressão" ||
                cbEssayType.getSelectionModel().getSelectedItem().toString()=="Flexão"){
//            serialConn.moveDownEssay();
            serialConn.moveDownAdjust();
        }
    }

    /**
     * Metodo que define direcao do movimento do eixo, baseado no tipo de ensaio
     */
    private void essayType(){
        if(cbEssayType.getSelectionModel().getSelectedItem().toString()=="Tração"){
            forceAdjustInversion = 1;
            positionAdjustInversion = 1;
            System.out.println(forceAdjustInversion + " e " + positionAdjustInversion);
        } else if(cbEssayType.getSelectionModel().getSelectedItem().toString()=="Compressão" ||
                cbEssayType.getSelectionModel().getSelectedItem().toString()=="Flexão"){
            forceAdjustInversion = -1;
            positionAdjustInversion = -1;
            System.out.println(forceAdjustInversion + " e " + positionAdjustInversion);
        }
    }

    /**
     * Metodo de interrupção automatica do ensaio
     */
    @FXML
    private boolean autoBreak() {
        if(fMax>0 && pMax>0 ){
            if(rbForceDownBreak.isSelected()){
                if(selectedUnitForceValue() >= fMax * (100 - Double.valueOf(String.valueOf(txtForcePercentageBreak)))/100) {
                    return false;
                }
                return true;

            } else if(rbMaxForceBreak.isSelected()){
                if(selectedUnitForceValue() < Double.parseDouble(txtMaxForceBreak.getText())){
                    return false;
                }
                return true;

            } else if(rbDislocationBreak.isSelected()){
                if(selectedUnitPositionValue() < Double.parseDouble(txtDislocationValueBreak.getText())){
                    return false;
                }
                return true;

            } else if(rbDislocationPause.isSelected()){
                if(selectedUnitPositionValue() < Double.parseDouble(txtDislocationValuePause.getText())){
                    return false;
                }
                return true;
            }
            serialConn.stopMove();
        }
        return false;
    }

    /**
     * REQUER IMPLAMTANÇÃO >> Método que pausa se o eixo estiver em movimento e retoma se estiver parado
     */
    @FXML
    private void essayPause() throws InterruptedException {
        if (moving == true) {
            serialConn.stopMove();
            moving = false;
            btnPause.setText("Retomar");
        } else {
            moving = true;
            btnPause.setText("Pause");
            essayReturn();
        }
    }

    /**
     * Método que pára o ensaio
     */
    @FXML
    private void essayStop() {
        if (moving == true) {
            serialConn.stopMove();
            moving = false;
        } else {
            System.out.println("O ensaio não foi iniciado!");
        }
    }

    /**
     * Método que solicita interrupção do movimento para a classe SerialConnection
     */
    @FXML
    public synchronized void stopMove() {
        serialConn.stopMove();
    }

    /**
     * Método que armazena no DB as configuracoes de ensaio definidas pelo usuario
     */
    @FXML
    private void saveMethod() {
        if(sysVar.getUserId()!=3){
            if(txtMethodName.getText()!=""){
                Method validation = methodDAO.findByMethod(txtMethodName.getText());
                if(validation==null){
                    Method method = new Method();
                    // Settar index de interrupcao automatica
                    if(rbForceDownBreak.isSelected()){
                        method.setAutoBreakIndex(0);
                    } else if(rbMaxForceBreak.isSelected()){
                        method.setAutoBreakIndex(1);
                    } else if(rbDislocationBreak.isSelected()){
                        method.setAutoBreakIndex(2);
                    } else if(rbDislocationPause.isSelected()){
                        method.setAutoBreakIndex(3);
                    }
                    // Settar index de tipo de corpo de amostra
                    if(rbRectangle.isSelected()){
                        method.setSpecimenTypeIndex(0);
                        // Settar dimensoes amostra retangular
                        method.setSpecimenAValue(Double.valueOf(txtSpecimenAValueRectangle.getText()));
                        method.setSpecimenBValue(Double.valueOf(txtSpecimenBValueRectangle.getText()));
                    } else if(rbCylinder.isSelected()){
                        method.setSpecimenTypeIndex(1);
                        // Settar dimensoes amostra cilindrica
                        method.setSpecimenAValue(Double.valueOf(txtSpecimenAValueCylinder.getText()));
                        method.setSpecimenBValue(Double.valueOf(txtSpecimenAValueCylinder.getText()));
                    } else if(rbTubular.isSelected()){
                        method.setSpecimenTypeIndex(2);
                        // Settar dimensoes amostra tubular
                        method.setSpecimenAValue(Double.valueOf(txtSpecimenValueTubular.getText()));
                        method.setSpecimenBValue(null);
                    }
                    // Settar index da norma
                    switch(cbNormList.getSelectionModel().getSelectedItem().toString()){
                        case "ISO 6892-1": method.setNormIndex(0);
                        case "NBR13384": method.setNormIndex(1);
                        case "NBR5739": method.setNormIndex(2);
                    }
                    // Settar index do tipo de ensaio
                    switch(cbEssayType.getSelectionModel().getSelectedItem().toString()){
                        case "Tração": method.setEssayTypeIndex(0);
                        case "Compressão": method.setEssayTypeIndex(1);
                        case "Flexão": method.setEssayTypeIndex(2);
                    }
                    // Settar index do extensometro 1
                    switch(cbExtensometer1.getSelectionModel().getSelectedItem().toString()){
                        case "Desconectado": method.setExtensometer1Index(0);
                        case "50": method.setExtensometer1Index(1);
                        case "250": method.setExtensometer1Index(2);
                        case "500": method.setExtensometer1Index(3);
                    }
                    // Settar index do extensometro 2
                    switch(cbExtensometer2.getSelectionModel().getSelectedItem().toString()){
                        case "Desconectado": method.setExtensometer2Index(0);
                        case "50": method.setExtensometer2Index(1);
                        case "250": method.setExtensometer2Index(2);
                        case "500": method.setExtensometer2Index(3);
                    }
                    // Settar index do chartView
                    if(rbForceXPosition.isSelected()){
                        method.setChartViewIndex(0);
                    } else if(rbStrainXDeform.isSelected()){
                        method.setChartViewIndex(1);
                    }
                    method.setMethodName(txtMethodName.getText());
                    method.setMethodDate(currentDay + " - " + currentHour);
                    method.setEssayVelocity(Double.valueOf(txtEssayVelocity.getText()));
                    method.setSpecimenCrossSectionArea(Double.valueOf(txtSpecimenCrossSectionArea.getText()));
                    method.setSpecimenCrossSectionLength(Double.valueOf(txtSpecimenCrossSectionLength.getText()));
                    method.setOffsetIntersectionLine(Double.valueOf(txtOffsetIntersectionLine.getText()));
                    method.setGainIntersectionLine(Double.valueOf(txtGainIntersectionLine.getText()));
                    methodDAO.create(method);

                    // Alerta de confirmação de registro do metodo
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Configurações de ensaio");
                    alert.setHeaderText("Configurações salvas com sucesso!");
                    Stage stage = (Stage) btnSaveMethod.getScene().getWindow();
                    alert.initOwner(stage);
                    alert.showAndWait();

                } else{
                    // Alerta de nome de metodo ja existente
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Configurações de ensaio");
                    alert.setHeaderText("O nome do método já existe! Escolha outro e tente novamente.");
                    Stage stage = (Stage) btnSaveMethod.getScene().getWindow();
                    alert.initOwner(stage);
                    alert.show();
                }

            } else{
                // Alerta de ausencia de nome para o ensaio
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Configurações de ensaio");
                alert.setHeaderText("O campo Nome do Ensaio deve estar preenchido!");
                Stage stage = (Stage) btnSaveMethod.getScene().getWindow();
                alert.initOwner(stage);
                alert.show();
            }

        } else {
            // Alerta de ausencia de nome para o ensaio
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Configurações de ensaio");
            alert.setHeaderText("Faça o login para habilitar essa função.");
            Stage stage = (Stage) btnSaveMethod.getScene().getWindow();
            alert.initOwner(stage);
            alert.show();
        }

    }

    /**
     * Método que cria um essay e salva no DB
     */
    @FXML
    private void essaySave() {
        if(chartString!=null && txtEssayIdentification.getText()!=""){
            essayFinalyzed = new Essay();
            essayFinalyzed.setUserId(sysVar.getUserId()); // Substituir por ID referente ao login
            essayFinalyzed.setEssayIdentification(txtEssayIdentification.getText());
            essayFinalyzed.setEssayNorm(cbNormList.getSelectionModel().getSelectedItem().toString());
            essayFinalyzed.setEssayUsedMachine(null);
            essayFinalyzed.setEssayChargeCell(0.0);
            essayFinalyzed.setEssayInitialForce(initialForce);
            essayFinalyzed.setEssayFinalForce(finalForce);
            essayFinalyzed.setEssayInitialPosition(initialPosition);
            essayFinalyzed.setEssayFinalPosition(finalPosition);
            essayFinalyzed.setEssayDislocationVelocity(Double.parseDouble(txtEssayVelocity.getText()));
            essayFinalyzed.setEssayTemperature(0.0);
            essayFinalyzed.setEssayPreCharge(0.0);
            essayFinalyzed.setEssayRelativeHumidity(0.0);
            essayFinalyzed.setEssayMaxForce(fMax);
            essayFinalyzed.setEssayMaxPosition(pMax);
            essayFinalyzed.setEssayMaxTension(tMax);
            essayFinalyzed.setEssayEscapeTension(tEsc);
            essayFinalyzed.setEssayAlong(along);
            essayFinalyzed.setEssayAreaRed(redArea);
            essayFinalyzed.setEssayMYoung(mYoung);
            essayFinalyzed.setEssayChart(chartString);
            essayFinalyzed.setEssayDay(currentDay);
            essayFinalyzed.setEssayHour(currentHour);

            if(sysVar.getUserId()==3){
                essayFinalyzed.setEssayId(1);
                essayDAO.update(essayFinalyzed);
            } else{
                essayDAO.create(essayFinalyzed);
            }
            System.out.println(essayFinalyzed);

            // Alerta de confirmação de registro de ensaio
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro de ensaio");
            alert.setHeaderText("Ensaio salvo com sucesso! Ele está disponível em Relatório");
            Stage stage = (Stage) btnEssaySave.getScene().getWindow();
            alert.initOwner(stage);
            alert.showAndWait();
            newEssay();
            tpEssayFlow.getSelectionModel().select(0);
        } else if(chartString==null){
            // Alerta de realização incorreta do ensaio
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro de ensaio");
            alert.setHeaderText("Finalize o ensaio para gerar os dados!");
            Stage stage = (Stage) btnEssaySave.getScene().getWindow();
            alert.initOwner(stage);
            alert.show();
        } else if(txtEssayIdentification.getText()==""){
            // Alerta de ausencia de nome para o ensaio
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro de ensaio");
            alert.setHeaderText("Preencha o nome do ensaio!");
            Stage stage = (Stage) btnEssaySave.getScene().getWindow();
            alert.initOwner(stage);
            alert.show();
        }

    }

    /**
     * Método que descarta as infomrações coletadas no ensaio realizado
     */
    @FXML
    private void essayDiscart() {
        essayFinalyzed = null;
        System.out.println(essayFinalyzed);
        // Popup informativo
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Descarte de ensaio");
        alert.setHeaderText("As informações do esaio foram descartadas.");
        Stage stage = (Stage) btnEssayDiscart.getScene().getWindow();
        alert.initOwner(stage);
        alert.showAndWait();
        newEssay();
        newChart();
        tpEssayFlow.getSelectionModel().select(0);
    }

    /**
     * Metodo que reseta todos os parametros para um novo ensaio
     */
    @FXML
    private void newEssay(){
        cbMethodList.getSelectionModel().clearSelection();
        cbNormList.getSelectionModel().clearSelection();
        cbEssayType.getSelectionModel().select(0);
        cbForceUnitSelection.getSelectionModel().select(0);
        cbPositionUnitSelection.getSelectionModel().select(0);
        lbFMax.setText("0.000");
        lbPMax.setText("0.000");
        lbTMax.setText("0.000");
        lbTEsc.setText("0.000");
        lbAlong.setText("0.000");
        lbRedArea.setText("0.000");
        lbMYoung.setText("0.000");
        txtEssayVelocity.setText("15000");
//        essayVelocity(15000);
        txtAdjustVelocity.setText("15000");
//        adjustVelocity();
        txtEssayIdentification.setText("");
        txtForcePercentageBreak.setText("20");
        txtMaxForceBreak.setText("0.00");
        txtDislocationValueBreak.setText("0.00");
        txtDislocationValuePause.setText("0.00");
        rbForceXPosition.isSelected();
        rbForceDownBreak.isSelected();
        tpEssayFlow.getSelectionModel().select(0);
        forceTare();
        initialForce = 0D;
        finalForce = 0D;
        initialPosition = 0D;
        finalPosition = 0D;
        fMax = 0D;
        pMax = 0D;
        tMax = 0D;
        tEsc = 0D;
        along = 0D;
        redArea = 0D;
        mYoung = 0D;
        currentForceUnit = "N";
        currentPositionUnit = "mm";
        moving = false;
        forceAdjustInversion = 1;
        positionAdjustInversion = 1;
        forceAdjustInversionView = 1;
        positionAdjustInversionView = 1;
    }

    /**
     * Metodo que limpa toda a area do grafico do ensaio
     */
    @FXML
    private void newChart(){
        chartString = null;
        chartEssayLine.getData().clear();
        series.getData().clear();
    }

    // INICIO*********** Métodos de realização do Ensaio ***********
}


//         Criando Thread >> Método contendo Thread, que chama método que contém Platform.runLater
//         Método padronizado no projeto é Criação de uma class que implements Runnable, contendo
//         Platform.runLater (para atualização da GUI), Thread iniciada no método de aplicação

//  private synchronized void FPReadingThread() {
//
//        try {
//            outputInjection("1");  // Requerimento do valor da força
//            Thread.sleep(13);
//            String impF = inputValue();
//            outputInjection("2");  // requerimento do valor da posição
//            Thread.sleep(13);
//            String impP = inputValue();
//
//            // Salvando dados no banco de dados - dados persistentes como variável global
//            SystemVariable sysVar = new SystemVariable(1, Double.valueOf(impF), Double.valueOf(impP));
//            systemVariableDAO.update(sysVar);
//
//            //Atualização da UI pela Thread a partir dos dados salvos no DB
//            Platform.runLater(() -> {
//                txtForceView.setText(String.format("%.2f", Double.valueOf(impF)));
//                txtPositionView.setText(String.format("%.2f", Double.valueOf(impP)));
//            });
//
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Método de conexão automática
//     */
//  @FXML
//  private void autoConnect() {
//
//    SystemParameter sysPar = systemParameterDAO.find();
//    port = SerialPort.getCommPort(sysPar.getPortName());
//    System.out.println("Conectado à porta: " + sysPar.getPortName() + " - " + port);
//    if (port.openPort()) {
//        txtConnected.setText("Conectado");
//        txtConnected.setStyle("-fx-background-color: #06BC0E");
//        port.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
//        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 50, 50);
//        port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
//
//        // Thread para solicitar Posição e Força e atualizar lbForceView e lbPositionView
//            Thread t = new Thread(() -> {
//                try {
//                    Thread.sleep(1000);
//                    while (true) {
//                        FPReadingThread();
//                    }
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            t.start();
//    }
