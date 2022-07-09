package com.example.discordfx.Moduls.Dto.DiscordServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MusicSender implements Runnable{

    private final int port;
    private final ArrayList<Socket> sockets = new ArrayList<>();

    public MusicSender(int port){
        this.port = port;
    }

    public int getPort(){
        return port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
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
                if(x.isConnected()) {
                    outputStream = new ObjectOutputStream(x.getOutputStream());
                    outputStream.writeObject(musicBytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
