package org.nhnnext.java.reactor;

import com.typesafe.config.Config;
import org.nhnnext.java.HandlerLoader;
import org.nhnnext.java.reactor.handlers.Handler;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

/**
 * Created by infinitu on 2015. 6. 13..
 */
public class Dispatcher extends HandlerLoader<Handler> implements MessageDispatcher{

    List<? extends Config> handlersConfigs;
    private final WorkerPool workerPool;

    public Dispatcher(Config config) {
        workerPool = new WorkerPool(config.getInt("pool-size"));
        handlersConfigs = config.getConfigList("handlers");
        handlerMap = new HashMap<>(handlersConfigs.size());
        loadHandlers(handlersConfigs);
    }

    public void newConnection(Socket socket){
        workerPool.getWorker().assignNewJob(socket,this);
    }

    @Override
    public Handler getHandlerForHeader(String header) {
        return handlerMap.get(header);
    }


}
