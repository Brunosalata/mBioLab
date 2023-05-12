package br.com.biopdi.mbiolabv2.controller.SceneController;

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

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private final SystemVariableDAO systemVariableDAO = new SystemVariableDAO();

    @FXML
    private TextField txtEssayUserId;
    @FXML
    private Label lbUserId, lbUserName, lbUserLogin, lbUserPassword;
    @FXML
    private Button btnEssayByUserId, btnDataPull;
    @FXML
    private ListView<Essay> lvEssayByDate;
    private final List<Essay> essayByDateList = new ArrayList<>();
    private ObservableList<Essay> obsEssayByDateList;

    java.util.Date systemDate = new java.util.Date();
    SimpleDateFormat dateComplete = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);


//    FIM ******************** Declarações iniciais **********************

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        essayByDate();


    }

    /**
     * Método que busca e apresenta listagem de ensaios em função do essayId
     */
    @FXML
    private void essayByDate(){
        try{
            lvEssayByDate.getItems().clear();
            lvEssayByDate.getItems().addAll(essayDAO.findByDate(currentDate));
            obsEssayByDateList = FXCollections.observableList(essayByDateList);
            lvEssayByDate.setItems(obsEssayByDateList);
        } catch (Exception e){
        System.out.println("Error: " + e.getMessage());
        }
    }

}


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
