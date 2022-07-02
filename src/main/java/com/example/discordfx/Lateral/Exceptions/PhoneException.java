package com.example.discordfx.Lateral.Exceptions;

public class PhoneException extends Exception{

    public String getErrorMessage(){
        return "Invalid phone number";
    }

}
