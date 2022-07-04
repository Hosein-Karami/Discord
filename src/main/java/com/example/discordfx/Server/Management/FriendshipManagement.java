package com.example.discordfx.Server.Management;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Service.ClientService.AccountsService;
import com.example.discordfx.Server.Service.ClientService.FriendShipServices;
import com.example.discordfx.Server.Start.Server;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                if (targetUser.checkIsBlock(user.getId()))
                    outputStream.writeObject("Target user blocked you");
                else {
                    outputStream.writeObject("OK");
                    if (targetUser.checkIsFriend(user.getId()))
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

    void blockUser(User user){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            String jwtToken = (String) inputStream.readObject();
            if(jwtToken.equals(user.getJwtToken())){
                outputStream.writeObject("Ok");
                String targetUsername = (String) inputStream.readObject();
                User targetUser = Server.accountsService.getParticularUser(targetUsername);
                if(targetUser == null)
                    outputStream.writeObject("There is no user with this username.");
                else{
                    outputStream.writeObject("Ok");
                    targetUser.loadInformation();
                    if(targetUser.checkIsFriend(user.getId()))
                        friendShipService.removeFriend(targetUser,user);
                    if(user.checkIsBlock(targetUser.getId()))
                        outputStream.writeObject("You blocked this user before.");
                    else{
                        outputStream.writeObject("Ok");
                        System.out.println(targetUser.getUsername() + "    " + targetUser.getId());
                        user.addBlock(targetUser.getId());
                        outputStream.writeObject("User blocked successfully.");
                    }
                }
            }
            else
                outputStream.writeObject("Verification failed.");
        } catch (Exception e) {
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
            user.removePending(senderUsername);
            targetUser.removeOutputRequest(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void cancelRequest(User user,Socket socket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            String targetUsername = (String) inputStream.readObject();
            AccountsService accountsService = new AccountsService();
            User targetUser = accountsService.getParticularUser(targetUsername);
            targetUser.removePending(user.getUsername());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("Request canceled successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
