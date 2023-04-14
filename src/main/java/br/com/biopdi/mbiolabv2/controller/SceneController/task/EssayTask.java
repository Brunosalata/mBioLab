package br.com.biopdi.mbiolabv2.controller.SceneController.task;

import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemParameterDAO;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import com.fazecast.jSerialComm.SerialPort;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class EssayTask extends Task<String> {
    SystemParameterDAO systemParameterDAO;
    private SerialPort port;
    @Override
    protected String call() throws Exception {

        SystemParameter sysPar = systemParameterDAO.find();
        System.out.println("Conectado");
        port = SerialPort.getCommPort(sysPar.getPortName());
        System.out.println(port);
        if (port.openPort()) {
            port.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 50, 50);
            port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
            if (port.openPort()) {
                try {
                    Thread.sleep(2000);
                    outputInjection("1x");
                    Thread.sleep(20);
                    String impF = inputValue();
                    outputInjection("2x");
                    Thread.sleep(20);
                    String impP = inputValue();


                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                try{
                    //Incluir criação do gráfico
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } else {
            port.closePort();
        }


        return null;
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
     *
     * @return String
     */
    private String inputValue() {
        Scanner s = new Scanner(port.getInputStream());
        return s.nextLine();
    }
}
