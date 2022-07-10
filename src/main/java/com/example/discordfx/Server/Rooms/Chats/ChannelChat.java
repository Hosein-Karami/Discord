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

    public void pinMessage(Message pinnedMessage){
        this.pinnedMessage = pinnedMessage;
    }

    public Message getPinnedMessage(){
        return pinnedMessage;
    }

}
