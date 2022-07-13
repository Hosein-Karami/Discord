/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Client.RoomHandler.FileReaderService;
import com.example.discordfx.Client.RoomHandler.Reciever.ChannelChatReciever;
import com.example.discordfx.Client.RoomHandler.Sender.ChannelChatSender;
import com.example.discordfx.Client.RoomHandler.VoiceManagement.Recorder;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
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

public class ChannelChat implements Initializable {

    private boolean mute;
    private boolean sendTagMessage = false;
    private Member member;
    private ChannelChatReciever reciever;
    private ChannelChatSender sender;
    private Recorder voiceRecorder;
    private final FileChooser fileChooser = new FileChooser();
    {
        fileChooser.setTitle("Discord");
    }

    @FXML
    TextField textField;
    @FXML
    TextField tempTextField;
    @FXML
    Button sendVoice;
    @FXML
    Button cancelVoice;
    @FXML
    TextArea messages;
    @FXML
    Button submitButton;
    @FXML
    Button likeButton;
    @FXML
    Button dislikeButton;
    @FXML
    Button smileButton;
    @FXML
    Button muteSetButton;

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
            member = (Member) inputStream.readObject();
            String memberUsername = (String) inputStream.readObject();
            Socket socket = new Socket(Start.hostIp, port);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(member.getUserId());
            sender = new ChannelChatSender(socket, memberUsername,messages);
            reciever = new ChannelChatReciever(messages, socket);
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
     * Is used to manage sender
     */
    public void pressSend(){
        if(sendTagMessage)
            sendTaggedMessage();
        else
            sendMessage();
    }

    /**
     * Is used to send text message
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
     * Is used to cancel voice message
     */
    public void cancelVoice(){
        voiceRecorder.stop();
        sendVoice.setVisible(false);
        cancelVoice.setVisible(false);
    }

    /**
     * Is used to pin messages
     */
    public void pinMessage(){
        if(member.canPinMessage()) {
            tempTextField.setVisible(true);
            submitButton.setVisible(true);
        }
    }

    /**
     * Is used to submit pinned message
     */
    public void submitPinMessage() {
        try {
            sender.pinMessage(Integer.parseInt(tempTextField.getText()));
            tempTextField.setVisible(false);
            submitButton.setVisible(false);
            tempTextField.clear();
        }catch (Exception e){
            System.out.println("Invalid format");
        }
    }

    /**
     * Is used to get pinned message
     */
    public void getPinnedMessage(){
        sender.getPinnedMessage();
    }

    /**
     * Is used to react messages
     */
    public void react(){
        tempTextField.setVisible(true);
        likeButton.setVisible(true);
        dislikeButton.setVisible(true);
        smileButton.setVisible(true);
    }

    /**
     * Is used to act like messages
     */
    public void like(){
        try {
            sender.react(Integer.parseInt(tempTextField.getText()), "LIKE");
            finishReact();
        }catch (Exception e){
            finishReact();
            System.out.println("Invalid format");
        }
    }

    /**
     * Is used to act dislike messages
     */
    public void dislike() {
        try {
            sender.react(Integer.parseInt(tempTextField.getText()), "DISLIKE");
            finishReact();
        }catch (Exception e){
            finishReact();
            System.out.println("Invalid format");
        }
    }

    /**
     * Is used to act smile messages
     */
    public void smile() {
        try {
            sender.react(Integer.parseInt(tempTextField.getText()), "SMILE");
            finishReact();
        }catch (Exception e){
            finishReact();
            System.out.println("Invalid format");
        }
    }

    /**
     * Is used to send react to server
     */
    public void finishReact(){
        likeButton.setVisible(false);
        dislikeButton.setVisible(false);
        smileButton.setVisible(false);
        tempTextField.clear();
        tempTextField.setVisible(false);
    }

    /**
     * Is used to tag a member
     */
    public void tag(){
        sendTagMessage = true;
        tempTextField.setVisible(true);
    }

    /**
     * Is used to send tagged message
     */
    public void sendTaggedMessage(){
        sender.tagMember(textField.getText(),tempTextField.getText());
        playNotificationSound();
        tempTextField.setVisible(false);
        sendTagMessage = false;
        textField.clear();
        tempTextField.clear();
    }

    /**
     * Is used when user want to clear messages
     */
    public void clearTextBox(){
        messages.clear();
    }

    /**
     * Is used to exit from channel
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
