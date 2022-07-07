package com.example.discordfx.Moduls.Dto.User;

import com.example.discordfx.Lateral.Notification;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SplittableRandom;

public class User implements Serializable {

    private final int id;
    private final String username;
    private String password;
    private String email;
    private String phone;
    private UserLateralInformation information;
    private String jwtToken;

    //Constructor :
    public User(int id,String username, String password, String email, String phone){
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        information = new UserLateralInformation();
    }

    public void loadInformation(){
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Files/UsersInfo/"+id+".bin"))){
            information = (UserLateralInformation) inputStream.readObject();
            System.out.println(information.getBlockesUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStatus(Status status){
        information.setStatus(status);
        updateInformation();
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setJwtToken(String jwtToken){
        this.jwtToken = jwtToken;
    }

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public String getPhone(){
        return phone;
    }

    public Status getStatus(){
        return information.getStatus();
    }

    public ArrayList<Notification> getNotifications(){
        return information.getNotifications();
    }

    public ArrayList<String> getPendings(){
        return information.getPendingUsernames();
    }

    public ArrayList<String> getServerChats(){
        return information.getDiscordServers();
    }

    public ArrayList<String> getFriends() {
        return information.getFriendsUsernames();
    }

    public ArrayList<String> getBlockedUsernames(){
        return information.getBlockesUsername();
    }

    public ArrayList<String> getOutputRequests(){
        return information.getOutputRequestsUsernames();
    }

    public String getJwtToken(){
        return jwtToken;
    }

    public HashMap<String,Integer> getPrivateChats(){
        return information.getPrivateChats();
    }

    public UserLateralInformation getInformation(){
        return information;
    }

    public void addPrivateChat(int port,String targetUsername){
        information.addPrivateChat(targetUsername,port);
        updateInformation();
    }

    public void addBlock(String targetUsername){
        information.addBlock(targetUsername);
        updateInformation();
    }

    public void addFriend(String targetUsername){
        information.addFriend(targetUsername);
        updateInformation();
    }

    public void addPending(String newPending){
        information.addPending(newPending);
        updateInformation();
    }

    public void addOutputRequest(String username){
        information.addOutputRequestUsername(username);
        updateInformation();
    }

    public void addNotification(Notification notification){
        information.addNotification(notification);
        updateInformation();
    }

    public boolean checkIsFriend(String targetUsername){
        return information.checkIsFriend(targetUsername);
    }

    public boolean checkIsPending(String targetUsername){
        return information.checkIsPending(targetUsername);
    }

    public boolean checkIsBlock(String targetUsername){
        return information.checkIsBlock(targetUsername);
    }

    public void unblock(String targetUsername){
        information.unblock(targetUsername);
    }

    public void removeFriend(String targetUsername){
        information.removeFriend(targetUsername);
        updateInformation();
    }

    public void deleteNotifications(){
        information.deleteNotifications();
        updateInformation();
    }

    public void removePending(String targetUsername){
        information.removePending(targetUsername);
        updateInformation();
    }

    public void removeOutputRequest(String targetUsername){
        information.removeOutputRequest(targetUsername);
        updateInformation();
    }

    public void updateInformation() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Files/UsersInfo/"+this.getId()+".bin"))){
            outputStream.writeObject(information);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
