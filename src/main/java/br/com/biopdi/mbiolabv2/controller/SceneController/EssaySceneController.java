package br.com.biopdi.mbiolabv2.controller.SceneController;


import br.com.biopdi.mbiolabv2.controller.SceneController.switchScene.SwitchMenuSceneController;
import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class EssaySceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    SwitchMenuSceneController switchScene = new SwitchMenuSceneController();
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private final SystemVariableDAO systemVariableDAO = new SystemVariableDAO();


    // Puxar dados do DB systemVariable salvas na Thread de leitura FPReadingThread()

    @FXML
    private CategoryAxis xChartSingle;
    @FXML
    private NumberAxis yChartSingle,xChartMulti, yChartMulti;// xChartSingle,
    @FXML
    private LineChart<?,?> xyChartSingle, xyChartMulti;
    @FXML
    private TextField txtConnected, txtForceView, txtPositionView, txtAdjustVelocity, txtAssayVelocity, txtEssayUserId, txtLed;
    @FXML
    private Label lbCurrentData, lbUserId, lbUserName, lbUserLogin, lbUserPassword;
    @FXML
    private Button btnEssayByUserId, btnSwitchToHomeScene, btnSwitchToDashboardScene, btnSwitchToEssayScene, btnSwitchToReportScene, btnSwitchToSettingScene;
    private SerialPort port;
    private XYChart.Series series;

    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoConnect();


        //dá para usar no gráfico
//        invokeForcePositionViewTask();



        // Mostra data local na base da aplicação
        lbCurrentData.setText(String.valueOf(systemDate));
    }

    /**
     * Método de conexão automática
     */
    @FXML
    private void autoConnect(){

        SystemParameter sysPar = systemParameterDAO.find();
        port = SerialPort.getCommPort(sysPar.getPortName());
        System.out.println("Conectado à porta: " + sysPar.getPortName() + " - " + port);
        if (port.openPort()) {
            txtConnected.setText("Conectado");
//            txtConnected.setStyle("NOME DO ESTILO");
            port.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 50, 50);
            port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

            // Thread para solicitar Posição e Força e atualizar lbForceView e lbPositionView
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    while (true) {
                        FPReadingThread();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t.start();

        } else {
            port.closePort();
            txtConnected.setText("Desconectado");
//            txtConnected.setStyle("NOME DO ESTILO");
        }
    }

    /**
     * Método que define o algorítmo da Thread que faz a leitura dinâmica da Força e da Posição
     */
    private void FPReadingThread() {

        try{
            Thread.sleep(500);
            outputInjection("1");  // Requerimento do valor da força
            Thread.sleep(20);
            String impF = inputValue();
            outputInjection("2");  // requerimento do valor da posição
            Thread.sleep(20);
            String impP = inputValue();

            //Atualização da UI pela Thread
            Platform.runLater(() -> {
                txtForceView.setText(impF);
                txtPositionView.setText(impP);
            });

            // Salvando dados no banco de dados - estratégia para alimentar um array que será usado para construir gráfico e armazenar os valores
            SystemVariable sysVar = new SystemVariable(1,Double.valueOf(impF),Double.valueOf(impP));
            systemVariableDAO.update(sysVar);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void chartTry(){
        ScheduledExecutorService scheduledExecutorService;
        int WINDOW_SIZE = 50;

        xChartSingle.setLabel("Time/s");
        xChartSingle.setAnimated(false); // axis animations are removed
        yChartSingle.setLabel("Value");
        yChartSingle.setAnimated(false); // axis animations are removed

        //creating the line chart with two axis created above
        xyChartSingle.setTitle("Realtime JavaFX Charts");
        xyChartSingle.setAnimated(false); // disable animations

        // add series to chart
        xyChartSingle.getData().add(series);

        // this is used to display time in HH:mm:ss format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        // setup a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        // put dummy data onto graph per second
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // get a random integer between 0-10
            Integer random = ThreadLocalRandom.current().nextInt(10);

            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), random));

                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 1, TimeUnit.SECONDS);

    }

    @FXML
    private void essayChartReader() throws InterruptedException {
        //defining a series
        int count = 0;

        Double[] chartDotVector;                                    // vetor que recebe Double Força e Posição do DB (systemVariable)
        String chartDotVectorStg;                                   // variável que recebe a String, da conversão do vetor chartDotVector
        List<String> essayChartFinalString = new ArrayList<>();     // ArrayList que recebe as Strings dos vetores para salvar no DB (essay.setEssayChart)
        String[] essayChartArrayReceiver;                           //
        Double[] essayChartSplit;                                   // vetor que recebe cada elemento do Array de String do DB
        List<Double[]> essayChartFinalDouble = new ArrayList<>();   //

        XYChart.Series series = new XYChart.Series();

        //Obtenção dos valores dos pontos
        while(count<10) {
//
            //Atualização da UI pela Thread

            SystemVariable sysVar = systemVariableDAO.find();
            chartDotVector = new Double[]{sysVar.getForce(), sysVar.getPosition()};
            System.out.println(chartDotVector[0]);
            System.out.println(chartDotVector[1]);

            //Conversão de Double[] para String e adicionar String do vetor ao arrayList<String>
            chartDotVectorStg = chartDotVector[0] + "; " + chartDotVector[1];
            essayChartFinalString.add(chartDotVectorStg);

            for(String item : essayChartFinalString){
                System.out.print(item);
            }


            count++;
            System.out.println(count);
        }
//            Platform.runLater(() -> {
        Essay essay = essayDAO.findById(27);
        essayChartArrayReceiver = new String[]{essay.getEssayChart()};
        for(String item : essayChartArrayReceiver){
            essayChartSplit = new Double[]{Double.valueOf(Arrays.toString(item.split(";")))};
            essayChartFinalDouble.add(essayChartSplit);
            series.getData().add(new XYChart.Data(essayChartSplit[0], essayChartSplit[1]));
            xyChartSingle.getData().add(series);
        }

//            });

//            SystemVariable sysVar = systemVariableDAO.find();
//            series1.getData().add(new XYChart.Data(sysVar.getForce(), sysVar.getPosition()));
//            xyChartSingle.getData().add(series1);
//            chartDotVector = new Double[]{sysVar.getForce(), sysVar.getPosition()};
//            essayChartArray.add(chartDotVector);
//            System.out.println(count);
//            count++;
//            Thread.sleep(500);


//        Essay essayChart = new Essay();
//        essayChart.setUserId(3);
//        essayChart.setEssayChart(essayChartFinalDouble.toString());
//        essayDAO.create(essayChart);




    }

    @FXML
    private void essayChart(){
        //defining the axes
        xChartSingle.setLabel("Deslocamento");
        yChartSingle.setLabel("Força");

        xyChartSingle.setTitle("Gráfico Força x Posição");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Chart de ensaio único");
        //populating the series with data
        Essay essay = essayDAO.findById(1);
        essay.getEssayChart().split(";");

        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));

        xyChartSingle.getData().add(series);

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
        txtForceView.setText(inputValue());
        //incluir tara para força
    }
    /**
     * Método que solicita o valor da posição (injeção de '2')
     */
    @FXML
    private void positionRequest() throws InterruptedException {
        outputInjection("2");
        Thread.sleep(20);
        txtPositionView.setText(inputValue());
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
        if(txtAdjustVelocity.getText()!=null){
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
    private void assayVelocity() {
        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if(txtAssayVelocity.getText()!=null){
            String adjust = 9 + txtAssayVelocity.getText();
            outputInjection(adjust);
        } else {
            txtAssayVelocity.setText("15000.00");
            outputInjection("915000");
        }
    }

    // FIM*********** Métodos de Movimento ***********


    @FXML
    private void led() throws InterruptedException{

        chartTry();
//        essayChartReader();
//        essayChart();


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
