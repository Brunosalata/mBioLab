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
import br.com.biopdi.mbiolabv2.model.bean.Method;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
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
                    + "normIndex INTEGER,"
                    + "essayTypeIndex INTEGER,"
                    + "extensometer1Index INTEGER,"
                    + "extensometer2Index INTEGER,"
                    + "chartViewIndex INTEGER,"
                    + "methodName TEXT,"
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
     * Método de inclusão de metodo na tabela tb_method
     *
     * @param method
     * @Description chamar a função, instanciando MethodDAO methodDAO e Method method. Então, chama db.create()
     */
    public void create(Method method) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO tb_method(id, autoBreakIndex, " +
                    "specimenTypeIndex, normIndex, essayTypeIndex, extensometer1Index, extensometer2Index, chartViewIndex, " +
                    "methodName, methodDate, essayVelocity, specimenAValue, specimenBValue, specimenCrossSectionArea, " +
                    "specimenCrossSectionLength, offsetIntersectionLine, gainIntersectionLine) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            stm.setInt(2, method.getAutoBreakIndex());
            stm.setInt(3, method.getSpecimenTypeIndex());
            stm.setInt(4, method.getNormIndex());
            stm.setInt(5, method.getEssayTypeIndex());
            stm.setInt(6, method.getExtensometer1Index());
            stm.setInt(7, method.getExtensometer2Index());
            stm.setInt(8, method.getChartViewIndex());
            stm.setString(9, method.getMethodName());
            stm.setString(10, String.valueOf(method.getMethodDate()));
            stm.setDouble(11, method.getEssayVelocity());
            stm.setDouble(12, method.getSpecimenAValue());
            stm.setDouble(13, method.getSpecimenBValue());
            stm.setDouble(14, method.getSpecimenCrossSectionArea());
            stm.setDouble(15, method.getSpecimenCrossSectionLength());
            stm.setDouble(16, method.getOffsetIntersectionLine());
            stm.setDouble(17, method.getGainIntersectionLine());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Método que lista todos os elementos da tabela tb_method
     *
     * @return lista de objetos em tb_method
     * @Description chamada da função: db.findAll()
     */
    public List<Method> findAll() {
        ArrayList<Method> result = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT id, autoBreakIndex, specimenTypeIndex, " +
                    "normIndex, essayTypeIndex, extensometer1Index, extensometer2Index, chartViewIndex, " +
                    "methodName, methodDate, essayVelocity, specimenAValue, specimenBValue, specimenCrossSectionArea, " +
                    "specimenCrossSectionLength, offsetIntersectionLine, gainIntersectionLine FROM tb_method ORDER BY id ASC;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Method method = new Method(
                    rs.getInt("id"),//method id
                    rs.getInt("autoBreakIndex"),//method autoBreak index
                    rs.getInt("specimenTypeIndex"),//method specimenType index
                    rs.getInt("normIndex"),//norm index
                    rs.getInt("essayTypeIndex"),//essayType index
                    rs.getInt("extensometer1Index"),//extensometer1 index
                    rs.getInt("extensometer2Index"),//extensometer2 index
                    rs.getInt("chartViewIndex"),//chartView index
                    rs.getString("methodName"),//methodName
                    rs.getString("methodDate"),//method date
                    rs.getDouble("essayVelocity"),//essayVelocity
                    rs.getDouble("specimenAValue"),//specimenAValue
                    rs.getDouble("specimenBValue"),//specimenBValue
                    rs.getDouble("specimenCrossSectionArea"),//specimenCrossSectionArea
                    rs.getDouble("specimenCrossSectionLength"),//specimenCrossSectionLength
                    rs.getDouble("offsetIntersectionLine"),//offsetIntersectionLine
                    rs.getDouble("gainIntersectionLine"));//gain Intersection line
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
     *
     * @param method
     */
    public void update(Method method) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("UPDATE tb_method SET "
                    + "autoBreakIndex INTEGER,"
                    + "specimenTypeIndex INTEGER,"
                    + "normIndex INTEGER,"
                    + "essayTypeIndex INTEGER,"
                    + "extensometer1Index INTEGER,"
                    + "extensometer2Index INTEGER,"
                    + "chartViewIndex INTEGER,"
                    + "methodName TEXT,"
                    + "methodDate DATE,"
                    + "essayVelocity REAL,"
                    + "specimenAValue REAL,"
                    + "specimenBValue REAL,"
                    + "specimenCrossSectionArea REAL,"
                    + "specimenCrossSectionLength REAL,"
                    + "offsetIntersectionLine REAL,"
                    + "gainIntersectionLine REAL) "
                    + "WHERE id = ?;");
            stm.setInt(2, method.getAutoBreakIndex());
            stm.setInt(3, method.getSpecimenTypeIndex());
            stm.setInt(4, method.getNormIndex());
            stm.setInt(5, method.getEssayTypeIndex());
            stm.setInt(6, method.getExtensometer1Index());
            stm.setInt(7, method.getExtensometer2Index());
            stm.setInt(8, method.getChartViewIndex());
            stm.setString(9, method.getMethodName());
            stm.setString(10, String.valueOf(method.getMethodDate()));
            stm.setDouble(11, method.getEssayVelocity());
            stm.setDouble(12, method.getSpecimenAValue());
            stm.setDouble(13, method.getSpecimenBValue());
            stm.setDouble(14, method.getSpecimenCrossSectionArea());
            stm.setDouble(15, method.getSpecimenCrossSectionLength());
            stm.setDouble(16, method.getOffsetIntersectionLine());
            stm.setDouble(17, method.getGainIntersectionLine());
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
     * @param method
     */
    public void delete(Method method) {
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("DELETE FROM tb_method WHERE id = ?;");
            stm.setInt(1, method.getId());
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
    public Method findById(int pk) {
        Method result = null;
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT id, autoBreakIndex, specimenTypeIndex, " +
                    "normIndex, essayTypeIndex, extensometer1Index, extensometer2Index, chartViewIndex, " +
                    "methodName, methodDate, essayVelocity, specimenAValue, specimenBValue, specimenCrossSectionArea, " +
                    "specimenCrossSectionLength, offsetIntersectionLine, gainIntersectionLine FROM tb_method WHERE id = ?;");
            stm.setInt(1, pk);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Method method = new Method(
                    rs.getInt("id"),//method id
                    rs.getInt("autoBreakIndex"),//method autoBreak index
                    rs.getInt("specimenTypeIndex"),//method specimenType index
                    rs.getInt("normIndex"),//norm index
                    rs.getInt("essayTypeIndex"),//essayType index
                    rs.getInt("extensometer1Index"),//extensometer1 index
                    rs.getInt("extensometer2Index"),//extensometer2 index
                    rs.getInt("chartViewIndex"),//chartView index
                    rs.getString("methodName"),//methodName
                    rs.getString("methodDate"),//method date
                    rs.getDouble("essayVelocity"),//essayVelocity
                    rs.getDouble("specimenAValue"),//specimenAValue
                    rs.getDouble("specimenBValue"),//specimenBValue
                    rs.getDouble("specimenCrossSectionArea"),//specimenCrossSectionArea
                    rs.getDouble("specimenCrossSectionLength"),//specimenCrossSectionLength
                    rs.getDouble("offsetIntersectionLine"),//offsetIntersectionLine
                    rs.getDouble("gainIntersectionLine"));//gain Intersection line
                result = method;
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
     * @param methodName
     * @return Cadastro do usuario pela primary key
     */
    public Method findByMethod(String methodName) {
        Method result = null;
        openConnection();
        try {
            PreparedStatement stm = conn.prepareStatement("SELECT id, autoBreakIndex, specimenTypeIndex, " +
                    "normIndex, essayTypeIndex, extensometer1Index, extensometer2Index, chartViewIndex, " +
                    "methodName, methodDate, essayVelocity, specimenAValue, specimenBValue, specimenCrossSectionArea, " +
                    "specimenCrossSectionLength, offsetIntersectionLine, gainIntersectionLine FROM tb_method WHERE methodName = ?;");
            stm.setString(1, methodName);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Method method = new Method(
                        rs.getInt("id"),//method id
                        rs.getInt("autoBreakIndex"),//method autoBreak index
                        rs.getInt("specimenTypeIndex"),//method specimenType index
                        rs.getInt("normIndex"),//norm index
                        rs.getInt("essayTypeIndex"),//essayType index
                        rs.getInt("extensometer1Index"),//extensometer1 index
                        rs.getInt("extensometer2Index"),//extensometer2 index
                        rs.getInt("chartViewIndex"),//chartView index
                        rs.getString("methodName"),//methodName
                        rs.getString("methodDate"),//method date
                        rs.getDouble("essayVelocity"),//essayVelocity
                        rs.getDouble("specimenAValue"),//specimenAValue
                        rs.getDouble("specimenBValue"),//specimenBValue
                        rs.getDouble("specimenCrossSectionArea"),//specimenCrossSectionArea
                        rs.getDouble("specimenCrossSectionLength"),//specimenCrossSectionLength
                        rs.getDouble("offsetIntersectionLine"),//offsetIntersectionLine
                        rs.getDouble("gainIntersectionLine"));//gain Intersection line
                result = method;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }
}

