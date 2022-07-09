package com.example.discordfx.Server.Dao;

import com.example.discordfx.Moduls.Entity.UserEntity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileDao implements GeneralDao{

    private int userCounter;

    @Override
    public void insert(UserEntity userEntity) throws Exception {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Files/Database.bin",true))){
            userEntity.setId(userCounter);
            outputStream.writeObject(userEntity);
            outputStream.flush();
            userCounter++;
        }catch (Exception e){
            throw new Exception();
        }
    }

    @Override
    public UserEntity logIn(String username, String password) throws Exception {
        UserEntity userEntity;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Files/Database.bin"))){
            while (true) {
                userEntity = (UserEntity) inputStream.readObject();
                if(userEntity == null)
                    return null;
                if (userEntity.getUsername().equals(username) && userEntity.getPassword().equals(password))
                    return userEntity;
            }
        }catch (Exception e){
            throw new Exception();
        }
    }

    @Override
    public UserEntity getParticularUser(String username) throws Exception {
        UserEntity userEntity;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Files/Database.bin"))){
            while (true) {
                userEntity = (UserEntity) inputStream.readObject();
                if(userEntity == null)
                    return null;
                if (userEntity.getUsername().equals(username))
                    return userEntity;
            }
        }catch (Exception e){
            throw new Exception();
        }
    }

    @Override
    public UserEntity getParticularUser(int id) throws Exception {
        UserEntity userEntity;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Files/Database.bin"))){
            while (true) {
                userEntity = (UserEntity) inputStream.readObject();
                if(userEntity == null)
                    return null;
                if (userEntity.getId() == id)
                    return userEntity;
            }
        }catch (Exception e){
            throw new Exception();
        }
    }

    @Override
    public void changeUsername(String username, String newUsername) throws Exception {

    }

    @Override
    public void changePassword(String targetUsername, String newPassword) throws Exception {

    }

    @Override
    public void changeEmail(String targetUsername, String newEmail) throws Exception {

    }

    @Override
    public void changePhone(String targetUsername, String newPhone) throws Exception {

    }

}
