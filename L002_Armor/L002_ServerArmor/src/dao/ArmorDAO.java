/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ArmorDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author DELL
 */
public class ArmorDAO implements Serializable{
    public List<ArmorDTO> loadDatafromFile(String filename){
        Vector<ArmorDTO> dto = new Vector<>();
        if(dto.size()>0) dto.clear();
        try{
            File f = new File(filename);
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream object = new ObjectInputStream(fi);
            ArmorDTO a;
            dto = (Vector<ArmorDTO>) object.readObject();
            while((a = (ArmorDTO) object.readObject())!=null){
                dto.add(a);
            }
            object.close();
            fi.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return dto;
           
    }
    
    public boolean writeDatatoFile(String filename, Vector<ArmorDTO> dto){
        try{
            FileOutputStream fo = new FileOutputStream(filename);
            ObjectOutputStream object = new ObjectOutputStream(fo);
            for(ArmorDTO a : dto){
                object.writeObject(a);
            }
            object.close();
            fo.close();
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    public  Comparator comparatorObj = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            ArmorDTO dto1 = (ArmorDTO) o1;
            ArmorDTO dto2 = (ArmorDTO) o2;
            return dto1.getId().compareTo(dto2.getId());
        }
    };
    
    
    
}