package com.example.discordfx.Log;

import java.io.FileWriter;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLog {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");//format of Localdate variable.

    public void openPortError(int port){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] Server\nError happening when we want to open port "+port+ " on server.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clientConnectSuccessfully(InetAddress address){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] Server\nClient whose address : "+address+" successfully connect to server.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openStreamError(InetAddress address){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] ClientManagement\nError happening when we want to open stream to transfer data with client whose address is :  "+address+ " on server.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logOut(String username){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] Server\n"+ username + " logout successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
