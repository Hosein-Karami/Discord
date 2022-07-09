package com.example.discordfx.Moduls.Dto.User;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.DiscordServer.Invitation;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

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
            System.out.println(information.getBlockesId());
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

    public ArrayList<Integer> getPendings(){
        return information.getPendingId();
    }

    public ArrayList<Integer> getServerChats(){
        return information.getDiscordServers();
    }

    public ArrayList<Integer> getFriends() {
        return information.getFriendsId();
    }

    public ArrayList<Integer> getBlockedId(){
        return information.getBlockesId();
    }

    public ArrayList<Integer> getOutputRequests(){
        return information.getOutputRequestsId();
    }

    public ArrayList<Invitation> getInvitations(){
        return information.getInvitations();
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

    public void addBlock(Integer targetId){
        information.addBlock(targetId);
        updateInformation();
    }

    public void addFriend(Integer targetId){
        information.addFriend(targetId);
        updateInformation();
    }

    public void addServerChat(Integer newDserverName){
        information.addDiscordServer(newDserverName);
        updateInformation();
    }

    public void addInvitation(Invitation newInvitation){
        information.addInvitation(newInvitation);
        updateInformation();
    }

    public void addPending(Integer newPending){
        information.addPending(newPending);
        updateInformation();
    }

    public void addOutputRequest(Integer Id){
        information.addOutputRequestUsername(Id);
        updateInformation();
    }

    public void addNotification(Notification notification){
        information.addNotification(notification);
        updateInformation();
    }

    public boolean checkIsFriend(Integer targetId){
        return information.checkIsFriend(targetId);
    }

//    public boolean checkIsPending(Integer targetId){
      //  return information.checkIsPending(targetId);
   // }

    public boolean checkIsBlock(Integer targetId){
        return information.checkIsBlock(targetId);
    }

    public boolean checkIsRequested(Integer targetId){
        return information.checkIsRequested(targetId);
    }

    public void unblock(Integer targetId){
        information.unblock(targetId);
    }

    public void removeFriend(Integer targetId){
        information.removeFriend(targetId);
        updateInformation();
    }

    public void deleteNotifications(){
        information.deleteNotifications();
        updateInformation();
    }

    public void removePending(Integer targetId){
        information.removePending(targetId);
        updateInformation();
    }

    public void removeServerChat(String targetServerChatName){
        information.removeDiscordServer(targetServerChatName);
        updateInformation();
    }

    public void removeInvitation(Invitation targetInvitation){
        information.removeInvitation(targetInvitation);
        updateInformation();
    }

    public void removeOutputRequest(Integer targetId){
        information.removeOutputRequest(targetId);
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
