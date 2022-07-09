package com.example.discordfx;

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
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class DiscordServer implements Initializable {

    static int DserverId;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void backToMenu(ActionEvent event){
        try {
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

}
