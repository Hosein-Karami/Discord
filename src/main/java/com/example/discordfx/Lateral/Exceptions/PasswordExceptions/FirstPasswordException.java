package com.example.discordfx.Lateral.Exceptions.PasswordExceptions;

public class FirstPasswordException extends PasswordException{

    @Override
    public String getErrorMessage(){
        return "Password should has at least 8 character";
    }

}
