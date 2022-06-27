package com.example.discordfx.Server.Management;

import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Rooms.Connector;
import com.example.discordfx.Server.Start.Main;
import com.example.discordfx.Server.Start.Server;

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
            String jwtToken = (String) inputStream.readObject();
            if(jwtToken.equals(user.getJwtToken())){
                outputStream.writeObject("Ok");
                String targetUsername = (String) inputStream.readObject();
                User targetUser = Server.accountsService.getParticularUser(targetUsername);
                if(targetUser != null) {
                    outputStream.writeObject("Ok");
                    if(targetUser.checkIsBlock(user.getId()))
                        outputStream.writeObject("This user blocked you :)");
                    else {
                        outputStream.writeObject("Ok");
                        Server.lastUsedPort++;
                        Connector connector = new Connector(Server.lastUsedPort, "Private");
                        connectors.put(Server.lastUsedPort,connector);
                        executorService.execute(connector);
                        outputStream.writeObject(Server.lastUsedPort);
                        user.addPrivateChat(Server.lastUsedPort,targetUsername);
                        targetUser.addPrivateChat(Server.lastUsedPort,user.getUsername());
                    }
                }
                else
                    outputStream.writeObject("There is no user with this username");
            }
            else
                outputStream.writeObject("Verification failed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
