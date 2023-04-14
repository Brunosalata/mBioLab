package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.SceneController.switchScene.SwitchSceneController;
import br.com.biopdi.mbiolabv2.controller.SceneController.task.ForcePositionViewTask;
import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.model.bean.*;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class HomeSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    SwitchSceneController switchScene = new SwitchSceneController();
    private UserDAO userDAO = new UserDAO();
    private EssayDAO essayDAO = new EssayDAO();
    private SetupDAO setupDAO = new SetupDAO();
    private SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private SystemVariableDAO systemVariableDAO = new SystemVariableDAO();
    @FXML
    private Chart chForcePosition;
    @FXML
    private TextField txtAdjustVelocity, txtAssayVelocity, txtEssayUserId;
    @FXML
    private Label lbForceView, lbPositionView, lbCurrentData, lbAutoConnection, lbUserId, lbUserName, lbUserLogin, lbUserPassword;
    @FXML
    private ComboBox cbPorts;
    @FXML
    private Button btnConnect, btnEssayByUserId, btnSwitchToHomeScene, btnSwitchToDashboardScene, btnSwitchToEssayScene, btnSwitchToReportScene, btnSwitchToSettingScene;
    @FXML
    private ListView<Setup> setupListView;
    @FXML
    private ListView<User> userListView;
    @FXML
    private ListView<Essay> essayListView;
    @FXML
    private ListView<Essay> essayByUserListView;

    private List<Setup> setupList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private List<Essay> essayList = new ArrayList<>();
    private List<Essay> essayByUserIdList = new ArrayList<>();
    private ObservableList<Setup> obsSetupList;
    private ObservableList<User> obsUserList;
    private ObservableList<Essay> obsEssayList;
    private ObservableList<Essay> obsEssayByUserIdList;
    private SerialPort port;


    public final ComboBox getCbPorts() {
        return cbPorts;
    }

    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);



    /**
     * Thread que faz a leitura da posição em tempo real
     */


//    FIM ******************** Declarações iniciais **********************

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        port = SerialPort.getCommPort("0");
//        portConnectionList();
        autoConnect();

//        invokeForcePositionViewTask();



        // Mostra data local na base da aplicação
        lbCurrentData.setText(String.valueOf(systemDate));
    }

    /**
     * Método que invoca a TaskForcePositionViewTask, com atuação paralela à Thread principal e retorna o valor a ser atualizado na lbForceView e lbPositionView
     */
//    private void invokeForcePositionViewTask() {
//        ForcePositionViewTask forcePositionViewTask = new ForcePositionViewTask();
//        forcePositionViewTask.valueProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    lbForceView.setText(newValue);
//            }
//        });
//        Thread th = new Thread(forcePositionViewTask);
//        th.setDaemon(true);
//        th.start();
//    }

    /**
     * Método que define o algorítmo da Thread que faz a leitura dinâmica da Força e da Posição
     */
    private void FPReadingThread() {

        try{
            Thread.sleep(2000);
            outputInjection("1x");
            Thread.sleep(20);
            String impF = inputValue();
//            System.out.println(impF);
            outputInjection("2x");
            Thread.sleep(20);
            String impP = inputValue();
//            System.out.println(impP);

            //Atualização da UI pela Thread
            Platform.runLater(() -> {
                lbForceView.setText(impF);
                lbPositionView.setText(impP);
            });

            SystemVariable sysVar = new SystemVariable(1,Double.valueOf(impF),Double.valueOf(impP));
            systemVariableDAO.update(sysVar);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void FPWritingThread(){
        SystemVariable sysVar2;
        try {
            Thread.sleep(2000);
            sysVar2 = systemVariableDAO.find();
            System.out.println(sysVar2.getForce());
            System.out.println(sysVar2.getPosition());
//            lbForceView.setText(Double.toString(sysVar2.getForce())); //Conversão double to String de force para setText em lbForce
//            lbPositionView.setText(Double.toString(sysVar2.getPosition())); //Conversão double to String de position para setText em lbPosition
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }


    /**
     * Método de conexão automática
     */
    @FXML
    private void autoConnect(){

        SystemParameter sysPar = systemParameterDAO.find();
        System.out.println("Conectado");
            if(sysPar==null){
                sysPar.setPortName(cbPorts.getSelectionModel().getSelectedItem().toString());
                sysPar.setSystemLanguage("pt");
                sysPar.setSoundOn("false");
                systemParameterDAO.create(sysPar);
            }
        port = SerialPort.getCommPort(sysPar.getPortName());
        System.out.println(port);
        if (port.openPort()) {
            btnConnect.setText("Conectado");
            cbPorts.setDisable(true);
            port.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 50, 50);
            port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

            // Thread para solicitar Posição e Força pela porta serial e persistir em tb_systemVariable
            Thread t = new Thread(() -> {

                while (true) {
                    FPReadingThread();
                    System.out.println("Dados salvos");
                }
            });
            t.start();

            //Estrutura testada e deixou extremamente pesada a aplicação.
//            // Thread alternativo para puxar dados BD e setText nas JLabel
//            Runnable task = () -> {
//                Platform.runLater(() -> {
//                    while (true) {
//                        #My code here
//                    }
//                });
//            };
//            Thread thread = new Thread(task);
//            thread.setDaemon(true);
//            thread.start();


            // Thread que puxa informação de tb_systemVariable para printar no console ou setText nas JLabel
                Thread t2 = new Thread(() -> {
//                        SystemVariable sysVar2;
                    while (true) {
                        System.out.println("Leitura de dados");
                        FPWritingThread();

//                            try {
//                                Thread.sleep(1000);
//                                System.out.println("Leitura de dados");
//                                sysVar2 = systemVariableDAO.find();
//                                System.out.println(sysVar2.getForce());
//                                System.out.println(sysVar2.getPosition());
////                                lbForceView.setText(Double.toString(sysVar2.getForce())); //Conversão double to String de force para setText em lbForce
////                                lbPositionView.setText(Double.toString(sysVar2.getPosition())); //Conversão double to String de position para setText em lbPosition
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e){
//                                throw new RuntimeException(e);
//                            }
                    }
                });
                t2.start();

        } else {
            port.closePort();
            btnConnect.setText("Conectar");
        }
    }

    /**
     * Método de abertura e fechamento de conexão
     */
    @FXML
    private void connect() {
        if (port != null) {
            //Método de abertura e fechamento de conexão serial
            if (btnConnect.getText().equals("Conectar")) {
                port = SerialPort.getCommPort(cbPorts.getSelectionModel().getSelectedItem().toString());
                if (port.openPort()) {
                    btnConnect.setText("Desconectar");
                    cbPorts.setDisable(true);
                    port.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
                    port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 50, 50);
                    port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

                    // Thread para solicitar Posição e Força pela porta serial e persistir em tb_systemVariable
                    Thread t = new Thread(() -> {

                        while (true) {
                            System.out.println("foi");
                            FPReadingThread();
                        }
                    });
                    t.start();

                    // Thread alternativo para puxar dados BD e setText nas JLabel
                    Runnable task = () -> {
                        Platform.runLater(() -> {
                            System.out.println("I'm running later...");
                            SystemVariable sysVar2;
                            while (true) {
                                System.out.println("Boa");
                                try {
                                    Thread.sleep(1000);
                                    sysVar2 = systemVariableDAO.find();
                                    System.out.println(sysVar2.getForce());
                                    System.out.println(sysVar2.getPosition());
                                    lbForceView.setText(Double.toString(sysVar2.getForce())); //Conversão double to String de force para setText em lbForce
                                    lbPositionView.setText(Double.toString(sysVar2.getPosition())); //Conversão double to String de position para setText em lbPosition
                                    Thread.sleep(1000);
                                } catch (InterruptedException e){
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    };
                    Thread thread = new Thread(task);
                    thread.setDaemon(true);
                    thread.start();


                    // Thread que puxa informação de tb_systemVariable para printar no console ou setText nas JLabel
//                    Thread t2 = new Thread(() -> {
//                        SystemVariable sysVar2;
//                        while (true) {
//                            System.out.println("Boa");
//                            try {
//                                Thread.sleep(1000);
//                                sysVar2 = systemVariableDAO.find();
//                                System.out.println(sysVar2.getForce());
//                                System.out.println(sysVar2.getPosition());
////                                lbForceView.setText(Double.toString(sysVar2.getForce())); //Conversão double to String de force para setText em lbForce
////                                lbPositionView.setText(Double.toString(sysVar2.getPosition())); //Conversão double to String de position para setText em lbPosition
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e){
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    });
//                    t2.start();

/////////////////////////////////////////////////// Platform.runLAter()

//                    Platform.runLater( () ->  {
//                        try {
//                            outputInjection("1x");
//                            Thread.sleep(10);
//                            System.out.println(inputValue());
////                            lbForceView.setText(inputValue());
//                            outputInjection("2x");
//                            Thread.sleep(10);
//                            System.out.println(inputValue());
////                            lbPositionView.setText(inputValue());
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                    });


/////////////////////////////////////////////////// Service<Void>

//                    Service<Void> ser = new Service<Void>() {
//                        @Override protected Task createTask() {
//                            return new Task<Void>() {
//                                @Override protected Void call() throws InterruptedException {
//                                    // You code you want to execute in service backgroundgoes here
//                                    outputInjection("1x");
//                                    Thread.sleep(10);
//                                        String inpF = inputValue();
//                                        lbForceView.setText(inpF);
//                                    System.out.println(inpF);
//                                    outputInjection("2x");
//                                    Thread.sleep(10);
//                                        String inpP = inputValue();
//                                        lbPositionView.setText(inpP);
//                                    System.out.println(inpP);
//                                    Thread.sleep(1000);
//
//
//
//                                    return null;
//                                }
//                            };
//                        }
//                    };
//                    ser.setOnSucceeded((WorkerStateEvent event) -> {
//                        // Anything which you want to update on javafx thread (GUI) after completion of background process.
//                    });
//
//                    ser.restart();


/////////////////////////////////////////////////// Chama FPReadingThread (mas se mantem na mesma Thread do GUI do JavaFX.. não permite atualização na GUI

//                    Thread t = new Thread(() -> {
//                        while (true) {
//                            FPReadingThread();
//                        }
//                    });
//                    t.start();

///////////////////////////////////////////////////

                }
            } else {
                port.closePort();
                cbPorts.setDisable(false);
                btnConnect.setText("Conectar");
            }

        }else{
            System.out.println("Nenhuma porta encontrada!");
        }

    }


    /**
     * Método de listagem de portas Seriais disponíveis dentro do ComboBox (cbPorts)
     */
    private void portConnectionList() {

        SerialPort[] portNames = SerialPort.getCommPorts();
        for (SerialPort portName : portNames) {
            cbPorts.getItems().add(portName.getSystemPortName());
        }
    }
    /**
     * Método genérico para injeção do output, aplicável para os diferentes processos port.OutputStream() que requeiram uma string
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
        outputInjection("1x");
        Thread.sleep(20);
        lbForceView.setText(inputValue());
        //incluir tara para força
    }
    /**
     * Método que solicita o valor da posição (injeção de '2')
     */
    @FXML
    private void positionRequest() throws InterruptedException {
        outputInjection("2x");
        Thread.sleep(20);
        lbPositionView.setText(inputValue());
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

    // INICIO*********** Métodos de setup ***********







    // FIM*********** Métodos de setup ***********


    // INICIO******** Métodos de Busca no Banco de Dados ********

    @FXML
    private void essayFindByUser(){
        try{
            essayByUserIdList.addAll(essayDAO.findByUser(Integer.parseInt(txtEssayUserId.getText())));
            obsEssayByUserIdList = FXCollections.observableList(essayByUserIdList);
            essayByUserListView.setItems(obsEssayByUserIdList);
        } catch (Exception e){
        System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void DBDataPull(){

        userList.addAll(userDAO.findAll());
        obsUserList = FXCollections.observableList(userList);
        userListView.setItems(obsUserList);

        setupList.addAll(setupDAO.findAll());
        obsSetupList = FXCollections.observableList(setupList);
        setupListView.setItems(obsSetupList);

        essayList.addAll(essayDAO.findAll());
        obsEssayList = FXCollections.observableList(essayList);
        essayListView.setItems(obsEssayList);
    }

    private void ImagemInclusion(){
        //Compactação para armazenamento
        // imagenBitmap é a imagem a ser armazenada
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte imageBitmap[]=stream.toByteArray();
    }
    private void ImagemConsulta(){
        //Descompactação para leitura
//        DataBaseHandler db = new DataBaseHandler(this);
//        User user = db.getUserById(userId);
//
//        byte[] outImage=contato.getUserImage();
//        ByteArrayOutputStream imageStream = new ByteArrayInputStream(outImage);
//        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
//        image.setImageBitmap(imageBitmap);
    }

//                 ******** NECESSÁRIO TESTAR AINDA *********

    // USER OK
//    @FXML
//    public void userSave(){
//        user.save(new User(lbUserName.getText(), lbUserLogin.getText(), lbUserPassword.getText()));
//    }
//    @FXML
//    public void userDelete(){
//        userDAO.delete(userDAO.findById(Integer.parseInt(lbUserId.getText())));
//    }
//    @FXML
//    public void userUpdate(){
//        user.save(new User((Integer.parseInt(lbUserId.getText())), lbUserName.getText(), lbUserLogin.getText(), lbUserPassword.getText()));
//    }
    // SETUP OK
//    @FXML
//    public void setupSave(){
//        user.save(new User(lbUserName.getText(), lbUserLogin.getText(), lbUserPassword.getText()));
//    }
//    @FXML
//    public void setupDelete(){
//        userDAO.delete(userDAO.findById(Integer.parseInt(lbUserId.getText())));
//    }
//    @FXML
//    public void setupUpdate(){
//        user.save(new User((Integer.parseInt(lbUserId.getText())), lbUserName.getText(), lbUserLogin.getText(), lbUserPassword.getText()));
//    }
//    // ESSAY OK
//    @FXML
//    public void essaySave(){
//        user.save(new User(lbUserName.getText(), lbUserLogin.getText(), lbUserPassword.getText()));
//    }
//    @FXML
//    public void essayDelete(){
//        userDAO.delete(userDAO.findById(Integer.parseInt(lbUserId.getText())));
//    }

    // FIM******** Métodos de Busca no Banco de Dados ********


    @FXML
    private void led() throws InterruptedException{
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

    // INICIO*********** Métodos de setup ***********

    @FXML
    private void switchToHomeScene() throws IOException {
        switchScene.switchToHomeScene(new ActionEvent());
    }
    @FXML
    private void switchToDashboardScene() throws IOException {
        switchScene.switchToDashboardScene(new ActionEvent());
    }
    @FXML
    private void switchToEssayScene() throws IOException {
        switchScene.switchToEssayScene(new ActionEvent());
    }
    @FXML
    private void switchToReportScene() throws IOException {
        switchScene.switchToReportScene(new ActionEvent());
    }
    @FXML
    public void switchToSystemSettingScene(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("src/main/resources/br/com/biopdi/mbiolabv2/systemSettingScene.fxml"));



//        stage = (Stage)(node.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


    // FIM*********** Métodos de setup ***********

}