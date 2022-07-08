package com.example.discordfx.Server.Management;

import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.ServerMembers.Role;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Start.Server;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class DserverManagement {

    public void makeServerChatImage(Socket socket, User serverMaker){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            String serverName = (String) inputStream.readObject();
            //Check name is unique??
            if(checkName(serverName)) {
                outputStream.writeObject("OK");
                Member owner = new Member(serverMaker);
                owner.addRole(getOwnerRole());
                Dserver dserver = new Dserver(owner, Server.discordServers.size());
                dserver.setName(serverName);
                Server.discordServers.add(dserver);
                serverMaker.addServerChat(serverName);
                File serverFolder = new File("Files/DiscordServers/" + dserver.getFolderNumber());
                serverFolder.mkdir();
                byte[] imageBytes = (byte[]) inputStream.readObject();
                if (imageBytes != null) {
                    FileOutputStream fileOutputStream = new FileOutputStream("Files/DiscordServers/" + dserver.getFolderNumber() + "/Profile.jpg");
                    fileOutputStream.write(imageBytes);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                else
                    setDefaultProfilePicture("Files/DiscordServers/" + dserver.getFolderNumber()+"/Profile.jpg");
                outputStream.writeObject("Server made successfully");
            }
            else
                outputStream.writeObject("Not unique");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setServerPicture(Socket socket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            byte[] imageBytes = inputStream.readAllBytes();
            String serverName = (String) inputStream.readObject();
            int serverFolderNumber = getParticularServer(serverName).getFolderNumber();
            File serverProfile = new File("Files/DiscordServers/"+serverFolderNumber+"/Profile.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(serverProfile);
            fileOutputStream.write(imageBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendServerChatInfo(Socket socket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            String serverName = (String) inputStream.readObject();
            Dserver targetServer = getParticularServer(serverName);
            File serverProfile = new File("Files/DiscordServers/"+targetServer.getFolderNumber()+"/Profile.jpg");
            outputStream.writeObject(Files.readAllBytes(serverProfile.toPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Dserver getParticularServer(String serverName){
        for(Dserver x : Server.discordServers){
            if(x.getName().equals(serverName))
                return x;
        }
        return null;
    }

    private void setDefaultProfilePicture(String path){
        File defaultPicture = new File("icon.png");
        try (FileOutputStream fileOutputStream = new FileOutputStream(path)){
            fileOutputStream.write(Files.readAllBytes(defaultPicture.toPath()));
            fileOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkName(String serverName){
        for(Dserver x : Server.discordServers){
            if(x.getName().equals(serverName))
                return false;
        }
        return true;
    }

    private Role getOwnerRole(){
        Role ownerRole = new Role("Owner");
        ownerRole.setDeleteChannel(true);
        ownerRole.setChangeServerName(true);
        ownerRole.setMakeChannel(true);
        ownerRole.setPinMessage(true);
        ownerRole.setLimitMemberToJoinChannel(true);
        ownerRole.setRemoveMemberFromServer(true);
        ownerRole.setChangeChannelName(true);
        return ownerRole;
    }

}
