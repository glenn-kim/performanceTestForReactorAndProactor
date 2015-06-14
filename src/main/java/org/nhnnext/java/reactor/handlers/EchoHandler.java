package org.nhnnext.java.reactor.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by infinitu on 2015. 6. 13..
 */
public class EchoHandler implements Handler{
    public final static int BUFF_SIZE = 1024;
    @Override
    public String getName() {
        return "Echo Handler";
    }

    @Override
    public void execute(InputStream in, OutputStream out) {
        byte[] buff = new byte[BUFF_SIZE];
        try {
            while(in.available()>0){
                int size = in.read(buff);
                out.write(buff,0,size);
            }
            out.flush();
            System.out.println("flush");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return getName();
    }
}
