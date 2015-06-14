package org.nhnnext.java.proactor.handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


/**
 * Created by infinitu on 2015. 6. 14..
 */
public interface Handler extends CompletionHandler<Integer, EventHandler.Event> {
    String getName();
}
