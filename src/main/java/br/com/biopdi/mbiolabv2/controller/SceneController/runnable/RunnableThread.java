package br.com.biopdi.mbiolabv2.controller.SceneController.runnable;

import br.com.biopdi.mbiolabv2.controller.SceneController.MainSceneController;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.PrintWriter;
import java.util.Scanner;

public class RunnableThread extends Thread {

    MainSceneController ms = new MainSceneController();
    public Label lbPositionView, lbForceView;
    public String name;
    public String SelectedPort;
    public SerialPort port;

    public RunnableThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        if (port!=null) {
            System.out.println(port);
            try{
                SelectedPort = ms.getCbPorts().getSelectionModel().getSelectedItem().toString();
            port = SerialPort.getCommPort(SelectedPort);
                System.out.println(port);
            port.openPort();
                System.out.println(port);
            while (true) {
                System.out.println(port);
                try {
                    Thread.sleep(1000);
                    outputInjection("1x");
                    Thread.sleep(20);

                    double impF = Double.parseDouble(inputValue());
                    outputInjection("2x");
                    Thread.sleep(20);
                    double impP = Double.parseDouble(inputValue());
                    Platform.runLater(() -> {
                    lbForceView.setText(String.valueOf(impF));
                    lbPositionView.setText(String.valueOf(impP));
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            } catch (RuntimeException e) {
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



