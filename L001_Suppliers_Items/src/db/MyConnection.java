/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author DELL
 */
public class MyConnection{
    //static String driver = "com.microsoft.jdbc."
    public static Connection getMyConnection() throws ClassNotFoundException, SQLException, Exception{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://TUNGPT\\SQLEXPRESS:1433; databaseName=Login;" + "user=sa; password=123456");
        return conn;
    }
}
