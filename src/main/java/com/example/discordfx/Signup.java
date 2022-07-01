package com.example.discordfx;

import com.example.discordfx.Management.AccountManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.regex.Pattern;

public class Signup {

    @FXML
    Text text;
    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    TextField email;
    @FXML
    TextField phoneNumber;

    public void back(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
        Parent root;
        try {
            root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            text.setText(e.getMessage());
        }
    }

    public void signup(ActionEvent event){
        String pass = password.getText();
        if(username.getText().length() < 6)
            text.setText("Username should has at least 6 character");
        else if(pass.length() < 8)
            text.setText("Invalid password.password should has at least 8 character.Try again");
        else if ( ! Pattern.compile("[0-9]").matcher(pass).find())
            text.setText("password should has number");
        else if ( ! Pattern.compile("[a-z]").matcher(pass).find())
            text.setText("password should has small alphabet");
        else if ( ! Pattern.compile("[A-Z]").matcher(pass).find())
            text.setText("password should has capital alphabet");
        else if(!(Pattern.compile("^(.+)@(.+)$").matcher(email.getText()).find()))
            text.setText("Invalid email");
        else{
            AccountManagement accountManagement = AccountManagement.getInstance(Start.socket);
            accountManagement.signUp(text,username.getText(),password.getText(),email.getText(),phoneNumber.getText());
            if(!(text.getText().equals("This username signed up before"))){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
                try {
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
        }
    }

}
