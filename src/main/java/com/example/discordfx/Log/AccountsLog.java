/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Log;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountsLog {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");//format of Localdate variable.

    public void changePictureSuccessfully(int userId){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] ClientService\nUser with id="+userId+
                    " changed profile picture successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePictureError(int userId){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Error] ClientService\nError happening when User with id="+userId+
                    " want to change picture of his/her profile.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeUsername(String olderUsername,String newUsername){
        try (FileWriter fileWriter = new FileWriter("Log.txt",true)){
            fileWriter.write(LocalDateTime.now().format(formatter) + " [Info] ClientService\nUser with username="+olderUsername+
                    " changed username to "+newUsername+" successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
