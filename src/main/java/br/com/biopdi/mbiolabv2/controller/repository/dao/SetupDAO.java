package br.com.biopdi.mbiolabv2.controller.repository.dao;

import br.com.biopdi.mbiolabv2.controller.repository.DBConnection;
import br.com.biopdi.mbiolabv2.model.bean.Setup;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SetupDAO extends DBConnection {

    /**
     * Construtor da classe, que cria a tabela tb_essay, caso ela não exista
     */
    public SetupDAO(){
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tb_setup ("
                + "setupId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "userId INTEGER,"
//                    CONSTRAINT essayUserId FOREIGN KEY (userId) REFERENCES tb_user (userId)
                + "MC1M1 INTEGER,"
                + "MC2M1 INTEGER,"
                + "MC3M1 INTEGER,"
                + "MC4M1 INTEGER,"
                + "MC5M1 INTEGER,"
                + "MC6M1 INTEGER,"
                + "MC7M1 INTEGER,"
                + "MC8M1 INTEGER,"
                + "MC9M1 INTEGER,"
                + "MC10M1 INTEGER,"
                + "MC11M1 INTEGER,"
                + "MC12M1 INTEGER,"
                + "MC13M1 INTEGER,"
                + "MC14M1 INTEGER,"
                + "MC15M1 INTEGER,"
                + "MC16M1 INTEGER,"
                + "MC17M1 INTEGER,"
                + "MC18M1 INTEGER,"
                + "MC19M1 INTEGER,"
                + "MC20M1 INTEGER,"
                + "MC21M1 INTEGER,"
                + "MC22M1 INTEGER,"
                + "MC23M1 INTEGER,"
                + "MC24M1 INTEGER,"
                + "MC25M1 INTEGER,"
                + "MC26M1 INTEGER,"
                + "MC27M1 INTEGER,"
                + "MC28M1 INTEGER,"
                + "MC29M1 INTEGER,"
                + "MC30M1 INTEGER,"
                + "channel1ext1 DOUBLE,"
                + "channel2ext1 DOUBLE,"
                + "channel3ext1 DOUBLE,"
                + "channel4ext1 DOUBLE,"
                + "channel1ext2 DOUBLE,"
                + "channel2ext2 DOUBLE,"
                + "channel3ext2 DOUBLE,"
                + "channel4ext2 DOUBLE,"
                + "setupName TEXT,"
                + "setupAuthor TEXT,"
                + "setupData DATE NOT NULL);");
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     //     * Método de inclusão de ensaio na tabela tb_setup
     //     * @param essay
     //     *
     //     * @Description chamar a função, instanciando SetupDAO setupDAO e Setup setup. Então, chama setupDAO.create()
     //     */
    public void create(Setup setup){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_setup VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            stm.setInt(2, setup.getUserId());
            stm.setInt(3, setup.getMC1M1());
            stm.setInt(4, setup.getMC2M1());
            stm.setInt(5, setup.getMC3M1());
            stm.setInt(6, setup.getMC4M1());
            stm.setInt(7, setup.getMC5M1());
            stm.setInt(8, setup.getMC6M1());
            stm.setInt(9, setup.getMC7M1());
            stm.setInt(10, setup.getMC8M1());
            stm.setInt(11, setup.getMC9M1());
            stm.setInt(12, setup.getMC10M1());
            stm.setInt(13, setup.getMC11M1());
            stm.setInt(14, setup.getMC12M1());
            stm.setInt(15, setup.getMC13M1());
            stm.setInt(16, setup.getMC14M1());
            stm.setInt(17, setup.getMC15M1());
            stm.setInt(18, setup.getMC16M1());
            stm.setInt(19, setup.getMC17M1());
            stm.setInt(20, setup.getMC18M1());
            stm.setInt(21, setup.getMC19M1());
            stm.setInt(22, setup.getMC20M1());
            stm.setInt(23, setup.getMC21M1());
            stm.setInt(24, setup.getMC22M1());
            stm.setInt(25, setup.getMC23M1());
            stm.setInt(26, setup.getMC24M1());
            stm.setInt(27, setup.getMC25M1());
            stm.setInt(28, setup.getMC26M1());
            stm.setInt(29, setup.getMC27M1());
            stm.setInt(30, setup.getMC28M1());
            stm.setInt(31, setup.getMC29M1());
            stm.setInt(32, setup.getMC30M1());
            stm.setInt(33, setup.getChannel1ext1());
            stm.setInt(34, setup.getChannel2ext1());
            stm.setInt(35, setup.getChannel3ext1());
            stm.setInt(36, setup.getChannel4ext1());
            stm.setInt(37, setup.getChannel1ext2());
            stm.setInt(38, setup.getChannel2ext2());
            stm.setInt(39, setup.getChannel3ext2());
            stm.setInt(40, setup.getChannel4ext2());
            stm.setString(41, setup.getSetupName());
            stm.setString(42, setup.getSetupAuthor());
            stm.setString(43, String.valueOf((setup.getSetupDate())));
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que lista todos os elementos da tabela tb_setup
     * @return lista de objetos em tb_setup
     *
     * @Description chamada da função: db.findAll()
     */
    public List<Setup> findAll() {
        ArrayList<Setup> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_setup ORDER BY setupId ASC;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Setup setup = new Setup(
                    rs.getInt(1),//setup id
                    rs.getInt(2),//setup user id
                    rs.getInt(3),//setup MC1M1
                    rs.getInt(4),//setup MC2M1
                    rs.getInt(5),//setup MC3M1
                    rs.getInt(6),//setup MC4M1
                    rs.getInt(7),//setup MC5M1
                    rs.getInt(8),//setup MC6M1
                    rs.getInt(9),//setup MC7M1
                    rs.getInt(10),//setup MC8M1
                    rs.getInt(11),//setup MC9M1
                    rs.getInt(12),//setup MC10M1
                    rs.getInt(13),//setup MC11M1
                    rs.getInt(14),//setup MC12M1
                    rs.getInt(15),//setup MC13M1
                    rs.getInt(16),//setup MC14M1
                    rs.getInt(17),//setup MC15M1
                    rs.getInt(18),//setup MC16M1
                    rs.getInt(19),//setup MC17M1
                    rs.getInt(20),//setup MC18M1
                    rs.getInt(21),//setup MC19M1
                    rs.getInt(22),//setup MC20M1
                    rs.getInt(23),//setup MC21M1
                    rs.getInt(24),//setup MC22M1
                    rs.getInt(25),//setup MC23M1
                    rs.getInt(26),//setup MC24M1
                    rs.getInt(27),//setup MC25M1
                    rs.getInt(28),//setup MC26M1
                    rs.getInt(29),//setup MC27M1
                    rs.getInt(30),//setup MC28M1
                    rs.getInt(31),//setup MC29M1
                    rs.getInt(32),//setup MC30M1
                    rs.getInt(33),//setup channel1ext1
                    rs.getInt(34),//setup channel2ext1
                    rs.getInt(35),//setup channel3ext1
                    rs.getInt(36),//setup channel4ext1
                    rs.getInt(37),//setup channel1ext2
                    rs.getInt(38),//setup channel2ext2
                    rs.getInt(39),//setup channel3ext2
                    rs.getInt(40),//setup channel4ext2
                    rs.getString(41),//setup Name
                    rs.getString(42),//setup Author
                    rs.getString(43));//setup Data
                result.add(setup);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método que atualiza as informações do sistema, baseado no equipamento. Manipulação pela equipe técnica
     * @param setup
     */
    public void update(Setup setup){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_setup SET "
                + "setupUserId = ?,"
                + "MC1M1 = ?,"
                + "MC2M1 = ?,"
                + "MC3M1 = ?,"
                + "MC4M1 = ?,"
                + "MC5M1 = ?,"
                + "MC6M1 = ?,"
                + "MC7M1 = ?,"
                + "MC8M1 = ?,"
                + "MC9M1 = ?,"
                + "MC10M1 = ?,"
                + "MC11M1 = ?,"
                + "MC12M1 = ?,"
                + "MC13M1 = ?,"
                + "MC14M1 = ?,"
                + "MC15M1 = ?,"
                + "MC16M1 = ?,"
                + "MC17M1 = ?,"
                + "MC18M1 = ?,"
                + "MC19M1 = ?,"
                + "MC20M1 = ?,"
                + "MC21M1 = ?,"
                + "MC22M1 = ?,"
                + "MC23M1 = ?,"
                + "MC24M1 = ?,"
                + "MC25M1 = ?,"
                + "MC26M1 = ?,"
                + "MC27M1 = ?,"
                + "MC28M1 = ?,"
                + "MC29M1 = ?,"
                + "MC30M1 = ?,"
                + "channel1ext1 = ?,"
                + "channel2ext1 = ?,"
                + "channel3ext1 = ?,"
                + "channel4ext1 = ?,"
                + "channel1ext2 = ?,"
                + "channel2ext2 = ?,"
                + "channel3ext2 = ?,"
                + "channel4ext2 = ?,"
                + "setupName = ?,"
                + "setupAuthor = ?,"
                + "setupData = ? "
                + "WHERE setupId = ?;");

            stm.setInt(1, setup.getUserId());
            stm.setInt(2, setup.getMC1M1());
            stm.setInt(3, setup.getMC2M1());
            stm.setInt(4, setup.getMC3M1());
            stm.setInt(5, setup.getMC4M1());
            stm.setInt(6, setup.getMC5M1());
            stm.setInt(7, setup.getMC6M1());
            stm.setInt(8, setup.getMC7M1());
            stm.setInt(9, setup.getMC8M1());
            stm.setInt(10, setup.getMC9M1());
            stm.setInt(11, setup.getMC10M1());
            stm.setInt(12, setup.getMC11M1());
            stm.setInt(13, setup.getMC12M1());
            stm.setInt(14, setup.getMC13M1());
            stm.setInt(15, setup.getMC14M1());
            stm.setInt(16, setup.getMC15M1());
            stm.setInt(17, setup.getMC16M1());
            stm.setInt(18, setup.getMC17M1());
            stm.setInt(19, setup.getMC18M1());
            stm.setInt(20, setup.getMC19M1());
            stm.setInt(21, setup.getMC20M1());
            stm.setInt(22, setup.getMC21M1());
            stm.setInt(23, setup.getMC22M1());
            stm.setInt(24, setup.getMC23M1());
            stm.setInt(25, setup.getMC24M1());
            stm.setInt(26, setup.getMC25M1());
            stm.setInt(27, setup.getMC26M1());
            stm.setInt(28, setup.getMC27M1());
            stm.setInt(29, setup.getMC28M1());
            stm.setInt(30, setup.getMC29M1());
            stm.setInt(31, setup.getMC30M1());
            stm.setInt(32, setup.getChannel1ext1());
            stm.setInt(33, setup.getChannel2ext1());
            stm.setInt(34, setup.getChannel3ext1());
            stm.setInt(35, setup.getChannel4ext1());
            stm.setInt(36, setup.getChannel1ext2());
            stm.setInt(37, setup.getChannel2ext2());
            stm.setInt(38, setup.getChannel3ext2());
            stm.setInt(39, setup.getChannel4ext2());
            stm.setString(40, setup.getSetupName());
            stm.setString(41, setup.getSetupAuthor());
            stm.setString(45, String.valueOf((setup.getSetupDate())));
            stm.setInt(46, setup.getSetupId());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que deleta o registro de um método específico
     * @param setup
     */
    public void delete(Setup setup){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("DELETE FROM tb_setup WHERE setupId = ?;");
            stm.setInt(1, setup.getSetupId());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método de busca por um método específico
     * @param pk
     * @return Cadastro de método pela primary key
     */
    public Setup findById(int pk){
        Setup result = null;
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_setup WHERE setupId = ?;");
            stm.setInt(1, pk);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                Setup setup = new Setup(
                    rs.getInt(1),//setup id
                    rs.getInt(2),//setup user id
                    rs.getInt(3),//setup MC1M1
                    rs.getInt(4),//setup MC2M1
                    rs.getInt(5),//setup MC3M1
                    rs.getInt(6),//setup MC4M1
                    rs.getInt(7),//setup MC5M1
                    rs.getInt(8),//setup MC6M1
                    rs.getInt(9),//setup MC7M1
                    rs.getInt(10),//setup MC8M1
                    rs.getInt(11),//setup MC9M1
                    rs.getInt(12),//setup MC10M1
                    rs.getInt(13),//setup MC11M1
                    rs.getInt(14),//setup MC12M1
                    rs.getInt(15),//setup MC13M1
                    rs.getInt(16),//setup MC14M1
                    rs.getInt(17),//setup MC15M1
                    rs.getInt(18),//setup MC16M1
                    rs.getInt(19),//setup MC17M1
                    rs.getInt(20),//setup MC18M1
                    rs.getInt(21),//setup MC19M1
                    rs.getInt(22),//setup MC20M1
                    rs.getInt(23),//setup MC21M1
                    rs.getInt(24),//setup MC22M1
                    rs.getInt(25),//setup MC23M1
                    rs.getInt(26),//setup MC24M1
                    rs.getInt(27),//setup MC25M1
                    rs.getInt(28),//setup MC26M1
                    rs.getInt(29),//setup MC27M1
                    rs.getInt(30),//setup MC28M1
                    rs.getInt(31),//setup MC29M1
                    rs.getInt(32),//setup MC30M1
                    rs.getInt(33),//setup channel1ext1
                    rs.getInt(34),//setup channel2ext1
                    rs.getInt(35),//setup channel3ext1
                    rs.getInt(36),//setup channel4ext1
                    rs.getInt(37),//setup channel1ext2
                    rs.getInt(38),//setup channel2ext2
                    rs.getInt(39),//setup channel3ext2
                    rs.getInt(40),//setup channel4ext2
                    rs.getString(41),//setup Name
                    rs.getString(42),//setup Author
                    rs.getString(43));//setup Data
                result = setup;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }
}
