package br.com.biopdi.mbiolabv2.controller.SceneController;

/*
 *  Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class UserRegisterSceneController implements Initializable {
    //    INICIO ******************** Declarações iniciais **********************
    private final UserDAO userDAO = new UserDAO();
    private SystemVariableDAO sysVarDAO = new SystemVariableDAO();
    private SystemVariable sysVar = sysVarDAO.find();
    @FXML
    private AnchorPane apLogin, apUserRegister, apUseTerm;
    @FXML
    private TextField txtName, txtLogin;
    @FXML
    private PasswordField txtPassword, txtPasswordConfirm;
    @FXML
    private Button btnDelete, btnCancel, btnRegister, btnUserUpdate;
    @FXML
    private ImageView ivUser;
    @FXML
    private RadioButton rbAgreement;
    private File imageFile;
    private String imageFilePath;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicia instanciando um objeto SystemVariable para ler o userId armazenado.
        // Se getUserId()==null é porque não tem usuário logado, logo, botões Excluir e Editar são desabilitados
        if(sysVar.getUserId()==2 || sysVar.getUserId()>=4){
            User user = userDAO.findById(sysVar.getUserId());
            txtName.setText(user.getUserName());
            txtLogin.setText(user.getUserLogin());
            txtPassword.setText("*****");
            txtPassword.setDisable(true);
            txtPasswordConfirm.setVisible(false);
            btnDelete.setVisible(true);
            btnRegister.setVisible(false);
            btnUserUpdate.setVisible(true);
            apUseTerm.setVisible(false);
        } else if(sysVar.getUserId()==1){
            btnRegister.setDisable(true);
            btnDelete.setDisable(true);
        }else{
            btnDelete.setVisible(false);
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

    /**
     * Metodo para edicao das informacoes do usuario ja cadastrado
     */
    @FXML
    private void userUpdate(){
        User user = userDAO.findById(sysVar.getUserId());
        user.setUserName(txtName.getText());
        user.setUserLogin(txtLogin.getText());
        user.setUserPassword(user.getUserPassword());
        user.setUserImagePath(user.getUserImagePath());
        userDAO.update(user);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Editar");
        alert.setHeaderText("Dados alterados com sucesso!");
        alert.show();

    }

    /**
     * Método para deleção de usuário
     * @throws IOException
     */
    @FXML
    private void userDelete(ActionEvent event) throws IOException {
        // deletar registro
        SystemVariable sysVar = sysVarDAO.find();
        User user = userDAO.findById(sysVar.getUserId());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Excluir usuário");
        alert.setHeaderText("Deseja mesmo excluir " + user.getUserLogin() + "?");
        alert.setContentText("ATENÇÃO: Seus ensaios não serão excluídos. Para excluí-los, solicite que o Administrador delete seu perfil.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            userDAO.delete(user);
            // Retorna à janela de login
            openLoginScene(event);
            // Ensaios não serão deletados automaticamente, apenas o Admin tem o poder de excluir um usuário e
            // seus ensaios, utilizando o painel em SystemSetting (Configuracoes)
            // Manter os dados, mas juntando em um pacote único (com demais usuários excluídos?
        }

    }

    /**
     * Método que valida os campos e salva um novo usuário no banco de dados
     */
    @FXML
    private void register(ActionEvent event) throws IOException {
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
                user.setUserImagePath(imageFilePath);
                userDAO.create(user);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cadastro");
                alert.setHeaderText("Sucesso!");
                alert.setContentText("Cadastro realizado com sucesso.");
                alert.showAndWait();
                openLoginScene(event);
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
    private void cancelRegister(ActionEvent event) throws IOException {
        // fecha a janela de Registro para voltar à janela de Login
        openLoginScene(event);
    }

    /**
     * Método genbérico para abertura de nova janela
     * @param event
     * @throws IOException
     */
    @FXML
    private void openLoginScene(ActionEvent event) throws IOException {
        // Abre a cena de login
        FXMLLoader loader = new FXMLLoader(mBioLabv2Application.class.getResource("loginScene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("mBioLab");
        stage.getIcons().add(new Image(mBioLabv2Application.class.getResourceAsStream("img/iconBiopdi.png")));
        stage.setResizable(false);  // Impede redimensionamento da janela
        stage.setScene(scene);
        stage.show();
    }
}
