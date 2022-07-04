package com.example.discordfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Dashboard {

    private final Socket socket = Start.socket;
    private OutputStream out;
    {
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void profile(ActionEvent event){
        try {
            out.write(1);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
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

    public void downloadFile(ActionEvent event){
        try {
            out.write(2);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FileDownloader.fxml"));
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

    public void setStatus(ActionEvent event){
        try {
            out.write(3);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SetStatus.fxml"));
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

    public void relationship(ActionEvent event){
        try {
            out.write(4);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RelationshipManagement.fxml"));
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
