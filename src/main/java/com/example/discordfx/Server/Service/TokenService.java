/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

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
