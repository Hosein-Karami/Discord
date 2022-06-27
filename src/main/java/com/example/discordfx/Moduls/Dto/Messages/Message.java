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

    protected String getSenderUsername(){
        return senderUsername;
    }

    protected String getTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");//format of Localdate variable.
        return dateTime.format(formatter);
    }

    protected void setText(String text){
        this.text = text;
    }

    public String getInformation(){
        return getTime() + "    " + getSenderUsername() + " : " + text;
    }

}
