/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Moduls.Dto.Messages;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Message implements Serializable {

    private final String senderUsername;
    private final LocalDateTime dateTime;
    private String text;

    public Message(String senderUsername,String text){
        this.senderUsername = senderUsername;
        this.dateTime = LocalDateTime.now();
        this.text = text;
    }

    /**
     * Is used to set text of message
     * @param text : text of message
     */
    protected void setText(String text){
        this.text = text;
    }

    /**
     * get access to username of sender
     * @return : username of sender
     */
    protected String getSenderUsername(){
        return senderUsername;
    }

    /**
     * get access to time of message
     * @return : time of message
     */
    protected String getTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");//format of Localdate variable.
        return dateTime.format(formatter);
    }

    /**
     * get access to text of message
     * @return : text of message
     */
    public String getText(){
        return text;
    }

    /**
     * get access to all information of message
     * @return : all information of message
     */
    public String getInformation(){
        return getTime() + "    " + getSenderUsername() + " : " + text;
    }

}
