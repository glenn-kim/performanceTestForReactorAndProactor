package org.nhnnext.java.proactor.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

/**
 * Created by infinitu on 2015. 6. 14..
 */
public class SayHelloHandler implements Handler{
    @Override
    public String getName() {
        return "nio SayHelloHandler";
    }

    @Override
    public void completed(Integer result, EventHandler.Event event) {
        try {
            event.getChannel().write(ByteBuffer.wrap("hello NIO Server!".getBytes())).get();
            event.getChannel().close();
        } catch (InterruptedException | IOException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, EventHandler.Event attachment) {

    }

    @Override
    public String toString() {
        return getName();
    }
}
