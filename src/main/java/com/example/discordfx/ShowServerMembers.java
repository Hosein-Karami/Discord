/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx;

import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Moduls.Dto.ServerMembers.Member;
import com.example.discordfx.Moduls.Dto.ServerMembers.Role;
import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShowServerMembers implements Initializable {

    @FXML
    ImageView profileImage;
    @FXML
    ImageView statusImage;
    @FXML
    TextArea roles;
    @FXML
    Text memberUsername;
    @FXML
    Text result;
    @FXML
    Button kickButton;
    @FXML
    Button addRole;
    @FXML
    TextField roleName;
    @FXML
    Button add;

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

    private User user;
    private final ArrayList<Integer> membersId = new ArrayList<>();
    private int memberIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            out.write(2);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ArrayList<Member> members = ((Dserver) inputStream.readObject()).getMembers();
            for(Member x : members)
                membersId.add(x.getUser().getId());
            Boolean canKick = (Boolean) inputStream.readObject();
            Boolean isOwner = (Boolean) inputStream.readObject();
            user = (User) inputStream.readObject();
            if(!canKick)
                kickButton.setVisible(false);
            if(!isOwner)
                addRole.setVisible(false);
            loadInformation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInformation(){
        try {
            out.write(1);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(membersId.get(memberIndex));
            outputStream.writeObject(user);
            byte[] profileImageBytes = (byte[]) inputStream.readObject();
            Status memberStatus = (Status) inputStream.readObject();
            String targetUsername = (String) inputStream.readObject();
            if(profileImageBytes == null){
                profileImage.setImage(null);
                statusImage.setImage(null);
            }
            else {
                File file = new File("ClientFiles/temp.jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(profileImageBytes);
                fileOutputStream.flush();
                fileOutputStream.close();
                Image friendProfile = new Image("file:ClientFiles/temp.jpg");
                profileImage.setImage(friendProfile);
                setProperStatusImage(memberStatus);
            }
            memberUsername.setText(targetUsername);
            setRoles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void next(){
        if(memberIndex != (membersId.size() - 1)) {
            profileImage.setImage(null);
            statusImage.setImage(null);
            memberIndex++;
            loadInformation();
        }
    }

    public void previous(){
        if(memberIndex != 0) {
            profileImage.setImage(null);
            statusImage.setImage(null);
            memberIndex--;
            loadInformation();
        }
    }

    public void kick(){
        try {
            out.write(2);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(membersId.get(memberIndex));
            result.setText((String) inputStream.readObject());
            profileImage.setImage(null);
            statusImage.setImage(null);
            roles.clear();
            memberUsername.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRole(){
        roleName.setVisible(true);
        add.setVisible(true);
    }

    public void add(){
        if(roleName.getText().isEmpty()){
            result.setText("Enter name of role");
            return;
        }
        if(roleName.getText().equals("Owner")){
            result.setText("You can't add this role to other");
            return;
        }
        try {
            out.write(3);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(roleName.getText());
            String status = (String) inputStream.readObject();
            if(status.equals("OK")) {
                outputStream.writeObject(membersId.get(memberIndex));
                result.setText((String) inputStream.readObject());
                roles.appendText(roleName.getText());
            }else
                result.setText("There is no role with this name in server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        roleName.setText("");
        roleName.setVisible(false);
        add.setVisible(false);
    }

    public void backToMenu(ActionEvent event){
        try {
            out.write(4);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DiscordServer.fxml"));
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

    private void setProperStatusImage(Status status){
        Image image = null;
        if(status == Status.Online)
            image = new Image("file:src/main/resources/Status/Online.png");
        else if(status == Status.Offline)
            image = new Image("file:src/main/resources/Status/Offline.png");
        else if(status == Status.Do_Not_Disturb)
            image = new Image("file:src/main/resources/Status/Do_Not_Disturb.png");
        else if(status == Status.Invisible)
            image = new Image("file:src/main/resources/Status/Invisible.png");
        else if(status == Status.Idle)
            image = new Image("file:src/main/resources/Status/Idle.png");
        statusImage.setImage(image);
    }

    private void setRoles(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(membersId.get(memberIndex));
            Member targetMember = (Member) inputStream.readObject();
            StringBuilder str = new StringBuilder();
            for(Role x : targetMember.getRoles())
                str.append(x.getName()).append(",");
            roles.setText(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
