/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Moduls.Dto.User;

import com.example.discordfx.Lateral.Notification;
import com.example.discordfx.Moduls.Dto.DiscordServer.Invitation;
import java.io.*;

public class User implements Serializable {

    private final int id;
    private String username;
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

    /**
     * Is used to load user's information from binary file
     */
    public void loadInformation(){
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Files/UsersInfo/"+id+".bin"))){
            information = (UserLateralInformation) inputStream.readObject();
            System.out.println(information.getBlockesId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to set status of user
     * @param status : status of user
     */
    public void setStatus(Status status){
        information.setStatus(status);
        updateInformation();
    }

    /**
     * Is used to set username of user
     * @param username : username of user
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Is used to set password of user
     * @param password : password of user
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Is used to set email of user
     * @param email : email of user
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Is used to set phone number of user
     * @param phone : phone number of user
     */
    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setJwtToken(String jwtToken){
        this.jwtToken = jwtToken;
    }

    /**
     * get access to id of user
     * @return : id of user
     */
    public int getId(){
        return id;
    }

    /**
     * get access to username of user
     * @return : username of user
     */
    public String getUsername(){
        return username;
    }

    /**
     * get access to password of user
     * @return : password of user
     */
    public String getPassword(){
        return password;
    }

    /**
     * get access to email of user
     * @return : email of user
     */
    public String getEmail(){
        return email;
    }

    /**
     * get access to phone of user
     * @return : phone of user
     */
    public String getPhone(){
        return phone;
    }

    /**
     * get access to information of user
     * @return : information of user
     */
    public UserLateralInformation getInformation(){
        return information;
    }

    /**
     * Is used to add a private chat
     * @param port : port of private chat
     * @param targetUsername : other username in private chat
     */
    public void addPrivateChat(int port,String targetUsername){
        information.addPrivateChat(targetUsername,port);
        updateInformation();
    }

    /**
     * Is used to add a block id
     * @param targetId : target id
     */
    public void addBlock(Integer targetId){
        information.addBlock(targetId);
        updateInformation();
    }

    /**
     * Is used to add a friend id
     * @param targetId : target id
     */
    public void addFriend(Integer targetId){
        information.addFriend(targetId);
        updateInformation();
    }

    /**
     * Is used to add a server chat id
     * @param targetId : target id
     */
    public void addServerChat(Integer targetId){
        information.addDiscordServer(targetId);
        updateInformation();
    }

    /**
     * Is used to add a invitation to a server chat
     * @param newInvitation : target invitation
     */
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

    public void unblock(Integer targetId){
        information.unblock(targetId);
        updateInformation();
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
