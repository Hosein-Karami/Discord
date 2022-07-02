package com.example.discordfx;

import com.example.discordfx.Lateral.Checker;
import com.example.discordfx.Lateral.Exceptions.EmailException;
import com.example.discordfx.Lateral.Exceptions.PasswordExceptions.PasswordException;
import com.example.discordfx.Lateral.Exceptions.PhoneException;
import com.example.discordfx.Moduls.Dto.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Profile implements Initializable {

    @FXML
    ImageView imageView;
    @FXML
    Text text;
    @FXML
    Text username;
    @FXML
    Text password;
    @FXML
    Text email;
    @FXML
    Text phone;
    @FXML
    TextField newUsername;
    @FXML
    TextField newPassword;
    @FXML
    TextField newEmail;
    @FXML
    TextField newPhone;
    @FXML
    Button changePassButton;
    @FXML
    Button changeEmailButton;
    @FXML
    Button changePhoneButton;

    private final Socket socket = Start.socket;
    public static User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("C:\\Users\\hosein\\IdeaProjects\\DiscordFx\\ClientFiles\\Profile.jpg");
        imageView.setImage(image);
        username.setText("Username : "+user.getUsername());
        password.setText("Password : "+user.getPassword());
        email.setText("Email : "+user.getEmail());
        phone.setText("Phone : "+user.getPhone());
    }

    public void setPicture(ActionEvent event){
        try{
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(1);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(user.getJwtToken());
            String status = (String) inputStream.readObject();
            if(status.equals("Verification failed."))
                text.setText("Verification failed");
            else {
                try {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FileChooser fileChooser = new FileChooser();
                    File image = fileChooser.showOpenDialog(stage);
                    if ((Pattern.compile("^(.+).jpg\\z").matcher(image.getName()).find())) {
                        outputStream.writeObject("Ok");
                        byte[] imageBytes = Files.readAllBytes(image.toPath());
                        outputStream.writeObject(imageBytes);
                        status = (String) inputStream.readObject();
                        changeImage(image, imageBytes);
                        text.setText(status);
                    } else {
                        outputStream.writeObject("Invalid format");
                        text.setText("Format of input file should be jpg");
                    }
                }catch (Exception e){
                    outputStream.writeObject("ERROR");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void wantToChangePass(){
        newPassword.setVisible(true);
        changePassButton.setVisible(true);
    }

    public void changePassword(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(3);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            try {
                Checker.checkPassword(newPassword.getText());
                outputStream.writeObject("OK");
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream.writeObject(user.getJwtToken());
                String status = (String) inputStream.readObject();
                if (status.equals("Verification failed"))
                    text.setText(status);
                else {
                    outputStream.writeObject(newPassword.getText());
                    text.setText((String) inputStream.readObject());
                    password.setText(newPassword.getText());
                    newPassword.setVisible(false);
                    changePassButton.setVisible(false);
                }
            }catch (PasswordException e){
                outputStream.writeObject("ERROR");
                text.setText(e.getErrorMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void wantToChangeEmail(){
        newEmail.setVisible(true);
        changeEmailButton.setVisible(true);
    }

    public void changeEmail(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(4);
            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                Checker.checkEmail(newEmail.getText());
                outputStream.writeObject("OK");
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream.writeObject(user.getJwtToken());
                String status = (String) inputStream.readObject();
                if (status.equals("Verification failed"))
                    text.setText(status);
                else {
                    outputStream.writeObject(newEmail.getText());
                    text.setText((String) inputStream.readObject());
                    email.setText(newEmail.getText());
                    newEmail.setVisible(false);
                    changeEmailButton.setVisible(false);
                }
            }catch (EmailException e){
                outputStream.writeObject("ERROR");
                text.setText(e.getErrorMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void wantToChangePhone(){
        newPhone.setVisible(true);
        changePhoneButton.setVisible(true);
    }

    public void changePhone(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(5);
            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                Checker.checkPhone(newPhone.getText());
                outputStream.writeObject("OK");
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream.writeObject(user.getJwtToken());
                String status = (String) inputStream.readObject();
                if (status.equals("Verification failed"))
                    text.setText(status);
                else {
                    outputStream.writeObject(newPhone.getText());
                    text.setText((String) inputStream.readObject());
                    phone.setText(newPhone.getText());
                    newPhone.setVisible(false);
                    changePhoneButton.setVisible(false);
                }
            }catch (PhoneException e){
                outputStream.writeObject("ERROR");
                text.setText(e.getErrorMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent event){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(6);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void changeImage(File newImage,byte[] imageBytes){
        Image newProfileImage = new Image(newImage.getPath());
        imageView.setImage(newProfileImage);
        try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\hosein\\IdeaProjects\\DiscordFx\\ClientFiles\\Profile.jpg")){
            fileOutputStream.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
