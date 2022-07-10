/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Lateral;

import java.io.Serial;
import java.io.Serializable;

public class Notification implements Serializable {

    @Serial
    private static final long serialVersionUID = 254231684205732173L;

    private final String notificationMessage;

    public Notification(String notificationMessage){
        this.notificationMessage = notificationMessage;
    }

    /**
     * @return : message of notification
     */
    public String getNotificationMessage(){
        return notificationMessage;
    }

}
