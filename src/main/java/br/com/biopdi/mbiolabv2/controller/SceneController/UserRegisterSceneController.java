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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserRegisterSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    @FXML
    private AnchorPane apUserRegister;
    @FXML
    private TextField txtName, txtLogin;
    @FXML
    private PasswordField txtPassword, txtPasswordConfirm;
    @FXML
    private Button btnDelete, btnEdit, btnCancel, btnRegister;
    @FXML
    private ImageView ivUser;
    @FXML
    private RadioButton rbAgreement;
    private Boolean ivInserted = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Inicia instanciando um objeto SystemVariable para ler o userId armazenado.
        SystemVariable sysVar = sysVarDAO.find();
        System.out.println(sysVar);
        System.out.println(sysVar.getPosition());
        System.out.println(sysVar.getForce());
        System.out.println(sysVar.getId());
        System.out.println(sysVar.getUserId());
        // Se getUserId()==null é porque não tem usuário logado, logo, botões Excluir e Editar são desabilitados
        if(sysVar.getUserId()!=null && sysVar.getUserId()!=0){
            btnDelete.setVisible(true);
            btnEdit.setVisible(true);
            btnRegister.setText("Salvar");
        } else{
            btnDelete.setVisible(false);
            btnEdit.setVisible(false);
            btnRegister.setText("Cadastrar");
        }
    }

    /**
     * REQUER CORREÇÃO >> Método que apresenta janela com alerta referente a suporte
     */
    @FXML
    private void supportRegisterMessage(){
        // apresenta mensagem de suporte
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Suporte");
        alert.setHeaderText("Precisa de ajuda?");
        alert.setContentText("Entre em contato com nossa equipe de atendimento pelo telefone (16) 3416-7080.");
//        alert.setGraphic(new ImageView(String.valueOf(getClass().getResource("br/com/biopdi/mbiolabv2/img/iconBiopdi.png"))));
        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        alert.showAndWait();
    }

    @FXML
    private void imageSelect(){

        // seleciona imagem para cadastro

    }

    @FXML
    private void profileEdit(){
        // edição do perfil

    }

    @FXML
    private void userDelete(){
        // deletar registro
        SystemVariable sysVar = sysVarDAO.find();
        User user = userDAO.findById(sysVar.getUserId());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Excluir usuário");
        alert.setHeaderText("Deseja mesmo excluir o usuário?");
        alert.setContentText("Nome: " + user.getUserName() + "/nLogin: " + user.getUserLogin());
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            userDAO.delete(user);
            System.exit(0);
            // RETONRAR À JANELA DE LOGIN

            // Deletar todos os ensaios realizados por ele?
            // Manter os dados, mas juntando em um pacote único (com demais usuários excluídos?
        }

    }

    /**
     * Método que valida os campos e salva um novo usuário no banco de dados
     */
    @FXML
    private void register() throws IOException {
        // realiza registro
        if(txtName.getText()=="" || txtLogin.getText()=="" || txtPassword.getText()=="") {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cadastro");
            alert.setHeaderText("Preenchimento incorreto!");
            alert.setContentText("Todos os campos são obrigatórios! Preencha novamente.");
            alert.show();
        } else{

            if(new String(txtPassword.getText()).equals(new String(txtPasswordConfirm.getText()))) {
                if(rbAgreement.isSelected()){
                    User user = new User();
                    user.setUserName(txtName.getText());
                    user.setUserLogin(txtLogin.getText());
                    user.setUserPassword(txtPassword.getText());
                    user.setUserImage(null);
                    userDAO.create(user);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Cadastro");
                    alert.setHeaderText("Sucesso!");
                    alert.setContentText("Cadastro realizado com sucesso.");
                    alert.showAndWait();
                    openNewScene("loginScene.fxml");
                    apUserRegister.getScene().getWindow().hide();
                } else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Termos e condições");
                    alert.setHeaderText("Necessário aceitar os termos.");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Senha divergente");
                alert.setHeaderText("Falhou!");
                alert.setContentText("Os campos de senha e confirmação devem ser iguais. Tente novamente.");
                alert.show();
            }
        }
    }

    /**
     * Método para fechar a janela de userRegister e retorna para a de Login
     */
    @FXML
    private void cancelRegister(){
        // fecha a janela de Registro para voltar à janela de Login
        apUserRegister.getScene().getWindow().hide();

    }

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
