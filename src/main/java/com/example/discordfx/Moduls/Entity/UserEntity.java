/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Moduls.Entity;

import java.io.Serializable;

public class UserEntity implements Serializable {

    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;

    //Constructor :
    public UserEntity(int id,String username, String password, String email, String phone){
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id =id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

}
