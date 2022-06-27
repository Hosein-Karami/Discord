package com.example.discordfx.Moduls.Dto.User;

import com.example.discordfx.Lateral.Notification;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UserLateralInformation implements Serializable {

    @Serial
    private static final long serialVersionUID = 254231684205732173L;

    private final ArrayList<Integer> friendsId = new ArrayList<>();
    private final ArrayList<Integer> blockesId = new ArrayList<>();
    private final ArrayList<Integer> invitationId = new ArrayList<>();
    private final ArrayList<Notification> notifications = new ArrayList<>();
    private final HashMap<String,Integer> privateChats = new HashMap<>();
    private Status status;

    public ArrayList<Integer> getBlockedId(){
        return blockesId;
    }

    public ArrayList<Integer> getInvitationId(){
        return invitationId;
    }

    public HashMap<String,Integer> getPrivateChats(){
        return privateChats;
    }

    public ArrayList<Integer> getFriendsId(){
        return friendsId;
    }

    public ArrayList<Notification> getNotifications(){
        return notifications;
    }

    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public void addFriend(int friendId){
        friendsId.add(friendId);
    }

    public void addBlock(Integer blockedId){
        blockesId.add(blockedId);
    }

    public void addInvitation(int senderId){
        invitationId.add(senderId);
    }

    public void addPrivateChat(String targetUsername,int port){
        privateChats.put(targetUsername,port);
    }

    public void addNotification(Notification notification){
        notifications.add(notification);
    }

    public void removeFriend(int targetUserId){
        friendsId.remove(((Integer) targetUserId));
    }

    public void deleteNotifications(){
        notifications.clear();
    }

    public void deleteInvitation(int targetId){
        invitationId.remove(((Integer) targetId));
    }

    public boolean checkIsBlock(Integer targetUserId){
        for(Integer x : blockesId){
            if(x.equals(targetUserId))
                return true;
        }
        return false;
    }

    public boolean checkIsFriend(int targetUserId){
        for(Integer x : friendsId){
            if(x == targetUserId)
                return true;
        }
        return false;
    }

}
