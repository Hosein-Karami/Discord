package com.example.discordfx.Client.RoomHandler.Sender;

import java.net.Socket;

public class PrivateChatSender extends GeneralSender{

    public PrivateChatSender(Socket socket, String senderUsername) {
        super(socket, senderUsername);
    }

}
