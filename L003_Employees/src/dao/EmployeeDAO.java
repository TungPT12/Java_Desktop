package dao;

import db.MyConnection;
import dto.EmployeeDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class EmployeeDAO {
    static Connection conn;
    static PreparedStatement preStm;
    static ResultSet rs;
    
    public static void closeConnection() throws Exception{
        if(rs!=null) rs.close();
        if(preStm!=null);
        if(conn!=null);
    }
    
    public static Vector<EmployeeDTO> getAllEmp() throws Exception{
        Vector<EmployeeDTO> listEmp = new Vector<>();
        String sql = "Select * From EmpTable Where isDelete = ? ";
        try{
           conn = MyConnection.getMyConnection();
           preStm = conn.prepareStatement(sql);
           preStm.setBoolean(1, false);
           rs = preStm.executeQuery();
            while(rs.next()){
                EmployeeDTO emp = new EmployeeDTO(rs.getString("ID").trim(),rs.getString("Fullname").trim(),rs.getString("Phone").trim(),rs.getString("Email").trim(),rs.getString("Address").trim(),
                                                    rs.getString("DayofBirth").trim(),rs.getBoolean("isDelete"));
                listEmp.add(emp);
            }
        }finally{
            closeConnection();
        }
        return listEmp;
    }
    
    public static EmployeeDTO findById(String id) throws Exception{
        String sql = "Select * From EmpTable Where ID = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, id);;
            rs  = preStm.executeQuery();
            if(rs.next()){
                return new EmployeeDTO(rs.getString("ID").trim(), rs.getString("Fullname").trim(), rs.getString("Phone").trim()
                        , rs.getString("Email").trim(), rs.getString("Address").trim(), rs.getString("DayofBirth").trim()
                        , rs.getBoolean("isDelete"));
            }
        }finally{
            closeConnection();
        }
        return null;
    }
    
    public static int insertData(EmployeeDTO emp) throws Exception{
        String sql = "Insert EmpTable Values(?,?,?,?,?,?,?)";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, emp.getId());
            preStm.setString(2, emp.getFullname());
            preStm.setString(3, emp.getPhone());
            preStm.setString(4, emp.getEmail());
            preStm.setString(5, emp.getAddress());
            preStm.setString(6, emp.getDob());
            preStm.setBoolean(7, emp.isIsDelete());
            return preStm.executeUpdate();
            
        }finally{
            closeConnection();
        }
    }
    
    public static int updateEmp(EmployeeDTO e) throws Exception{
        String sql = "Update EmpTable Set Fullname = ?, Phone = ?, Email = ?, Address = ?, DayofBirth = ? Where ID = ? and isDelete = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, e.getFullname());
            preStm.setString(2, e.getPhone());
            preStm.setString(3, e.getEmail());
            preStm.setString(4, e.getAddress());
            preStm.setString(5, e.getDob());
            preStm.setString(6, e.getId());
            preStm.setBoolean(7, e.isIsDelete());
            return preStm.executeUpdate();
        }finally{
            closeConnection();
        }
    }
    
    public static int deleteEmp(Vector<EmployeeDTO> listEmp) throws Exception{
        int count=0;
        String sql = "Update EmpTable Set isDelete = ? Where ID = ?";
        try{
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            for(EmployeeDTO e : listEmp){
                preStm.setBoolean(1, true);
                preStm.setString(2, e.getId());
                preStm.executeUpdate();
                count++;
            }
        }finally{
            closeConnection();
        }
        return count;
    }
}
