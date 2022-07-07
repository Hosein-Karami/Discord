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
import java.util.ArrayList;

public class RelationshipManagement {

    //Tab 1 :

    @FXML
    Text requestText;
    @FXML
    TextField requestUsername;

    private User user;
    private InputStream in;
    private OutputStream out;
    {
        try {
            out = Start.socket.getOutputStream();
            in = Start.socket.getInputStream();
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendRequest() {
        if (requestUsername.getText().isEmpty()) {
            requestText.setText("Enter target username");
            return;
        }
        try {
            if (user.checkIsPending(requestUsername.getText())) {
                requestText.setText("You requested to this user before");
                return;
            }
            if(user.getUsername().equals(requestUsername.getText())){
                requestText.setText("You can't send request to yourself");
                return;
            }
            if(user.checkIsFriend(requestUsername.getText())){
                requestText.setText("You are friend from before");
                return;
            }
            out.write(5);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(requestUsername.getText());
            String Status = (String) inputStream.readObject();
            System.out.println(Status.equals("OK"));
            if (Status.equals("OK")) {
                Status = (String) inputStream.readObject();
                if (Status.equals("OK")) {
                    Status = (String) inputStream.readObject();
                    if (Status.equals("OK")) {
                        requestText.setText((String) inputStream.readObject());
                        out.write(20);
                        inputStream = new ObjectInputStream(in);
                        user = (User) inputStream.readObject();
                    } else
                        requestText.setText(Status);
                } else
                    requestText.setText(Status);
            } else
                requestText.setText(Status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestUsername.setText("");
    }

    //Tab 2 :

    @FXML
    ImageView profileImage_1;
    @FXML
    ImageView status_1;
    @FXML
    Text username_1;
    @FXML
    Text statusText_1;
    @FXML
    Button acceptButton_1;
    @FXML
    Button rejectButton_1;
    @FXML
    Button nextButton_1;
    @FXML
    Button previousButton_1;

    private int senderIndex = 0;
    private ArrayList<String> requestsSenders;

    public void initialize() {
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
            requestsSenders = user.getPendings();
            loadInputRequests();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void nextRequest(){
        if(senderIndex != (requestsSenders.size() - 1)) {
            profileImage_1.setImage(null);
            status_1.setImage(null);
            senderIndex++;
            initialize();
        }
    }

    public void previousRequest(){
        if(senderIndex != 0) {
            profileImage_1.setImage(null);
            status_1.setImage(null);
            senderIndex--;
            initialize();
        }
    }

    public void loadInputRequests(){
        try {
            if(requestsSenders.size() == 0){
                statusText_1.setText("You don't have any input request");
                acceptButton_1.setVisible(false);
                rejectButton_1.setVisible(false);
                nextButton_1.setVisible(false);
                status_2.setImage(null);
                profileImage_1.setImage(null);
                username_1.setVisible(false);
                previousButton_1.setVisible(false);
            }
            else
                loadInformation(requestsSenders,senderIndex,profileImage_1,status_1,username_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptRequest(){
        try {
            out.write(6);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(requestsSenders.get(senderIndex));
            outputStream.writeObject("Accept");
            statusText_1.setText((String) inputStream.readObject());
            requestsSenders.remove(senderIndex);
            profileImage_1.setImage(null);
            status_1.setImage(null);
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rejectRequest(){
        try {
            out.write(6);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(requestsSenders.get(senderIndex));
            outputStream.writeObject("Reject");
            statusText_1.setText((String) inputStream.readObject());
            requestsSenders.remove(senderIndex);
            profileImage_1.setImage(null);
            status_1.setImage(null);
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Tab 3 :

    @FXML
    ImageView profileImage_2;
    @FXML
    ImageView status_2;
    @FXML
    Text username_2;
    @FXML
    Text statusText_2;
    @FXML
    Button cancelButton;
    @FXML
    Button nextButton_2;
    @FXML
    Button previousButton_2;

    private int requestIndex;
    private ArrayList<String> outputRequests;

    public void initialize_2(){
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
            outputRequests = user.getOutputRequests();
            loadOutputRequests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadOutputRequests(){
        try {
            if(outputRequests.size() == 0){
                statusText_2.setText("You don't have any output request");
                profileImage_2.setVisible(false);
                cancelButton.setVisible(false);
                nextButton_2.setVisible(false);
                previousButton_2.setVisible(false);
            }
            else
                loadInformation(outputRequests,requestIndex,profileImage_2,status_2,username_2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void next(){
        if(requestIndex != (outputRequests.size() - 1)) {
            profileImage_2.setImage(null);
            status_2.setImage(null);
            requestIndex++;
            initialize_2();
        }
    }

    public void previous(){
        if(requestIndex != 0) {
            profileImage_2.setImage(null);
            status_2.setImage(null);
            requestIndex--;
            initialize_2();
        }
    }

    public void cancelRequest(){
        try {
            out.write(8);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(outputRequests.get(requestIndex));
            statusText_2.setText((String) inputStream.readObject());
            profileImage_2.setImage(null);
            status_2.setImage(null);
            username_2.setText("");
            initialize_2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Tab 4 :

    @FXML
    Text text_3;
    @FXML
    TextField targetUsername;

    public void block(){
        if(targetUsername.getText().isEmpty()){
            text_3.setText("Enter your target username");
            return;
        }
        if(targetUsername.getText().equals(user.getUsername())){
            text_3.setText("You can't block yourself :|");
            return;
        }
        if(user.checkIsBlock(targetUsername.getText())){
            text_3.setText("You blocked this user before");
            return;
        }
        try {
            out.write(9);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(targetUsername.getText());
            String status = (String) inputStream.readObject();
            if(status.equals("OK")){
                status = (String) inputStream.readObject();
                text_3.setText(status);
            }
            else
                text_3.setText("Invalid username");
        } catch (Exception e) {
            e.printStackTrace();
        }
        targetUsername.setText("");
    }

    private void loadInformation(ArrayList<String> usernames,int index,ImageView profileImage,ImageView status,Text username){
        try {
            System.out.println("aa");
            out.write(7);
            System.out.println("bb");
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            System.out.println("cc");
            ObjectInputStream inputStream = new ObjectInputStream(in);
            System.out.println("dd");
            outputStream.writeObject(usernames.get(index));
            outputStream.writeObject(user);
            System.out.println("ee");
            byte[] senderProfile = (byte[]) inputStream.readObject();
            System.out.println("ff");
            Status userStatus = (Status) inputStream.readObject();
            System.out.println("gg");
            if(senderProfile == null) {
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
            username.setText(usernames.get(index));
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

}