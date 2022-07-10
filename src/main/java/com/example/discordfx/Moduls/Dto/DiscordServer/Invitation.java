/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Moduls.Dto.DiscordServer;

import java.io.Serializable;

public class Invitation implements Serializable {

    private final int serverId;
    private final String invitationText;

    public Invitation(int serverId,String senderUsername){
        this.serverId = serverId;
        this.invitationText = senderUsername+" invite you to join this server";
    }

    /**
     * get access to text of invitation
     * @return : invitation text
     */
    public String getInvitationText(){
        return invitationText;
    }

    /**
     * get access to id of server which he/she invited to
     * @return : server's id
     */
    public int getServerId(){
        return serverId;
    }

}
