package br.com.biopdi.mbiolabv2.controller.repository.dao;

import br.com.biopdi.mbiolabv2.controller.repository.DBConnection;
import br.com.biopdi.mbiolabv2.model.bean.Method;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MethodDAO extends DBConnection {

    /**
     * Construtor da classe, que cria a tabela tb_method, caso ela não exista
     */
    public MethodDAO() {
        try {
            openConnection();
            PreparedStatement stm = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tb_method ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "autoBreakIndex INTEGER,"
                    + "specimenTypeIndex INTEGER,"
                    + "methodName TEXT,"
                    + "norm TEXT,"
                    + "essayType TEXT,"
                    + "normDescription TEXT,"
                    + "extensometer1 TEXT,"
                    + "extensometer2 TEXT,"
                    + "methodDate DATE,"
                    + "essayVelocity REAL,"
                    + "specimenAValue REAL,"
                    + "specimenBValue REAL,"
                    + "specimenCrossSectionArea REAL,"
                    + "specimenCrossSectionLength REAL,"
                    + "offsetIntersectionLine REAL,"
                    + "gainIntersectionLine REAL);");
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     //     * Método de inclusão de metodo na tabela tb_method
     //     * @param method
     //     *
     //     * @Description chamar a função, instanciando MethodDAO methodDAO e Method method. Então, chama db.create()
     //     */
    public void create(Method method){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_method VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            stm.setInt(2, method.getAutoBreakIndex());
            stm.setInt(3, method.getSpecimenTypeIndex());
            stm.setString(4, method.getMethodName());
            stm.setString(5, method.getNorm());
            stm.setString(6, method.getEssayType());
            stm.setString(7, method.getNormDescription());
            stm.setString(8, method.getExtensometer1());
            stm.setString(9, method.getExtensometer2());
            stm.setString(10, String.valueOf(method.getMethodDate()));
            stm.setDouble(11, method.getEssayVelocity());
            stm.setDouble(12, method.getSpecimenAValue());
            stm.setDouble(13, method.getSpecimenBValue());
            stm.setDouble(14, method.getSpecimenCrossSectionArea());
            stm.setDouble(15, method.getSpecimenCrossSectionLength());
            stm.setDouble(16, method.getOffsetIntersectionLine());
            stm.setDouble(17, method.getGainIntersectionLine());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que lista todos os elementos da tabela tb_method
     * @return lista de objetos em tb_method
     *
     * @Description chamada da função: db.findAll()
     */
    public List<Method> findAll() {
        ArrayList<Method> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_method ORDER BY id ASC;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Method method = new Method(
                        rs.getInt(1),//method id
                        rs.getInt(2),//method autoBreakIndex
                        rs.getInt(3),//method specimenTypeIndex
                        rs.getString(4),//methodName
                        rs.getString(5),//norm
                        rs.getString(6),//essayType
                        rs.getString(7),//normDescription
                        rs.getString(8),//extensometer1
                        rs.getString(9),//extensometer2
                        rs.getString(10),//method date
                        rs.getDouble(11),//essayVelocity
                        rs.getDouble(12),//specimenAValue
                        rs.getDouble(13),//specimenBValue
                        rs.getDouble(14),//specimenCrossSectionArea
                        rs.getDouble(15),//specimenCrossSectionLength
                        rs.getDouble(16),//offsetIntersectionLine
                        rs.getDouble(17));//gain Intersection line
                result.add(method);
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
     * @param method
     */
    public void update(Method method){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_method SET "
                    + "autoBreakIndex INTEGER,"
                    + "specimenTypeIndex INTEGER,"
                    + "methodName TEXT,"
                    + "norm TEXT,"
                    + "essayType TEXT,"
                    + "normDescription TEXT,"
                    + "extensometer1 TEXT,"
                    + "extensometer2 TEXT,"
                    + "methodDate DATE,"
                    + "essayVelocity REAL,"
                    + "specimenAValue REAL,"
                    + "specimenBValue REAL,"
                    + "specimenCrossSectionArea REAL,"
                    + "specimenCrossSectionLength REAL,"
                    + "offsetIntersectionLine REAL,"
                    + "gainIntersectionLine REAL) "
                    + "WHERE id = ?;");
            stm.setInt(1, method.getId());
            stm.setInt(2, method.getAutoBreakIndex());
            stm.setInt(3, method.getSpecimenTypeIndex());
            stm.setString(4, method.getMethodName());
            stm.setString(5, method.getNorm());
            stm.setString(6, method.getEssayType());
            stm.setString(7, method.getNormDescription());
            stm.setString(8, method.getExtensometer1());
            stm.setString(9, method.getExtensometer2());
            stm.setString(9, String.valueOf(method.getMethodDate()));
            stm.setDouble(10, method.getEssayVelocity());
            stm.setDouble(11, method.getSpecimenAValue());
            stm.setDouble(12, method.getSpecimenBValue());
            stm.setDouble(13, method.getSpecimenCrossSectionArea());
            stm.setDouble(14, method.getSpecimenCrossSectionLength());
            stm.setDouble(15, method.getOffsetIntersectionLine());
            stm.setDouble(16, method.getGainIntersectionLine());
            stm.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que deleta o cadastro de um usuario específico
     * @param method
     */
    public void delete(Method method){
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("DELETE FROM tb_method WHERE id = ?;");
            stm.setInt(1, method.getId());
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
    public Method findById(int pk){
        Method result = null;
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_method WHERE id = ?;");
            stm.setInt(1, pk);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                Method method = new Method(
                    rs.getInt(1),//method id
                    rs.getInt(2),//method autoBreakIndex
                    rs.getInt(3),//method specimenTypeIndex
                    rs.getString(4),//methodName
                    rs.getString(5),//norm
                    rs.getString(6),//essayType
                    rs.getString(7),//normDescription
                    rs.getString(8),//extensometer1
                    rs.getString(9),//extensometer2
                    rs.getString(10),//method Date
                    rs.getDouble(11),//essayVelocity
                    rs.getDouble(12),//specimenAValue
                    rs.getDouble(13),//specimenBValue
                    rs.getDouble(14),//specimenCrossSectionArea
                    rs.getDouble(15),//specimenCrossSectionLength
                    rs.getDouble(16),//offsetIntersectionLine
                    rs.getDouble(17));//gain Intersection line
                result = method;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * Método de busca por um cadastro específico
     * @param methodName
     * @return Cadastro do usuario pela primary key
     */
    public Method findByMethod(String methodName){
        Method result = null;
        openConnection();
        try{
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM tb_method WHERE methodName = ?;");
            stm.setString(1, methodName);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                Method method = new Method(
                        rs.getInt(1),//method id
                        rs.getInt(2),//method autoBreakIndex
                        rs.getInt(3),//method specimenTypeIndex
                        rs.getString(4),//methodName
                        rs.getString(5),//norm
                        rs.getString(6),//essayType
                        rs.getString(7),//normDescription
                        rs.getString(8),//extensometer1
                        rs.getString(9),//extensometer2
                        rs.getString(10),//method Date
                        rs.getDouble(11),//essayVelocity
                        rs.getDouble(12),//specimenAValue
                        rs.getDouble(13),//specimenBValue
                        rs.getDouble(14),//specimenCrossSectionArea
                        rs.getDouble(15),//specimenCrossSectionLength
                        rs.getDouble(16),//offsetIntersectionLine
                        rs.getDouble(17));//gain Intersection line
                result = method;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }
}
