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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
    private File imageFile;
    private String imageFilePath;
    private Stage stage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Inicia instanciando um objeto SystemVariable para ler o userId armazenado.
        SystemVariable sysVar = sysVarDAO.find();
        // Se getUserId()==null é porque não tem usuário logado, logo, botões Excluir e Editar são desabilitados
        if(sysVar.getUserId()!=0){
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
     * Método que apresenta janela com alerta referente a suporte
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

    /**
     * Método para seleção de imagem via FileChooser e armazena o Path da imagem
     */
    @FXML
    private void imageSelect() {
        // seleciona imagem para cadastro
        FileChooser f = new FileChooser();
        f.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens","*.jpg","*.jpeg","*.png","*.bitmap"));
        imageFile = f.showOpenDialog(new Stage());
        if(imageFile!=null){
            ivUser.setImage(new Image(imageFile.getPath()));
            imageFilePath = imageFile.getPath();
        }
    }

    @FXML
    private void profileEdit(){
        // edição do perfil

    }

    /**
     * Método para deleção de usuário
     * @throws IOException
     */
    @FXML
    private void userDelete() throws IOException {
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
            // Retorna à janela de login
            openNewScene("loginScene.fxml");
            apUserRegister.getScene().getWindow().hide();

            // Deletar todos os ensaios realizados por ele?
            // Manter os dados, mas juntando em um pacote único (com demais usuários excluídos?
        }

    }

    /**
     * Método que valida os campos e salva um novo usuário no banco de dados
     */
    @FXML
    private void register() throws IOException {
        // Busca user pelo login
        User userValidation = userDAO.findByLogin(txtLogin.getText());
        if(userValidation!=null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cadastro");
            alert.setHeaderText("Esse Login já existe! Escolha outro.");
            alert.show();
        } else if (txtName.getText()=="" || txtLogin.getText()=="" || txtPassword.getText()=="") {
            // realiza registro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cadastro");
            alert.setHeaderText("Preenchimento incorreto!");
            alert.setContentText("Todos os campos são obrigatórios! Preencha novamente.");
            alert.show();
        } else if (new String(txtPassword.getText()).equals(new String(txtPasswordConfirm.getText()))) {
            if(rbAgreement.isSelected()){
                User user = new User();
                user.setUserName(txtName.getText());
                user.setUserLogin(txtLogin.getText());
                user.setUserPassword(txtPassword.getText());
                if(imageFile!=null){
                    user.setUserImagePath(imageFilePath);
                    userDAO.createWithImage(user);
                } else {
                    userDAO.create(user);
                }
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

    /**
     * Método para fechar a janela de userRegister e retorna para a de Login
     */
    @FXML
    private void cancelRegister(){
        // fecha a janela de Registro para voltar à janela de Login
        apUserRegister.getScene().getWindow().hide();
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
