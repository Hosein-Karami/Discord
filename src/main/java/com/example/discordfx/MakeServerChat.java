/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;

public class MakeServerChat {

    @FXML
    TextField serverName;
    @FXML
    Text resultText;
    @FXML
    ImageView serverProfile;

    private byte[] profileBytes = null;
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

    /**
     * Is used to make a server chat
     */
    public void makeServer(){
        if(serverName.getText().isEmpty()){
            resultText.setText("Enter name of server");
            return;
        }
        try {
            out.write(17);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(serverName.getText());
            String status = (String) inputStream.readObject();
            if(status.equals("OK")){
                outputStream.writeObject(profileBytes);
                resultText.setText((String) inputStream.readObject());
            }
            else
                resultText.setText("This name is used before");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to set image of server chat
     * @param event .
     */
    public void setPicture(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        File imageFile = fileChooser.showOpenDialog(stage);
        if(imageFile != null){
            String imageName = imageFile.getName();
            if(imageName.contains(".jpg") || imageName.contains(".png") || imageName.contains(".jpeg")) {
                try {
                    profileBytes = Files.readAllBytes(imageFile.toPath());
                    serverProfile.setImage(new Image(imageFile.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                resultText.setText("Invalid picture format,format should be jpg,png,jpeg");
        }
    }

    /**
     * Is used to back
     * @param event .
     */
    public void backToMenu(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowServerChats.fxml"));
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
