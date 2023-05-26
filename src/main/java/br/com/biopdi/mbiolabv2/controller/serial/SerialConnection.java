package br.com.biopdi.mbiolabv2.controller.serial;
/*
 *  Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
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

import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemParameterDAO;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Bruno Salata Lima - 26/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project Essay.java
 */
public class SerialConnection {
    private SystemParameterDAO sysParDAO = new SystemParameterDAO();
    private SerialPort port;

    public void openPort(){
        // Instance to get an object SystemParameter from tb_systemParameter (single line)
        SystemParameter sysPar = sysParDAO.find();
        // portName receives portName from systemParameterDAO
        port = SerialPort.getCommPort(sysPar.getPortName());
        // Abertura de porta serial
        port.openPort();
        // Configuracao da porta serial
        port.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 50, 50);
        port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
    }

    public void closePort(){
        port.closePort();
    }

    /**
     * Método genérico para injeção do output, aplicável para os diferentes processos port.OutputStream() que requeiram uma 'string'
     *
     * @param stg
     */
    public synchronized void outputInjection(String stg) {
        PrintWriter output = new PrintWriter(port.getOutputStream(), true);
        output.print(stg);
        output.flush();
    }

    /**
     * Método que recebe o valor real do input
     *
     * @return String
     */
    public synchronized String inputValue() {
        Scanner s = new Scanner(port.getInputStream());
        return s.nextLine();
    }

    // INICIO*********** Métodos pré ensaio ***********

    /**
     * Método que zera a posição (injeção de '0')
     */
    @FXML
    public synchronized void resetPosition() {
        outputInjection("0");
    }

    /**
     * Método que solicita o valor da força (injeção de '1')
     */
    @FXML
    public synchronized void forceRequest() {
        outputInjection("1");
    }

    /**
     * Método que solicita o valor da posição (injeção de '2')
     */
    @FXML
    public synchronized void positionRequest() {
        outputInjection("2");
    }

    // FIM*********** Métodos pré ensaio ***********

    // INICIO*********** Métodos de Movimento ***********

    /**
     * Método que solicita interrupção do movimento (injeção de '3')
     */
    @FXML
    public synchronized void stopMove() {
        outputInjection("3");
    }

    /**
     * Método que solicita movimento de ajuste para cima (injeção de '4')
     */
    @FXML
    public synchronized void moveUp() {
        outputInjection("4");
        System.out.println("Moveu CIMA");
    }

    /**
     * Método que solicita movimento de ajuste para baixo (injeção de '5')
     */
    @FXML
    public synchronized void moveDown() {
        outputInjection("5");
        System.out.println("Moveu BAIXO");
    }

    /**
     * Método que solicita movimento de ensaio para cima (injeção de '6')
     */
    @FXML
    public synchronized void moveUpAssay() {
        outputInjection("6");
    }

    /**
     * Método que solicita movimento de ensaio para baixo (injeção de '7')
     */
    @FXML
    public synchronized void moveDownAssay() {
        outputInjection("7");
    }

    // FIM*********** Métodos de Movimento ***********

    // INICIO*********** Métodos Ajuste de Velocidade ***********

    /**
     * Método que define a velocidade de ajuste (injeção de '8')
     */
    @FXML
    public synchronized void adjustVelocity(String adjustVel) {
        int value = Integer.parseInt(adjustVel);
        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if (adjustVel != null) {
            if(value >= 40000){
                outputInjection("840000");
            } else if(value >= 15000){
                String adjust = 8 + adjustVel;
                outputInjection(adjust);
            } else {
                outputInjection("815000");
            }
        } else {
            outputInjection("815000");
        }
    }

    /**
     * Método que define a velocidade de ensaio (injeção de '9')
     */
    @FXML
    public synchronized void essayVelocity(String essayVel) {
        int value = Integer.parseInt(essayVel);
        //Incluir range minimo, maximo e null (IF ou SWITCH)
        if (essayVel != null) {
            if(value >= 40000){
                outputInjection("940000");
            } else if(value >= 15000){
                String adjust = 9 + essayVel;
                outputInjection(adjust);
            } else {
                outputInjection("915000");
            }
        } else {
            outputInjection("915000");
        }
    }

    // FIM*********** Métodos Ajuste de Velocidade ***********
}
