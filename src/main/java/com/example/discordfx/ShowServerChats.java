/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.DiscordServer.Invitation;
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

public class ShowServerChats implements Initializable {

    private User user;
    private ArrayList<Invitation> invitations;

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

    private ArrayList<Integer> servers;
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
            user = (User) inputStream.readObject();
            invitations = user.getInformation().getInvitations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize_1() {
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
            servers = user.getInformation().getDiscordServers();
            if(servers.size() == 0){
                text.setText("You aren't in any discord server");
                nextButton.setVisible(false);
                previousButton.setVisible(false);
                joinButton.setVisible(false);
                serverChatName.setVisible(false);
            }
            else {
                serverIndex = 0;
                loadInformation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInformation(){
        load(servers.get(serverIndex),serverProfileImage,serverChatName);
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
        DiscordServer.DserverId = servers.get(serverIndex);
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

    private int inviteIndex;

    @FXML
    ImageView serverProfileImage_2;
    @FXML
    Text serverChatName_2;
    @FXML
    Text text_2;
    @FXML
    Text invitationMessage;
    @FXML
    Button nextButton_2;
    @FXML
    Button previousButton_2;
    @FXML
    Button acceptButton;
    @FXML
    Button rejectButton;

    public void initialize_2(){
        if(invitations.size() == 0){
            nextButton_2.setVisible(false);
            previousButton_2.setVisible(false);
            acceptButton.setVisible(false);
            rejectButton.setVisible(false);
            text_2.setText("You don't have any invitation");
        }
        else {
            inviteIndex = 0;
            loadInformation_2();
        }
    }

    public void loadInformation_2(){
        Invitation targetInvitation = invitations.get(inviteIndex);
        load(targetInvitation.getServerId(),serverProfileImage_2,serverChatName_2);
        invitationMessage.setText(targetInvitation.getInvitationText());
    }

    public void next_2(){
        if(inviteIndex != (invitations.size() - 1)) {
            serverProfileImage_2.setImage(null);
            inviteIndex++;
            loadInformation_2();
        }
    }

    public void previous_2(){
        if(inviteIndex != 0) {
            serverProfileImage_2.setImage(null);
            inviteIndex++;
            loadInformation_2();
        }
    }

    public void accept(){
        try {
            out.write(24);
            invitationHandle("Accept");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reject(){
        try {
            out.write(24);
            invitationHandle("Reject");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load(int serverId,ImageView profile,Text serverName){
        try {
            out.write(21);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(serverId);
            String name = (String) inputStream.readObject();
            byte[] serverImageBytes = (byte[]) inputStream.readObject();
            if(serverImageBytes == null)
                profile.setImage(null);
            else {
                File file = new File("ClientFiles/temp.jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(serverImageBytes);
                fileOutputStream.flush();
                fileOutputStream.close();
                Image serverImage = new Image("file:ClientFiles/temp.jpg");
                profile.setImage(serverImage);
                file.delete();
                serverName.setText(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invitationHandle(String reaction){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(invitations.get(inviteIndex));
            outputStream.writeObject(reaction);
            text_2.setText((String) inputStream.readObject());
            invitations.remove(inviteIndex);
            if(inviteIndex == invitations.size())
                inviteIndex--;
            serverProfileImage_2.setImage(null);
            invitationMessage.setText("");
            serverChatName_2.setText("");
            initialize_2();
        } catch (Exception e) {
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

}
