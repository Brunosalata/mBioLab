package br.com.biopdi.mbiolabv2.controller.repository.dao;

import br.com.biopdi.mbiolabv2.controller.repository.DBConnection;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;
import br.com.biopdi.mbiolabv2.model.bean.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemParameterDAO extends DBConnection {
    /**
     * Construtor da classe, que cria a tabela tb_systemParameter, caso ela não exista
     */
    public SystemParameterDAO() {
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tb_systemParameter ("
                    + "id,"
                    + "portName TEXT,"
                    + "systemLanguage TEXT,"
                    + "soundOn TEXT);");
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     //     * Método de inclusão de parametros na tabela tb_systemParameter
     //     * @param systemParameter
     //     *
     //     * @Description chamar a função, instanciando SystemParameterDAO systemParameterDAO e SystemParameter systemParameter.
     //     * Então, chama systemParameterDAO.create()
     //     */
    public void create(SystemParameter systemParameter){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_systemParameter VALUES(?,?,?,?);");
            stm.setInt(1, 1);
            stm.setString(2, systemParameter.getPortName());
            stm.setString(3, systemParameter.getSystemLanguage());
            stm.setString(4, systemParameter.getSoundOn());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que atualiza os parâmetros do sistema, informados pelo usuario
     * @param systemParameter
     */
    public void update(SystemParameter systemParameter){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_systemParameter SET "
                    + "portName = ?, "
                    + "systemLanguage = ?, "
                    + "soundOn = ? "
                    + "WHERE id = 1;");
            stm.setString(1, systemParameter.getPortName());
            stm.setString(2, systemParameter.getSystemLanguage());
            stm.setString(3, systemParameter.getSoundOn());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de busca por um método específico
     * Não possui parametro pois é tabela de linha única
     * @return Cadastro de método pela primary key
     */
    public SystemParameter find(){
        SystemParameter result = null;
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_systemParameter WHERE id = 1;");
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                SystemParameter systemParameter = new SystemParameter(
                        rs.getInt(1),//user id
                        rs.getString(2),//portName
                        rs.getString(3),//system Language
                        rs.getString(4));//soundOn
                result = systemParameter;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }
}
