package br.com.biopdi.mbiolabv2.controller.repository.dao;

import br.com.biopdi.mbiolabv2.controller.repository.DBConnection;
import br.com.biopdi.mbiolabv2.model.bean.Essay;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EssayDAO extends DBConnection {

    /**
     * Construtor da classe, que cria a tabela tb_essay, caso ela não exista
     */
    public EssayDAO(){
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tb_essay ("
                + "essayId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "UserId INTEGER  NOT NULL,"
//                CONSTRAINT userId FOREIGN KEY (userId) REFERENCES tb_user (userId)
                + "essayIdentification TEXT,"
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
                + "essayGraphic BLOB,"
                + "essayData DATE NOT NULL);");
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     //     * Método de inclusão de ensaio na tabela tb_essay
     //     * @param essay
     //     *
     //     * @Description chamar a função, instanciando EssayDAO essayDAO e Essay essay. Então, chama essayDAO.create()
     //     */
    public void create(Essay essay){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_essay VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            stm.setInt(2, essay.getUserId());
            stm.setString(3, essay.getEssayIdentification());
            stm.setString(4, essay.getEssayNorm());
            stm.setString(5, essay.getEssayUsedMachine());
            stm.setDouble(6, essay.getEssayChargeCell());
            stm.setDouble(7, essay.getEssayInitialForce());
            stm.setDouble(8, essay.getEssayFinalForce());
            stm.setDouble(9, essay.getEssayInitialPosition());
            stm.setDouble(10, essay.getEssayFinalPosition());
            stm.setDouble(11, essay.getEssayDislocationVelocity());
            stm.setDouble(12, essay.getEssayTemperature());
            stm.setDouble(13, essay.getEssayPreCharge());
            stm.setDouble(14, essay.getEssayRelativeHumidity());
            stm.setString(16, String.valueOf((essay.getEssayDate())));
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que lista todos os elementos da tabela tb_essay
     * @return lista de objetos em tb_essay
     *
     * @Description chamada da função: db.findAll()
     */
    public List<Essay> findAll() {
        ArrayList<Essay> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_essay ORDER BY essayId ASC;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Essay essay = new Essay(
                    rs.getInt(1),//essay id
                    rs.getInt(2),//essay user id
                    rs.getString(3),//essay identification
                    rs.getString(4),//essay norm
                    rs.getString(5),//essay Charge cell
                    rs.getDouble(6),//essay Used Machine
                    rs.getDouble(7),//essay Initial Force
                    rs.getDouble(8),//essay Final Force
                    rs.getDouble(9),//essay Initial Position
                    rs.getDouble(10),//essay Final Position
                    rs.getDouble(11),//essay Dislocation velocity
                    rs.getDouble(12),//essay Temperature
                    rs.getDouble(13),//essay Pre Charge
                    rs.getDouble(14),//essay Relative Humidity
                    rs.getBytes(15),//essay Graphic
                    rs.getDate(16));//essay Data
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
     * @param essay
     */
    public void update(Essay essay){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_essay SET "
                + "essayUserId = ?,"
                + "essayIdentification = ?,"
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
                + "essayGraphic = ?,"
                + "essayData = ? "
                + "WHERE essayId = ?;");
            stm.setInt(1, essay.getUserId());
            stm.setString(2, essay.getEssayIdentification());
            stm.setString(3, essay.getEssayNorm());
            stm.setString(4, essay.getEssayUsedMachine());
            stm.setDouble(5, essay.getEssayChargeCell());
            stm.setDouble(6, essay.getEssayInitialForce());
            stm.setDouble(7, essay.getEssayFinalForce());
            stm.setDouble(8, essay.getEssayInitialPosition());
            stm.setDouble(9, essay.getEssayFinalPosition());
            stm.setDouble(10, essay.getEssayDislocationVelocity());
            stm.setDouble(11, essay.getEssayPreCharge());
            stm.setDouble(12, essay.getEssayPreCharge());
            stm.setDouble(13, essay.getEssayRelativeHumidity());
            stm.setBytes(14, essay.getEssayGraphic());
            stm.setString(15, String.valueOf((essay.getEssayDate())));
            stm.setInt(16, essay.getEssayId());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que deleta o registro de um ensaio específico
     * @param essay
     */
    public void delete(Essay essay){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("DELETE FROM tb_essay WHERE essayId = ?;");
            stm.setInt(1, essay.getUserId());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de busca por um ensaio específico
     * @param pk
     * @return Cadastro de ensaio pela primary key
     */
    public Essay findById(int pk){
        Essay result = null;
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_essay WHERE essayId = ?;");
            stm.setInt(1, pk);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                Essay essay = new Essay(
                    rs.getInt(1),//essay id
                    rs.getInt(2),//essay user id
                    rs.getString(3),//essay identification
                    rs.getString(4),//essay norm
                    rs.getString(5),//essay Charge cell
                    rs.getDouble(6),//essay Used Machine
                    rs.getDouble(7),//essay Initial Force
                    rs.getDouble(8),//essay Final Force
                    rs.getDouble(9),//essay Initial Position
                    rs.getDouble(10),//essay Final Position
                    rs.getDouble(11),//essay Dislocation velocity
                    rs.getDouble(12),//essay Temperature
                    rs.getDouble(13),//essay Pre Charge
                    rs.getDouble(14),//essay Relative Humidity
                    rs.getBytes(15),//essay Graphic
                    rs.getTimestamp(16));//essay Data
                result = essay;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }




    public List<Essay> findByUser(int userId) {
        ArrayList<Essay> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_essay WHERE essayUserId = ?;");
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Essay essay = new Essay(
                        rs.getInt(1),//essay id
                        rs.getInt(2),//essay user id
                        rs.getString(3),//essay identification
                        rs.getString(4),//essay norm
                        rs.getString(5),//essay Charge cell
                        rs.getDouble(6),//essay Used Machine
                        rs.getDouble(7),//essay Initial Force
                        rs.getDouble(8),//essay Final Force
                        rs.getDouble(9),//essay Initial Position
                        rs.getDouble(10),//essay Final Position
                        rs.getDouble(11),//essay Dislocation velocity
                        rs.getDouble(12),//essay Temperature
                        rs.getDouble(13),//essay Pre Charge
                        rs.getDouble(14),//essay Relative Humidity
                        rs.getBytes(15),//essay Graphic
                        rs.getTimestamp(16));//essay Data
                result.add(essay);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }



}
