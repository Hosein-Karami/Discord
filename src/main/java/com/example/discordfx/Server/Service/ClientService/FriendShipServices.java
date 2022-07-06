package com.example.discordfx.Server.Service.ClientService;

import com.example.discordfx.Log.ServicesLog.FriendshipLog;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Moduls.Entity.UserEntity;

public class FriendShipServices {

    public void addFriends(User firstUser, User secondUser){
        firstUser.addFriend(secondUser.getUsername());
        secondUser.addFriend(firstUser.getUsername());
        try {
            firstUser.updateInformation();
            secondUser.updateInformation();
            FriendshipLog log = new FriendshipLog();
            log.addFriendSuccessfully(firstUser.getUsername(), secondUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFriend(User firstUser,User secondUser) throws Exception {
        firstUser.removeFriend(secondUser.getUsername());
        secondUser.removeFriend(firstUser.getUsername());
        firstUser.updateInformation();
        secondUser.updateInformation();
    }

    public void removeRequest(User receiver,User sender) {
        receiver.removePending(sender.getUsername());
    }

    public void blockUser(User blocker,String targetUsername) throws Exception {
        blocker.addBlock(targetUsername);
        FriendshipLog log = new FriendshipLog();
        try {
            blocker.updateInformation();
            log.blockSuccessfully(blocker.getUsername(),targetUsername);
        } catch (Exception e) {
            log.blockError(blocker.getUsername(),targetUsername);
            throw new Exception();
        }
    }

    private User convertEntityToDto(UserEntity entity){
        return new User(entity.getId(), entity.getUsername(),entity.getPassword(),entity.getEmail(),entity.getPhone());
    }

}
