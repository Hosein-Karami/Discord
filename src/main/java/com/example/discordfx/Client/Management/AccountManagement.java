/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Client.Management;

import com.example.discordfx.Moduls.Dto.User.User;
import javafx.scene.text.Text;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AccountManagement {

    private final Socket socket;

    public AccountManagement(Socket socket){
        this.socket = socket;
    }

    /**
     * Is used when a client want to login
     * @param username : client's username
     * @param password : client's password
     * @return : result refrence
     */
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

    /**
     * Is used when a client want to register
     * @param text : text for show results
     * @param username : client's username
     * @param password : client's password
     * @param email : client's email
     * @param phone : client's phone number
     */
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
            User newUser = new User(0,username,password,email,phone);
            outputStream.writeObject(newUser);
            String status = (String) inputStream.readObject();
        } catch (Exception e) {
            text.setText(e.getMessage());
        }
    }

}
