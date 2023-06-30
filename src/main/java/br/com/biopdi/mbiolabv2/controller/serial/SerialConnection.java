package br.com.biopdi.mbiolabv2.controller.serial;
/*
 *  Copyright (c) 2023.
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

import br.com.biopdi.mbiolabv2.controller.repository.dao.SetupDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemParameterDAO;
import br.com.biopdi.mbiolabv2.model.bean.Setup;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Bruno Salata Lima - 26/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class SerialConnection extends Thread {
    private final SetupDAO setupDAO = new SetupDAO();
    private final SystemParameterDAO sysParDAO = new SystemParameterDAO();
    public SerialPort port;
    private Integer MC1M1, MC2M1, MC3M1, MC4M1, MC5M1, MC6M1, MC7M1, MC8M1, MC9M1, MC10M1, MC11M1, MC12M1, MC13M1,
            MC14M1, MC15M1, MC16M1, MC17M1, MC18M1, MC19M1, MC20M1, MC21M1, MC22M1, MC23M1, MC24M1, MC25M1, MC26M1,
            MC27M1, MC28M1, MC29M1, MC30M1;

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

    /**
     * Boolean que indica se porta serial esta aberta ou nao
     * @return
     */
    public boolean isOpen(){
        if(port.isOpen()){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metodo para fechar porta serial
     */
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
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(port.getInputStream());
            if (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    // INICIO*********** Métodos pré ensaio ***********

    /**
     * Metodo que solicita qual celula de carga esta conectada (injeção de '=')
     */
    public synchronized void chargeCellRequest() {
        outputInjection("=");
    }

    /**
     * Metodo que solicita sinal de erro emitido via serial (injeção de '>')
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
    public synchronized void moveUpEssay() {
        outputInjection("6");
        System.out.println("Moveu CIMA 6");
    }

    /**
     * Metodo que solicita movimento de ensaio para baixo (injeção de '7')
     */
    public synchronized void moveDownEssay() {
        outputInjection("7");
        System.out.println("Moveu BAIXO 7");
    }

    // FIM*********** Métodos de Movimento ***********

    // INICIO*********** Métodos Ajuste de Velocidade ***********

    /**
     * Metodo que define a velocidade de ajuste (injeção de '8')
     */
    public synchronized void adjustVelocity(Integer adjustVel) {
        outputInjection(8 + String.valueOf(adjustVel));
    }

    /**
     * Metodo que define a velocidade de ensaio (injeção de '9')
     */
    public synchronized void essayVelocity(Integer essayVel) {
        outputInjection(9 + String.valueOf(essayVel));
    }

    /**
     * Metodo para teste de conexao com equipamento
     */
    public boolean testConnection(){ // REQUER CORRECOES - IMPLEMENTACAO CORRETA
//        openPort();
//        try{
//            serialConnVerification();
//            Thread.sleep(0);
//            if(port.bytesAvailable()!=0){
//                port.flushIOBuffers();
//                System.out.println("Bom");
//                closePort();
//                return true;
//            } else{
//                System.out.println("Ruim");
//                closePort();
//                return false;
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    /**
     * Método de conexão automática, buscando o portName do systemSetting no DB
     */
    @FXML
    public synchronized void setupParameter() {
        // abertura de porta
        openPort();
        if(isOpen()){
            // Leitura dos parâmetros MCs
            System.out.println("Lendo parâmetros");
            try {
                Thread.sleep(1000);
                // Leitura do MC1M1
                outputInjection("a");
                Thread.sleep(0);
                MC1M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M2
                outputInjection("b");
                Thread.sleep(0);
                MC2M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M3
                outputInjection("c");
                Thread.sleep(0);
                MC3M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M4
                outputInjection("d");
                Thread.sleep(0);
                MC4M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M5
                outputInjection("e");
                Thread.sleep(0);
                MC5M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M6
                outputInjection("f");
                Thread.sleep(0);
                MC6M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M7
                outputInjection("g");
                Thread.sleep(0);
                MC7M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M8
                outputInjection("h");
                Thread.sleep(0);
                MC8M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M9
                outputInjection("i");
                Thread.sleep(0);
                MC9M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M10
                outputInjection("j");
                Thread.sleep(0);
                MC10M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M11
                outputInjection("k");
                Thread.sleep(0);
                MC11M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M12
                outputInjection("l");
                Thread.sleep(0);
                MC12M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M13
                outputInjection("m");
                Thread.sleep(0);
                MC13M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M14
                outputInjection("n");
                Thread.sleep(0);
                MC14M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M15
                outputInjection("o");
                Thread.sleep(0);
                MC15M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M16
                outputInjection("p");
                Thread.sleep(0);
                MC16M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M17
                outputInjection("q");
                Thread.sleep(0);
                MC17M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M18
                outputInjection("r");
                Thread.sleep(0);
                MC18M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M19
                outputInjection("s");
                Thread.sleep(0);
                MC19M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M20
                outputInjection("t");
                Thread.sleep(0);
                MC20M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M21
                outputInjection("u");
                Thread.sleep(0);
                MC21M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M22
                outputInjection("v");
                Thread.sleep(0);
                MC22M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M23
                outputInjection("w");
                Thread.sleep(0);
                MC23M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M24
                outputInjection("x");
                Thread.sleep(0);
                MC24M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M25
                outputInjection("y");
                Thread.sleep(0);
                MC25M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M26
                outputInjection("z");
                Thread.sleep(0);
                MC26M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M27
                outputInjection("{");
                Thread.sleep(0);
                MC27M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M28
                outputInjection("|");
                Thread.sleep(0);
                MC28M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M29
                outputInjection("}");
                Thread.sleep(0);
                MC29M1 = Integer.valueOf(inputValue());
                // Leitura do MC1M30
                outputInjection("~");
                Thread.sleep(0);
                MC30M1 = Integer.valueOf(inputValue());

                // Persistindo dados no DB
                setupDAO.update(new Setup(1, MC1M1, MC2M1, MC3M1, MC4M1, MC5M1, MC6M1, MC7M1, MC8M1, MC9M1, MC10M1,
                        MC11M1, MC12M1, MC13M1, MC14M1, MC15M1, MC16M1, MC17M1, MC18M1, MC19M1, MC20M1, MC21M1, MC22M1,
                        MC23M1, MC24M1, MC25M1, MC26M1, MC27M1, MC28M1, MC29M1, MC30M1));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Parâmetros armazenados com sucesso!");
            // fechando porta serial
            closePort();
        } else{
            System.out.println("Porta não conectada. Verificar equipamento.");
        }
    }
    // FIM*********** Métodos Ajuste de Velocidade ***********
}