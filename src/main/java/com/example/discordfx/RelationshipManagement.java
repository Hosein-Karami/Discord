package com.example.discordfx;

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
    }

    @FXML
    ImageView imageView;
    @FXML
    Text senderUsername;
    @FXML
    Text statusText;
    @FXML
    Button acceptButton;
    @FXML
    Button rejectButton;
    @FXML
    Button nextButton;
    @FXML
    Button previousButton;

    private int senderIndex = 0;

    private ArrayList<String> requestsSenders;

    public void initializeSenderIndex() {
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
        statusText.setText("");
        if(senderIndex != (requestsSenders.size() - 1))
            senderIndex++;
        loadInputRequests();
    }

    public void previousRequest(){
        statusText.setText("");
        if(senderIndex != 0)
            senderIndex--;
        loadInputRequests();
    }

    public void loadInputRequests(){
        try {
            requestsSenders = user.getPendings();
            if(requestsSenders.size() == 0){
                statusText.setText("You don't have any input request");
                acceptButton.setVisible(false);
                rejectButton.setVisible(false);
                nextButton.setVisible(false);
                previousButton.setVisible(false);
                imageView.setVisible(false);
                return;
            }
            out.write(7);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(requestsSenders.get(senderIndex));
            byte[] senderProfile = (byte[]) inputStream.readObject();
            File file = new File("ClientFiles/temp.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(senderProfile);
            fileOutputStream.flush();
            fileOutputStream.close();
            Image firstSenderProfileImage = new Image("file:ClientFiles/temp.jpg");
            imageView.setImage(firstSenderProfileImage);
            senderUsername.setText(requestsSenders.get(senderIndex));
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
            user.removePending(requestsSenders.get(senderIndex));
            if (senderIndex != (requestsSenders.size() - 1)) {
                senderIndex++;
                loadInputRequests();
            }
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
            user.removePending(requestsSenders.get(senderIndex));
            if (senderIndex != (requestsSenders.size() - 1)) {
                senderIndex++;
                loadInputRequests();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    ImageView imageView_2;
    @FXML
    Text text_2;
    @FXML
    Button button_2;

    private int requestIndex;

    public void initializeIndex(){
        requestIndex = 0;

    }

}