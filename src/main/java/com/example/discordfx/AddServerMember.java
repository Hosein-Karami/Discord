package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.User.Status;
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
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddServerMember implements Initializable {

    private User user;
    private ArrayList<String> friends;
    private int friendIndex;
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
    ImageView targetUserProfile;
    @FXML
    ImageView targetUserStatus;
    @FXML
    TextField targetUsername;
    @FXML
    Text resultText;
    @FXML
    Text friendUsername;
    @FXML
    Text resultText_2;
    @FXML
    Button nextButton;
    @FXML
    Button previousButton;
    @FXML
    Button inviteButton;
    @FXML
    Button searchButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
            friends = user.getFriends();
            if(friends.size() == 0){
                resultText.setText("You don't have any friend");
                nextButton.setVisible(false);
                previousButton.setVisible(false);
                inviteButton.setVisible(false);
                targetUsername.setVisible(false);
                searchButton.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendInvitation(){
        try {
            out.write(2);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(friends.get(friendIndex));
            resultText.setText((String) inputStream.readObject());
            inviteButton.setVisible(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void load(){
        String friendUsername = friends.get(friendIndex);
        try {
            out.write(1);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(friendUsername);
            String status = (String) inputStream.readObject();
            out.write(3);
            outputStream = new ObjectOutputStream(out);
            inputStream = new ObjectInputStream(in);
            outputStream.writeObject(friendUsername);
            outputStream.writeObject(user);
            byte[] imageBytes = (byte[]) inputStream.readObject();
            Status userStatus = (Status) inputStream.readObject();
            setTargetUserProfile(imageBytes);
            setProperStatusImage(userStatus);
            targetUsername.setText(friendUsername);
            if(status.equals("NO")) {
                resultText_2.setVisible(false);
                inviteButton.setVisible(true);
            }
            else {
                inviteButton.setVisible(false);
                resultText_2.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchFriend(){
        if(targetUsername.getText().isEmpty()){
            resultText.setText("Enter your friend username");
            return;
        }
        if(friends.contains(targetUsername.getText())){
            friendIndex = friends.indexOf(targetUsername.getText());
            load();
        }
        else
            resultText.setText("You don't have friend with this username");
    }

    public void next(){
        if(friendIndex != (friends.size() - 1)) {
            targetUserProfile.setImage(null);
            targetUserStatus.setImage(null);
            friendIndex++;
            load();
        }
    }

    public void previous(){
        if(friendIndex != 0) {
            targetUserProfile.setImage(null);
            targetUserStatus.setImage(null);
            friendIndex--;
            load();
        }
    }

    public void backToServer(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DiscordServer.fxml"));
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

    private void setTargetUserProfile(byte[] bytes) {
        File file = new File("ClientFiles/temp.jpg");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        Image profile = new Image("ClientFiles/temp.jpg");
        targetUserProfile.setImage(profile);
        file.delete();
    }

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
        targetUserStatus.setImage(image);
    }

}
