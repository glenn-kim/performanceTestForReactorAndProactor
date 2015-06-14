package org.nhnnext.java.proactor;

import com.typesafe.config.Config;
import org.nhnnext.java.HandlerLoader;
import org.nhnnext.java.proactor.handlers.EventHandler;
import org.nhnnext.java.proactor.handlers.Handler;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.List;

/**
 * Created by infinitu on 2015. 6. 14..
 */
public class Dispatcher extends HandlerLoader<Handler> implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    public static final int HEADER_SIZE = 6;

    public Dispatcher(Config config) {
        List<? extends Config> handlersConfigs = config.getConfigList("handlers");
        handlerMap = new HashMap<>(handlersConfigs.size());
        loadHandlers(handlersConfigs);
    }

    @Override
    public void completed(AsynchronousSocketChannel client, AsynchronousServerSocketChannel listener) {
        listener.accept(listener, this);
        ByteBuffer buff = ByteBuffer.allocate(HEADER_SIZE);
        client.read(buff, buff, new EventHandler(this,client));
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        exc.printStackTrace();
        System.err.println("Error On Dispatcher exit 1");
        System.exit(1);
    }

    public Handler getHandlerForHeader(String header){
        return handlerMap.get(header);
    }

}
