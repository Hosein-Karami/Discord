/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.ServerMembers.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;

public class MakeRole {

    private InputStream in;
    private OutputStream out;
    {
        try {
            in = Start.socket.getInputStream();
            out = Start.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    TextField roleName;
    @FXML
    Text result;
    @FXML
    RadioButton addChannel;
    @FXML
    RadioButton removeChannel;
    @FXML
    RadioButton removeMember;
    @FXML
    RadioButton limitChannelAccess;
    @FXML
    RadioButton changeServerName;
    @FXML
    RadioButton pinMessages;

    /**
     * Is used to make a role in server
     */
    public void makeRole(){
        if(roleName.getText().isEmpty()){
            result.setText("Enter name of role");
            return;
        }
        if(roleName.getText().equals("Owner")){
            result.setText("You can't make this role");
            return;
        }
        try {
            out.write(1);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(roleName.getText());
            String status = (String) inputStream.readObject();
            if(status.equals("OK")) {
                outputStream.writeObject(getRole());
                result.setText((String) inputStream.readObject());
            }
            else
                result.setText("This name is used before");
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

    /**
     * Is used to make a proper role with user wishes
     * @return : proper role
     */
    private Role getRole(){
        Role role = new Role(roleName.getText());
        if(addChannel.isSelected())
            role.setMakeChannel(true);
        if(removeChannel.isSelected())
            role.setDeleteChannel(true);
        if(removeMember.isSelected())
            role.setRemoveMemberFromServer(true);
        if(limitChannelAccess.isSelected())
            role.setLimitMemberToJoinChannel(true);
        if(changeServerName.isSelected())
            role.setChangeServerName(true);
        if(pinMessages.isSelected())
            role.setPinMessage(true);
        return role;
    }

}
