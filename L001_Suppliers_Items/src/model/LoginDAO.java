/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author DELL
 */
public class LoginDAO {
    
    private static Connection conn;
    private static PreparedStatement preStm;
    private static ResultSet rs;
    
    public static void closeConnection() throws Exception{
        if(rs!=null) rs.close();
        if(preStm!=null) preStm.close();
        if(conn!=null) conn.close();
    }
    
    public static String getLogin(String userID, String password) throws Exception{
        String sql = "Select fullname From login Where username = ? and password = ?";
        String fullname = null;
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, userID);
            preStm.setString(2, password);
            rs = preStm.executeQuery();
            while(rs.next()){
                fullname = rs.getString("fullname");
            }
        }finally{
            closeConnection();
        }
        return fullname;
    }
}
