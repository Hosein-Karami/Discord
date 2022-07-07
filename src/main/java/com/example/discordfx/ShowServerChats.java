package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.User.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShowServerChats implements Initializable {

    @FXML
    ImageView serverProfileImage;
    @FXML
    Text text;
    @FXML
    Text serverChatName;
    @FXML
    Button nextButton;
    @FXML
    Button previousButton;

    private ArrayList<String> servers;
    private int serverIndex;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            servers = ((User) inputStream.readObject()).getServerChats();
            if(servers.size() == 0){
                text.setText("You aren't in any discord server");
                nextButton.setVisible(false);
                previousButton.setVisible(false);
            }
            else
                loadInformation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInformation(){
        try {
            out.write(21);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(servers.get(serverIndex));
            byte[] serverImageBytes = (byte[]) inputStream.readObject();
            if(serverImageBytes == null)
                serverProfileImage.setImage(null);
            else {
                File file = new File("ClientFiles/temp.jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(serverImageBytes);
                fileOutputStream.flush();
                fileOutputStream.close();
                Image serverImage = new Image("file:ClientFiles/temp.jpg");
                serverProfileImage.setImage(serverImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
