package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.SceneController.switchScene.SwitchMenuSceneController;
import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemVariableDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.UserDAO;
import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import br.com.biopdi.mbiolabv2.model.bean.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private final SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    @FXML
    private AnchorPane apLogin, apUserRegister;
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private VBox vbRegister;
    private Stage stage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
                    apLogin.getScene().getWindow().hide();
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
        alert.setHeaderText("Precisa de ajuda?");
        alert.setContentText("Entre em contato com nossa equipe de atendimento pelo telefone (16) 3416-7080.");
//        alert.setGraphic(new ImageView(String.valueOf(getClass().getResource("br/com/biopdi/mbiolabv2/img/iconBiopdi.png"))));
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
        // Armazena userId = 0 nas variáveis de sistema para consulta global e limitação de acesso a dados
        SystemVariable sysVar = sysVarDAO.find();
        sysVar.setUserId(0);
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
            System.exit(0);
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
        stage.setResizable(false);  // Impede redimensionamento da janela
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

        FXMLLoader loader = new FXMLLoader(mBioLabv2Application.class.getResource("switchMenuScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("mBioLab");
        stage.getIcons().add(new Image(mBioLabv2Application.class.getResourceAsStream("img/iconBiopdi.png")));
        stage.setResizable(false);  // Impede redimensionamento da janela
        stage.setScene(scene);
        stage.show();
        stage.setFullScreen(true);

    }
}
