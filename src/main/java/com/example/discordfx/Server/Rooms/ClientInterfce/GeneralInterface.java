/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

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

    /**
     * Is used to send a text to users
     * @param inputStream .
     */
    public void sendTextMessage(ObjectInputStream inputStream) {
        try {
            TextMessage message = (TextMessage) inputStream.readObject();
            chat.sendMessage(message.getInformation(),clientSocket);
            chat.addMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used to send a file to users
     * @param inputStream .
     */
    public void sendFile(ObjectInputStream inputStream){
        try {
            String fileName = (String) inputStream.readObject();
            FileMessage message_2 = (FileMessage) inputStream.readObject();
            chat.sendMessage(message_2,clientSocket);
            byte[] bytes = (byte[]) inputStream.readObject();
            saveFile(bytes, fileName);
            chat.addMessage(message_2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used to send a voice to users
     * @param inputStream .
     */
    public void sendVoice(ObjectInputStream inputStream) {
        try {
            byte[] bytes = (byte[]) inputStream.readObject();
            chat.sendMessage(bytes,clientSocket);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used to exit from chat
     */
    public void exit(){
        chat.sendMessageToParticularSocket("#LEFT",clientSocket);
        chat.removeSocket(clientSocket);
    }

    /**
     * Is used to save a file in server
     * @param bytes : bytes of file
     * @param fileName : name of file
     */
    private void saveFile(byte[] bytes,String fileName){
        try{
            File file = new File("ServerFiles/ChatFiles/"+fileName);
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
