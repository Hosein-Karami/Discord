package com.example.discordfx.Moduls.Dto.DiscordServer;

import java.io.Serializable;
import java.util.ArrayList;

public class Channel implements Serializable {

    private String name;
    private final int port;
    private final ArrayList<Integer> banedMembersId = new ArrayList<>();
    private int joinedClientNumber;

    public Channel(String name, int port){
        this.name = name;
        this.port = port;
    }

    public String getName(){
        return name;
    }

    public int getPort(){
        return port;
    }


    public int getJoinedClientNumber(){
        return joinedClientNumber;
    }

    public void increaseJoinedClientNumber(){
        joinedClientNumber++;
    }

    public void decreaseJoinedClientNumber(){
        joinedClientNumber--;
    }

    public void addBannedId(Integer targetId){
        banedMembersId.add(targetId);
    }

    public void deleteChannel(){
        name = null;
        banedMembersId.clear();
    }

    public boolean checkIsBanned(Integer targetUserId){
        if(banedMembersId.contains(targetUserId))
            return true;
        return false;
    }

}
