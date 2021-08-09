/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ArmorDAO;
import db.MyConnection;
import dto.ArmorDTO;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import remote_interface.ServerRemote;


public class ServerController extends UnicastRemoteObject implements ServerRemote {
    protected ServerController() throws RemoteException {}
    Connection conn;
    PreparedStatement preStm;
    ResultSet rs;
    Vector<ArmorDTO> listArmor = new Vector<>();
    ArmorDAO dao = new ArmorDAO();
    String fileName = "../L002/Armor.txt";
            
    @Override
    public String creatArmor(ArmorDTO dto) throws RemoteException{
        if(!dto.getId().matches("[A-Z0-9]{1,10}")){
            return "ID has lenght is 10 and can not have special character!";
        }
        if(dto.getClassification().length()>30 || dto.getClassification().length()<1){
            return "Classifiaction has from 1 to 30 characters!";
        }
        if(!dto.getDefense().matches("\\d+")){
            return "Denfense must be integer number";
        }
        if(dto.getDescription().length()>300 || dto.getDescription().length()<1){
            return "Description has from 1 to 300 characters!";
        }
        if(dto.getStatus().length()<1){
            return "Status can not null";
        }
        
        else{
            listArmor.add(dto);
            if(dao.writeDatatoFile(fileName, listArmor)==false){
               if(findPos(dto.getId())>=0){
                   listArmor.remove(findPos(dto.getId()));
               }
                return "Write to file error. Please try again.";
            }
        }
        Collections.sort(listArmor, dao.comparatorObj);
        return null;
    }
    
    @Override
    public ArmorDTO findByArmorID(String id) throws RemoteException{
        for(int i=0;i<listArmor.size();i++){
            if(id.equals(listArmor.get(i).getId())){
                return new ArmorDTO(listArmor.get(i).getId(), listArmor.get(i).getClassification(), listArmor.get(i).getTime(), 
                        listArmor.get(i).getDefense(), listArmor.get(i).getDescription(), listArmor.get(i).getStatus());
            }
        }
        return null;
    }
    
    @Override
    public Vector<ArmorDTO> getAllArmor() throws RemoteException{
        if(listArmor.size()>0){
            listArmor.clear();
        }
        listArmor =  (Vector<ArmorDTO>) dao.loadDatafromFile(fileName);
        Collections.sort(listArmor, dao.comparatorObj);
        return listArmor;
    }
    
    @Override
    public boolean removeArmor(String id) throws RemoteException{
        int pos = findPos(id);
        if(pos>=0){
            boolean checkWrite = dao.writeDatatoFile(fileName, listArmor);
            if(checkWrite==true){
                listArmor.remove(pos);
                dao.writeDatatoFile(fileName, listArmor);
                dao.loadDatafromFile(fileName);
            }
            else{
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String updateArmor(ArmorDTO dto) throws RemoteException{
        if(dto.getClassification().length()>30 || dto.getClassification().length()<1){
            return "Classifiaction has from 1 to 30 characters!";
        }
        if(!dto.getDefense().matches("\\d+")){
            return "Denfense must be integer number";
        }
        if(dto.getDescription().length()>300 || dto.getDescription().length()<1){
            return "Description has from 1 to 300 characters!";
        }
        if(dto.getStatus().length()<1){
            return "Status can not null";
        }
        
        else{
            int pos = findPos(dto.getId());
            listArmor.get(pos).setId(dto.getId());
            listArmor.get(pos).setClassification(dto.getClassification());
            listArmor.get(pos).setTime(getTime());
            listArmor.get(pos).setDefense(dto.getDefense());
            listArmor.get(pos).setDescription(dto.getDescription());
            listArmor.get(pos).setStatus(dto.getStatus());
            dao.writeDatatoFile(fileName, listArmor);
        }

        return null;
    }
    
    @Override
    public String tung(String id) throws RemoteException{
        return "tung";
    }
    
    @Override
    public String getTime() throws RemoteException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy (HH:mm:ss)");
        Date nowTime = new Date();
        String nowDate = simpleDateFormat.format(nowTime);
        return nowDate;
    }
    
    public void closeConnection(){
        try{
            if(rs!=null) rs.close();
            if(preStm!=null) preStm.close();
            if(conn!=null) conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    @Override
    public String checkLogin(String username, String password) throws RemoteException{
        String fullname =null;
       try{
            String sql = "Select Fullname From UserManager Where Username = ? and Password = ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, username);
            preStm.setString(2, password);
            rs = preStm.executeQuery();
            while(rs.next()){
                fullname = rs.getString("Fullname");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
       finally{
            closeConnection();
        }
       return fullname;
    }
    
    public int findPos(String id){
        for(int i=0;i<listArmor.size();i++){
            if(id.equals(listArmor.get(i).getId())) return i;
        }
        return -1;
    }
    
    public void openRemote() throws Exception{
        try{
            ServerRemote serverRemote = new ServerController();
            LocateRegistry.createRegistry(1204);
            Naming.bind("rmi://127.0.0.2:1204/ArmorRemote", serverRemote);
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }
    
    public static void main (String[] args) {
        try {
            ServerController serverController = new ServerController();
            serverController.openRemote();
            System.out.println("Server is running....");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}      
