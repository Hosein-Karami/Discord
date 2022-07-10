/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Moduls.Dto.User;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.DiscordServer.Invitation;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UserLateralInformation implements Serializable {

    @Serial
    private static final long serialVersionUID = 254231684205732173L;

    private final ArrayList<Integer> friendsId = new ArrayList<>();
    private final ArrayList<Integer> blockesId = new ArrayList<>();
    private final ArrayList<Integer> pendingId = new ArrayList<>();
    private final ArrayList<Integer> outputRequestsId = new ArrayList<>();
    private final ArrayList<Integer> discordServers = new ArrayList<>();
    private final ArrayList<Invitation> invitations = new ArrayList<>();
    private final ArrayList<Notification> notifications = new ArrayList<>();
    private final HashMap<String,Integer> privateChats = new HashMap<>();
    private Status status;

    /**
     * get access to blocked id of user
     * @return : blocked id of user
     */
    public ArrayList<Integer> getBlockesId(){
        return blockesId;
    }

    /**
     * get access to pending requests of user
     * @return : pending requests of user
     */
    public ArrayList<Integer> getPendingId(){
        return pendingId;
    }

    /**
     * get access to private chats of user
     * @return : private chats of user
     */
    public HashMap<String,Integer> getPrivateChats(){
        return privateChats;
    }

    /**
     * get access to friends id of user
     * @return : friends id of user
     */
    public ArrayList<Integer> getFriendsId(){
        return friendsId;
    }

    /**
     * get access to invitations of user
     * @return : invitations of user
     */
    public ArrayList<Invitation> getInvitations(){
        return invitations;
    }

    /**
     * get access to id of servers of user
     * @return : id of servers
     */
    public ArrayList<Integer> getDiscordServers(){
        return discordServers;
    }

    /**
     * get access to notifications of user
     * @return : notifications of user
     */
    public ArrayList<Notification> getNotifications(){
        return notifications;
    }

    /**
     * get access to output requests of user
     * @return : output requests of user
     */
    public ArrayList<Integer> getOutputRequestsId() {
        return outputRequestsId;
    }

    /**
     * get access to status of user
     * @return : status of user
     */
    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public void addFriend(Integer friendId){
        friendsId.add(friendId);
    }

    public void addPending(Integer newPending){
        pendingId.add(newPending);
    }

    public void addDiscordServer(Integer newDiscordServer){
        discordServers.add(newDiscordServer);
    }

    public void addOutputRequestUsername(Integer newOutputRequest){
        outputRequestsId.add(newOutputRequest);
    }

    public void addBlock(Integer blockedId){
        blockesId.add(blockedId);
    }

    public void addPrivateChat(String targetUsername,int port){
        privateChats.put(targetUsername,port);
    }

    public void addInvitation(Invitation newInvitation){
        invitations.add(newInvitation);
    }

    public void addNotification(Notification notification){
        notifications.add(notification);
    }

    public void unblock(Integer targetId){
        blockesId.remove(targetId);
    }

    public void removeFriend(Integer targetId){
        friendsId.remove(targetId);
    }

    public void removePending(Integer targetPendingId){
        pendingId.remove(targetPendingId);
    }

    public void removeDiscordServer(String targetDiscordServer){
        discordServers.remove(targetDiscordServer);
    }

    public void removeInvitation(Invitation targetInvitation){
        invitations.remove(targetInvitation);
    }

    public void removeOutputRequest(Integer targetId){
        outputRequestsId.remove(targetId);
    }

    public void deleteNotifications(){
        notifications.clear();
    }

    public boolean checkIsBlock(Integer targetId){
        for(Integer x : blockesId){
            if(x.equals(targetId))
                return true;
        }
        return false;
    }

    public boolean checkIsPending(Integer targetId){
        for(Integer x : pendingId){
            if(x.equals(targetId))
                return true;
        }
        return false;
    }

    public boolean checkIsRequested(Integer targetId){
        for(Integer x : outputRequestsId){
            if(x.equals(targetId))
                return true;
        }
        return false;
    }

    public boolean checkIsFriend(Integer targetId){
        for(Integer x : friendsId){
            if(x.equals(targetId))
                return true;
        }
        return false;
    }

}
