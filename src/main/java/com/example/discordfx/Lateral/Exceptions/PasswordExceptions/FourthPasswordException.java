package com.example.discordfx.Lateral.Exceptions.PasswordExceptions;

public class FourthPasswordException extends PasswordException{

    @Override
    public String getErrorMessage(){
        return "Password should has capital alphabet";
    }

}
