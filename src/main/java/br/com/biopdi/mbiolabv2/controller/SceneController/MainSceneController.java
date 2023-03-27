package br.com.biopdi.mbiolabv2.controller.SceneController;

import java.io.*;

import br.com.biopdi.mbiolabv2.controller.repository.DBConnection;
import br.com.biopdi.mbiolabv2.controller.repository.dao.UserDAO;
import br.com.biopdi.mbiolabv2.model.bean.User;
import com.fazecast.jSerialComm.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainSceneController implements Initializable {

    @FXML
    private Chart chForcePosition;

    @FXML
    private TextField txtAdjustVelocity, txtAssayVelocity;

    @FXML
    private Label lbForceView, lbPositionView, lbAutoConnection;

    @FXML
    private ComboBox cbPorts;

    @FXML
    private Button btnConnect, btnLed, btnPosition;

    private SerialPort port;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portConnectionList();
        //realTime();

        /**
         * Thread que faz a leitura da posição em tempo real
         */
//        Platform.runLater( () ->  {
//            while(true){
//                try {
//                    forceRequest();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
    }


    public void autoConnection(){
//        /**
//         * autoconexão via contains("UART")
//         */
//        if(lbAutoConnection.getText().equals("Conectando")){
//            SerialPort[] portNames = SerialPort.getCommPorts();
//            for (SerialPort port : portNames) {
//                    port.openPort();
//                if(port.getPortDescription().contains("UART")) {
//                    lbAutoConnection.setText("Conectado");
//                    port.setComPortParameters(115200, 8, 1, 0);
//                }
//            }
//        } else {
//            cbPorts.setDisable(false);
//            lbAutoConnection.setText("Desconectado");
//        }

//        /**
//         * autoconexão via output e input validator
//         */
//        if(lbAutoConnection.getText().equals("Conectando")){
//
//            SerialPort[] portNames = SerialPort.getCommPorts();
//            for (SerialPort portName : portNames) {
//
//                port = SerialPort.getCommPort(portName.getSystemPortName());
//                if(port.openPort()){
//                    Thread.sleep(2000);
//                    outputInjection("x");
//                    Thread.sleep(20);
//                    if(inputValue()!="10") {
//                        port.closePort();
//                    } else{
//                        System.out.printf("Conexão realizada com sucesso na porta %s", port.getSystemPortName());
//                        lbAutoConnection.setText("Conectado");
//                        port.setComPortParameters(115200, 8, 1, 0);
//                    }
//                }
//
//            }
//        } else {
//            cbPorts.setDisable(false);
//            lbAutoConnection.setText("Desconectado");
//        }
    }
    /**
     * Método de abertura e fechamento de conexão
     */
    @FXML
    private void connect() throws InterruptedException {

        //Método de abertura e fechamento de conexão serial
        if (btnConnect.getText().equals("Conectar")) {
            port = SerialPort.getCommPort(cbPorts.getSelectionModel().getSelectedItem().toString());
            if (port.openPort()) {
                btnConnect.setText("Desconectar");
                cbPorts.setDisable(true);
                port.setBaudRate(115200);
            }
        } else {
            port.closePort();
            cbPorts.setDisable(false);
            btnConnect.setText("Conectar");
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
        outputInjection("1");
        Thread.sleep(20);
        lbForceView.setText(inputValue());
    }

    /**
     * Método que solicita o valor da posição (injeção de '2')
     */
    public void realTime() {
        while(port.openPort()){
            new Thread(() -> {
                outputInjection("2");
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lbPositionView.setText(inputValue());
            }).start();
        }
    }

    @FXML
    private void positionRequest() throws InterruptedException {
        outputInjection("2");
        Thread.sleep(20);
        lbPositionView.setText(inputValue());
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
        String adjust = 8 + txtAdjustVelocity.getText();
        outputInjection(adjust);
    }

    /**
     * Método que solicita velocidade de ensaio (injeção de '9')
     */
    @FXML
    private void assayVelocity() {
        String adjust = 9 + txtAssayVelocity.getText();
        outputInjection(adjust);
    }

    // FIM*********** Métodos de Movimento ***********

    // INICIO*********** Métodos de setup ***********







    // FIM*********** Métodos de setup ***********



    @FXML
    private void led() throws InterruptedException{
        System.out.println("Iniciando");
        System.out.println("Desce");
        outputInjection("5");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Sobe");
        outputInjection("4");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Parar");
        outputInjection("3");
        Thread.sleep(1000);

        UserDAO uDao = new UserDAO();
        User user = new User("Bruno", "brunosl","1234");
        System.out.println(user);
        UserDAO uDAO = new UserDAO();
        uDAO.create(user);
        System.out.println("Salvo com sucesso");
        System.out.println(uDAO.findAll());







    }
}