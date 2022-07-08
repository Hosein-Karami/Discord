package com.example.discordfx.Moduls.Dto.ServerMembers;

import java.io.Serializable;

public class Role implements Serializable {

    private final String name;
    private boolean makeChannel;
    private boolean deleteChannel;
    private boolean removeMemberFromServer;
    private boolean changeServerName;
    private boolean limitMemberToJoinChannel;
    private boolean changeChannelName;
    private boolean pinMessage;

    public Role(String name){
        this.name = name;
    }

    public void setMakeChannel(boolean makeChannel){
        this.makeChannel = makeChannel;
    }

    public void setDeleteChannel(boolean deleteChannel){
        this.deleteChannel = deleteChannel;
    }

    public void setRemoveMemberFromServer(boolean removeMemberFromServer){
        this.removeMemberFromServer = removeMemberFromServer;
    }

    public void setLimitMemberToJoinChannel(boolean limitMemberToJoinChannel){
        this.limitMemberToJoinChannel = limitMemberToJoinChannel;
    }

    public void setChangeServerName(boolean changeServerName){
        this.changeServerName = changeServerName;
    }

    public void setChangeChannelName(boolean changeChannelName){
        this.changeChannelName = changeChannelName;
    }

    public void setPinMessage(boolean pinMessage){
        this.pinMessage = pinMessage;
    }

    public boolean isMakeChannel() {
        return makeChannel;
    }

    public boolean isDeleteChannel() {
        return deleteChannel;
    }

    public boolean isRemoveMemberFromServer() {
        return removeMemberFromServer;
    }

    public boolean isChangeServerName() {
        return changeServerName;
    }

    public boolean isChangeChannelName(){
        return changeChannelName;
    }

    public boolean isPinMessage() {
        return pinMessage;
    }

    public boolean isLimitMemberToJoinChannel(){
        return limitMemberToJoinChannel;
    }

    public String getName(){
        return name;
    }

}
