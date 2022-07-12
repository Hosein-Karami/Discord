/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

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

public class PrivateChatManagement {

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
                if (targetUser.getInformation().checkIsBlock(user.getId()))
                    outputStream.writeObject("This user blocked you :)");
                else {
                    outputStream.writeObject("OK");
                    Server.lastUsedPort++;
                    Connector connector = new Connector(Server.lastUsedPort);
                    connector.setChat();
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
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            String temp = (String) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
