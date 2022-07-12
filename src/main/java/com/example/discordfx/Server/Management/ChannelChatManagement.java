/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

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

    /**
     * Is used to make a channel in a discord server
     * @param socket .
     * @param dserver : target discord server
     */
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
                runChannel(channelPort,dserver);
                outputStream.writeObject("Channel made successfully");
            }
            else
                outputStream.writeObject("This name is used before");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to run a channel
     * @param channelPort : port of channel
     * @param dserver : target discord server
     */
    private void runChannel(int channelPort,Dserver dserver){
        Connector connector = new Connector(channelPort);
        connector.setChat(dserver);
        Main.executorService.execute(connector);
    }

}
