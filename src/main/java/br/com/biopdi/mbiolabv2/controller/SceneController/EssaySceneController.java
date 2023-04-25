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
import javafx.scene.input.MouseEvent;

import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class EssaySceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
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
    private LineChart<Number, Number> chartEssayLine, chartSingleLine, chartMultiLine;
    private XYChart.Series<Number, Number> series;
    @FXML
    private TextField txtConnected, txtForceView, txtPositionView, txtAdjustVelocity, txtEssayVelocity, txtEssayUserId, txtLed;
    @FXML
    private Label lbCurrentData, lbUserId, lbUserName, lbUserLogin, lbUserPassword;
    @FXML
    private Button btnStart, btnPause, btnStop, btnChargeMethod, btnEssayByUserId;
    private SerialPort port;




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
//            System.out.println(impF);
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
     * Método de conexão automática
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
     * MODIFICAR CÓDIGO >> Método que cria, em tempo real, o gráfico do ensaio. Nele, podemos implementar switch (para alteração dos parâmetros N/mm ou MPa/%)
     */
    class RTChartCreate implements Runnable{
        SystemVariableDAO systemVariableDAO = new SystemVariableDAO();
        @Override
        public void run() {
            try{
                while (true){

                    Thread.sleep(50);
                        SystemVariable sysVar = systemVariableDAO.find();
                        double x = sysVar.getForce();
                        double y = sysVar.getPosition();
                        System.out.println(x + " " + y);
                    // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                    Platform.runLater(() -> {
                        // Update UI.
                        series.getData().add(new XYChart.Data<>(x,y));
                    });
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void essayChart(int pk) {
        XYChart.Series seriesSingle = new XYChart.Series();
        //populating the series with data

        Essay essay = essayDAO.findById(pk);
        seriesSingle.setName(essay.getEssayIdentification());
        // 1;1,2;2,3;3,4;4,5;5,6;6,7;7,8;8,9;9,10;10

        String strArraySplit[] = essay.getEssayChart().split(",");
        for(String str : strArraySplit){
            String dot[] = str.split(";");
            for(int i = 0; i<dot.length;i+=2){
                System.out.println(dot[i] + " " + dot[i+1]);
                seriesSingle.getData().add(new XYChart.Data(Double.parseDouble(dot[i]),Double.parseDouble(dot[i+1])));
            }
        }
        chartMultiLine.getData().add(seriesSingle);

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
    @FXML
    private void essayStart(){

        // Essay Chart construction and parameters
        chartEssayLine.getData().clear();
        series = new XYChart.Series<>();
        series.setName("Leitura");
        chartEssayLine.getData().add(series);

        // Thread to update the series on chart
        Thread chartThread = new Thread(new RTChartCreate());
        chartThread.start();
    }
    @FXML
    private void essayPause(){

    }
    @FXML
    private void essayStop(){

    }

    // FIM*********** Métodos de Movimento ***********


    @FXML
    private void led() throws InterruptedException {

        essayChart(Integer.parseInt(txtLed.getText()));




//        System.out.println("Iniciando");
//        System.out.println("Desce");
//        outputInjection("5");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Sobe");
//        outputInjection("4");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Parar");
//        outputInjection("3");
//        Thread.sleep(1000);

//        Setup setup = new Setup(6,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,200,200,0,0,100,100,0,"Teste Setup 1","Bruno", currentDate);
//        SetupDAO setupDAO = new SetupDAO();
//        setupDAO.create(setup);
//        System.out.println(setup);
//        System.out.println(setupDAO.findAll());
//
//        user = new User("Diego","diegoDev","12345");
//        user.save(user);
//        user = new User("Bruno", "brunoslima","biopdi");
//        user.save(user);
//        System.out.println(userDAO.findAll());
//        essay = new Essay(1,"Abemus data","ISO 9999","mBio portátil",220,0,45000,0,-65000,20000,25.4,0,35.0,null,currentDate);
//        essay.save(essay);
//        System.out.println(essayDAO.findAll());
//        Essay essay2 = essayDAO.findById(1);
//        System.out.println(essay2);
//        essay2.setEssayIdentification("Teste de update");
//        essay2.save(essay2);

        //3,"Teste 2","ISO 9001","mBio portátil", 0.003, 0,30000,0,-40500,5,25,0,35
//        eDAO.update(new Essay(2,3,"Teste 2","ISO 9001","mBio portátil", 0.003, 0,30000,0,-40500,5,25,0,35));
//        System.out.println(essayDAO.findAll());

    }
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