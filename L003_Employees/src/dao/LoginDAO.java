/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

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
    
    public static  String checkLogin(String username, String password) throws Exception{
        String fullname =null;
       try{
            String sql = "Select Fullname From Login Where Username = ? and Password = ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, username);
            preStm.setString(2, password);
            rs = preStm.executeQuery();
            while(rs.next()){
                fullname = rs.getString("Fullname");
            }
        }finally{
            closeConnection();
        }
       return fullname;
    }
}
