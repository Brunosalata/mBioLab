package br.com.biopdi.mbiolabv2.controller.SceneController.runnable;

import br.com.biopdi.mbiolabv2.controller.SceneController.HomeSceneController;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemParameterDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemVariableDAO;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.PrintWriter;
import java.util.Scanner;

public class RunnableThread extends Thread {
    @FXML
    private Label lbPositionView, lbForceView;
    private String name;
    private SerialPort port;
    private SystemVariableDAO systemVariableDAO;

    public RunnableThread(String name) {
        this.name = name;
    }

    @FXML
    @Override
    public void run() {

        while (true) {

            try{

                outputInjection("1x");
                Thread.sleep(20);
                String impF = inputValue();
                System.out.println(impF);
                outputInjection("2x");
                Thread.sleep(20);
                String impP = inputValue();
                System.out.println(impP);

                SystemVariable sysVar = new SystemVariable(1,Double.valueOf(impF),Double.valueOf(impP));
                systemVariableDAO.update(sysVar);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void outputInjection(String stg) {
        PrintWriter output = new PrintWriter(port.getOutputStream(), true);
        output.print(stg);
        output.flush();
    }
    private String inputValue() {
        Scanner s = new Scanner(port.getInputStream());
        return s.nextLine();
    }
}



