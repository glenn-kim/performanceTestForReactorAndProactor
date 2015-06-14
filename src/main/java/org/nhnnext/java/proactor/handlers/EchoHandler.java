package org.nhnnext.java.proactor.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by infinitu on 2015. 6. 14..
 */
public class EchoHandler implements Handler{
    @Override
    public String getName() {
        return "nio EchoHandler";
    }

    @Override
    public void completed(Integer result, EventHandler.Event event) {
        if(result <= 0){
            try {
                event.getChannel().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        event.getChannel().write((ByteBuffer) event.getBuffer().duplicate().flip());
        event.getChannel().read(event.getBuffer(),event,this);
    }

    @Override
    public void failed(Throwable exc, EventHandler.Event event) {

    }
    @Override
    public String toString() {
        return getName();
    }
}
