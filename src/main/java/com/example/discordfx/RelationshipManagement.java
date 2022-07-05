package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RelationshipManagement implements Initializable {

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRequest() {
        if (requestUsername.getText().isEmpty()) {
            requestText.setText("Enter target username first");
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
            out.write(5);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(requestUsername.getText());
            String status = (String) inputStream.readObject();
            if (status.equals("OK")) {
                status = (String) inputStream.readObject();
                if (status.equals("OK")) {
                    status = (String) inputStream.readObject();
                    if (status.equals("OK")) {
                        requestText.setText((String) inputStream.readObject());
                    } else
                        requestText.setText(status);
                } else
                    requestText.setText(status);
            } else
                requestText.setText(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestUsername.setText("");
    }

    //Tab 2 :

    @FXML
    ImageView imageView;
    @FXML
    ImageView status;
    @FXML
    Text text_1;
    @FXML
    Text statusText;
    @FXML
    Button acceptButton;
    @FXML
    Button rejectButton;
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
            senderIndex = 0;
            loadInputRequests();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void nextRequest(){
        if(senderIndex != (requestsSenders.size() - 1)) {
            statusText.setText("");
            imageView.setImage(null);
            status.setImage(null);
            senderIndex++;
            loadInputRequests();
        }
    }

    public void previousRequest(){
        if(senderIndex != 0) {
            statusText.setText("");
            imageView.setImage(null);
            status.setImage(null);
            senderIndex--;
            loadInputRequests();
        }
    }

    public void loadInputRequests(){
        try {
            requestsSenders = user.getPendings();
            if(requestsSenders.size() == 0){
                statusText.setText("You don't have any input request");
                acceptButton.setVisible(false);
                rejectButton.setVisible(false);
                nextButton_1.setVisible(false);
                status_2.setImage(null);
                imageView.setImage(null);
                text_1.setVisible(false);
                previousButton_1.setVisible(false);
                return;
            }
            out.write(7);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(requestsSenders.get(senderIndex));
            byte[] senderProfile = (byte[]) inputStream.readObject();
            Status userStatus = (Status) inputStream.readObject();
            File file = new File("ClientFiles/temp.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(senderProfile);
            fileOutputStream.flush();
            fileOutputStream.close();
            setProperStatusImage(userStatus,status);
            Image firstSenderProfileImage = new Image("file:ClientFiles/temp.jpg");
            imageView.setImage(firstSenderProfileImage);
            text_1.setText(requestsSenders.get(senderIndex));
            file.delete();
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
            statusText.setText((String) inputStream.readObject());
            requestsSenders.remove(senderIndex);
            imageView.setImage(null);
            status.setImage(null);
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
            statusText.setText((String) inputStream.readObject());
            requestsSenders.remove(senderIndex);
            imageView.setImage(null);
            status.setImage(null);
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Tab 3 :

    @FXML
    ImageView imageView_2;
    @FXML
    ImageView status_2;
    @FXML
    Text text_2;
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
            requestIndex = 0;
            loadOutputRequests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadOutputRequests(){
        try {
            if(outputRequests.size() == 0){
                statusText_2.setText("You don't have any output request");
                imageView_2.setVisible(false);
                cancelButton.setVisible(false);
                nextButton_2.setVisible(false);
                previousButton_2.setVisible(false);
                return;
            }
            out.write(7);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(outputRequests.get(senderIndex));
            byte[] recieverProfileBytes = (byte[]) inputStream.readObject();
            Status userStatus = (Status) inputStream.readObject();
            File file = new File("ClientFiles/temp.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(recieverProfileBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            Image recieverProfileImage = new Image("file:ClientFiles/temp.jpg");
            imageView_2.setImage(recieverProfileImage);
            setProperStatusImage(userStatus,status_2);
            text_2.setText(outputRequests.get(requestIndex));
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void next(){
        if(requestIndex != (outputRequests.size() - 1)) {
            text_2.setText("");
            imageView_2.setImage(null);
            status_2.setImage(null);
            requestIndex++;
            loadOutputRequests();
        }
    }

    public void previous(){
        if(requestIndex != 0) {
            text_2.setText("");
            imageView_2.setImage(null);
            status_2.setImage(null);
            requestIndex--;
            loadOutputRequests();
        }
    }

    public void cancelRequest(){
        try {
            out.write(8);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(outputRequests.get(requestIndex));
            statusText_2.setText((String) inputStream.readObject());
            imageView_2.setImage(null);
            status_2.setImage(null);
            text_2.setText("");
            initialize_2();
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
        try {
            out.write(9);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(targetUsername.getText());
            String status = (String) inputStream.readObject();
            if(status.equals("OK")){
                status = (String) inputStream.readObject();
                if(status.equals("OK")){
                    status = (String) inputStream.readObject();
                    text_3.setText(status);
                }
                else
                    text_3.setText("You blocked this user before");
            }
            else
                text_3.setText("Invalid username");
        } catch (Exception e) {
            e.printStackTrace();
        }
        targetUsername.setText("");
    }

    //Tab 5 :

    @FXML
    TextField targetUsername_2;
    @FXML
    Text text_4;

    public void unblock() {
        if (targetUsername_2.getText().isEmpty()) {
            text_4.setText("Enter target username");
            return;
        }
        if (targetUsername_2.getText().equals(user.getUsername())) {
            text_4.setText("You can't unblock yourself");
            return;
        }
        try {
            out.write(10);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(targetUsername_2.getText());
            String status = (String) inputStream.readObject();
            if(status.equals("OK")){
                status = (String) inputStream.readObject();
                if(status.equals("OK")){
                    status = (String) inputStream.readObject();
                    text_4.setText(status);
                }
                else
                    text_4.setText("You didn't block this user");
            }
            else
                text_4.setText("Invalid username");
        }catch (Exception e){
            e.printStackTrace();
        }
        targetUsername_2.setText("");
    }

}