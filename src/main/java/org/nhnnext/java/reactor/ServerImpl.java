package org.nhnnext.java.reactor;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.nhnnext.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by infinitu on 2015. 6. 13..
 */
public class ServerImpl extends Server{
    Config config;
    private int port;
    public Dispatcher dispatcher;

    public ServerImpl() {
        config = ConfigFactory.load().getConfig("java.reactor");
        dispatcher = new Dispatcher(config);
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);

            //noinspection InfiniteLoopStatement
            while(true){
                Socket socket = serverSocket.accept();
                dispatcher.newConnection(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(int port){
        this.port = port;
        this.start();
    }



}
