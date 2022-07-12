/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

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

    /**
     * get access to user of member
     * @return : refrence of user
     */
    public User getUser(){
        AccountsService service = new AccountsService();
        return service.getParticularUser(userId);

    }

    /**
     * get access to id of member's user
     * @return : id of member's user
     */
    public int getUserId(){
        return userId;
    }

    /**
     * get access to roles of member
     * @return : roles of member
     */
    public ArrayList<Role> getRoles(){
        return roles;
    }

    /**
     * Is used to add a role to member
     * @param newRole : new role
     */
    public void addRole(Role newRole){
        roles.add(newRole);
    }

    /**
     * show this member is owner of server chat or not
     * @return : boolean value
     */
    public boolean isOwner(){
        for(Role x : roles){
            if(x.getName().equals("Owner"))
                return true;
        }
        return false;
    }

    /**
     * show member can kick members from server chat or not
     * @return : boolean value
     */
    public boolean canKickMembers(){
        for(Role x : roles){
            if(x.isRemoveMemberFromServer())
                return true;
        }
        return false;
    }

    /**
     * show member can change name of server or not
     * @return : boolean value
     */
    public boolean canChangeServerName(){
        for(Role x : roles){
            if(x.isChangeServerName())
                return true;
        }
        return false;
    }

    /**
     * show member can make a channel or not
     * @return : boolean value
     */
    public boolean canMakeChannel(){
        for(Role x : roles){
            if(x.isMakeChannel())
                return true;
        }
        return false;
    }

    /**
     * show member can delete a channel or not
     * @return : boolean value
     */
    public boolean canDeleteChannel(){
        for(Role x : roles){
            if(x.isDeleteChannel())
                return true;
        }
        return false;
    }

    /**
     * show member can limit members to access to channels or not
     * @return : boolean value
     */
    public boolean canLimitMembersToChannels(){
        for(Role x : roles){
            if(x.isLimitMemberToJoinChannel())
                return true;
        }
        return false;
    }

    /**
     * show member can pin messages in channels or not
     * @return : boolean value
     */
    public boolean canPinMessage(){
        for(Role x : roles){
            if(x.isPinMessage())
                return true;
        }
        return false;
    }

}
