package org.nhnnext.java.reactor;

import org.nhnnext.java.reactor.handlers.Handler;

/**
 * Created by infinitu on 2015. 6. 13..
 */
public interface MessageDispatcher {
    Handler getHandlerForHeader(String header);
}
