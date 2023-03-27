package br.com.biopdi.mbiolabv2.model.bean;

import br.com.biopdi.mbiolabv2.controller.repository.dao.UserDAO;

import java.util.List;

public class User {
    private Integer userId;
    private String userName, userLogin, userPassword;

    public User(String userName, String userLogin, String userPassword) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    public User(int userId, String userName, String userLogin, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
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
//    public void save(){
//        if(userId != null && dao.find(userId) != null){
//            dao.update(this);
//        } else {
//            dao.create(this);
//        }
//    }
//    /**
//     * Método delete() que pode ser chamado diretamente da classe User
//     */
//    public void delete(){
//        if(dao.find(userId) != null){
//            dao.delete(this);
//        }
//    }
//    /**
//     * Método findAll() que pode ser chamado diretamente da classe User
//     */
//    public static List<User> findAll(){
//        return dao.findAll();
//    }
//    /**
//     * Método find() que pode ser chamado diretamente da classe User
//     */
//    public static User find(int pk){
//        return dao.find(pk);
//    }

}