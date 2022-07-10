/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Lateral.Exceptions;

public class UsernameException extends Exception{

    public String getErrorMessage(){
        return "Username should bigger than 5 alphabet";
    }

}
