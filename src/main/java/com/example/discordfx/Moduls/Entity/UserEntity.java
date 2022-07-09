package com.example.discordfx.Moduls.Entity;

public class UserEntity {

    private int id;
    private final String username;
    private final String password;
    private final String email;
    private final String phone;

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

}
