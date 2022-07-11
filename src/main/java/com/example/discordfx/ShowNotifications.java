/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Lateral.Notification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowNotifications implements Initializable {

    @FXML
    TextArea textArea;

    private OutputStream out;
    private InputStream in;
    {
        try {
            in = Start.socket.getInputStream();
            out = Start.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used to initialize the fxml page
     * @param url .
     * @param resourceBundle .
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            out.write(13);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            Notification notification;
            StringBuilder str = new StringBuilder();
            while (true){
                notification = (Notification) inputStream.readObject();
                if(notification == null)
                    break;
                str.append(notification.getNotificationMessage()).append("\n");
            }
            textArea.setText(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Is used when user want to delete notifications
     */
    public void deleteNotifications(){
        try {
            out.write(14);
            textArea.clear();
        } catch (IOException e) {
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
