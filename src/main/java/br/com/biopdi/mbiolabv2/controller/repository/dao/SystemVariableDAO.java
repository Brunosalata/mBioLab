package br.com.biopdi.mbiolabv2.controller.repository.dao;

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

import br.com.biopdi.mbiolabv2.controller.repository.DBConnection;
import br.com.biopdi.mbiolabv2.model.bean.SystemVariable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class SystemVariableDAO extends DBConnection {

    /**
     * Construtor da classe, que cria a tabela tb_systemVariable, caso ela não exista
     */
    public SystemVariableDAO() {
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tb_systemVariable ("
                    + "id INTEGER,"
                    + "force REAL,"
                    + "position REAL,"
                    + "userId INTEGER);");
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
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_systemVariable(id, force, position, userId) VALUES(?,?,?,?);");
            stm.setInt(1, systemVariable.getId());
            stm.setDouble(2, systemVariable.getForce());
            stm.setDouble(3, systemVariable.getPosition());
            stm.setInt(4,systemVariable.getUserId());
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
    public void updateEssay(SystemVariable systemVariable) {
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
     * Método que atualiza o valor do parâmetro userId ({id}), ao fazer login ou iniciar sistema (0)
     *
     * @param systemVariable
     */
    public void updateUser(SystemVariable systemVariable) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_systemVariable SET "
                    + "userId = ? "
                    + "WHERE id = 1;");
            stm.setInt(1,systemVariable.getUserId());
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
            PreparedStatement stm = conn.prepareStatement("SELECT id, force, position, userId FROM tb_systemVariable WHERE id = 1;");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                SystemVariable systemVariable = new SystemVariable(
                        rs.getInt("id"),//id
                        rs.getDouble("force"),//force
                        rs.getDouble("position"),//position
                        rs.getInt("userId"));//userId
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
