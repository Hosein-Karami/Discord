package com.example.discordfx.Server.Service;

import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Management.AccountManagement;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ProfileService {

    private final Socket clientSocket;
    private final User user;
    private final AccountManagement accountManagement = new AccountManagement();

    public ProfileService(User user, Socket clientSocket){
        this.clientSocket = clientSocket;
        this.user = user;
    }

    public void start(){
        try {
            ObjectInputStream inputStream;
            Integer choice = 0;
            while (choice != 7){
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
                choice = (Integer) inputStream.readObject();
                if(choice == 1)
                    accountManagement.setPicture(user,clientSocket);
                else if(choice == 3)
                    accountManagement.changePassword(user,clientSocket);
                else if(choice == 4)
                    accountManagement.changeEmail(user,clientSocket);
                else if(choice == 5)
                    accountManagement.changePhone(user,clientSocket);
                else if(choice == 6)
                    accountManagement.logout(user);
            }
        } catch (Exception e) {

        }
    }

}
