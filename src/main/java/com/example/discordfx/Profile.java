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
    private OutputStream out;
    private InputStream in;
    public static User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            Image image = new Image("file:ClientFiles/Profile.jpg");
            imageView.setImage(image);
            username.setText("Username : " + user.getUsername());
            password.setText("Password : " + user.getPassword());
            email.setText("Email : " + user.getEmail());
            phone.setText("Phone : " + user.getPhone());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setPicture(ActionEvent event){
        try{
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            File image = fileChooser.showOpenDialog(stage);
            if(image != null){
                String fileName = image.getName();
                if(fileName.contains(".jpg") || fileName.contains(".png") || fileName.contains(".jpeg")){
                    ObjectOutputStream outputStream = new ObjectOutputStream(out);
                    ObjectInputStream inputStream = new ObjectInputStream(in);
                    outputStream.writeObject(1);
                    outputStream = new ObjectOutputStream(out);
                    outputStream.writeObject(Files.readAllBytes(image.toPath()));
                    text.setText((String) inputStream.readObject());
                }
                else
                    text.setText("Invalid file format,format should be jpg,png,jpeg");
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
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(3);
            outputStream = new ObjectOutputStream(out);
            try {
                Checker.checkPassword(newPassword.getText());
                outputStream.writeObject("OK");
                ObjectInputStream inputStream = new ObjectInputStream(in);
                outputStream.writeObject(user.getJwtToken());
                String status = (String) inputStream.readObject();
                if (status.equals("Verification failed"))
                    text.setText(status);
                else {
                    outputStream.writeObject(newPassword.getText());
                    text.setText((String) inputStream.readObject());
                    password.setText("Password : "+newPassword.getText());
                    user.setPassword(newPassword.getText());
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
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(4);
            try {
                outputStream = new ObjectOutputStream(out);
                Checker.checkEmail(newEmail.getText());
                outputStream.writeObject("OK");
                ObjectInputStream inputStream = new ObjectInputStream(in);
                outputStream.writeObject(user.getJwtToken());
                String status = (String) inputStream.readObject();
                if (status.equals("Verification failed"))
                    text.setText(status);
                else {
                    outputStream.writeObject(newEmail.getText());
                    text.setText((String) inputStream.readObject());
                    email.setText("Email : "+newEmail.getText());
                    user.setEmail(newEmail.getText());
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
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(5);
            try {
                outputStream = new ObjectOutputStream(out);
                Checker.checkPhone(newPhone.getText());
                outputStream.writeObject("OK");
                ObjectInputStream inputStream = new ObjectInputStream(in);
                outputStream.writeObject(user.getJwtToken());
                String status = (String) inputStream.readObject();
                if (status.equals("Verification failed"))
                    text.setText(status);
                else {
                    outputStream.writeObject(newPhone.getText());
                    text.setText((String) inputStream.readObject());
                    phone.setText("Phone : "+newPhone.getText());
                    user.setPhone(newPhone.getText());
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
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(7);
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

    public void logout(ActionEvent event){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(6);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
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
        try (FileOutputStream fileOutputStream = new FileOutputStream("file:ClientFiles/Profile.jpg")){
            fileOutputStream.flush();
            fileOutputStream.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
