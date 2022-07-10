/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Client.RoomHandler.Sender;

import com.example.discordfx.Moduls.Dto.Messages.TextMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChannelChatSender extends GeneralSender{

    public ChannelChatSender(Socket socket, String senderUsername) {
        super(socket, senderUsername);
    }

    /**
     * Is used when user want to pin a message
     * @param messageNumber : target message number
     */
    public void pinMessage(int messageNumber){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#PIN");
            outputStream.writeObject(messageNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used when client want to get pinned message in a channel chat
     */
    public void getPinnedMessage(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#GETPIN");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used when client react to a message
     * @param messageNumber : target message number
     * @param react : react of client to message
     */
    public void react(int messageNumber,String react){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#REACT");
            outputStream.writeObject(messageNumber);
            outputStream.writeObject(react);
            outputStream.writeObject(senderUsername);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used when client want to tag a member of server
     * @param messageText : message text
     * @param taggedUsername : target username
     */
    public void tagMember(String messageText,String taggedUsername){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#TAG");
            TextMessage message = new TextMessage(senderUsername,"Tagged member : " + taggedUsername +"   message : "+messageText);
            outputStream.writeObject(message);
            outputStream.writeObject(taggedUsername);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
