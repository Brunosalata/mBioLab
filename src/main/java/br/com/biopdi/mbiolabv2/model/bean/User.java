package br.com.biopdi.mbiolabv2.model.bean;

import br.com.biopdi.mbiolabv2.controller.repository.dao.UserDAO;

import java.util.Arrays;
import java.util.List;

public class User {
    private Integer userId;
    private String userName, userLogin, userPassword;
    private byte[] userImage;

    public User(){
    }
    public User(String userName, String userLogin, String userPassword) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }
    public User(Integer userId, String userName, String userLogin, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }
    public User(String userName, String userLogin, String userPassword, byte[] userImage) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userImage = userImage;
    }
    public User(Integer userId, String userName, String userLogin, String userPassword, byte[] userImage) {
        this.userId = userId;
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userImage = userImage;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public byte[] getUserImage() {
        return userImage;
    }
    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userLogin='" + userLogin + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
    //------------------ DAO VERIFY -----------------

    private static UserDAO dao = new UserDAO();

    /**
     * Método save() que pode ser chamado diretamente da classe User
     */
    public void save(User user){
        if(userId != null && dao.findById(userId) != null){
            dao.update(this);
        } else {
            dao.create(this);
        }
    }
    /**
     * Método delete() que pode ser chamado diretamente da classe User
     */
    public void delete(int i){
        if(dao.findById(userId) != null){
            dao.delete(this);
        }
    }
    /**
     * Método findAll() que pode ser chamado diretamente da classe User
     */
    public static List<User> findAll(){
        return dao.findAll();
    }
    /**
     * Método find() que pode ser chamado diretamente da classe User
     */
    public static User findById(int pk){
        return dao.findById(pk);
    }

}