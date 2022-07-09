package com.example.discordfx.Server.Management;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Service.AccountsService;
import com.example.discordfx.Server.Start.Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class FriendshipManagement {

    private final Socket clientSocket;

    FriendshipManagement(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    void requestFriendShip(User user) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            String targetUsername = (String) inputStream.readObject();
            User targetUser = Server.accountsService.getParticularUser(targetUsername);
            if (targetUser == null)
                outputStream.writeObject("There is no user with this username");
            else {
                outputStream.writeObject("OK");
                targetUser.loadInformation();
                if (targetUser.checkIsBlock(user.getId()))
                    outputStream.writeObject("Target user blocked you");
                else {
                    outputStream.writeObject("OK");
                    if (targetUser.checkIsFriend(user.getId()))
                        outputStream.writeObject("You were one of his/her friend from before");
                    if (user.checkIsRequested(targetUser.getId()))
                        outputStream.writeObject("You requested to this user before");
                    else {
                        outputStream.writeObject("OK");
                        if (user.checkIsFriend(targetUser.getId()))
                            outputStream.writeObject("You are friend from before");
                        else {
                            outputStream.writeObject("OK");
                            targetUser.addNotification(new Notification(user.getUsername() + " send you a friendship request"));
                            targetUser.addPending(user.getId());
                            user.addOutputRequest(targetUser.getId());
                            outputStream.writeObject("Request send successfully");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void invitationsHandle(User user) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Integer senderId = (Integer) inputStream.readObject();
            AccountsService accountsService = new AccountsService();
            User targetUser = accountsService.getParticularUser(senderId);
            String action = (String) inputStream.readObject();
            if(action.equals("Accept")){
                outputStream.writeObject("You are friend from now");
                user.addFriend(targetUser.getId());
                targetUser.addFriend(user.getId());
                Notification notification = new Notification(user.getUsername() + " accepted your friendship request");
                targetUser.addNotification(notification);
            }
            else{
                outputStream.writeObject("Friendship request rejected");
                Notification notification = new Notification(user.getUsername() + " rejected your friendship request");
                targetUser.addNotification(notification);
            }
            user.removePending(senderId);
            targetUser.removeOutputRequest(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void cancelRequest(User user){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            Integer targetId = (Integer) inputStream.readObject();
            AccountsService accountsService = new AccountsService();
            User targetUser = accountsService.getParticularUser(targetId);
            targetUser.removePending(user.getId());
            user.removeOutputRequest(targetId);
            outputStream.writeObject("Request canceled successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void removeFriend(User user){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            AccountsService accountsService = new AccountsService();
            Integer targetId = (Integer) inputStream.readObject();
            User targetUser = accountsService.getParticularUser(targetId);
            targetUser.removeFriend(user.getId());
            user.removeFriend(targetId);
            outputStream.writeObject("Friend removed successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendOnlineFriends(User user){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            AccountsService accountsService = new AccountsService();
            User friend;
            for(Integer x : user.getFriends()){
                friend = accountsService.getParticularUser(x);
                if(friend.getStatus() == Status.Online)
                    outputStream.writeObject(x);
            }
            outputStream.writeObject(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void block(User user){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            String targetUsername = (String) inputStream.readObject();
            User targetUser = Server.accountsService.getParticularUser(targetUsername);
            if(targetUser == null)
                outputStream.writeObject("Error");
            else {
                outputStream.writeObject("OK");
                if(user.checkIsBlock(targetUser.getId()))
                    outputStream.writeObject("Blocked before");
                else{
                    outputStream.writeObject("OK");
                    targetUser.loadInformation();
                    if (targetUser.checkIsFriend(user.getId())) {
                        user.removeFriend(targetUser.getId());
                        targetUser.removeFriend(user.getId());
                    }
                    user.addBlock(targetUser.getId());
                    outputStream.writeObject("User blocked successfully");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unblock(User user){
        try {
            AccountsService accountsService = new AccountsService();
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            Integer targetId = (Integer) inputStream.readObject();
            User targetUser = accountsService.getParticularUser(targetId);
            user.unblock(targetUser.getId());
            outputStream.writeObject("User unblocked successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
