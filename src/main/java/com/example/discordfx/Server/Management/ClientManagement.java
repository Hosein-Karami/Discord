package com.example.discordfx.Server.Management;


import com.example.discordfx.Log.ServerLog;
import com.example.discordfx.Moduls.Dto.User.User;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientManagement implements Runnable{

    private final AccountManagement accountManagement = new AccountManagement();
    private final FriendshipManagement friendshipManagement;
    private final Socket clientSocket;
    private InputStream in;
    private OutputStream out;
    private final ServerLog log = new ServerLog();

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
        try {
            int choose = in.read();
            if(choose == 1) {
                ProfileManagement profileManagement = new ProfileManagement(user,clientSocket);
                profileManagement.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
