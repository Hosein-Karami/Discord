/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Management;

import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.DiscordServer.Invitation;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.ServerMembers.Role;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Start.Server;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class DserverManagement {

    /**
     * Is used to make a server chat
     * @param socket .
     * @param serverMaker : maker of server chat
     */
    public void makeServerChat(Socket socket, User serverMaker){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            String serverName = (String) inputStream.readObject();
            //Check name is unique??
            if(getDserver(serverName) == null) {
                outputStream.writeObject("OK");
                Role ownerRole = getOwnerRole();
                Member owner = new Member(serverMaker.getId());
                owner.addRole(ownerRole);
                Dserver dserver = new Dserver(owner, Server.discordServers.size());
                dserver.setName(serverName);
                Server.discordServers.add(dserver);
                serverMaker.addServerChat(dserver.getId());
                File serverFolder = new File("ServerFiles/DiscordServers/" + dserver.getId());
                serverFolder.mkdir();
                byte[] imageBytes = (byte[]) inputStream.readObject();
                if (imageBytes != null) {
                    FileOutputStream fileOutputStream = new FileOutputStream("ServerFiles/DiscordServers/" + dserver.getId() + "/Profile.jpg");
                    fileOutputStream.write(imageBytes);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                else
                    setDefaultProfilePicture("ServerFiles/DiscordServers/" + dserver.getId()+"/Profile.jpg");
                outputStream.writeObject("Server made successfully");
            }
            else
                outputStream.writeObject("Not unique");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to manage invitation which sent to user
     * @param user : user
     * @param socket .
     */
    public void invitationHandle(User user,Socket socket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            Invitation targetInvitation = (Invitation) inputStream.readObject();
            Dserver targetServer = Server.discordServers.get(targetInvitation.getServerId());
            String reaction = (String) inputStream.readObject();
            if(reaction.equals("Accept")){
                targetServer.addMember(new Member(user.getId()));
                user.addServerChat(targetInvitation.getServerId());
                outputStream.writeObject("You joined to server successfully");
            }
            else
                outputStream.writeObject("Invitation rejected successfully");
            user.removeInvitation(targetInvitation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to send information of server chat
     * @param socket .
     */
    public void sendServerChatInfo(Socket socket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            Integer serverId = (Integer) inputStream.readObject();
            Dserver targetServer = Server.discordServers.get(serverId);
            File serverProfile = new File("ServerFiles/DiscordServers/"+targetServer.getId()+"/Profile.jpg");
            outputStream.writeObject(targetServer.getName());
            outputStream.writeObject(Files.readAllBytes(serverProfile.toPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to set picture of server chat
     * @param path : image path
     */
    private void setDefaultProfilePicture(String path){
        File defaultPicture = new File("ServerFiles/Pictures/defaultServer.jpg");
        try (FileOutputStream fileOutputStream = new FileOutputStream(path)){
            fileOutputStream.write(Files.readAllBytes(defaultPicture.toPath()));
            fileOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used to get a particular server chat with its name
     * @param serverName : name of server chat
     * @return : target server chat
     */
    public Dserver getDserver(String serverName){
        for(Dserver x : Server.discordServers){
            if(x.getName().equals(serverName))
                return x;
        }
        return null;
    }

    /**
     * Is used to make and get owner role for server chat maker
     * @return : owner role
     */
    private Role getOwnerRole(){
        Role ownerRole = new Role("Owner");
        ownerRole.setDeleteChannel(true);
        ownerRole.setChangeServerName(true);
        ownerRole.setMakeChannel(true);
        ownerRole.setPinMessage(true);
        ownerRole.setLimitMemberToJoinChannel(true);
        ownerRole.setRemoveMemberFromServer(true);
        return ownerRole;
    }

}
