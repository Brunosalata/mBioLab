package br.com.biopdi.mbiolabv2.controller.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import org.sqlite.SQLiteConfig;

/**
 *
 * @author Bruno Salata Lima
 */
import java.sql.*;

public class DBConnection {
    public static final String URL = "jdbc:sqlite:mBioLabDB.db";
    public static final String DRIVER = "org.sqlite.JDBC";
    protected Connection conn;
    public Connection openConnection(){
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(URL,  config.toProperties());
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