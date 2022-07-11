/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MakePrivateChat implements Initializable {

    private User user;
    private InputStream in;
    private OutputStream out;
    {
        try {
            out = Start.socket.getOutputStream();
            in = Start.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    TextField targetUsername;
    @FXML
    Text text;

    /**
     * Is used to initialize the fxml page
     * @param url .
     * @param resourceBundle .
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            out.write(20);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            user = (User) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to make a private chat
     * @param event .
     */
    public void makePrivateChat(ActionEvent event){
        if(targetUsername.getText().isEmpty()){
            text.setText("Enter target username");
            return;
        }
        if(targetUsername.getText().equals(user.getUsername())){
            text.setText("You can't start private chat with yourself");
            return;
        }
        if(user.getInformation().getPrivateChats().get(targetUsername.getText()) != null){
            text.setText("You have private chat with this user");
            return;
        }
        try {
            out.write(15);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(targetUsername.getText());
            String status = (String) inputStream.readObject();
            if(status.equals("OK")){
                status = (String) inputStream.readObject();
                if(status.equals("OK")){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("PrivateChat.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
                else
                    text.setText("This user blocked you :)");
            }
            else
                text.setText("Invalid username");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to back
     * @param event .
     */
    public void backToMenu(ActionEvent event){
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
