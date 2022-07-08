package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.DiscordServer.Invitation;
import com.example.discordfx.Moduls.Dto.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;

public class ShowServerChats {

    private User user;

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
    @FXML
    Button joinButton;

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

    public void initialize() {
        servers = new ArrayList<>();
        try {
            out.write(23);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            String serverName;
            while (true){
                serverName = (String) inputStream.readObject();
                if(serverName == null)
                    break;
                servers.add(serverName);
            }
            if(servers.size() == 0){
                text.setText("You aren't in any discord server");
                nextButton.setVisible(false);
                previousButton.setVisible(false);
                joinButton.setVisible(false);
            }
            else {
                serverIndex = 0;
                inviteIndex = 0;
                loadInformation();
            }
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
                file.delete();
                serverChatName.setText(servers.get(serverIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void next(){
        if(serverIndex != (servers.size() - 1)) {
            serverProfileImage.setImage(null);
            serverIndex++;
            loadInformation();
        }
    }

    public void previous(){
        if(serverIndex != 0) {
            serverProfileImage.setImage(null);
            serverIndex--;
            loadInformation();
        }
    }

    public void connect(ActionEvent event){
        DiscordServer.DserverName = servers.get(serverIndex);
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

    public void makeDserver(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MakeServerChat.fxml"));
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

    //Invitation handle

    private ArrayList<Invitation> invitations;
    private int inviteIndex;

    @FXML
    ImageView serverProfileImage_2;
    @FXML
    Text serverChatName_2;
    @FXML
    Text text_2;
    @FXML
    Button nextButton_2;
    @FXML
    Button previousButton_2;
    @FXML
    Button acceptButton;
    @FXML
    Button rejectButton;

    public void initialize_2(){
        invitations = user.getInvitations();
        inviteIndex = 0;
        serverIndex = 0;

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
