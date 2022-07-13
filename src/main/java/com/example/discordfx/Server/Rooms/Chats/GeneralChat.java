/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Rooms.Chats;

import com.example.discordfx.Moduls.Dto.Messages.Message;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public abstract class GeneralChat {

    protected ArrayList<Socket> joinSockets = new ArrayList<>();
    protected ArrayList<String> memberUsernames = new ArrayList<>();
    protected final ArrayList<Message> messages = new ArrayList<>();
    protected int joinedNumbers;

    public abstract void join(Socket joinedSocket);

    /**
     * Is used to send message to all users except sender
     * @param t : message
     * @param clientSocket : sender socket
     * @param <T> : type of message
     */
    synchronized public <T> void sendMessage(T t,Socket clientSocket){
        ObjectOutputStream outputStream;
        for(Socket x : joinSockets) {
            if(x == clientSocket)
                continue;
            if(x.isClosed())
                joinSockets.remove(x);
            else {
                try {
                    outputStream = new ObjectOutputStream(x.getOutputStream());
                    outputStream.writeObject(t);
                } catch (IOException e) {
                    joinSockets.remove(x);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Is used to send message to a particular user
     * @param t : message
     * @param targetSocket : user's socket
     * @param <T> : type of message
     */
    synchronized public <T> void sendMessageToParticularSocket(T t,Socket targetSocket){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(targetSocket.getOutputStream());
            outputStream.writeObject(t);
        } catch (IOException e) {
            e.printStackTrace();
            joinSockets.remove(targetSocket);
        }
    }

    /**
     * Is used to add a message to chat's messages
     * @param newMessage .
     */
    public void addMessage(Message newMessage){
        messages.add(newMessage);

    }

    /**
     * Is used to remove a socket
     * @param socket : target socket
     */
    public void removeSocket(Socket socket){
        joinSockets.remove(socket);
        joinedNumbers--;
    }

    /**
     * Is used to send before messages of chat for new joined socket
     * @param socket : target socket
     */
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

    /**
     * Is used to get a particular message of chat with its index in messages arraylist
     * @param messageIndex : index of message
     * @return : target message
     */
    public Message getParticularMessage(int messageIndex){
        return messages.get(messageIndex);
    }

    /**
     * Is used to get size of chat's messages
     * @return : size of chat's messages
     */
    public int getMessagesSize(){
        return messages.size();
    }

}
