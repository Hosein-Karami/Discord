package com.example.discordfx.Log.ServicesLog;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountsLog {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");//format of Localdate variable.

    public void changePictureSuccessfully(int userId){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] ClientService\nUser with id="+userId+
                    " changed profile picture successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePictureError(int userId){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] ClientService\nError happening when User with id="+userId+
                    " want to change picture of his/her profile.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
