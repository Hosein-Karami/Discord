/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Rooms.ClientInterfce;

import com.example.discordfx.Server.Rooms.Chats.GeneralChat;
import java.io.ObjectInputStream;
import java.net.Socket;

public class PrivateChatInterface extends GeneralInterface implements Runnable{


    public PrivateChatInterface(GeneralChat chat, Socket socket) {
        super(chat, socket);
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
                if(!(temp.equals("#EXIT"))) {
                    chat.sendMessage(temp);
                    String Info = (String) inputStream.readObject();
                    chat.sendMessage(Info);
                }
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
