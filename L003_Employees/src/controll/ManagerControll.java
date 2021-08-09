
package controll;

import dao.EmployeeDAO;
import dto.EmployeeDTO;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;


public class ManagerControll {
    Vector<EmployeeDTO> listEmp = new Vector<>();
    Vector<EmployeeDTO> listEmpDelete = new Vector<>();
    
    public void getData(){
        try {
            listEmp.clear();
            listEmp = EmployeeDAO.getAllEmp();
            Collections.sort(listEmp, compareObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public Vector<EmployeeDTO> showAllEmp(){
        return listEmp;
    }
    
    public String CreateEmp(EmployeeDTO e) throws Exception{
        e = new EmployeeDTO(e.getId(),e.getFullname(),e.getPhone(),e.getEmail(),e.getAddress(),e.getDob(),e.isIsDelete());
        if(EmployeeDAO.findById(e.getId())!=null){
            return null;
        }
        else if(listEmp.add(e)){
            Collections.sort(listEmp, compareObject);
            return "Create successfully!";
        }else{
            return null;
        }
    }
    
    public int saveToDataBase(){
        try {
            for(EmployeeDTO e : listEmp){
                
                if(EmployeeDAO.findById(e.getId())!=null){
                    if(EmployeeDAO.updateEmp(e)==0){
                        return -1;
                    }
                }
                
                else if(EmployeeDAO.findById(e.getId())==null){
                    if(EmployeeDAO.insertData(e)==0){
                        return -1;
                    }
                }  
            }
            
            EmployeeDAO.deleteEmp(listEmpDelete);
            
            return 1;
        } catch (Exception ex) {
            return -1;
        }
    }
    
    public Vector<EmployeeDTO> findByName(String name){
        
        Vector<EmployeeDTO> findList = new Vector<>();
        if(name.length()==0){
            return findList;
        }
        for(EmployeeDTO e : listEmp){
            String findFullname = e.getFullname();
            if( (findFullname.toLowerCase()).contains(name.toLowerCase()) || (findFullname.toUpperCase()).contains(name.toUpperCase()) ){
                e.getId();
                e.getFullname();
                e.getPhone();
                e.getEmail();
                e.getAddress();
                e.getDob();
                e.isIsDelete();
                findList.add(e);
                
            }
        }
        return findList;
    }
    
    public Vector<EmployeeDTO> findById(String id){
        Vector<EmployeeDTO> findList = new Vector<>();
        if(id.length()==0){
            return findList;
        }
        for(EmployeeDTO e : listEmp){
            if( id.equals(e.getId()) ){
                e.getId();
                e.getFullname();
                e.getPhone();
                e.getEmail();
                e.getAddress();
                e.getDob();
                e.isIsDelete();
                findList.add(e);  
            }
        }
        return findList;
    }
    
    public String updateEmp(EmployeeDTO emp){
        for(EmployeeDTO e : listEmp){
            if(e.getId().equals(emp.getId())){
                e.setId(emp.getId());
                e.setFullname(emp.getFullname());
                e.setPhone(emp.getPhone());
                e.setEmail(emp.getEmail());
                e.setAddress(emp.getAddress());
                e.setDob(emp.getDob());
                e.setIsDelete(emp.isIsDelete());
                return "Update successfully!";
            }
        }
        return null;
    }
    
    public EmployeeDTO checkDuplicateId(String id){
        for(EmployeeDTO e :  listEmp){
            if(e.getId().equals(id)){
                e.getId();
                e.getFullname();
                e.getPhone();
                e.getEmail();
                e.getAddress();
                e.getDob();
                e.isIsDelete();
                return e;    
            }
        }
        return null;
    }
    
    public String deleteEmp(EmployeeDTO emp){
        
        for(EmployeeDTO  e : listEmp){
            if(emp.getId().equals(e.getId())){
                listEmpDelete.add(e);
                listEmp.remove(e);
                return "Delete Successfully!";
            }
        }
        return null;
    }
    
    public Comparator compareObject = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            EmployeeDTO emp1 = (EmployeeDTO) o1;
            EmployeeDTO emp2 = (EmployeeDTO) o2;
            return emp1.getId().compareTo(emp2.getId());
        }
    };
}
