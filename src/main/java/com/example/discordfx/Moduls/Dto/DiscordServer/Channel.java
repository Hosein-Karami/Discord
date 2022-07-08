package com.example.discordfx.Moduls.Dto.DiscordServer;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.User.User;

import java.util.ArrayList;

public class Channel {

    private String name;
    private final int port;
    private final ArrayList<Member> banedMembers = new ArrayList<>();
    private int joinedClientNumber;

    public Channel(String name, int port){
        this.name = name;
        this.port = port;
    }

    public void setName(String newName,ArrayList<Member> serverMembers){
        Notification notification = new Notification("Name of " + name + " changed to "+newName);
        User user;
        for(Member x : serverMembers) {
            user = x.getUser();
            user.addNotification(notification);
        }
        name = newName;
    }

    public void increaseJoinedClientNumber(){
        joinedClientNumber++;
    }

    public void decreaseJoinedClientNumber(){
        joinedClientNumber--;
    }

    public String getName(){
        return name;
    }

    public int getPort(){
        return port;
    }

    public Member getParticularBannedMember(String username){
        for(Member x : banedMembers){
            if(x.getUser().getUsername().equals(username))
                return x;
        }
        return null;
    }

    public int getJoinedClientNumber(){
        return joinedClientNumber;
    }

    public void addBannedMember(Member newMember){
        Notification notification = new Notification("You are banned to access to channel with name : " + name);
        newMember.getUser().addNotification(notification);
        banedMembers.add(newMember);
    }

    public void removeBannedMember(Member targetMember){
        Notification notification = new Notification("You can join in channel with name : " + name + " from now.");
        targetMember.getUser().addNotification(notification);
        banedMembers.remove(targetMember);
    }

    public void deleteChannel(){
        name = null;
        banedMembers.clear();
    }

}
