package br.com.biopdi.mbiolabv2.controller.SceneController;

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

import br.com.biopdi.mbiolabv2.controller.repository.dao.*;
import br.com.biopdi.mbiolabv2.model.bean.Schedule;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jfxtras.scene.control.CalendarPicker;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class HomeSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private final EssayDAO essayDAO = new EssayDAO();
    private final ScheduleDAO scheduleDAO = new ScheduleDAO();
    private final SetupDAO setupDAO = new SetupDAO();
    private final SystemParameterDAO systemParameterDAO = new SystemParameterDAO();
    private final SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private final SystemVariable sysVar = sysVarDAO.find();

    @FXML
    private CalendarPicker cpCalendar;
    @FXML
    private TextField txtEssayUserId;
    @FXML
    private Label lbUserId, lbUserName, lbUserLogin, lbUserPassword;
    @FXML
    private Button btnScheduleDelete, btnScheduleCreate;
    @FXML
    private ListView<Schedule> lvSchedule;
    private final List<Schedule> scheduleList = new ArrayList<>();
    private ObservableList<Schedule> obsSchedule;
    private Date date;
    private Schedule currentSchedule;

    java.util.Date systemDate = new java.util.Date();
    SimpleDateFormat scheduleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    SimpleDateFormat reducedScheduleDate = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat brasilianDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String currentDate = brasilianDate.format(systemDate);


//    FIM ******************** Declarações iniciais **********************

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Monitoramento de alterações do calendario
        cpCalendar.calendarProperty().addListener(new ChangeListener<Calendar>() {
            @Override
            public void changed(ObservableValue<? extends Calendar> observableValue, Calendar calendar, Calendar t1) {
                date = cpCalendar.getCalendar().getTime();
                scheduleListRefresh();
            }
        });
        lvSchedule.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Schedule>() {
            @Override
            public void changed(ObservableValue<? extends Schedule> observableValue, Schedule schedule, Schedule t1) {
                try {
                    // currentSchedule recebe o objeto selecionado na lvSchedule
                    currentSchedule = lvSchedule.getSelectionModel().getSelectedItem();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });

        cpCalendar.setCalendar(Calendar.getInstance());
        scheduleListRefresh();

    }

    /**
     * Metodo para agentamento de ensaio, recebendo date e armazenando no DB
     */
    @FXML
    private void scheduleSave(){
        // CRIAR ENTIDADE AGENDAMENTO (USERID, TIPO DE ENSAIO, ANO, MES, DIA, HORA, MINUTO)
        Schedule schedule = new Schedule(sysVar.getUserId(), scheduleDate.format(date));
        scheduleDAO.create(schedule);
        System.out.println(schedule);
        scheduleListRefresh();
    }

    /**
     * Metedo para exclusão de agendamento de horario
     */
    @FXML
    private void scheduleDelete(){
        if(sysVar.getUserId() == currentSchedule.getUserId() || sysVar.getUserId()<=2){
            scheduleDAO.delete(currentSchedule);
            scheduleListRefresh();
        } else{
            // Alerta de usuario agendado e logado divergentes
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Usuário divergente");
            alert.setHeaderText("Esse agendamento foi feito por outro usuário e não pode ser excluído por você.");
            Stage stage = (Stage) btnScheduleCreate.getScene().getWindow();
            alert.initOwner(stage);
            alert.show();
        }
    }

    /**
     * Metodo que listagem de ensaios agendados na lvSchedule
     */
    @FXML
    private void scheduleListRefresh(){
        try{
            lvSchedule.getItems().clear();
            lvSchedule.getItems().addAll(scheduleDAO.findAllByDay(reducedScheduleDate.format(date)));
            obsSchedule = FXCollections.observableList(scheduleList);
            lvSchedule.setItems(obsSchedule);
        } catch (Exception e) {
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
