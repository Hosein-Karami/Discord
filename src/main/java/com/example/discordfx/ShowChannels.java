package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.DiscordServer.Channel;
import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShowChannels implements Initializable {

    private ArrayList<Channel> channels;
    private int channelIndex;
    private Member member;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            member = (Member) inputStream.readObject();
            channels = ((Dserver) inputStream.readObject()).getChannels();
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

    }

    public void deleteChannel(){

    }

    public void limitMembers(){

    }

}
