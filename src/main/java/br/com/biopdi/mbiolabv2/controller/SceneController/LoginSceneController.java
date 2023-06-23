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

import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemVariableDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.UserDAO;
import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import br.com.biopdi.mbiolabv2.model.bean.User;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class LoginSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private final SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    @FXML
    private AnchorPane apLogin, apUserRegister;
    public static AnchorPane rootP;
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private VBox vbRegister;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!mBioLabv2Application.isSplashLoaded){
            loadSplashScreen();
        }
        rootP = apLogin;
    }

    /**
     *  Método para realização do ‘login’, mediante senha
     */
    @FXML
    private void login(ActionEvent event) throws IOException {
        // Busca user pelo login
        User user = userDAO.findByLogin(txtLogin.getText());
        // Verifica se os campos de login e senha estão preenchidos
        if(txtLogin.getText()=="" || txtPassword.getText()=="") {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cadastro");
            alert.setHeaderText("Preenchimento incorreto!");
            alert.setContentText("Todos os campos são obrigatórios! Preencha novamente.");
            alert.show();
        } else {
            // Verificação de existência de usuário
            if (user == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login");
                alert.setHeaderText("Usuário inexistente");
                alert.show();
            } else {
                // Verifica se login e senha digitados conmferem com as informações salvas no banco de dados
                if (txtLogin.getText().equals(user.getUserLogin()) && txtPassword.getText().equals(user.getUserPassword())) {
                    // Armazena userId nas variáveis de sistema para consulta global
                    SystemVariable systemVariable = sysVarDAO.find();
                    systemVariable.setUserId(user.getUserId());
                    sysVarDAO.updateUser(systemVariable);
                    // Código para iniciar o menu principal
                    // abre janela de cadastro
                    openSwitchMenuFullScene(event);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login");
                    alert.setHeaderText("Falhou!");
                    alert.setContentText("Login ou senha inválido! Tente novamente.");
                    alert.show();
                }
            }
        }
    }

    /**
     * Método que apresenta janela com alerta referente a suporte
     */
    @FXML
    private void supportMessage(){
        // apresenta mensagem de suporte
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Suporte");
        alert.setGraphic(new ImageView(mBioLabv2Application.class.getResource("img/darkIcon/icons8-suporte-on-line-96.png").toString()));
        alert.setHeaderText("Precisa de ajuda?");
        alert.setContentText("Entre em contato com nossa equipe de atendimento pelo telefone (16) 3416-7080.");
        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        alert.showAndWait();
    }

    /**
     * Método que retorna a cena de Registro de usuário
     * @throws IOException
     */
    @FXML
    private void openRegisterScene(ActionEvent event) throws IOException {
        // abre janela de cadastro
        openUserRegisterScene(event);
    }

    /**
     * Método que acessa a aplicação sem necessidade de login
     * Não permite acessar ensaios salvos ou salvar ensaios
     */
    @FXML
    private void fastAccess(ActionEvent event) throws IOException {
        // ALTERAR Armazena userId = 3 nas variáveis de sistema para consulta global e limitação de acesso a dados
        SystemVariable sysVar = sysVarDAO.find();
        sysVar.setUserId(1);
        sysVarDAO.updateUser(sysVar);
        openSwitchMenuFullScene(event);
    }

    /**
     * Método de cancalamento do login
     */
    @FXML
    private void cancelLogin(){
        // cancela login
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancelar login");
        alert.setHeaderText("Sair da aplicação");
        alert.setContentText("Deseja mesmo fechar o programa?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            Platform.exit();
        }
    }

    /**
     * Metodo generico para abertura de nova janela
     * @param event
     * @throws IOException
     */
    @FXML
    private void openUserRegisterScene(ActionEvent event) throws IOException {
        // abre janela de cadastro
        FXMLLoader loader = new FXMLLoader(mBioLabv2Application.class.getResource("userRegisterScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("mBioLab");
        stage.getIcons().add(new Image(mBioLabv2Application.class.getResourceAsStream("img/iconBiopdi.png")));
        stage.setResizable(false); // Impede redimensionamento da janela
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Metodo generico para abertura de nova janela full screem
     * @param event
     * @throws IOException
     */
    @FXML
    private void openSwitchMenuFullScene(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(mBioLabv2Application.class.getResource("switchMenuScene3.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("mBioLab");
        stage.getIcons().add(new Image(mBioLabv2Application.class.getResourceAsStream("img/iconBiopdi.png")));
        stage.setResizable(false);  // Impede redimensionamento da janela
        stage.setFullScreenExitHint("Pressione o botão 'Sair' para sair");
        stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

    }

    private void loadSplashScreen() {
        try {
            mBioLabv2Application.isSplashLoaded = true;
            //Load splash screen view FXML
            AnchorPane pane = FXMLLoader.load(mBioLabv2Application.class.getResource("splashScene.fxml"));
            //Add it to root container (Can be StackPane, AnchorPane etc)
            apLogin.getChildren().setAll(pane);

            //Load splash screen with fade in effect
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            //Finish splash with fade out effect
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            //After fade in, start fade out
            fadeIn.setOnFinished((e) -> {
                fadeOut.play();
            });

            //After fade out, load actual content
            fadeOut.setOnFinished((e) -> {
                try {
                    AnchorPane parentContent = FXMLLoader.load(mBioLabv2Application.class.getResource("loginScene.fxml"));
                    apLogin.getChildren().setAll(parentContent);
                } catch (IOException ex) {
                    Logger.getLogger(mBioLabv2Application.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(mBioLabv2Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
