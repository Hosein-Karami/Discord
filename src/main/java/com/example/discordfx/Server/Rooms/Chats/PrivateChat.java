package com.example.discordfx.Server.Rooms.Chats;

import com.example.discordfx.Server.Rooms.ClientInterface;
import com.example.discordfx.Server.Start.Main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;

public class PrivateChat extends GeneralChat{

    private final ExecutorService executorService = Main.executorService;

    public PrivateChat(int port) {
        super(port);
    }

    @Override
    public void join(Socket joinedSocket) {
        joinSockets.add(joinedSocket);
        ClientInterface clientInterface = new ClientInterface(this,joinedSocket);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(joinedSocket.getInputStream());
            String username = (String) inputStream.readObject();
            if( ! (memberUsernames.contains(username)))
                memberUsernames.add(username);
            System.out.println("START");
            sendBeforeMessages(joinedSocket);
            executorService.execute(clientInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendParticularVoice(Socket clientSocket,int voiceIndex){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(Files.readAllBytes(voices.get(voiceIndex).toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
