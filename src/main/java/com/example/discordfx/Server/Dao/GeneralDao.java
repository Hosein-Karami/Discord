package com.example.discordfx.Server.Dao;

import com.example.discordfx.Moduls.Entity.UserEntity;

public interface GeneralDao {

    public void insert(UserEntity userEntity) throws Exception;
    public UserEntity logIn(String username,String password) throws Exception;
    public UserEntity getParticularUser(String username) throws Exception;
    public UserEntity getParticularUser(int id) throws Exception;
    public void changeUsername(String username,String newUsername) throws Exception;
    public void changePassword(String targetUsername,String newPassword) throws Exception;
    public void changeEmail(String targetUsername,String newEmail) throws Exception;
    public void changePhone(String targetUsername,String newPhone) throws Exception;

}
