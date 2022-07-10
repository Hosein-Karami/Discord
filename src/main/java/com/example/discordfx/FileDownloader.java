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
import java.net.Socket;

public class FileDownloader {

    @FXML
    TextField fileName;
    @FXML
    Text text;

    private final Socket socket = Start.socket;

    public void download(){
        if(fileName.getText().isEmpty()) {
            text.setText("Enter name of file");
            return;
        }
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("OK");
            outputStream.writeObject(fileName.getText());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            String status = (String) inputStream.readObject();
            if(status.equals("OK")){
                byte[] fileBytes = (byte[]) inputStream.readObject();
                if(fileBytes != null){
                    File file = new File("ClientFiles/"+fileName.getText());
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(fileBytes);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    fileName.setText("");
                    text.setText("You can go to ClientFiles and see downloaded file");
                }
            }
            else
                text.setText(status);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent event){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("Break");
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

