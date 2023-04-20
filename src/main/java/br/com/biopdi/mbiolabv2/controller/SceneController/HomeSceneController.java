package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.SceneController.switchScene.SwitchMenuSceneController;
import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.model.bean.Essay;
import br.com.biopdi.mbiolabv2.model.bean.Setup;
import br.com.biopdi.mbiolabv2.model.bean.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class HomeSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    SwitchMenuSceneController switchScene = new SwitchMenuSceneController();
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private final SystemVariableDAO systemVariableDAO = new SystemVariableDAO();

    @FXML
    private TextField txtEssayUserId;
    @FXML
    private Label lbCurrentData, lbUserId, lbUserName, lbUserLogin, lbUserPassword;
    @FXML
    private Button btnEssayByUserId, btnSwitchToHomeScene, btnSwitchToDashboardScene, btnSwitchToEssayScene, btnSwitchToReportScene, btnSwitchToSettingScene;
    @FXML
    private ListView<Setup> setupListView;
    @FXML
    private ListView<User> userListView;
    @FXML
    private ListView<Essay> essayListView;
    @FXML
    private ListView<Essay> essayByUserListView;

    private final List<Setup> setupList = new ArrayList<>();
    private final List<User> userList = new ArrayList<>();
    private final List<Essay> essayList = new ArrayList<>();
    private final List<Essay> essayByUserIdList = new ArrayList<>();
    private ObservableList<Setup> obsSetupList;
    private ObservableList<User> obsUserList;
    private ObservableList<Essay> obsEssayList;
    private ObservableList<Essay> obsEssayByUserIdList;



    Date systemDate = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);



    /**
     * Thread que faz a leitura da posição em tempo real
     */


//    FIM ******************** Declarações iniciais **********************

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        // Mostra data local na base da aplicação
        lbCurrentData.setText(String.valueOf(systemDate));
    }

    /**
     * Método que invoca a TaskForcePositionViewTask, com atuação paralela à Thread principal e retorna o valor a ser atualizado na lbForceView e lbPositionView
     * Dá para usar no gráfico
     */
//    private void invokeForcePositionViewTask() {
//        ForcePositionViewTask forcePositionViewTask = new ForcePositionViewTask();
//        forcePositionViewTask.valueProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    lbForceView.setText(newValue);
//            }
//        });
//        Thread th = new Thread(forcePositionViewTask);
//        th.setDaemon(true);
//        th.start();
//    }











    // INICIO*********** Métodos de setup ***********







    // FIM*********** Métodos de setup ***********


    // INICIO******** Métodos de Busca no Banco de Dados ********

    @FXML
    private void essayFindByUser(){
        try{
            essayByUserIdList.addAll(essayDAO.findByUser(Integer.parseInt(txtEssayUserId.getText())));
            obsEssayByUserIdList = FXCollections.observableList(essayByUserIdList);
            essayByUserListView.setItems(obsEssayByUserIdList);
        } catch (Exception e){
        System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void DBDataPull(){

        userList.addAll(userDAO.findAll());
        obsUserList = FXCollections.observableList(userList);
        userListView.setItems(obsUserList);

        setupList.addAll(setupDAO.findAll());
        obsSetupList = FXCollections.observableList(setupList);
        setupListView.setItems(obsSetupList);

        essayList.addAll(essayDAO.findAll());
        obsEssayList = FXCollections.observableList(essayList);
        essayListView.setItems(obsEssayList);
    }

    private void ImagemInclusion(){
        //Compactação para armazenamento
        // imagenBitmap é a imagem a ser armazenada
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte imageBitmap[]=stream.toByteArray();
    }
    private void ImagemConsulta(){
        //Descompactação para leitura
//        DataBaseHandler db = new DataBaseHandler(this);
//        User user = db.getUserById(userId);
//
//        byte[] outImage=contato.getUserImage();
//        ByteArrayOutputStream imageStream = new ByteArrayInputStream(outImage);
//        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
//        image.setImageBitmap(imageBitmap);
    }

//                 ******** NECESSÁRIO TESTAR AINDA *********

    // USER OK
//    @FXML
//    public void userSave(){
//        user.save(new User(lbUserName.getText(), lbUserLogin.getText(), lbUserPassword.getText()));
//    }
//    @FXML
//    public void userDelete(){
//        userDAO.delete(userDAO.findById(Integer.parseInt(lbUserId.getText())));
//    }
//    @FXML
//    public void userUpdate(){
//        user.save(new User((Integer.parseInt(lbUserId.getText())), lbUserName.getText(), lbUserLogin.getText(), lbUserPassword.getText()));
//    }
    // SETUP OK
//    @FXML
//    public void setupSave(){
//        user.save(new User(lbUserName.getText(), lbUserLogin.getText(), lbUserPassword.getText()));
//    }
//    @FXML
//    public void setupDelete(){
//        userDAO.delete(userDAO.findById(Integer.parseInt(lbUserId.getText())));
//    }
//    @FXML
//    public void setupUpdate(){
//        user.save(new User((Integer.parseInt(lbUserId.getText())), lbUserName.getText(), lbUserLogin.getText(), lbUserPassword.getText()));
//    }
//    // ESSAY OK
//    @FXML
//    public void essaySave(){
//        user.save(new User(lbUserName.getText(), lbUserLogin.getText(), lbUserPassword.getText()));
//    }
//    @FXML
//    public void essayDelete(){
//        userDAO.delete(userDAO.findById(Integer.parseInt(lbUserId.getText())));
//    }

    // FIM******** Métodos de Busca no Banco de Dados ********




    // INICIO*********** Métodos de setup ***********




    // FIM*********** Métodos de setup ***********

}