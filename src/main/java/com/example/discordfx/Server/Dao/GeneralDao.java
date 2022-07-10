/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Dao;

import com.example.discordfx.Moduls.Entity.UserEntity;

public interface GeneralDao {

    void insert(UserEntity userEntity);
    UserEntity logIn(String username,String password) ;
    UserEntity getParticularUser(String username) ;
    UserEntity getParticularUser(int id) ;
    void changeUsername(String username,String newUsername);
    void changePassword(String targetUsername,String newPassword);
    void changeEmail(String targetUsername,String newEmail);
    void changePhone(String targetUsername,String newPhone);

}
