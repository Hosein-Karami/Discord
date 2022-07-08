package com.example.discordfx.Moduls.Dto.DiscordServer;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Start.Server;

import java.io.Serializable;
import java.util.ArrayList;

public class Dserver implements Serializable {

    private final int id;
    private String name;
    private int counterOfJoinedClient;
    private final int folderNumber;
    private Member superChatMaker;
    private final ArrayList<Member> members = new ArrayList<>();
    private final ArrayList<Channel> channels = new ArrayList<>();

    public Dserver(Member superChatMaker,int id){
        this.superChatMaker = superChatMaker;
        this.folderNumber = id;
        this.id = id;
        Server.lastUsedPort++;
        members.add(superChatMaker);
    }

    public void setName(String name){
        this.name = name;
        for(Member x : members)
            x.getUser().updateInformation();
    }

    public void increaseCounterOfJoinedClient(){
        counterOfJoinedClient++;
    }

    public void decreaseCounterOfJoinedClient(){
        counterOfJoinedClient--;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Channel> getChannels(){
        return channels;
    }

    public ArrayList<Member> getMembers(){
        return members;
    }

    public int getCounterOfJoinedClient(){
        return counterOfJoinedClient;
    }

    public int getId(){
        return id;
    }

    public Member getSuperChatMaker() {
        return superChatMaker;
    }

    public Member getParticularMember(String username){
        for(Member x : members){
            if(x.getUser().getUsername().equals(username)) {
                System.out.println("SALAM BACHEHA");
                return x;
            }
        }
        return null;
    }

    public Channel getParticularChannel(String channelName){
        for(Channel x : channels){
            if(x.getName().equals(channelName))
                return x;
        }
        return null;
    }

    public void addMember(Member newMember){
        for(Member x : members){
            if(x.equals(newMember))
                return;
        }
        members.add(newMember);
        newMember.getUser().addServerChat(id);
        newMember.getUser().addNotification(new Notification("You are added to server chat with name : "+name));
    }

    public void addChannel(Channel channel){
        channels.add(channel);
    }

    public void removeMember(Member targetMember){
        members.remove(targetMember);
        targetMember.getUser().removeServerChat(name);
    }

    public void removeChannel(Channel targetChannel){
        channels.remove(targetChannel);
    }

    public void deleteServerChat(){
        Notification notification = new Notification(this.name + " deleted by its owner.");
        User user;
        for(Member x : members) {
            user = x.getUser();
            user.removeServerChat(name);
            user.addNotification(notification);
        }
        members.clear();
        channels.clear();
        name = null;
        superChatMaker = null;
    }

}
