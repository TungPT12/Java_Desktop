/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controll.SupplierDTO;
import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class SuppliersDAO {
    static Connection conn;
    static PreparedStatement preStm;
    static ResultSet rs;
    
    public static void closeConnection() throws Exception{
        if(rs!=null) rs.close();
        if(preStm!=null) preStm.close();
        if(conn!=null) conn.close();
        
    }
    
    public static ArrayList<SupplierDTO> getAllSupplier() throws Exception{
        ArrayList<SupplierDTO> supplierList = new ArrayList<>();
        String sql = "Select * From Suppliers";
        try{
            conn = MyConnection.getMyConnection();
            preStm  = conn.prepareStatement(sql);
            rs = preStm.executeQuery();
            while(rs.next()){
                SupplierDTO s = new SupplierDTO();
                s.setSupCode(rs.getString("SupCode"));
                s.setSupName(rs.getString("SupName"));
                s.setAddress(rs.getString("Address"));
                s.setCollaborating(rs.getBoolean("Colloborating"));
                supplierList.add(s);
            }
        }finally{
            closeConnection();
        }
        return supplierList;
    }
    
    public static boolean getCollaborating(String supCode) throws Exception{
        String sql = "Select Colloborating From Suppliers Where SupCode = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, supCode);
            rs = preStm.executeQuery();
            if(rs.next()){
                return rs.getBoolean("Colloborating");
            }
        }finally{
            closeConnection();
        }
        return false;
    }
    
    public static SupplierDTO getSupplierByCode(String supCode) throws Exception{
        String sql = "Select * From Suppliers Where Supcode = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, supCode);
            rs = preStm.executeQuery();
            if(rs.next()){
                return new SupplierDTO(rs.getString("SupCode"), rs.getString("SupName"), rs.getString("Address"), rs.getBoolean("Colloborating"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
        return null;
    }
    
     public static int insertSupplier(SupplierDTO s) throws Exception{
        String sql = "Insert Suppliers Values(?,?,?,?)";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, s.getSupCode());
            preStm.setString(2, s.getSupName());
            preStm.setString(3, s.getAddress());
            preStm.setBoolean(4, s.isCollaborating());
            return preStm.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
     
    public static int updateSupplier(SupplierDTO s) throws Exception{
        String sql = "Update Suppliers Set SupName = ?, Address = ?, Colloborating = ? Where SupCode = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, s.getSupName());
            preStm.setString(2, s.getAddress());
            preStm.setBoolean(3, s.isCollaborating());
            preStm.setString(4, s.getSupCode());
            return preStm.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    
    
    
    public static int deleteSupplier(String supcode)throws Exception{
        String sql = "Delete From Suppliers Where SupCode = ?";
        deleteItemBySupCode(supcode);
        try{
            conn = MyConnection.getMyConnection();
            preStm  = conn.prepareStatement(sql);
            preStm.setString(1, supcode);
            return preStm.executeUpdate();
        }finally{
            closeConnection();
        }
    }
    
    public static int deleteItemBySupCode(String supCode) throws Exception{
        String sql = "Delete From Items Where SupCode = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm  = conn.prepareStatement(sql);
            preStm.setString(1, supCode);
            return preStm.executeUpdate();
        }finally{
            closeConnection();
        }
    }
    
}
