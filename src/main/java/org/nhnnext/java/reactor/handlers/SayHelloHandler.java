package org.nhnnext.java.reactor.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by infinitu on 2015. 6. 13..
 */
public class SayHelloHandler implements Handler{
    @Override
    public String getName() {
        return "Say Hello Handler";
    }

    @Override
    public void execute(InputStream in, OutputStream out) {
        try {
            out.write("Hello World!!".getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return getName();
    }
}
