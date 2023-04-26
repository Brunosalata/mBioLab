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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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


    // Puxar dados do DB systemVariable salvas na Thread de leitura FPReadingThread()
    @FXML
    private ComboBox cbMethodList;
    //    @FXML
//    private NumberAxis xEssayChart, yEssayChart, xChartSingle, yChartSingle,xChartMulti, yChartMulti;
    @FXML
    private LineChart<Number, Number> chartEssayLine;
    private XYChart.Series<Number, Number> series;
    @FXML
    private TextField txtConnected, txtForceView, txtPositionView, txtAdjustVelocity, txtEssayVelocity, txtEssayUserId, txtLed;
    @FXML
    private Label lbEssayTemperature, lbEssayRelativeHumidity;
    @FXML
    private TextField txtEssayIdentification, txtEssayNorm, txtUsedMachine, txtEssayChargeCell, txtInitialForce, txtFinalForce, txtInitialPosition, txtFinalPosition, txtDislocationVelocity, txtEssayPreCharge;
    @FXML
    private Button btnStart, btnPause, btnStop, btnChargeMethod, btnEssayByUserId, btnEssaySave;
    private SerialPort port;

    private Double initialForce, finalForce, initialPosition, finalPosition;
    private String chartString = null;
    private Boolean moving = false;


    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        autoConnect();
        savedMethodList();


    }


    /**
     * Classe que define o algorítmo da Thread que faz a leitura dinâmica da Força e da Posição
     */
    class SystemVariableReader implements Runnable {
        SystemVariableDAO systemVariableDAO = new SystemVariableDAO();

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                while (true) {
                    outputInjection("1");  // Requerimento do valor da força
                    Thread.sleep(13);
                    String impF = inputValue();
                    outputInjection("2");  // requerimento do valor da posição
                    Thread.sleep(13);
                    String impP = inputValue();

                    // Salvando dados no banco de dados - dados persistentes como variável global
                    SystemVariable sysVar = new SystemVariable(1, Double.valueOf(impF), Double.valueOf(impP));
                    systemVariableDAO.update(sysVar);

                    //Atualização da UI pela Thread a partir dos dados salvos no DB
                    Platform.runLater(() -> {
                        txtForceView.setText(String.format("%.2f", Double.valueOf(impF)));
                        txtPositionView.setText(String.format("%.2f", Double.valueOf(impP)));
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
    private void autoConnect() {

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
            Thread sysVarThread = new Thread(new SystemVariableReader());
            sysVarThread.start();

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
    private Method chargeMethod() {
        Method method = new MethodDAO().findByMethod(cbMethodList.getSelectionModel().getSelectedItem().toString());
        System.out.println(method);
        return method;
    }


//    ************* Chart Construction *****************

    /**
     * MODIFICAR CÓDIGO (TROCAR COUNT POR CONTIÇÔES) >> Método que cria, em tempo real, o gráfico do ensaio. Nele, podemos implementar switch (para alteração dos parâmetros N/mm ou MPa/%)
     */
    class RTChartCreate implements Runnable {
        SystemVariableDAO systemVariableDAO = new SystemVariableDAO();
        int count = 0;

        @Override
        public void run() {
            try {
                while (count < 10) {

                    Thread.sleep(50);
                    SystemVariable sysVar = systemVariableDAO.find();
                    double x = sysVar.getForce();
                    double y = sysVar.getPosition();
                    System.out.println(x + " " + y);
                    // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                    Platform.runLater(() -> {
                        // Update UI.
                        series.getData().add(new XYChart.Data<>(x, y));
                    });

                    // Adding dot values in a global String chartString
                    // essayChart String type: 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10
                    if (chartString != null) {
                        chartString += "," + x + ";" + y;
                    } else {
                        chartString = x + ";" + y;
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
//        txtForceView.setText(inputValue());
        //incluir tara para força
    }

    /**
     * Método que solicita o valor da posição (injeção de '2')
     */
    @FXML
    private void positionRequest() throws InterruptedException {
        outputInjection("2");
        Thread.sleep(20);
//        txtPositionView.setText(inputValue());
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
        moving = true;
        // Setting initial values
        SystemVariable sysVarInit = systemVariableDAO.find();
        initialForce = sysVarInit.getForce();
        initialPosition = sysVarInit.getPosition();

        // Essay Chart construction and parameters
        chartEssayLine.getData().clear();
        series = new XYChart.Series<>();
        series.setName("Leitura");
        chartEssayLine.getData().add(series);

        // Thread to update the series on chart
        Thread chartThread = new Thread(new RTChartCreate());
        chartThread.start();
        chartThread.join();

        // Setting final values
        SystemVariable sysVarFinal = systemVariableDAO.find();
        finalForce = sysVarFinal.getForce();
        finalPosition = sysVarFinal.getPosition();


        essayExp = new Essay();
        essayExp.setUserId(1); // Substituir por ID referente ao login
        essayExp.setEssayIdentification("Teste botão Iniciar");
        essayExp.setEssayNorm("Norma Padrão");
        essayExp.setEssayUsedMachine("Máquina do Tempo");
        essayExp.setEssayChargeCell(0.300);
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

        // Create essay to call essaySave method inserting this object essay as parameter
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
     * REQUER IMPLAMTANÇÃO >> Método que pausa se o eixo estiver em movimento e retoma se estiver parado
     */
    @FXML
    private void essayPause() {
        if (moving == true) {
            stopMove();
            moving = false;
        } else {
            System.out.println("Implementar retorno do movimento cima ou baixo");
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
        essayDAO.create(essayExp);
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