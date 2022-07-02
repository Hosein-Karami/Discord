package com.example.discordfx.Lateral.Exceptions;

public class EmailException extends Exception{

    public String getErrorMessage(){
        return "Invalid email";
    }

}
