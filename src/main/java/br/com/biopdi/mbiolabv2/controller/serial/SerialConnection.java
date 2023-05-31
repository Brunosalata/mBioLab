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
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 10, 10);
        port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
    }

    public boolean isOpen(){
        if(port.isOpen()){
            return true;
        };
        return false;
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
     * Metodo que solicita qual celula de carga esta conectada (injeção de '=')
     */
    public synchronized void chargeCellRequest() {
        outputInjection("=");
    }

    /**
     * Metodo que le sinla de erro emitido via serial (injeção de '>')
     */
    public synchronized void errorRead() {
        outputInjection(">");
    }

    /**
     * Metodo que solicita retorno '1' (injeção de '?')
     */
    public synchronized void serialConnVerification() {
        outputInjection("?");
    }

    /**
     * Metodo que zera a posição (injeção de '0')
     */
    public synchronized void resetPosition() {
        outputInjection("0");
    }

    /**
     * Metodo que solicita o valor da força (injeção de '1')
     */
    public synchronized void forceRequest() {
        outputInjection("1");
    }

    /**
     * Metodo que solicita o valor da posição (injeção de '2')
     */
    public synchronized void positionRequest() {
        outputInjection("2");
    }

    // FIM*********** Métodos pré ensaio ***********

    // INICIO*********** Métodos de Movimento ***********

    /**
     * Metodo que solicita interrupção do movimento (injeção de '3')
     */
    public synchronized void stopMove() {
        outputInjection("3");
    }

    /**
     * Metodo que solicita movimento de ajuste para cima (injeção de '4')
     */
    public synchronized void moveUpAdjust() {
        outputInjection("4");
    }

    /**
     * Metodo que solicita movimento de ajuste para baixo (injeção de '5')
     */
    public synchronized void moveDownAdjust() {
        outputInjection("5");
    }

    /**
     * Metodo que solicita movimento de ensaio para cima (injeção de '6')
     */
    public synchronized void moveUpAssay() {
        outputInjection("6");
        System.out.println("Moveu CIMA 6");
    }

    /**
     * Metodo que solicita movimento de ensaio para baixo (injeção de '7')
     */
    public synchronized void moveDownAssay() {
        outputInjection("7");
        System.out.println("Moveu BAIXO 7");
    }

    // FIM*********** Métodos de Movimento ***********

    // INICIO*********** Métodos Ajuste de Velocidade ***********

    /**
     * Metodo que define a velocidade de ajuste (injeção de '8')
     */
    public synchronized void adjustVelocity(Integer adjustVel) {
        outputInjection(String.valueOf(8 + adjustVel));
    }

    /**
     * Metodo que define a velocidade de ensaio (injeção de '9')
     */
    public synchronized void essayVelocity(Integer essayVel) {
        outputInjection(String.valueOf(9 + essayVel));
    }

    // FIM*********** Métodos Ajuste de Velocidade ***********
}
