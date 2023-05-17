package br.com.biopdi.mbiolabv2.controller.SceneController;

import java.io.*;

import br.com.biopdi.mbiolabv2.controller.SceneController.runnable.RunnableThread;
import br.com.biopdi.mbiolabv2.controller.repository.dao.EssayDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SetupDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemParameterDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.UserDAO;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import br.com.biopdi.mbiolabv2.model.bean.Setup;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import br.com.biopdi.mbiolabv2.model.bean.User;
import com.fazecast.jSerialComm.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadFactory;

public class MainSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    UserDAO userDAO = new UserDAO();
    EssayDAO essayDAO = new EssayDAO();
    SetupDAO setupDAO = new SetupDAO();
    SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    @FXML
    private Chart chForcePosition;
    @FXML
    private TextField txtAdjustVelocity, txtAssayVelocity, txtEssayUserId;
    @FXML
    private Label lbForceView, lbPositionView, lbCurrentData, lbAutoConnection, lbUserId, lbUserName, lbUserLogin, lbUserPassword;
    @FXML
    private ComboBox cbPorts;
    @FXML
    private Button btnConnect, btnEssayByUserId;
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

    Thread thread1;

    /**
     * Thread que faz a leitura da posição em tempo real
     */


//    FIM ******************** Declarações iniciais **********************

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        port = SerialPort.getCommPort("0");
//        portConnectionList();
        autoConnect();

        // Mostra data local na base da aplicação
        lbCurrentData.setText(String.valueOf(systemDate));
    }

    /**
     * Método que define o algorítmo da Thread que faz a leitura dinâmica da Força e da Posição
     */
    private void FPReadingThread() {

        try{
        outputInjection("1x");
        Thread.sleep(10);
        System.out.println(inputValue());
//        String impF = inputValue();
//        lbForceView.setText(impF);
//        lbForceView.setText(inputValue());
        outputInjection("2x");
        Thread.sleep(10);
        System.out.println(inputValue());
//        String impP = inputValue();
//        lbPositionView.setText(impP);
//        lbPositionView.setText(inputValue());
        Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Método de conexão automática
     */
    private void autoConnect(){
//        if(port!=null) {

            SystemParameter sysPar = systemParameterDAO.find();
            System.out.println(sysPar);
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

            } else {
                port.closePort();
                cbPorts.setDisable(false);
                btnConnect.setText("Conectar");
            }
//        } else {
//            System.out.println("Nenhuma porta encontrada!");
//        }
    }

    /**
     * Método de abertura e fechamento de conexão
     */
    @FXML
    private void connect() {
        if (port != null) {
            //Método de abertura e fechamento de conexão serial
            if (btnConnect.getText().equals("Conectar")) {
                Setup setupCom = setupDAO.findById(1);

                port = SerialPort.getCommPort(cbPorts.getSelectionModel().getSelectedItem().toString());
                if (port.openPort()) {
                    btnConnect.setText("Desconectar");
                    cbPorts.setDisable(true);
                    port.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
                    port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 50, 50);
                    port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);


/////////////////////////////////////////////////// Platform.runLAter()

//                    Platform.runLater( () ->  {
//                        try {
//                            outputInjection("1x");
//                            Thread.sleep(10);
//                            String inpF = inputValue();
//                            lbForceView.setText(inpF);
//                            System.out.println(inpF);
//                            outputInjection("2x");
//                            Thread.sleep(10);
//                            String inpP = inputValue();
//                            lbPositionView.setText(inpP);
//                            System.out.println(inpP);
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
//
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
        } else{
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
//        Setup setup2 = setupDAO.findById(1);
//        setup2.setMC1M1(20500);
//        setup2.setSetupName("Teste Update");
//        setupDAO.update(setup2);
//        System.out.println(setupDAO.findAll());
////
////        System.out.println(essayDAO.findAll());
////        System.out.println(userDao.findAll());
//
//
//
////        user = new User("Diego","diegoDev","12345");
////        user.save(user);
////        user = new User("Bruno", "brunoslima","biopdi");
////        user.save(user);
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