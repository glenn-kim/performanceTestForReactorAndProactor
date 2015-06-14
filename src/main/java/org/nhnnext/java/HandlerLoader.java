package org.nhnnext.java;

import com.typesafe.config.Config;
import org.nhnnext.java.proactor.handlers.Handler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by infinitu on 2015. 6. 14..
 */
public class HandlerLoader<T>{
    protected HashMap<String,T> handlerMap;

    protected HandlerLoader(){}

    protected void loadHandlers(List<? extends Config> handlersConfigs) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        for(Config conf : handlersConfigs) {
            String header = conf.getString("header");
            String handlerPath = conf.getString("path");
            try {
                //noinspection unchecked
                T handler = (T) loader.loadClass(handlerPath).getConstructor().newInstance();
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

    public void registerHandler(String header, T handler){
        handlerMap.put(header,handler);
        System.out.printf("%s handler has been registered\n", handler.toString());
    }
}
