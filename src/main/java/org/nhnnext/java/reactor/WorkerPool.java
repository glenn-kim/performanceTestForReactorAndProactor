package org.nhnnext.java.reactor;

import java.util.function.Consumer;

/**
 * Created by infinitu on 2015. 6. 13..
 */
public class WorkerPool {
    private final int poolSize;
    private int idleWorker;
    private Worker[] workers;
    private final Object locker;

    public WorkerPool(int poolSize) {
        this.poolSize = poolSize;
        idleWorker = poolSize;
        workers = new Worker[poolSize];
        locker = new Object();
        for(int i = 0 ; i < poolSize ; i++) {
            workers[i]=new Worker(this);
            workers[i].start();
        }
    }

    public Worker getWorker() {
        while (true) {
            synchronized (locker) {
                if (idleWorker > 0) {
                    idleWorker--;
                    return workers[idleWorker];
                }

                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void returnWorker(Worker worker) throws Exception {
        synchronized (locker) {
            if(idleWorker >= poolSize){
                throw new Exception("pool Size Overflow");
            }
            workers[idleWorker] = worker;
            idleWorker++;
            locker.notify();
        }
    }
}
