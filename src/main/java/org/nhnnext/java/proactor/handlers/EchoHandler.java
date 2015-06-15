package org.nhnnext.java.proactor.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by infinitu on 2015. 6. 14..
 */
public class EchoHandler implements Handler{
    Future<Integer> writing = null;
    final Object locker = new Object();
    @Override
    public String getName() {
        return "nio EchoHandler";
    }

    @Override
    public void completed(Integer result, EventHandler.Event event) {
        ByteBuffer recieved = (ByteBuffer) event.getBuffer().duplicate().flip();


        if(result < 0){
            try {
                event.getChannel().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

//        synchronized (locker) {
            try {
                while(recieved.position() != recieved.limit()) {
                    event.getChannel().write(recieved).get();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        event.getBuffer().clear();
        event.getChannel().read(event.getBuffer(), event, this);
//        }

    }

    @Override
    public void failed(Throwable exc, EventHandler.Event event) {

    }
    @Override
    public String toString() {
        return getName();
    }
}
