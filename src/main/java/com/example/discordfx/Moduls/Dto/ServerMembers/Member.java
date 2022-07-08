package com.example.discordfx.Moduls.Dto.ServerMembers;

import com.example.discordfx.Moduls.Dto.User.User;
import java.io.Serializable;
import java.util.ArrayList;

public class Member implements Serializable {

    private final User user;
    private final ArrayList<Role> roles = new ArrayList<>();

    public Member(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public ArrayList<Role> getRoles(){
        return roles;
    }

    public void addRole(Role newRole){
        roles.add(newRole);
    }

    public boolean canChangeServerName(){
        for(Role x : roles){
            if(x.isChangeServerName())
                return true;
        }
        return false;
    }

}
