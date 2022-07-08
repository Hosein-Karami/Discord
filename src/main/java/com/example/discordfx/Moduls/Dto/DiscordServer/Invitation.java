package com.example.discordfx.Moduls.Dto.DiscordServer;

import java.io.Serializable;

public class Invitation implements Serializable {

    private final int serverId;
    private final String invitationText;

    public Invitation(int serverId,String senderUsername){
        this.serverId = serverId;
        this.invitationText = senderUsername+" invite you to join this server";
    }

    public String getInvitationText(){
        return invitationText;
    }

    public int getServerId(){
        return serverId;
    }

}
