/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unil;

import java.util.StringTokenizer;

/**
 *
 * @author DELL
 */
public class MyUntil {
    
    
    public String checkDate(String date) throws Exception{
        String[] getDate = date.split("/");
        int day = Integer.parseInt(getDate[0]);
        int month = Integer.parseInt(getDate[1]);
        int year = Integer.parseInt(getDate[2]);
        if( year%400==0 || (year%4==0 && year%100!=0)){
            
            if(month==1 || month == 3 || month==5 || month==7 || month==8 || month==10 || month==12){
                if(day>0 && day<32){
                    return null;
                }
                else{
                    throw new Exception("This day not exit!");
                }
            }
                      
            else if(month==4 || month==6 || month==9 || month==11){
                if(day>0 && day<31){
                    return null;
                }
                else{
                    throw new Exception("This day not exit!");
                }
            }
                      
            else if(month==2){
                if(day>0 && day<30){
                    return null;
                }
                else{
                    throw new Exception("This day not exit!");
                }   
            }
            
            else{
               throw new Exception("This month not exit!");
            }
        }
    
    
        else{
            if(month==1 || month == 3 || month==5 || month==7 || month==8 || month==10 || month==12){
                if(day>0 && day<32){
                    return null;
                }
                else{
                    return "This day not exit!";
                }
            }
            
            else if(month==4 || month==6 || month==9 || month==11){
                if(day>0 && day<31){
                    return null;
                }
                else{
                    throw new Exception("This day not exit!");
                }
            }
            
            else if(month==2){
                if(day>0 && day<29){
                    return null;
                }
                else{
                    throw new Exception("This day not exit!");
                }
            }
            
            else{
                throw new Exception("This month not exit!");
            }
        }
    }  
}
