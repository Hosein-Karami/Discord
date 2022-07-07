package com.example.discordfx.Server.Rooms;

import com.example.discordfx.Moduls.Dto.Messages.FileMessage;
import com.example.discordfx.Moduls.Dto.Messages.TextMessage;
import com.example.discordfx.Server.Rooms.Chats.GeneralChat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientInterface implements Runnable{

    private final GeneralChat chat;
    private final Socket clientSocket;

    public ClientInterface(GeneralChat chat, Socket clientSocket){
        this.chat = chat;
        this.clientSocket = clientSocket;
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
                    case "#TEXT":
                        TextMessage message = (TextMessage) inputStream.readObject();
                        chat.sendMessage(message.getInformation());
                        chat.addMessage(message);
                        System.out.println(message.getInformation());
                        break;
                    case "#FILE": {
                        String fileName = (String) inputStream.readObject();
                        FileMessage message_2 = (FileMessage) inputStream.readObject();
                        chat.sendMessage(message_2);
                        byte[] bytes = (byte[]) inputStream.readObject();
                        saveFile(bytes, fileName);
                        chat.addMessage(message_2);
                        break;
                    }
                    case "#VOICE": {
                        String status = (String) inputStream.readObject();
                        if (status.equals("OK")) {
                            byte[] bytes = (byte[]) inputStream.readObject();
                            chat.sendMessage(bytes);
                        }
                        break;
                    }
                    case "#EXIT":
                        chat.removeJoinSocket(clientSocket);
                        chat.sendMessageToParticularSocket("#LEFT",clientSocket);
                        break label;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile(byte[] bytes,String fileName){
        try{
            File file = new File("Files/ChatFiles/"+fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
