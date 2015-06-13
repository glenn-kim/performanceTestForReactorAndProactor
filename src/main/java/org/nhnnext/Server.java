package org.nhnnext;

import java.io.IOException;

/**
 * Created by infinitu on 2015. 6. 13..
 */
public abstract class Server extends Thread{
    public abstract void start(int port);
}
