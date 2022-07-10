package com.example.discordfx.Server.Management;

import com.example.discordfx.Moduls.Dto.DiscordServer.Channel;
import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Server.Rooms.Connector;
import com.example.discordfx.Server.Start.Main;
import com.example.discordfx.Server.Start.Server;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChannelChatManagement {

    public void makeChannel(Socket socket, Dserver dserver){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            String channelName = (String) inputStream.readObject();
            if(dserver.getParticularChannel(channelName) == null){
                Server.lastUsedPort++;
                int channelPort = Server.lastUsedPort;
                Channel channel = new Channel(channelName,channelPort);
                dserver.addChannel(channel);
                runChannel(channelPort);
                outputStream.writeObject("Channel made successfully");
            }
            else
                outputStream.writeObject("This name is used before");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runChannel(int channelPort){
        Connector connector = new Connector(channelPort,"Channel");
        Main.executorService.execute(connector);
    }

}
