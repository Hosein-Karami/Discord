package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.User.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FriendsManagement {

    private User user;

    private OutputStream out;
    private InputStream in;
    {
        try {
            in = Start.socket.getInputStream();
            out = Start.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //All friends :

    @FXML
    ImageView profileImage_1;
    @FXML
    ImageView status_1;
    @FXML
    Text username_1;
    @FXML
    Button nextButton_1;

    public void initialize_1(){
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
