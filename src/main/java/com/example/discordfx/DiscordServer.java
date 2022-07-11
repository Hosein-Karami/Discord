/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.DiscordServer.MusicReceiver;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class DiscordServer implements Initializable {

    static int DserverId;
    private int musicPort;
    private MusicReceiver receiver;
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
    ImageView serverImageProfile;
    @FXML
    Text name;
    @FXML
    Text result;

    /**
     * Is used to initialize the fxml page
     * @param url .
     * @param resourceBundle .
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            out.write(21);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(DserverId);
            String serverName = (String) inputStream.readObject();
            byte[] serverImageBytes = (byte[]) inputStream.readObject();
            setServerImageProfile(serverImageBytes);
            name.setText(serverName);
            out.write(22);
            outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(DserverId);
            inputStream = new ObjectInputStream(in);
            member = (Member) inputStream.readObject();
            musicPort = (Integer) inputStream.readObject();
            connectToMusicSender();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to add member to server
     * @param event .
     */
    public void addMember(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddServerMember.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to show members of server
     * @param event .
     */
    public void showMembers(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowServerMembers.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to change name of server
     * @param event .
     */
    public void changeName(ActionEvent event){
        if(member.canChangeServerName()){
            try {
                out.write(3);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangeServerName.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
            result.setText("Permission denied");
    }

    /**
     * Is used to make a role in server
     * @param event .
     */
    public void makeRole(ActionEvent event){
        if(member.isOwner()){
            try{
                out.write(4);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MakeRole.fxml"));
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
        else
            result.setText("Permission denied");
    }

    /**
     * Is used to make a channel in server
     * @param event .
     */
    public void makeChannel(ActionEvent event){
        if(member.canMakeChannel()){
            try {
                out.write(5);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MakeChannelChat.fxml"));
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
        else
            result.setText("Permission denied");
    }

    /**
     * Is used to go channel section
     * @param event .
     */
    public void channels(ActionEvent event){
        try {
            out.write(6);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageChannels.fxml"));
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

    /**
     * Is used to send music on server
     * @param event .
     */
    public void sendMusicOnServer(ActionEvent event){
        try {
            FileChooser fileChooser = new FileChooser();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            File targetFile = fileChooser.showOpenDialog(stage);
            if((targetFile.getName().contains(".wav")) || (targetFile.getName().contains(".mp3"))){
                out.write(7);
                ObjectOutputStream outputStream = new ObjectOutputStream(out);
                ObjectInputStream inputStream = new ObjectInputStream(in);
                byte[] musicBytes = Files.readAllBytes(targetFile.toPath());
                outputStream.writeObject(musicBytes);
                result.setText((String) inputStream.readObject());
            }else
                result.setText("Invalid format,Format of selected file should be wav or mp3");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used to delete server
     * @param event .
     */
    public void deleteServer(ActionEvent event){
        if(member.isOwner()) {
            try {
                out.write(8);
                ObjectInputStream inputStream = new ObjectInputStream(in);
                String status = (String) inputStream.readObject();
                if (status.equals("ERROR"))
                    result.setText("You can't delete server when some members are in server");
                else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            result.setText("Permission denied");
    }

    /**
     * Is used to stop music
     */
    public void stopMusic(){
        receiver.stop();
    }

    /**
     * Is used to resume music
     */
    public void resumeMusic(){
        receiver.resume();
    }

    /**
     * Is used to back menu
     * @param event .
     */
    public void backToMenu(ActionEvent event){
        try {
            out.write(9);
            receiver.stop();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to set image of server
     * @param imageBytes : bytes of image
     */
    private void setServerImageProfile(byte[] imageBytes){
        try {
            File file = new File("ClientFiles/temp.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(imageBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            Image serverImage = new Image("file:ClientFiles/temp.jpg");
            serverImageProfile.setImage(serverImage);
            file.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Is used to connect members to music sender
     */
    private void connectToMusicSender(){
        receiver = new MusicReceiver(musicPort);
        Start.executorService.execute(receiver);
    }

}
