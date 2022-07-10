/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Client.RoomHandler.Reciever;

import javafx.scene.control.TextArea;
import java.io.ObjectInputStream;
import java.net.Socket;

public class PrivateChatReciever extends GeneralReciever implements Runnable{

    public PrivateChatReciever(Socket socket, TextArea textArea) {
        super(textArea,socket);
    }

    /**
     * start reader thread for recieve messages from server and show them
     */
    @Override
    public void run() {
        try {
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
                        disconnect();
                        break label;
                    }
                }
            }
        }catch (Exception e) {
                e.printStackTrace();
        }
    }

}
