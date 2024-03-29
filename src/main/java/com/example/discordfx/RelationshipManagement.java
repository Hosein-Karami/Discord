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

    /**
     * Is used to send friendship request to a particular user
     */
    public void sendRequest() {
        if (requestUsername.getText().isEmpty()) {
            requestText.setText("Enter target username");
            return;
        }
        try {
            if (user.getUsername().equals(requestUsername.getText())) {
                requestText.setText("You can't send request to yourself");
                return;
            }
            out.write(5);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(requestUsername.getText());
            String Status = (String) inputStream.readObject();
            if (Status.equals("OK")) {
                Status = (String) inputStream.readObject();
                if (Status.equals("OK")) {
                    Status = (String) inputStream.readObject();
                    if (Status.equals("OK")) {
                        Status = (String) inputStream.readObject();
                        if (Status.equals("OK")) {
                            requestText.setStyle("-fx-fill: #00f626");
                            Status = (String) inputStream.readObject();
                            requestText.setText(Status + " to " + requestUsername.getText());
                            out.write(20);
                            inputStream = new ObjectInputStream(in);
                            user = (User) inputStream.readObject();
                        }
                    } else requestText.setText(Status);
                } else requestText.setText(Status);
            } else requestText.setText(Status);
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
    private ArrayList<Integer> requestsSenders;

    /**
     * Is used to initialize pending page
     */
    public void initialize() {
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
            requestsSenders = user.getInformation().getPendingId();
            loadInputRequests();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used to load next pending user
     */
    public void nextRequest(){
        if(senderIndex != (requestsSenders.size() - 1)) {
            profileImage_1.setImage(null);
            status_1.setImage(null);
            senderIndex++;
            initialize();
        }
    }

    /**
     * Is used to load previous pending user
     */
    public void previousRequest(){
        if(senderIndex != 0) {
            profileImage_1.setImage(null);
            status_1.setImage(null);
            senderIndex--;
            initialize();
        }
    }

    /**
     * Is used to load requests info
     */
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

    /**
     * Is used to accept request
     */
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
            if(senderIndex == requestsSenders.size())
                senderIndex--;
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to reject request
     */
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
            if(senderIndex == requestsSenders.size())
                senderIndex--;
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
    private ArrayList<Integer> outputRequests;

    /**
     * Is used to initialize output requests page
     */
    public void initialize_2(){
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
            outputRequests = user.getInformation().getOutputRequestsId();
            loadOutputRequests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to load requests info
     */
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

    /**
     * Is used to load next
     */
    public void next(){
        if(requestIndex != (outputRequests.size() - 1)) {
            profileImage_2.setImage(null);
            status_2.setImage(null);
            requestIndex++;
            initialize_2();
        }
    }

    /**
     * Is used to load previous
     */
    public void previous(){
        if(requestIndex != 0) {
            profileImage_2.setImage(null);
            status_2.setImage(null);
            requestIndex--;
            initialize_2();
        }
    }

    /**
     * Is used to cancel request
     */
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
            if(requestIndex == outputRequests.size())
                requestIndex--;
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

    /**
     * Is used to block a particular user
     */
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
                if(status.equals("OK"))
                    text_3.setText((String) inputStream.readObject());
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

    /**
     * Is used to load information of users
     * @param Id : target user's id
     * @param index : index in arraylist
     * @param profileImage : target user's profile image
     * @param status : status of target user
     * @param username : username of target user
     */
    private void loadInformation(ArrayList<Integer> Id,int index,ImageView profileImage,ImageView status,Text username){
        try {
            out.write(7);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(Id.get(index));
            outputStream.writeObject(user);
            byte[] senderProfile = (byte[]) inputStream.readObject();
            Status userStatus = (Status) inputStream.readObject();
            String targetUsername = (String) inputStream.readObject();
            username.setText(targetUsername);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to set image of target user's status
     * @param status : status of target user
     * @param imageView : imageview for show status image
     */
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

    /**
     * Is used to back
     * @param event .
     */
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