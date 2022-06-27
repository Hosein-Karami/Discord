package com.example.discordfx.Server.Rooms.Chats;

import com.example.discordfx.Moduls.Dto.Messages.Message;
import com.example.discordfx.Moduls.Dto.User.User;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public abstract class GeneralChat {

    protected ArrayList<Socket> joinSockets = new ArrayList<>();
    protected ArrayList<User> users = new ArrayList<>();
    private final ArrayList<Message> messages = new ArrayList<>();

    public abstract void join(Socket joinedSocket);

    synchronized public <T> void sendMessage(T t,Socket sender){
        ObjectOutputStream outputStream;
        for(Socket x : joinSockets) {
            if (x != sender) {
                try {
                    outputStream = new ObjectOutputStream(x.getOutputStream());
                    outputStream.writeObject(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    synchronized public <T> void sendMessageToParticularSocket(T t,Socket targetSocket){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(targetSocket.getOutputStream());
            outputStream.writeObject(t);
        } catch (IOException e) {
            e.printStackTrace();
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
            outputStream.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Socket> getJoinSockets(){
        return joinSockets;
    }

}
