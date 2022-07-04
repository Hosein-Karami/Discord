package com.example.discordfx.Server.Service.ClientService;

import com.example.discordfx.Log.ServicesLog.FriendshipLog;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Moduls.Entity.UserEntity;
import com.example.discordfx.Server.Dao.UserDao;

import java.util.ArrayList;

public class FriendShipServices {

    public void addFriends(User firstUser, User secondUser){
        firstUser.addFriend(secondUser.getId());
        secondUser.addFriend(firstUser.getId());
        try {
            firstUser.updateInformation();
            secondUser.updateInformation();
            FriendshipLog log = new FriendshipLog();
            log.addFriendSuccessfully(firstUser.getId(), secondUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFriend(User firstUser,User secondUser) throws Exception {
        firstUser.removeFriend(secondUser.getId());
        secondUser.removeFriend(firstUser.getId());
        firstUser.updateInformation();
        secondUser.updateInformation();
    }

    public void removeRequest(User receiver,User sender) {
        receiver.removePending(sender.getUsername());
    }

    public String friendshipRequests(User user){
        ArrayList<Integer> friendshipRequests = user.getInformation().getInvitationId();
        if(friendshipRequests.size() == 0)
            return "You don't have any friendship request.";
        String requests = "";
        try {
            UserDao userDao = new UserDao();
            int temp = 1;
            for (Integer x : friendshipRequests) {
                requests = requests.concat(temp + ")" + convertEntityToDto(userDao.getParticularUser(x)).getUsername() + "\n");
                temp++;
            }
            return requests;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void requestFriendship(User targetUser,int senderId) throws Exception {
        targetUser.addInvitation(senderId);
        FriendshipLog log = new FriendshipLog();
        try {
            targetUser.updateInformation();
            log.friendRequestSuccessfully(senderId, targetUser.getId());
        } catch (Exception e) {
            log.friendRequestError(senderId, targetUser.getId());
            throw new Exception();
        }
    }

    public void blockUser(User blocker,Integer targetUserId) throws Exception {
        blocker.addBlock(targetUserId);
        FriendshipLog log = new FriendshipLog();
        try {
            blocker.updateInformation();
            log.blockSuccessfully(blocker.getId(),targetUserId);
        } catch (Exception e) {
            log.blockError(blocker.getId(),targetUserId);
            throw new Exception();
        }
    }

    private User convertEntityToDto(UserEntity entity){
        return new User(entity.getId(), entity.getUsername(),entity.getPassword(),entity.getEmail(),entity.getPhone());
    }

}
