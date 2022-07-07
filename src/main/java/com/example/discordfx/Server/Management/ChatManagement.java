package com.example.discordfx.Server.Management;

import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Rooms.Connector;
import com.example.discordfx.Server.Start.Main;
import com.example.discordfx.Server.Start.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

public class ChatManagement {

    private final ExecutorService executorService = Main.executorService;
    private final HashMap<Integer, Connector> connectors = new HashMap<>();

    void makePrivateChat(User user,Socket clientSocket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            String targetUsername = (String) inputStream.readObject();
            User targetUser = Server.accountsService.getParticularUser(targetUsername);
            if(targetUser != null) {
                outputStream.writeObject("OK");
                if (targetUser.checkIsBlock(user.getUsername()))
                    outputStream.writeObject("This user blocked you :)");
                else {
                    outputStream.writeObject("OK");
                    Server.lastUsedPort++;
                    Connector connector = new Connector(Server.lastUsedPort, "Private");
                    connectors.put(Server.lastUsedPort, connector);
                    executorService.execute(connector);
                    outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    outputStream.writeObject(Server.lastUsedPort);
                    outputStream.writeObject(user);
                    user.addPrivateChat(Server.lastUsedPort, targetUsername);
                    targetUser.addPrivateChat(Server.lastUsedPort, user.getUsername());
                }
            }
            else
                outputStream.writeObject("Verification failed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectToPrivateChat(User user,int port,Socket clientSocket){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(port);
            outputStream.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
