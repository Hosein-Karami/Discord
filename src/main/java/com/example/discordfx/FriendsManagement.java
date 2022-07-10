/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FriendsManagement implements Initializable{

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
            user = (User) inputStream.readObject();
        } catch (Exception e) {
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
    Text textStatus_1;
    @FXML
    Button nextButton_1;
    @FXML
    Button previousButton_1;
    @FXML
    Button removeFriend_1;

    private ArrayList<Integer> allFriends = new ArrayList<>();
    private User user;
    private int friendIndex;

    public void initialize_1() {
        blockedIndex = 0;
        friendIndex = 0;
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
            allFriends = user.getInformation().getFriendsId();
            load_1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load_1() {
        if (allFriends.size() == 0) {
            textStatus_1.setText("You don't have any friend");
            nextButton_1.setVisible(false);
            previousButton_1.setVisible(false);
            username_1.setVisible(false);
            profileImage_1.setVisible(false);
            status_1.setVisible(false);
            removeFriend_1.setVisible(false);
        }
        else
            loadInformation(allFriends,friendIndex,profileImage_1,status_1,username_1);
    }

    public void next_1(){
        if(friendIndex != (allFriends.size() - 1)) {
            profileImage_1.setImage(null);
            status_1.setImage(null);
            friendIndex++;
            load_1();
        }
    }

    public void previous_1(){
        if(friendIndex != 0) {
            profileImage_1.setImage(null);
            status_1.setImage(null);
            friendIndex--;
            load_1();
        }
    }

    public void removeFriend_1(){
        removeFriend(allFriends.get(friendIndex),profileImage_1,status_1,username_1,textStatus_1);
        initialize_1();
    }

    //Online friends :

    @FXML
    ImageView profileImage_2;
    @FXML
    ImageView status_2;
    @FXML
    Text username_2;
    @FXML
    Text textStatus_2;
    @FXML
    Button nextButton_2;
    @FXML
    Button previousButton_2;
    @FXML
    Button removeFriend_2;

    private final ArrayList<Integer> onlineFriends = new ArrayList<>();
    private int onlineFriendIndex;

    public void initialize_2(){
        friendIndex = 0;
        onlineFriendIndex = 0;
        try {
            out.write(12);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            Integer onlineFriendId;
            while (true){
                onlineFriendId = (Integer) inputStream.readObject();
                if(onlineFriendId == null)
                    break;
                onlineFriends.add(onlineFriendId);
            }
            load_2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load_2(){
        if (onlineFriends.size() == 0) {
            textStatus_2.setText("You don't have any online friend");
            nextButton_2.setVisible(false);
            previousButton_2.setVisible(false);
            username_2.setVisible(false);
            profileImage_2.setVisible(false);
            status_2.setVisible(false);
            removeFriend_2.setVisible(false);
        }
        else
            loadInformation(onlineFriends,onlineFriendIndex,profileImage_2,status_2,username_2);
    }

    public void next_2(){
        if(onlineFriendIndex != (onlineFriends.size() - 1)) {
            username_2.setText("");
            profileImage_2.setImage(null);
            status_2.setImage(null);
            onlineFriendIndex++;
            load_2();
        }
    }

    public void previous_2(){
        if(onlineFriendIndex != 0) {
            username_2.setText("");
            profileImage_2.setImage(null);
            status_2.setImage(null);
            onlineFriendIndex--;
            load_2();
        }
    }

    public void removeFriend_2(){
        removeFriend(onlineFriends.get(onlineFriendIndex),profileImage_2,status_2,username_2,textStatus_2);
        initialize_2();
    }

    //Blocked users :

    @FXML
    ImageView profileImage_3;
    @FXML
    ImageView status_3;
    @FXML
    Text username_3;
    @FXML
    Text textStatus_3;
    @FXML
    Button nextButton_3;
    @FXML
    Button previousButton_3;
    @FXML
    Button unblock;

    private ArrayList<Integer> blockedId;
    private int blockedIndex;

    public void initialize_3(){
        friendIndex = 0;
        blockedIndex = 0;
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
            blockedId = user.getInformation().getBlockesId();
            load_3();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load_3(){
        if (blockedId.size() == 0) {
            textStatus_3.setText("You didn't block any user");
            nextButton_3.setVisible(false);
            previousButton_3.setVisible(false);
            username_3.setVisible(false);
            profileImage_3.setVisible(false);
            status_3.setVisible(false);
            unblock.setVisible(false);
        }
        else
            loadInformation(blockedId,blockedIndex,profileImage_3,status_3,username_3);
    }

    public void next_3(){
        if(blockedIndex != (blockedId.size() - 1)) {
            username_3.setText("");
            profileImage_3.setImage(null);
            status_3.setImage(null);
            blockedIndex++;
            load_3();
        }
    }

    public void previous_3(){
        if(blockedIndex != 0) {
            username_3.setText("");
            profileImage_3.setImage(null);
            status_3.setImage(null);
            blockedIndex--;
            load_3();
        }
    }

    public void unblock() {
        try {
            out.write(10);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(blockedId.get(blockedIndex));
            textStatus_3.setText((String) inputStream.readObject());
            initialize_3();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void backToMenu(ActionEvent event){
        try {
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

    private void loadInformation(ArrayList<Integer> usernames,int index,ImageView profileImage,ImageView status,Text username){
        try {
            out.write(7);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(usernames.get(index));
            outputStream.writeObject(user);
            byte[] senderProfile = (byte[]) inputStream.readObject();
            Status userStatus = (Status) inputStream.readObject();
            String Username = (String) inputStream.readObject();
            username.setText(Username);
            if(senderProfile == null){
                profileImage.setImage(null);
                status.setImage(null);
            }
            else {
                File file = new File("ClientFiles/temp.jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(senderProfile);
                fileOutputStream.flush();
                fileOutputStream.close();
                setProperStatusImage(userStatus, status);
                Image friendProfile = new Image("file:ClientFiles/temp.jpg");
                profileImage.setImage(friendProfile);
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFriend(Integer targetId,ImageView profileImage,ImageView statusImage,Text username,Text result){
        try {
            out.write(11);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(targetId);
            String status = (String) inputStream.readObject();
            result.setText(status);
            statusImage.setImage(null);
            profileImage.setImage(null);
            username.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProperStatusImage(Status status,ImageView imageView){
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
        imageView.setImage(image);
    }

}
