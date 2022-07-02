package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.User.Status;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SetStatus {

    @FXML
    Text text;

    private OutputStream out;
    {
        try {
            out = Start.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Online(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(Status.Online);
            text.setText("Your status changed to Online successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Idle(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(Status.Idle);
            text.setText("Your status changed to Idle successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Do_Not_Disturb(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(Status.Do_Not_Disturb);
            text.setText("Your status changed to Do_Not_Disturb successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Invisible(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(Status.Invisible);
            text.setText("Your status changed to Invisible successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent event){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(null);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
