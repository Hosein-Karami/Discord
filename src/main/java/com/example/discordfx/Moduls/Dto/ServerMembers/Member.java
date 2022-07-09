package com.example.discordfx.Moduls.Dto.ServerMembers;

import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Service.AccountsService;
import java.io.Serializable;
import java.util.ArrayList;

public class Member implements Serializable {

    private final int userId;
    private final ArrayList<Role> roles = new ArrayList<>();

    public Member(int userId){
        this.userId = userId;
    }

    public User getUser(){
        AccountsService service = new AccountsService();
        return service.getParticularUser(userId);
    }

    public ArrayList<Role> getRoles(){
        return roles;
    }

    public void addRole(Role newRole){
        roles.add(newRole);
    }

    public boolean isOwner(){
        for(Role x : roles){
            if(x.getName().equals("Owner"))
                return true;
        }
        return false;
    }

    public boolean canKickMembers(){
        for(Role x : roles){
            if(x.isRemoveMemberFromServer())
                return true;
        }
        return false;
    }

    public boolean canChangeServerName(){
        for(Role x : roles){
            if(x.isChangeServerName())
                return true;
        }
        return false;
    }


}
