package br.com.biopdi.mbiolabv2.controller.SceneController.runnable;

import br.com.biopdi.mbiolabv2.controller.SceneController.MainSceneController;
import com.fazecast.jSerialComm.SerialPort;
import javafx.scene.control.Label;

import java.io.PrintWriter;
import java.util.Scanner;

public class RunnableThread implements Runnable {

    public Label lbPositionView, lbForceView;
    public String name;
    public SerialPort port;

    public RunnableThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {

        while(true) {

            try {
                Thread.sleep(1000);
                outputInjection("1");
                Thread.sleep(20);
                lbForceView.setText(inputValue());
                outputInjection("2");
                Thread.sleep(20);
                lbPositionView.setText(inputValue());
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



