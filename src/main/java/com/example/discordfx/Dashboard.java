/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

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

    /**
     * Is used to go user to profile page
     * @param event .
     */
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

    /**
     * Is used when user want to get files from server
     * @param event .
     */
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

    /**
     * Is used when user want to set status for himself/herself
     * @param event .
     */
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

    /**
     * Is used when user want to manage his/her relationship with other users
     * @param event .
     */
    public void relationship(ActionEvent event){
        try {
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

    /**
     * Is used when user want to manage his/her friendship with other users
     * @param event .
     */
    public void friendshipManagement(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FriendsManagement.fxml"));
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
     * Is used when user want to see his/her notifications
     * @param event .
     */
    public void notification(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowNotifications.fxml"));
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
     * Is used when user want to make a private chat with a user
     * @param event .
     */
    public void makePrivateChat(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MakePrivateChat.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectToPrivateChat(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConnectToPrivateChat.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used when user want to manage his/her server chats
     * @param event .
     */
    public void showServers(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowServerChats.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used when user want to logout
     * @param event .
     */
    public void logOut(ActionEvent event){
        try {
            out.write(200);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
