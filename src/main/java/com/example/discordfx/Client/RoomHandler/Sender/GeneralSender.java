/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Client.RoomHandler.Sender;

import com.example.discordfx.Moduls.Dto.Messages.FileMessage;
import com.example.discordfx.Moduls.Dto.Messages.TextMessage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

public class GeneralSender {

    protected OutputStream out;
    protected final String senderUsername;

    public GeneralSender(Socket socket,String senderUsername){
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.senderUsername = senderUsername;
    }

    /**
     * Is used to send a text message
     * @param text : text of message
     */
    public void sendText(String text){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#TEXT");
            outputStream.writeObject(senderUsername + " is writing...");
            TextMessage message = new TextMessage(senderUsername,text);
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to send a text file
     * @param file : refrence of file
     */
    public void sendFile(File file){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            outputStream.writeObject("#FILE");
            outputStream.writeObject(senderUsername + " is sending a file...");
            outputStream.writeObject(file.getName());
            outputStream.writeObject(new FileMessage(senderUsername,file.getName()));
            outputStream.writeObject(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to send a voice
     */
    public void sendVoice(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#VOICE");
            outputStream.writeObject(senderUsername + " is recording a voice...");
            File voiceFile = new File("ClientFiles/voice.wav");
            byte[] voiceBytes = Files.readAllBytes(voiceFile.toPath());
            outputStream.writeObject(voiceBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used when client want left from chat
     */
    public void exitChat(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#EXIT");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
