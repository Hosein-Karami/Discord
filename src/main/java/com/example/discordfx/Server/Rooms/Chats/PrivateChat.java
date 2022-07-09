package com.example.discordfx.Server.Rooms.Chats;

import com.example.discordfx.Server.Rooms.ClientInterfce.PrivateChatInterface;
import com.example.discordfx.Server.Start.Main;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class PrivateChat extends GeneralChat{

    private final ExecutorService executorService = Main.executorService;

    @Override
    public void join(Socket joinedSocket) {
        joinSockets.add(joinedSocket);
        PrivateChatInterface clientInterface = new PrivateChatInterface(this,joinedSocket);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(joinedSocket.getInputStream());
            String username = (String) inputStream.readObject();
            if( ! (memberUsernames.contains(username)))
                memberUsernames.add(username);
            sendBeforeMessages(joinedSocket);
            executorService.execute(clientInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
