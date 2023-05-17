package br.com.biopdi.mbiolabv2.controller.SceneController.switchScene;

import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SwitchSceneController {
    private FXMLLoader loader;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private URL url;
    private Node node;
    public void switchToHomeScene(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getClassLoader().getResource("homeScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSystemSettingScene(ActionEvent event) throws IOException {
        node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("systemSettingScene.fxml"));



//        stage = (Stage)(node.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToDashboardScene(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getClassLoader().getResource("dashboardScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToEssayScene(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getClassLoader().getResource("essayScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToReportScene(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getClassLoader().getResource("reportScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoginScene(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getClassLoader().getResource("loginScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToUserRegisterScene(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getClassLoader().getResource("userRegisterScene.fxml"));
        stage = (Stage)(node.getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

//    @FXML
//    void goTo(ActionEvent event) {
//        Node node = (Node) event.getSource();
//
//        Stage stage = (Stage) node.getScene().getWindow();
//        Parent root = null;
//        try {
//            root = FXMLLoader.load(getClass().getResource(“Pesagem.fxml”));
//        } catch (IOException ex) {
//            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

}
