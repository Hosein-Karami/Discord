package com.example.discordfx.Moduls.Dto.DiscordServer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.net.Socket;

public class MusicReceiver implements Runnable{

    private InputStream in;
    private Clip clip;
    private Socket socket;

    public MusicReceiver(int port){
        try {
            String ip = "127.0.0.1";
            socket = new Socket(ip,port);
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            while (true){
                byte[] musicBytes = (byte[]) inputStream.readObject();
                if(musicBytes != null)
                    playMusic(musicBytes);
            }
        } catch (Exception e) {

        }
    }

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

    public void stop(){
        if(clip != null)
            clip.stop();
    }

    public void resume(){
        clip.start();
    }

}