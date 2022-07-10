/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Client.RoomHandler.Reciever;

import com.example.discordfx.Moduls.Dto.Messages.Message;
import javafx.scene.control.TextArea;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ChannelChatReciever extends GeneralReciever implements Runnable{

    public ChannelChatReciever(TextArea textArea, Socket socket) {
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
                if(messageType.equals("#REACT"))
                    System.out.println("SALAM");
                inputStream = new ObjectInputStream(in);
                if (!(messageType.equals("#LEFT")) && (!(messageType.equals("#GETPIN"))) && (!(messageType.equals("#PIN"))) &&
                        (!(messageType.equals("#REACT"))) && (!(messageType.equals("#TAGMESSAGE")))) {
                    showMessage((String) inputStream.readObject());
                    inputStream = new ObjectInputStream(in);
                }
                switch (messageType) {
                    case "#TEXT","#REACT","#TAGMESSAGE" -> showMessage((String) inputStream.readObject());
                    case "#FILE" -> getFile(inputStream);
                    case "#VOICE" -> getVoice(inputStream);
                    case "#PIN" -> pin(inputStream);
                    case "#GETPIN" -> getPinnedMessage(inputStream);
                    case "#LEFT" -> {
                        disconnect();
                        break label;
                    }
                }
            }

        }catch (Exception e) {
        }
    }

    /**
     * Is used when client pin a message and we should show results
     * @param inputStream : inputStream of server
     */
    private void pin(ObjectInputStream inputStream){
        try {
            String status = (String) inputStream.readObject();
            if(status.equals("OK"))
                showMessage("Message pinned successfully");
            else
                showMessage("Invalid message number");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used when user want to see pinned message in a channel chat
     * @param inputStream : inputStream of server
     */
    private void getPinnedMessage(ObjectInputStream inputStream){
        try {
            Message pinnedMessage = (Message) inputStream.readObject();
            if(pinnedMessage == null)
                showMessage("No message pinned in this channel");
            else
                showMessage("Pinned message : "+pinnedMessage.getInformation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
