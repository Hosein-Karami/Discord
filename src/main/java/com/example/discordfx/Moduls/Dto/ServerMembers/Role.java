/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Moduls.Dto.ServerMembers;

import java.io.Serializable;

public class Role implements Serializable {

    private final String name;
    private boolean makeChannel;
    private boolean deleteChannel;
    private boolean removeMemberFromServer;
    private boolean changeServerName;
    private boolean limitMemberToJoinChannel;
    private boolean pinMessage;

    public Role(String name){
        this.name = name;
    }

    /**
     * determine can make a channel or not
     * @param makeChannel : ability value
     */
    public void setMakeChannel(boolean makeChannel){
        this.makeChannel = makeChannel;
    }

    /**
     * determine can delete a channel or not
     * @param deleteChannel : ability value
     */
    public void setDeleteChannel(boolean deleteChannel){
        this.deleteChannel = deleteChannel;
    }

    /**
     * determine can remove members from server or not
     * @param removeMemberFromServer : ability value
     */
    public void setRemoveMemberFromServer(boolean removeMemberFromServer){
        this.removeMemberFromServer = removeMemberFromServer;
    }

    /**
     * determine can limit members to access to channels or not
     * @param limitMemberToJoinChannel : ability value
     */
    public void setLimitMemberToJoinChannel(boolean limitMemberToJoinChannel){
        this.limitMemberToJoinChannel = limitMemberToJoinChannel;
    }

    /**
     * determine can change name of server or not
     * @param changeServerName : ability value
     */
    public void setChangeServerName(boolean changeServerName){
        this.changeServerName = changeServerName;
    }

    /**
     * determine can pin messages in channels or not
     * @param pinMessage : ability value
     */
    public void setPinMessage(boolean pinMessage){
        this.pinMessage = pinMessage;
    }

    /**
     * show member can make a channel or not
     * @return : ability value
     */
    public boolean isMakeChannel() {
        return makeChannel;
    }

    /**
     * show member can delete a channel or not
     * @return : ability value
     */
    public boolean isDeleteChannel() {
        return deleteChannel;
    }

    /**
     * show member can remove a member from server or not
     * @return : ability value
     */
    public boolean isRemoveMemberFromServer() {
        return removeMemberFromServer;
    }

    /**
     * show member can change name of server or not
     * @return : ability value
     */
    public boolean isChangeServerName() {
        return changeServerName;
    }

    /**
     * show member can pin messages in channels or not
     * @return : ability value
     */
    public boolean isPinMessage() {
        return pinMessage;
    }

    /**
     * show member can limit members to access to channels or not
     * @return : ability value
     */
    public boolean isLimitMemberToJoinChannel(){
        return limitMemberToJoinChannel;
    }

    /**
     * access to name of role
     * @return : name of role
     */
    public String getName(){
        return name;
    }

}
