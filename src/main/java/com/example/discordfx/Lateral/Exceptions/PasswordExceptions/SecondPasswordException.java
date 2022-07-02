package com.example.discordfx.Lateral.Exceptions.PasswordExceptions;

public class SecondPasswordException extends PasswordException{

    @Override
    public String getErrorMessage(){
        return "Password should has number";
    }

}
