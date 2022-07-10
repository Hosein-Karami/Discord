package com.example.discordfx.Server.Rooms;

import com.example.discordfx.Log.ServerLog;
import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Server.Rooms.Chats.ChannelChat;
import com.example.discordfx.Server.Rooms.Chats.GeneralChat;
import com.example.discordfx.Server.Rooms.Chats.PrivateChat;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class Connector implements Runnable{

    private final int port;
    private final ServerLog serverLog = new ServerLog();
    private GeneralChat chat;

    public Connector(int port){
        this.port = port;

    }

    public void setChat(Dserver dserver){
        chat = new ChannelChat(dserver);
    }

    public void setChat(){
        chat = new PrivateChat();
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
