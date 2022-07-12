/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */


package com.example.discordfx.Client.RoomHandler;

import com.example.discordfx.Client.RoomHandler.Sender.GeneralSender;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileReaderService implements Runnable{

    private final GeneralSender sender;
    private final File targetFile;

    public FileReaderService(File targetFile, GeneralSender sender){
        this.sender = sender;
        this.targetFile = targetFile;
    }

    /**
     * Is used to read a fil in thread
     */
    @Override
    public void run() {
        try {
            byte[] fileBytes = Files.readAllBytes(targetFile.toPath());
            sender.sendFile(fileBytes,targetFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
