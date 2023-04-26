package br.com.biopdi.mbiolabv2.controller.repository;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static final String URL = "jdbc:sqlite:database/mBioLabDB.db";
    public static final String DRIVER = "org.sqlite.JDBC";
    protected Connection conn;

    /**
     * Método de abertura de conexão com o DB mBioLabDB
     *
     * @return
     */
    public Connection openConnection() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error: FATAL ERROR, cannot instantiate the driver " + DRIVER);
        }
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(URL, config.toProperties());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método de fechamento de conexão com o DB mBioLabDB
     */
    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}