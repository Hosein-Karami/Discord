package com.example.discordfx.Server.Dao;

import com.example.discordfx.Log.UserDaoLog;
import com.example.discordfx.Moduls.Entity.UserEntity;
import java.sql.*;

public class UserDao {

    private final UserDaoLog log = new UserDaoLog();
    private Connection connection;
    {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/Project","root","1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(UserEntity userEntity) throws Exception {
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
            throw new Exception();
        }
    }

    public UserEntity logIn(String username,String password) throws Exception {
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
            throw new Exception();
        }
    }

    public UserEntity getParticularUser(String username) throws Exception {
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
            throw new Exception();
        }
    }

    public UserEntity getParticularUser(int id) throws Exception {
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
            throw new Exception();
        }
    }

    public void changePassword(String targetUsername,String newPassword) throws Exception {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Users SET password=? WHERE username=?");
            preparedStatement.setString(1,newPassword);
            preparedStatement.setString(2,targetUsername);
            preparedStatement.execute();
            log.changePasswordSuccessfully(targetUsername);
        } catch (Exception e) {
            log.changePasswordError(targetUsername);
            throw new Exception();
        }
    }

}
