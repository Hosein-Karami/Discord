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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;

public class MakeChannelChat {

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
    Text result;
    @FXML
    TextField channelName;

    public void makeChannel(){
        if(channelName.getText().isEmpty()){
            result.setText("Enter channel name");
            return;
        }
        try {
            out.write(1);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(channelName.getText());
            result.setText((String) inputStream.readObject());
        }catch (Exception e){
            e.printStackTrace();
        }
        channelName.clear();
    }

    public void backToMenu(ActionEvent event){
        try {
            out.write(2);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DiscordServer.fxml"));
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

}
