package br.com.biopdi.mbiolabv2.model.bean;

import br.com.biopdi.mbiolabv2.controller.repository.dao.UserDAO;

import java.util.List;
import java.util.Objects;

public class User {
    private Integer userId;
    private String userName, userLogin, userPassword, userImagePath;

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
    public User(String userName, String userLogin, String userPassword, String userImagePath) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userImagePath = userImagePath;
    }
    public User(Integer userId, String userName, String userLogin, String userPassword, String userImagePath) {
        this.userId = userId;
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userImagePath = userImagePath;
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

    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }

    /**
     * Método equals e hashCode, para permitir a comparação entre elementos
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(userName, user.userName) && Objects.equals(userLogin, user.userLogin) && Objects.equals(userPassword, user.userPassword) && Objects.equals(userImagePath, user.userImagePath);
    }
    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userLogin, userPassword, userImagePath);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userLogin='" + userLogin + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userImagePath='" + userImagePath + '\'' +
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