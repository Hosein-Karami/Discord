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

    private final ArrayList<String> friendsUsername = new ArrayList<>();
    private final ArrayList<String> blockesUsername = new ArrayList<>();
    private final ArrayList<String> pendingUsernames = new ArrayList<>();
    private final ArrayList<String> outputRequestsUsernames = new ArrayList<>();
    private final ArrayList<Integer> discordServers = new ArrayList<>();
    private final ArrayList<Invitation> invitations = new ArrayList<>();
    private final ArrayList<Notification> notifications = new ArrayList<>();
    private final HashMap<String,Integer> privateChats = new HashMap<>();
    private Status status;

    public ArrayList<String> getBlockesUsername(){
        return blockesUsername;
    }

    public ArrayList<String> getPendingUsernames(){
        return pendingUsernames;
    }

    public HashMap<String,Integer> getPrivateChats(){
        return privateChats;
    }

    public ArrayList<String> getFriendsUsernames(){
        return friendsUsername;
    }

    public ArrayList<Invitation> getInvitations(){
        return invitations;
    }

    public ArrayList<Integer> getDiscordServers(){
        return discordServers;
    }

    public ArrayList<Notification> getNotifications(){
        return notifications;
    }

    public ArrayList<String> getOutputRequestsUsernames() {
        return outputRequestsUsernames;
    }

    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public void addFriend(String friendUsername){
        friendsUsername.add(friendUsername);
    }

    public void addPending(String newPending){
        pendingUsernames.add(newPending);
    }

    public void addDiscordServer(Integer newDiscordServer){
        discordServers.add(newDiscordServer);
    }

    public void addOutputRequestUsername(String newOutputRequest){
        outputRequestsUsernames.add(newOutputRequest);
    }

    public void addBlock(String blockedUsername){
        blockesUsername.add(blockedUsername);
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

    public void unblock(String targetUsername){
        blockesUsername.remove(targetUsername);
    }

    public void removeFriend(String targetUsername){
        friendsUsername.remove(targetUsername);
    }

    public void removePending(String targetPendingUsername){
        pendingUsernames.remove(targetPendingUsername);
    }

    public void removeDiscordServer(String targetDiscordServer){
        discordServers.remove(targetDiscordServer);
    }

    public void removeInvitation(Invitation targetInvitation){
        invitations.remove(targetInvitation);
    }

    public void removeOutputRequest(String targetUsername){
        outputRequestsUsernames.remove(targetUsername);
    }

    public void deleteNotifications(){
        notifications.clear();
    }

    public boolean checkIsBlock(String targetUsername){
        for(String x : blockesUsername){
            if(x.equals(targetUsername))
                return true;
        }
        return false;
    }

    public boolean checkIsPending(String targetUsername){
        for(String x : pendingUsernames){
            if(x.equals(targetUsername))
                return true;
        }
        return false;
    }

    public boolean checkIsFriend(String targetUsername){
        for(String x : friendsUsername){
            if(x.equals(targetUsername))
                return true;
        }
        return false;
    }

}
