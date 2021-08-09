/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controll.ItemDTO;
import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class ItemsDAO {
    static Connection conn;
    static PreparedStatement preStm;
    static ResultSet rs;
    
    public static void closeConnection() throws Exception{
        if(rs!=null) rs.close();
        if(preStm!=null) preStm.close();
        if(conn!=null) conn.close();
        
    }
    
    public static ArrayList<ItemDTO> getAllItems() throws Exception{
        ArrayList<ItemDTO> itemList = new ArrayList<>();
        String sql = "Select * From Items";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            rs = preStm.executeQuery();
            while(rs.next()){
                ItemDTO i = new ItemDTO();
                i.setItemCode(rs.getString("ItemCode"));
                i.setItemName(rs.getString("ItemName"));
                i.setSupCode(rs.getString("SupCode"));
                i.setUnit(rs.getString("Unit"));
                i.setPrice(rs.getFloat("Price"));
                itemList.add(i);
            }
        }finally{
            closeConnection();
        }
        return itemList;
    }
    
    public static boolean getSupplying(String itemCode) throws Exception{
        String sql = "Select Supplying From Items Where ItemCode = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, itemCode);
            rs = preStm.executeQuery();
            if(rs.next()){
                return rs.getBoolean("Supplying");
            }
        }finally{
            closeConnection();
        }
        return false;
    }
    
    public static ItemDTO getItemByCode(String itemCode) throws Exception{
        String sql = "Select * From Items Where ItemCode = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, itemCode);
            rs = preStm.executeQuery();
            if(rs.next()){
                return new ItemDTO(rs.getString("ItemCode"),rs.getString("ItemName"), rs.getString("SupCode"), rs.getString("Unit"), rs.getFloat("Price"),rs.getBoolean("Supplying"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
       return null; 
    }
    
    public static int insertItem(ItemDTO i) throws Exception{
        String sql = "Insert Items Values(?,?,?,?,?,?)";
        try{
            conn= MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, i.getItemCode());
            preStm.setString(2, i.getItemName());
            preStm.setString(3, i.getSupCode());
            preStm.setString(4, i.getUnit());
            preStm.setFloat(5, i.getPrice());
            preStm.setBoolean(6, i.isSupplying());
            return preStm.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        } 
       return 0;
    }
    
    public static int updateIteṃ(ItemDTO i) throws Exception{
        String sql = "Update Items Set ItemName = ?, Unit = ?, Price = ?, Supplying = ? Where ItemCode = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, i.getItemName());
            preStm.setString(2, i.getUnit());
            preStm.setFloat(3, i.getPrice());
            preStm.setBoolean(4, i.isSupplying());
            preStm.setString(5, i.getItemCode());
            return preStm.executeUpdate();
        }finally{
            closeConnection();
        }
    }
    public static int deleteItem(String code)throws Exception{
        String sql = "Delete From Items Where ItemCode = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm  = conn.prepareStatement(sql);
            preStm.setString(1, code);
            return preStm.executeUpdate();
        }finally{
            closeConnection();
        }
    }
}
