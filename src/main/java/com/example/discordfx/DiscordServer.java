package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.User.Status;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class DiscordServer implements Initializable {

    static String DserverName;
    private OutputStream out;
    private InputStream in;
    {
        try {
            out = Start.socket.getOutputStream();
            in = Start.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    ImageView serverImageProfile;
    @FXML
    Text name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            out.write(21);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(DserverName);
            byte[] serverImageBytes = (byte[]) inputStream.readObject();
            setServerImageProfile(serverImageBytes);
            name.setText(DserverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMember(ActionEvent event){

    }

    private void setServerImageProfile(byte[] imageBytes){
        try {
            File file = new File("ClientFiles/temp.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(imageBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            Image serverImage = new Image("file:ClientFiles/temp.jpg");
            serverImageProfile.setImage(serverImage);
            file.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
