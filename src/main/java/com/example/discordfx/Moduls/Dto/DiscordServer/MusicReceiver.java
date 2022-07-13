package com.example.discordfx.Moduls.Dto.DiscordServer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.net.Socket;

public class MusicReceiver implements Runnable{

    private InputStream in;
    private Clip clip;
    private boolean end;

    public MusicReceiver(int port){
        try {
            String ip = "127.0.0.1";
            Socket socket = new Socket(ip, port);
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to get music bytes from server
     */
    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            while (true){
                byte[] musicBytes = (byte[]) inputStream.readObject();
                if(musicBytes != null && (!end))
                    playMusic(musicBytes);
            }
        } catch (Exception e) {

        }
    }

    /**
     * Is used to play music
     * @param musicBytes : bytes of music
     */
    public void playMusic(byte[] musicBytes){
        try {
            File file = new File("ClientFiles/DiscordServerMusic.wav");
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(musicBytes);
            outputStream.flush();
            outputStream.close();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to stop player
     */
    public void stop(){
        if(clip != null)
            clip.stop();
    }

    /**
     * Is used to resume player
     */
    public void resume(){
        if(clip != null)
            clip.start();
    }

    /**
     * Is used to end player
     */
    public void end(){
        end = true;
    }

}
