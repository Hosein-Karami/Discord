/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Rooms.Chats;

import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.Messages.Message;
import com.example.discordfx.Moduls.Dto.Messages.TextMessage;
import com.example.discordfx.Server.Rooms.ClientInterfce.ChannelChatInterface;
import com.example.discordfx.Server.Service.AccountsService;
import com.example.discordfx.Start;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashSet;

public class ChannelChat extends GeneralChat{

    private final HashSet<Integer> usersId = new HashSet<>();
    private final Dserver dserver;
    private Message pinnedMessage = null;

    public ChannelChat(Dserver dserver) {
        this.dserver = dserver;
    }

    @Override
    public void join(Socket joinedSocket) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(joinedSocket.getInputStream());
            Integer userId =  (Integer) inputStream.readObject();
            manageJoin(userId,joinedSocket);
            joinSockets.add(joinedSocket);
            sendBeforeMessages(joinedSocket);
            ChannelChatInterface clientInterface = new ChannelChatInterface(this, joinedSocket);
            Start.executorService.execute(clientInterface);
            joinedNumbers++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to get discord server which this channel is in it
     * @return : dserver
     */
    public Dserver getDserver(){
        return dserver;
    }

    /**
     * Is used to get pinned message
     * @return : pinned message
     */
    public Message getPinnedMessage(){
        return pinnedMessage;
    }

    /**
     * Is used to pin a message
     * @param messageNumber : target message's number
     * @return : validity of task
     */
    public boolean pinMessage(int messageNumber){
        if(messageNumber <= messages.size()){
            pinnedMessage = messages.get(messageNumber - 1);
            System.out.println(pinnedMessage.getInformation());
            return true;
        }
        else
            return false;
    }

    /**
     * Is used to manage joined members
     * @param id : joined member's id
     * @param joinedSocket .
     */
    private void manageJoin(Integer id,Socket joinedSocket){
        if(!(usersId.contains(id))){
            AccountsService service = new AccountsService();
            String username = (service.getParticularUser(id)).getUsername();
            TextMessage welcomeMessage = new TextMessage("SERVER","Everyone welcome " + username);
            sendMessage(welcomeMessage.getInformation(),joinedSocket);
            addMessage(welcomeMessage);
            usersId.add(id);
        }
    }

}
