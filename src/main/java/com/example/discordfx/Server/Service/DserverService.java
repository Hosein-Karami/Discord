package com.example.discordfx.Server.Service;

import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.DiscordServer.Invitation;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Management.AccountManagement;
import java.io.*;
import java.net.Socket;

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
        System.out.println(client.getUsername());
        System.out.println(member.getUser().getUsername());
        System.out.println(member.getUser().getPassword());
        System.out.println("1");
        for(String x : member.getUser().getFriends())
            System.out.println(x);
        System.out.println("2");
        //System.out.println(member.getUser().getFriends().toString());
        try {
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        int choose;
        try {
            choose = in.read();
            if (choose == 1) {
                ObjectOutputStream outputStream = new ObjectOutputStream(out);
                outputStream.writeObject(member.getUser());
                addMember();
            }
        } catch (Exception e) {
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
                else if(choose == 2)
                    invite();
                else if(choose == 3)
                    sendUserInfo();
                else
                    break;
            }
        }catch (Exception e){
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

    private void invite(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            String targetUsername = (String) inputStream.readObject();
            AccountsService service = new AccountsService();
            User user = service.getParticularUser(targetUsername);
            String senderUsername = (String) inputStream.readObject();
            System.out.println(user.getPassword() + "  " + user.getUsername() + "  " + user.getEmail());
            Invitation invitation = new Invitation(dserver.getId(),senderUsername);
            System.out.println(invitation.getInvitationText());
            user.addInvitation(invitation);
            outputStream.writeObject("Invitation send successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendUserInfo(){
        AccountManagement accountManagement = new AccountManagement();
        accountManagement.sendProfileImage(clientSocket);
    }

    private void sendServerInfo(){

    }

}