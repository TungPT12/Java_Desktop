/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controll;

/**
 *
 * @author DELL
 */
public class LoginDTO {
    private String userID;
    private String fullName;
    private String password;

    public LoginDTO(String userID, String fullName, String password) {
        this.userID = userID;
        this.fullName = fullName;
        this.password = password;
    }

    public LoginDTO() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
