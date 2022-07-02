package com.example.discordfx.Lateral.Exceptions.PasswordExceptions;

public class ThirdPasswordException extends PasswordException{

    @Override
    public String getErrorMessage() {
        return "Password should has small alphabet";
    }

}
