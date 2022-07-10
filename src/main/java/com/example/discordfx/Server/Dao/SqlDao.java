/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Dao;

import com.example.discordfx.Log.UserDaoLog;
import com.example.discordfx.Moduls.Entity.UserEntity;
import java.sql.*;

public class SqlDao implements GeneralDao{

    private final UserDaoLog log = new UserDaoLog();
    private Connection connection;
    {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/Project","root","1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(UserEntity userEntity) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Users SET username=?,password=?,email=?,phone=?");
            preparedStatement.setString(1, userEntity.getUsername());
            preparedStatement.setString(2, userEntity.getPassword());
            preparedStatement.setString(3, userEntity.getEmail());
            preparedStatement.setString(4, userEntity.getPhone());
            preparedStatement.execute();
            log.insertSuccess(userEntity.getUsername());
        } catch (Exception e) {
            log.insertError();
            e.printStackTrace();
        }
    }

    @Override
    public UserEntity logIn(String username,String password) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users WHERE username='"+username+"' AND password='"+password+"'");
            while (resultSet.next()){
                if(resultSet.getInt(1) > 0){
                    UserEntity targetUser = new UserEntity(resultSet.getInt(1),
                            resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),
                            resultSet.getString(5));
                    log.logInSuccessfully(username);
                    return targetUser;
                }
            }
            return null;
        } catch (Exception e) {
            log.logInError(username);
            return null;
        }
    }

    @Override
    public UserEntity getParticularUser(String username) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users WHERE username='"+username+"'");
            while (resultSet.next()) {
                if (resultSet.getInt(1) > 0) {
                    return new UserEntity(resultSet.getInt(1),
                            resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),
                            resultSet.getString(5));
                }
            }
            return null;
        } catch (Exception e) {
            log.getError(username);
            return null;
        }
    }

    @Override
    public UserEntity getParticularUser(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users WHERE id='"+id+"'");
            while (resultSet.next()){
                return new UserEntity(resultSet.getInt(1),
                        resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),
                        resultSet.getString(5));
            }
            return null;
        } catch (Exception e) {
            log.getError(id);
            return null;
        }
    }

    @Override
    public void changeUsername(String username,String newUsername) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Users SET username=? WHERE username=?");
            preparedStatement.setString(1,newUsername);
            preparedStatement.setString(2,username);
            preparedStatement.execute();
            System.out.println(username +"     " + newUsername);
            log.changeUsernameSuccessfully(username,newUsername);
        } catch (Exception e) {
            log.changeUsernameError(username);
            e.printStackTrace();
        }
    }

    @Override
    public void changePassword(String targetUsername,String newPassword) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Users SET password=? WHERE username=?");
            preparedStatement.setString(1,newPassword);
            preparedStatement.setString(2,targetUsername);
            preparedStatement.execute();
            log.changePasswordSuccessfully(targetUsername);
        } catch (Exception e) {
            log.changePasswordError(targetUsername);
            e.printStackTrace();
        }
    }

    @Override
    public void changeEmail(String targetUsername,String newEmail) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Users SET email=? WHERE username=?");
            preparedStatement.setString(1,newEmail);
            preparedStatement.setString(2,targetUsername);
            preparedStatement.execute();
            log.changeEmailSuccessfully(targetUsername);
        } catch (Exception e) {
            log.changeEmailError(targetUsername);
            e.printStackTrace();
        }
    }

    @Override
    public void changePhone(String targetUsername,String newPhone) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Users SET phone=? WHERE username=?");
            preparedStatement.setString(1,newPhone);
            preparedStatement.setString(2,targetUsername);
            preparedStatement.execute();
            log.changePhoneSuccessfully(targetUsername);
        } catch (Exception e) {
            log.changePhoneError(targetUsername);
            e.printStackTrace();
        }
    }

}
