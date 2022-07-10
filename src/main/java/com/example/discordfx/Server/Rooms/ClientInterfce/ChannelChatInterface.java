package com.example.discordfx.Server.Rooms.ClientInterfce;

import com.example.discordfx.Moduls.Dto.Messages.Message;
import com.example.discordfx.Moduls.Dto.Messages.TextMessage;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
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
                if((!(temp.equals("#PIN"))) && (!(temp.equals("#GETPIN")))) {
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
                    case "#REACT" -> react();
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


    private void react(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Integer messageNumber = (Integer) inputStream.readObject();
            Integer reactNumber = (Integer) inputStream.readObject();
            String reacterUsername = (String) inputStream.readObject();
            if(messageNumber <= (((ChannelChat)chat).getSizeOfMessages())) {
                String reaction;
                if(reactNumber == 1)
                    reaction = "\uD83D\uDE00";
                else if(reactNumber == 2)
                    reaction = "\uD83D\uDC4D";
                else
                    reaction = "\uD83D\uDC4E";
                Message targetMessage = ((ChannelChat) chat).getParticularMessage(messageNumber - 1);
                Message reactionMessage = new TextMessage(reacterUsername,"reaction : " + reaction + "   target message : "
                        +targetMessage.getText());
                chat.sendMessage("#REACT");
                chat.sendMessage(reactionMessage.getInformation());
                chat.addMessage(reactionMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
