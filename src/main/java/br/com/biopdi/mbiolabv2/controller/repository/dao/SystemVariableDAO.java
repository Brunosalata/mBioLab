package br.com.biopdi.mbiolabv2.controller.repository.dao;

import br.com.biopdi.mbiolabv2.controller.repository.DBConnection;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemVariableDAO extends DBConnection {

    /**
     * Construtor da classe, que cria a tabela tb_systemVariable, caso ela não exista
     */
    public SystemVariableDAO() {
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tb_systemVariable ("
                    + "id,"
                    + "force REAL,"
                    + "position REAL);");
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de inclusão de valores de força e posição na tabela tb_systemVariable
     *
     * @param systemVariable
     * @Description chamar a função, instanciando SystemVariableDAO systemVariableDAO e SystemVariable systemVariable.
     * Então, chama systemVariableDAO.create()
     */
    public void create(SystemVariable systemVariable) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_systemVariable VALUES(?,?,?);");
            stm.setDouble(1, systemVariable.getId());
            stm.setDouble(2, systemVariable.getForce());
            stm.setDouble(3, systemVariable.getPosition());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que atualiza os parâmetros do sistema, informados pelo usuario
     *
     * @param systemVariable
     */
    public void update(SystemVariable systemVariable) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_systemVariable SET "
                    + "force = ?, "
                    + "position = ? "
                    + "WHERE id = 1;");
            stm.setDouble(1, systemVariable.getForce());
            stm.setDouble(2, systemVariable.getPosition());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de busca pelas variáveis de sistema
     * Não possui parametro pois é tabela de linha única
     *
     * @return Cadastro de método pela primary key
     */
    public SystemVariable find() {
        SystemVariable result = null;
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_systemVariable WHERE id = 1;");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                SystemVariable systemVariable = new SystemVariable(
                        rs.getInt(1),//id
                        rs.getDouble(2),//force
                        rs.getDouble(3));//position
                result = systemVariable;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

}
