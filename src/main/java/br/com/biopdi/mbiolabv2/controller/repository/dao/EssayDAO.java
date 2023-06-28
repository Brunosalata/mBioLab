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
import br.com.biopdi.mbiolabv2.model.bean.Essay;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class EssayDAO extends DBConnection {

    /**
     * Construtor da classe, que cria a tabela tb_essay, caso ela não exista
     */
    public EssayDAO() {
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tb_essay ("
                    + "essayId INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "userId INTEGER  NOT NULL,"
                    + "essayIdentification TEXT,"
                    + "essayType TEXT,"
                    + "essayNorm TEXT,"
                    + "essayUsedMachine TEXT,"
                    + "essayChargeCell DOUBLE,"
                    + "essayInitialForce DOUBLE,"
                    + "essayFinalForce DOUBLE,"
                    + "essayInitialPosition DOUBLE,"
                    + "essayFinalPosition DOUBLE,"
                    + "essaydislocationVelocity DOUBLE,"
                    + "essayTemperature DOUBLE,"
                    + "essayPreCharge DOUBLE,"
                    + "essayRelativeHumidity DOUBLE,"
                    + "essayMaxForce DOUBLE,"
                    + "essayMaxPosition DOUBLE,"
                    + "essayMaxTension DOUBLE,"
                    + "essayEscapeTension DOUBLE,"
                    + "essayAlong DOUBLE,"
                    + "essayAreaRed DOUBLE,"
                    + "essayMYoung DOUBLE,"
                    + "essayChart TEXT,"
                    + "essayDay DATE,"
                    + "essayHour DATE);");
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de inclusão de ensaio na tabela tb_essay
     * @param essay
     *
     * @Description chamar a função, instanciando EssayDAO essayDAO e Essay essay. Então, chama essayDAO.create()
     *
     */
    public void create(Essay essay) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_essay(essayId, userId, essayIdentification, " +
                    "essayType, essayNorm, essayUsedMachine, essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, " +
                    "essayFinalPosition, essaydislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity, " +
                    "essayMaxForce, essayMaxPosition, essayMaxTension, essayEscapeTension, essayAlong, essayAreaRed, " +
                    "essayMYoung, essayChart, essayDay, essayHour) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            stm.setInt(2, essay.getUserId());
            stm.setString(3, essay.getEssayIdentification());
            stm.setString(4, essay.getEssayType());
            stm.setString(5, essay.getEssayNorm());
            stm.setString(6, essay.getEssayUsedMachine());
            stm.setDouble(7, essay.getEssayChargeCell());
            stm.setDouble(8, essay.getEssayInitialForce());
            stm.setDouble(9, essay.getEssayFinalForce());
            stm.setDouble(10, essay.getEssayInitialPosition());
            stm.setDouble(11, essay.getEssayFinalPosition());
            stm.setDouble(12, essay.getEssayDislocationVelocity());
            stm.setDouble(13, essay.getEssayTemperature());
            stm.setDouble(14, essay.getEssayPreCharge());
            stm.setDouble(15, essay.getEssayRelativeHumidity());
            stm.setDouble(16,essay.getEssayMaxForce());
            stm.setDouble(17,essay.getEssayMaxPosition());
            stm.setDouble(18,essay.getEssayMaxTension());
            stm.setDouble(19,essay.getEssayEscapeTension());
            stm.setDouble(20,essay.getEssayAlong());
            stm.setDouble(21,essay.getEssayAreaRed());
            stm.setDouble(22,essay.getEssayMYoung());
            stm.setString(23, essay.getEssayChart());
            stm.setString(24, String.valueOf((essay.getEssayDay())));
            stm.setString(25, String.valueOf((essay.getEssayHour())));
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que lista todos os elementos da tabela tb_essay
     *
     * @return lista de objetos em tb_essay
     * @Description chamada da função: db.findAll()
     */
    public List<Essay> findAll() {
        ArrayList<Essay> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT essayId, userId, essayIdentification, essayType, essayNorm, " +
                    "essayUsedMachine, essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, " +
                    "essayFinalPosition, essaydislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity, " +
                    "essayMaxForce, essayMaxPosition, essayMaxTension, essayEscapeTension, essayAlong, essayAreaRed, " +
                    "essayMYoung, essayChart, essayDay, essayHour FROM tb_essay ORDER BY essayId ASC;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Essay essay = new Essay(
                        rs.getInt("essayId"),//essay id
                        rs.getInt("userId"),//essay user id
                        rs.getString("essayIdentification"),//essay identification
                        rs.getString("essayType"),//essay Type
                        rs.getString("essayNorm"),//essay norm
                        rs.getString("essayUsedMachine"),//essay Charge cell
                        rs.getDouble("essayChargeCell"),//essay Used Machine
                        rs.getDouble("essayInitialForce"),//essay Initial Force
                        rs.getDouble("essayFinalForce"),//essay Final Force
                        rs.getDouble("essayInitialPosition"),//essay Initial Position
                        rs.getDouble("essayFinalPosition"),//essay Final Position
                        rs.getDouble("essaydislocationVelocity"),//essay Dislocation velocity
                        rs.getDouble("essayTemperature"),//essay Temperature
                        rs.getDouble("essayPreCharge"),//essay Pre Charge
                        rs.getDouble("essayRelativeHumidity"),//essay Relative Humidity
                        rs.getDouble("essayMaxForce"),// essay Max Force
                        rs.getDouble("essayMaxPosition"),// essay Max Position
                        rs.getDouble("essayMaxTension"),// essay MAx Tension
                        rs.getDouble("essayEscapeTension"),// essay Escape Tension
                        rs.getDouble("essayAlong"),// essay Along
                        rs.getDouble("essayAreaRed"),// essay Area Redution
                        rs.getDouble("essayMYoung"),// essay MYoung
                        rs.getString("essayChart"),//essay Chart
                        rs.getString("essayDay"),//essay Day
                        rs.getString("essayHour"));//essay Hour
                result.add(essay);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método que atualiza as informações de um ensaio específico
     *
     * @param essay
     */
    public void update(Essay essay) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_essay SET "
                    + "userId = ?,"
                    + "essayIdentification = ?,"
                    + "essayType  = ?,"
                    + "essayNorm  = ?,"
                    + "essayUsedMachine = ?,"
                    + "essayChargeCell = ?,"
                    + "essayInitialForce = ?,"
                    + "essayFinalForce = ?,"
                    + "essayInitialPosition = ?,"
                    + "essayFinalPosition = ?,"
                    + "essaydislocationVelocity = ?,"
                    + "essayTemperature = ?,"
                    + "essayPreCharge = ?,"
                    + "essayRelativeHumidity = ?,"
                    + "essayMaxForce = ?,"
                    + "essayMaxPosition = ?,"
                    + "essayMaxTension = ?,"
                    + "essayEscapeTension = ?,"
                    + "essayAlong = ?,"
                    + "essayAreaRed = ?,"
                    + "essayMYoung = ?,"
                    + "essayChart = ?,"
                    + "essayDay = ? "
                    + "essayHour = ? "
                    + "WHERE essayId = ?;");
            stm.setInt(1, essay.getUserId());
            stm.setString(2, essay.getEssayIdentification());
            stm.setString(3, essay.getEssayType());
            stm.setString(4, essay.getEssayNorm());
            stm.setString(5, essay.getEssayUsedMachine());
            stm.setDouble(6, essay.getEssayChargeCell());
            stm.setDouble(7, essay.getEssayInitialForce());
            stm.setDouble(8, essay.getEssayFinalForce());
            stm.setDouble(9, essay.getEssayInitialPosition());
            stm.setDouble(10, essay.getEssayFinalPosition());
            stm.setDouble(11, essay.getEssayDislocationVelocity());
            stm.setDouble(12, essay.getEssayPreCharge());
            stm.setDouble(13, essay.getEssayPreCharge());
            stm.setDouble(14, essay.getEssayRelativeHumidity());
            stm.setDouble(15,essay.getEssayMaxForce());
            stm.setDouble(16,essay.getEssayMaxPosition());
            stm.setDouble(17,essay.getEssayMaxTension());
            stm.setDouble(18,essay.getEssayEscapeTension());
            stm.setDouble(19,essay.getEssayAlong());
            stm.setDouble(20,essay.getEssayAreaRed());
            stm.setDouble(21,essay.getEssayMYoung());
            stm.setString(22, essay.getEssayChart());
            stm.setString(23, String.valueOf((essay.getEssayDay())));
            stm.setString(24, String.valueOf((essay.getEssayHour())));
            stm.setInt(25, essay.getEssayId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que deleta o registro de um ensaio específico
     *
     * @param essay
     */
    public void delete(Essay essay) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("DELETE FROM tb_essay WHERE essayId = ?;");
            stm.setInt(1, essay.getEssayId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de busca por um ensaio específico
     *
     * @param pk
     * @return Cadastro de ensaio pela primary key
     */
    public Essay findById(int pk) {
        Essay result = null;
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT essayId, userId, essayIdentification, essayType, essayNorm, " +
                    "essayUsedMachine, essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, " +
                    "essayFinalPosition, essaydislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity, " +
                    "essayMaxForce, essayMaxPosition, essayMaxTension, essayEscapeTension, essayAlong, essayAreaRed, " +
                    "essayMYoung, essayChart, essayDay, essayHour FROM tb_essay WHERE essayId = ?;");
            stm.setInt(1, pk);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Essay essay = new Essay(
                        rs.getInt("essayId"),//essay id
                        rs.getInt("userId"),//essay user id
                        rs.getString("essayIdentification"),//essay identification
                        rs.getString("essayType"),//essay Type
                        rs.getString("essayNorm"),//essay norm
                        rs.getString("essayUsedMachine"),//essay Charge cell
                        rs.getDouble("essayChargeCell"),//essay Used Machine
                        rs.getDouble("essayInitialForce"),//essay Initial Force
                        rs.getDouble("essayFinalForce"),//essay Final Force
                        rs.getDouble("essayInitialPosition"),//essay Initial Position
                        rs.getDouble("essayFinalPosition"),//essay Final Position
                        rs.getDouble("essaydislocationVelocity"),//essay Dislocation velocity
                        rs.getDouble("essayTemperature"),//essay Temperature
                        rs.getDouble("essayPreCharge"),//essay Pre Charge
                        rs.getDouble("essayRelativeHumidity"),//essay Relative Humidity
                        rs.getDouble("essayMaxForce"),// essay Max Force
                        rs.getDouble("essayMaxPosition"),// essay Max Position
                        rs.getDouble("essayMaxTension"),// essay MAx Tension
                        rs.getDouble("essayEscapeTension"),// essay Escape Tension
                        rs.getDouble("essayAlong"),// essay Along
                        rs.getDouble("essayAreaRed"),// essay Area Redution
                        rs.getDouble("essayMYoung"),// essay MYoung
                        rs.getString("essayChart"),//essay Chart
                        rs.getString("essayDay"),//essay Day
                        rs.getString("essayHour"));//essay Hour
                result = essay;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método que chama todos os ensaios relacionados ao usuário indicado
     *
     * @param userId
     * @return
     */
    public List<Essay> findByUser(int userId) {
        ArrayList<Essay> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT essayId, userId, essayIdentification, essayType, essayNorm, " +
                    "essayUsedMachine, essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, " +
                    "essayFinalPosition, essaydislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity, " +
                    "essayMaxForce, essayMaxPosition, essayMaxTension, essayEscapeTension, essayAlong, essayAreaRed, " +
                    "essayMYoung, essayChart, essayDay, essayHour FROM tb_essay WHERE userId = ?;");
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Essay essay = new Essay(
                        rs.getInt("essayId"),//essay id
                        rs.getInt("userId"),//essay user id
                        rs.getString("essayIdentification"),//essay identification
                        rs.getString("essayType"),//essay Type
                        rs.getString("essayNorm"),//essay norm
                        rs.getString("essayUsedMachine"),//essay Charge cell
                        rs.getDouble("essayChargeCell"),//essay Used Machine
                        rs.getDouble("essayInitialForce"),//essay Initial Force
                        rs.getDouble("essayFinalForce"),//essay Final Force
                        rs.getDouble("essayInitialPosition"),//essay Initial Position
                        rs.getDouble("essayFinalPosition"),//essay Final Position
                        rs.getDouble("essaydislocationVelocity"),//essay Dislocation velocity
                        rs.getDouble("essayTemperature"),//essay Temperature
                        rs.getDouble("essayPreCharge"),//essay Pre Charge
                        rs.getDouble("essayRelativeHumidity"),//essay Relative Humidity
                        rs.getDouble("essayMaxForce"),// essay Max Force
                        rs.getDouble("essayMaxPosition"),// essay Max Position
                        rs.getDouble("essayMaxTension"),// essay MAx Tension
                        rs.getDouble("essayEscapeTension"),// essay Escape Tension
                        rs.getDouble("essayAlong"),// essay Along
                        rs.getDouble("essayAreaRed"),// essay Area Redution
                        rs.getDouble("essayMYoung"),// essay MYoung
                        rs.getString("essayChart"),//essay Chart
                        rs.getString("essayDay"),//essay Day
                        rs.getString("essayHour"));//essay Hour
                result.add(essay);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método que chama todos os ensaios pela data indicada
     *
     * @param essayData
     * @return
     */
    public List<Essay> findByDate(String essayData) {
        ArrayList<Essay> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT essayId, userId, essayIdentification, essayType, essayNorm, " +
                    "essayUsedMachine, essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, " +
                    "essayFinalPosition, essaydislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity, " +
                    "essayMaxForce, essayMaxPosition, essayMaxTension, essayEscapeTension, essayAlong, essayAreaRed, " +
                    "essayMYoung, essayChart, essayDay, essayHour FROM tb_essay WHERE essayData = ?;");
            stm.setInt(1, Integer.parseInt(essayData));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Essay essay = new Essay(
                        rs.getInt("essayId"),//essay id
                        rs.getInt("userId"),//essay user id
                        rs.getString("essayIdentification"),//essay identification
                        rs.getString("essayType"),//essay Type
                        rs.getString("essayNorm"),//essay norm
                        rs.getString("essayUsedMachine"),//essay Charge cell
                        rs.getDouble("essayChargeCell"),//essay Used Machine
                        rs.getDouble("essayInitialForce"),//essay Initial Force
                        rs.getDouble("essayFinalForce"),//essay Final Force
                        rs.getDouble("essayInitialPosition"),//essay Initial Position
                        rs.getDouble("essayFinalPosition"),//essay Final Position
                        rs.getDouble("essaydislocationVelocity"),//essay Dislocation velocity
                        rs.getDouble("essayTemperature"),//essay Temperature
                        rs.getDouble("essayPreCharge"),//essay Pre Charge
                        rs.getDouble("essayRelativeHumidity"),//essay Relative Humidity
                        rs.getDouble("essayMaxForce"),// essay Max Force
                        rs.getDouble("essayMaxPosition"),// essay Max Position
                        rs.getDouble("essayMaxTension"),// essay MAx Tension
                        rs.getDouble("essayEscapeTension"),// essay Escape Tension
                        rs.getDouble("essayAlong"),// essay Along
                        rs.getDouble("essayAreaRed"),// essay Area Redution
                        rs.getDouble("essayMYoung"),// essay MYoung
                        rs.getString("essayChart"),//essay Chart
                        rs.getString("essayDay"),//essay Day
                        rs.getString("essayHour"));//essay Hour
                result.add(essay);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método que chama todos os ensaios pela norma indicada
     *
     * @param norm
     * @return
     */
    public List<Essay> findByNorm(String norm) {
        ArrayList<Essay> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT essayId, userId, essayIdentification, essayType, essayNorm, " +
                    "essayUsedMachine, essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, " +
                    "essayFinalPosition, essaydislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity, " +
                    "essayMaxForce, essayMaxPosition, essayMaxTension, essayEscapeTension, essayAlong, essayAreaRed, " +
                    "essayMYoung, essayChart, essayDay, essayHour FROM tb_essay WHERE essayNorm = ?;");
            stm.setInt(1, Integer.parseInt(norm));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Essay essay = new Essay(
                        rs.getInt("essayId"),//essay id
                        rs.getInt("userId"),//essay user id
                        rs.getString("essayIdentification"),//essay identification
                        rs.getString("essayType"),//essay Type
                        rs.getString("essayNorm"),//essay norm
                        rs.getString("essayUsedMachine"),//essay Charge cell
                        rs.getDouble("essayChargeCell"),//essay Used Machine
                        rs.getDouble("essayInitialForce"),//essay Initial Force
                        rs.getDouble("essayFinalForce"),//essay Final Force
                        rs.getDouble("essayInitialPosition"),//essay Initial Position
                        rs.getDouble("essayFinalPosition"),//essay Final Position
                        rs.getDouble("essaydislocationVelocity"),//essay Dislocation velocity
                        rs.getDouble("essayTemperature"),//essay Temperature
                        rs.getDouble("essayPreCharge"),//essay Pre Charge
                        rs.getDouble("essayRelativeHumidity"),//essay Relative Humidity
                        rs.getDouble("essayMaxForce"),// essay Max Force
                        rs.getDouble("essayMaxPosition"),// essay Max Position
                        rs.getDouble("essayMaxTension"),// essay MAx Tension
                        rs.getDouble("essayEscapeTension"),// essay Escape Tension
                        rs.getDouble("essayAlong"),// essay Along
                        rs.getDouble("essayAreaRed"),// essay Area Redution
                        rs.getDouble("essayMYoung"),// essay MYoung
                        rs.getString("essayChart"),//essay Chart
                        rs.getString("essayDay"),//essay Day
                        rs.getString("essayHour"));//essay Hour
                result.add(essay);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método que chama todos os ensaios pelo tipo indicado de ensaio
     *
     * @param essayType
     * @return
     */
    public List<Essay> findByType(String essayType) {
        ArrayList<Essay> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT essayId, userId, essayIdentification, essayType, essayNorm, " +
                    "essayUsedMachine, essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, " +
                    "essayFinalPosition, essaydislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity, " +
                    "essayMaxForce, essayMaxPosition, essayMaxTension, essayEscapeTension, essayAlong, essayAreaRed, " +
                    "essayMYoung, essayChart, essayDay, essayHour FROM tb_essay WHERE essayType = ?;");
            stm.setInt(1, Integer.parseInt(essayType));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Essay essay = new Essay(
                        rs.getInt("essayId"),//essay id
                        rs.getInt("userId"),//essay user id
                        rs.getString("essayIdentification"),//essay identification
                        rs.getString("essayType"),//essay Type
                        rs.getString("essayNorm"),//essay norm
                        rs.getString("essayUsedMachine"),//essay Charge cell
                        rs.getDouble("essayChargeCell"),//essay Used Machine
                        rs.getDouble("essayInitialForce"),//essay Initial Force
                        rs.getDouble("essayFinalForce"),//essay Final Force
                        rs.getDouble("essayInitialPosition"),//essay Initial Position
                        rs.getDouble("essayFinalPosition"),//essay Final Position
                        rs.getDouble("essaydislocationVelocity"),//essay Dislocation velocity
                        rs.getDouble("essayTemperature"),//essay Temperature
                        rs.getDouble("essayPreCharge"),//essay Pre Charge
                        rs.getDouble("essayRelativeHumidity"),//essay Relative Humidity
                        rs.getDouble("essayMaxForce"),// essay Max Force
                        rs.getDouble("essayMaxPosition"),// essay Max Position
                        rs.getDouble("essayMaxTension"),// essay MAx Tension
                        rs.getDouble("essayEscapeTension"),// essay Escape Tension
                        rs.getDouble("essayAlong"),// essay Along
                        rs.getDouble("essayAreaRed"),// essay Area Redution
                        rs.getDouble("essayMYoung"),// essay MYoung
                        rs.getString("essayChart"),//essay Chart
                        rs.getString("essayDay"),//essay Day
                        rs.getString("essayHour"));//essay Hour
                result.add(essay);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método de busca o último ensaio salvo
     */
    public Essay findLastId() {
        Essay result = null;
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT essayId, userId, essayIdentification, essayType, essayNorm, " +
                    "essayUsedMachine, essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, " +
                    "essayFinalPosition, essaydislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity, " +
                    "essayMaxForce, essayMaxPosition, essayMaxTension, essayEscapeTension, essayAlong, essayAreaRed, " +
                    "essayMYoung, essayChart, essayDay, essayHour FROM tb_essay ORDER BY essayId DESC limit 1;");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Essay essay = new Essay(
                        rs.getInt("essayId"),//essay id
                        rs.getInt("userId"),//essay user id
                        rs.getString("essayIdentification"),//essay identification
                        rs.getString("essayType"),//essay Type
                        rs.getString("essayNorm"),//essay norm
                        rs.getString("essayUsedMachine"),//essay Charge cell
                        rs.getDouble("essayChargeCell"),//essay Used Machine
                        rs.getDouble("essayInitialForce"),//essay Initial Force
                        rs.getDouble("essayFinalForce"),//essay Final Force
                        rs.getDouble("essayInitialPosition"),//essay Initial Position
                        rs.getDouble("essayFinalPosition"),//essay Final Position
                        rs.getDouble("essaydislocationVelocity"),//essay Dislocation velocity
                        rs.getDouble("essayTemperature"),//essay Temperature
                        rs.getDouble("essayPreCharge"),//essay Pre Charge
                        rs.getDouble("essayRelativeHumidity"),//essay Relative Humidity
                        rs.getDouble("essayMaxForce"),// essay Max Force
                        rs.getDouble("essayMaxPosition"),// essay Max Position
                        rs.getDouble("essayMaxTension"),// essay MAx Tension
                        rs.getDouble("essayEscapeTension"),// essay Escape Tension
                        rs.getDouble("essayAlong"),// essay Along
                        rs.getDouble("essayAreaRed"),// essay Area Redution
                        rs.getDouble("essayMYoung"),// essay MYoung
                        rs.getString("essayChart"),//essay Chart
                        rs.getString("essayDay"),//essay Day
                        rs.getString("essayHour"));//essay Hour
                result = essay;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método de busca o último ensaio salvo
     */
    public Essay findLastIdByUser(int pk) {
        Essay result = null;
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT essayId, userId, essayIdentification, essayType, essayNorm, " +
                    "essayUsedMachine, essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, " +
                    "essayFinalPosition, essaydislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity, " +
                    "essayMaxForce, essayMaxPosition, essayMaxTension, essayEscapeTension, essayAlong, essayAreaRed, " +
                    "essayMYoung, essayChart, essayDay, essayHour FROM tb_essay WHERE userId = " + pk + " ORDER BY essayId DESC limit 1;");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Essay essay = new Essay(
                        rs.getInt("essayId"),//essay id
                        rs.getInt("userId"),//essay user id
                        rs.getString("essayIdentification"),//essay identification
                        rs.getString("essayType"),//essay Type
                        rs.getString("essayNorm"),//essay norm
                        rs.getString("essayUsedMachine"),//essay Charge cell
                        rs.getDouble("essayChargeCell"),//essay Used Machine
                        rs.getDouble("essayInitialForce"),//essay Initial Force
                        rs.getDouble("essayFinalForce"),//essay Final Force
                        rs.getDouble("essayInitialPosition"),//essay Initial Position
                        rs.getDouble("essayFinalPosition"),//essay Final Position
                        rs.getDouble("essaydislocationVelocity"),//essay Dislocation velocity
                        rs.getDouble("essayTemperature"),//essay Temperature
                        rs.getDouble("essayPreCharge"),//essay Pre Charge
                        rs.getDouble("essayRelativeHumidity"),//essay Relative Humidity
                        rs.getDouble("essayMaxForce"),// essay Max Force
                        rs.getDouble("essayMaxPosition"),// essay Max Position
                        rs.getDouble("essayMaxTension"),// essay MAx Tension
                        rs.getDouble("essayEscapeTension"),// essay Escape Tension
                        rs.getDouble("essayAlong"),// essay Along
                        rs.getDouble("essayAreaRed"),// essay Area Redution
                        rs.getDouble("essayMYoung"),// essay MYoung
                        rs.getString("essayChart"),//essay Chart
                        rs.getString("essayDay"),//essay Day
                        rs.getString("essayHour"));//essay Hour
                result = essay;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }
}

