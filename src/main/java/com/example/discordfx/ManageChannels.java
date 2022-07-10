package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.DiscordServer.Channel;
import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageChannels implements Initializable {

    private ArrayList<Channel> channels;
    private int channelIndex;
    private Member member;
    private Dserver dserver;
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
    TextField bannedUsername;
    @FXML
    Text result;
    @FXML
    Text channelName;
    @FXML
    Button next;
    @FXML
    Button previous;
    @FXML
    Button deleteButton;
    @FXML
    Button limitButton;
    @FXML
    Button bannedButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            member = (Member) inputStream.readObject();
            dserver = (Dserver) inputStream.readObject();
            channels = dserver.getChannels();
            if(!(member.canDeleteChannel()))
                deleteButton.setVisible(false);
            if(!(member.canLimitMembersToChannels()))
                limitButton.setVisible(false);
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(){
        channelName.setText(channels.get(channelIndex).getName());
    }

    public void next(){
        if(channelIndex != (channels.size() - 1)) {
            channelIndex++;
            load();
        }
    }

    public void previous(){
        if(channelIndex != 0) {
            channelIndex--;
            load();
        }
    }

    public void join(ActionEvent event){
        if(channels.get(channelIndex).checkIsBanned(member.getUser().getId())){
            result.setText("You are banned to access to this channel");
            return;
        }
        try {
            out.write(1);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(channels.get(channelIndex).getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteChannel(){
        try {
            out.write(2);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(channels.get(channelIndex).getName());
            result.setText((String) inputStream.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limitMember(){
        if(bannedUsername.getText().isEmpty()){
            result.setText("Enter target username");
            return;
        }
        try {
            out.write(3);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(channels.get(channelIndex).getName());
            outputStream.writeObject(bannedUsername.getText());
            result.setText((String) inputStream.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backToMenu(ActionEvent event){
        try {
            out.write(4);
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

}
