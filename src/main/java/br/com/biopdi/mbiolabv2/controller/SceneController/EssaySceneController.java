package br.com.biopdi.mbiolabv2.controller.SceneController;


import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import br.com.biopdi.mbiolabv2.model.bean.Method;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class EssaySceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private Essay essayExp;
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
    private Button btnStart, btnPause, btnStop, btnChargeMethod, btnEssayByUserId, btnEssaySave;
    @FXML
    private RadioButton rbForceXPosition, rbStrainXDeform, rbForceDownBreak, rbMaxForceBreak, rbDislocationBreak, rbDislocationPause;
    private SerialPort port;

    private Double currentForce, currentPosition, initialForce, finalForce, initialPosition, finalPosition, fMax, pMax,
            tMax, tEsc, along, redArea, mYoung;
    private String chartString = null;
    private Boolean moving = false;
    private int autoBreakCount;


    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

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

            List<Double> forceList = new ArrayList<>();
            List<Double> positionList = new ArrayList<>();

            try {
                while (count < 100) {

                    Thread.sleep(0);
                    // Armazenando Forca e Posicao (variaveis globais) em arrays
                    forceList.add(currentForce);
                    positionList.add(currentPosition);

                    //Identifica valor de Forca Max N
                    Platform.runLater(() -> {
                        // Update UI.
                        try {
                            Thread.sleep(15);
                            for(Double force : forceList){
                                if(force>fMax){
                                    fMax=force;
                                }
                                lbFMax.setText(String.format("%.2f", Double.valueOf(fMax)));
                                System.out.println(forceList);
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    });
                    //Identifica valor de Posicao Max mm
                    Platform.runLater(() -> {
                        // Update UI.
                        try {
                            Thread.sleep(15);
                             for(Double position : positionList) {
                                 if (position > pMax) {
                                     pMax = position;
                                 }
                                 lbPMax.setText(String.format("%.2f", Double.valueOf(pMax)));
                                 System.out.println(positionList);
                             }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    //Identifica valor de Tensao Max MPa
                    Platform.runLater(() -> {
                        // Update UI.
                        // condição
                        try {
                            Thread.sleep(15);
                            for (int i = 0; i < count; i++) {
                                lbTMax.setText(String.valueOf(i));
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    //Identifica valor de Tensao de escoamento MPa
                    Platform.runLater(() -> {
                        // Update UI.
                        // condição
                        try {
                            Thread.sleep(15);
                            for (int i = 0; i < count; i++) {
                                lbTEsc.setText(String.valueOf(i));
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    //Identifica valor de Alongamento %
                    Platform.runLater(() -> {
                        // Update UI.
                        // condição
                        try {
                            Thread.sleep(15);
                            for (int i = 0; i < count; i++) {
                                lbAlong.setText(String.valueOf(i));
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    //Identifica valor de Reducao de Area %
                    Platform.runLater(() -> {
                        // Update UI.
                        // condição
                        try {
                            Thread.sleep(15);
                            for (int i = 0; i < count; i++) {
                                lbRedArea.setText(String.valueOf(i));
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    //Identifica valor de M. Young MPa
                    Platform.runLater(() -> {
                        // Update UI.
                        // condição
                        try {
                            Thread.sleep(15);
                            for (int i = 0; i < count; i++) {
                                lbMYoung.setText(String.valueOf(i));
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    System.out.println(currentForce + " " + currentPosition);
                    // Exposicao dos valores no grafico em funcao da escolha do usuario MPa x % ou N x mm
                    // INCLUIR FORMULA DE CONVERSAO DOS VALORES
                    Platform.runLater(() -> {
                        // Update UI.
                        try {
                            Thread.sleep(15);
                            if (rbForceXPosition.isSelected()) {
                                series.getData().add(new XYChart.Data<>(currentForce, currentPosition));
                            } else if (rbStrainXDeform.isSelected()) {
                                double nf = currentForce * 1000;
                                double np = currentPosition * 1000;
                                series.getData().add(new XYChart.Data<>(nf, np));
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
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
    private void outputInjection(String stg) {
        PrintWriter output = new PrintWriter(port.getOutputStream(), true);
        output.print(stg);
        output.flush();
    }

    /**
     * Método que recebe o valor real do input
     *
     * @return String
     */
    private String inputValue() {
        Scanner s = new Scanner(port.getInputStream());
        return s.nextLine();
    }

    // INICIO*********** Métodos pré ensaio ***********

    /**
     * Método que zera a posição (injeção de '0')
     */
    @FXML
    private void resetPosition() {
        outputInjection("0");
    }

    /**
     * Método que solicita o valor da força (injeção de '1')
     */
    @FXML
    private void forceRequest() throws InterruptedException {
        outputInjection("1");
        Thread.sleep(20);

        //incluir tara para força
    }

    /**
     * Método que solicita o valor da posição (injeção de '2')
     */
    @FXML
    private void positionRequest() throws InterruptedException {
        outputInjection("2");
        Thread.sleep(20);

        //incluir validação de real movimento do eixo
    }

    // FIM*********** Métodos pré ensaio ***********

    // INICIO*********** Métodos de Movimento ***********

    /**
     * Método que solicita interrupção do movimento (injeção de '3')
     */
    @FXML
    private void stopMove() {
        outputInjection("3");
    }

    /**
     * Método que solicita movimento de ajuste para cima (injeção de '4')
     */
    @FXML
    private void moveUp() {
        outputInjection("4");
    }

    /**
     * Método que solicita movimento de ajuste para baixo (injeção de '5')
     */
    @FXML
    private void moveDown() {
        outputInjection("5");
    }

    /**
     * Método que solicita movimento de ensaio para cima (injeção de '6')
     */
    @FXML
    private void moveUpAssay() {
        outputInjection("6");
    }

    /**
     * Método que solicita movimento de ensaio para baixo (injeção de '7')
     */
    @FXML
    private void moveDownAssay() {
        outputInjection("7");
    }

    /**
     * Método que solicita velocidade de ajuste (injeção de '8')
     */
    @FXML
    private void adjustVelocity() {
        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if (txtAdjustVelocity.getText() != null) {
            String adjust = 8 + txtAdjustVelocity.getText();
            outputInjection(adjust);
        } else {
            txtAdjustVelocity.setText("15000");
            outputInjection("815000");
        }

    }

    /**
     * Método que solicita velocidade de ensaio (injeção de '9')
     */
    @FXML
    private void essayVelocity() {
        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if (txtEssayVelocity.getText() != null) {
            String adjust = 9 + txtEssayVelocity.getText();
            outputInjection(adjust);
        } else {
            txtEssayVelocity.setText("15000.00");
            outputInjection("915000");
        }
    }

    /**
     * Método que inicia o ensaio
     *
     * @throws InterruptedException
     */
    @FXML
    private void essayStart() throws InterruptedException {

        try{
            essayVelocity();
            Thread.sleep(15);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

            //Comando para subir ou descer o eixo

        if(cbEssayType.getSelectionModel().getSelectedItem().toString()=="Tração"){

        } else if(cbEssayType.getSelectionModel().getSelectedItem().toString()=="Compressão" ||
        cbEssayType.getSelectionModel().getSelectedItem().toString()=="Flexão"){

        }
        //SE PRA BAIXO, ESSAY DOWN
        moving = true;

        // Essay Chart construction and parameters
        chartEssayLine.getData().clear();
        series = new XYChart.Series<>();
        series.setName("Leitura");
        chartEssayLine.getData().add(series);

        // Setting inicial values
        initialForce = currentForce;
        initialPosition = currentPosition;

        // Thread to update the series on chart
        Thread chartThread = new Thread(new RTChartCreate());
        chartThread.start();
        chartThread.join();




        // Setting final values
        finalForce = currentForce;
        finalPosition = currentPosition;


        essayExp = new Essay();
        essayExp.setUserId(4); // Substituir por ID referente ao login
        essayExp.setEssayIdentification("Teste Ensaio");
        essayExp.setEssayNorm("null");
        essayExp.setEssayUsedMachine("mBio Portátil");
        essayExp.setEssayChargeCell(0.500);
        essayExp.setEssayInitialForce(initialForce);
        essayExp.setEssayFinalForce(finalForce);
        essayExp.setEssayInitialPosition(initialPosition);
        essayExp.setEssayFinalPosition(finalPosition);
        essayExp.setEssayDislocationVelocity(15000);
        essayExp.setEssayTemperature(35.0);
        essayExp.setEssayPreCharge(0.0);
        essayExp.setEssayRelativeHumidity(40);
        essayExp.setEssayChart(chartString);
        essayExp.setEssayDate(currentDate);
        System.out.println(essayExp);

//        // Create essay to call essaySave method inserting this object essay as parameter
//        essayExp = new Essay();
//        essayExp.setUserId(1); // Substituir por ID referente ao login
//        essayExp.setEssayIdentification(txtEssayIdentification.getText());
//        essayExp.setEssayNorm(txtEssayNorm.getText());
//        essayExp.setEssayUsedMachine(txtUsedMachine.getText());
//        essayExp.setEssayChargeCell(Double.parseDouble(txtEssayChargeCell.getText()));
//        essayExp.setEssayInitialForce(initialForce);
//        essayExp.setEssayFinalForce(finalForce);
//        essayExp.setEssayInitialPosition(initialPosition);
//        essayExp.setEssayFinalPosition(finalPosition);
//        essayExp.setEssayDislocationVelocity(Double.parseDouble(txtDislocationVelocity.getText()));
//        essayExp.setEssayTemperature(Double.parseDouble(lbEssayTemperature.getText()));
//        essayExp.setEssayPreCharge(Double.parseDouble(txtEssayPreCharge.getText()));
//        essayExp.setEssayRelativeHumidity(Double.parseDouble(lbEssayRelativeHumidity.getText()));
//        essayExp.setEssayChart(chartString);
//        essayExp.setEssayDate(currentDate);
//        System.out.println(essayExp);
    }

    /**
     * Metodo de interrupção automatica do ensaio
     */
    private boolean autoBreak(){
        if(fMax>0 && pMax>0 ){
            if(rbForceDownBreak.isSelected()){
                if(currentForce <= fMax - fMax*(Double.valueOf(String.valueOf(txtForcePercentageBreak)))/100){
                    stopMove();
                    return true;
                };
            } else if(rbMaxForceBreak.isSelected()){
                Double forceLimit = Double.valueOf(String.valueOf(txtMaxForceBreak));
                if(currentForce >= forceLimit){
                    stopMove();
                    return true;
                }
            } else if(rbDislocationBreak.isSelected()){
                Double maxPosition = Double.valueOf(String.valueOf(txtDislocationValueBreak));
                if(currentPosition >= maxPosition){
                    stopMove();
                    return true;
                }
            } else if(rbDislocationPause.isSelected()){
                Double maxPosition = Double.valueOf(String.valueOf(txtDislocationValueBreak));
                if(currentPosition >= maxPosition){
                    essayPause();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * REQUER IMPLAMTANÇÃO >> Método que pausa se o eixo estiver em movimento e retoma se estiver parado
     */
    @FXML
    private void essayPause() {
        if (moving == true) {
            stopMove();
            moving = false;
            btnPause.setText("Retomar");
        } else {
            moving = true;
            if(cbEssayType.getSelectionModel().getSelectedItem().toString()=="Tração"){
                outputInjection("4");
            } else if(cbEssayType.getSelectionModel().getSelectedItem().toString()=="Compressão" ||
                    cbEssayType.getSelectionModel().getSelectedItem().toString()=="Flexão"){
                outputInjection("5");
            }
            btnPause.setText("Pause");
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
    public void essaySave() {
        if(sysVar.getUserId()==3){
            essayExp.setEssayId(1);
            essayDAO.update(essayExp);
        } else if(sysVar.getUserId()>3){
            essayDAO.create(essayExp);
        }
        System.out.println(essayExp);
    }

    /**
     * Método que descarta as infomrações coletadas no ensaio realizado
     */
    @FXML
    public void essayDiscart() {
        essayExp = null;
        System.out.println(essayExp);
    }
    // FIM*********** Métodos de Movimento ***********


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
//  }