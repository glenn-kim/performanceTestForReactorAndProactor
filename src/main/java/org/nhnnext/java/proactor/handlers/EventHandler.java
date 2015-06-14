package org.nhnnext.java.proactor.handlers;

import org.nhnnext.java.proactor.Dispatcher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by infinitu on 2015. 6. 14..
 */
public class EventHandler implements CompletionHandler<Integer, ByteBuffer> {

    static class Event{

        private AsynchronousSocketChannel channel;
        private ByteBuffer buffer;

        public Event(AsynchronousSocketChannel channel, ByteBuffer buffer) {
            this.channel = channel;
            this.buffer = buffer;
        }

        public AsynchronousSocketChannel getChannel() {
            return channel;
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }
    }

    final static int BUFF_SIZE = 1024;

    private byte[] headerBuff = new byte[Dispatcher.HEADER_SIZE];
    private AsynchronousSocketChannel channel;
    private Dispatcher dispatcher;

    public EventHandler(Dispatcher dispatcher, AsynchronousSocketChannel result) {
        this.dispatcher = dispatcher;
        this.channel = result;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        Handler handler = null;
        if(result >= -1) {
            String header = parseHeader(buffer);
            handler = dispatcher.getHandlerForHeader(header);
        }

        if(handler == null)
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        else{
            ByteBuffer newbuff = ByteBuffer.allocate(BUFF_SIZE);
            channel.read(newbuff,new Event(channel,newbuff),handler);
        }

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        exc.printStackTrace();
    }

    private String parseHeader(ByteBuffer b){
        if(b.position()<Dispatcher.HEADER_SIZE){
            return null; //ignore
        }
        b.flip();
        b.get(headerBuff);
        return new String(headerBuff);
    }
}
