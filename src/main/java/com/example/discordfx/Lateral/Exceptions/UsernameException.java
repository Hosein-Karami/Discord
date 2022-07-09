package com.example.discordfx.Lateral.Exceptions;

public class UsernameException extends Exception{

    public String getErrorMessage(){
        return "Username should bigger than 5 alphabet";
    }

}
