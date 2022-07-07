package com.example.discordfx.Client.RoomHandler.Reciever;

import com.example.discordfx.Client.RoomHandler.VoiceManagement.AudioPlayer;
import com.example.discordfx.Moduls.Dto.Messages.FileMessage;
import com.example.discordfx.Moduls.Dto.Messages.TextMessage;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;

public class GeneralReciever {

    private final TextArea textArea;

    public GeneralReciever(TextArea textArea){
        this.textArea = textArea;
    }

    public void showMessage(String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.appendText(message + "\n");
            }
        });
    }

    public void getFile(ObjectInputStream inputStream){
        try {
            FileMessage message = (FileMessage) inputStream.readObject();
            showMessage(message.getInformation());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getVoice(ObjectInputStream inputStream){
        try {
            byte[] voiceBytes = (byte[]) inputStream.readObject();
            saveVoice(voiceBytes);
            AudioPlayer player = new AudioPlayer(new File("ClientFiles/Voice.wav"));
            player.run();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveVoice(byte[] voiceBytes){
        File file = new File("ClientFiles/Voice.wav");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(voiceBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
