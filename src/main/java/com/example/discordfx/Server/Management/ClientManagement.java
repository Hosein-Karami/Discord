/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Management;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Service.DserverService;
import com.example.discordfx.Server.Service.ProfileService;
import com.example.discordfx.Server.Start.Server;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientManagement implements Runnable{

    private final AccountManagement accountManagement = new AccountManagement();
    private final FriendshipManagement friendshipManagement;
    private final PrivateChatManagement chatManagement = new PrivateChatManagement();
    private final DserverManagement dserverManagement = new DserverManagement();
    private final Socket clientSocket;
    private User user;
    private InputStream in;
    private OutputStream out;

    public ClientManagement(Socket clientSocket){
        this.clientSocket = clientSocket;
        try {
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        friendshipManagement = new FriendshipManagement(clientSocket);
    }

    /**
     * Is used to management client's requests
     */
    @Override
    public void run() {
        while (true) {
            try {
                int request = in.read();
                if(request == 1) {
                    User user = accountManagement.logIn(clientSocket);
                    if(user != null)
                        start(user);
                }
                else if(request == 2)
                    accountManagement.signUp(clientSocket);
                else
                    break;
            } catch (Exception e) {
                break;
            }
        }
    }

    /**
     * Is used when user log in
     * @param user : refrence of user who log in
     */
    private void start(User user){
        this.user = user;
        while (true) {
            try {
                int choose = in.read();
                user.loadInformation();
                if (choose == 1) {
                    ProfileService profileService = new ProfileService(user, clientSocket);
                    profileService.start();
                } else if (choose == 2)
                    downloadFile();
                else if(choose == 3)
                    accountManagement.setStatus(user,clientSocket);
                else if(choose == 4)
                    accountManagement.sendUserInfoWithUsername(clientSocket);
                else if(choose == 5)
                    friendshipManagement.requestFriendShip(user);
                else if(choose == 6)
                    friendshipManagement.invitationsHandle(user);
                else if(choose == 7)
                    accountManagement.sendUserInfoWithId(clientSocket);
                else if(choose == 8)
                    friendshipManagement.cancelRequest(user);
                else if(choose == 9)
                    friendshipManagement.block(user);
                else if(choose == 10)
                    friendshipManagement.unblock(user);
                else if(choose == 11)
                    friendshipManagement.removeFriend(user);
                else if(choose == 12)
                    friendshipManagement.sendOnlineFriends(user);
                else if(choose == 13)
                    sendNotifications();
                else if(choose == 14)
                    user.deleteNotifications();
                else if(choose == 15)
                    chatManagement.makePrivateChat(user,clientSocket);
                else if(choose == 16)
                    connectToPrivateChat();
                else if(choose == 17)
                    dserverManagement.makeServerChat(clientSocket,user);
                else if(choose == 20)
                    sendUserInfo();
                else if(choose == 21)
                    dserverManagement.sendServerChatInfo(clientSocket);
                else if(choose == 22)
                    connectToServerChat();
                else if(choose == 23)
                    sendUserServerChats();
                else if(choose == 24)
                    dserverManagement.invitationHandle(user,clientSocket);
                else {
                    user.setStatus(Status.Offline);
                    break;
                }
            } catch (IOException e) {
                user.setStatus(Status.Offline);
                break;
            }
        }
    }

    /**
     * Is used to send a user information
     */
    private void sendUserInfo(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used when user want to download a file from server
     */
    private void downloadFile(){
        while (true) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(in);
                String status = (String) inputStream.readObject();
                if(status.equals("Break"))
                    break;
                ObjectOutputStream outputStream = new ObjectOutputStream(out);
                String name = (String) inputStream.readObject();
                Path path = Paths.get("ServerFiles/ChatFiles/" + name);
                if (Files.exists(path)) {
                    outputStream.writeObject("OK");
                    byte[] bytes = Files.readAllBytes(path);
                    outputStream.writeObject(bytes);
                    outputStream.flush();
                } else
                    outputStream.writeObject("There is no file with this name");
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Is used to send notifications of user
     */
    private void sendNotifications(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            for(Notification x : user.getInformation().getNotifications())
                outputStream.writeObject(x);
            outputStream.writeObject(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to connect to a private chat
     */
    public void connectToPrivateChat(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Integer port = (Integer) inputStream.readObject();
            chatManagement.connectToPrivateChat(user,port,clientSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to connect to a server chat
     */
    private void connectToServerChat(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Integer serverId = (Integer) inputStream.readObject();
            DserverService service = new DserverService(Server.discordServers.get(serverId),clientSocket,user);
            service.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to send previous messages of a chat to user
     */
    private void sendUserServerChats(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            for(Integer x : user.getInformation().getDiscordServers())
                outputStream.writeObject(Server.discordServers.get(x).getName());
            outputStream.writeObject(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}