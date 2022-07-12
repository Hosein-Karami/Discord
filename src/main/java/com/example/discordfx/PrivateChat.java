/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Client.RoomHandler.FileReaderService;
import com.example.discordfx.Client.RoomHandler.Reciever.PrivateChatReciever;
import com.example.discordfx.Client.RoomHandler.Sender.PrivateChatSender;
import com.example.discordfx.Client.RoomHandler.VoiceManagement.Recorder;
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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class PrivateChat implements Initializable {

    private PrivateChatReciever reciever;
    private PrivateChatSender sender;
    private Recorder voiceRecorder;
    private boolean mute;
    private final FileChooser fileChooser = new FileChooser();

    @FXML
    TextField textField;
    @FXML
    Button sendVoice;
    @FXML
    Button cancelVoice;
    @FXML
    Button muteSetButton;
    @FXML
    TextArea messages;

    /**
     * Is used to initialize the fxml page
     * @param url .
     * @param resourceBundle .
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(Start.socket.getInputStream());
            Integer port = (Integer) inputStream.readObject();
            User user = (User) inputStream.readObject();
            Socket socket = new Socket(Start.hostIp, port);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(user.getUsername());
            sender = new PrivateChatSender(socket, user.getUsername(),messages);
            reciever = new PrivateChatReciever(socket, messages);
            Start.executorService.execute(reciever);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMute(){
        if(mute){
            mute = false;
            muteSetButton.setText("Mute");
        }
        else {
            mute = true;
            muteSetButton.setText("Unmute");
        }
        reciever.setMute(mute);
    }

    /**
     * Is used to send message
     */
    public void sendMessage(){
        if(!(sendVoice.isVisible())) {
            sender.sendText(textField.getText());
            playNotificationSound();
            textField.clear();
        }
    }

    /**
     * Is used to send file
     * @param event .
     */
    public void sendFile(ActionEvent event) {
        if (!(sendVoice.isVisible())) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                FileReaderService service = new FileReaderService(file,sender);
                Start.executorService.execute(service);
                playNotificationSound();
            }
        }
    }

    /**
     * Is used to record voice
     */
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

    /**
     * Is used to send voice
     */
    public void sendVoice(){
        voiceRecorder.stop();
        sender.sendVoice();
        playNotificationSound();
        sendVoice.setVisible(false);
        cancelVoice.setVisible(false);
    }

    /**
     * Is used to cancel voice
     */
    public void cancelVoice(){
        voiceRecorder.stop();
        sendVoice.setVisible(false);
        cancelVoice.setVisible(false);
    }

    /**
     * Is used to clear text area
     */
    public void clear(){
        messages.clear();
    }

    /**
     * Is used to left from chat
     * @param event .
     */
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

    private void playNotificationSound() {
        if (!mute) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("ClientFiles/SendNotification.wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
