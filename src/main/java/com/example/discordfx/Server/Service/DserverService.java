package com.example.discordfx.Server.Service;

import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Management.AccountManagement;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class DserverService {

    private final Dserver dserver;
    private final Member member;
    private final Socket clientSocket;
    private InputStream in;
    private OutputStream out;

    public DserverService(Dserver dserver, Socket clientSocket, User client) {
        this.dserver = dserver;
        this.clientSocket = clientSocket;
        member = dserver.getParticularMember(client.getUsername());
        try {
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        int choose;
        try {
            while (true) {
                choose = in.read();
                if(choose == 1)
                    addMember();
                else if(choose == 3)
                    sendUserInfo();
                else
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addMember(){
        int choose;
        try {
            while (true) {
                choose = in.read();
                if(choose == 1)
                    checkExistenceMember();
                else if(choose == 3)
                    sendUserInfo();
                else
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkExistenceMember(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            String targetUsername = (String) inputStream.readObject();
            if(dserver.getParticularMember(targetUsername) == null)
                outputStream.writeObject("NO");
            else
                outputStream.writeObject("YES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendUserInfo(){
        AccountManagement accountManagement = new AccountManagement();
        accountManagement.sendProfileImage(clientSocket);
    }

}
