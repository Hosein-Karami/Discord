package com.example.discordfx.Lateral;

import com.example.discordfx.Lateral.Exceptions.EmailException;
import com.example.discordfx.Lateral.Exceptions.PasswordExceptions.FirstPasswordException;
import com.example.discordfx.Lateral.Exceptions.PasswordExceptions.FourthPasswordException;
import com.example.discordfx.Lateral.Exceptions.PasswordExceptions.SecondPasswordException;
import com.example.discordfx.Lateral.Exceptions.PasswordExceptions.ThirdPasswordException;
import com.example.discordfx.Lateral.Exceptions.PhoneException;

import java.util.regex.Pattern;

public class Checker {

    public static void checkPassword(String password) throws FirstPasswordException, SecondPasswordException, ThirdPasswordException, FourthPasswordException {
        if(password.length() < 8)
            throw new FirstPasswordException();
        if ( ! Pattern.compile("[0-9]").matcher(password).find())
            throw new SecondPasswordException();
        if ( ! Pattern.compile("[a-z]").matcher(password).find())
            throw new ThirdPasswordException();
        if ( ! Pattern.compile("[A-Z]").matcher(password).find())
            throw new FourthPasswordException();
    }

    public static void checkEmail(String email) throws EmailException {
        if(!(Pattern.compile("^(.+)@(.+)$").matcher(email).find()))
            throw new EmailException();
    }

    public static void checkPhone(String phone) throws PhoneException {
        if(Pattern.compile("[A-Z]").matcher(phone).find() || Pattern.compile("[a-z]").matcher(phone).find())
            throw new PhoneException();
        if(phone.length() != 11)
            throw new PhoneException();
    }

}
