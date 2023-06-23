package br.com.biopdi.mbiolabv2.controller.repository.dao;

/*
 *  Copyright (c) 2023.
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

import br.com.biopdi.mbiolabv2.controller.repository.DBConnection;
import br.com.biopdi.mbiolabv2.model.bean.SystemParameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
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
                    + "soundOn TEXT,"
                    + "theme TEXT);");
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de inclusão de parametros na tabela tb_systemParameter
     *
     * @param systemParameter
     * @Description chamar a função, instanciando SystemParameterDAO systemParameterDAO e SystemParameter systemParameter.
     * Então, chama systemParameterDAO.create()
     */
    public void create(SystemParameter systemParameter) {
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_systemParameter(id, portName, systemLanguage, soundOn, theme) VALUES(?,?,?,?,?);");
            stm.setInt(1, 1);
            stm.setString(2, systemParameter.getPortName());
            stm.setString(3, systemParameter.getSystemLanguage());
            stm.setString(4, systemParameter.getSoundOn());
            stm.setString(5,systemParameter.getTheme());
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
     * @param systemParameter
     */
    public void update(SystemParameter systemParameter) {
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_systemParameter SET "
                    + "portName = ?, "
                    + "systemLanguage = ?, "
                    + "soundOn = ?, "
                    + "theme = ? "
                    + "WHERE id = 1;");
            stm.setString(1, systemParameter.getPortName());
            stm.setString(2, systemParameter.getSystemLanguage());
            stm.setString(3, systemParameter.getSoundOn());
            stm.setString(4,systemParameter.getTheme());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de busca por um método específico
     * Não possui parametro pois é tabela de linha única
     *
     * @return Cadastro de método pela primary key
     */
    public SystemParameter find() {
        SystemParameter result = null;
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT id, portName, systemLanguage, soundOn, theme FROM tb_systemParameter WHERE id = 1;");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                SystemParameter systemParameter = new SystemParameter(
                    rs.getInt("id"),//user id
                    rs.getString("portName"),//portName
                    rs.getString("systemLanguage"),//system Language
                    rs.getString("soundOn"),//soundOn
                    rs.getString("theme"));//theme
                result = systemParameter;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }
}
