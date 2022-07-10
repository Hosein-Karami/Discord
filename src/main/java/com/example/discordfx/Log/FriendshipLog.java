package com.example.discordfx.Log;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FriendshipLog {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");//format of Localdate variable.

    public void friendRequestSuccessfully(String senderUsername,String receiverUsername){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] FriendShipServices\n"+senderUsername+ " send friendship request to "+receiverUsername+" successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void friendRequestError(String senderUsername,String receiverUsername){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] FriendShipServices\nError happening when "+senderUsername+ " want to send friendship request to "+receiverUsername+".\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void blockSuccessfully(String blockingUsername,String blockedUsername){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] FriendShipServices\n"+blockingUsername+
                    " blocked "+blockedUsername+".\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void blockError(String blockingUsername,String blockedUsername){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] FriendShipServices\nError happening when ="+blockingUsername+
                    " want to block "+blockedUsername+".\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFriendSuccessfully(String firstUsername,String secondUsername){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] FriendShipServices\n"+firstUsername+
                    " connect friendship with "+secondUsername+".\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
