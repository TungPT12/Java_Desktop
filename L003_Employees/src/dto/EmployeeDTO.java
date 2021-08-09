package dto;

import unil.MyUntil;

public class EmployeeDTO {
    String id , fullname, phone, email, address, dob;
    boolean isDelete;

    public EmployeeDTO() {
    }
    
    

    public EmployeeDTO(String id, String fullname, String phone, String email, String address, String dob, boolean isDelete) throws Exception{
        if(!id.matches("[A-Z0-9]{1,10}")){
            throw new Exception("Id must be has from 1 to 10 character, not contains special characters and can't null! ");
        }
        this.id = id;
        
        if((fullname.length()<1) || fullname.length()>30){
            throw new Exception("Fullname must be have from 1 to 30 character!");
        }
        this.fullname = fullname;
        
        if(!phone.matches("[0-9]{3,15}")){
            throw new Exception("Phone must be number and has from 3 to 15 numbers!");
        }
        this.phone = phone;
        
        if(email.length()<5 || email.length()>30){
            throw new Exception("Email has from 5 to 30 character!");
        }
        if(email.length()>=5 && email.length()<=30){
            if(!email.matches("^[A-Z0-9a-z]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]+.[\\w]+)")){
                throw new Exception("Email must be flowwing form \"abc123@gmail.com\" or \"abc123@fpt.edu.vn\" and can not have special charcater!");
            }
            //if(!email.matches("^\\w+@[gmail.com||fpt.edu.vn]$"))
        }
        this.email = email;
        
        if(address.length()<1 || address.length()>300){
            throw new Exception("Address must be have from 1 to 300 character and can not null!");
        }
        this.address = address;
        
        if(!dob.matches("^\\d{1,2}/\\d{1,2}/\\d{4}$")){
            System.out.println(dob.length());
            System.out.println(dob);
            throw new Exception("Birth day must be flow form \"dd/MM/yyyy\"!");
        }
        if(dob.matches("^\\d{1,2}/\\d{1,2}/\\d{4}")){
            MyUntil mu = new MyUntil();
            String checkDOB = mu.checkDate(dob);
            if(checkDOB!=null){
                throw new Exception(checkDOB);
            }
        }
        this.dob = dob;
        
        this.isDelete = isDelete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public boolean isIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }
    
}
