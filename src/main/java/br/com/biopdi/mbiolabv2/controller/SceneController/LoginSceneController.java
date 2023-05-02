package br.com.biopdi.mbiolabv2.controller.SceneController;

import br.com.biopdi.mbiolabv2.controller.repository.dao.SystemVariableDAO;
import br.com.biopdi.mbiolabv2.controller.repository.dao.UserDAO;
import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;
import br.com.biopdi.mbiolabv2.model.bean.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private final SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    Stage stage;
    @FXML
    private AnchorPane apLogin, apUserRegister;
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtPassword;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     *  Método para realização do ‘login’, mediante senha
     */
    @FXML
    private void login() throws IOException {
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
                    systemVariable.setUseId(user.getUserId());
                    sysVarDAO.updateUser(systemVariable);
                    // Código para iniciar o menu principal
                    // abre janela de cadastro
                    openNewScene("switchMenuScene.fxml");
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
     * REQUER CORREÇÃO >> Método que retorna a cena de Registro de usuário
     * @throws IOException
     */
    @FXML
    private void openRegisterScene() throws IOException {
        // abre janela de cadastro
        openNewScene("userRegisterScene.fxml");
        apLogin.getScene().getWindow().hide();
    }

    /**
     * Método que acessa a aplicação sem necessidade de login
     * Não permite acessar ensaios salvos ou salvar ensaios
     */
    @FXML
    private void fastAccess() throws IOException {
        // Armazena userId = 0 nas variáveis de sistema para consulta global e limitação de acesso a dados
        SystemVariable systemVariable = sysVarDAO.find();
        systemVariable.setUseId(0);
        sysVarDAO.updateUser(systemVariable);
        openNewScene("switchMenuScene.fxml");
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
     * Método genbérico para abertura de nova janela
     * @param fxmlFile
     * @throws IOException
     */
    @FXML
    private void openNewScene(String fxmlFile) throws IOException {
        // abre janela de cadastro
        AnchorPane ap = FXMLLoader.load(mBioLabv2Application.class.getResource(fxmlFile));
        Scene scene = new Scene(ap);
        Stage stage = new Stage();
        stage.setTitle("mBioLab");
        stage.getIcons().add(new Image(mBioLabv2Application.class.getResourceAsStream("img/iconBiopdi.png")));
        stage.setResizable(false);  // Impede redimensionamento da janela
        stage.setScene(scene);
        stage.show();
    }
}
