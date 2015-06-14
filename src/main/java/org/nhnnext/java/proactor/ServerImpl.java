package org.nhnnext.java.proactor;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.nhnnext.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by infinitu on 2015. 6. 14..
 */
public class ServerImpl extends Server{
    Config config;
    private int port;
    private int poolSize;
    private int backlog;
    private ExecutorService executor;

    public ServerImpl() {
        config = ConfigFactory.load("Application.conf").getConfig("java.proactor");
        poolSize = config.getInt("pool-size");
        backlog = config.getInt("backlog");

    }

    @Override
    public void run() {

        try {
            executor = Executors.newFixedThreadPool(poolSize);
            AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
            AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(channelGroup);
            listener.bind(new InetSocketAddress(port), backlog);
            listener.accept(listener, new Dispatcher(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while(true){
                    sleep(10000);
            }
        } catch (InterruptedException e) {
            executor.shutdown();
        }
    }

    @Override
    public void start(int port){
        this.port = port;
        this.start();

    }

}
