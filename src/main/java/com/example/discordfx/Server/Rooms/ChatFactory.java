package com.example.discordfx.Server.Rooms;

import com.example.discordfx.Server.Rooms.Chats.GeneralChat;
import com.example.discordfx.Server.Rooms.Chats.PrivateChat;

public class ChatFactory {

    public GeneralChat getProperChat(String chatType){
        if(chatType.equals("Private"))
            return new PrivateChat();
        return null;
    }

}
