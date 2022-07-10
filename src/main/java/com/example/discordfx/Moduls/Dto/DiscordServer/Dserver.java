/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Moduls.Dto.DiscordServer;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.ServerMembers.Role;
import java.io.Serializable;
import java.util.ArrayList;

public class Dserver implements Serializable {

    private String name;
    private final int id;
    private final Member superChatMaker;
    private final ArrayList<Member> members = new ArrayList<>();
    private final ArrayList<Role> roles = new ArrayList<>();
    private final ArrayList<Channel> channels = new ArrayList<>();

    public Dserver(Member superChatMaker,int id){
        this.superChatMaker = superChatMaker;
        this.id = id;
        members.add(superChatMaker);
    }

    /**
     * Is used to set name of server
     * @param name : name of server
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Is used to get access to name of server
     * @return : name of server
     */
    public String getName(){
        return name;
    }

    /**
     * Is used to get access to channels of server
     * @return : channels of server
     */
    public ArrayList<Channel> getChannels(){
        return channels;
    }

    /**
     * Is used to get access to members of server
     * @return : members of server
     */
    public ArrayList<Member> getMembers(){
        return members;
    }

    /**
     * Is used to get access to Id of server
     * @return : Id of server
     */
    public int getId(){
        return id;
    }

    /**
     * Is used to get access to maker of server
     * @return : refrence of server maker
     */
    public Member getSuperChatMaker() {
        return superChatMaker;
    }

    /**
     * Is used to get a particular member with his/her id
     * @param targetId : id of member
     * @return : particular member
     */
    public Member getParticularMember(int targetId){
        for(Member x : members){
            if(x.getUser().getId() == targetId)
                return x;
        }
        return null;
    }

    /**
     * Is used to get a particular channel with its name
     * @param channelName : name of channel
     * @return : particular channel
     */
    public Channel getParticularChannel(String channelName){
        for(Channel x : channels){
            if(x.getName().equals(channelName))
                return x;
        }
        return null;
    }

    /**
     * Is used to get a particular role with its name
     * @param roleName : name of role
     * @return : particular role
     */
    public Role getParticularRole(String roleName){
        for(Role x : roles){
            if(x.getName().equals(roleName))
                return x;
        }
        return null;
    }

    /**
     * Is used to add a channel to server
     * @param newChannel : refrence of new channel
     */
    public void addChannel(Channel newChannel){
        channels.add(newChannel);
    }

    /**
     * Is used to add a member to server
     * @param newMember : refrence of new member
     */
    public void addMember(Member newMember){
        for(Member x : members){
            if(x.equals(newMember))
                return;
        }
        members.add(newMember);
        newMember.getUser().addServerChat(id);
        newMember.getUser().addNotification(new Notification("You are added to server chat with name : "+name));
    }

    /**
     * Is used to add a role to server
     * @param newRole : refrence of new role
     */
    public void addRole(Role newRole){
        roles.add(newRole);
    }

    /**
     * Is used to kick a member from server
     * @param userId : target member's id
     */
    public void kickMember(Integer userId){
        int targetIndex = 0;
        for(Member x : members){
            if(x.getUser().getId() == userId)
                break;
            targetIndex++;
        }
        Notification notification = new Notification("You are kicked from serer with name : " + name);
        members.get(targetIndex).getUser().addNotification(notification);
        members.remove(targetIndex);
    }

    /**
     * Is used to delete a channel from server
     * @param channelName : target channel's name
     */
    public void removeChannel(String channelName){
        int targetIndex = 0;
        for(Channel x : channels){
            if(x.getName().equals(channelName))
                break;
            targetIndex++;
        }
        channels.remove(targetIndex);
    }

}
