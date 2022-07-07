package com.example.discordfx.Server.Management;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientManagement implements Runnable{

    private final AccountManagement accountManagement = new AccountManagement();
    private final FriendshipManagement friendshipManagement;
    private final ChatManagement chatManagement = new ChatManagement();
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

    private void start(User user){
        this.user = user;
        while (true) {
            try {
                int choose = in.read();
                if (choose == 1) {
                    ProfileManagement profileManagement = new ProfileManagement(user, clientSocket);
                    profileManagement.start();
                } else if (choose == 2)
                    downloadFile();
                else if(choose == 3)
                    setStatus();
                else if(choose == 4){
                    ObjectOutputStream outputStream = new ObjectOutputStream(out);
                    outputStream.writeObject(user);
                }
                else if(choose == 5)
                    friendshipManagement.requestFriendShip(user);
                else if(choose == 6)
                    friendshipManagement.invitationsHandle(user);
                else if(choose == 7)
                    accountManagement.sendProfileImage(clientSocket);
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
                else if(choose == 20){
                    ObjectOutputStream outputStream = new ObjectOutputStream(out);
                    outputStream.writeObject(user);
                }
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

    private void downloadFile(){
        while (true) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(in);
                String status = (String) inputStream.readObject();
                if(status.equals("Break"))
                    break;
                ObjectOutputStream outputStream = new ObjectOutputStream(out);
                String name = (String) inputStream.readObject();
                Path path = Paths.get("Files/ChatFiles/" + name);
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

    private void setStatus(){
        ObjectInputStream inputStream;
        Status status;
        while (true){
            try {
                inputStream = new ObjectInputStream(in);
                status = (Status) inputStream.readObject();
                if(status == null)
                    break;
                user.setStatus(status);
            }catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
    }

    private void sendNotifications(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            for(Notification x : user.getNotifications())
                outputStream.writeObject(x);
            outputStream.writeObject(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToPrivateChat(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Integer port = (Integer) inputStream.readObject();
            chatManagement.connectToPrivateChat(user,port,clientSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}