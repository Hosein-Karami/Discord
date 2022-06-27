package com.example.discordfx.Moduls.Dto.Messages;

public class FileMessage extends Message{

    public FileMessage(String senderUsername,String fileName) {
        super(senderUsername,null);
        String text = getSenderUsername() + " send a file,You can get this file via go to menu and Download file section and enter "+fileName;
        setText(text);
    }

}
