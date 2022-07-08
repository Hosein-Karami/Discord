package com.example.discordfx;

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

    static String DserverName;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            out.write(21);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(DserverName);
            byte[] serverImageBytes = (byte[]) inputStream.readObject();
            setServerImageProfile(serverImageBytes);
            name.setText(DserverName);
            out.write(22);
            outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(DserverName);
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
            System.out.println("4");
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
