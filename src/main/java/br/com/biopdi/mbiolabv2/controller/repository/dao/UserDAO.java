package br.com.biopdi.mbiolabv2.controller.repository.dao;




import br.com.biopdi.mbiolabv2.controller.repository.DBConnection;
import br.com.biopdi.mbiolabv2.model.bean.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDAO extends DBConnection {
    /**
     * Construtor da classe, que cria a tabela tb_user, caso ela não exista
     */
    public UserDAO(){
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tb_user ("
                    + "userId INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "userName TEXT,"
                    + "userLogin TEXT,"
                    + "userPassword TEXT);");
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     //     * Método de inclusão de usuario na tabela tb_user
     //     * @param user
     //     *
     //     * @Description chamar a função, instanciando DBConnection db e User user. Então, chama db.create()
     //     */
    public void create(User user){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_user VALUES(?,?,?,?);");
            stm.setString(2, user.getUserName());
            stm.setString(3, user.getUserLogin());
            stm.setString(4, user.getUserPassword());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que lista todos os elementos da tabela tb_user
     * @return lista de objetos em tb_user
     *
     * @Description chamada da função: db.findAll()
     */
    public List<User> findAll() {
        ArrayList<User> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_user ORDER BY userId ASC;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt(1),//user id
                        rs.getString(2),//username
                        rs.getString(3),//user login
                        rs.getString(4));//user password
                result.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }






    /**
     * Método que atualiza as informações de um usuario específico
     * @param user
     */
    public void update(User user){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_user SET "
                    + "userName = ?, "
                    + "userLogin = ?, "
                    + "userPassword = ? "
                    + "WHERE userId = ?;");
            stm.setString(1, user.getUserName());
            stm.setString(2, user.getUserLogin());
            stm.setString(3, user.getUserPassword());
            stm.setInt(4, user.getUserId());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que deleta o cadastro de um usuario específico
     * @param user
     */
    public void delete(User user){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("DELETE FROM tb_user WHERE userId = ?;");
            stm.setInt(1, user.getUserId());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de busca por um cadastro específico
     * @param pk
     * @return Cadastro do usuario pela primary key
     */
    public User find(int pk){
        User result = null;
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_user WHERE userId = ?;");
            stm.setInt(1, pk);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                User user = new User(
                        rs.getInt(1),//user id
                        rs.getString(2),//username
                        rs.getString(3),//user login
                        rs.getString(4));//user password
                result = user;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }
}
