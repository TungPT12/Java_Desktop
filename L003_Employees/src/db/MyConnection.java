/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author DELL
 */
public class MyConnection {
    static  String url = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static  String driver = "jdbc:sqlserver://SE140843\\SQLEXPRESS:1433; databaseName=Employees; user=sa; password=123456";
    public static Connection getMyConnection() throws Exception{
        Class.forName(url);
        Connection conn = DriverManager.getConnection(driver);
        return conn;
    }
}
