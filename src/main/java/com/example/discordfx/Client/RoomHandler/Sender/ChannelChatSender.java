package com.example.discordfx.Client.RoomHandler.Sender;

import java.net.Socket;

public class ChannelChatSender extends GeneralSender{

    public ChannelChatSender(Socket socket, String senderUsername) {
        super(socket, senderUsername);
    }


}
