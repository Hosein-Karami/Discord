/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Client.RoomHandler.Reciever.PrivateChatReciever;
import com.example.discordfx.Client.RoomHandler.Sender.PrivateChatSender;
import com.example.discordfx.Client.RoomHandler.VoiceManagement.Recorder;
import com.example.discordfx.Moduls.Dto.User.User;
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
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class PrivateChat implements Initializable {

    private PrivateChatSender sender;
    private Recorder voiceRecorder;
    private final FileChooser fileChooser = new FileChooser();
    {
        fileChooser.setTitle("Discord");
    }

    @FXML
    TextField textField;
    @FXML
    Button sendVoice;
    @FXML
    Button cancelVoice;
    @FXML
    TextArea messages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(Start.socket.getInputStream());
            Integer port = (Integer) inputStream.readObject();
            User user = (User) inputStream.readObject();
            Socket socket = new Socket(Start.hostIp, port);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(user.getUsername());
            sender = new PrivateChatSender(socket, user.getUsername());
            PrivateChatReciever reciever = new PrivateChatReciever(socket, messages);
            Start.executorService.execute(reciever);
            System.out.println("9");
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

    public void clear(){
        messages.clear();
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
