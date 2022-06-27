package com.example.discordfx.Server.Rooms;

import com.example.discordfx.Log.ServerLog;
import com.example.discordfx.Server.Rooms.Chats.GeneralChat;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connector implements Runnable{

    private final int port;
    private final ServerLog serverLog = new ServerLog();
    private final GeneralChat chat;

    public Connector(int port,String chatType){
        this.port = port;
        chat = new ChatFactory().getProperChat(chatType);
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket client = serverSocket.accept();
                chat.join(client);
            }
        } catch (IOException e) {
            serverLog.openPortError(port);
            e.printStackTrace();
        }
    }

}
