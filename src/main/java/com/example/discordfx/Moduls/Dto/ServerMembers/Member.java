package com.example.discordfx.Moduls.Dto.ServerMembers;

import com.example.discordfx.Moduls.Dto.User.User;
import java.io.Serializable;

public class Member implements Serializable {

    private final User user;
    private Role role;

    public Member(User user, Role role){
        this.user = user;
        this.role = role;
    }

    public User getUser(){
        return user;
    }

    public Role getRole(){
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }

}
