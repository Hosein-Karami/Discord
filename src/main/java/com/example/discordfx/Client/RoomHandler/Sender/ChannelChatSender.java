package com.example.discordfx.Client.RoomHandler.Sender;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChannelChatSender extends GeneralSender{

    public ChannelChatSender(Socket socket, String senderUsername) {
        super(socket, senderUsername);
    }

    public void pinMessage(int messageNumber){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#PIN");
            outputStream.writeObject(messageNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPinnedMessage(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#GETPIN");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
