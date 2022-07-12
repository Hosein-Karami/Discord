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

    /**
     * Is used to set status of user
     * @param status : status of user
     */
    public void setStatus(Status status){
        this.status = status;
    }

    /**
     * Is used to add a friend id
     * @param friendId : target id
     */
    public void addFriend(Integer friendId){
        friendsId.add(friendId);
    }

    /**
     * Is used to a pending request
     * @param newPending .
     */
    public void addPending(Integer newPending){
        pendingId.add(newPending);
    }

    /**
     * Is used to add a server chat id
     * @param newDiscordServer : target id
     */
    public void addDiscordServer(Integer newDiscordServer){
        discordServers.add(newDiscordServer);
    }

    /**
     * Is used to a output request
     * @param newOutputRequest .
     */
    public void addOutputRequestUsername(Integer newOutputRequest){
        outputRequestsId.add(newOutputRequest);
    }

    /**
     * Is used to add a block id
     * @param blockedId : target id
     */
    public void addBlock(Integer blockedId){
        blockesId.add(blockedId);
    }

    /**
     * Is used to add a private chat
     * @param port : port of private chat
     * @param targetUsername : other username in private chat
     */
    public void addPrivateChat(String targetUsername,int port){
        privateChats.put(targetUsername,port);
    }

    /**
     * Is used to add a invitation to a server chat
     * @param newInvitation : target invitation
     */
    public void addInvitation(Invitation newInvitation){
        invitations.add(newInvitation);
    }

    /**
     * Is used to add a new notification
     * @param notification : new notification
     */
    public void addNotification(Notification notification){
        notifications.add(notification);
    }

    /**
     * Is used to unblock a user
     * @param targetId : user's id
     */
    public void unblock(Integer targetId){
        blockesId.remove(targetId);
    }

    /**
     * Is used to remove a friend's id
     * @param targetId : target user's id
     */
    public void removeFriend(Integer targetId){
        friendsId.remove(targetId);
    }

    /**
     * Is used to remove a pending request
     * @param targetPendingId : target pending's id
     */
    public void removePending(Integer targetPendingId){
        pendingId.remove(targetPendingId);
    }

    /**
     * Is used to remove a server chat with id
     * @param targetDiscordServer : Id of target server chat
     */
    public void removeDiscordServer(Integer targetDiscordServer){
        discordServers.remove(targetDiscordServer);
    }

    /**
     * Is used to remove an invitation
     * @param targetInvitation : Id of target invitation
     */
    public void removeInvitation(Invitation targetInvitation){
        invitations.remove(targetInvitation);
    }

    /**
     * Is used to remove an output request
     * @param targetId :
     */
    public void removeOutputRequest(Integer targetId){
        outputRequestsId.remove(targetId);
    }

    /**
     * Is used to delete a notification
     */
    public void deleteNotifications(){
        notifications.clear();
    }

    /**
     * Is used to check this user is blocked target user or not
     * @param targetId : target user's id
     * @return : result
     */
    public boolean checkIsBlock(Integer targetId){
        for(Integer x : blockesId){
            if(x.equals(targetId))
                return true;
        }
        return false;
    }

    /**
     * Is used to check a particular user is requested to this user or not
     * @param targetId : target user's id
     * @return : result
     */
    public boolean checkIsRequested(Integer targetId){
        for(Integer x : outputRequestsId){
            if(x.equals(targetId))
                return true;
        }
        return false;
    }

    /**
     * Is used to check a particular user is friend with this user or not
     * @param targetId : target user's id
     * @return : result
     */
    public boolean checkIsFriend(Integer targetId){
        for(Integer x : friendsId){
            if(x.equals(targetId))
                return true;
        }
        return false;
    }

}
