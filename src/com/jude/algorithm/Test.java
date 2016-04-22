package com.jude.algorithm;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mr.Jude on 2016/4/4.
 */
public class Test {
    private static volatile int value = 0;
    private static class LoopVolatile implements Runnable {
        public void run() {
            int count = 0;
            while (count < 1000) {
                value++;
                count++;
            }
        }
    }
    public static void main(String[] args) {
        Thread t1 = new Thread(new LoopVolatile());
        t1.start();
        Thread t2 = new Thread(new LoopVolatile());
        t2.start();
        while (t1.isAlive() || t2.isAlive()) {
        }
        System.out.println("final val is: " + value);
        ReentrantLock

    }
}
