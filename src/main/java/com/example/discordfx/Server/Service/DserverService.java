package com.example.discordfx.Server.Service;

import com.example.discordfx.Moduls.Dto.DiscordServer.Channel;
import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.DiscordServer.Invitation;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.ServerMembers.Role;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Management.AccountManagement;
import com.example.discordfx.Server.Management.ChannelChatManagement;
import com.example.discordfx.Server.Management.DserverManagement;
import java.io.*;
import java.net.Socket;

public class DserverService {

    private final Dserver dserver;
    private final Member member;
    private final Socket clientSocket;
    private final ChannelChatManagement management = new ChannelChatManagement();
    private InputStream in;
    private OutputStream out;

    public DserverService(Dserver dserver, Socket clientSocket, User client) {
        this.dserver = dserver;
        this.clientSocket = clientSocket;
        for(Channel x : dserver.getChannels())
            System.out.println(x.getName());
        member = dserver.getParticularMember(client.getId());
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
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(member);
            choose = in.read();
            if (choose == 1) {
                outputStream = new ObjectOutputStream(out);
                outputStream.writeObject(member.getUser());
                addMember();
            }
            else if(choose == 2){
                outputStream = new ObjectOutputStream(out);
                outputStream.writeObject(dserver);
                outputStream.writeObject(member.canKickMembers());
                outputStream.writeObject(member.isOwner());
                outputStream.writeObject(member.getUser());
                showMembers();
            }
            else if(choose == 3)
                changeName();
            else if(choose == 4)
                makeRole();
            else if(choose == 5)
                makeChannel();
            else if(choose == 6){
                outputStream = new ObjectOutputStream(out);
                outputStream.writeObject(member);
                outputStream.writeObject(dserver);
                manageChannels();
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
                else if(choose == 4)
                    searchFriend();
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
            Integer targetUsername = (Integer) inputStream.readObject();
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
            Integer targetId = (Integer) inputStream.readObject();
            AccountsService service = new AccountsService();
            User user = service.getParticularUser(targetId);
            String senderUsername = (String) inputStream.readObject();
            Invitation invitation = new Invitation(dserver.getId(),senderUsername);
            user.addInvitation(invitation);
            outputStream.writeObject("Invitation send successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendUserInfo(){
        AccountManagement accountManagement = new AccountManagement();
        accountManagement.sendUserInfo(clientSocket);
    }

    private void searchFriend(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            String targetUsername = (String) inputStream.readObject();
            AccountsService service = new AccountsService();
            User targetUser = service.getParticularUser(targetUsername);
            if(targetUser == null)
                outputStream.writeObject(-1);
            else {
                outputStream.writeObject(1);
                if(member.getUser().checkIsFriend(targetUser.getId()))
                    outputStream.writeObject(targetUser.getId());
                else
                    outputStream.writeObject(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMembers(){
        AccountManagement management = new AccountManagement();
        int choice;
        while (true){
            try {
                choice = in.read();
                if(choice == 1) {
                    management.sendUserInfo(clientSocket);
                    sendMemberRoles();
                }
                else if(choice == 2)
                    kickMember();
                else if(choice == 3)
                    addRoleToMember();
                else
                    break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void sendMemberRoles(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            Integer memberId = (Integer) inputStream.readObject();
            outputStream.writeObject(dserver.getParticularMember(memberId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRoleToMember(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            String roleName = (String) inputStream.readObject();
            Role targetRole = dserver.getParticularRole(roleName);
            if(targetRole != null){
                outputStream.writeObject("OK");
                Integer targetMemberId = (Integer) inputStream.readObject();
                Member targetMember = dserver.getParticularMember(targetMemberId);
                targetMember.addRole(targetRole);
                outputStream.writeObject("Role added successfully");
            }
            else
                outputStream.writeObject("Not exist");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void kickMember() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            Integer memberId = (Integer) inputStream.readObject();
            if (dserver.getSuperChatMaker().getUser().getId() != memberId) {
                dserver.kickMember(memberId);
                outputStream.writeObject("User kicked successfully");
            } else
                outputStream.writeObject("You can't kick server maker");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeName() {
        DserverManagement management = new DserverManagement();
        int choice;
        while (true) {
            try {
                choice = in.read();
                if(choice == 2)
                    break;
                ObjectInputStream inputStream = new ObjectInputStream(in);
                ObjectOutputStream outputStream = new ObjectOutputStream(out);
                String newName = (String) inputStream.readObject();
                if (management.getDserver(newName) == null) {
                    dserver.setName(newName);
                    outputStream.writeObject("Name changed successfully");
                } else
                    outputStream.writeObject("This name is used before");
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void makeRole(){
        int choice;
        while (true){
            try {
                choice = in.read();
                if(choice == 2)
                    break;
                ObjectInputStream inputStream = new ObjectInputStream(in);
                ObjectOutputStream outputStream = new ObjectOutputStream(out);
                String roleName = (String) inputStream.readObject();
                if(dserver.getParticularRole(roleName) == null){
                    outputStream.writeObject("OK");
                    Role newRole = (Role) inputStream.readObject();
                    dserver.addRole(newRole);
                    outputStream.writeObject("Role added successfully");
                }
                else
                    outputStream.writeObject("Not unique");
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void makeChannel(){
        int choose;
        try {
            while (true) {
                choose = in.read();
                if(choose == 2)
                    break;
                management.makeChannel(clientSocket, dserver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void manageChannels(){
        ChannelChatService service = new ChannelChatService(dserver,clientSocket);
        service.start();
    }

}
