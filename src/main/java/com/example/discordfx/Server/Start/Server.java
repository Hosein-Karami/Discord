/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Server.Start;

import com.example.discordfx.Log.ServerLog;
import com.example.discordfx.Moduls.Dto.DiscordServer.Dserver;
import com.example.discordfx.Server.Management.ClientManagement;
import com.example.discordfx.Server.Service.AccountsService;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static int lastUsedPort = 2000;
    public static AccountsService accountsService = new AccountsService();
    static ArrayList<Socket> clients = new ArrayList<>();
    public static final ArrayList<Dserver> discordServers = new ArrayList<>();
    static ExecutorService executorService = Executors.newCachedThreadPool();
    private final ServerLog log = new ServerLog();

    void start(){
        int port = 2000;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            synchronized (this){
                while (true){
                    Socket newClient = serverSocket.accept();
                    log.clientConnectSuccessfully(newClient.getInetAddress());
                    runClientThread(newClient);
                }
            }
        } catch (Exception e) {
            log.openPortError(port);
            e.printStackTrace();
        }
    }

    void runClientThread(Socket newClient){
        clients.add(newClient);
        ClientManagement clientManagement = new ClientManagement(newClient);
        executorService.execute(clientManagement);
    }

}
