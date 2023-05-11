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
    public UserDAO() {
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tb_user ("
                    + "userId INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "userName TEXT,"
                    + "userLogin TEXT,"
                    + "userPassword TEXT,"
                    + "userImage BLOB);");
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de inclusão de usuario sem imagem na tabela tb_user
     *
     * @param user
     * @Description chamar a função, instanciando UserDAO userDAO e User user. Então, chama db.create()
     */
    public void create(User user) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_user(userId, userName, userLogin, userPassword, userImagePath) VALUES (?,?,?,?,?);");
            var i = 1;
            stm.setString(++i, user.getUserName());
            stm.setString(++i, user.getUserLogin());
            stm.setString(++i, user.getUserPassword());
            stm.setString(++i, user.getUserImagePath() != null ? user.getUserImagePath() : null);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que lista todos os elementos da tabela tb_user
     *
     * @return lista de objetos em tb_user
     * @Description chamada da função: db.findAll()
     */
    public List<User> findAll() {
        ArrayList<User> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT userId, userName, userLogin, userPassword, userImagePath FROM tb_user ORDER BY userId ASC;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getInt("userId"),//user id
                    rs.getString("userName"),//username
                    rs.getString("userLogin"),//user login
                    rs.getString("userPassword"));//user password
                    rs.getString("userImagePath");//user image
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
     *
     * @param user
     */
    public void update(User user) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_user SET "
                    + "userName = ?, "
                    + "userLogin = ?, "
                    + "userPassword = ?, "
                    + "userImagePath = ? "
                    + "WHERE userId = ?;");
            stm.setString(1, user.getUserName());
            stm.setString(2, user.getUserLogin());
            stm.setString(3, user.getUserPassword());
            stm.setString(4, user.getUserImagePath());
            stm.setInt(5, user.getUserId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que deleta o cadastro de um usuario específico
     *
     * @param user
     */
    public void delete(User user) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("DELETE FROM tb_user WHERE userId = ?;");
            stm.setInt(1, user.getUserId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de busca por um cadastro específico
     *
     * @param pk
     * @return Cadastro do usuario pela primary key
     */
    public User findById(int pk) {
        User result = null;
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT userId, userName, userLogin, userPassword, userImagePath FROM tb_user WHERE userId = ?;");
            stm.setInt(1, pk);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("userId"),//user id
                        rs.getString("userName"),//username
                        rs.getString("userLogin"),//user login
                        rs.getString("userPassword"));//user password
                        rs.getString("userImagePath");//user image
                result = user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método de busca por um cadastro específico
     *
     * @param stg
     * @return Cadastro do usuario pela primary key
     */
    public User findByLogin(String stg) {
        User result = null;
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT userId, userName, userLogin, userPassword, userImagePath FROM tb_user WHERE userLogin = ?;");
            stm.setString(1, stg);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getInt("userId"),//user id
                    rs.getString("userName"),//username
                    rs.getString("userLogin"),//user login
                    rs.getString("userPassword"));//user password
                    rs.getString("userImagePath");//user image
                result = user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }
}
