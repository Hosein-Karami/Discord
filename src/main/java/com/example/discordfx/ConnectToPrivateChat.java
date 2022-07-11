/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ConnectToPrivateChat implements Initializable {

    @FXML
    Text text;
    @FXML
    Text username;
    @FXML
    ImageView profileImage;
    @FXML
    ImageView statusImage;
    @FXML
    Button nextButton;
    @FXML
    Button previousButton;
    @FXML
    Button connect;

    private int index;
    private User user;
    private HashMap<String,Integer> privateChats;
    private ArrayList<String> usernames;
    private InputStream in;
    private OutputStream out;
    {
        try {
            out = Start.socket.getOutputStream();
            in = Start.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to initialize the fxml page
     * @param url .
     * @param resourceBundle .
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
            privateChats = user.getInformation().getPrivateChats();
            if(privateChats.size() == 0) {
                text.setText("You don't have any private chat");
                nextButton.setVisible(false);
                previousButton.setVisible(false);
                connect.setVisible(false);
            }
            else {
                usernames = new ArrayList<>(privateChats.keySet());
                index = 0;
                loadInformation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * s used to load information of people who user has chat with them
     */
    public void loadInformation(){
        try {
            out.write(4);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(usernames.get(index));
            outputStream.writeObject(user);
            byte[] senderProfile = (byte[]) inputStream.readObject();
            Status userStatus = (Status) inputStream.readObject();
            if(senderProfile == null){
                profileImage.setImage(null);
                statusImage.setImage(null);
            }
            else {
                File file = new File("ClientFiles/temp.jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(senderProfile);
                fileOutputStream.flush();
                fileOutputStream.close();
                setProperStatusImage(userStatus);
                Image friendProfile = new Image("file:ClientFiles/temp.jpg");
                profileImage.setImage(friendProfile);
                file.delete();
            }
            username.setText(usernames.get(index));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to load next private chat info
     */
    public void next(){
        if(index != (usernames.size() - 1)) {
            username.setText("");
            profileImage.setImage(null);
            statusImage.setImage(null);
            index++;
            loadInformation();
        }
    }

    /**
     * Is used to load previous private chat info
     */
    public void previous(){
        if(index != 0) {
            username.setText("");
            profileImage.setImage(null);
            statusImage.setImage(null);
            index--;
            loadInformation();
        }
    }

    /**
     * Is used to connect to a private chat
     * @param event .
     */
    public void connect(ActionEvent event){
        try {
            out.write(16);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(privateChats.get(usernames.get(index)));
            System.out.println(privateChats.get(usernames.get(index)));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PrivateChat.fxml"));
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

    /**
     * Is used to set status image of target user
     * @param status : status of target user
     */
    private void setProperStatusImage(Status status){
        Image image = null;
        if(status == Status.Online)
            image = new Image("file:src/main/resources/Status/Online.png");
        else if(status == Status.Offline)
            image = new Image("file:src/main/resources/Status/Offline.png");
        else if(status == Status.Do_Not_Disturb)
            image = new Image("file:src/main/resources/Status/Do_Not_Disturb.png");
        else if(status == Status.Invisible)
            image = new Image("file:src/main/resources/Status/Invisible.png");
        else if(status == Status.Idle)
            image = new Image("file:src/main/resources/Status/Idle.png");
        statusImage.setImage(image);
    }

}
