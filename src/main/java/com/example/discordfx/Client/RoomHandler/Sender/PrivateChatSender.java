/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Client.RoomHandler.Sender;

import java.net.Socket;

public class PrivateChatSender extends GeneralSender{

    public PrivateChatSender(Socket socket, String senderUsername) {
        super(socket, senderUsername);
    }

}
