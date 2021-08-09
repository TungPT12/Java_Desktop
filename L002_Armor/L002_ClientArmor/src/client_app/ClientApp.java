package client_app;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sword Lake
 */
import java.rmi.Naming;
import remote_interface.ServerRemote;
 
public class ClientApp {
    public static void main(String[] args) {
        new ClientApp().go();
    }
 
    public void go() {
        String msg;
        try {            
//            ServerRemote serverRemote = (ServerRemote) Naming.lookup("rmi://127.0.0.1:1099/RemoteUsername");
            ServerRemote serverRemote = (ServerRemote) Naming.lookup("rmi://127.0.0.2:1204/ArmorRemote");
            //msg = "Hello "+serverRemote.getUsername();
           // System.out.println(msg);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
 
    }
    
}