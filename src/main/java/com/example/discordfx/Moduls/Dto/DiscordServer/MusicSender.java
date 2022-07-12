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

    /**
     * Is used to get access to port of music sender's port
     * @return : port of music sender
     */
    public int getPort(){
        return port;
    }

    /**
     * Is used to recieve music bytes from a member and send to all joined members
     */
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

    /**
     * Is used to recieve music bytes from a member and send to all joined members
     */
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

    /**
     * Is used to stop sender
     */
    public void stop(){
        sockets.clear();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
