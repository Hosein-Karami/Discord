package com.example.discordfx.Server.Service;

import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Management.AccountManagement;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ProfileService {

    private final Socket clientSocket;
    private InputStream in;
    private final User user;
    private final AccountManagement accountManagement = new AccountManagement();

    public ProfileService(User user, Socket clientSocket){
        this.clientSocket = clientSocket;
        this.user = user;
        try {
            in = clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            int choice = 0;
            while (choice != 7){
                choice = in.read();
                if(choice == 1)
                    accountManagement.setPicture(user,clientSocket);
                else if(choice == 2)
                    accountManagement.changeUsername(user,clientSocket);
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
            e.printStackTrace();
        }
    }

}
