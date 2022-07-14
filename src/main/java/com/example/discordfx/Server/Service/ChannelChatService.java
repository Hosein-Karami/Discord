/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Service;

import com.example.discordfx.Moduls.Dto.DiscordServer.Channel;
import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.User.User;
import java.io.*;
import java.net.Socket;

public class ChannelChatService {

    private final AccountsService service = new AccountsService();
    private final Dserver dserver;
    private InputStream in;
    private OutputStream out;

    public ChannelChatService(Dserver dserver,Socket socket){
        this.dserver = dserver;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        int choice;
        try{
            while (true){
                choice = in.read();
                if(choice == 1) {
                    joinChannel();
                    break;
                }
                else if(choice == 2)
                    deleteChannel();
                else if(choice == 3)
                    addBannedMember();
                else
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used to join user to a particular channel
     */
    private void joinChannel(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            Integer channelPort = (Integer) inputStream.readObject();
            Member member = (Member) inputStream.readObject();
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(channelPort);
            outputStream.writeObject(member);
            outputStream.writeObject(member.getUser().getUsername());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to delete a channel
     */
    private void deleteChannel(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            String channelName = (String) inputStream.readObject();
            Channel targetChannel = dserver.getParticularChannel(channelName);
            if(targetChannel.getJoinedClientNumber() == 0){
                dserver.removeChannel(channelName);
                targetChannel.deleteChannel();
                outputStream.writeObject("Channel deleted successfully");
            }
            else
                outputStream.writeObject("You can't delete channel when some member are in channel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to add banned members to channel
     */
    private void addBannedMember(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            String targetChannelName = (String) inputStream.readObject();
            String targetUsername = (String) inputStream.readObject();
            User user = service.getParticularUser(targetUsername);
            if(user == null)
                outputStream.writeObject("There is no member with this username in server");
            else {
                int targetId = user.getId();
                Member targetMember = dserver.getParticularMember(targetId);
                if(targetMember == null)
                    outputStream.writeObject("There is no member with this username in server");
                else {
                    if (dserver.getSuperChatMaker().getUser().getId() == targetId)
                        outputStream.writeObject("You can't ban owner");
                    else {
                        dserver.getParticularChannel(targetChannelName).addBannedId(targetId);
                        outputStream.writeObject("User banned successfully");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
