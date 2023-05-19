package br.com.biopdi.mbiolabv2.controller.SceneController;


import br.com.biopdi.mbiolabv2.controller.SceneController.switchScene.FxmlLoader;
import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import br.com.biopdi.mbiolabv2.model.bean.Method;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.PrintWriter;
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
    private Essay essayFinalyzed;
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final MethodDAO methodDAO = new MethodDAO();
    private final SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private final SystemVariableDAO systemVariableDAO = new SystemVariableDAO();
    private SystemVariable sysVar = systemVariableDAO.find();


    // Puxar dados do DB systemVariable salvas na Thread de leitura FPReadingThread()
    @FXML
    private ComboBox cbMethodList, cbNormList, cbEssayType;
    @FXML
    private LineChart<Number, Number> chartEssayLine;
    private XYChart.Series<Number, Number> series;
    @FXML
    private Label lbFMax, lbPMax, lbTMax, lbTEsc, lbAlong, lbRedArea, lbMYoung, lbEssayTemperature, lbEssayRelativeHumidity;
    @FXML
    private TextField txtConnected, txtForceView, txtPositionView, txtEssayVelocity, txtAdjustVelocity,
            txtEssayIdentification, txtEssayNorm, txtUsedMachine, txtEssayChargeCell, txtInitialForce,
            txtFinalForce, txtInitialPosition, txtFinalPosition, txtDislocationVelocity, txtEssayPreCharge,
            txtMaxForceBreak, txtDislocationValueBreak, txtDislocationValuePause, txtForcePercentageBreak;
    @FXML
    private Button btnAdjustVelocity, btnPositionUp, btnPositionDown, btnStart, btnPause, btnStop, btnChargeMethod, btnEssayByUserId, btnEssaySave;
    @FXML
    private RadioButton rbForceXPosition, rbStrainXDeform, rbForceDownBreak, rbMaxForceBreak, rbDislocationBreak, rbDislocationPause;
    private SerialPort port;

    private Double currentForce = 0D, currentPosition = 0D, initialForce, finalForce, initialPosition, finalPosition, fMax, pMax,
            tMax, tEsc, along, redArea, mYoung;
    private String chartString = null;
    private Boolean moving = false;
    private int autoBreakCount;
    @FXML
    private AnchorPane apAlert;


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
        autoConnect();
        savedMethodList();

    }

    /**
     * Classe que define o algorítmo da Thread que faz a leitura dinâmica da Força e da Posição
     */
    class ForcePositionReader implements Runnable {
//        SystemVariableDAO systemVariableDAO = new SystemVariableDAO();
        @FXML
        @Override
        public synchronized void run() {

            try {
                Thread.sleep(1000);
                while (true) {
                    outputInjection("1");  // Requerimento do valor da força
                    Thread.sleep(0);
                    currentForce = Double.valueOf(inputValue());
                    outputInjection("2");  // requerimento do valor da posição
                    Thread.sleep(0);
                    currentPosition = Double.valueOf(inputValue());

                    // Salvando dados no banco de dados - dados persistentes como variável global
//                    SystemVariable sysVar = new SystemVariable(1, currentForce, currentPosition);
//                    systemVariableDAO.updateEssay(sysVar);

                    //Atualização da UI pela Thread a partir das variáveis globais
                    Platform.runLater(() -> {
                        txtForceView.setText(String.format("%.2f", currentForce));
                    });
                    Platform.runLater(() -> {
                        txtPositionView.setText(String.format("%.2f", currentPosition));
                    });
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Método de conexão automática, buscando o portName do systemSetting no DB
     */
    @FXML
    private synchronized void autoConnect() {

        // Instance to get an object SystemParameter from tb_systemParameter (single line)
        SystemParameter sysPar = systemParameterDAO.find();
        // portName receives portName from systemParameterDAO
        port = SerialPort.getCommPort(sysPar.getPortName());
        System.out.println("Conectado à porta: " + sysPar.getPortName() + " - " + port);
        if (port.openPort()) {
            txtConnected.setText("Conectado");
            txtConnected.setStyle("-fx-background-color: #06BC0E");
            port.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 50, 50);
            port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

            // Thread to update the Força and Posição textLabel from GUI
            Thread FPReader = new Thread(new ForcePositionReader());
            FPReader.start();

        } else {
            port.closePort();
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
        //SETTAR VALORES NAS TEXTFIELD
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
        String[] essayTypes = new String[]{"Compressão", "Fadiga", "Flexão", "Tração"};
        for (String type : essayTypes) {
            cbEssayType.getItems().add(type);
        }
    }

//    ************* Chart Construction *****************

    /**
     * MODIFICAR CÓDIGO (TROCAR COUNT POR CONTIÇÔES) >> Método que cria, em tempo real, o gráfico do ensaio.
     * Nele, podemos implementar switch (para alteração dos parâmetros N/mm ou MPa/%)
     */
    class RTChartCreate implements Runnable {
//        SystemVariableDAO systemVariableDAO = new SystemVariableDAO();
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

            List<Double> forceList = new ArrayList<>();
            List<Double> positionList = new ArrayList<>();

            try {
                while (count<30) {
//                while(autoBreak()==false){
                    Thread.sleep(50);

                    // Armazenando Forca e Posicao (variaveis globais) em arrays
                    if(essayTypeMove()==1){
                        forceList.add(currentForce);
                        positionList.add(currentPosition);
                    } else if (essayTypeMove()==-1){
                        forceList.add(currentForce * -1);
                        positionList.add(currentPosition * -1);
                    }

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

                    System.out.println(currentForce + " " + currentPosition);
                    // Exposicao dos valores no grafico em funcao da escolha do usuario MPa x % ou N x mm
                    // INCLUIR FORMULA DE CONVERSAO DOS VALORES
                    Platform.runLater(() -> {
                        // Update UI.
                        series.getData().add(new XYChart.Data<>(currentPosition, currentForce));
                    });

                    // Adding dot values in a global String chartString
                    // essayChart String type: 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10
                    if (chartString != null) {
                        chartString += "," + currentForce + ";" + currentPosition;
                    } else {
                        chartString = currentForce + ";" + currentPosition;
                    }
                    System.out.println(chartString);
                    count++;
                }
                stopMove();
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
     * Método genérico para injeção do output, aplicável para os diferentes processos port.OutputStream() que requeiram uma 'string'
     *
     * @param stg
     */
    private synchronized void outputInjection(String stg) {
        PrintWriter output = new PrintWriter(port.getOutputStream(), true);
        output.print(stg);
        output.flush();
    }

    /**
     * Método que recebe o valor real do input
     *
     * @return String
     */
    private synchronized String inputValue() {
        Scanner s = new Scanner(port.getInputStream());
        return s.nextLine();
    }

    // INICIO*********** Métodos pré ensaio ***********

    /**
     * Método que zera a posição (injeção de '0')
     */
    @FXML
    private synchronized void resetPosition() {
        outputInjection("0");
    }

    /**
     * Método que solicita o valor da força (injeção de '1')
     */
    @FXML
    private synchronized void forceRequest() throws InterruptedException {
        outputInjection("1");
    }

    /**
     * Método que solicita o valor da posição (injeção de '2')
     */
    @FXML
    private synchronized void positionRequest() throws InterruptedException {
        outputInjection("2");
    }

    // FIM*********** Métodos pré ensaio ***********

    // INICIO*********** Métodos de Movimento ***********

    /**
     * Método que solicita interrupção do movimento (injeção de '3')
     */
    @FXML
    private synchronized void stopMove() {
        outputInjection("3");
    }

    /**
     * Método que solicita movimento de ajuste para cima (injeção de '4')
     */
    @FXML
    private synchronized void moveUp() {
        outputInjection("4");
        System.out.println("Moveu CIMA");
    }

    /**
     * Método que solicita movimento de ajuste para baixo (injeção de '5')
     */
    @FXML
    private synchronized void moveDown() {
        outputInjection("5");
        System.out.println("Moveu BAIXO");
    }

    /**
     * Método que solicita movimento de ensaio para cima (injeção de '6')
     */
    @FXML
    private synchronized void moveUpAssay() {
        outputInjection("6");
    }

    /**
     * Método que solicita movimento de ensaio para baixo (injeção de '7')
     */
    @FXML
    private synchronized void moveDownAssay() {
        outputInjection("7");
    }

    // FIM*********** Métodos de Movimento ***********

    // INICIO*********** Métodos Ajuste de Velocidade ***********

    /**
     * Método que define a velocidade de ajuste (injeção de '8')
     */
    @FXML
    private synchronized void adjustVelocity() {
        int value = Integer.parseInt(txtAdjustVelocity.getText());
        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if (txtAdjustVelocity.getText() != null) {
            if(value >= 40000){
                txtAdjustVelocity.setText("40000");
                outputInjection("840000");
            } else if(value >= 15000){
                String adjust = 8 + txtAdjustVelocity.getText();
                outputInjection(adjust);
            } else {
                txtAdjustVelocity.setText("15000");
                outputInjection("815000");
            }
        } else {
            txtAdjustVelocity.setText("15000");
            outputInjection("815000");
        }
    }

    /**
     * Método que define a velocidade de ensaio (injeção de '9')
     */
    @FXML
    private synchronized void essayVelocity() {
        int value = Integer.parseInt(txtEssayVelocity.getText());
        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if (txtEssayVelocity.getText() != null) {
            if(value >= 40000){
                txtEssayVelocity.setText("40000");
                outputInjection("940000");
            } else if(value >= 15000){
                String adjust = 9 + txtEssayVelocity.getText();
                outputInjection(adjust);
            } else {
                txtEssayVelocity.setText("15000");
                outputInjection("915000");
            }
        } else {
            txtEssayVelocity.setText("15000");
            outputInjection("915000");
        }
    }

    // FIM*********** Métodos Ajuste de Velocidade ***********

    // INICIO*********** Métodos de realização do Ensaio ***********


///////////////////////////////////
    @FXML
    private synchronized void test() throws InterruptedException {



        // Construcao do grafico, da serie e inclusao de parametros do grafico
        chartEssayLine.getData().clear();
        series = new XYChart.Series<>();
        series.setName("Leitura");
        chartEssayLine.getData().add(series);

        // Aquisicao de valores iniciais de Forca e Posicao
        initialForce = currentForce;
        initialPosition = currentPosition;


        //indica que eixo está em movimento
        moving = true;

        // Thread que atualiza os valores no grafico
        Thread chartThread = new Thread(new RTChartCreate());
        Platform.runLater(() -> {
            // Inicia o movimento
            essayTypeMove();
            chartThread.start();
        });

        // Setting final values
        finalForce = currentForce;
        finalPosition = currentPosition;

    }

    @FXML
    private synchronized void testRecover() throws InterruptedException {

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
        finalForce = currentForce;
        finalPosition = currentPosition;

    }
///////////////////////////////////

    /**
     * Método que inicia o ensaio
     *
     * @throws InterruptedException
     */
    @FXML
    private synchronized void essayStart() throws InterruptedException {

        //Aquisicao dos parametros inseridos pelo usuario
        try{
            essayVelocity();
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
        series.setName("Leitura");
        chartEssayLine.getData().add(series);

        // Aquisicao de valores iniciais de Forca e Posicao
        initialForce = currentForce;
        initialPosition = currentPosition;

        // Thread que atualiza os valores no grafico
        Thread chartThread = new Thread(new RTChartCreate());
        chartThread.start();
        chartThread.join();


        // Setting final values
        finalForce = currentForce;
        finalPosition = currentPosition;


    }

    /**
     * Metodo que define direcao do movimento do eixo, baseado no tipo de ensaio
     */
    private int essayTypeMove(){
        if(cbEssayType.getSelectionModel().getSelectedItem().toString()=="Tração"){
            moveUp();
            return 1;
        } else if(cbEssayType.getSelectionModel().getSelectedItem().toString()=="Compressão" ||
                cbEssayType.getSelectionModel().getSelectedItem().toString()=="Flexão"){
            moveDown();
            return -1;
        }
        return 0;
    }

    /**
     * Metodo de interrupção automatica do ensaio
     */
    @FXML
    private boolean autoBreak() {
        if(fMax>0 && pMax>0 ){
            if(rbForceDownBreak.isSelected()){
                while(currentForce >= fMax * (100 - Double.valueOf(String.valueOf(txtForcePercentageBreak)))/100) {
                    return false;
                }
                return true;

            } else if(rbMaxForceBreak.isSelected()){
                while(currentForce < Double.parseDouble(txtMaxForceBreak.getText())){
                    return false;
                }
                return true;

            } else if(rbDislocationBreak.isSelected()){
                while(currentPosition < Double.parseDouble(txtDislocationValueBreak.getText())){
                    return false;
                }
                return true;

            } else if(rbDislocationPause.isSelected()){
                while(currentPosition < Double.parseDouble(txtDislocationValuePause.getText())){
                    return false;
                }
                return true;
            }
            stopMove();
        }
        return false;
    }

    /**
     * REQUER IMPLAMTANÇÃO >> Método que pausa se o eixo estiver em movimento e retoma se estiver parado
     */
    @FXML
    private void essayPause() throws InterruptedException {
        if (moving == true) {
            stopMove();
            moving = false;
            btnPause.setText("Retomar");
        } else {
            moving = true;
            btnPause.setText("Pause");
            testRecover();
        }
    }

    /**
     * Método que pára o ensaio
     */
    @FXML
    private void essayStop() {
        if (moving == true) {
            stopMove();
            moving = false;
        } else {
            System.out.println("O ensaio não foi iniciado!");
        }
    }

    /**
     * Método que cria um essay e salva no DB
     */
    @FXML
    public void essaySave(ActionEvent event) throws IOException {
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
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro de Ensaio");
            alert.setHeaderText("Ensaio salvo com sucesso!");
            alert.initOwner(new Stage());
            alert.show();
        });


    }

    /**
     * Método que descarta as infomrações coletadas no ensaio realizado
     */
    @FXML
    public void essayDiscart() {
        essayFinalyzed = null;
        System.out.println(essayFinalyzed);
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
