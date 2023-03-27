package br.com.biopdi.mbiolabv2.controller.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author Bruno Salata Lima
 */
import java.sql.*;

public class DBConnection {

    protected Connection conn;
    public Connection openConnection(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:mBioLabDB.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection(){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}