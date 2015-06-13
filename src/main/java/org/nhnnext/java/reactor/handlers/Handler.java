package org.nhnnext.java.reactor.handlers;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by infinitu on 2015. 6. 13..
 */
public interface Handler {
    String getName();
    void execute(InputStream in, OutputStream out);
}
