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
import br.com.biopdi.mbiolabv2.model.bean.Schedule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salata Lima - 07/06/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project Essay.java
 */
public class ScheduleDAO extends DBConnection {

    /**
     * Construtor da classe, que cria a tabela tb_user, caso ela não exista
     */
    public ScheduleDAO() {
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tb_schedule ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "userId INTEGER,"
                    + "date TEXT);");
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
     * @param schedule
     * @Description chamar a função, instanciando UserDAO userDAO e User user. Então, chama db.create()
     */
    public void create(Schedule schedule) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_schedule(id, userId, date) VALUES (?,?,?);");
            var i = 1;
            stm.setInt(++i, schedule.getUserId());
            stm.setString(++i, schedule.getDate());
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
    public List<Schedule> findAll() {
        ArrayList<Schedule> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT id, userId, date FROM tb_schedule ORDER BY " +
                    "SUBSTR( date, 7,4)," +
                    "SUBSTR( date, 4,2)," +
                    "SUBSTR( date, 1,2)," +
                    "SUBSTR( date, 12,2)," +
                    "SUBSTR( date, 16,2);");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule(
                        rs.getInt("id"),//id
                        rs.getInt("userId"),//user Id
                        rs.getString("date"));//date
                result.add(schedule);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método que lista todos os elementos da tabela tb_schedule
     *
     * @return lista de objetos em tb_schedule
     */
    public List<Schedule> findAllByDay(String date) {
        ArrayList<Schedule> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT id, userId, date FROM tb_schedule " +
                    "WHERE SUBSTR( date, 1,10) = ? " +
                    "ORDER BY " +
                    "SUBSTR( date, 7,4)," +
                    "SUBSTR( date, 4,2)," +
                    "SUBSTR( date, 1,2)," +
                    "SUBSTR( date, 12,2)," +
                    "SUBSTR( date, 16,2);");
            stm.setString(1, date); // data selecionada
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule(
                        rs.getInt("id"),//id
                        rs.getInt("userId"),//user Id
                        rs.getString("date"));//date
                result.add(schedule);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método que deleta o cadastro de um usuario específico
     *
     * @param schedule
     */
    public void delete(Schedule schedule) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("DELETE FROM tb_schedule WHERE id = ?;");
            stm.setInt(1, schedule.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}
