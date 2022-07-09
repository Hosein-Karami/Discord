package com.example.discordfx.Server.Rooms.ClientInterfce;

import com.example.discordfx.Server.Rooms.Chats.GeneralChat;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ChannelChatInterface extends GeneralInterface implements Runnable{

    public ChannelChatInterface(GeneralChat chat, Socket clientSocket) {
        super(chat, clientSocket);
    }

    @Override
    public void run() {
        String temp;
        ObjectInputStream inputStream;
        try {
            label:
            while (true) {
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
                temp = (String) inputStream.readObject();
                chat.sendMessage(temp);
                String Info = (String) inputStream.readObject();
                chat.sendMessage(Info);
                switch (temp) {
                    case "#TEXT" -> sendTextMessage(inputStream);
                    case "#FILE" -> sendFile(inputStream);
                    case "#VOICE" -> sendVoice(inputStream);
                    case "#EXIT" ->{
                        exit();
                        break label;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
