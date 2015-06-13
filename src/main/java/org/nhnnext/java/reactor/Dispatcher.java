package org.nhnnext.java.reactor;

import com.typesafe.config.Config;
import org.nhnnext.java.reactor.handlers.Handler;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

/**
 * Created by infinitu on 2015. 6. 13..
 */
public class Dispatcher implements MessageDispatcher {

    List<? extends Config> handlersConfigs;
    private final WorkerPool workerPool;
    HashMap<String,Handler> handlerMap;

    public Dispatcher(Config config) {
        workerPool = new WorkerPool(config.getInt("pool-size"));
        handlersConfigs = config.getConfigList("handlers");
        handlerMap = new HashMap<>(handlersConfigs.size());
        loadHandlers();
    }

    public void newConnection(Socket socket){
        workerPool.getWorker().assignNewJob(socket,this);
    }

    @Override
    public Handler getHandlerForHeader(String header) {
        return handlerMap.get(header);
    }

    private void loadHandlers() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        for(Config conf : handlersConfigs) {
            String header = conf.getString("header");
            String handlerPath = conf.getString("path");
            try {
                Handler handler = (Handler) loader.loadClass(handlerPath).getConstructor().newInstance();
                registerHandler(header,handler);
            } catch (ClassNotFoundException e) {
                System.err.printf("%s not found for header %s\n", handlerPath, header);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.err.printf("%s found but can not be created", handlerPath);
            } catch (ClassCastException e) {
                System.err.printf("%s found but it is not implementation of Handler", handlerPath);
            }
        }
    }

    public void registerHandler(String header, Handler handler){
        handlerMap.put(header,handler);
        System.out.printf("%s handler has been registered\n", handler.getName());
    }
}
