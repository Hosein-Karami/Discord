/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Rooms.ClientInterfce;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.Messages.TextMessage;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Rooms.Chats.ChannelChat;
import com.example.discordfx.Server.Rooms.Chats.GeneralChat;
import com.example.discordfx.Server.Service.AccountsService;
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
                if((!(temp.equals("#PIN"))) && (!(temp.equals("#GETPIN"))) && (!(temp.equals("#REACT"))) &&
                        (!(temp.equals("#EXIT"))) && (!(temp.equals("#TAG")))) {
                    chat.sendMessage(temp);
                    String Info = (String) inputStream.readObject();
                    chat.sendMessage(Info);
                }
                switch (temp) {
                    case "#TEXT" -> sendTextMessage(inputStream);
                    case "#FILE" -> sendFile(inputStream);
                    case "#VOICE" -> sendVoice(inputStream);
                    case "#GETPIN" ->{
                        chat.sendMessageToParticularSocket(temp,clientSocket);
                        chat.sendMessageToParticularSocket(((ChannelChat)chat).getPinnedMessage(),clientSocket);
                    }
                    case "#PIN"-> {
                        chat.sendMessageToParticularSocket(temp,clientSocket);
                        Integer messageNumber = (Integer) inputStream.readObject();
                        pinMessage(messageNumber);
                    }
                    case "#REACT" -> react(inputStream);
                    case "#TAG" -> tag(inputStream);
                    case "#EXIT" ->{
                        exit();
                        break label;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void pinMessage(int messageNumber) {
        if(((ChannelChat)chat).pinMessage(messageNumber))
            chat.sendMessageToParticularSocket("OK",clientSocket);
        else
            chat.sendMessageToParticularSocket("ERROR",clientSocket);
    }

    private void react(ObjectInputStream inputStream){
        try {
            Integer messageNumber = (Integer) inputStream.readObject();
            String react = (String) inputStream.readObject();
            String reactorUsername = (String) inputStream.readObject();
            if((messageNumber > 0) && (messageNumber <= chat.getMessagesSize())){
                String reactEmoji;
                if(react.equals("LIKE"))
                    reactEmoji = "\uD83D\uDC4D";
                else if(react.equals("DISLIKE"))
                    reactEmoji = "\uD83D\uDC4E";
                else
                    reactEmoji = "\uD83D\uDE00";
                TextMessage reactMessage = new TextMessage(reactorUsername,"React : "+reactEmoji+"  react message : "+
                        chat.getParticularMessage(messageNumber - 1).getText());
                chat.sendMessage("#REACT");
                chat.sendMessage(reactMessage.getInformation());
                chat.addMessage(reactMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void tag(ObjectInputStream inputStream){
        try {
            TextMessage message = (TextMessage) inputStream.readObject();
            String taggedUsername = (String) inputStream.readObject();
            AccountsService service = new AccountsService();
            User targetUser = service.getParticularUser(taggedUsername);
            if((targetUser != null) && (((ChannelChat)chat).getDserver().getParticularMember(targetUser.getId()) != null)){
                chat.sendMessage("#TAGMESSAGE");
                chat.sendMessage(message.getInformation());
                Notification notification = new Notification("You are tagged,"+message.getInformation());
                targetUser.addNotification(notification);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
