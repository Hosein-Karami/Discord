/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Management;

import com.example.discordfx.Lateral.FileCopier;
import com.example.discordfx.Log.ServerLog;
import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Service.AccountsService;
import com.example.discordfx.Server.Start.Main;
import com.example.discordfx.Server.Start.Server;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class AccountManagement {

    ServerLog log = new ServerLog();

    /**
     * Is used when user want to register
     * @param clientSocket .
     */
    void signUp(Socket clientSocket) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            String username = (String) inputStream.readObject();
            User userWithParticularUsername = Server.accountsService.getParticularUser(username);
            if(userWithParticularUsername != null){
                outputStream.writeObject("This username signed up before");
                return;
            }
            outputStream.writeObject("OK");
            User newUser = (User)inputStream.readObject();
            Server.accountsService.signUp(newUser);
            File file = new File("ServerFiles/Pictures/"+Server.accountsService.getParticularUser(username).getId() + ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            File defaultProfile = new File("ServerFiles/Pictures/default.jpg");
            FileCopier copier = new FileCopier(defaultProfile,fileOutputStream);
            Main.executorService.execute(copier);
            outputStream.writeObject("OK");
        } catch (Exception e) {
            log.openStreamError(clientSocket.getInetAddress());
            e.printStackTrace();
        }
    }

    /**
     * Is used when user want to log in
     * @param clientSocket .
     */
    User logIn(Socket clientSocket){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            String username = (String) inputStream.readObject();
            String password = (String) inputStream.readObject();
            User user = Server.accountsService.logIn(username,password);
            outputStream.writeObject(user);
            if(user != null) {
                user.setStatus(Status.Online);
                return user;
            }
        } catch (Exception e) {
            log.openStreamError(clientSocket.getInetAddress());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Is used when user want to log out
     * @param user : refrence of target user
     */
    public void logout(User user){
        Server.accountsService.logout(user);
    }

    /**
     * Is used when user want to set his/her profile picture
     * @param user : refrence of user
     * @param clientSocket .
     */
    public void setPicture(User user, Socket clientSocket){
        Server.accountsService.setPicture(user,clientSocket);
    }

    /**
     * Is used want to change his/her username
     * @param user : refrence of user
     * @param clientSocket .
     */
    public void changeUsername(User user,Socket clientSocket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            String newUsername = (String) inputStream.readObject();
            AccountsService service = Server.accountsService;
            if(service.getParticularUser(newUsername) == null){
                service.changeUsername(user.getUsername(),newUsername);
                user.setUsername(newUsername);
                outputStream.writeObject("OK");
            }
            else
                outputStream.writeObject("Not unique");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used want to change his/her password
     * @param user : refrence of user
     * @param clientSocket .
     */
    public void changePassword(User user, Socket clientSocket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            String newPassword = (String) inputStream.readObject();
            Server.accountsService.changePassword(user.getUsername(), newPassword);
            outputStream.writeObject("Password changed successfully");
            user.setPassword(newPassword);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used want to change his/her email
     * @param user : refrence of user
     * @param clientSocket .
     */
    public void changeEmail(User user,Socket clientSocket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            String newEmail = (String) inputStream.readObject();
            Server.accountsService.changeEmail(user.getUsername(), newEmail);
            outputStream.writeObject("Email changed successfully");
            user.setEmail(newEmail);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used want to change his/her phone
     * @param user : refrence of user
     * @param clientSocket .
     */
    public void changePhone(User user,Socket clientSocket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            String newPhone = (String) inputStream.readObject();
            Server.accountsService.changePhone(user.getUsername(), newPhone);
            outputStream.writeObject("Phone changed successfully");
            user.setPhone(newPhone);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to set status picture of user
     * @param user : refrence of user
     * @param socket .
     */
    public void setStatus(User user,Socket socket){
        ObjectInputStream inputStream;
        Status status;
        while (true){
            try {
                inputStream = new ObjectInputStream(socket.getInputStream());
                status = (Status) inputStream.readObject();
                if(status == null)
                    break;
                user.setStatus(status);
            }catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Is used to send a particular user's info with his/her id
     * @param socket .
     */
    public void sendUserInfoWithId(Socket socket){
        try{
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            Integer targetId = (Integer) inputStream.readObject();
            User viewerUser = (User) inputStream.readObject();
            User  targetUser = Server.accountsService.getParticularUser(targetId);
            if(targetUser.getInformation().checkIsBlock(viewerUser.getId())){
                outputStream.writeObject(null);
                outputStream.writeObject(null);
            }
            else {
                String imageAddress = "ServerFiles/Pictures/" + targetUser.getId() + ".jpg";
                File profileImage = new File(imageAddress);
                byte[] imageBytes = Files.readAllBytes(profileImage.toPath());
                outputStream.writeObject(imageBytes);
                outputStream.writeObject(targetUser.getInformation().getStatus());
            }
            outputStream.writeObject(targetUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to send a particular user's info with his/her username
     * @param socket .
     */
    public void sendUserInfoWithUsername(Socket socket){
        try{
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            String targetUsername = (String) inputStream.readObject();
            User viewerUser = (User) inputStream.readObject();
            User  targetUser = Server.accountsService.getParticularUser(targetUsername);
            if(targetUser.getInformation().checkIsBlock(viewerUser.getId())){
                outputStream.writeObject(null);
                outputStream.writeObject(null);
            }
            else {
                String imageAddress = "ServerFiles/Pictures/" + targetUser.getId() + ".jpg";
                File profileImage = new File(imageAddress);
                byte[] imageBytes = Files.readAllBytes(profileImage.toPath());
                outputStream.writeObject(imageBytes);
                outputStream.writeObject(targetUser.getInformation().getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
