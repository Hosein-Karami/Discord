/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Dao;

import com.example.discordfx.Log.UserDaoLog;
import com.example.discordfx.Moduls.Entity.UserEntity;
import java.io.*;
import java.util.HashMap;

public class FileDao implements GeneralDao{

    private static int counter;
    private UserDaoLog log = new UserDaoLog();
    private static final HashMap<Integer,String> usersInfo = new HashMap<>();

    /**
     * Is used to insert a new user to database
     * @param userEntity : entity refrence of new user
     */
    @Override
    public void insert(UserEntity userEntity) {
        File file = new File(getValidPath(userEntity.getUsername()));
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))){
            userEntity.setId(counter);
            outputStream.writeObject(userEntity);
            outputStream.flush();
            usersInfo.put(counter,userEntity.getUsername());
            counter++;
            log.insertSuccess(userEntity.getUsername());
        } catch (Exception e) {
            log.insertError();
            e.printStackTrace();
        }
    }

    /**
     * Is used when a user want to login
     * @param username : username of user
     * @param password : password of user
     * @return : if password and username are true return entity refrence of user,else return null
     */
    @Override
    public UserEntity logIn(String username, String password) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(getValidPath(username)))){
            UserEntity targetUser = (UserEntity) inputStream.readObject();
            if(targetUser.getPassword().equals(password)) {
                log.logInSuccessfully(username);
                return targetUser;
            }
            return null;
        }catch (FileNotFoundException e){
            return null;
        }
        catch (Exception e) {
            log.logInError(username);
            return null;
        }
    }

    /**
     * Is used to get a user with his/her username
     * @param username : username of target user
     * @return : if username is validated,return entity refrence of user,else return null
     */
    @Override
    public UserEntity getParticularUser(String username) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(getValidPath(username)))){
            return  (UserEntity) inputStream.readObject();
        }catch (FileNotFoundException e){
            return null;
        }catch (Exception e){
            log.getError(username);
            return null;
        }
    }

    /**
     * Is used to get a user with his/her id
     * @param id : id of target user
     * @return : if id is validated,return entity refrence of user,else return null
     */
    @Override
    public UserEntity getParticularUser(int id) {
        String targetUsername = usersInfo.get(id);
        System.out.println(usersInfo);
        System.out.println(id);
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(getValidPath(targetUsername)))){
            return  (UserEntity) inputStream.readObject();
        }catch (FileNotFoundException e){
            return null;
        }catch (Exception e){
            log.getError(targetUsername);
            return null;
        }
    }

    /**
     * Is used when user want to change his/her username
     * @param username : old username
     * @param newUsername : new username
     */
    @Override
    public void changeUsername(String username, String newUsername) {
        int targetId = 0;
        for(String x : usersInfo.values()){
            if(x.equals(username))
                break;
            targetId++;
        }
        UserEntity targetUser;
        File olderFile = new File(getValidPath(username));
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(olderFile))){
            targetUser = (UserEntity) inputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            log.changeUsernameError(username);
            return;
        }
        targetUser.setUsername(newUsername);
        File newFile = new File(getValidPath(newUsername));
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(newFile))){
            outputStream.writeObject(targetUser);
            outputStream.flush();
            log.changeUsernameSuccessfully(username,newUsername);
            usersInfo.remove(targetId);
            usersInfo.put(targetId,newUsername);
            olderFile.delete();
        }catch (Exception e){
            e.printStackTrace();
            log.changeUsernameError(username);
        }
    }

    /**
     * Is used when a user want to change his/her password
     * @param targetUsername : user's username
     * @param newPassword : new password
     */
    @Override
    public void changePassword(String targetUsername, String newPassword) {
        UserEntity targetUser;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(getValidPath(targetUsername)))){
            targetUser = (UserEntity) inputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        targetUser.setPassword(newPassword);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(getValidPath(targetUsername)))){
            outputStream.writeObject(targetUser);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used when a user want to change his/her email
     * @param targetUsername : user's username
     * @param newEmail : new email
     */
    @Override
    public void changeEmail(String targetUsername, String newEmail) {
        UserEntity targetUser;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(getValidPath(targetUsername)))){
            targetUser = (UserEntity) inputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        targetUser.setEmail(newEmail);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(getValidPath(targetUsername)))){
            outputStream.writeObject(targetUser);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used when a user want to change his/her phone
     * @param targetUsername : user's username
     * @param newPhone : new phone
     */
    @Override
    public void changePhone(String targetUsername, String newPhone) {
        UserEntity targetUser;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(getValidPath(targetUsername)))){
            targetUser = (UserEntity) inputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        targetUser.setPhone(newPhone);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(getValidPath(targetUsername)))){
            outputStream.writeObject(targetUser);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getValidPath(String targetUsername){
        return "Files/DatabaseFiles/"+targetUsername+".bin";
    }


}
