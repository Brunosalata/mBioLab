package br.com.biopdi.mbiolabv2.controller.SceneController.switchScene;

import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class FxmlLoader {
    private Pane view;

    public Pane getPage(String fileName){
        try{
            URL fileUrl = mBioLabv2Application.class.getResource(fileName);
            if(fileUrl == null){
                throw new java.io.FileNotFoundException("O arquivo FXML não foi encontrado!");
            }
            view = new FXMLLoader().load(fileUrl);


        } catch (Exception e) {
            System.out.println("A página " + fileName + " não foi encontrada. Por favor, verifique a classe FxmlLoader.");
            throw new RuntimeException(e);
        }

        return view;
    }
}
