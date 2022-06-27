package com.example.discordfx.Log.ServicesLog;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FriendshipLog {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");//format of Localdate variable.

    public void friendRequestSuccessfully(int senderId,int receiverId){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] FriendShipServices\nUser with id="+senderId+ " send friendship request to user with id="+receiverId+" successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void friendRequestError(int senderId,int receiverId){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] FriendShipServices\nError happening when user with id="+senderId+ " want to send friendship request to user with id="+receiverId+".\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void blockSuccessfully(Integer blockingUserId,Integer blockedUserId){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] FriendShipServices\nUser with id="+blockingUserId+
                    " blocked user with id="+blockedUserId+".\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void blockError(Integer blockingUserId,Integer blockedUserId){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] FriendShipServices\nError happening when user with id="+blockingUserId+
                    " want to block user with id="+blockedUserId+".\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFriendSuccessfully(int firstUserId,int secondUserId){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] FriendShipServices\nUser with id="+firstUserId+
                    " connect friendship with user with id="+secondUserId+".\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
