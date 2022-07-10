package com.example.discordfx.Server.Rooms.ClientInterfce;

import com.example.discordfx.Moduls.Dto.Messages.FileMessage;
import com.example.discordfx.Moduls.Dto.Messages.TextMessage;
import com.example.discordfx.Server.Rooms.Chats.GeneralChat;
import java.io.*;
import java.net.Socket;

public abstract class GeneralInterface {

    protected final GeneralChat chat;
    protected final Socket clientSocket;

    public GeneralInterface(GeneralChat chat,Socket clientSocket){
        this.chat = chat;
        this.clientSocket = clientSocket;
    }

    public void sendTextMessage(ObjectInputStream inputStream) {
        try {
            TextMessage message = (TextMessage) inputStream.readObject();
            chat.sendMessage(message.getInformation());
            chat.addMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendFile(ObjectInputStream inputStream){
        try {
            String fileName = (String) inputStream.readObject();
            FileMessage message_2 = (FileMessage) inputStream.readObject();
            chat.sendMessage(message_2);
            byte[] bytes = (byte[]) inputStream.readObject();
            saveFile(bytes, fileName);
            chat.addMessage(message_2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendVoice(ObjectInputStream inputStream) {
        try {
            byte[] bytes = (byte[]) inputStream.readObject();
            chat.sendMessage(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void exit(){
        chat.sendMessageToParticularSocket("#LEFT",clientSocket);
        chat.removeJoinSocket(clientSocket);
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
