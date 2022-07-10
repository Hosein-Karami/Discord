package com.example.discordfx.Server.Rooms.Chats;

import com.example.discordfx.Moduls.Dto.Messages.Message;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public abstract class GeneralChat {

    protected ArrayList<Socket> joinSockets = new ArrayList<>();
    protected ArrayList<String> memberUsernames = new ArrayList<>();
    protected final ArrayList<Message> messages = new ArrayList<>();

    public abstract void join(Socket joinedSocket);

    synchronized public <T> void sendMessage(T t){
        ObjectOutputStream outputStream;
        for(Socket x : joinSockets) {
            try {
                outputStream = new ObjectOutputStream(x.getOutputStream());
                outputStream.writeObject(t);
            } catch (IOException e) {
                joinSockets.remove(x);
            }
        }
    }

    synchronized public <T> void sendMessageToParticularSocket(T t,Socket targetSocket){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(targetSocket.getOutputStream());
            outputStream.writeObject(t);
        } catch (IOException e) {
            e.printStackTrace();
            joinSockets.remove(targetSocket);
        }
    }

    synchronized public void removeJoinSocket(Socket targetSocket){
        joinSockets.remove(targetSocket);
    }

    public void addMessage(Message newMessage){
        messages.add(newMessage);
    }

    protected void sendBeforeMessages(Socket socket){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            StringBuilder str = new StringBuilder();
            for(Message x : messages)
                str.append(x.getInformation()).append("\n");
            outputStream.writeObject(str.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
