package com.example.discordfx.Log;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDaoLog {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");//format of Localdate variable.

    public void insertSuccess(String username) {
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] UserDao\nan user added successfully with username=" + username + ".\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertError() {
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] UserDao\nError happening when we want to add new user.\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logInSuccessfully(String username){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] UserDao\nUser with username="+username+ " log in successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logInError(String username){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] UserDao\nError happening when user with username="+username+" want to log in.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getError(int id){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] UserDao\nError happening when we want to fetch user with id="+id+" from database.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getError(String username){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] UserDao\nError happening when we want to fetch "+username+" from database.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePasswordSuccessfully(String username){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] UserDao\nUser with username="+username+ " changed his/her password successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePasswordError(String username){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] UserDao\nError happening when user with username="+username+" want to change his/her password.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
