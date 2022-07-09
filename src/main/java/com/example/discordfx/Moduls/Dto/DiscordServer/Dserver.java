package com.example.discordfx.Moduls.Dto.DiscordServer;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.ServerMembers.Role;
import com.example.discordfx.Server.Start.Server;
import java.io.Serializable;
import java.util.ArrayList;

public class Dserver implements Serializable {

    private String name;
    private final int id;
    private final Member superChatMaker;
    private final ArrayList<Member> members = new ArrayList<>();
    private final ArrayList<Role> roles = new ArrayList<>();
    private final ArrayList<Channel> channels = new ArrayList<>();

    public Dserver(Member superChatMaker,int id){
        this.superChatMaker = superChatMaker;
        this.id = id;
        Server.lastUsedPort++;
        members.add(superChatMaker);
    }

    public void setName(String name){
        this.name = name;
        for(Member x : members)
            x.getUser().updateInformation();
    }

    public String getName(){
        return name;
    }

    public ArrayList<Channel> getChannels(){
        return channels;
    }

    public ArrayList<Member> getMembers(){
        return members;
    }

    public ArrayList<Role> getRoles(){
        return roles;
    }

    public int getId(){
        return id;
    }

    public Member getSuperChatMaker() {
        return superChatMaker;
    }

    public Member getParticularMember(int targetId){
        for(Member x : members){
            if(x.getUser().getId() == targetId)
                return x;
        }
        return null;
    }

    public Role getParticularRole(String roleName){
        for(Role x : roles){
            if(x.getName().equals(roleName))
                return x;
        }
        return null;
    }

    public void addMember(Member newMember){
        for(Member x : members){
            if(x.equals(newMember))
                return;
        }
        members.add(newMember);
        newMember.getUser().addServerChat(id);
        newMember.getUser().addNotification(new Notification("You are added to server chat with name : "+name));
    }

    public void addRole(Role newRole){
        roles.add(newRole);
    }


    public void kickMember(Integer userId){
        int targetIndex = 0;
        for(Member x : members){
            if(x.getUser().getId() == userId)
                break;
            targetIndex++;
        }
        Notification notification = new Notification("You are kicked from serer with name : " + name);
        members.get(targetIndex).getUser().addNotification(notification);
        members.remove(targetIndex);
    }

}
