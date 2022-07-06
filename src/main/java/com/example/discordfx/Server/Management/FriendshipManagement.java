package com.example.discordfx.Server.Management;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Service.ClientService.AccountsService;
import com.example.discordfx.Server.Service.ClientService.FriendShipServices;
import com.example.discordfx.Server.Start.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FriendshipManagement {

    private final FriendShipServices friendShipService = new FriendShipServices();
    private final Socket clientSocket;

    FriendshipManagement(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    void requestFriendShip(User user){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            String targetUsername = (String) inputStream.readObject();
            User targetUser = Server.accountsService.getParticularUser(targetUsername);
            if(targetUser == null)
                outputStream.writeObject("There is no user with this username");
            else {
                outputStream.writeObject("OK");
                targetUser.loadInformation();
                if (targetUser.checkIsBlock(user.getUsername()))
                    outputStream.writeObject("Target user blocked you");
                else {
                    outputStream.writeObject("OK");
                    if (targetUser.checkIsFriend(user.getUsername()))
                        outputStream.writeObject("You were one of his/her friend from before");
                    else {
                        outputStream.writeObject("OK");
                        targetUser.addNotification(new Notification(user.getUsername() + " send you a friendship request"));
                        targetUser.addPending(user.getUsername());
                        user.addOutputRequest(targetUsername);
                        //friendShipService.requestFriendship(targetUser, user.getId());
                        outputStream.writeObject("Request send successfully");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void invitationsHandle(User user) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            String senderUsername = (String) inputStream.readObject();
            AccountsService accountsService = new AccountsService();
            User targetUser = accountsService.getParticularUser(senderUsername);
            String action = (String) inputStream.readObject();
            if(action.equals("Accept")){
                outputStream.writeObject("You are friend from now");
                user.addFriend(targetUser.getUsername());
                targetUser.addFriend(user.getUsername());
                Notification notification = new Notification(user.getUsername() + " accepted your friendship request");
                targetUser.addNotification(notification);
            }
            else{
                outputStream.writeObject("Friendship request rejected");
                Notification notification = new Notification(user.getUsername() + " rejected your friendship request");
                targetUser.addNotification(notification);
            }
            user.removePending(senderUsername);
            targetUser.removeOutputRequest(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void cancelRequest(User user){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            String targetUsername = (String) inputStream.readObject();
            AccountsService accountsService = new AccountsService();
            User targetUser = accountsService.getParticularUser(targetUsername);
            targetUser.removePending(user.getUsername());
            user.removeOutputRequest(targetUsername);
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
            String targetUsername = (String) inputStream.readObject();
            User targetUser = accountsService.getParticularUser(targetUsername);
            targetUser.removeFriend(user.getUsername());
            user.removeFriend(targetUsername);
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
            for(String x : user.getFriends()){
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
                targetUser.loadInformation();
                if (targetUser.checkIsFriend(user.getUsername()))
                    friendShipService.removeFriend(targetUser, user);
                if (user.checkIsBlock(targetUser.getUsername()))
                    outputStream.writeObject("Blocked before");
                else {
                    outputStream.writeObject("OK");
                    user.addBlock(targetUser.getUsername());
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
            String targetUsername = (String) inputStream.readObject();
            User targetUser = accountsService.getParticularUser(targetUsername);
            user.unblock(targetUser.getUsername());
            outputStream.writeObject("User unblocked successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
