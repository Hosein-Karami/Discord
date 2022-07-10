package com.example.discordfx.Server.Rooms.Chats;

import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.Messages.Message;
import com.example.discordfx.Server.Rooms.ClientInterfce.ChannelChatInterface;
import com.example.discordfx.Start;
import java.net.Socket;

public class ChannelChat extends GeneralChat{

    private Dserver dserver;
    private Message pinnedMessage = null;

    @Override
    public void join(Socket joinedSocket) {
        try {
            joinSockets.add(joinedSocket);
            sendBeforeMessages(joinedSocket);
            ChannelChatInterface clientInterface = new ChannelChatInterface(this, joinedSocket);
            Start.executorService.execute(clientInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Dserver getDserver(){
        return dserver;
    }

    public Message getPinnedMessage(){
        return pinnedMessage;
    }

    public Message getParticularMessage(int messageIndex){
        return messages.get(messageIndex);
    }

    public int getSizeOfMessages(){
        return messages.size();
    }

    public boolean pinMessage(int messageNumber){
        if(messageNumber <= messages.size()){
            pinnedMessage = messages.get(messageNumber - 1);
            System.out.println(pinnedMessage.getInformation());
            return true;
        }
        else
            return false;
    }

}
