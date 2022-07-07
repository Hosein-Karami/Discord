package com.example.discordfx.Client.RoomHandler.Reciever;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class PrivateChatReciever extends GeneralReciever implements Runnable{

    private InputStream in;

    public PrivateChatReciever(Socket socket, TextArea textArea) {
        super(textArea);
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("STARTTTTTTT");
            ObjectInputStream inputStream = new ObjectInputStream(in);
            String beforeMessages = (String) inputStream.readObject();
            System.out.println(beforeMessages);
            showMessage(beforeMessages);
            String messageType;
            label:
            while (true) {
                inputStream = new ObjectInputStream(in);
                messageType = (String) inputStream.readObject();
                inputStream = new ObjectInputStream(in);
                if (!(messageType.equals("#LEFT"))) {
                    showMessage((String) inputStream.readObject());
                    inputStream = new ObjectInputStream(in);
                }
                switch (messageType) {
                    case "#TEXT" -> showMessage((String) inputStream.readObject());
                    case "#FILE" -> getFile(inputStream);
                    case "#VOICE" -> getVoice(inputStream);
                    case "#LEFT" -> {
                        break label;
                    }
                }
            }
        }catch (Exception e) {
                e.printStackTrace();
        }
    }

}
