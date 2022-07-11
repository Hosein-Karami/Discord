/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Client.Management.AccountManagement;
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

    /**
     * Is used to back
     * @param event .
     */
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

    /**
     * Is used to sign up
     * @param event .
     */
    public void signup(ActionEvent event){
        if(checkInfo()){
            AccountManagement accountManagement = new AccountManagement(Start.socket);
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

    /**
     * Is used to check validity of inputs
     * @return : validity value of inputs
     */
    private boolean checkInfo(){
        String pass = password.getText();
        if(username.getText().length() < 6) {
            text.setText("Username should has at least 6 character");
            return false;
        }
        if(pass.length() < 8) {
            text.setText("Password should has at least 8 character");
            return false;
        }
        if ( ! Pattern.compile("[0-9]").matcher(pass).find()) {
            text.setText("password should has number");
            return false;
        }
        if ( ! Pattern.compile("[a-z]").matcher(pass).find()) {
            text.setText("password should has small alphabet");
            return false;
        }
        if ( ! Pattern.compile("[A-Z]").matcher(pass).find()) {
            text.setText("password should has capital alphabet");
            return false;
        }
        if(!(Pattern.compile("^(.+)@(.+)$").matcher(email.getText()).find())) {
            text.setText("Invalid email");
            return false;
        }
        if(Pattern.compile("[A-Z]").matcher(phoneNumber.getText()).find() || Pattern.compile("[a-z]").matcher(phoneNumber.getText()).find()) {
            text.setText("Phone number has only numbers,not alphabets");
            return false;
        }
        if(!(phoneNumber.getText().isBlank())) {
            if (phoneNumber.getText().length() != 11) {
                text.setText("Phone number length should be 11");
                return false;
            }
        }
        return true;
    }
}
