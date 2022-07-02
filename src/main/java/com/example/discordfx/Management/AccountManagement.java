package com.example.discordfx.Management;

import com.example.discordfx.Moduls.Dto.User.User;
import javafx.scene.text.Text;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AccountManagement {

    private final Socket socket;
    private static AccountManagement accountManagement = null;

    private AccountManagement(Socket socket){
        this.socket = socket;
    }

    public static AccountManagement getInstance(Socket socket){
        if(accountManagement == null)
            accountManagement = new AccountManagement(socket);
        return accountManagement;
    }

    public User logIn(String username, String password){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(username);
            outputStream.writeObject(password);
            return (User) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void signUp(Text text,String username,String password,String email,String phone){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(username);
            String statusOfUsername = (String) inputStream.readObject();
            if(statusOfUsername.equals("This username signed up before")){
                text.setText(statusOfUsername);
                return;
            }
            outputStream.writeObject(new User(0,username,password,email,phone));
        } catch (Exception e) {
            text.setText(e.getMessage());
        }
    }

}