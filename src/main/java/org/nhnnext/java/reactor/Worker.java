package org.nhnnext.java.reactor;

import org.nhnnext.java.reactor.handlers.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by infinitu on 2015. 6. 13..
 */
public class Worker extends Thread{

    public static final int HEADER_SIZE = 6;

    private final WorkerPool pool;
    private boolean terminate = false;
    private byte[] headerBuffer = new byte[HEADER_SIZE];
    private Socket socket = null;
    private MessageDispatcher dispatcher;
    private final Object locker = new Object();

    public Worker(WorkerPool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        while(!terminate){
            if(this.socket == null){
                try {
                    synchronized (locker) {
                        locker.wait(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    InputStream stream = socket.getInputStream();
                    String header = parseHeader(stream);
                    Handler handler = dispatcher.getHandlerForHeader(header);
                    if(handler != null) {
                        System.out.printf("%s header received, mapping handler is %s\n",header ,handler.getName());
                        handler.execute(stream, socket.getOutputStream());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    jobFinished();
                }

            }
        }
    }

    public void assignNewJob(Socket socket, MessageDispatcher dispatcher){
        this.socket = socket;
        this.dispatcher = dispatcher;

        synchronized (locker) {
            locker.notify();
        }
    }

    public void stopGently(){
        this.terminate = true;
    }

    private void jobFinished(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket = null;
        try {
            pool.returnWorker(this);
        } catch (Exception e) {
            e.printStackTrace();
            stopGently();
        }
    }

    private String parseHeader(InputStream stream) throws IOException {
        stream.read(headerBuffer); //ignore buffering problem
        return new String(headerBuffer);
    }
}
