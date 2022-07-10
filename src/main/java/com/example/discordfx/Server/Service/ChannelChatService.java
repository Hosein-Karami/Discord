package com.example.discordfx.Server.Service;

import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import java.net.Socket;

public class ChannelChatService {

    private final Socket socket;
    private final Dserver dserver;

    public ChannelChatService(Dserver dserver,Socket socket){
        this.dserver = dserver;
        this.socket = socket;
    }

    public void start(){

    }

}
