package br.com.biopdi.mbiolabv2.controller.SceneController;


import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.controller.serial.SerialConnection;
import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import br.com.biopdi.mbiolabv2.model.bean.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
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
    static {Locale.setDefault(Locale.US);}  // Define a localidade para usar ponto como separador decimal
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final MethodDAO methodDAO = new MethodDAO();
    private final SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private SystemVariable sysVar = sysVarDAO.find();
    private User user = userDAO.findById(sysVar.getUserId());
    private Setup setup;
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
    private XYChart.Series<Number, Number> series, seriesTxD;
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
            txtPercentObtainedForce, txtObtainedForce, txtSpecimenReducedArea;
    @FXML
    private Button btnPositionUp, btnPositionDown, btnStart, btnPause, btnStop, btnChargeMethod,
            btnEssayByUserId, btnEssaySave, btnEssayDiscart, btnForceZero, btnSaveMethod;
    @FXML
    private RadioButton rbForceXPosition, rbStrainXDeform, rbForceDownBreak, rbMaxForceBreak, rbDislocationBreak,
            rbDislocationPause, rbRectangle, rbCylinder, rbTubular;
    @FXML
    private ImageView ivEssayUser;
    @FXML
    private Slider shAdjustVelocity;
    @FXML
    private TabPane tpEssayFlow;

    private Double currentBaseForce = 0D, taredCurrentForce = 0D, currentNewtonForce = 0D, currentKgForce = 0D,
            forceTare = 0D, currentBasePosition = 0D, currentMmPosition = 0D, currentPolPosition = 0D,
            referencePosition = 0D, currentTension = 1D, currentDeformation = 0D, initialForce, finalForce, initialPosition,
            finalPosition, fMax, pMax, tMax, tEsc, along, redArea, mYoung, nForceConversionFactor = 1D,
            kgForceConversionFactor = 1D, inPositionConversionFactor = 1D;
    private String chartString = null, chartStringTensionXDeform = null, currentForceUnit = "N", currentPositionUnit = "mm";
    private Boolean moving = false, errorEmergencyButton = false, errorInfLimit = false, errorSupLimit = false,
            errorChargeCellLimit = false, errorChargeCellDisconnected = false, essayfinished = true;
    private Integer forceAdjustInversion = 1, positionAdjustInversion = 1, forceAdjustInversionView = 1,
            positionAdjustInversionView = 1, currentChargeCell = 0, chargeCellMultipFactor = 1, errorDecValue = 0;
    @FXML
    private VBox vBoxEssayStart;


    Date systemDate;
    SimpleDateFormat brasilianDay = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat brasilianHour = new SimpleDateFormat("HH:mm");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(serialConn.testConnection()){
            serialConn.setupParameter();
        }
        setup = setupDAO.find();
        ivEssayUser.setClip(new Circle(20,20,20));
        normList();
        essayTypeList();
        savedMethodList();
        extensometer1List();
        extensometer2List();
        forceUnitSelection();
        positionUnitSelection();
        if(serialConn.testConnection()){
            autoConnect();
        } else{
            txtConnected.setText("Desconectado");
            txtConnected.setStyle("-fx-background-color: #BCAA06");
        }
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
                    txtSpecimenCrossSectionCalcule.setText("1.00");
                    if(Double.parseDouble(txtSpecimenAValueRectangle.getText())!=0 &&
                            Double.parseDouble(txtSpecimenBValueRectangle.getText())!=0){
                        txtSpecimenCrossSectionCalcule.setText(String.format("%.2f",Double.parseDouble(txtSpecimenAValueRectangle.getText()) *
                                Double.parseDouble(txtSpecimenBValueRectangle.getText())));
                    }
                });
            }
        });
        txtSpecimenBValueRectangle.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Platform.runLater(()->{
                    txtSpecimenCrossSectionCalcule.setText("1.00");
                    if(Double.parseDouble(txtSpecimenAValueRectangle.getText())!=0 &&
                            Double.parseDouble(txtSpecimenBValueRectangle.getText())!=0){
                        txtSpecimenCrossSectionCalcule.setText(String.format("%.2f",Double.parseDouble(txtSpecimenAValueRectangle.getText()) *
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
                    txtSpecimenCrossSectionCalcule.setText("1.00");
                    if(Double.parseDouble(txtSpecimenAValueCylinder.getText())!=0 &&
                            Double.parseDouble(txtSpecimenBValueCylinder.getText())!=0){
                        txtSpecimenCrossSectionCalcule.setText(String.format("%.2f",Double.parseDouble(txtSpecimenAValueCylinder.getText()) *
                                Double.parseDouble(txtSpecimenBValueCylinder.getText())));
                    }
                });
            }
        });
        txtSpecimenBValueCylinder.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Platform.runLater(()->{
                    txtSpecimenCrossSectionCalcule.setText("1.00");
                    if(Double.parseDouble(txtSpecimenAValueCylinder.getText())!=0 &&
                            Double.parseDouble(txtSpecimenBValueCylinder.getText())!=0){
                        txtSpecimenCrossSectionCalcule.setText(String.format("%.2f",Double.parseDouble(txtSpecimenAValueCylinder.getText()) *
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
                        txtSpecimenCrossSectionCalcule.setText("1.00");
                    if(Double.parseDouble(txtSpecimenAValueCylinder.getText())==0){
                        txtSpecimenCrossSectionCalcule.setText(String.format("%.2f",Math.pow(Double.parseDouble(txtSpecimenValueTubular.getText()),2)));
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
                    txtSpecimenAValueRectangle.setText("1.00");
                    txtSpecimenBValueRectangle.setText("1.00");
                if(rbRectangle.isSelected()){
                    txtSpecimenAValueRectangle.setDisable(false);
                    txtSpecimenBValueRectangle.setDisable(false);
                } else{
                    txtSpecimenAValueRectangle.setDisable(true);
                    txtSpecimenBValueRectangle.setDisable(true);
                    txtSpecimenAValueRectangle.setText(null);
                    txtSpecimenBValueRectangle.setText(null);
                }
            }
        });
        // Desabilitar campos de edicao em amostras cilindricas se a opcao não estiver selecionada
        rbCylinder.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    txtSpecimenAValueCylinder.setText("1.00");
                    txtSpecimenBValueCylinder.setText("1.00");
                if(rbCylinder.isSelected()){
                    txtSpecimenAValueCylinder.setDisable(false);
                    txtSpecimenBValueCylinder.setDisable(false);
                } else{
                    txtSpecimenAValueCylinder.setDisable(true);
                    txtSpecimenBValueCylinder.setDisable(true);
                    txtSpecimenAValueCylinder.setText(null);
                    txtSpecimenBValueCylinder.setText(null);
                }
            }
        });
        // Desabilitar campos de edicao em amostras cilindricas se a opcao não estiver selecionada
        rbTubular.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    txtSpecimenValueTubular.setText("1.00");
                if(rbTubular.isSelected()){
                    txtSpecimenValueTubular.setDisable(false);
                } else{
                    txtSpecimenValueTubular.setDisable(true);
                    txtSpecimenValueTubular.setText(null);
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
        // Altera campo txtSpecimenReducedArea quando txtSpecimenCrossSectionArea for alterado
        txtSpecimenCrossSectionArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Platform.runLater(()->{
                    txtSpecimenReducedArea.setText(txtSpecimenCrossSectionArea.getText());
                });
            }
        });
        // Altera valor de Reducao de Area calculado quando txtSpecimenReducedArea é alterado
        txtSpecimenReducedArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //Identifica valor de Reducao de Area %
                Double specimenArea = Double.parseDouble(txtSpecimenCrossSectionArea.getText());
                Double reducedArea = Double.parseDouble(txtSpecimenReducedArea.getText());
                Platform.runLater(() -> {
                    // Update UI.
                    lbRedArea.setText(String.format("%.2f",((specimenArea-reducedArea)/specimenArea)*100));
                });
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
                    // Obtendo valor digital da forca
                    calculatedForce();
                    // Obtendo valor de pulsos da posicao
                    calculatedPosition();
                    // Obtendo informacao sobre qual celula de carga esta conectada (reflete no indice multiplicador
                    // de conversao de unidade de forca)
                    chargeCellRequest();
                    // Leitura do sinal de erro
                    errorRequest();
                    // Definicao do fator multiplicativo para conversao de unidade de forca em funcao da celula de carga
                    unitConvertion(currentChargeCell);

                    // Monitoramento do erro em tempo real e resposta imediata na interface
                    Platform.runLater(()->{
//                        errorIdentification();
                    });

                    // Atualização da UI pela Thread a partir das variáveis globais
                    Platform.runLater(() -> {
                        txtForceView.setText(String.format("%.5f", selectedUnitForceValue() * forceAdjustInversionView));
                        txtPositionView.setText(String.format("%.5f", selectedUnitPositionValue() * positionAdjustInversionView));
                    });
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void calculatedForce(){
            try{
                serialConn.forceRequest();  // Requerimento do valor da força
                Thread.sleep(0);
                // Conversao do valor digital
                currentBaseForce = Double.valueOf(serialConn.inputValue()) * chargeCellMultipFactor;
                taredCurrentForce = currentBaseForce - forceTare;
//                System.out.println("kgConversionForce: " + kgForceConversionFactor);
//                System.out.println("taredForce: " + taredCurrentForce);
//                System.out.println("newtonForce antes: " + currentNewtonForce);
//                System.out.println("kgForce antes: " + currentKgForce);
                currentNewtonForce = taredCurrentForce * kgForceConversionFactor * 9.78; // convertendo valor em Newton
                currentKgForce = taredCurrentForce * kgForceConversionFactor; // convertendo valor em Kgf
//                System.out.println("newtonForce depois: " + currentNewtonForce);
//                System.out.println("kgForce depois: " + currentKgForce);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        private void calculatedPosition(){
            try{
                serialConn.positionRequest();  // requerimento do valor da posição
                Thread.sleep(0);
                // conversão de pulsos em posicao (mm)
//                System.out.println("basePosition: " + currentBasePosition);
//                System.out.println("basePosition input: "+ Double.valueOf(serialConn.inputValue()));
                currentBasePosition = Double.valueOf(serialConn.inputValue()) * positionMultipFactor();
//                System.out.println("basePosition: " + currentBasePosition);
//                System.out.println("currentMmPosition antes: " + currentMmPosition);
//                System.out.println("currentPolPosition antes: " + currentPolPosition);
                currentMmPosition = currentBasePosition;
                currentPolPosition = currentBasePosition * 0.0393701; // conversao de mm para in
//                System.out.println("currentMmPosition depois: " + currentMmPosition);
//                System.out.println("currentPolPosition depois: " + currentPolPosition);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        private void chargeCellRequest(){
            try{
                serialConn.chargeCellRequest();
                Thread.sleep(0);
                currentChargeCell = Integer.valueOf(serialConn.inputValue());
//                System.out.println(currentChargeCell);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        private void errorRequest(){
            try{
                serialConn.errorRead();
                Thread.sleep(0);
                errorDecValue = Integer.valueOf(serialConn.inputValue());
//                System.out.println(errorDecValue);
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

            case 0: errorChargeCellDisconnected = true;
            case 1: kgForceConversionFactor = setup.getMC1M1() / 1000000000D; break;
            case 2: kgForceConversionFactor = setup.getMC2M1() / 1000000000D; break;
            case 3: kgForceConversionFactor = setup.getMC3M1() / 100000000D; break;
            case 4: kgForceConversionFactor = setup.getMC4M1() / 100000000D; break;
            case 5: kgForceConversionFactor = setup.getMC5M1() / 100000000D; break;
            case 6: kgForceConversionFactor = setup.getMC6M1() / 10000000D; break;
            case 7: kgForceConversionFactor = setup.getMC7M1() / 10000000D; break;
            case 8: kgForceConversionFactor = setup.getMC8M1() / 10000000D; break;
            case 9: kgForceConversionFactor = setup.getMC9M1() / 1000000D; break;
            case 10: kgForceConversionFactor = setup.getMC10M1() / 1000000D; break;
            case 11: kgForceConversionFactor = setup.getMC11M1() / 1000000D; break;
            case 12: kgForceConversionFactor = setup.getMC12M1() / 100000D; break;
            case 13: kgForceConversionFactor = setup.getMC13M1() / 100000D; break;
            case 14: kgForceConversionFactor = setup.getMC14M1() / 100000D; break;
            case 15: kgForceConversionFactor = setup.getMC15M1() / 100000D; break;
        }
    }

    /**
     * Metodo para determinar fator multiplicador para calcular posicao (mm)
     */
    private Double positionMultipFactor(){
        // Necessario explicitar as variaveis, uma vez que sua inclusao na expressao direto so setup.getMC pode gerar
        // inteiros truncados e refletir no resultado
        int mc19M1 = setup.getMC19M1();
        int mc20M1 = setup.getMC20M1();
        int mc21M1 = setup.getMC21M1();
        int mc22M1 = setup.getMC22M1();
        int mc17M1 = setup.getMC17M1();

        // ((Polia motor / (Polia fuso * Redutor)) * Passo fuso) / PPR  -> que será multiplicado pela leitura de pulsos
        double multFactor = ((double) mc19M1 / (mc20M1 * mc21M1)) * mc22M1 / mc17M1;
        return multFactor;
    }

    /**
     * Metodo para determinar fator multiplicador para calcular frequencia (mm/min)
     */
    private Double velocityMultipFactor(){
        // Frequencia max / Velocidade max  -> que sera multiplicado pela velocidade para determinar a frequencia velocidade
        Double multFactor = (double) (setup.getMC16M1()/setup.getMC18M1());
        return multFactor;
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
        // Erro - Botao de emergencia
        if(splitBin[0]==1){
            errorEmergencyButton = false;
            Platform.runLater(()->{

            });
        } else if(splitBin[0]==0){
            errorEmergencyButton = true;
            Platform.runLater(()->{

            });
        }
        // Erro - Fim do curso inferior
        if(splitBin[1]==1){
            errorInfLimit = false;
            Platform.runLater(()->{

            });
        } else if(splitBin[1]==0){
            errorInfLimit = true;
            Platform.runLater(()->{

            });
        }
        // Erro - Fim do curso superior
        if(splitBin[2]==1){
            errorSupLimit = false;
            Platform.runLater(()->{

            });
        } else if(splitBin[2]==0){
            errorSupLimit = true;
            Platform.runLater(()->{

            });
        }
        // Erro - Limite de celula de carga
        if(splitBin[3]==1){
            errorChargeCellLimit = false;
            Platform.runLater(()->{

            });
        } else if(splitBin[3]==0){
            errorChargeCellLimit = true;
            Platform.runLater(()->{

            });
        }
        // Erro - Celula de carga desconectada
        if(splitBin[4]==1){
            errorChargeCellDisconnected = false;
            Platform.runLater(()->{

            });
        } else if(splitBin[4]==0){
            errorChargeCellDisconnected = true;
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
     * Método de listagem de normas dentro do ComboBox (cbNormList)
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

            mYoung = 0D;
            var ref = new Object() {
                Double minDotT = 0D;
                Double maxDotT = 0D;
                Double minDotP = 0D;
                Double maxDotP = 0D;
            };


            final Double[] mYoungcount = {0D};
            Double adjustedForce = 0D;
            Double adjustedPosition = 0D;
            Double specimenArea = Double.valueOf(txtSpecimenCrossSectionArea.getText());
            Double parallelLineAvg[] = {0D,0D};

            if(forceAdjustInversion==-1){
                forceAdjustInversionView=-1;
                positionAdjustInversionView=-1;
            }

            List<Double> forceList = new ArrayList<>();
            List<Double> positionList = new ArrayList<>();
            List<Double> tensionList = new ArrayList<>();
            Double[][] mYoungList = new Double[10][2];
            List<Double> escTensionLineChart = new ArrayList<>();
            List<Double> escDeformLineChart = new ArrayList<>();
            List<Double> escTensionChartList = new ArrayList<>();
            List<Double> escDeformChartList = new ArrayList<>();

            try {

                while (autoBreak()==false) {
//                while(autoBreak()==false){
                    Thread.sleep(50);

                    // Aquisicao dos valores ja convertidos para a unidade selecionada e ajustados para ensaio para
                    // cima ou para baixo
                    adjustedForce = selectedUnitForceValue() * forceAdjustInversion;
                    adjustedPosition = selectedUnitPositionValue() * positionAdjustInversion;
                    Double currentDeform = (adjustedPosition - initialPosition) / initialPosition;
                    // Calculo da tensao
                    currentTension = adjustedForce / specimenArea;


                    forceList.add(adjustedForce);
                    positionList.add(adjustedPosition);
                    tensionList.add(currentTension);
                    if(ref.maxDotT == 0D){
                        for(int i = 1; i > mYoungList.length - 1; i++){
                            //mYoungList[i-1] = mYoungList[i];
                            // ou
                            mYoungList[i-1][0] = mYoungList[i][0];
                            mYoungList[i-1][1] = mYoungList[i][1];
                        }
                        mYoungList[mYoungList.length - 1][0] = adjustedForce;
                        mYoungList[mYoungList.length - 1][1] = adjustedPosition;
                    }


                    //Identifica valor de Forca Max
                    Platform.runLater(() -> {
                        // Update UI.
                        for(Double force : forceList) {
                            if (force > fMax) {
                                fMax = force;
                                lbFMax.setText(String.format("%.2f", Double.valueOf(fMax)));
                            }
                        }
                    });
                    //Identifica valor de Posicao Max
                    Platform.runLater(() -> {
                        // Update UI.
                         for(Double position : positionList) {
                             if (position > pMax) {
                                 pMax = position;
                                 lbPMax.setText(String.format("%.2f", Double.valueOf(pMax)));
                             }
                         }
                    });
                    //Identifica valor de Tensao Max MPa
                    Platform.runLater(() -> {
                        // Update UI.
                        for(Double tension : tensionList) {
                            if (tension > tMax) {
                                tMax = tension;
                                lbTMax.setText(String.format("%.2f", Double.valueOf(tMax)));
                            }
                        }
                    });
                    // Construcao da reta a partir do minDotT e maxDotT do M Young
                    Platform.runLater(()->{
                        if(ref.maxDotT !=0){
                            parallelLineAvg[0] = (ref.maxDotT - ref.minDotT) / mYoungcount.length; // media de incremento no eixo y por ponto no grafico
                            parallelLineAvg[1] = (ref.maxDotP - ref.minDotP) / mYoungcount.length; // media de incremento no eixo x por ponto no grafico
                            for(int i = 0; mYoungcount[0] * 2 > i; i++){
                                escTensionLineChart.add(ref.minDotT + parallelLineAvg[0]*i);
                                escDeformLineChart.add(ref.minDotP * 1.02 + parallelLineAvg[1]*i);
                            }
                        }
                    });
                    //Identifica valor de Tensao de escoamento MPa (ponto em comum das 4 da leitura das 4 Lists)
                    Platform.runLater(() -> {
                        // Update UI.
                        if(tEsc==0){
                            if(parallelLineAvg[0]!=0){
                                for(Double pl : escDeformLineChart){
                                    for(Double pc : escDeformChartList){
                                        for(Double tl : escTensionChartList){
                                            for(Double tc : escTensionLineChart){
                                                if(pl==pc && tl==tc){
                                                    tEsc = tc;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else{
                            lbTEsc.setText(String.format("%.2f", tEsc));
                        }
                    });
                    //Identifica valor de Alongamento %
                    Platform.runLater(() -> {
                        // Update UI.
                        lbAlong.setText(String.format("%.2f",((pMax-initialPosition)/initialPosition)*100));
                    });

                    //Identifica valor de M. Young MPa
                    Platform.runLater(() -> {
                        // Update UI.
                        Double prop;
                        Double sum = 0D;
                        Double currentProp;
                        if(tEsc==0){
                            escTensionChartList.add(currentTension); // Incluir currentDeformation
                            escDeformChartList.add(currentDeform);
                        }
                        if(forceList.size()>30){
                                currentProp = mYoungList[mYoungList.length - 1][0]/mYoungList[mYoungList.length - 1][1];
                            if(ref.minDotT == 0){
                                mYoungcount[0]++;
                                for (int i = mYoungList.length - 1; i < mYoungList.length; i--) {
                                    prop = mYoungList[i][0]/mYoungList[i][1];
                                    sum += prop;
                                }
                                Double avg = sum/mYoungList.length;
                                Double avgMaxLim = avg * 1.05;
                                Double avgMinLim = avg * 0.95;
                                if(avgMinLim <= currentProp || avgMaxLim >= currentProp){
                                    ref.minDotT = mYoungList[0][0];
                                    ref.minDotP = mYoungList[0][1];
                                }
                            } else if(ref.maxDotT == 0){
                                mYoungcount[0]++;
                                for (int i = mYoungList.length - 1; i < mYoungList.length; i--) {
                                    prop = mYoungList[i][0]/mYoungList[i][1];
                                    sum += prop;
                                }
                                Double avg = sum/mYoungList.length;
                                Double avgMaxLim = avg * 1.05;
                                Double avgMinLim = avg * 0.95;
                                if(avgMinLim >= currentProp || avgMaxLim <= currentProp){
                                    ref.maxDotT = mYoungList[0][0];
                                    ref.maxDotP = mYoungList[0][1];
                                }
                            }
                        }
                        lbMYoung.setText(String.format("%.4f", (ref.maxDotT - ref.minDotT)/(ref.maxDotP - ref.minDotP)));
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
                        if(rbForceXPosition.isSelected()){
                            series.getData().add(new XYChart.Data<>(finalAdjustedPosition, finalAdjustedForce));
                        } else if(rbStrainXDeform.isSelected()) {
                            series.getData().add(new XYChart.Data<>(currentDeform, currentTension));
                        }
                    });

                    // Adding dot values in a global String chartString
                    // essayChart String type: 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10
                    if (chartString != null) {
                        chartString += "," + String.format("%.4f", adjustedForce) + ";" + String.format("%.4f", adjustedPosition);
                    } else {
                        chartString = String.format("%.4f", adjustedForce) + ";" + String.format("%.4f", adjustedPosition);
                    }

                    // Adicionando pontos ao chartString global para tensao por deformacao
                    if (chartStringTensionXDeform != null) {
                        chartStringTensionXDeform += "," + String.format("%.4f", currentTension) + ";" + String.format("%.4f", currentDeform);
                    } else {
                        chartStringTensionXDeform = String.format("%.4f", currentTension) + ";" + String.format("%.4f", currentDeform);
                    }
                    System.out.println(chartString);
                    System.out.println(chartStringTensionXDeform);
                    count++;
                }
                essayfinished = true;
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

    /**
     * REQUER IMPLEMENTACAO CORRETA: Método que define a velocidade de ajuste
     */
    @FXML
    private synchronized void adjustVelocity(Integer value) {
        Double essayVel = value * velocityMultipFactor();
        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if (essayVel != null) {
            if(value >= 600){
                txtEssayVelocity.setText("600");
                serialConn.adjustVelocity(600);
            } else if(value >= 1){
                serialConn.adjustVelocity(value);
            } else {
                txtEssayVelocity.setText("1");
                serialConn.adjustVelocity(1);
            }
        } else {
            txtEssayVelocity.setText("1");
            serialConn.adjustVelocity(1);
        }
    }

    /**
     * REQUER IMPLEMENTACAO CORRETA: Método que define a velocidade de ensaio
     */
    @FXML
    private synchronized void essayVelocity(Integer value) {
        Double essayVel = value * velocityMultipFactor();
        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if (essayVel != null) {
            if(value >= 600){
                txtEssayVelocity.setText("600");
                serialConn.essayVelocity(600);
            } else if(value >= 1){
                serialConn.essayVelocity(value);
            } else {
                txtEssayVelocity.setText("1");
                serialConn.essayVelocity(1);
            }
        } else {
            txtEssayVelocity.setText("1");
            serialConn.essayVelocity(1);
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

        essayfinished = false;
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
        String selectedItem = cbForceUnitSelection.getSelectionModel().getSelectedItem().toString();

        if (selectedItem.equals("N")) {
            currentForceUnit = "N";
            return currentNewtonForce;
        } else if (selectedItem.equals("Kg")) {
            currentForceUnit = "Kg";
            return currentKgForce;
        }

        return null; // Caso nenhuma condição seja atendida, retorne null ou outro valor adequado.
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
        if(taredCurrentForce>2500 && pMax>0 ){
            if(rbForceDownBreak.isSelected()){
                if(selectedUnitForceValue() >= fMax * (100 - Double.valueOf(txtForcePercentageBreak.getText()))/100) {
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
                    systemDate = new Date();
                    String currentDay = brasilianDay.format(systemDate);
                    String currentHour = brasilianHour.format(systemDate);
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
        if(essayfinished = true){
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
                systemDate = new Date();
                String currentDay = brasilianDay.format(systemDate);
                String currentHour = brasilianHour.format(systemDate);
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
        } else{
            // Alerta de ensaio nao finalizado corretamente
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Erro de ensaio");
            alert.setHeaderText("O ensaio não foi finalizado corretamente! Descarte o ensaio e tente novamente.");
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
        txtEssayVelocity.setText("1");
//        essayVelocity(1);
        txtAdjustVelocity.setText("1");
//        adjustVelocity(1);
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
        // Personalizacao da imagem do usuario
        if(user.getUserImagePath()!=null){
            ivEssayUser.setImage(new Image(user.getUserImagePath()));
        } else{
            ivEssayUser.setImage(new Image(mBioLabv2Application.class.getResource("img/lightIcon/user.png").toExternalForm()));
        }
    }

    /**
     * Metodo que limpa toda a area do grafico do ensaio
     */
    @FXML
    private void newChart(){
        chartString = null;
        chartStringTensionXDeform = null;
        chartEssayLine.getData().clear();
        series.getData().clear();
    }

    // INICIO*********** Métodos de realização do Ensaio ***********
}


/*
 *  Copyright (c) 2023.
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
