package com.example.discordfx.Server.Service;

import com.example.discordfx.Moduls.Dto.User.User;

public class TokenService {

    private final User user;

    //Constructor :
    public TokenService(User user){
        this.user = user;
    }

    public String getToken(){
        return "sss";
    }

}
