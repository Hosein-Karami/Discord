package com.example.discordfx.Server.Management;


import com.example.discordfx.Log.ServerLog;
import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientManagement implements Runnable{

    private final AccountManagement accountManagement = new AccountManagement();
    private final FriendshipManagement friendshipManagement;
    private final Socket clientSocket;
    private final ServerLog log = new ServerLog();

    public ClientManagement(Socket clientSocket){
        this.clientSocket = clientSocket;
        friendshipManagement = new FriendshipManagement(clientSocket);
    }

    @Override
    public void run() {
        while (true) {
            try {
                InputStream inputStream = clientSocket.getInputStream();
                int request = inputStream.read();
                if(request == 1) {
                    accountManagement.signUp(clientSocket);
                }
                else if(request == 2) {
                    User user = accountManagement.logIn(clientSocket);
                    if(user != null)
                        start(user);
                }
                else
                    break;
            } catch (Exception e) {
                break;
            }
        }
    }

    private void start(User user){
        int choose;
        while (true){
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                InputStream inputStream = clientSocket.getInputStream();
                user.loadInformation();
                outputStream.writeObject(user);
                choose = inputStream.read();
                if(choose == 1)
                    accountManagement.setPicture(user,clientSocket);
                else if(choose == 5)
                    accountManagement.setStatus(user,clientSocket);
                else if(choose == 6)
                    friendshipManagement.requestFriendShip(user);
                else if(choose == 7)
                    friendshipManagement.showFriends(user);
                else if(choose == 8)
                    friendshipManagement.blockUser(user);
                else if(choose == 9)
                    friendshipManagement.showBlocks(user);
                else if(choose == 10)
                    friendshipManagement.invitationsHandle(user);
                else if(choose == 11)
                    accountManagement.changePassword(user,clientSocket);
                else if(choose == 12){
                    ChatManagement chatManagements = new ChatManagement();
                    chatManagements.makePrivateChat(user,clientSocket);
                }
                else if(choose == 13)
                    downloadFile();
                else if(choose == 15){
                    log.logOut(user.getUsername());
                    user.setStatus(Status.Offline);
                    return;
                }
            } catch (Exception e) {
                log.openStreamError(clientSocket.getInetAddress());
                e.printStackTrace();
                return;
            }
        }
    }

    private void downloadFile(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            String name = (String) inputStream.readObject();
            Path path = Paths.get("Files/ChatFiles/"+name);
            if(Files.exists(path)){
                outputStream.writeObject("OK");
                System.out.println("Salam");
                byte[] bytes = Files.readAllBytes(path);
                outputStream.writeObject(bytes);
                outputStream.flush();
            }
            else
                outputStream.writeObject("There is no file with this name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
