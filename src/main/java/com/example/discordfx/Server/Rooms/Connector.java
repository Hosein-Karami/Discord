package com.example.discordfx.Server.Rooms;

import com.example.discordfx.Log.ServerLog;
import com.example.discordfx.Server.Rooms.Chats.GeneralChat;
import java.io.File;
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
        File chatDirectory = new File("Files/ChatVoices/"+port);
        chatDirectory.mkdir();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println(client.getInetAddress());
                chat.join(client);
            }
        } catch (IOException e) {
            serverLog.openPortError(port);
            e.printStackTrace();
        }
    }

}
