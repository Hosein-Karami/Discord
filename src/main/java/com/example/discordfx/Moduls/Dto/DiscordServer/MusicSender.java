package com.example.discordfx.Moduls.Dto.DiscordServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MusicSender implements Runnable, Serializable {

    private final int port;
    private final ArrayList<Socket> sockets = new ArrayList<>();
    private ServerSocket serverSocket;

    public MusicSender(int port){
        this.port = port;
    }

    public int getPort(){
        return port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();
                sockets.add(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public void sendMusic(byte[] musicBytes){
        ObjectOutputStream outputStream;
        for(Socket x : sockets){
            try {
                outputStream = new ObjectOutputStream(x.getOutputStream());
                outputStream.writeObject(musicBytes);
            }catch (Exception e){
                sockets.remove(x);
            }
        }
    }

    public void stop(){
        sockets.clear();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
