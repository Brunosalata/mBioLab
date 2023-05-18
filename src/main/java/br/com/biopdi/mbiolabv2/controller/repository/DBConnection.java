package br.com.biopdi.mbiolabv2.controller.repository;

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

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
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