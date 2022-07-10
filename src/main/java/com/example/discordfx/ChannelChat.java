package com.example.discordfx;

import com.example.discordfx.Client.RoomHandler.Reciever.ChannelChatReciever;
import com.example.discordfx.Client.RoomHandler.Sender.ChannelChatSender;
import com.example.discordfx.Client.RoomHandler.VoiceManagement.Recorder;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChannelChat implements Initializable {

    private Member member;
    private ChannelChatSender sender;
    private Recorder voiceRecorder;
    private final FileChooser fileChooser = new FileChooser();
    {
        fileChooser.setTitle("Discord");
    }

    @FXML
    TextField textField;
    @FXML
    TextField pinMessageNumber;
    @FXML
    Button sendVoice;
    @FXML
    Button cancelVoice;
    @FXML
    TextArea messages;
    @FXML
    Button submitPinMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(Start.socket.getInputStream());
            Integer port = (Integer) inputStream.readObject();
            member = (Member) inputStream.readObject();
            Socket socket = new Socket(Start.hostIp, port);
            sender = new ChannelChatSender(socket, member.getUser().getUsername());
            ChannelChatReciever reciever = new ChannelChatReciever(messages, socket);
            Start.executorService.execute(reciever);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(){
        if(!(sendVoice.isVisible())) {
            sender.sendText(textField.getText());
            textField.clear();
        }
    }

    public void sendFile(ActionEvent event) {
        if (!(sendVoice.isVisible())) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null)
                sender.sendFile(file);
        }
    }

    public void recordeVoice() {
        if (!(sendVoice.isVisible())) {
            sendVoice.setVisible(true);
            cancelVoice.setVisible(true);
            File file = new File("ClientFiles/voice.wav");
            Recorder recorder = new Recorder(file);
            Thread recorderThread = new Thread(recorder);
            voiceRecorder = recorder;
            recorderThread.start();
        }
    }

    public void sendVoice(){
        voiceRecorder.stop();
        sender.sendVoice();
        sendVoice.setVisible(false);
        cancelVoice.setVisible(false);
    }

    public void cancelVoice(){
        voiceRecorder.stop();
        sendVoice.setVisible(false);
        cancelVoice.setVisible(false);
    }

    public void pinMessage(){
        if(member.canPinMessage()) {
            pinMessageNumber.setVisible(true);
            submitPinMessage.setVisible(true);
        }
    }

    public void submitPinMessage() {
        try {
            sender.pinMessage(Integer.parseInt(pinMessageNumber.getText()));
            System.out.println("SSSSSSSSSSSSSSSS");
            pinMessageNumber.setVisible(false);
            submitPinMessage.setVisible(false);
        }catch (Exception e){
            System.out.println("Invalid format");
        }
    }

    public void getPinnedMessage(){
        sender.getPinnedMessage();
    }

    public void exit(ActionEvent event){
        sender.exitChat();
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
