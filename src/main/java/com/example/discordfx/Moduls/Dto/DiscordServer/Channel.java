/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

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

    /**
     * get access to name of channel
     * @return : name of channel
     */
    public String getName(){
        return name;
    }

    /**
     * get access to port of channel
     * @return : port of channel
     */
    public int getPort(){
        return port;
    }

    /**
     * get access to counter joined members
     * @return : counter joined members
     */
    public int getJoinedClientNumber(){
        return joinedClientNumber;
    }

    /**
     * Is used to increase counter of joined members to channel
     */
    public void increaseJoinedClientNumber(){
        joinedClientNumber++;
    }

    /**
     * Is used to decrease counter of joined members to channel
     */
    public void decreaseJoinedClientNumber(){
        joinedClientNumber--;
    }

    /**
     * Is used to add a banned member's id
     * @param targetId : target Id
     */
    public void addBannedId(Integer targetId){
        banedMembersId.add(targetId);
    }

    /**
     * Is used to delete channel
     */
    public void deleteChannel(){
        name = null;
        banedMembersId.clear();
    }

    /**
     * Is used to check a member is banned or not
     * @param targetUserId : Id of target user
     * @return : boolean value
     */
    public boolean checkIsBanned(Integer targetUserId){
        if(banedMembersId.contains(targetUserId))
            return true;
        return false;
    }

}
