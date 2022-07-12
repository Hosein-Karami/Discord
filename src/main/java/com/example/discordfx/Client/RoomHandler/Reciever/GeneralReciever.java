/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Client.RoomHandler.Reciever;

import com.example.discordfx.Client.RoomHandler.VoiceManagement.AudioPlayer;
import com.example.discordfx.Moduls.Dto.Messages.FileMessage;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.net.Socket;

public class GeneralReciever {

    private boolean mute;
    protected final Socket socket;
    protected InputStream in;
    private final TextArea textArea;

    public GeneralReciever(TextArea textArea, Socket socket){
        this.socket = socket;
        this.textArea = textArea;
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMute(boolean mute){
        this.mute = mute;
    }

    /**
     * Is used to show messages in textarea
     * @param message : new message from server
     */
    public void showMessage(String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.appendText(message + "\n");
            }
        });
        playNotificationSound();
    }

    /**
     * Is used to show client how to get file which saves in server
     * @param inputStream : Stream of server
     */
    public void getFile(ObjectInputStream inputStream){
        try {
            FileMessage message = (FileMessage) inputStream.readObject();
            showMessage(message.getInformation());
            playNotificationSound();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used when voice chat is coming from server
     * @param inputStream : Stream of server
     */
    public void getVoice(ObjectInputStream inputStream){
        try {
            byte[] voiceBytes = (byte[]) inputStream.readObject();
            saveVoice(voiceBytes);
            AudioPlayer player = new AudioPlayer(new File("ClientFiles/Voice.wav"));
            playNotificationSound();
            player.run();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to close socket when user want to left from chat
     */
    public void disconnect(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to save voice files and play them
     * @param voiceBytes : bytes of voice file
     */
    private void saveVoice(byte[] voiceBytes){
        File file = new File("ClientFiles/Voice.wav");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(voiceBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to play notification sound
     */
    protected void playNotificationSound() {
        if (!mute) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("ClientFiles/RecieveNotification.wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
