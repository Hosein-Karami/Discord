/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Service;

import com.example.discordfx.Log.AccountsLog;
import com.example.discordfx.Log.ServerLog;
import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Moduls.Entity.UserEntity;
import com.example.discordfx.Server.Dao.FileDao;
import com.example.discordfx.Server.Dao.GeneralDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AccountsService {

    private final GeneralDao userDao = new FileDao();
    private final AccountsLog log = new AccountsLog();

    /**
     * Is used to sign up
     * @param newUser .
     * @throws Exception .
     */
    public void signUp(User newUser) throws Exception {
        try {
            userDao.insert(convertDtoToEntity(newUser));
            //Make a binary file for this user in LateralFiles directory :
            User user = convertEntityToDto(userDao.getParticularUser(newUser.getUsername()));
            user.setStatus(Status.Offline);
            File file = new File("Files/UsersInfo/"+ user.getId() + ".bin");
            file.createNewFile();
            //Initialize file :
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(user.getInformation());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    /**
     * Is used to get particular user
     * @param username : target user's username
     * @return : target user
     */
    public User getParticularUser(String username){
        try {
            UserEntity userEntity = userDao.getParticularUser(username);
            if(userEntity != null) {
                User user = convertEntityToDto(userEntity);
                user.loadInformation();
                return user;
            }
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Is used to get particular user
     * @param userId : target user's id
     * @return : target user
     */
    public User getParticularUser(int userId){
        try {
            UserEntity userEntity = userDao.getParticularUser(userId);
            if(userEntity != null) {
                User user = convertEntityToDto(userEntity);
                user.loadInformation();
                return user;
            }
            else {
                System.out.println("Sag too roohet");
                return null;
            }
        } catch (Exception e) {
            System.out.println("saaaaag to roohet");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Is used to log in
     * @param username .
     * @param password .
     * @return : log in user
     */
    public User logIn(String username,String password){
        try {
            UserEntity userEntity = userDao.logIn(username,password);
            if(userEntity!=null){
                User user = convertEntityToDto(userEntity);
                user.setJwtToken(new TokenService(user).getToken());
                user.loadInformation();
                user.setStatus(Status.Online);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Is used to log out
     * @param user : lefter user
     */
    public void logout(User user){
        ServerLog log = new ServerLog();
        log.logOut(user.getUsername());
        user.setStatus(Status.Offline);
    }

    /**
     * Is used to change username of user
     * @param username : old username
     * @param newUsername : new username
     * @throws Exception .
     */
    public void changeUsername(String username,String newUsername) throws Exception {
        try {
            userDao.changeUsername(username,newUsername);
            log.changeUsername(username,newUsername);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    /**
     * Is used to change password of user
     * @param username : username of user
     * @param newPassword .
     * @throws Exception .
     */
    public void changePassword(String username,String newPassword) throws Exception {
        try {
            userDao.changePassword(username,newPassword);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    /**
     * Is used to change email of user
     * @param username : username of user
     * @param newEmail .
     * @throws Exception .
     */
    public void changeEmail(String username,String newEmail) throws Exception {
        try {
            userDao.changeEmail(username,newEmail);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    /**
     * Is used to change phone number of user
     * @param username : username of user
     * @param newPhone .
     * @throws Exception .
     */
    public void changePhone(String username,String newPhone) throws Exception {
        try {
            userDao.changePhone(username, newPhone);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    /**
     * Is used to set a user's profile image
     * @param user .
     * @param clientSocket .
     */
    public void setPicture(User user, Socket clientSocket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            byte[] bytes = (byte[]) inputStream.readObject();
            FileOutputStream outputStream_2 = new FileOutputStream("Files/Pictures/" + user.getId() + ".jpg");
            outputStream_2.write(bytes);
            outputStream_2.close();
            outputStream.writeObject("Your profile changed successfully");
            log.changePictureSuccessfully(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            log.changePictureError(user.getId());
        }
    }

    /**
     * Is used to convert entity to dto
     * @param entity : entity refrence
     * @return : dto refrence
     */
    private User convertEntityToDto(UserEntity entity){
        return new User(entity.getId(), entity.getUsername(),entity.getPassword(),entity.getEmail(),entity.getPhone());
    }

    /**
     * Is used to convert dto to entity
     * @param dto : dto refrence
     * @return : entity refrence
     */
    private UserEntity convertDtoToEntity(User dto){
        return new UserEntity(dto.getId(), dto.getUsername(),dto.getPassword(),dto.getEmail(),dto.getPhone());
    }

}
