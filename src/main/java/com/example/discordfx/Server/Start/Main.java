/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Start;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args){
        Server server = new Server();
        server.start();
    }

}
