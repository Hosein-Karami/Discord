package com.example.discordfx.Server.Service;

import com.example.discordfx.Log.ServerLog;
import com.example.discordfx.Log.ServicesLog.AccountsLog;
import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Moduls.Entity.UserEntity;
import com.example.discordfx.Server.Dao.UserDao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AccountsService {

    private final UserDao userDao = new UserDao();
    private final AccountsLog log = new AccountsLog();

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
            throw new Exception();
        }
    }

    public User getParticularUser(String username) {
        try {
            UserEntity userEntity = userDao.getParticularUser(username);
            if(userEntity != null) {
                User user = convertEntityToDto(userEntity);
                user.loadInformation();
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getParticularUser(int userId){
        try {
            UserEntity userEntity = userDao.getParticularUser(userId);
            if(userEntity != null) {
                User user = convertEntityToDto(userEntity);
                user.loadInformation();
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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

    public void logout(User user){
        ServerLog log = new ServerLog();
        log.logOut(user.getUsername());
        user.setStatus(Status.Offline);
    }

    public void changePassword(String username,String newPassword) throws Exception {
        try {
            userDao.changePassword(username,newPassword);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public void changeEmail(String username,String newPassword) throws Exception {
        try {
            userDao.changeEmail(username,newPassword);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public void changePhone(String username,String newPhone) throws Exception {
        try {
            userDao.changeEmail(username, newPhone);
        } catch (Exception e) {
            throw new Exception();
        }
    }

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

    private User convertEntityToDto(UserEntity entity){
        return new User(entity.getId(), entity.getUsername(),entity.getPassword(),entity.getEmail(),entity.getPhone());
    }

    private UserEntity convertDtoToEntity(User dto){
        return new UserEntity(dto.getId(), dto.getUsername(),dto.getPassword(),dto.getEmail(),dto.getPhone());
    }

}
