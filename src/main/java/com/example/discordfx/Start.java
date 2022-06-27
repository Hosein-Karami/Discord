package com.example.discordfx;

import com.example.discordfx.Client.Management.AccountManagement;
import com.example.discordfx.Moduls.Dto.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Start {

    @FXML
    Text text;
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public static Socket socket;
    {
        try {
            String ip = "127.0.0.1";
            int port = 2000;
            socket = new Socket(ip, port);
        } catch (IOException e) {
            text.setText(e.getMessage());
        }
    }

    private final AccountManagement accountManagement = AccountManagement.getInstance(socket);

    public void login(){
        if(username.getText().isEmpty() || password.getText().isEmpty())
            text.setText("Enter username and password");
        else{
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(2);
                User user = accountManagement.logIn(text,username.getText(),password.getText());
                if(user == null)
                    text.setText("Username or password is false");
            } catch (IOException e) {
                text.setText(e.getMessage());
            }
        }
    }

    public void signup(ActionEvent event){
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(1);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Signup.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(){
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(3);
            System.exit(0);
        } catch (IOException e) {
            text.setText(e.getMessage());
        }
    }

}